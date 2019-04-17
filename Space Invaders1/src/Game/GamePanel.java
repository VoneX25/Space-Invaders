
package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import Controller.KeyboardController;


/**
 *
 * @author Spartan Tech
 */
public class GamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3601824237073395126L;
	// Required components. Do not remove!
	private Timer gameTimer;
	private KeyboardController controller;
	// Controls size of game window and framerate
	private final int gameWidth = 800;
	private final int gameHeight = 800;
	private final int framesPerSecond = 120;

	// Added Counters
	Random r = new Random();
	private int score = 0;
	private int level = 1;
	private int numberOfLives = 3;
	private int highScore;
	private int markerX, markerY;
	private static int bossHealth = 30;
	File f = new File("Highscore.txt");
	public static boolean dead = false;
	public static boolean damage = false;
	public static int contadorBoss = 0;

	// Added Objects
	private Ship playerShip;
	private Ship singleLife;
	private Ship bonusEnemy;
	private Enemy enemy;
	private Shield shield;
	private Bullet bullet;
	private Beam beam, beam2, beam3;

	// Added Booleans
	private boolean newBulletCanFire = true;
	private boolean newBeamCanFire = true;
	private boolean newBonusEnemy = true;
	private boolean hitMarker = false;

	// Added Array Lists
	public ArrayList<Ship> lifeList = new ArrayList<Ship>();
	private ArrayList<Ship> bonusEnemyList = new ArrayList<Ship>();
	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	private ArrayList<Shield> shieldList = new ArrayList<Shield>();
	private ArrayList<Beam> beamList = new ArrayList<Beam>();
	private ImageIcon background = new ImageIcon("images/backgroundSkin.jpg");

	// Added Audio files and streams


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// EXTRA METHODS

	// Used in the Enemy class to help with the draw method for the boss
	public static int getBossHealth() {
		return bossHealth;
	}
	//getters para las animaciones de da y morir

	public static boolean getDead() {
		return dead;
	}
	public static boolean getDamage() {
		return damage;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SETUP GAME

	public final void setupGame() {

		// Sets enemies for normal levels
		if (level != 3 && level != 6 && level != 9 && level != 12 && level != 15) {
			// Six columns
			for (int row = 0; row < 6; row++) {
				// 5 rows
				for (int column = 0; column < 5; column++) {
					enemy = new Enemy((20 + (row * 100)), (20 + (column * 60)), level, 0, column, null, 40, 40); // Enemy speed will increase each level
					enemyList.add(enemy);
				}
			}
		}
		// Sets enemy for boss levels
		if (level == 3 || level == 6 || level == 9 || level == 12 || level == 15) {
			//He borrado el sonido //            // Plays boss roar
			enemy = new Enemy(20, 20, 3, 0, 100, null, 150, 150);
			enemyList.add(enemy);
		}
		// Gives directions on level 1
		if (level == 1) {
			JOptionPane.showMessageDialog(null, "Bienvenido a Space Intruders!\n\nCOSAS QUE DEBES SABER:\n\n- Usa las flechas izquierda/derecha para moverte\n- Aprieta el espacio para disparar\n- Los enemigos son m疽 r疳idos cada nivel"
					+ "\n- JEFE cada 3 niveles\n- Un enemigo bonus aparecer� aleatoriamente\n- Disparale para conseguir puntos extra!\n- Aprieta P para pausar\n- Tienes 3 vidas\n\nDIVIERTETE!");
		}
		// Resets all controller movement
		controller.resetController();

		// Sets the player's ship values   
		playerShip = new Ship(375, 730, null, controller);

		// Sets the life counter Ships
		for (int column = 0; column < numberOfLives; column++) {
			singleLife = new Ship(48 + (column * 20), 10, Color.WHITE, null);
			lifeList.add(singleLife);
		}

		// Sets the values for 3 rows and 3 columns of shields
		for (int row = 0;row < 12; row++) {
			int n = 0;
			for (int column = 0; column < 120; column++) {
				if (column % 30 == 0) {
					n = n + 80;
				}
				shield = new Shield(1 + (column * 3) + n, 650 - (row * 5), 10, 10, Color.RED);
				shieldList.add(shield);
				column++;
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PAINT
	@Override
	public void paint(Graphics g) {

		// Sets background image
		background.paintIcon(null, g, 0, -150);

		// makes a string that says "+100" on enemy hit
		if (bullet != null) {
			if (hitMarker) {
				g.setColor(Color.WHITE);
				if (level != 3 && level != 6 && level != 9 && level != 12) {
					g.drawString("+ 100", markerX + 20, markerY -= 1);
				} else {
					g.drawString("- 1", markerX + 75, markerY += 1);
				}
			}
		}
		// Draws the player's ship
		playerShip.draw(g);

		if (dead) {
			newBulletCanFire = false;
		}

		// Draws 3 evenly-spaced shields 
		for (int index = 0; index < shieldList.size(); index++) {
			shieldList.get(index).draw(g);
		}

		//Draws 3 different kinds of aliens
		try {
			for (int index = 0; index < enemyList.size(); index++) {
				enemyList.get(index).draw(g);
			}

		} catch (IndexOutOfBoundsException e) {
		}

		// Draw a bullet on space bar press
		if (controller.getKeyStatus(32)) {
			if (newBulletCanFire) {
				bullet = new Bullet(playerShip.getXPosition() + 22, playerShip.getYPosition() - 10, 0, Color.RED);
				//He borrado el sonido // 				// Plays bullet sound
				newBulletCanFire = false;
			}
		}
		// Only attempts to draw bullet after key press
		if (bullet != null) {
			bullet.draw(g);
		}

		// Generates random beams shot from enemies
		if (level != 3 && level != 6 && level != 9 && level != 12 && level != 15) {
			if (newBeamCanFire) {
				for (int index = 0; index < enemyList.size(); index++) {
					if ((r.nextInt(30) == index)||(r.nextInt(29) == index)) {
						beam = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition(), 0, Color.YELLOW);
						beamList.add(beam);

						//He borrado el sonido //				// Plays beam sound for normal enemies
					}
					newBeamCanFire = false;
				}
			}
		}
		// Generates beams at a faster rate for boss
		if (level == 3 || level == 6 || level == 9 || level == 12 || level == 15) {
			//if (Enemy.disparar) {
				if (newBeamCanFire == true) {
					for (int index = 0; index < enemyList.size(); index++) {
						if (r.nextInt(5) == index) {
							beam = new Beam(enemyList.get(index).getXPosition() + 75, enemyList.get(index).getYPosition() + 140, 0, Color.YELLOW);
							beam2 = new Beam(enemyList.get(index).getXPosition(), enemyList.get(index).getYPosition() + 110, 0, Color.YELLOW);
							beam3 = new Beam(enemyList.get(index).getXPosition() + 150, enemyList.get(index).getYPosition() + 110, 0, Color.YELLOW);
							beamList.add(beam);
							beamList.add(beam2);
							beamList.add(beam3);
							//He borrado el sonido de las balas del boss// 			// Plays beam sound for boss
						}
						newBeamCanFire = false;
					}
				}
			//}
		}
		// Draws the generated beams
		for (int index = 0; index < beamList.size(); index++) {
			beamList.get(index).draw(g);
		}
		// Generates random bonus enemy
		if (newBonusEnemy) {
			if (r.nextInt(3000) == 1500) {
				bonusEnemy = new Ship(-50, 30, Color.RED, null);
				bonusEnemyList.add(bonusEnemy);
				newBonusEnemy = false;
			}
		}
		// Draws bonus enemy
		for (int index = 0; index < bonusEnemyList.size(); index++) {
			bonusEnemyList.get(index).bonusDraw(g);
		}

		// Sets the score display
		g.setColor(Color.WHITE);
		g.drawString("Puntuación: " + score, 260, 20);

		// Sets the life counter display
		g.setColor(Color.WHITE);
		g.drawString("Vidas:", 11, 20);
		for (int index = 0; index < lifeList.size(); index++) {
			lifeList.get(index).lifeDraw(g);
		}
		// Sets level display
		g.setColor(Color.WHITE);
		g.drawString("Nivel " + level, 750, 20);

		// Sets Highscore display
		g.setColor(Color.WHITE);
		g.drawString("Record: " + highScore, 440, 20);

		// Draws a health display for boss level
		if (level == 3 || level == 6 || level == 9 || level == 12 || level == 15) {
			g.setColor(Color.WHITE);
			g.drawString("Salud del jefe: " + bossHealth, 352, 600);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// UPDATE GAME STATE

	public void updateGameState(int frameNumber) {

		// Allows player to move left and right
		if (!dead) {
			playerShip.move();
		}
		// Updates highscore
		try {
			@SuppressWarnings("resource")
			Scanner fileScan = new Scanner(f);
			while (fileScan.hasNextInt()) {
				String nextLine = fileScan.nextLine();
				@SuppressWarnings("resource")
				Scanner lineScan = new Scanner(nextLine);
				highScore = lineScan.nextInt();
			}
		} catch (FileNotFoundException e) { 
		}
		// Adds option to reset highScore
		if (controller.getKeyStatus(80)) {
			int answer = JOptionPane.showConfirmDialog(null, "                  PAUSA", ":)", 2);
			controller.resetController();

		}
		// Updates the high score text file if your score exceeds the previous high score
		try {
			if (score > highScore) {
				String scoreString = Integer.toString(score);
				PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
				pw.write(scoreString);
				pw.close();
			}
		} catch (FileNotFoundException e) {
		}

		// Makes enemies move and change direction at borders
		if ((enemyList.get(enemyList.size() - 1).getXPosition() + enemyList.get(enemyList.size() - 1).getXVelocity()) > 760 || (enemyList.get(0).getXPosition() + enemyList.get(0).getXVelocity()) < 0) {
			for (int index = 0; index < enemyList.size(); index++) {
				enemyList.get(index).setXVelocity(enemyList.get(index).getXVelocity() * -1);
				enemyList.get(index).setYPosition(enemyList.get(index).getYPosition() + 10);
			}
		} else {
			for (int index = 0; index < enemyList.size(); index++) {
				enemyList.get(index).move();
			}
		}

		// Move bullet
		if (bullet != null) {
			bullet.setYPosition(bullet.getYPosition() - 20);
			if (bullet.getYPosition() < 0) {
				newBulletCanFire = true;
			}

			// Checks for collisions with normal enemies
			for (int index = 0; index < enemyList.size(); index++) {
				if (bullet.isColliding(enemyList.get(index))) {
					//He borrado el sonido// 			// Plays hitmarker sound if you hit an enemy
					bullet = new Bullet(0, 0, 0, null);
					newBulletCanFire = true;
					// Updates score for normal levels
					if (level != 3 && level != 6 && level != 9 && level != 12 && level != 15) {
						score += 100;
						hitMarker = true;
						markerX = enemyList.get(index).getXPosition(); // Gets positions that the "+ 100" spawns off of
						markerY = enemyList.get(index).getYPosition();
						enemyList.remove(index);

					}
					// Updates score for boss levels
					if (level == 3 || level == 6 || level == 9 || level == 12 || level == 15) {
						hitMarker = true;
						markerX = enemyList.get(index).getXPosition(); // Gets positions that the "- 1" spawns off of
						markerY = enemyList.get(index).getYPosition() + 165;
						bossHealth -= 1;
						if (bossHealth == 0) {
							enemyList.remove(index);
							score += 9000;// Bonus score for defeating boss
						}
					}
				}
			}

			// Checks for collisions with shield and bullets
			for (int index = 0; index < shieldList.size(); index++) {
				if (bullet.isColliding(shieldList.get(index))) {
					// Each if statement changes color of the shield, indicating "strength"
					// STRONG
					if (shieldList.get(index).getColor() == Color.RED) {
						shieldList.remove(index);
						//He borrado el sonido// 
						bullet = new Bullet(0, 0, 0, null);
						newBulletCanFire = true;
						// GOOD
					} else if (shieldList.get(index).getColor() == Color.ORANGE) {
						shieldList.remove(index);
						//He borrado el sonido// 
						bullet = new Bullet(0, 0, 0, null);
						newBulletCanFire = true;
						// OKAY
					} else if (shieldList.get(index).getColor() == Color.YELLOW) {
						shieldList.remove(index);
						//He borrado el sonido// 
						bullet = new Bullet(0, 0, 0, null);
						newBulletCanFire = true;
						// WEAK, BREAKS ON HIT
					} else if (shieldList.get(index).getColor() == Color.WHITE) {
						shieldList.remove(index);
						//He borrado el sonido// 
						bullet = new Bullet(0, 0, 0, null);
						newBulletCanFire = true;
					}
				}
			}
		}
		// Moves bonus enemy
		if (!bonusEnemyList.isEmpty()) {
			for (int index = 0; index < bonusEnemyList.size(); index++) {
				bonusEnemyList.get(index).setXPosition(bonusEnemyList.get(index).getXPosition() + (2));
				if (bonusEnemyList.get(index).getXPosition() > 800) {
					bonusEnemyList.remove(index);
					newBonusEnemy = true;
				}
			}
			// bonus enemy and bullet collsion
			for (int index = 0; index < bonusEnemyList.size(); index++) {
				if (bullet != null) {
					if (bonusEnemyList.get(index).isColliding(bullet)) {
						bonusEnemyList.remove(index);
						bullet = new Bullet(0, 0, 0, null);
						newBulletCanFire = true;
						newBonusEnemy = true;
						//He borrado el sonido//  										// Plays sound if player hits a bonus enemy
						score += 5000; // add bonus to score on hit
					}
				}
			}
		}

		// Moves beams on normal levels
		if (level != 3 && level != 6 && level != 9 && level != 12 && level != 15) {
			if (beam != null) {
				for (int index = 0; index < beamList.size(); index++) {
					beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (4));
					if (beamList.get(index).getYPosition() > 800) {
						beamList.remove(index);
					}
				}
			}
		}
		// Moves beams at a faster speed for boss
		if (level == 3 || level == 6 || level == 9 || level == 12 || level == 15) {
			if (beam != null) {
				for (int index = 0; index < beamList.size(); index++) {
					beamList.get(index).setYPosition(beamList.get(index).getYPosition() + (2 * level)); //Boss beam speed will increase each level
					if (beamList.get(index).getYPosition() > 800) {
						beamList.remove(index);
					}
				}
			}
		}

		// Checks for beam and shield collisions
		try {
			for (int j = 0; j < shieldList.size(); j++) {
				for (int index = 0; index < beamList.size(); index++) {
					if (beamList.get(index).isColliding(shieldList.get(j))) {
						// STRONG
						if (shieldList.get(j).getColor() == Color.RED) {
							shieldList.remove(j);
							//He borrado el sonido// 
							beamList.remove(index);
						} else if (shieldList.get(j).getColor() == Color.ORANGE) {
							shieldList.remove(j);
							//He borrado el sonido// 
							beamList.remove(index);
						} else if (shieldList.get(j).getColor() == Color.YELLOW) {
							shieldList.remove(j);
							//He borrado el sonido// 
							beamList.remove(index);
						} else if (shieldList.get(j).getColor() == Color.WHITE) {
							shieldList.remove(j);
							//He borrado el sonido// 
							beamList.remove(index);
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {
		}

		// Checks for beam and player collisions
		for (int index = 0; index < beamList.size(); index++) {
			if (beamList.get(index).isColliding(playerShip) && !damage) {
				beamList.remove(index);
				//He borrado el sonido// 					// Plays damage sound
				lifeList.remove(lifeList.size() - 1); // Removes life if hit by bullet
				damage = true;
			}
		}

		// Paces beam shooting by only allowing new beams to be fired once all old beams are off screen or have collided
		if (beamList.isEmpty()) {
			newBeamCanFire = true;
		}

		//Destroys shields if aliens collide with them
		for (int input = 0; input < enemyList.size(); input++) {
			for (int j = 0; j < shieldList.size(); j++) {
				if (enemyList.get(input).isColliding(shieldList.get(j))) {
					shieldList.remove(j);
				}
			}
			// If aliens exceed this X Position, you reset the level and lose a life
			if (enemyList.get(input).getYPosition() + 50 >= 750) {
				enemyList.clear();
				shieldList.clear();
				lifeList.clear();
				beamList.clear();
				bossHealth = 30;
				numberOfLives -= 1;
				//He borrado el sonido// 			 // Plays death sound when enemies reach bottom
				setupGame();
			}
		}

		//Updates the life counter display 
		if (playerShip.isColliding) {
			int index = lifeList.size() - 1;
			lifeList.remove(index);
		} 
		// Ends game if player runs out of lives
		else if (lifeList.isEmpty()) {
			dead = true;

			//He borrado el sonido// 			 // Plays death sound when you run out of lives
			if (Ship.timer2 == 299) {
				// Gives the player an option to play again or exit
				int answer = JOptionPane.showConfirmDialog(null, "Would you like to play again?", "You lost the game with " + score + " points", 0);
				// If they choose to play again, this resets every element in the game
				if (answer == 0) {
					lifeList.clear();
					enemyList.clear();
					shieldList.clear();
					beamList.clear();
					bonusEnemyList.clear();
					score = 0;
					level = 1;
					bossHealth = 30;
					numberOfLives = 3;
					newBulletCanFire = true;
					newBeamCanFire = true;
					newBonusEnemy = true;
					setupGame();
					dead = false;
					damage = false;
				}
				// If they choose not to play again, it closes the game
				if (answer == 1) {
					System.exit(0);
				}
			}
		}

		// Goes to next level, resets all lists, sets all counters to correct values
		if (enemyList.isEmpty()) {
			beamList.clear();
			shieldList.clear();
			bonusEnemyList.clear();
			lifeList.clear();

			level += 1;
			bossHealth = 30;
			setupGame();
			//He borrado el sonido//  // Plays level up sound
		}

		// All streams needed for every sound in the game
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// GAME PANEL    

	public GamePanel() {
		// Set the size of the Panel
		this.setSize(gameWidth, gameHeight);
		this.setPreferredSize(new Dimension(gameWidth, gameHeight));
		this.setBackground(Color.BLACK);

		// Register KeyboardController as KeyListener
		controller = new KeyboardController();
		this.addKeyListener(controller);

		// Call setupGame to initialize fields
		this.setupGame();
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	/**
	 * Method to start the Timer that drives the animation for the game. It is
	 * not necessary for you to modify this code unless you need to in order to
	 * add some functionality.
	 */
	public void start() {
		// Set up a new Timer to repeat every 20 milliseconds (50 FPS)
		gameTimer = new Timer(1000 / framesPerSecond, new ActionListener() {

			// Tracks the number of frames that have been produced.
			// May be useful for limiting action rates
			private int frameNumber = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				// Update the game's state and repaint the screen
				updateGameState(frameNumber++);
				repaint();
			}
		});
		Timer gameTimerHitMarker = new Timer(1000, new ActionListener() {

			// Tracks the number of frames that have been produced.
			// May be useful for limiting action rates
			@Override
			public void actionPerformed(ActionEvent e) {
				// Update the game's state and repaint the screen
				hitMarker = false;
			}
		});

		gameTimer.setRepeats(true);
		gameTimer.start();
		gameTimerHitMarker.setRepeats(true);
		gameTimerHitMarker.start();
	}

}
