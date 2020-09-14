using NetworkUtil;
using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace TankWars
{
    public class WebController
    {
        /// <summary>
        /// Allows the web server to connect to the game server, then listens for a request
        /// </summary>
        public void HandleHttpConnection(SocketState s)
        {
            s.OnNetworkAction = ServeHttpRequest;
            Networking.GetData(s);
        }

        /// <summary>
        /// If a request is received, parse that request and call the appropriate WebViews method to construct the tables
        /// </summary>
        /// <param name="s"></param>
        private static void ServeHttpRequest(SocketState s)
        {
            string request = s.GetData();
            if (request.Contains("GET /games?player="))
            {
                string[] parts = Regex.Split(request, @"(?<=[=])");
                List<SessionModel> session = DatabaseController.GetGamesForPlayer(parts[1]);
                Networking.SendAndClose(s.TheSocket, WebViews.GetPlayerGames(parts[1], session));
            }
            else if (request.Contains("GET /games"))
            {
                Dictionary<uint, GameModel> games = DatabaseController.GetAllGames();
                Networking.SendAndClose(s.TheSocket, WebViews.GetAllGames(games));
            }

        }
    }
}