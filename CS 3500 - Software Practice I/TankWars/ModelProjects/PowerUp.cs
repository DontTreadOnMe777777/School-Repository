//Authors: Brandon Walters and Alysha Armstrong 11/15/19

using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace TankWars
{
    /// <summary>
    /// Represents a powerup in the TankWars game
    /// </summary>
    [JsonObject(MemberSerialization.OptIn)]
    public class PowerUp
    {
        /// <summary>
        /// ID of the powerup
        /// </summary>
        [JsonProperty(PropertyName = "power")]
        public int ID { get; private set; }

        /// <summary>
        /// Location of the powerup in the game as a 2D vector
        /// </summary>
        [JsonProperty(PropertyName = "loc")]
        public Vector2D origin { get; private set; }

        /// Indicates if the powerup has been collected
        [JsonProperty(PropertyName = "died")]
        private bool died = false;

        public PowerUp() {}

        /// <summary>
        /// Returns true if the powerup has been collected by a player,
        /// false if it has not.
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

        /// <summary>
        /// Used to set the dead property
        /// </summary>
        /// <returns></returns>
        public void SetOrigin(Vector2D newOri)
        {
            origin = newOri;
        }
    }
}
