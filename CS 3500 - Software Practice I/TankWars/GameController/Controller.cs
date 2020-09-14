//Authors: Alysha Armstrong and Brandon Walters, 11/13/2019

//Controller for TankWars client. This is responsible for 
//parsing information from the Network Controller, updating 
//the model, and informing the View of changes. 


using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Text.RegularExpressions;
using System.Timers;
using NetworkUtil;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace TankWars
{
    /// <summary>
    /// Parses the information received from the server, updates 
    //  the model, and informs the View of changes.
    /// </summary>
    public class Controller
    {
        // World containing all the objects in the game
        private World theWorld;
        // Players name set in form text box
        private string playerName;
        /// indicates if the player id was received
        bool idRecieved = false;
        /// idicates if the handshake is complete
        private bool handshakecomplete = false;

        // Delegate for when a server update is received
        public delegate void ServerUpdateHandler();
        // event for when a server update is received
        private event ServerUpdateHandler UpdateArrived;
        // socket to use
        private Socket socket;

        public Controller()
        {
            theWorld = new World();
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
        /// Set ServerUpdateHandler event for the ServerUpdateHandler
        /// </summary>
        /// <param name="h">event to invoke when a server update is received</param>
        public void RegisterServerUpdateHandler(ServerUpdateHandler h)
        {
            UpdateArrived += h;
        }

        /// <summary>
        /// Begin the connection with the server when the Connect button is selected in 
        /// the Form app. Connects to the host specified in the text box and sets
        /// the OnNetworkAction to FirstContact
        /// </summary>
        /// <param name="host">host name specified in the serverTextBox in the Form App</param>
        public void InitializeConnectToServer(string host, string name)
        {
            playerName = name;
            Networking.ConnectToServer(FirstContact, host, Constants.Port);
        }

        /// <summary>
        /// After the connection to the server is made, change the callback to 
        /// RecieveStartUp, send the player's name, and call GetData. 
        /// </summary>
        /// <param name="s">the socketstate from the Network Controller</param>
        private void FirstContact(SocketState s)
        {
            socket = s.TheSocket;
            // change the callback
            s.OnNetworkAction = new Action<SocketState>(RecieveStartUp);
            // send the players name
            Networking.Send(s.TheSocket, playerName + "\n");
            Networking.GetData(s);
        }

        /// <summary>
        /// Extract the startup data from the server, set the callback to 
        /// RecieveWorld and call GetData
        /// </summary>
        /// <param name="s">the socketstate from the Network Controller</param>
        private void RecieveStartUp(SocketState s)
        {
            // Process messages until the player id and world size are received
            while (!handshakecomplete)
                ProcessStartupMessages(s);

            // Change the callback 
            s.OnNetworkAction = new Action<SocketState>(RecieveWorld);
            Networking.GetData(s);
        }

        /// <summary>
        /// Updates the world whenever new data is recieved 
        /// </summary>
        /// <param name="s">the socketstate from the Network Controller</param>
        private void RecieveWorld(SocketState s)
        {
            RecieveServerMessages(s);
            // continue the event loop
            Networking.GetData(s);
        }

        /// <summary>
        /// Processes the startup messages from the server containing the player
        /// id and world size
        /// </summary>
        /// <param name="state">the socketstate from the Network Controller</param>
        private void ProcessStartupMessages(SocketState state)
        {
            string totalData = state.GetData();
            string[] parts = Regex.Split(totalData, @"(?<=[\n])");

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

                if (!idRecieved)
                {
                    // Parse the player id from the message
                    if (int.TryParse(p, out int result))
                    {
                        theWorld.SetPlayerID(result);
                        idRecieved = true;
                    }
                    // Then remove it from the SocketState's growable buffer
                    state.RemoveData(0, p.Length);
                }
                else if (!handshakecomplete)
                {
                    // Parse the world size from the message
                    if (int.TryParse(p, out int result))
                    {
                        theWorld.WorldSize = result;
                        theWorld.WorldSizeReceived = true;
                        handshakecomplete = true;
                    }
                    // Then remove it from the SocketState's growable buffer
                    state.RemoveData(0, p.Length);
                }
            }
        }

        /// <summary>
        /// Recieves the world messages from the server and calls
        /// ProcessWorldMessage to process them 
        /// </summary>
        /// <param name="state">the socketstate from the Network Controller</param>
        private void RecieveServerMessages(SocketState state)
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

                // When a message is received, process it and add it to the world
                messages.Add(p);

                state.RemoveData(0, p.Length);
            }

            if (messages.Count != 0)
            {
                ProcessWorldMessage(messages);
                // If a message was received, send the controll commands to the
                // server and trigger the update arrived event
                if (UpdateArrived != null)
                {
                    UpdateArrived();
                }
            }

            // Send control command to the server
            SendControlCommand();

        }

        /// <summary>
        /// Process a JSON message and add it to or update it in the World 
        /// </summary>
        /// <param name="message">JSON message for the object to add to the server</param>
        private void ProcessWorldMessage(List<string> messages)
        {
            lock (theWorld)
            {
                foreach (string message in messages)
                {
                    JObject json = JObject.Parse(message);

                    if (json["wall"] != null)
                    {
                        Wall newWall = JsonConvert.DeserializeObject<Wall>(message);
                        theWorld.AddOrUpdateWall(newWall.ID, newWall);
                    }
                    else if (json["tank"] != null)
                    {
                        Tank newTank = JsonConvert.DeserializeObject<Tank>(message);
                        theWorld.AddOrUpdateTank(newTank.ID, newTank);
                    }
                    else if (json["proj"] != null)
                    {
                        Projectile newProj = JsonConvert.DeserializeObject<Projectile>(message);
                        theWorld.AddOrUpdateProjectile(newProj.ID, newProj);
                    }
                    else if (json["power"] != null)
                    {
                        PowerUp newPow = JsonConvert.DeserializeObject<PowerUp>(message);
                        theWorld.AddOrUpdatePowerup(newPow.ID, newPow);
                    }
                    else if (json["beam"] != null)
                    {
                        Beam newBeam = JsonConvert.DeserializeObject<Beam>(message);
                        theWorld.AddBeam(newBeam.ID, newBeam);
                    }
                }
            }
        }

        /// <summary>
        /// Send the updates in the client to the server
        /// </summary>
        public void SendControlCommand()
        {
            string controlCommandString = JsonConvert.SerializeObject(theWorld.GetControlCommand());
            Networking.Send(socket, controlCommandString + "\n");
        }
    }
}
