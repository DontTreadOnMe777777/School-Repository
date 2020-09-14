/* A 'string set' is defined as a set of strings stored
 * in sorted order in a drop list.  See the class video
 * for details.
 *
 * For lists that do not exceed 2^(max_next_width elements+1)
 * elements, the add, remove, and contains functions are 
 * O(lg size) on average.  The operator= and get_elements 
 * functions are O(size).   
 * 
 * Peter Jensen
 * January 28, 2020
 */

#include "string_set.h"
#include <iostream>  // For debugging, if needed.

namespace cs3505
{
  /*******************************************************
   * string_set member function definitions
   ***************************************************** */
  
  /** Constructor:  The parameter indicates the maximum
    *   width of the next pointers in the drop list nodes.
    */
  string_set::string_set(int max_next_width)
  {
    node * head_node = new node("", max_next_width);
    head = head_node;
    size = 0;
    current = head;
    this->max_next_width = max_next_width;
  }

  
  /** Copy constructor:  Initialize this set
    *   to contain exactly the same elements as
    *   another set.
    */
  string_set::string_set (const string_set & other)
  {
    node * head_node = new node(*other.head);
    head = head_node;
    size = 0;
    this->max_next_width = other.max_next_width;
    this->add(head_node->data);

    node * temp = other.current;

    while(temp->nodes[0] != NULL)
    {
      node * new_node = new node(*temp);
      this->add(new_node->data);
      size++;
    }
  }


  /** Destructor:  release any memory allocated
    *   for this object.
    */
  string_set::~string_set()
  {
    while(current->nodes[0] != NULL)
    {
      previous = current;
      current = current->nodes[0];
      delete previous;
      size--;
    }
    delete current;
    size--;
  }

  void string_set::add(const std::string & target)
  {
    node * node_to_add = new node(target, max_next_width);

    int node_size;
    node_size = node_to_add->size;

    std::vector<node *> nodes_to_change;

    for(int i = max_next_width; i >= 0; i--)
    {
      while(current->nodes[i] != NULL && i <= node_size)
      {
	    if(current->nodes[i]->data < target)
	    {
	      previous = current;
	      current = current->nodes[i];
        }

	    else if(current->nodes[i]->data == target)
	    {
	      current = head;
          previous = NULL;
	      return;
        }

	    else if(current->nodes[i]->data > target)
	    {
	      nodes_to_change.insert(nodes_to_change.begin(), current);
	      break;
        }
      }

      if(current->nodes[i] == NULL && i <= node_size - 1) 
      {
          current->nodes[i] = node_to_add;
      }
    }

    for(int j = 0; j <= node_size - 1; j++)
    {
      if(!nodes_to_change.empty())
      {
	    node_to_add->nodes.push_back(nodes_to_change[j]->nodes[j]);
        nodes_to_change[j]->nodes[j] = node_to_add;
      }
    }

    current = head;
    previous = NULL;
    size++;
  }

  void string_set::remove(const std::string & target)
  {
    std::vector<node *> nodes_to_change;
    node * node_to_remove = new node("Not Found!", max_next_width);

    for(int i = max_next_width; i >= 0; i--)
    {
      while(current->nodes[i] != NULL)
      {
	    if(current->nodes[i]->data == target)
	    {
	      node_to_remove = current->nodes[i];
	      if(node_to_remove->nodes[i] == NULL)
	      {
	          current->nodes[i] = NULL;
	      }
	      
	      else 
	      {
	         nodes_to_change.insert(nodes_to_change.begin(), current); 
	         previous = current;
	         current = current->nodes[i];
	      }
        }

	    else if(current->nodes[i]->data < target)
	    {
	      previous = current;
	      current = current->nodes[i];
        }
      }
    }
	
    if(node_to_remove->data != "Not Found!")
    {
      for(int j = 0; j < nodes_to_change.size(); j++)
      {
        nodes_to_change[j]->nodes[j] = node_to_remove->nodes[j];
      }

      delete node_to_remove;
      size--;
      current = head;
      previous = NULL;
    } 
  }

  bool string_set::contains(const std::string & target) const
  {
    node * temp = current;
    for(int i = max_next_width; i >= 0; i--)
    {
      while(temp->nodes[0] != NULL)
      {
	    if(temp->nodes[0]->data == target)
	    {
	      return true;
        }
        
	    temp = temp->nodes[0];
      }
    }
    return false;
  }

  int string_set::get_size() const
  {
    return size;
  }

  std::vector<std::string> string_set::get_elements()
  {
    std::vector<std::string> elements;

    while(current->nodes[0] != NULL)
    {
      elements.push_back(current->data);
      current = current->nodes[0];
    }

    current = head;
    return elements;
  }

  string_set & string_set::operator= (const string_set & rhs)
  {
    node * temp = rhs.current;
    while(current->nodes[0] != NULL)
    {
      previous = current;
      current = current->nodes[0];
      delete previous;
    }
    delete current;

    while(rhs.current->nodes[0] != NULL)
    {
      node * copynode = new node(*rhs.current);
      this->add(copynode->data);
      if(copynode->data == "")
      {
	head = copynode;
	current = head;
	previous = NULL;
      }

      temp = temp->nodes[0];
    }
    return *this;
  }
  // Additional public and private helper function definitions needed

}
