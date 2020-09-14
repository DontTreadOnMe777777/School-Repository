/* Brandon Walters, u1199080
 * January 23rd, 2020
 * CS 3505 - Mr. Jensen
 *
 * This class is built to represent a point vector. It contains three fields: an x coordinate, a y coordinate,
 * and a z coordinate, all of which are represented by doubles.
 *
 * This class also contains declarations for many methods, which will be implemented in the pntvec.cpp class.
 * These methods are largely operator overloads designed to allow for pntvec math, along with some methods used
 * to help compare two pntvec objects to each other.
 */

// Inclusion for overwriting the << and >> operators
#include <iostream>

#ifndef PNTVEC_H
#define PNTVEC_H

class pntvec 
{
  // The only private thing in this class is the doubles representing the coordinates
  private:
    double x, y, z;
  
  // Everything else is public, including all methods and constructors
  public:
    // First, the three constructors
    pntvec();
    pntvec(double point_x, double point_y, double point_z);
    pntvec(const pntvec & other);

    // Overwritten mathematical operators
    pntvec & operator= (const pntvec & other);
    const pntvec operator+ (const pntvec & rhs) const;
    const pntvec operator- (const pntvec & rhs) const;
    const pntvec operator* (const double & rhs) const;
    const pntvec operator- () const;
    // Utility methods for coordinates and distance calculation
    const double get_x() const;
    const double get_y() const;
    const double get_z() const;

    const double distance_to(const pntvec & other) const;
    // Finally, the two friend functions for stream overwriting
    friend std::ostream & operator<< (std::ostream & out, const pntvec & p);
    friend std::istream & operator>> (std::istream & in, pntvec & p);
};

#endif

