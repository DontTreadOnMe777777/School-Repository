// Written by Brandon Walters and Alysha Armstrong 11/13/19

using System;
using System.Collections.Generic;
using System.Net.Sockets;

namespace TankWars
{
    /// <summary>
    /// A container for the objects in the TankWars game
    /// </summary>
    public class World
    {
        // Stores all the tanks in the game
        private Dictionary<int, Tank> tanks;
        // stores all the powerups in the game
        private Dictionary<int, PowerUp> powerups;
        // stores all the beams in the game
        private Dictionary<int, Beam> beams;
        //  stores all the walls in the game
        private Dictionary<int, Wall> walls;
        // stores all the projectiles in the game
        private Dictionary<int, Projectile> projectiles;
        // stores all the control commands in the game, for the server
        private Dictionary<int, ControlCommand> controlCommands;

        /// Just for the Server

        // stores control commands 

        private Dictionary<int, bool> canFireBeam;
        private ControlCommand controlCommand;

        private Dictionary<int,double> shotsHitByPlayer;
        private Dictionary<int, double> shotsTakenByPlayer;



        /// <summary>
        /// The size of the world sent from the server
        /// </summary>
        public int WorldSize { get; set; }
        // If the world size was sent from the server
        public bool WorldSizeReceived = false;
        // Player ID sent from the server
        private int playerID;


        public World()
        {
            tanks = new Dictionary<int, Tank>();
            powerups = new Dictionary<int, PowerUp>();
            beams = new Dictionary<int, Beam>();
            walls = new Dictionary<int, Wall>();
            projectiles = new Dictionary<int, Projectile>();
            controlCommands = new Dictionary<int, ControlCommand>();
            canFireBeam = new Dictionary<int, bool>();
            shotsHitByPlayer = new Dictionary<int, double>();
            shotsTakenByPlayer = new Dictionary<int, double>();

            controlCommand = new ControlCommand();
        }

        /// <summary>
        /// Set the 
        /// </summary>
        /// <param name="id">player id</param>
        public void SetPlayerID(int id)
        {
            playerID = id;
        }

        /// <summary>
        /// Return the player ID
        /// </summary>
        /// <returns></returns>
        public int GetPlayerID()
        {
            return playerID;
        }

        /// <summary>
        /// Add a new wall or update an existing wall in the game
        /// </summary>
        /// <param name="id">wall id</param>
        /// <param name="wall">wall to add</param>
        public void AddOrUpdateWall(int id, Wall wall)
        {
            if (walls.ContainsKey(id))
                walls[id] = wall;
            else
                walls.Add(id, wall);
        }

        /// <summary>
        /// Add a new tank to the game or update an existing tank. If the tank 
        /// is disconnected remove it from the game. 
        /// </summary>
        /// <param name="id">tank id</param>
        /// <param name="t">tank to add</param>
        public void AddOrUpdateTank(int id, Tank t)
        {
            if (tanks.ContainsKey(id))
            {
                if (t.IsDisconnected())
                    tanks.Remove(id);
                else
                    tanks[id] = t;
            }
            else
            {
                if (t.GetHP() != 0)
                    tanks.Add(id, t);
            }
        }

        /// Add a new tank to the game or update an existing tank. If the tank 
        /// is disconnected remove it from the game. 
        /// </summary>
        /// <param name="id">tank id</param>
        /// <param name="t">tank to add</param>
        public void AddOrUpdateServerTank(int id, Tank t)
        {
            if (tanks.ContainsKey(id))
            {
                tanks[id] = t;
            }
            else
            {
                tanks.Add(id, t);
            }
        }

        /// <summary>
        /// Add a new projectile to the game or update an existing one.
        /// If the project has died, remove it from the game.
        /// </summary>
        /// <param name="id">projectile id</param>
        /// <param name="p">projectile</param>
        public void AddOrUpdateProjectile(int id, Projectile p)
        {
            if (projectiles.ContainsKey(id))
            {
                if (p.IsDead())
                    projectiles.Remove(id);
                else
                    projectiles[id] = p;
            }
            else
            {
                if (!p.IsDead())
                    projectiles.Add(id, p);
            }
        }

        /// <summary>
        /// Add a beam to the game
        /// </summary>
        /// <param name="id">beam id</param>
        /// <param name="b">beam to add</param>
        public void AddBeam(int id, Beam b)
        {
            if (beams.ContainsKey(id))
                beams[id] = b;
            else
                beams.Add(id, b);
        }

        /// <summary>
        /// Add the permission to fire a beam to the game
        /// </summary>
        /// <param name="id">beam id</param>
        /// <param name="b">beam to add</param>
        public void AddCanFireBeam(int id)
        {
            lock (canFireBeam)
            {
                canFireBeam.TryGetValue(id, out bool canFire);
                if (!canFire)
                {
                    canFireBeam.Add(id, true);
                }
            }
        }

        /// <summary>
        /// Add a powerup to the game. If an existing powerup
        /// has died, remove it
        /// </summary>
        /// <param name="id">powerup id</param>
        /// <param name="p">powerup to add</param>
        public void AddOrUpdatePowerup(int id, PowerUp p)
        {
            if (powerups.ContainsKey(id))
            {
                if (p.IsDead())
                    powerups.Remove(id);
                else
                    powerups[id] = p;
            }
            else
            {
                if (!p.IsDead())
                    powerups.Add(id, p);
            }
        }

        public Tank GetPlayer(int id)
        {
            if (tanks.TryGetValue(id, out Tank t))
            {
                return t;
            }

            return null;
        }

        /// <summary>
        /// Add a powerup to the game. If an existing powerup
        /// has died, remove it
        /// </summary>
        /// <param name="id">powerup id</param>
        /// <param name="p">powerup to add</param>
        public void AddOrUpdateControlCommand(int id, ControlCommand c)
        {
            if (controlCommands.ContainsKey(id))
            {
                controlCommands[id] = c;
            }
            else
            {
                controlCommands.Add(id, c);
            }
        }

        /// <summary>
        /// Return the walls in the game
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, Wall> GetWalls()
        {
            return walls;
        }

        /// <summary>
        /// Return the tanks in the game
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, Tank> GetTanks()
        {
            return tanks;
        }

        /// <summary>
        /// Return the porjectiles in the game
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, Projectile> GetProjectiles()
        {
            return projectiles;
        }

        /// <summary>
        /// Return the powerups in the game
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, PowerUp> GetPowerups()
        {
            return powerups;
        }

        /// <summary>
        /// Return the beams in the game
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, Beam> GetBeams()
        {
            return beams;
        }

        /// <summary>
        /// Return the control commands in the game
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, ControlCommand> GetControlCommands()
        {
            return controlCommands;
        }

        /// <summary>
        /// Return who can currently fire beams in the game
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, bool> GetCanFireBeam()
        {
            return canFireBeam;
        }

        /// <summary>
        /// Return the hit shots in the game
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, double> GetShotsHitByPlayer()
        {
            return shotsHitByPlayer;
        }

        /// <summary>
        /// Used to add a hit shot
        /// </summary>
        /// <param name="id"></param>
        public void addShotHit(int id)
        {
            shotsHitByPlayer.TryGetValue(id, out double oldShotsHit);
            if (shotsHitByPlayer.ContainsKey(id))
            {
                shotsHitByPlayer[id] = oldShotsHit + 1;
            }
            else
            {
                shotsHitByPlayer.Add(id, 1);
            }
        }

        /// <summary>
        /// Return all shots fired in the game
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, double> GetShotsTakenByPlayer()
        {
            return shotsTakenByPlayer;
        }

        /// <summary>
        /// Used to add a shot taken
        /// </summary>
        /// <param name="id"></param>
        public void addShotTaken(int id)
        {
            shotsTakenByPlayer.TryGetValue(id, out double oldShots);
            if (shotsTakenByPlayer.ContainsKey(id))
            {
                shotsTakenByPlayer[id] = oldShots + 1;
            }
            else
            {
                shotsTakenByPlayer.Add(id, 1);
            }
        }

        /// <summary>
        /// Remove a beam from the game
        /// </summary>
        /// <param name="beam">beam to remove</param>
        public void RemoveBeam(Beam beam)
        {
            beams.Remove(beam.ID);
        }

        public void RemoveProjectile(Projectile p)
        {
            projectiles.Remove(p.ID);
        }

        public void RemovePowerup(PowerUp p)
        {
            powerups.Remove(p.ID);
        }

        public void RemoveCanFireBeam(int tankID)
        {
            canFireBeam.Remove(tankID);
        }

        public void RemoveTank(Tank t)
        {
            tanks.Remove(t.ID);
        }


        /// <summary>
        /// Set whether the player wants to move or not, and the desired direction. 
        /// </summary>
        /// <param name="movingString">Possible values are: "none", "up", "left", "down", "right"</param>
        public void SetCommandMoving(string movingString)
        {
            controlCommand.SetMoving(movingString);
        }

        /// <summary>
        ///  Set whether the player wants to fire or not, and the desired type. 
        /// </summary>
        /// <param name="fireString">Possible values are: "none", "main",
        /// (for a normal projectile) and "alt" (for a beam attack).</param>
        public void SetCommandFire(string fireString)
        {
            controlCommand.SetFire(fireString);
        }

        /// <summary>
        /// Set  where the player wants to aim their turret
        /// </summary>
        /// <param name="turretDirection">normalized 2D vector</param>
        public void SetCommandTurret(Vector2D turretDirection)
        {
            turretDirection.Normalize();
            tanks.TryGetValue(playerID, out Tank result);
            if (result != null)
            {
                result.SetAiming(turretDirection);
            }
            controlCommand.SetTurretDirection(turretDirection);
        }

        /// <summary>
        /// Return the control commands
        /// </summary>
        /// <returns></returns>
        public ControlCommand GetControlCommand()
        {
            return controlCommand;
        }
    }
}
