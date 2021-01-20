/*******************************************
 * Solutions for the CS:APP Performance Lab
 ********************************************/

#include <stdio.h>
#include <stdlib.h>
#include "defs.h"

/* 
 * Please fill in the following student struct 
 */
student_t student = {
  "Brandon Walters",     /* Full name */
  "u1199080@utah.edu",  /* Email address */
};

/***************
 * COMPLEX KERNEL
 ***************/

/******************************************************
 * Your different versions of the complex kernel go here
 ******************************************************/

/* 
 * naive_complex - The naive baseline version of complex 
 */
char naive_complex_descr[] = "naive_complex: Naive baseline implementation";
void naive_complex(int dim, pixel *src, pixel *dest)
{
  int i, j;

  for(i = 0; i < dim; i++)
    for(j = 0; j < dim; j++)
    {

      dest[RIDX(dim - j - 1, dim - i - 1, dim)].red = ((int)src[RIDX(i, j, dim)].red +
						      (int)src[RIDX(i, j, dim)].green +
						      (int)src[RIDX(i, j, dim)].blue) / 3;
      
      dest[RIDX(dim - j - 1, dim - i - 1, dim)].green = ((int)src[RIDX(i, j, dim)].red +
							(int)src[RIDX(i, j, dim)].green +
							(int)src[RIDX(i, j, dim)].blue) / 3;
      
      dest[RIDX(dim - j - 1, dim - i - 1, dim)].blue = ((int)src[RIDX(i, j, dim)].red +
						       (int)src[RIDX(i, j, dim)].green +
						       (int)src[RIDX(i, j, dim)].blue) / 3;

    }
}


/* 
 * complex - Your current working version of complex
 * IMPORTANT: This is the version you will be graded on
 */
char complex_descr[] = "complex: Current working version, utilizing blocking algorithm from lecture and loop tiling";
void complex(int dim, pixel *src, pixel *dest)
{
  int i;
  int j;
  int ii;
  int jj;
  int w;

  // Since we know the smallest image size is 32 and we want to maintain loop tiling benefits,
  // we can set w to either 32 or 16, so that any size of image can be appopriately accomodated. Using
  // 128 as the change point produces the best results.
  if(dim >= 128)
  {
      w = 32;
  }

  else
  {
      w = 16;
  }

  for(i = 0; i < dim; i += w)
    {
      int dimRIDX1 = dim - 1;

      for(j = 0; j < dim; j += w)
	{
	  for(ii = i; ii < i + w; ii++)
	    {
	      int dimRIDX2 = dimRIDX1 - ii;

	      for(jj = j; jj < j + w; jj++)
		{
		  pixel sourcePixel = src[RIDX(jj, ii, dim)];
		  int value = (int)((sourcePixel.red + sourcePixel.green + sourcePixel.blue) / 3);
	          int index = RIDX(dimRIDX2, dimRIDX1 - jj, dim);

		  dest[index].red = value;
		  dest[index].green = value;
		  dest[index].blue = value;
		}
	    }
	}
    }
}

/*********************************************************************
 * register_complex_functions - Register all of your different versions
 *     of the complex kernel with the driver by calling the
 *     add_complex_function() for each test function. When you run the
 *     driver program, it will test and report the performance of each
 *     registered test function.  
 *********************************************************************/

void register_complex_functions() {
  add_complex_function(&complex, complex_descr);
  add_complex_function(&naive_complex, naive_complex_descr);
}


/***************
 * MOTION KERNEL
 **************/

/***************************************************************
 * Various helper functions for the motion kernel
 * You may modify these or add new ones any way you like.
 **************************************************************/


/* 
 * weighted_combo - Returns new pixel value at (i,j) 
 */
static pixel weighted_combo(int dim, int i, int j, pixel *src) 
{
  int ii, jj;
  pixel current_pixel;

  int red, green, blue;
  red = green = blue = 0;

  int num_neighbors = 0;
  for(ii=0; ii < 3; ii++)
    for(jj=0; jj < 3; jj++) 
      if ((i + ii < dim) && (j + jj < dim)) 
      {
	num_neighbors++;
	red += (int) src[RIDX(i+ii,j+jj,dim)].red;
	green += (int) src[RIDX(i+ii,j+jj,dim)].green;
	blue += (int) src[RIDX(i+ii,j+jj,dim)].blue;
      }
  
  current_pixel.red = (unsigned short) (red / num_neighbors);
  current_pixel.green = (unsigned short) (green / num_neighbors);
  current_pixel.blue = (unsigned short) (blue / num_neighbors);
  
  return current_pixel;
}
/*
 * The case used for pixels with a normal 3x3 frame (every pixel outside of the last 2 rows/columns
 */
__attribute__((always_inline)) static pixel normalPixel(int i, int j, int dim, pixel *src)
{
  int red, green, blue;
  pixel current_pixel;
  red = green = blue = 0;

  // Row of the target pixel, row1j is the target pixel
  int row1 = i * dim;
  int row1j = row1 + j;
  int row1j1 = row1 + j + 1;
  int row1j2 = row1 + j + 2;

  int row2 = (i + 1) * dim;
  int row2j = row2 + j;
  int row2j1 = row2 + j + 1;
  int row2j2 = row2 + j + 2;

  int row3 = (i + 2) * dim;
  int row3j = row3 + j;
  int row3j1 = row3 + j + 1;
  int row3j2 = row3 + j + 2;

  // Start with the values of the target pixel
  red += (int)src[row1j].red;
  green += (int)src[row1j].green;
  blue += (int)src[row1j].blue;

  // Adds on the rest of the first row
  red += (int)src[row1j1].red;
  red += (int)src[row1j2].red;
  green += (int)src[row1j1].green;
  green += (int)src[row1j2].green;
  blue += (int)src[row1j1].blue;
  blue += (int)src[row1j2].blue;

  // Adds on the second row
  red += (int)src[row2j].red;
  red += (int)src[row2j1].red;
  red += (int)src[row2j2].red;
  green += (int)src[row2j].green;
  green += (int)src[row2j1].green;
  green += (int)src[row2j2].green;
  blue += (int)src[row2j].blue;
  blue += (int)src[row2j1].blue;
  blue += (int)src[row2j2].blue;

  // Adds on the third row
  red += (int)src[row3j].red;
  red += (int)src[row3j1].red;
  red += (int)src[row3j2].red;
  green += (int)src[row3j].green;
  green += (int)src[row3j1].green;
  green += (int)src[row3j2].green;
  blue += (int)src[row3j].blue;
  blue += (int)src[row3j1].blue;
  blue += (int)src[row3j2].blue;

  // Get the average and add each color to its respective current_pixel value
  current_pixel.red = (unsigned short)(red / 9);
  current_pixel.green = (unsigned short)(green / 9);
  current_pixel.blue = (unsigned short)(blue / 9);
  
  return current_pixel;
}

/*
 * The case for pixels in the second column from the right edge
 */
__attribute__((always_inline)) static pixel secondFromRight(int i, int j, int dim, pixel *src)
{
  pixel current_pixel;
  int red, green, blue;
  red = green = blue = 0;

  int row1 = i * dim;
  int row1j = row1 + j;
  int row1j1 = row1 + j + 1;

  int row2 = (i + 1) * dim;
  int row2j = row2 + j;
  int row2j1 = row2 + j + 1;

  int row3 = (i + 2) * dim;
  int row3j = row3 + j;
  int row3j1 = row3 + j + 1;

  // Target pixel colors
  red += (int)src[row1j].red;
  green += (int)src[row1j].green;
  blue += (int)src[row1j].blue;

  // Get the other pixel in the first row
  red += (int)src[row1j1].red;
  green += (int)src[row1j1].green;
  blue += (int)src[row1j1].blue;

  // Adds the second row
  red += (int)src[row2j].red;
  red += (int)src[row2j1].red;
  green += (int)src[row2j].green;
  green += (int)src[row2j1].green;
  blue += (int)src[row2j].blue;
  blue += (int)src[row2j1].blue;

  // Adds the third row
  red += (int)src[row3j].red;
  red += (int)src[row3j1].red;
  green += (int)src[row3j].green;
  green += (int)src[row3j1].green;
  blue += (int)src[row3j].blue;
  blue += (int)src[row3j1].blue;

  // Average the values to create the new pixel
  current_pixel.red = (unsigned short)(red / 6);
  current_pixel.green = (unsigned short)(green / 6);
  current_pixel.blue = (unsigned short)(blue / 6);

  return current_pixel;
}

/*
 * The case for pixels on the right edge
 */
__attribute__((always_inline)) static pixel farRight(int i, int j, int dim, pixel *src)
{
  pixel current_pixel;
  int red, green, blue;
  red = green = blue = 0;

  int row1 = i * dim;
  int row1j = row1 + j;

  int row2 = (i + 1) * dim;
  int row2j = row2 + j;

  int row3 = (i + 2) * dim;
  int row3j = row3 + j;

  // Start with the target pixel
  red += (int)src[row1j].red;
  green += (int)src[row1j].green;
  blue += (int)src[row1j].blue;

  // Add the pixel from the second row
  red += (int)src[row2j].red;
  green += (int)src[row2j].green;
  blue += (int)src[row2j].blue;

  // Add the pixel from the third row
  red += (int)src[row3j].red;
  green += (int)src[row3j].green;
  blue += (int)src[row3j].blue;

  // Create the average for the new pixel
  current_pixel.red = (unsigned short)(red / 3);
  current_pixel.green = (unsigned short)(green / 3);
  current_pixel.blue = (unsigned short)(blue / 3);

  return current_pixel;
}

/*
 * The case for pixels in the second from bottom row
 */
__attribute__((always_inline)) static pixel secondFromBottom(int i, int j, int dim, pixel *src)
{
  pixel current_pixel;
  int red, green, blue;
  red = green = blue = 0;

  int row1 = i * dim;
  int row1j = row1 + j;
  int row1j1 = row1 + j + 1;
  int row1j2 = row1 + j + 2;

  int row2 = (i + 1) * dim;
  int row2j = row2 + j;
  int row2j1 = row2 + j + 1;
  int row2j2 = row2 + j + 2;

  // Start with the target pixel
  red += (int)src[row1j].red;
  green += (int)src[row1j].green;
  blue += (int)src[row1j].blue;

  // Add the rest of the first row
  red += (int)src[row1j1].red;
  red += (int)src[row1j2].red;
  green += (int)src[row1j1].green;
  green += (int)src[row1j2].green;
  blue += (int)src[row1j1].blue;
  blue += (int)src[row1j2].blue;

  // Add the second row
  red += (int)src[row2j].red;
  red += (int)src[row2j1].red;
  red += (int)src[row2j2].red;
  green += (int)src[row2j].green;
  green += (int)src[row2j1].green;
  green += (int)src[row2j2].green;
  blue += (int)src[row2j].blue;
  blue += (int)src[row2j1].blue;
  blue += (int)src[row2j2].blue;

  // Get the average for the new pixel
  current_pixel.red = (unsigned short)(red / 6);
  current_pixel.green = (unsigned short)(green / 6);
  current_pixel.blue = (unsigned short)(blue / 6);

  return current_pixel;
}

/*
 * The case for pixels in the bottom row
 */
__attribute__((always_inline)) static pixel farBottom(int i, int j, int dim, pixel *src)
{
  pixel current_pixel;
  int red, green, blue;
  red = green = blue = 0;

  int row1 = i * dim;
  int row1j = row1 + j;
  int row1j1 = row1 + j + 1;
  int row1j2 = row1 + j + 2;

  // Start with the target pixel
  red += (int)src[row1j].red;
  green += (int)src[row1j].green;
  blue += (int)src[row1j].blue;

  // Get the rest of the first row
  red += (int)src[row1j1].red;
  red += (int)src[row1j2].red;
  green += (int)src[row1j1].green;
  green += (int)src[row1j2].green;
  blue += (int)src[row1j1].blue;
  blue += (int)src[row1j2].blue;

  // Get the average for the new pixel
  current_pixel.red = (unsigned short)(red / 3);
  current_pixel.green = (unsigned short)(green / 3);
  current_pixel.blue = (unsigned short)(blue / 3);

  return current_pixel;
}

/*
 * The case for the pixel at the corner of the second to last row and column (the pixel 1 up and left from the
 * bottom right corner)
 */
__attribute__((always_inline)) static pixel innerCorner(int i, int j, int dim, pixel *src)
{
  pixel current_pixel;
  int red, green, blue;
  red = green = blue = 0;

  int row1 = i * dim;
  int row1j = row1 + j;
  int row1j1 = row1 + j + 1;

  int row2 = (i + 1) * dim;
  int row2j = row2 + j;
  int row2j1 = row2 + j + 1;

  // Start with the target pixel
  red += (int)src[row1j].red;
  green += (int)src[row1j].green;
  blue += (int)src[row1j].blue;

  // Get the other pixel of the first row
  red += (int)src[row1j1].red;
  green += (int)src[row1j1].green;
  blue += (int)src[row1j1].blue;

  // Get the second row
  red += (int)src[row2j].red;
  red += (int)src[row2j1].red;
  green += (int)src[row2j].green;
  green += (int)src[row2j1].green;
  blue += (int)src[row2j].blue;
  blue += (int)src[row2j1].blue;

  // Get the average for the new pixel
  current_pixel.red = (unsigned short)(red / 4);
  current_pixel.green = (unsigned short)(green / 4);
  current_pixel.blue = (unsigned short)(blue / 4);

  return current_pixel;
}

/*
 * The case for the pixel directly above the bottom right corner
 */
__attribute__((always_inline)) static pixel topOfOuterCorner(int i, int j, int dim, pixel *src)
{
  pixel current_pixel;
  int red, green, blue;
  red = green = blue = 0;

  int row1 = i * dim;
  int row1j = row1 + j;

  int row2 = (i + 1) * dim;
  int row2j = row2 + j;

  // Start with the target pixel
  red += (int)src[row1j].red;
  green += (int)src[row1j].green;
  blue += (int)src[row1j].blue;

  // Get the second pixel
  red += (int)src[row2j].red;
  green += (int)src[row2j].green;
  blue += (int)src[row2j].blue;

  // Get the average for the new pixel
  current_pixel.red = (unsigned short)(red / 2);
  current_pixel.green = (unsigned short)(green / 2);
  current_pixel.blue = (unsigned short)(blue / 2);

  return current_pixel;
}

/*
 * The case for the pixel to the left of the bottom right corner
 */
__attribute__((always_inline)) static pixel leftOfOuterCorner(int i, int j, int dim, pixel *src)
{
  pixel current_pixel;
  int red, green, blue;
  red = green = blue = 0;

  int row1 = i * dim;
  int row1j = row1 + j;
  int row1j1 = row1 + j + 1;

  // Start with the target pixel
  red += (int)src[row1j].red;
  green += (int)src[row1j].green;
  blue += (int)src[row1j].blue;

  // Get the other pixel in the row
  red += (int)src[row1j1].red;
  green += (int)src[row1j1].green;
  blue += (int)src[row1j1].blue;

  // Get the average for the new pixel
  current_pixel.red = (unsigned short)(red / 2);
  current_pixel.green = (unsigned short)(green / 2);
  current_pixel.blue = (unsigned short)(blue / 2);

  return current_pixel;
}

/*
 * The case for the pixel in the bottom right corner
 */
__attribute__((always_inline)) static pixel outerCorner(int i, int j, int dim, pixel *src)
{
  pixel current_pixel;
  int red, green, blue;
  red = green = blue = 0;

  int row1 = i * dim;
  int row1j = row1 + j;

  // Get the pixel's values
  red += (int)src[row1j].red;
  green += (int)src[row1j].green;
  blue += (int)src[row1j].blue;

  // Assign to the new pixel
  current_pixel.red = (unsigned short)(red);
  current_pixel.green = (unsigned short)(green);
  current_pixel.blue = (unsigned short)(blue);
  
  return current_pixel;
}

/******************************************************
 * Your different versions of the motion kernel go here
 ******************************************************/

char optimized_motion_descr[] = "optimized_motion: Optimized implementation using helper methods for all cases";
void optimized_motion(int dim, pixel *src, pixel *dst)
{
  int i;
  int j;
  int k;

  // Iterate through all pixels not in the last 2 rows
  for(i = 0; i < dim - 2; i++)
    {
      for(j = 0; j < dim - 2; j++)
	{
	  dst[RIDX(i, j, dim)] = normalPixel(i, j, dim, src);
	}
      // Handles the last 2 columns
      dst[RIDX(i, j, dim)] = secondFromRight(i, j, dim, src);
      dst[RIDX(i, j + 1, dim)] = farRight(i, j + 1, dim, src);
    }
  // Go back to handle the last two rows, BE CAREFUL TO KEEP I AND J AS ARE
  for(k = 0; k < dim - 2; k++)
    {
      dst[RIDX(i, k, dim)] = secondFromBottom(i, k, dim, src);
      dst[RIDX(i + 1, k, dim)] = farBottom(i + 1, k, dim, src);
    }
  // The final four pixels, making up a 2x2 square in the bottom right of the image
  dst[RIDX(i, j, dim)] = innerCorner(i, j, dim, src);
  dst[RIDX(i, j + 1, dim)] = topOfOuterCorner(i, j + 1, dim, src);
  dst[RIDX(i + 1, j, dim)] = leftOfOuterCorner(i + 1, j, dim, src);
  dst[RIDX(i + 1, j + 1, dim)] = outerCorner(i + 1, j + 1, dim, src);
}

/*
 * naive_motion - The naive baseline version of motion 
 */
char naive_motion_descr[] = "naive_motion: Naive baseline implementation";
void naive_motion(int dim, pixel *src, pixel *dst) 
{
  int i, j;
    
  for (i = 0; i < dim; i++)
    for (j = 0; j < dim; j++)
      dst[RIDX(i, j, dim)] = weighted_combo(dim, i, j, src);
}


/*
 * motion - Your current working version of motion. 
 * IMPORTANT: This is the version you will be graded on
 */
char motion_descr[] = "motion: Current working version, using helper methods on all cases";
void motion(int dim, pixel *src, pixel *dst) 
{
  optimized_motion(dim, src, dst);
}

/********************************************************************* 
 * register_motion_functions - Register all of your different versions
 *     of the motion kernel with the driver by calling the
 *     add_motion_function() for each test function.  When you run the
 *     driver program, it will test and report the performance of each
 *     registered test function.  
 *********************************************************************/

void register_motion_functions() {
  add_motion_function(&motion, motion_descr);
  add_motion_function(&naive_motion, naive_motion_descr);
}
