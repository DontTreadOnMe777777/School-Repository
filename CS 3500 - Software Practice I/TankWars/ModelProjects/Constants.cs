// Authors: Brandon Walters and Alysha Armstrong

using System;
using System.Collections.Generic;
using System.Text;

namespace TankWars
{
    /// <summary>
    /// Constants for TanksWars
    /// </summary>
    public class Constants
    {

        /// <summary>
        /// The size of the universe
        /// </summary>
        //public int UniverseSize { get; set; }
        public const int UniverseSize = 1200;

        public const int MaxHP = 3;

        /// <summary>
        /// Port address to connect to
        /// </summary>
        public const int Port = 11000;

        /// <summary>
        /// Height for drawing the tank
        /// </summary>
        public const int TankHeight = 60;
        /// <summary>
        /// Width for drawing the tank
        /// </summary>
        public const int TankWidth = 60;
        /// <summary>
        /// Width for drawing the turret
        /// </summary>
        public const int TurretWidth = 50;
        /// <summary>
        /// Height for drawing the turret
        /// </summary>
        public const int TurretHeight = 50;
        /// <summary>
        /// Thickeness of the hit points bar
        /// </summary>
        public const int HPBarHeight = 5;
        /// <summary>
        /// Length of the hit points bar when full health
        /// </summary>
        public const int HPFullBarWidth = 45;
        /// <summary>
        /// Height of a wall for drawing
        /// </summary>
        public const int WallHeight = 50;
        /// <summary>
        /// Width of a wall for drawing
        /// </summary>
        public const int WallWidth = 50;
        /// <summary>
        /// Width of a powerup for drawing
        /// </summary>
        public const int PowerupWidth = 8;
        /// <summary>
        /// Height of a powerup for drawing
        /// </summary>
        public const int PowerupHeight = 8;
        /// <summary>
        /// Projectile height for drawing
        /// </summary>
        public const int ProjectileHeight = 30;
        /// <summary>
        /// Projectile width for drawing
        /// </summary>
        public const int ProjectileWidth = 30;
        /// <summary>
        /// Width of explosion for drawing
        /// </summary>
        public const int ExplosionWidth = 60;
        /// <summary>
        /// Height of the explosion for drawing
        /// </summary>
        public const int ExplosionHeight = 60;

    }
}
