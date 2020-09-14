/* Brandon Walters
 * January 24th, 2020
 * CS 3505, Prof. Jensen
 *
 * This class is the implementation of the pntvec object, using methods declared in the pntvec.h file.
 * This file will provide implementations of all methods defined in the pntvec.h file.
 */

//Included for stream operations
#include <iostream>
// The header for this class, contains all class declarations
#include "pntvec.h"
// Included for using of the sqrt and pow functions
#include <cmath>

// The default constructor. Will set all coordinates to 0.
pntvec::pntvec()
{
  x = 0;
  y = 0;
  z = 0;
}

// The double constructor. Will construct a pntvec given three double values.
pntvec::pntvec(double point_x, double point_y, double point_z)
{
  x = point_x;
  y = point_y;
  z = point_z;
}

// The copy constructor. Will create a pntvec that is a copy of the given pntvec.
pntvec::pntvec(const pntvec & other)
{
  this->x = other.x;
  this->y = other.y;
  this->z = other.z;
}

// Overwritten assignment operator, used to assign one pntvec's values to another pntvec.
pntvec & pntvec::operator= (const pntvec & rhs)
{
  (*this).x = rhs.x;
  (*this).y = rhs.y;
  (*this).z = rhs.z;

  return *this;
}

// Overwritten addition operator, creates a pntvec that is the sum of two pntvecs.
const pntvec pntvec::operator+ (const pntvec & rhs) const
{
  pntvec result;

  result.x = x + rhs.x;
  result.y = y + rhs.y;
  result.z = z + rhs.z;

  return result;
}

// Overwritten subtraction operator, creates a pntvec that is the difference of two pntvecs.
const pntvec pntvec::operator- (const pntvec & rhs) const
{
  pntvec result;

  result.x = x - rhs.x;
  result.y = y - rhs.y;
  result.z = z - rhs.z;

  return result;
}

// Overwritten multiplication operator, will create a pntvec that is the result of a pntvec being scaled by a scalar.
const pntvec pntvec::operator* (const double & rhs) const
{
  pntvec result;

  result.x = x * rhs;
  result.y = y * rhs;
  result.z = z * rhs;

  return result;
}

// Overwritten negation operator, will create a pntvec that is the opposite (flipped values) of a given pntvec.
const pntvec pntvec::operator- () const
{
  pntvec result;

  result.x = x * -1;
  result.y = y * -1;
  result.z = z * -1;

  return result;
}

// Returns the x coordinate
const double pntvec::get_x() const
{
  return x;
}

// Returns the y coordinate
const double pntvec::get_y() const
{
  return y;
}

// Returns the z coordinate
const double pntvec::get_z() const
{
  return z;
}

// Calculates the distance between two pntvecs and returns the result as a double.
const double pntvec::distance_to(const pntvec & rhs) const
{
  pntvec result;
  result.x = x - rhs.x;
  result.y = y - rhs.y;
  result.z = z - rhs.z;

  // Distance formula
  return sqrt(pow(result.x, 2) + pow(result.y, 2) + pow(result.z, 2));
}

// Overwritten out stream operator, allows for proper pntvec formatting when streamed to a file, terminal, etc.
std::ostream & operator<< (std::ostream & out, const pntvec & p)
{
  out << "(";
  out << p.get_x() << ", ";
  out << p.get_y() << ", ";
  out << p.get_z() << ")";

  return out;
}

// Overwritten in stream operator, creates a proper pntvec when values are read from a file, terminal, etc.
std::istream & operator>> (std::istream & in, pntvec & p)
{
  in >> p.x >> p.y >> p.z;
  return in;
}



