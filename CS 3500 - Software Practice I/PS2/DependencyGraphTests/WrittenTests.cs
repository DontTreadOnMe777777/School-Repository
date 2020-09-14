// Written by Joe Zachary and Brandon Walters, 9/5/19

using Microsoft.VisualStudio.TestTools.UnitTesting;
using SpreadsheetUtilities;
using System.Collections.Generic;

namespace DependencyGraphTests
{
    [TestClass]
    public class WrittenTests
    {
        /// <summary>
        /// Checks the hasDependents method, using both an empty and a non-empty DependencyGraph.
        /// </summary>
        [TestMethod]
        public void HasDependents()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("x", "y");
            Assert.IsTrue(t.HasDependents("x"));
            t.RemoveDependency("x", "y");
            Assert.IsFalse(t.HasDependents("x"));
            Assert.IsFalse(t.HasDependents("DoesNotExist"));
        }

        /// <summary>
        /// Checks the hasDependees method, using both an empty and a non-empty DependencyGraph.
        /// </summary>
        [TestMethod]
        public void HasDependees()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("x", "y");
            Assert.IsTrue(t.HasDependees("y"));
            t.RemoveDependency("x", "y");
            Assert.IsFalse(t.HasDependees("y"));
            Assert.IsFalse(t.HasDependees("DoesNotExist"));
        }

        /// <summary>
        /// Checks the this method, with both an empty and a non-empty DependencyGraph.
        /// </summary>
        [TestMethod]
        public void thisTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("x", "y");
            Assert.AreEqual(1, t["y"]);
            t.RemoveDependency("x", "y");
            Assert.AreEqual(0, t["y"]);
            Assert.AreEqual(0, t["DoesNotExist"]);
        }

        /// <summary>
        /// Checks the size method, with multiple adds and removes, and using both an empty and a non-empty graph.
        /// </summary>
        [TestMethod]
        public void sizeTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("x", "y");
            t.AddDependency("b", "d");
            t.AddDependency("z", "a");
            Assert.AreEqual(3, t.Size);

            t.RemoveDependency("x", "y");
            t.RemoveDependency("b", "d");
            Assert.AreEqual(1, t.Size);
            t.RemoveDependency("z", "a");
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        /// Checks the edge case of an ordered pair where both dependent and dependee are the same string. Should work as normal.
        /// </summary>
        [TestMethod]
        public void AddAndRemoveSelfTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("x", "x");
            Assert.AreEqual(1, t.Size);
            Assert.IsTrue(t.HasDependees("x"));
            Assert.IsTrue(t.HasDependents("x"));

            t.RemoveDependency("x", "x");
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        /// Checks the edge case of an ordered pair where both dependent and dependee are the same string. Should work as normal.
        /// </summary>
        [TestMethod]
        public void GivenExampleTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "c");
            t.AddDependency("b", "d");
            t.AddDependency("d", "d");
            Assert.AreEqual(4, t.Size);
            Assert.IsTrue(t.HasDependents("a"));
            Assert.IsFalse(t.HasDependees("a"));
        }

        /// <summary>
        /// Checks the edge case of an ordered pair where one or both of an ordered pair is an empty string. Should work as normal.
        /// </summary>
        [TestMethod]
        public void EmptyStringTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "");
            t.AddDependency(" ", "");
            Assert.AreEqual(2, t.Size);
            Assert.IsTrue(t.HasDependents("a"));
            Assert.IsTrue(t.HasDependents(" "));
        }

        /// <summary>
        /// Checks the case of an empty dependents list. Should return an empty list.
        /// </summary>
        [TestMethod]
        public void GetDependentsFalseTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "");
            t.AddDependency(" ", "");
            IEnumerator<string> e = t.GetDependents("DoesNotExist").GetEnumerator();
            Assert.IsNull(e.Current);
        }

        /// <summary>
        /// Checks the case of an empty dependees list. Should return an empty list.
        /// </summary>
        [TestMethod]
        public void GetDependeesFalseTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "");
            t.AddDependency(" ", "");
            IEnumerator<string> e = t.GetDependees("DoesNotExist").GetEnumerator();
            Assert.IsNull(e.Current);
        }
    }
}
