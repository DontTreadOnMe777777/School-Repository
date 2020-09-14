//Authors: Brandon Walters and Alysha Armstrong 11/17/19
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace TankWars
{
    /// <summary>
    /// Represents player movements in the TankWars game.
    /// </summary>
    [JsonObject(MemberSerialization.OptIn)]
    public class ControlCommand
    {
        /// <summary>
        /// whether the player wants to move or not, and the 
        /// desired direction. Possible values are: "none",
        /// "up", "left", "down", "right".
        /// </summary>
        [JsonProperty(PropertyName = "moving")]
        public string moving { get; private set; }

        [JsonProperty(PropertyName = "fire")]
        private string fire;

        [JsonProperty(PropertyName = "tdir")]
        private Vector2D dir;

        public ControlCommand()
        {
            moving = "none";
            fire = "none";
            dir = new Vector2D(0, -1);
        }

        public string GetFire()
        {
            return fire;
        }

        public Vector2D GetTurretDirection()
        {
            return dir;
        }

        /// <summary>
        /// Set whether the player wants to fire or not, and the desired type.
        /// </summary>
        /// <param name="fireString">Type of fire. Possible values are: "none",
        /// "main", (for a normal projectile) and "alt" (for a beam attack). </param>
        public void SetFire(string fireString)
        {
            fire = fireString;
        }

        /// <summary>
        /// Set whether the player wants to move or not, and the 
        /// desired direction.
        /// </summary>
        /// <param name="movingString">Possible values are: "none",
        /// "up", "left", "down", "right"</param>
        public void SetMoving(string movingString)
        {
            moving = movingString;
        }

        /// <summary>
        ///  a Vector2D representing where the player wants to aim their turret
        /// </summary>
        /// <param name="turretVector">direction</param>
        public void SetTurretDirection(Vector2D turretVector)
        {
            dir = turretVector;
        }
    }
}
