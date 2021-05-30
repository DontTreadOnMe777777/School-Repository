/**
 * Write your solutions here and turn in just this file.
 * Please keep in mind that your solution must compile and work with the basic
 * tester and tests. Feel free to extend them, but we will have our
 * own edits to them and your assignment1.c needs to work correctly with the
 * original tests/tester.
 */
#define _POSIX_C_SOURCE 200809L // for strdup; it won't be part of C until 2023.
#include <string.h>
#include <stdlib.h>

#include "assignment1.h"

// Struct for the nodes to be used in our doubly linked list implementation, contains the key-value pair and pointers
struct cacheNode
{
  char* nodeKey;
  char* nodeValue;

  struct cacheNode* nextNode;
  struct cacheNode* prevNode;
};
// Pointers to the first and last nodes in the list, used for traversal and deletion
static struct cacheNode* headNode = NULL;
static struct cacheNode* lastNode = NULL;

// The number of nodes in the cache, used to keep under our max of 16 nodes
static int numNodes = 0;

/*
 * The method used to search our cache for a specific key. Returns the value associated if the key is in the cache, NULL otherwise. 
 */
char* cache_get(const char* key) {
  struct cacheNode* currentNode;
  currentNode = headNode;

  // Starting from the head node, search through the linked list for the key
  while (currentNode != NULL)
    {
      // If we find the key in the cache, we bring it to the front
      if (strcmp(currentNode->nodeKey, key) == 0)
	{
	  // If the node is the last node and there are <= 2 nodes
	  if (currentNode->nextNode == NULL && currentNode != headNode)
	    {
	      struct cacheNode* previousNode = currentNode->prevNode;

	      previousNode->nextNode = NULL;

	      currentNode->prevNode = NULL;
	      currentNode->nextNode = headNode;
	      headNode->prevNode = currentNode;
	      headNode = currentNode;
	    }

	  // If the node is not the first or last node
	  else if (currentNode != headNode)
	    {
	      struct cacheNode* previousNode = currentNode->prevNode;
	      struct cacheNode* nextNode = currentNode->nextNode;

	      previousNode->nextNode = nextNode;
	      nextNode->prevNode = previousNode;

	      currentNode->prevNode = NULL;
	      currentNode->nextNode = headNode;
	      headNode->prevNode = currentNode;
	      headNode = currentNode;
	    }
	  return strdup(currentNode->nodeValue);
	}
      // Otherwise, continue iterating through the linked list
      else
	{
	  currentNode = currentNode->nextNode;
	}
    }
  return NULL;
}

/*
 * This method is used to update or create a new node with the specified key/value pair, and put it at the front of the cache. 
 */
void cache_set(const char* key, const char* value) {

  // If there is no head node already (an empty cache), then we create our first new node and make it the head
  if (headNode == NULL)
    {
      struct cacheNode* newNode;
      newNode = malloc(sizeof(struct cacheNode));
      
      newNode->nodeKey = strdup(key);
      newNode->nodeValue = strdup(value);

      headNode = newNode;

      numNodes++;
      return;
    }

  // Otherwise, scan through the linked list for a matching key.
  else
    {
      struct cacheNode* currentNode = headNode;
      
      while (currentNode != NULL)
	{
	  // If a matching key is found, update the value and move the node to the front
	  if (strcmp(currentNode->nodeKey, key) == 0)
	    {
	      currentNode->nodeValue = strdup(value);
	      // If the node is the last node and there are <= 2 nodes in the cache
	      if (currentNode->nextNode == NULL && currentNode != headNode)
		{
		  struct cacheNode* previousNode = currentNode->prevNode;

		  previousNode->nextNode = NULL;

		  currentNode->prevNode = NULL;
		  currentNode->nextNode = headNode;
		  headNode->prevNode = currentNode;
		  headNode = currentNode;
		}
	      // If the node is not the first or last node
	      else if (currentNode != headNode)
		{
		  struct cacheNode* previousNode = currentNode->prevNode;
		  struct cacheNode* nextNode = currentNode->nextNode;

		  previousNode->nextNode = nextNode;
		  nextNode->prevNode = previousNode;

		  currentNode->prevNode = NULL;
		  currentNode->nextNode = headNode;
		  headNode->prevNode = currentNode;
		  headNode = currentNode;
		}
	      return;
	    }
	  // Else, continue iterating, being sure to mark a last node for possible deletion
	  else
	    {
	      if (currentNode->nextNode == NULL)
		{
		  lastNode = currentNode;
		}

	      currentNode = currentNode->nextNode;
	    }
	}

      // If no matching key exists, create a new node to put at the front
      struct cacheNode* newNode;
      newNode = malloc(sizeof(struct cacheNode));

      newNode->nodeKey = strdup(key);
      newNode->nodeValue = strdup(value);

      newNode->nextNode = headNode;
      headNode->prevNode = newNode;
      headNode = newNode;

      numNodes++;

      // If we have too many nodes, mark the end node for deletion
      if (numNodes > 16)
	{
	  cache_del(lastNode->nodeKey);
	}
    }
}

/*
 * This method deletes a specified node from the cache, if it exists.
 */
bool cache_del(const char* key) {
  struct cacheNode* currentNode = headNode;

  // Iterate through the cache, searching for the specified key
  while (currentNode != NULL)
    {
      // If we find the specified key, delete the node and adjust pointers accordingly
      if (strcmp(currentNode->nodeKey, key) == 0)
	{
	  // If the only node in the cache
	  if (currentNode->nextNode == NULL && currentNode == headNode)
	    {
	      headNode = NULL;
	    }

	  // If the last node
	  else if (currentNode->nextNode == NULL)
	    {
	      struct cacheNode* previousNode = currentNode->prevNode;

	      previousNode->nextNode = NULL;
	    }

	  // If the first node
	  else if (currentNode == headNode)
	    {
	      struct cacheNode* nextNode = currentNode->nextNode;

	      nextNode->prevNode = NULL;
	      headNode = nextNode;
	    }

	  // Otherwise
	  else
	    {
	      struct cacheNode* previousNode = currentNode->prevNode;
	      struct cacheNode* nextNode = currentNode->nextNode;

	      previousNode->nextNode = nextNode;
	      nextNode->prevNode = previousNode;
	    }

	  // Free the current node's memory, decrement the number of nodes, and return a true deletion.
	  free(currentNode);
	  numNodes--;
	  return true;
	}
      // Otherwise, continue iterating
      else
	{
	  currentNode = currentNode->nextNode;
	}
    }
    
  return false;
}

/*
 * This method systematically deletes all nodes currently in the cache, freeing up all memory and preparing the cache for fresh insertions.
 */
void cache_clear(void) {
  struct cacheNode* currentNode = headNode;

  // Delete every node in the cache
  while (currentNode != NULL)
    {
      struct cacheNode* nodeToDelete = currentNode;
      currentNode = currentNode->nextNode;
      cache_del(nodeToDelete->nodeKey);
    }
}
