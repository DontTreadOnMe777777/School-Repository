using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data.MySqlClient;
using Server;

namespace TankWars
{
    public class DatabaseController
    {
        /// <summary>
        /// The connection string.
        /// Your uID login name serves as both your database name and your uid
        /// </summary>
        public const string connectionString = "server=atr.eng.utah.edu;" +
          "database=cs3500_u1072028;" +
          "uid=cs3500_u1072028;" +
          "password=1234";


        /// <summary>
        /// Get the stats for all of the games from the database
        /// </summary>
        /// <returns></returns>
        public static Dictionary<uint, GameModel> GetAllGames()
        {
            Dictionary<uint, uint> games = new Dictionary<uint, uint>();
            // Connect to the DB
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                try
                {
                    // Open a connection
                    conn.Open();

                    // Create a command
                    MySqlCommand command = conn.CreateCommand();

                    command.CommandText = "select gameID,Duration from Games";

                    // Execute the command and cycle through the DataReader object
                    using (MySqlDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            games.Add((uint)reader["gameID"], (uint)reader["Duration"]);
                        }
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                }

                return AddPlayersToGameModel(games);
            }
        }

        private static Dictionary<uint, GameModel> AddPlayersToGameModel(Dictionary<uint, uint> games)
        {
            Dictionary<uint, GameModel> allGames = new Dictionary<uint, GameModel>();
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                try
                {
                    // Open a connection
                    conn.Open();

                    // Create a command
                    //MySqlCommand command = conn.CreateCommand();
                    uint dur;
                    foreach (uint id in games.Keys)
                    {
                        dur = games[id];
                        GameModel game = new GameModel(id, dur);
                        // Create a command
                        MySqlCommand playersStats = conn.CreateCommand();
                        playersStats.CommandText = "select * from Players where gameID = " + id;
                        using (MySqlDataReader reader2 = playersStats.ExecuteReader())
                        {
                            while (reader2.Read())
                            {
                                game.AddPlayer((string)reader2["player"], (uint)reader2["score"], (uint)reader2["accuracy"]);
                            }
                        }
                        allGames.Add((uint)id, game);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                }

            }
            return allGames;
        }


        /// <summary>
        /// 
        /// </summary>
        /// <param name="playerName"></param>
        public static List<SessionModel> GetGamesForPlayer(string playerName)

        {
            List<SessionModel> sessions = new List<SessionModel>();
            // Connect to the DB
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                try
                {
                    // Open a connection
                    conn.Open();

                    // Create a command
                    MySqlCommand command = conn.CreateCommand();
                    command.CommandText = "select * from Players where player =" + playerName + " , Games";

                    // Execute the command and cycle through the DataReader object
                    using (MySqlDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            SessionModel oneSession = new SessionModel((uint)reader["gameID"], (uint)reader["Duration"], (uint)reader["score"], (uint)reader["accuracy"]);
                            sessions.Add(oneSession);
                        }
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                }

                return sessions;
            }
        }

        public static void UploadGame(Controller con)
        {
            using (MySqlConnection conn = new MySqlConnection(connectionString))
            {
                try
                {
                    // Open a connection
                    conn.Open();

                    // Insert the game duration
                    MySqlCommand command = conn.CreateCommand();
                    command.CommandText = "insert into Games (Duration) values(@val1)";
                    command.Parameters.AddWithValue("@val1", con.returnTimeInSeconds());
                    command.ExecuteNonQuery();

                    // Get the game id
                    MySqlCommand getID = conn.CreateCommand();
                    getID.CommandText = "SELECT gameID FROM Games ORDER BY gameID DESC LIMIT 1";
                    uint gameID = 0;
                    // Execute the command and cycle through the DataReader object
                    using (MySqlDataReader reader = getID.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            gameID = (uint)reader["gameID"];
                        }
                    }

                    MySqlCommand insertPlayer = conn.CreateCommand();
                    insertPlayer.CommandText = "insert into Players (player,gameID,score,accuracy) values(@val2,@val3,@val4,@val5)";

                    foreach (Tank tank in con.GetWorld().GetTanks().Values)
                    {
                        insertPlayer.Parameters.AddWithValue("@val2", tank.name);
                        insertPlayer.Parameters.AddWithValue("@val3", gameID);
                        insertPlayer.Parameters.AddWithValue("@val4", tank.GetScore());
                        insertPlayer.Parameters.AddWithValue("@val5", con.returnPlayerAccuracy(tank.ID));
                        insertPlayer.ExecuteNonQuery();
                    }

                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                }
            }
        }
    }
}
