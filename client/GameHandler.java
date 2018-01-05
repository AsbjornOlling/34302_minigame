/*
 * Panel containing game content
 * and utilities to load and render games
 */

import java.util.*;

// gui stuff
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameHandler extends JPanel {
	int WIDTH, HEIGHT;

	// constructor
	public GameHandler(int WIDTH, int HEIGHT) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;

		// just for testing - a box with the full dimensions
		this.setLayout(new GridLayout(1,1));
		this.add(Box.createRigidArea(new Dimension(WIDTH, HEIGHT)));
	} // constructor


	// construct and display a minigame
	public void loadGame(Game game) {
		this.removeAll();
		this.add(game);
	} // Game

	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	} // getPreferredSize

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	} // paintComponent
} // GameHandler


// parent class for all minigames
abstract class Game extends JPanel {

	// constructor
	public Game() {
	} // constructor

	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	} // getPreferredSize
} // Game


class ButtonGame extends Game {
	// constructor 
	public ButtonGame() {
	} // constructor
} // ButtonGame


   /*\
   ***
   *** 
   ***
   ***
 *******
\*******/
