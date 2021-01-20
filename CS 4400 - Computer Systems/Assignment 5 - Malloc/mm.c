/*
 * Brandon Walters, u1199080
 *
 * This first-fit malloc implementation makes use of the recommended strategies for improving utilization and throughput,
 * namely an explicit free list that keeps track of all free blocks in memory, a system of coalescing free blocks 
 * to reduce overall fragmentation, a system using mem_unmap() for unmapping empty pages to keep overall memory usage down,
 * and limiting mem_map() calls to a single function that allocates a large amount of memory at once.
 *
 * This implementation uses structures and macros from the provided macros.txt file and the CS:APP textbook, Chapter 9.9.
 */
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>
#include <string.h>

#include "mm.h"
#include "memlib.h"

/* always use 16-byte alignment */
#define ALIGNMENT 16

/* rounds up to the nearest multiple of ALIGNMENT */
#define ALIGN(size) (((size) + (ALIGNMENT-1)) & ~(ALIGNMENT-1))

/* rounds up to the nearest multiple of mem_pagesize() */
#define PAGE_ALIGN(size) (((size) + (mem_pagesize()-1)) & ~(mem_pagesize()-1))

/* methods from macros.txt */

// needed for OVERHEAD
typedef size_t block_header;
typedef size_t block_footer;

// Total size of each block's overhead (header size + footer size)
#define OVERHEAD (sizeof(block_header) + sizeof(block_footer))

// Pointer to the block's header
#define HDRP(bp) ((char *)(bp) - sizeof(block_header))

// Pointer to the block's footer
#define FTRP(bp) ((char *)(bp) + GET_SIZE(HDRP(bp)) - OVERHEAD)

// Pointer to the next block
#define NEXT_BLKP(bp) ((char *)(bp) + GET_SIZE(HDRP(bp)))

// Pointer to the previous block
#define PREV_BLKP(bp) ((char *)(bp) - GET_SIZE((char *)(bp) - OVERHEAD))

// Get a pointer's value
#define GET(p) (*(size_t *)(p))

// Set a pointer's value
#define PUT(p, val) (*(size_t *)(p) = (val))

// Combines a size and an allocation bit
#define PACK(size, alloc) ((size) | (alloc))

// Gets the size from a header pointer
#define GET_ALLOC(p) (GET(p) & 0x1)

// Gets the allocation bit from a header pointer
#define GET_SIZE(p) (GET(p) & ~0xF)

// Maximum value of two values
#define MAX(a, b) ((a) > (b) ? (a) : (b))

/* Begin helper functions, some from macros.txt */

// Setting a block as allocated, then updates backing structures as needed
static void set_allocated(void *b, size_t size);

// Requests more memory through mem_map, then updates backing structures as needed
static void extend(size_t s);

// Coalesces a freed block together
static void* coalesce(void *bp);

// Adding a node to the explicit free list
static void addNode(void *bp);

// Deleting a node from the explicit free list
static void deleteNode(void *bp);

/* End helper functions */

// Node struct for the explicit free list
typedef struct node{
  struct node* previousNode;
  struct node* nextNode;
}node;

/* Begin added globals */

void *current_avail = NULL;
int current_avail_size = 0;

// Pointer to the first free node in the explicit free list
static node* firstFreeNode;

/* End added globals */

/* 
 * mm_init - initialize the malloc package.
 */
int mm_init(void)
{
  current_avail = NULL;
  current_avail_size = 0;
  firstFreeNode = NULL;
  
  return 0;
}

/* 
 * mm_malloc - Allocate a block by using the firstFreeNode pointer,
 *     grabbing a new page using extend() if necessary.
 */
void *mm_malloc(size_t size)
{
  int newsize = ALIGN(MAX(size, sizeof(node*)) + OVERHEAD);

  // If there is nothing in the explicit free list, use extend() to call mem_map()
  if(firstFreeNode == NULL)
    {
      // 128 seems to be the best balance between throughput and utilization
      extend(newsize * 128);
    }

  // Gets the first free node in the list
  node* currentFree = firstFreeNode;

  // Loops as needed until allocation
  while(1)
    {
      // If the new payload fits in the block, allocate and return it
      if(GET_SIZE(HDRP(currentFree)) >= newsize)
	{
	  set_allocated(currentFree, newsize);
	  return (void *)currentFree;
	}

      // If not and there's no more nodes to search, call mem_map()
      if(currentFree->nextNode == NULL)
	{
	  extend(newsize * 128);
	  currentFree = firstFreeNode;
	}

      // Otherwise, move to the next block in the explicit free list
      else
	{
	  currentFree = currentFree->nextNode;
	}
    }
}

/*
 * mm_free - Freeing a block does nothing.
 */
void mm_free(void *ptr)
{
  size_t blockSize = GET_SIZE(HDRP(ptr));

  // Set the allocation bits to 0
  PUT(HDRP(ptr), PACK(blockSize, 0));
  PUT(FTRP(ptr), PACK(blockSize, 0));

  // Coalesce the newly freed block, return that new block 
  void* coalescedFree = coalesce(ptr);

  void* checkHeader = PREV_BLKP(coalescedFree);
  void* checkFooter = NEXT_BLKP(coalescedFree);

  size_t checkHeaderSize = GET_SIZE(HDRP(checkHeader));
  size_t checkHeaderAlloc = GET_ALLOC(HDRP(checkHeader));
  size_t checkFooterSize = GET_SIZE(HDRP(checkFooter));
  size_t checkFooterAlloc = GET_ALLOC(HDRP(checkFooter));

  // Checks the entire page, if the header and footer are consistent with an empty page, unmap the page
  if(checkHeaderSize == 16 && checkHeaderAlloc == 1 && checkFooterSize == 0 && checkFooterAlloc == 1)
    {
      deleteNode(coalescedFree);
      mem_unmap(coalescedFree-32, GET_SIZE(HDRP(coalescedFree))+32);
    }
}

/*
 * Sets a block as allocated. Includes logic for splitting a block to decrease fragmentation/wasted space. 
 */
static void set_allocated(void *b, size_t size)
{
  size_t freeBlockSize = GET_SIZE(HDRP(b));

  // Set the allocation bits to 1
  PUT(HDRP(b), PACK(size, 1));
  PUT(FTRP(b), PACK(size, 1));

  // Delete the block from the explicit free list
  deleteNode(b);

  size_t remainingFreeSize = freeBlockSize - size;

  // If the remaining page size after allocation is too big, split the block and re-add the new free block to the explicit free list
  if(remainingFreeSize >= 32)
    {
      char *p = (char *)b;
      PUT(HDRP(NEXT_BLKP(b)), PACK(remainingFreeSize, 0));
      PUT(FTRP(NEXT_BLKP(b)), PACK(remainingFreeSize, 0));

      addNode(p+size);
    }

  // Otherwise, just use the whole block, even if the size is slightly bigger than needed
  else
    {
      PUT(HDRP(b), PACK(freeBlockSize, 1));
      PUT(FTRP(b), PACK(freeBlockSize, 1));
    }
}

/*
 * This method maps new memory for the allocator using mem_map().
 */
static void extend(size_t size)
{
  current_avail_size = PAGE_ALIGN(size);
  current_avail = mem_map(current_avail_size);

  char *p = (char *)current_avail;

  // Sets up the appropriate header/footer info for the new block
  PUT(p+8, PACK(16, 1));
  PUT(p+16, PACK(16, 1));
  PUT(p+24, PACK(current_avail_size-32, 0));
  PUT(FTRP(p+32), PACK(current_avail_size-32, 0));
  PUT(FTRP(p+32)+8, PACK(0, 1));

  addNode(p+32);
}

/*
 * This method implements a strategy for coalescing freed blocks into larger free blocks, which helps alleviate fragmentation
 * issues.
 *
 * Modified from the CS:APP textbook, Chapter 9.9
 */
static void* coalesce(void *bp)
{
  // Pointers for the block before and after the newly freed block
  void* prevBlock = PREV_BLKP(bp);
  void* nextBlock = NEXT_BLKP(bp);

  // Those blocks' allocation bits
  size_t prev_alloc = GET_ALLOC(HDRP(prevBlock));
  size_t next_alloc = GET_ALLOC(HDRP(nextBlock));

  // Case 1: Both adjacent blocks are allocated: Simply add the node to the explicit free list and return
  if(prev_alloc && next_alloc)
    {
      addNode(bp);
      return bp;
    }

  // Case 2: Next block is not allocated: Combine the two blocks together, delete the old node and replace with the coalesced one
  else if(prev_alloc && !next_alloc)
    {
      size_t size = GET_SIZE(HDRP(bp)) + GET_SIZE(HDRP(nextBlock));
      PUT(HDRP(bp), PACK(size, 0));
      PUT(FTRP(nextBlock), PACK(size, 0));

      deleteNode(nextBlock);
      addNode(bp);

      return bp;
    }

  // Case 3: Previous block is not allocated: Combine the two blocks together. Since the previous block is already
  // in the explicit free list, don't add the new block and also return the previous block's pointer
  else if(!prev_alloc && next_alloc)
    {
      size_t size = GET_SIZE(HDRP(bp)) + GET_SIZE(HDRP(prevBlock));
      PUT(HDRP(prevBlock), PACK(size, 0));
      PUT(FTRP(bp), PACK(size, 0));

      return prevBlock;
    }

  // Case 4: Both are unallocated: Combine the 3 blocks together, delete the next block from the free list. Since the previous
  // block is already in the explicit free list, don't add the new block and also return the previous block's pointer
  else if(!prev_alloc && !next_alloc)
    {
      size_t size = GET_SIZE(HDRP(bp)) + GET_SIZE(HDRP(prevBlock)) + GET_SIZE(HDRP(nextBlock));
      PUT(HDRP(prevBlock), PACK(size, 0));
      PUT(FTRP(nextBlock), PACK(size, 0));

      deleteNode(nextBlock);
      
      return prevBlock;
    }

  return bp;
}

/*
 * Method for adding a node to the explicit free list
 */
static void addNode(void *bp)
{
  node* newNode = (node *)bp;

  // If we already have at least one free node, replaces the original firstFreeNode with the new node
  if(firstFreeNode != NULL)
    {
      firstFreeNode->previousNode = newNode;
      newNode->nextNode = firstFreeNode;
      newNode->previousNode = NULL;
      firstFreeNode = newNode;
    }

  // If not, just make the firstFreeNode the new node
  else
    {
      firstFreeNode = newNode;
      firstFreeNode->previousNode = NULL;
      firstFreeNode->nextNode = NULL;
    }
}

/*
 * Method for deleting a node from the explicit free list
 */
static void deleteNode(void *bp)
{
  node* nodeToDelete = (node *)bp;

  // Pointers to the deleted node's neighbors
  node* deletePreviousNode = nodeToDelete->previousNode;
  node* deleteNextNode = nodeToDelete->nextNode;

  // If the deleted node is the first free node, check if it is the only free node
  if(nodeToDelete == firstFreeNode)
    {
      // If not, move the firstFreeNode to the next node
      if(firstFreeNode->nextNode != NULL)
	{
	  firstFreeNode = firstFreeNode->nextNode;
	  firstFreeNode->previousNode = NULL;
	}
 
      // If so, just nullify the firstFreeNode's properties
      else
	{
	  firstFreeNode->previousNode = NULL;
	  firstFreeNode->nextNode = NULL;
	  firstFreeNode = NULL;
	}
    }

  // Case 1: No next node (deleted node is at the end of the list): Just detach the deleted node from the previous node
  else if(deleteNextNode == NULL && deletePreviousNode != NULL)
    {
      deletePreviousNode->nextNode = NULL;
    } 

  // Case 2: No previous node (deleted node is at the beginning of the list): If this already isn't marked as firstFreeNode, use the same
  // procedure from that case
  else if(deletePreviousNode == NULL && deleteNextNode != NULL)
    {
      firstFreeNode = deleteNextNode;
      firstFreeNode->previousNode = NULL;
    }

  // Case 3: Both existt (deleted node is in the middle of the list): disconnect the deleted node from both of its neighbors
  else if(deletePreviousNode != NULL && deleteNextNode != NULL)
    {
      deleteNextNode->previousNode = deletePreviousNode;
      deletePreviousNode->nextNode = deleteNextNode;
    }
}
