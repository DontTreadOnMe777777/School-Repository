// Written by Joe Zachary and Brandon Walters, 9/4/19

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SpreadsheetUtilities
{

    /// <summary>
    /// (s1,t1) is an ordered pair of strings
    /// t1 depends on s1; s1 must be evaluated before t1
    /// 
    /// A DependencyGraph can be modeled as a set of ordered pairs of strings.  Two ordered pairs
    /// (s1,t1) and (s2,t2) are considered equal if and only if s1 equals s2 and t1 equals t2.
    /// Recall that sets never contain duplicates.  If an attempt is made to add an element to a 
    /// set, and the element is already in the set, the set remains unchanged.
    /// 
    /// Given a DependencyGraph DG:
    /// 
    ///    (1) If s is a string, the set of all strings t such that (s,t) is in DG is called dependents(s).
    ///        (The set of things that depend on s)    
    ///        
    ///    (2) If s is a string, the set of all strings t such that (t,s) is in DG is called dependees(s).
    ///        (The set of things that s depends on) 
    //
    // For example, suppose DG = {("a", "b"), ("a", "c"), ("b", "d"), ("d", "d")}
    //     dependents("a") = {"b", "c"}
    //     dependents("b") = {"d"}
    //     dependents("c") = {}
    //     dependents("d") = {"d"}
    //     dependees("a") = {}
    //     dependees("b") = {"a"}
    //     dependees("c") = {"a"}
    //     dependees("d") = {"b", "d"}
    /// </summary>
    public class DependencyGraph
    {
        // To represent the lists of dependents and dependees, I have used a Dictionary acting as a Map, with strings as keys and HashSets containing strings as the values. 
        // Each value corresponds to a dependent or dependee, and the HashSets store its associated dependees or dependents, respectfully.
        // If a string has no dependees or dependents, that HashSet is left blank. The use of a map allows for the finding of keys at O(1) complexity.
        private Dictionary<string, HashSet<string>> dependentsMap;
        private Dictionary<string, HashSet<string>> dependeesMap;
        private int size = 0;

        /// <summary>
        /// Creates an empty DependencyGraph.
        /// </summary>
        public DependencyGraph()
        {
            // Represents the dependents, with the strings being the names of the dependents and the HashSet containing all of its dependees
             dependentsMap = new Dictionary<string, HashSet<string>>();

            // Represents the dependees, with the strings being the names of the dependees and the HashSet containing all of its dependents
            dependeesMap = new Dictionary<string, HashSet<string>>();
        }


        /// <summary>
        /// The number of ordered pairs in the DependencyGraph.
        /// </summary>
        public int Size
        {
            get { return size; }
        }


        /// <summary>
        /// The size of dependees(s).
        /// This property is an example of an indexer.  If dg is a DependencyGraph, you would
        /// invoke it like this:
        /// dg["a"]
        /// It should return the size of dependees("a")
        /// </summary>
        public int this[string s]
        {
            get
            {
                HashSet<string> dependeesList;

                // If the dependent does exist
                if (dependeesMap.TryGetValue(s, out dependeesList))
                {
                    dependeesMap.TryGetValue(s, out dependeesList);
                }

                // If the dependent does not exist
                else
                {
                    dependeesList = new HashSet<string>();
                }
                
                return dependeesList.Count;
            }
        }


        /// <summary>
        /// Reports whether dependents(s) is non-empty.
        /// </summary>
        public bool HasDependents(string s)
        {
            if (dependentsMap.ContainsKey(s))
            {
                HashSet<string> dependentsList = new HashSet<string>();
                dependentsMap.TryGetValue(s, out dependentsList);
                return dependentsList.Count > 0;
            }

            return false;
        }


        /// <summary>
        /// Reports whether dependees(s) is non-empty.
        /// </summary>
        public bool HasDependees(string s)
        {
            if (dependeesMap.ContainsKey(s))
            {
                HashSet<string> dependeesList = new HashSet<string>();
                dependeesMap.TryGetValue(s, out dependeesList);
                return dependeesList.Count > 0;
            }

            return false;
        }


        /// <summary>
        /// Enumerates dependents(s).
        /// </summary>
        public IEnumerable<string> GetDependents(string s)
        {
            HashSet<string> dependentList;
            if (dependentsMap.TryGetValue(s, out dependentList))
            {
                return dependentList;
            }

            else
            {
                dependentList = new HashSet<string>();
                return dependentList;
            }
            
        }

        /// <summary>
        /// Enumerates dependees(s).
        /// </summary>
        public IEnumerable<string> GetDependees(string s)
        {
            HashSet<string> dependeeList;
            if (dependeesMap.TryGetValue(s, out dependeeList))
            {
                return dependeeList;
            }

            else
            {
                dependeeList = new HashSet<string>();
                return dependeeList;
            }
        }


        /// <summary>
        /// <para>Adds the ordered pair (s,t), if it doesn't exist</para>
        /// 
        /// <para>This should be thought of as:</para>   
        /// 
        ///   t depends on s
        ///
        /// </summary>
        /// <param name="s"> s must be evaluated first. T depends on S</param>
        /// <param name="t"> t cannot be evaluated until s is</param>        /// 
        public void AddDependency(string s, string t)
        {
            // If the dependent does not already exist
            if (!dependentsMap.ContainsKey(s))
            {
                dependentsMap.Add(s, new HashSet<string>());
            }

            // If the dependee does not already exist in the dependent map
            if (!dependentsMap.ContainsKey(t))
            {
                dependentsMap.Add(t, new HashSet<string>());
            }

            HashSet<string> dependeesList;
            // Adds the dependee to the list associated with the dependent
            dependentsMap.TryGetValue(s, out dependeesList);
            if (!dependeesList.Contains(t))
            {
                // Increments size
                size++;
            }
            dependeesList.Add(t);

            // If the dependee does not already exist
            if (!dependeesMap.ContainsKey(t))
            {
                dependeesMap.Add(t, new HashSet<string>());
            }

            // If the dependent does not already exist in the dependee map
            if (!dependeesMap.ContainsKey(s))
            {
                dependeesMap.Add(s, new HashSet<string>());
            }

            HashSet<string> dependentsList;
            // Adds the dependent to the list associated with the dependee
            dependeesMap.TryGetValue(t, out dependentsList);
            dependentsList.Add(s); 
        }


        /// <summary>
        /// Removes the ordered pair (s,t), if it exists
        /// </summary>
        /// <param name="s"></param>
        /// <param name="t"></param>
        public void RemoveDependency(string s, string t)
        {
            HashSet<string> dependeesList = new HashSet<string>();
            // Removes the dependee from the list associated with the dependent

            if (dependentsMap.ContainsKey(s))
            {
                dependentsMap.TryGetValue(s, out dependeesList);
            }
            
            dependeesList.Remove(t);

            HashSet<string> dependentsList = new HashSet<string>();
            // Removes the dependent from the list associated with the dependee

            if (dependeesMap.ContainsKey(t))
            {
                dependeesMap.TryGetValue(t, out dependentsList);
            }
           
            dependentsList.Remove(s);

            // Decrements size
            size--;
        }


        /// <summary>
        /// Removes all existing ordered pairs of the form (s,r).  Then, for each
        /// t in newDependents, adds the ordered pair (s,t).
        /// </summary>
        public void ReplaceDependents(string s, IEnumerable<string> newDependents)
        {
            HashSet<string> dependentsList = new HashSet<string>();
            if (dependentsMap.ContainsKey(s))
            {
                dependentsMap.TryGetValue(s, out dependentsList);
            }

            // Removes existing dependencies
            foreach (string dependent in dependentsList.ToList())
            {
                RemoveDependency(s, dependent);
            }

            // Adds new dependencies
            foreach (string dependentToAdd in newDependents.ToList())
            {
                AddDependency(s, dependentToAdd);
            }

        }


        /// <summary>
        /// Removes all existing ordered pairs of the form (r,s).  Then, for each 
        /// t in newDependees, adds the ordered pair (t,s).
        /// </summary>
        public void ReplaceDependees(string s, IEnumerable<string> newDependees)
        {
            HashSet<string> dependeesList = new HashSet<string>();

            if (dependeesMap.ContainsKey(s))
            {
                dependeesMap.TryGetValue(s, out dependeesList);
            }

            // Removes existing dependencies
            foreach (string dependee in dependeesList.ToList())
            {
                RemoveDependency(dependee, s);
            }

            // Adds new dependencies
            foreach (string dependeeToAdd in newDependees.ToList())
            {
                AddDependency(dependeeToAdd, s);
            }
        }

    }

}


