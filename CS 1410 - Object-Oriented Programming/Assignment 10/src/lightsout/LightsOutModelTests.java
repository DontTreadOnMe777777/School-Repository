package lightsout;

import javax.swing.SwingUtilities;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightsOutModelTests
{
    @Test
    public void test1 ()
    {
        SwingUtilities.invokeLater( () -> new LightsOutView());
        LightsOutModel newBoard = new LightsOutModel(5,5);
        newBoard.makeMove(4, 4);
        assertEquals(1, newBoard.getOccupant(4,4));
        
    }

}
