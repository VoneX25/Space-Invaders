
package Game;

import Controller.KeyboardController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @author Spartan Tech
 */
public class Ship extends ControlledGameObject {

    ImageIcon ship = new ImageIcon("images/shipSkin.gif");
    ImageIcon explosion = new ImageIcon("images/explosion.gif");
    ImageIcon damageship = new ImageIcon("images/shipSkinD.gif");
    ImageIcon bonusEnemy = new ImageIcon("images/bonusEnemySkin.gif");
    ImageIcon lifeCounterShip = new ImageIcon("images/shipSkinSmall.gif");
    ImageIcon alerta = new ImageIcon("images/alerta.gif");
    
    int timer = 0;
    public static int timer2 = 0;
    public static int timer3 = 0;

    // Constructor for all ship objects
    public Ship(int xPosition, int yPosition, Color color, KeyboardController control) {
        super(xPosition, yPosition, color, control);
    }

    // Draw bonus enemy ship
    public void bonusDraw(Graphics g) {

        bonusEnemy.paintIcon(null, g, this.getXPosition(), this.getYPosition());

    }

    // Draw ships for life counter
    public void lifeDraw(Graphics g) {
        lifeCounterShip.paintIcon(null, g, this.getXPosition(), this.getYPosition());
    }

    // Draw player controlled ship
    @Override
    public void draw(Graphics g) {
    	
    	//Animaciones de muerte y daño
    	
    	if (GamePanel.getDead() && timer2 < 300){
    		explosion.paintIcon(null, g, this.getXPosition() - 80, this.getYPosition());
    		timer2++;
    		if (timer2 == 299) {
    			GamePanel.dead = false;
    		}
    	}
    	else if (GamePanel.getDamage() && timer < 350) {
    		damageship.paintIcon(null, g, this.getXPosition(), this.getYPosition());
    		timer++;
    		if (timer == 349){
    			GamePanel.damage = false;
    		}
    	}
    	else {
    		ship.paintIcon(null, g, this.getXPosition(), this.getYPosition());
    		timer = 0;
    		timer2 = 0;
    	}
    }

    // Gets the hit box for all ship objects
    @Override
    public Rectangle getBounds() {
        Rectangle shipHitbox = new Rectangle(this.getXPosition(), this.getYPosition(), 50, 50);
        return shipHitbox;
    }

    // Used to move all ship objects
    @Override
    public void move() {
        // Left arrow key press
        if (control.getKeyStatus(37)) {
            xPos -= 10;
            if (xPos < 15) {
            	xPos = 15;
            }
        }
        // Right arrow key press
        if (control.getKeyStatus(39)) {
            xPos += 10;
            if (xPos > 745) {
            	xPos = 745;
            }
        }
                
        // Move from edge to edge without stopping
      
    }
    
}
