/* Brandon Walters
 * January 25th, 2020
 * CS 3505, Prof. Jensen
 *
 * This program is used to test the methods and function written for the pntvec class in the pntvec.cpp file.
 * If the pntvec class operates as expected, this method will return 0. Otherwise, this method will print an
 * error message with the name of the method in question.
 */

// The header for this class, contains all class declarations
#include "pntvec.h"
// Included for use of streaming and printing to the console
#include <iostream>
// Included for testing the overloaded streaming operators
#include <sstream>

// Application entry point, tests all methods in the pntvec class. If an error occurs, returns -1 and prints error message and location to the console. Otherwise, returns 0.
int main()
{
  // Tests of all three constuctors
  pntvec empty_con;
  pntvec double_con(1.0, 2.0, 3.0);
  pntvec copy_con(double_con);

  if(empty_con.get_x() - 0 > 0.1 || empty_con.get_y() - 0 > 0.1 || empty_con.get_z() - 0 > 0.1)
  {
    std::cout << "Error in the default constructor." << std::endl;
    return -1;
  }

  if(double_con.get_x() - 1.0 > 0.1 || double_con.get_y() - 2.0 > 0.1|| double_con.get_z() - 3.0 > 0.1)
  {
    std::cout << "Error in the double constructor." << std::endl;
    return -1;
  }

  if(copy_con.get_x() - 1.0 > 0.1|| copy_con.get_y() - 2.0 > 0.1|| copy_con.get_z() - 3.0 > 0.1)
  {
    std::cout << "Error in the copy constructor." << std::endl;
    return -1;
  }

  
  // Tests of the overwritten mathematical operators
  copy_con = empty_con;

  if(copy_con.get_x() - 0 > 0.1 || copy_con.get_y() - 0 > 0.1|| copy_con.get_z() - 0 > 0.1)
  {
    std::cout << "Error in the assignment operator." << std::endl;
    return -1;
  }

  copy_con = copy_con + double_con;

  if(copy_con.get_x() - 1.0 > 0.1|| copy_con.get_y() - 2.0 > 0.1|| copy_con.get_z() - 3.0 > 0.1)
  {
    std::cout << "Error in the addition operator." << std::endl;
    return -1;
  }

  copy_con = copy_con - double_con;

  if(copy_con.get_x() - 0 > 0.1|| copy_con.get_y() - 0 > 0.1|| copy_con.get_z() - 0 > 0.1)
  {
    std::cout << "Error in the subtraction operator." << std::endl;
    return -1;
  }

  double d = 10.0;
  double_con = double_con * d;

  if(double_con.get_x() - 10.0 > 0.1|| double_con.get_y() - 20.0 > 0.1|| double_con.get_z() - 30.0 > 0.1)
  {
    std::cout << "Error in the multiplication operator." << std::endl;
    return -1;
  }

  double_con = -double_con;

  if(double_con.get_x() - -10.0 > 0.1|| double_con.get_y() - -20.0 > 0.1|| double_con.get_z() - -30.0 > 0.1)
  {
    std::cout << "Error in the negation operator." << std::endl;
    return -1;
  }

  // Tests of the utility methods
  d = double_con.get_x();

  if(d - -10.0 > 0.1)
  {
    std::cout << "Error in the get_x method." << std::endl;
    return -1;
  }

  d = double_con.get_y();

  if(d - -20.0 > 0.1)
  {
    std::cout << "Error in the get_y method." << std::endl;
    return -1;
  }

  d = double_con.get_z();

  if(d - -30.0 > 0.1)
  {
    std::cout << "Error in the get_z method." << std::endl;
    return -1;
  }

  d = empty_con.distance_to(double_con);

  if(d - 37.4 > 0.1)
  {
    std::cout << "Error in the distance method." << std::endl;
    return -1;
  }


  // Tests of the overloaded streaming operators

  // Uses an ostringstream to simulate streaming pntvecs out
  std::ostringstream stringstream;
  stringstream << empty_con;
  std::string s1 = "(0.0, 0.0, 0.0)";

  if(!stringstream.str().compare(s1))
  {
    std::cout << stringstream.str() << std::endl;
    std::cout << "Error in the output operator." << std::endl;
    return -1;
  }

  // Uses an istringstream to simulate a stream in to create a new pntvec
  std::istringstream instream("0.0 0.0 0.0");
  instream >> double_con;

  if(double_con.get_x() - 0 > 0.1|| double_con.get_y() - 0 > 0.1|| double_con.get_z() - 0 > 0.1)
  {
    std::cout << "Error in the input operator." << std::endl;
    return -1;
  }
  
  return 0;
}
