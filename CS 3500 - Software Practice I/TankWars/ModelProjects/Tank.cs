//Authors: Alysha Armstrong and Brandon Walters 11/13/19

using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace TankWars
{
    /// <summary>
    /// Represents a Tank in the TankWars Game
    /// </summary>
    [JsonObject(MemberSerialization.OptIn)]
    public class Tank
    {
        /// <summary>
        /// unique ID of the tank
        /// </summary>
        [JsonProperty(PropertyName = "tank")]
        public int ID { get; private set; }

        /// <summary>
        /// Location of the tank as a 2D vector
        /// </summary>
        [JsonProperty(PropertyName = "loc")]
        private Vector2D location;

        /// Orientation of the tank as a 2D vector
        [JsonProperty(PropertyName = "bdir")]
        private Vector2D orientation = new Vector2D(0, -1);

        /// a Vector2D representing the direction of the
        /// tank's turret (where it's aiming)
        [JsonProperty(PropertyName = "tdir")]
        private Vector2D aiming = new Vector2D(0, -1);

        /// <summary>
        /// Players name
        /// </summary>
        [JsonProperty(PropertyName = "name")]
        public string name { get; private set; }

        ///  The hit points of the tank
        [JsonProperty(PropertyName = "hp")]
        private int hitPoints = Constants.MaxHP;

        /// The players score
        [JsonProperty(PropertyName = "score")]
        private int score = 0;

        ///  If the tank died on that frame. This will only be true 
        ///  on the exact frame in which the tank died
        [JsonProperty(PropertyName = "died")]
        private bool died = false;

        // If the player disconnected
        [JsonProperty(PropertyName = "dc")]
        private bool disconnected = false;

        // If the player joined on this frame
        [JsonProperty(PropertyName = "join")]
        private bool joined = false;

        /// <summary>
        /// <summary>
        /// Create a new tank by specifying the unique 
        /// player id, its name, and its location
        /// </summary>
        /// <param name="id">Player id</param>
        /// <param name="playerName"></param>
        /// <param name="loc">Player location</param>
        public Tank(int id, string playerName, Vector2D loc)
        {
            ID = id;
            name = playerName;
            location = loc;
            joined = true;
        }

        Tank() { }

        public void SetName(string newName)
        {
            name = newName;
        }

        public void SetJoined(bool join)
        {
            joined = join;
        }

        public void SetID(int id)
        {
            ID = id;
        }

        /// <summary>
        /// Set disconnected to true, set HP to 0, and set died to 
        /// true when the player disconnects
        /// </summary>
        public void DiconnectPlayer()
        {
            disconnected = true;
            hitPoints = 0;
            died = true;
        }

    
        /// <summary>
        /// Return the players location as a 2D vector
        /// </summary>
        /// <returns></returns>
        public Vector2D GetLocation()
        {
            return location;
        }

        /// <summary>
        /// Retrun the tanks orientation as a 2D vector
        /// </summary>
        /// <returns></returns>
        public Vector2D GetOrientation()
        {
            return orientation;
        }

        /// <summary>
        /// Retrun the tanks orientation as a 2D vector
        /// </summary>
        /// <returns></returns>
        public void SetLocation(Vector2D newLocation)
        {
            location = newLocation;
        }

        /// <summary>
        /// Returns the tank's hit points
        /// </summary>
        /// <returns></returns>
        public int GetHP()
        {
            return hitPoints;
        }

        public void SetHP(int newHP)
        {
            hitPoints = newHP;
        }

        public void SetOrientation(Vector2D newOrient)
        {
            orientation = newOrient;
        }

        /// <summary>
        /// True if the tank was disconnected on this frame
        /// </summary>
        /// <returns></returns>
        public bool IsDisconnected()
        {
            return disconnected;
        }

        /// <summary>
        /// Returns the player's score
        /// </summary>
        /// <returns></returns>
        public int GetScore()
        {
            return score;
        }

        public void SetScore(int newScore)
        {
            score = newScore;
        }

        /// <summary>
        /// True if the player died on this frame
        /// </summary>
        /// <returns></returns>
        public bool IsDead()
        {
            return died;
        }

        public void SetDead(bool dead)
        {
            died = dead;
        }

        /// <summary>
        /// Returns a Vector2D representing the direction of the
        /// tank's turret (where it's aiming)
        /// </summary>
        /// <returns></returns>
        public Vector2D GetAiming()
        {
            return aiming;
        }

        /// <summary>
        /// Set the Vector2D representing the direction of the
        /// tank's turret (where it's aiming)
        /// </summary>
        /// <param name="aimVector"></param>
        public void SetAiming(Vector2D aimVector)
        {
            aiming = aimVector;
        }
    }
}
