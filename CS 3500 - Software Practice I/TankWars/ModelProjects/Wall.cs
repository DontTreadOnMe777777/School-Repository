// Written by Brandon Walters and Alysha Armstrong 11/15/19

// Walls for the TankWars game. They will always be axis-aligned (purely horizontal 
// or purely vertical, never diagonal) and can overlap and intersect eachother.


using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace TankWars
{
    /// <summary>
    /// Represents a wall in the TankWars Game.
    /// </summary>
    [JsonObject(MemberSerialization.OptIn)]
    public class Wall
    {
        /// <summary>
        /// ID of the wall
        /// </summary>
        [JsonProperty(PropertyName = "wall")]
        public int ID { get; private set; }

        /// <summary>
        /// a Vector2D representing one endpoint of the wall
        /// </summary>
        [JsonProperty(PropertyName = "p1")]
        public Vector2D point1 { get; private set; }

        /// <summary>
        /// a Vector2D representing one endpoint of the wall
        /// </summary>
        [JsonProperty(PropertyName = "p2")]
        public Vector2D point2 { get; private set; }


        public Wall()
        {
        }

        /// <summary>
        /// Create a new wall
        /// </summary>
        /// <param name="p1">Vector2D for one end of the wall</param>
        /// <param name="p2">Vector2D for the other end of the wall</param>
        /// <param name="id">ID of the wall</param>
        public Wall(Vector2D p1, Vector2D p2, int id)
        {
            ID = id;
            point1 = p1;
            point2 = p2;
        }
    }
}
