package lightsout;

import javax.swing.SwingUtilities;

public class LightsOut
{
    public static void main (String[] args)
    {
        SwingUtilities.invokeLater( () -> new LightsOutView());
    }
}
