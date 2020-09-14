/* This node class is used to build linked lists for the
 * string_set class.
 *
 * Peter Jensen
 * January 28, 2020
 */

#include "node.h"
#include <cstdlib>

// We're not in a namespace.  We are not in any class.  Symbols defined
//   here are globally available.  We need to qualify our function names
//   so that we are definining our cs3505::node class functions.
//
// Note that we could also use the namespace cs3505 { } block.  This would
//   eliminate one level of name qualification.  The 'using' statement will
//   not help in this situation.  
// 
// Qualify it as shown here for functions: cs3505::node::functionname, etc.

/*******************************************************
 * node member function definitions
 ***************************************************** */

cs3505::node::node(const std::string & text, const int & max_width)
{
  this->data = text;
  this->nodes = std::vector<node*>();
  nodes.push_back(NULL); 
  this->size = 1;

  while(size < max_width)
  {
    if(text == "" || rand() % 2 == 0)
    {
      nodes.push_back(NULL);
      size++;
    }
    else
    {
      break;
    }
  }
}

cs3505::node::node(const node & other)
{
  this->data = other.data;
  this->nodes = other.nodes;
  this->size = other.size;
}

cs3505::node::~node()
{
  for(int i = 0; i < nodes.size(); i++)
  {
    nodes[i] = NULL;
  }
}
// Students will decide how to implement the constructor, destructor, and
//   any helper methods.
