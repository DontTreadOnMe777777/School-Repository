using NetworkUtil;
using System;
using Server;
using System.Net.Sockets;
using System.Threading;
using TankWars;

namespace Server
{
    class Program
    {
        public static void Main(string[] args)
        {
            // Creates the controller for the server and the web database
            Controller con = new Controller();
            WebController wc = new WebController();

            // Starts the listener for the server and requests from the web server for the database
            Networking.StartServer(con.HandleNewClient, 11000);
            Networking.StartServer(wc.HandleHttpConnection, 80);
            
            // Puts the world update into a seperate thread to continuously update all clients
            Thread thread = new Thread(con.UpdateLoop);
            thread.Start();
            
            Console.Read();
            //save game to database
            DatabaseController.UploadGame(con);
        }
    }
}
