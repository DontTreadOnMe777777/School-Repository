using System.Collections.Generic;
using System.Text;

namespace TankWars
{
  /// <summary>
  /// You should not need to modify this class.
  /// Use this class as a helper to generate HTTP + HTML responses.
  /// Your web server will first query the database to get the appropriate data,
  /// then pass that data to these helper methods to format it in HTML.
  /// </summary>
  public static class WebViews
  {
    // HTTP and HTML headers/footers
    private const string httpOkHeader = "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n";
    private const string httpBadHeader = "HTTP/1.1 404 Not Found\r\nConnection: close\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n";
    private const string htmlHeader = "<!DOCTYPE html><html><head><title>TankWars</title></head><body>";
    private const string htmlFooter = "</body></html>";

    /// <summary>
    /// Returns an HTTP response indicating the request was bad
    /// </summary>
    /// <returns></returns>
    public static string Get404()
    {
      return httpBadHeader + WrapHtml("Bad http request");
    }

    /// <summary>
    /// Returns an HTTP response containing HTML tables representing the given games
    /// Query your database to construct a dictionary of games to pass to this method
    /// </summary>
    /// <param name="games">Information about all games known</param>
    /// <returns></returns>
    public static string GetAllGames(Dictionary<uint, GameModel> games)
    {
      StringBuilder sb = new StringBuilder();

      foreach (uint gid in games.Keys)
      {
        sb.Append("Game " + gid + " (" + games[gid].Duration + " seconds)<br>");
        sb.Append("<table border=\"1\">");
        sb.Append("<tr><th>Name</th><th>Score</th><th>Accuracy</th></tr>");
        foreach (PlayerModel p in games[gid].GetPlayers())
        {
          sb.Append("<tr>");
          sb.Append("<td>" + p.Name + "</td>");
          sb.Append("<td>" + p.Score + "</td>");
          sb.Append("<td>" + p.Accuracy + "</td>");
          sb.Append("</tr>");
        }
        sb.Append("</table><br><hr>");
      }

      return httpOkHeader + WrapHtml(sb.ToString());
    }

    /// <summary>
    /// Returns an HTTP response containing one HTML table representing the games
    /// that a certain player has played in
    /// Query your database for games played by the named player, then pass that name
    /// and the list of sessions to this method
    /// </summary>
    /// <param name="name">The name of the player</param>
    /// <param name="games">The list of sessions the player has played</param>
    /// <returns></returns>
    public static string GetPlayerGames(string name, List<SessionModel> games)
    {
      StringBuilder sb = new StringBuilder();

      sb.Append("Games for " + name + "<br>");
      sb.Append("<table border=\"1\">");
      sb.Append("<tr><th>GameID</th><th>Duration</th><th>Score</th><th>Accuracy</th></tr>");

      foreach (SessionModel s in games)
      {
        sb.Append("<tr>");
        sb.Append("<td>" + s.GameID + "</td>");
        sb.Append("<td>" + s.Duration + "</td>");
        sb.Append("<td>" + s.Score + "</td>");
        sb.Append("<td>" + s.Accuracy + "</td>");
        sb.Append("</tr>");
      }
      sb.Append("</table><br><hr>");

      return httpOkHeader + WrapHtml(sb.ToString());
    }

    /// <summary>
    /// Returns a simple HTTP greeting response
    /// </summary>
    /// <param name="numPlayers"></param>
    /// <returns></returns>
    public static string GetHomePage(int numPlayers)
    {
      return httpOkHeader + WrapHtml("Welcome to TankWars");
    }

    /// <summary>
    /// Helper for wraping a string in an HTML header and footer
    /// </summary>
    /// <param name="content"></param>
    /// <returns></returns>
    private static string WrapHtml(string content)
    {
      return htmlHeader + content + htmlFooter;
    }

  }
}

