// Authors: Alysha Armstrong and Brandon Walters 11/13/19

using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace TankWars
{
    /// <summary>
    /// Represents a Projectile in the TankWars game
    /// </summary>
    [JsonObject(MemberSerialization.OptIn)]
    public class Projectile
    {
        /// <summary>
        /// unique ID of the projectile
        /// </summary>
        [JsonProperty(PropertyName = "proj")]
        public int ID{ get; private set; }

        /// <summary>
        /// location of the projectile as a 2D vector
        /// </summary>
        [JsonProperty(PropertyName = "loc")]
        public Vector2D location { get; private set; }

        /// <summary>
        /// Orientation of the projectile as a 2D vector
        /// </summary>
        [JsonProperty(PropertyName = "dir")]
        public Vector2D orientation { get; private set; }


        /// If the projectile died on this frame 
        [JsonProperty(PropertyName = "died")]
        private bool died = false;

        /// <summary>
        /// Owner of the projectiles ID
        /// </summary>
        [JsonProperty(PropertyName = "owner")]
        public int ownerID { get; private set; }

        public Projectile()
        {

        }

        /// <summary>
        ///True if the projectile died on this frame 
        /// </summary>
        /// <returns></returns>
        public bool IsDead()
        {
            return died;
        }

        /// <summary>
        /// Used to set the dead property
        /// </summary>
        /// <returns></returns>
        public void SetDead(bool dead)
        {
            died = dead;
        }

        public void SetLocation(Vector2D newLocation)
        {
            location = newLocation;
        }

        public void SetOrientation(Vector2D newOrient)
        {
            orientation = newOrient;
        }
    }
}
