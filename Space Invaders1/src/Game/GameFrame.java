
package Game;

import java.awt.Color;
import javax.swing.JFrame;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Toolkit;

/**
 *
 * @author Spartan Tech
 */
public class GameFrame extends JFrame
{
    private GamePanel game;
    /**
     * @wbp.nonvisual location=775,179
     */
    private final Component horizontalGlue_1 = Box.createHorizontalGlue();
    
    public GameFrame()
    {
        // Add text to title bar 
        super("Space Invaders");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Usuario\\git\\Space-Invaders\\Space Invaders1\\images\\icon1.png"));
        
        // Make sure the program exits when the close button is clicked
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        // Create an instance of the Game class and turn on double buffering
        //  to ensure smooth animation
        game = new GamePanel();
        game.setDoubleBuffered(true);
        
        // Add the Breakout instance to this frame's content pane to display it
        this.getContentPane().add(game); 
        
        Component horizontalGlue = Box.createHorizontalGlue();
        game.add(horizontalGlue);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        
        // Start the game
        game.start();  
    }
    
    public static void main(String[] args) 
    {
         java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame().setVisible(true);
            }
        });
        
    }
}