// Authors: Brandon Walters and Alysha Armstrong 
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace TankWars
{
    /// <summary>
    /// Represents a Beam in the TankWars game.
    /// </summary>
    [JsonObject(MemberSerialization.OptIn)]
    public class Beam
    {
        /// <summary>
        /// Beam ID
        /// </summary>
        [JsonProperty(PropertyName = "beam")]
        public int ID { get; private set; }

        /// <summary>
        /// Beam origin position in a 2D vector
        /// </summary>
        [JsonProperty(PropertyName = "org")]
        public Vector2D origin { get; private set; }

        /// <summary>
        /// Beam direction in a 2D vector
        /// </summary>
        [JsonProperty(PropertyName = "dir")]
        public Vector2D direction { get; private set; }

        /// <summary>
        /// ID for the owner of the beam
        /// </summary>
        [JsonProperty(PropertyName = "owner")]
        public int ownerID { get; private set;}

        public Beam()
        {

        }

        public void SetOrigin(Vector2D newOrigin)
        {
            origin = newOrigin;
        }

        public void SetDirection(Vector2D newDirection)
        {
            direction = newDirection;
        }
    }
}
