using System.Collections.Generic;

namespace TankWars
{
  /// <summary>
  /// A simple container class representing one player in one game
  /// </summary>
  public class PlayerModel
  {
    public readonly string Name;
    public readonly uint Score;
    public readonly uint Accuracy;
    public PlayerModel(string n, uint s, uint a)
    {
      Name = n;
      Score = s;
      Accuracy = a;
    }
  }


  /// <summary>
  /// A simple container class representing one game and its players
  /// </summary>
  public class GameModel
  {
    public readonly uint ID;
    public readonly uint Duration;
    private List<PlayerModel> players;

    public GameModel(uint id, uint d)
    {
      Duration = d;
      players = new List<PlayerModel>();
    }

    /// <summary>
    /// Adds a player to the game
    /// </summary>
    /// <param name="name">The player's name</param>
    /// <param name="score">The player's score</param>
    /// <param name="accuracy">The player's accuracy</param>
    public void AddPlayer(string name, uint score, uint accuracy)
    {
      players.Add(new PlayerModel(name, score, accuracy));
    }

    /// <summary>
    /// Returns the players in this game
    /// </summary>
    /// <returns></returns>
    public List<PlayerModel> GetPlayers()
    {
      return players;
    }

  }

  /// <summary>
  /// A simple container class representing the information about one player's session in one game
  /// </summary>
  public class SessionModel
  {
    public readonly uint GameID;
    public readonly uint Duration;
    public readonly uint Score;
    public readonly uint Accuracy;

    public SessionModel(uint gid, uint dur, uint score, uint acc)
    {
      GameID = gid;
      Duration = dur;
      Score = score;
      Accuracy = acc;
    }

  }

}