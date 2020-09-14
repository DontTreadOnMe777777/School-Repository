using NetworkUtil;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net.Sockets;
using System.Text.RegularExpressions;
using System.Timers;
using System.Xml;
using TankWars;

namespace Server
{
    public class Controller
    {
        // The world, which contains all of the data needed to inform the clients on the state of the game
        private World theWorld;
        // All of the connected clients sockets
        public Dictionary<int, Socket> clientSockets;
        // Update string that is sent to each of the clients
        private string updateMessage = "";
        // Value used to determine when to attempt spawning a new powerup
        private bool canPlaceNewPowerup = true;
        // Value used to determine when the start of the server-client connection is completed
        private bool startup = true;
        // Tank stored for respawning
        private Tank tankToRespawn;
        // Random used to generate random numbers for respawning
        Random rand = new Random();
        // Counter used to track the game-time in milliseconds
        private int timeInMilliseconds = 0;

        /// <summary>
        /// Timer for replacing powerups
        /// </summary>
        private System.Timers.Timer powerupTimer = new System.Timers.Timer();

        /// <summary>
        /// Timer for respawning tanks
        /// </summary>
        private System.Timers.Timer tankTimer = new System.Timers.Timer(3000);

        // Used as settings from the XML settings file
        int RespawnRate;
        int ProjectileSpeed;
        double EngineStrength;
        int MaxPowerups;
        int MaxPowerupDelay;
        int UniverseSize;
        int MaxHP;
        int FramesPerShot;
        public int MSPerFrame;

        public Controller()
        {
            // Creates our world, reads the settings from the XML file
            theWorld = new World();
            ReadSettings("../../../../Resources/settings.xml");
            // Creates the list to store all clients
            clientSockets = new Dictionary<int, Socket>();

            // Sets p=up the respawn timers
            powerupTimer.Enabled = false;
            tankTimer.Enabled = false;

            tankTimer.Elapsed += (sender, e) => TankTimer_Elapsed(sender, e, tankToRespawn);
            tankTimer.Elapsed += (sender, e) => PowerupTimer_Elapsed(sender, e);
        }

        /// <summary>
        /// Return the world being used by the controller
        /// </summary>
        /// <returns></returns>
        public World GetWorld()
        {
            return theWorld;
        }
        /// <summary>
        /// Returns the time of the server in seconds, for the database
        /// </summary>
        /// <returns></returns>
        public int returnTimeInSeconds()
        {
            return timeInMilliseconds / 1000;
        }

        /// <summary>
        /// Returns the accuracy of a given player
        /// </summary>
        /// <returns></returns>
        public int returnPlayerAccuracy(int id)
        {
            theWorld.GetShotsTakenByPlayer().TryGetValue(id, out double shotsTaken);
            theWorld.GetShotsHitByPlayer().TryGetValue(id, out double shotsHit);

            return (int)(shotsHit / shotsTaken) * 100;
        }

        /// <summary>
        /// Triggered whenn the powerup respawn timer is hit
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void PowerupTimer_Elapsed(object sender, ElapsedEventArgs e)
        {
            powerupTimer.Stop();
            PowerUp powerup = new PowerUp();
            powerup.SetOrigin(GeneratRandomSpawnLocation());
            theWorld.AddOrUpdatePowerup(powerup.ID, powerup);
        }

        /// <summary>
        /// Triggered whenn the tank respawn timer is hit
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void TankTimer_Elapsed(object sender, ElapsedEventArgs e, Tank tankToRespawn)
        {
            tankTimer.Stop();
            RespawnTank(tankToRespawn);
        }

        /// <summary>
        /// Used to respawn a tank after the timer has elapsed
        /// </summary>
        /// <param name="tank"></param>
        private void RespawnTank(Tank tank)
        {
            lock (theWorld) 
            {
                // Randomly assigns the tank a spawn location
                tank.SetLocation(GeneratRandomSpawnLocation());

                // Sets the tank's HP back to max, which tells the clients that it's alive
                tank.SetHP(Constants.MaxHP);
            }
        }

        // Returns all the clients connected to this server
        public Dictionary<int, Socket> returnClients()
        {
            return clientSockets;
        }

        /// <summary>
        /// Infinte loop to update the world as often as specified by MSPerFrame
        /// in the settings file
        /// </summary>
        public void UpdateLoop()
        {
            // Uses a stopwatch to determine when to update the world, keeping track of a "frame"
            Stopwatch watch = new Stopwatch();
            watch.Start();
            while (true)
            {
                while (watch.ElapsedMilliseconds < MSPerFrame) { }
                timeInMilliseconds += MSPerFrame;
                watch.Restart();
                Networking.StartServer(HandleNewClient, 11000);

                lock (theWorld)
                {
                    foreach (int id in clientSockets.Keys)
                    {
                        if (clientSockets.TryGetValue(id, out Socket s))
                        {
                            // If any clients are disconnected, remove them from the list of clients and then remove their tank from the game
                            if (!s.Connected)
                            {
                                Tank t = theWorld.GetPlayer(id);
                                t.DiconnectPlayer();
                                theWorld.AddOrUpdateServerTank(id, t);
                                clientSockets.Remove(id);
                                s.Close();
                            }
                        }
                    }
                    // Otherwise, updates each client that is connected with the new world
                    foreach (Socket s in clientSockets.Values)
                    {
                        if (s.Connected)
                        {
                            SendUpdate(s);
                        }
                    }
                    // Calls a cleanup method to remove all dead objects
                    CleanupWorld();
                }
            }
        }


        /// <summary>
        // Delegate callback passed to the networking class to handle a new client connecting.
        // Changes the callback for the socket state to a new method that receives the player's name, 
        // then asks for data.
        /// </summary>
        /// <param name="s">the socketstate from the Network Controller</param>
        public void HandleNewClient(SocketState s)
        {
            // If there was an error connecting, close the socket
            if (s.ErrorOccured)
                s.TheSocket.Close();

            // Change the callback 
            s.OnNetworkAction = new Action<SocketState>(RecievePlayerName);
            Networking.GetData(s);
        }

        /// <summary>
        // Delegate that implements the server's part of the initial handshake.
        // Make a new Tank with the given name and a new unique ID (recommend using the SocketState's ID). 
        // Then change the callback to a method that handles command requests from the client. 
        // Then send the startup info to the client. Then add the client's socket to a list of all clients. 
        // Then ask the client for data. 
        /// </summary>
        /// <param name="s">socket state for client</param>
        private void RecievePlayerName(SocketState s)
        {
            // If there was an error connecting, close the socket
            if (s.ErrorOccured)
                s.TheSocket.Close();
            // recieve the players name
            string name = null;
            while (name == null)
                name = ParsePlayerName(s);
            // Create a new tank and gives it a random location to spawn at
            Vector2D randLoc = GeneratRandomSpawnLocation();
            Tank newPlayer = new Tank((int)s.ID, name, randLoc);
            newPlayer.SetJoined(true);
            theWorld.AddOrUpdateTank(newPlayer.ID, newPlayer);
            // Change the callback 
            s.OnNetworkAction = new Action<SocketState>(HandleClientData);
            // Send the startup data - player id, universesize, walls
            SendStartup(s);
            // Add the client's socket
            clientSockets.Add((int)s.ID, s.TheSocket);

            Networking.GetData(s);
        }

        /// <summary>
        /// Delegate for processing client direction commands. 
        /// Process the command, then ask for more data.
        /// </summary>
        /// <param name="s">socket state for client</param>
        private void HandleClientData(SocketState s)
        {
            if (s.ErrorOccured)
            {
                s.TheSocket.Close();
            }
            else
            {
                ParseClientMessages(s);
                Networking.GetData(s);
            }
        }

        /// <summary>
        /// Parse the messages from the client and then call a
        /// method to proccess them
        /// </summary>
        /// <param name="state">socket state for client</param>
        private string ParsePlayerName(SocketState state)
        {
            string totalData = state.GetData();
            string[] parts = Regex.Split(totalData, @"(?<=[\n])");
            List<string> messages = new List<string>();

            // Loop until we have processed all messages.
            // We may have received more than one
            foreach (string p in parts)
            {
                // Ignore empty strings added by the regex splitter
                if (p.Length == 0)
                    continue;
                // The regex splitter will include the last string even if it doesn't end with a '\n',
                // So we need to ignore it if this happens. 
                if (p[p.Length - 1] != '\n')
                    break;

                state.RemoveData(0, p.Length);
                // Return the players name
                return p.Substring(0, p.Length - 1);
            }
            return null;
        }

        /// <summary>
        /// Send the startup information to the client. First, send 
        /// the player's unique id. Then, send the UniverseSize. Then send
        /// the walls since they are only sent once.
        /// </summary>
        /// <param name="state">socket state for client</param>
        private void SendStartup(SocketState state)
        {
            // Send the player id
            Networking.Send(state.TheSocket, state.ID.ToString() + "\n");
            // Send the UniverseSize
            Networking.Send(state.TheSocket, Constants.UniverseSize.ToString() + "\n");
            // Send each wall
            foreach (Wall wall in theWorld.GetWalls().Values)
            {
                updateMessage = updateMessage + JsonConvert.SerializeObject(wall) + "\n";
            }

            Networking.Send(state.TheSocket, updateMessage);
            updateMessage = "";
        }

        /// <summary>
        /// Method used to create the update string to be sennt to each client, informing them on the state of the world
        /// </summary>
        /// <param name="state"></param>
        public void SendUpdate(Socket state)
        {
            lock (theWorld)
            {
                foreach (Tank t in theWorld.GetTanks().Values)
                {
                    updateMessage = updateMessage + JsonConvert.SerializeObject(t) + "\n";
                }

                foreach (Projectile p in theWorld.GetProjectiles().Values)
                {
                    updateMessage = updateMessage + JsonConvert.SerializeObject(p) + "\n";
                }

                foreach (PowerUp p in theWorld.GetPowerups().Values)
                {
                    updateMessage = updateMessage + JsonConvert.SerializeObject(p) + "\n";
                }

                foreach (Beam b in theWorld.GetBeams().Values)
                {
                    updateMessage = updateMessage + JsonConvert.SerializeObject(b) + "\n";
                }

                Networking.Send(state, updateMessage);
                updateMessage = "";
            }
        }

        /// <summary>
        /// Removes all beams, resets each tank back to alive, and removes all other objects with the dead tag.
        /// </summary>
        private void CleanupWorld()
        {
            lock (theWorld)
            {
                foreach (Tank t in theWorld.GetTanks().Values)
                {
                    if (t.IsDead())
                    {
                        t.SetDead(false);
                    }
                }

                foreach (Projectile p in theWorld.GetProjectiles().Values)
                {
                    if (p.IsDead())
                    {
                        theWorld.RemoveProjectile(p);
                    }
                }

                foreach (PowerUp p in theWorld.GetPowerups().Values)
                {
                    if (p.IsDead())
                    {
                        theWorld.RemovePowerup(p);
                    }
                }

                foreach (Beam b in theWorld.GetBeams().Values)
                {
                    theWorld.RemoveBeam(b);
                }
            }
        }

        /// <summary>
        /// Parse the messages from the client and then call a
        /// method to proccess the last one - only one message per
        /// client should be processed on each frame
        /// </summary>
        /// <param name="state">clients socket state</param>
        private void ParseClientMessages(SocketState state)
        {
            string totalData = state.GetData();
            string[] parts = Regex.Split(totalData, @"(?<=[\n])");
            // the last message recieved from a client - to update the world with
            string message = null;

            // Loop until we have processed all messages.
            // We may have received more than one
            foreach (string p in parts)
            {
                // Ignore empty strings added by the regex splitter
                if (p.Length == 0)
                    continue;
                // The regex splitter will include the last string even if it doesn't end with a '\n',
                // So we need to ignore it if this happens. 
                if (p[p.Length - 1] != '\n')
                    break;

                message = p;

                state.RemoveData(0, p.Length);
            }

            // If a control command was received, process it
            if (message != null)
            {
                ProcessCommandControls(state, message);
            }
            // Update the world to send to each client
            UpdateWorld();
        }


        /// <summary>
        /// Process a JSON message and add it to or update it in the World 
        /// </summary>
        /// <param name="message">JSON message for the object to add to the server</param>
        private void ProcessCommandControls(SocketState state, string message)
        {
            lock (theWorld)
            {
                JObject json = JObject.Parse(message);
                // Store the control command so we can use it to update the player's tank
                if (json["moving"] != null)
                {
                    ControlCommand command = JsonConvert.DeserializeObject<ControlCommand>(message);
                    theWorld.AddOrUpdateControlCommand((int)state.ID, command);
                }
                else
                {

                }
            }
        }

        /// <summary>
        /// Generate a random postion in the world that is not colliding 
        /// with anything else
        /// </summary>
        /// <returns></returns>
        private Vector2D GeneratRandomSpawnLocation()
        {
            bool toContinue;
            while (true)
            {
                toContinue = false;
                // Generate a random location
                int xRand = rand.Next(-UniverseSize / 2, UniverseSize / 2);
                int yRand = rand.Next(-UniverseSize / 2, UniverseSize / 2);

                // Check if this location intersects any walls
                foreach (Wall wall in theWorld.GetWalls().Values)
                {
                    double y1;
                    double y2;
                    double x1;
                    double x2;

                    if (wall.point1.GetX() == wall.point2.GetX())
                    {
                        // Get the starting point and end point of the wall
                        if (wall.point1.GetY() < wall.point2.GetY())
                        {
                            y1 = wall.point1.GetY() - 0.5 *( Constants.WallHeight - Constants.TankHeight);
                            y2 = wall.point2.GetY() + 0.5 *( Constants.WallHeight + Constants.TankHeight);
                        }
                        else
                        {
                            y1 = wall.point2.GetY() - 0.5 *( Constants.WallHeight - Constants.TankHeight);
                            y2 = wall.point1.GetY() + 0.5 * (Constants.WallHeight + Constants.TankHeight);
                        }

                        x1 = wall.point2.GetX() - 0.5 *(Constants.WallHeight - Constants.TankHeight);
                        x2 = wall.point2.GetX() + 0.5 * (Constants.WallHeight + Constants.TankHeight);

                    }
                    // wall is horizontal 
                    else
                    {
                        if (wall.point1.GetX() < wall.point2.GetX())
                        {
                            x1 = wall.point1.GetX() - 0.5 * (Constants.WallHeight - Constants.TankHeight);
                            x2 = wall.point2.GetX() + 0.5 * (Constants.WallHeight + Constants.TankHeight);
                        }
                        else
                        {
                            x1 = wall.point2.GetX() - 0.5 * (Constants.WallHeight - Constants.TankHeight);
                            x2 = wall.point1.GetX() + 0.5 * (Constants.WallHeight + Constants.TankHeight);
                        }

                        y1 = wall.point2.GetY() - 0.5 * (Constants.WallHeight - Constants.TankHeight);
                        y2 = wall.point2.GetY() + 0.5 * (Constants.WallHeight + Constants.TankHeight);
                    }

                    if (xRand > x1 && xRand < x2 && yRand > y1 && yRand < y2)
                    {
                        toContinue = true;
                        break;
                    }
                }

                // If locations intersect, generate a new location and check again
                if (toContinue)
                    continue;

                //Check if this location intersects with any tanks
                foreach (Tank tank in theWorld.GetTanks().Values)
                {
                    double x = tank.GetLocation().GetX();
                    double y = tank.GetLocation().GetY();

                    if (xRand > x - Constants.TankHeight && xRand < x + Constants.TankHeight &&
                        yRand > y - Constants.TankHeight && yRand < y + Constants.TankHeight)
                    {
                        toContinue = true;
                        break;
                    }
                }
                // If locations intersect, generate a new location and check again
                if (toContinue)
                    continue;

                return new Vector2D((double)xRand, (double)yRand);
            }
        }

        /// <summary>
        /// This method does all of the math, updating each object in the server according to the status of the world and the control commands given by each client
        /// </summary>
        private void UpdateWorld()
        {

            // Update all projectiles
            lock (theWorld)
            {
                foreach (Projectile p in theWorld.GetProjectiles().Values)
                {
                    // Gets the vector that should be used to calculate the projectile's new location
                    Vector2D vector = p.orientation;
                    vector = vector * ProjectileSpeed;

                    // World boundary check
                    if (Math.Abs(p.location.GetX()) > UniverseSize/2 || Math.Abs(p.location.GetY()) > UniverseSize/2)
                    {
                        p.SetDead(true);
                    }
                    // Checks for collision with walls
                    foreach (Wall w in theWorld.GetWalls().Values)
                    {
                        if (WallCheckProjectile(w, p))
                        {
                            p.SetDead(true);
                        }
                    }
                    // Checks for collision with tanks
                    foreach (Tank t in theWorld.GetTanks().Values)
                    {
                        if ((p.location - t.GetLocation()).Length() < 30)
                        {
                            if (p.ownerID != t.ID)
                            {
                                // Update the tank that was hit
                                theWorld.GetTanks().TryGetValue(p.ownerID, out Tank tankToScore);

                                p.SetDead(true);

                                // Adds a hit to the shooter's accuracy
                                theWorld.addShotHit(p.ownerID);

                                tankToScore.SetHP(tankToScore.GetHP() - 1);

                                // If the tank died to this projectile, give the shooter a point and prepare to respawn the dead tank
                                if (tankToScore.GetHP() == 0)
                                {
                                    tankToScore.SetDead(true);
                                    tankToRespawn = tankToScore;
                                    tankTimer.Start();
                                    t.SetScore(t.GetScore() + 1);
                                }
                            }
                        }
                    }
                    // If there were no collision, update the projectile give it its new location
                    p.SetLocation(p.location + vector);
                    vector = new Vector2D(0, 0);
                }
            }

            // Update the players
            lock (theWorld)
            {
                foreach (Tank tank in theWorld.GetTanks().Values)
                {
                    Vector2D vector = new Vector2D(0, 0);

                    // If there was a control command sent from this client, read it and update the world accordingly
                    if (theWorld.GetControlCommands().TryGetValue(tank.ID, out ControlCommand command))
                    {

                        if (command.moving == "left")
                        {
                            vector = new Vector2D(-1, 0);
                            tank.SetOrientation(vector);
                            vector = vector * EngineStrength;
                        }

                        if (command.moving == "right")
                        {
                            vector = new Vector2D(1, 0);
                            tank.SetOrientation(vector);
                            vector = vector * EngineStrength;
                        }

                        if (command.moving == "up")
                        {
                            vector = new Vector2D(0, -1);
                            tank.SetOrientation(vector);
                            vector = vector * EngineStrength;
                        }

                        if (command.moving == "down")
                        {
                            vector = new Vector2D(0, 1);
                            tank.SetOrientation(vector);
                            vector = vector * EngineStrength;
                        }

                        // Set the aim to the new direction specified by the client
                        tank.SetAiming(command.GetTurretDirection());

                        // Create a projectile or beam if the control command says to
                        if (command.GetFire() == "main")
                        {
                            Projectile newProj = new Projectile();
                            newProj.SetLocation(tank.GetLocation());
                            newProj.SetOrientation(tank.GetAiming());
                            theWorld.AddOrUpdateProjectile(tank.ID, newProj);
                            // Updates the shooter's accuracy
                            theWorld.addShotTaken(newProj.ownerID);
                        }

                        if (command.GetFire() == "alt" && theWorld.GetCanFireBeam().TryGetValue(tank.ID, out bool canFire))
                        {
                            Beam newBeam = new Beam();
                            newBeam.SetOrigin(tank.GetLocation());
                            newBeam.SetDirection(tank.GetAiming());
                            theWorld.AddBeam(tank.ID, newBeam);
                        }
                    }

                    // If the tank disconnected on this frame, remove the tank from the world
                    if (tank.IsDisconnected())
                    {
                        theWorld.RemoveTank(tank);
                    }

                    // World size check
                    if (Math.Abs(tank.GetLocation().GetX()) > UniverseSize / 2)
                    {
                        tank.SetLocation(new Vector2D(tank.GetLocation().GetX() * -1, tank.GetLocation().GetY()));
                    }

                    else if (Math.Abs(tank.GetLocation().GetY()) > UniverseSize / 2)
                    {
                        tank.SetLocation(new Vector2D(tank.GetLocation().GetX(), tank.GetLocation().GetY() * -1));
                    }

                    // Check for a collision with a wall
                    foreach (Wall w in theWorld.GetWalls().Values)
                    {
                        Vector2D oldTankLoc = tank.GetLocation();
                        tank.SetLocation(tank.GetLocation() + vector);
                        if (WallCheckTank(w, tank))
                        {
                            vector = new Vector2D(0, 0);
                        }
                        tank.SetLocation(oldTankLoc);
                    }

                    // Check if the tank has picked up (collided with) a powerup
                    foreach (PowerUp p in theWorld.GetPowerups().Values)
                    {
                        if ((p.origin - tank.GetLocation()).Length() < 30)
                        {
                            p.SetDead(true);
                            // Tell the world that a beam can be fired from this tank
                            theWorld.AddCanFireBeam(tank.ID);
                        }
                    }
                    // Set the tank's new location
                    tank.SetLocation(tank.GetLocation() + vector);
                    vector = new Vector2D(0, 0);
                }
            }

            // Update the powerups
            lock (theWorld)
            {
                // On spawn, create the two new powerups and add them to the world
                if (theWorld.GetPowerups().Values.Count < MaxPowerups && startup)
                {
                    PowerUp powerup = new PowerUp();
                    powerup.SetOrigin(GeneratRandomSpawnLocation());

                    PowerUp powerup2 = new PowerUp();
                    powerup2.SetOrigin(GeneratRandomSpawnLocation());

                    theWorld.AddOrUpdatePowerup(powerup.ID, powerup);
                    theWorld.AddOrUpdatePowerup(powerup2.ID, powerup2);

                    startup = false;
                }

                foreach (PowerUp p in theWorld.GetPowerups().Values)
                {
                    // If a projectile is dead, start the respawn timer to create a new one
                    if (p.IsDead())
                    {
                        Random rnd = new Random();
                        powerupTimer = new System.Timers.Timer(rnd.Next(0, MaxPowerupDelay));
                        powerupTimer.Start();
                    }
                }
            }

            // Update the beams
            lock (theWorld)
            {
                foreach (Beam b in theWorld.GetBeams().Values)
                {
                    foreach (Tank t in theWorld.GetTanks().Values)
                    {
                        // If a tank is hit by the beam, kill the tank, give the shooter 1 point, and start the respawn timer for the dead tank
                        if (Intersects(b.origin, b.direction, t.GetLocation(), 30))
                        {
                            if (b.ownerID != t.ID)
                            {
                                theWorld.GetTanks().TryGetValue(b.ownerID, out Tank tankToScore);

                                tankToScore.SetHP(0);
                                tankToScore.SetDead(true);
                                tankToRespawn = tankToScore;
                                tankTimer.Start();
                                t.SetScore(t.GetScore() + 1);
                            }
                        }
                    }
                    // Since the beam has been fired, remove a charge from the tank that fired it
                    theWorld.RemoveCanFireBeam(b.ownerID);
                }
            }
        }

        /// <summary>
        /// Used to check if a projectile will hit a wall on the given frame.
        /// </summary>
        /// <param name="w"></param>
        /// <param name="p"></param>
        /// <returns></returns>
        private bool WallCheckProjectile(Wall w, Projectile p)
        {
            double point1XLeft = w.point1.GetX() - 25;
            double point1XRight = w.point1.GetX() + 25;
            double point1YUp = w.point1.GetY() - 25;
            double point1YDown = w.point1.GetY() + 25;

            double point2XLeft = w.point2.GetX() - 25;
            double point2XRight = w.point2.GetX() + 25;
            double point2YUp = w.point2.GetY() - 25;
            double point2YDown = w.point2.GetY() + 25;

            // Vertical wall
            if (point1XLeft == point2XLeft)
            {
                if (p.location.GetX() > point1XLeft && p.location.GetX() < point1XRight)
                {
                    if (p.location.GetY() > point1YUp && p.location.GetY() < point2YDown)
                    {
                        return true;
                    }
                }
                return false;
            }

            // Horizontal wall
            else
            {
                if (p.location.GetY() > point1YUp && p.location.GetY() < point1YDown)
                {
                    if (p.location.GetX() > point1XLeft && p.location.GetX() < point2XRight)
                    {
                        return true;
                    }
                }
                return false;
            }
        }

        /// <summary>
        /// Used to check if a projectile will hit a tank
        /// </summary>
        /// <param name="w"></param>
        /// <param name="t"></param>
        /// <returns></returns>
        private bool WallCheckTank(Wall w, Tank t)
        {
            double point1XLeft = w.point1.GetX() - 55;
            double point1XRight = w.point1.GetX() + 55;
            double point1YUp = w.point1.GetY() - 55;
            double point1YDown = w.point1.GetY() + 55;

            double point2XLeft = w.point2.GetX() - 55;
            double point2XRight = w.point2.GetX() + 55;
            double point2YUp = w.point2.GetY() - 55;
            double point2YDown = w.point2.GetY() + 55;

            // Vertical wall
            if (point1XLeft == point2XLeft)
            {
                if (t.GetLocation().GetX() > point1XLeft && t.GetLocation().GetX() < point1XRight)
                {
                    if (t.GetLocation().GetY() > point1YUp && t.GetLocation().GetY() < point2YDown)
                    {
                        return true;
                    }
                }
                return false;
            }

            // Horizontal wall
            else
            {
                if (t.GetLocation().GetY() > point1YUp && t.GetLocation().GetY() < point1YDown)
                {
                    if (t.GetLocation().GetX() > point1XLeft && t.GetLocation().GetX() < point2XRight)
                    {
                        return true;
                    }
                }
                return false;
            }
        }

        /// <summary>
        /// Read the settings file for TankWars and the properties 
        /// of this game
        /// </summary>
        /// <param name="filename">settings file</param>
        public void ReadSettings(string filename)
        {
            // x position in 2DVector
            string x = null;
            // y position in 2DVector
            string y = null;
            // p1 vector for a wall
            Vector2D p1Vec = null;
            // p2 vector for a wall
            Vector2D p2Vec = null;
            // Wall ID
            int id = 0;
            try
            {
                // Create an XmlReader inside this block, and automatically Dispose() it at the end.
                using (XmlReader reader = XmlReader.Create(filename))
                {
                    while (reader.Read())
                    {
                        if (reader.IsStartElement())
                        {
                            switch (reader.Name)
                            {
                                case "GameSettings":
                                    break;
                                case "UniverseSize":
                                    reader.Read();
                                    if (int.TryParse(reader.Value, out int size))
                                        UniverseSize = size;
                                    else
                                        throw new Exception("invalid UniverseSize");
                                    break;
                                case "MSPerFrame":
                                    reader.Read();
                                    if (int.TryParse(reader.Value, out int ms))
                                        MSPerFrame = ms;
                                    else
                                        throw new Exception("invalid MSPerFrame");
                                    break;
                                case "FramesPerShot":
                                    reader.Read();
                                    if (int.TryParse(reader.Value, out int f))
                                        FramesPerShot = f;
                                    else
                                        throw new Exception("invalid FramesPerShot");
                                    break;
                                case "RespawnRate":
                                    reader.Read();
                                    if (int.TryParse(reader.Value, out int rate))
                                        RespawnRate = rate;
                                    else
                                        throw new Exception("invalid RespawnRate");
                                    break;
                                case "MaxHP":
                                    reader.Read();
                                    if (int.TryParse(reader.Value, out int hp))
                                        MaxHP = hp;
                                    else
                                        throw new Exception("invalid MaxHP");
                                    break;
                                case "ProjectileSpeed":
                                    reader.Read();
                                    if (int.TryParse(reader.Value, out int sp))
                                        ProjectileSpeed = sp;
                                    else
                                        throw new Exception("invalid ProjectileSpeed");
                                    break;
                                case "EngineStrength":
                                    reader.Read();
                                    if (double.TryParse(reader.Value, out double st))
                                        EngineStrength = st;
                                    else
                                        throw new Exception("invalid EngineStrength");
                                    break;
                                case "MaxPowerups":
                                    reader.Read();
                                    if (int.TryParse(reader.Value, out int pu))
                                        MaxPowerups = pu;
                                    else
                                        throw new Exception("invalid TMaxPowerups");
                                    break;
                                case "MaxPowerupDelay":
                                    reader.Read();
                                    if (int.TryParse(reader.Value, out int pud))
                                        MaxPowerupDelay = pud;
                                    else
                                        throw new Exception("MaxPowerupDelay");
                                    break;
                                case "Wall":
                                    break;
                                case "p1":
                                    break;
                                case "p2":
                                    break;
                                case "x":
                                    reader.Read();
                                    x = reader.Value;
                                    break;
                                case "y":
                                    reader.Read();
                                    y = reader.Value;
                                    break;
                                default:
                                    throw new Exception();
                            }
                        }
                        else // If it's not a start element, it's probably an end element
                        {
                            try
                            {
                                // Create the p1 vector for the wall
                                if (reader.Name == "p1")
                                {
                                    if (int.TryParse(x, out int x_comp) && int.TryParse(y, out int y_comp))
                                        p1Vec = new Vector2D(x_comp, y_comp);
                                    else
                                        throw new Exception("invalid wall position 1");

                                    x = null;
                                    y = null;
                                }

                                // Create the p2 vector for the wall
                                if (reader.Name == "p2")
                                {
                                    if (int.TryParse(x, out int x_comp) && int.TryParse(y, out int y_comp))
                                        p2Vec = new Vector2D(x_comp, y_comp);
                                    else
                                        throw new Exception("invalid wall position 2");

                                    x = null;
                                    y = null;
                                }

                                //Create a wall with the p1 and p2 vector
                                if (reader.Name == "Wall")
                                {
                                    if (p1Vec != null && p2Vec != null)
                                    {
                                        // Create a new wall and add it to the World
                                        theWorld.AddOrUpdateWall(id, new Wall(p1Vec, p2Vec, id));
                                        id++;
                                    }
                                    else
                                        throw new Exception("Invalid wall");
                                    p1Vec = null;
                                    p2Vec = null;
                                }
                            }
                            catch (Exception e)
                            {
                                throw e;
                            }

                        }
                    }
                }
            }
            catch (Exception e)
            {
                if (e is System.IO.FileNotFoundException)
                    throw new Exception("File not found");
                if (e is System.Xml.XmlException)
                    throw new Exception("Format Error");
                if (e is ArgumentException)
                    throw new Exception("Not a valid URI");
                if (e is System.IO.DirectoryNotFoundException)
                    throw new Exception("Directory not found");
                else
                    throw e;
            }
        }

        /// <summary>
        /// Determines if a ray interescts a circle
        /// </summary>
        /// <param name="rayOrig">The origin of the ray</param>
        /// <param name="rayDir">The direction of the ray</param>
        /// <param name="center">The center of the circle</param>
        /// <param name="r">The radius of the circle</param>
        /// <returns></returns>
        public static bool Intersects(Vector2D rayOrig, Vector2D rayDir, Vector2D center, double r)
        {
            // ray-circle intersection test
            // P: hit point
            // ray: P = O + tV
            // circle: (P-C)dot(P-C)-r^2 = 0
            // substitute to solve for t gives a quadratic equation:
            // a = VdotV
            // b = 2(O-C)dotV
            // c = (O-C)dot(O-C)-r^2
            // if the discriminant is negative, miss (no solution for P)
            // otherwise, if both roots are positive, hit

            double a = rayDir.Dot(rayDir);
            double b = ((rayOrig - center) * 2.0).Dot(rayDir);
            double c = (rayOrig - center).Dot(rayOrig - center) - r * r;

            // discriminant
            double disc = b * b - 4.0 * a * c;

            if (disc < 0.0)
                return false;

            // find the signs of the roots
            // technically we should also divide by 2a
            // but all we care about is the sign, not the magnitude
            double root1 = -b + Math.Sqrt(disc);
            double root2 = -b - Math.Sqrt(disc);

            return (root1 > 0.0 && root2 > 0.0);
        }
    }

}
