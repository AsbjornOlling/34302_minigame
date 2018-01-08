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
	MinigameClient parent;

	final int WIDTH, HEIGHT;
	String playerName = "dinmor";

	// constructor
	public GameHandler(MinigameClient parent) {
		this.parent = parent;
		this.WIDTH = parent.GAMEWIDTH;
		this.HEIGHT = parent.GUIHEIGHT;

		this.setLayout(new GridLayout(1, 1));

		// testing - load button game
		this.loadGame(new ClickTenTimes(this));
	} // constructor


	// display a minigame
	public void loadGame(Game game) {
		// clear screen and add new game
		this.removeAll();
		this.add(game);

		// start game thread
		Thread t = new Thread(game);
		t.start();
	} // loadGame


	// send gameComplete message
	// and load the next game
	public void gameComplete(int score) {
		// send GAMECOMPLETE message

		// load new game
		loadGame(new ClickTenTimes(this));
	} // gameComplete


	// return gamewindow dimension
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	} // getPreferredSize


	// do something, idk: this one is required
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	} // paintComponent
} // GameHandler


// parent class for all minigames
abstract class Game extends JPanel implements Runnable, ActionListener {
	GameHandler handler;
	int gameOverScreenTime = 500;


	// constructor
	public Game(GameHandler handler) {
		this.handler = handler;
	} // constructor


	// return full size of handler
	public Dimension getPreferredSize() {
		return new Dimension(handler.WIDTH, handler.HEIGHT);
	} // getPreferredSize
} // Game


// ClickTenTimes: A game about a button
class ClickTenTimes extends Game {
	boolean shouldRun;
	JButton tenClickButton;
	Integer clicksLeft;
	long startTime;


	// constructor 
	public ClickTenTimes(GameHandler handler) {
		super(handler);

		// set simple layout
		this.setLayout(new GridLayout(3, 3));

		// eight empty spaces and one button
		for (int i = 0; i < 9; i++) {	
			if (i != 4) {
				// add empty spacer
				add(Box.createRigidArea(
						new Dimension(handler.WIDTH/3, handler.HEIGHT/3)));
			} else {
				// place button in center field
				tenClickButton = new JButton();
				tenClickButton.addActionListener(this);
				add(tenClickButton);
			}
		} // grid loop

		// init button
		clicksLeft = 10;
		update();

		// get time and start game
		startTime = System.currentTimeMillis();
		shouldRun = true;
	} // constructor


	// thread loop
	public void run() {
		while (shouldRun) {
			update();
		} 
		finishGame();
	} // thread loop


	// event listener
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == tenClickButton) {
			clicksLeft--;
		}
	} // event listener


	// update button state
	public void update() {
		tenClickButton.setText(clicksLeft.toString());

		// check for terminating gamestate
		if (clicksLeft <= 0) {
			shouldRun = false;
		} 
	} // setButtonText


	// calculate and send score
	public void finishGame() {
		// calculate time to complete
		long endTime = System.currentTimeMillis();
		long timePassed = endTime - startTime;
		System.out.println("GAME COMPLETED IN: "+timePassed);
		
		// calculate score TODO make score of max 100
		int score = (int) timePassed;
		System.out.println("SCORE GOT: "+score);

		// show game over screen for some time
		tenClickButton.setText("DONE");

		// sleep for a time
		try { Thread.sleep(gameOverScreenTime); } catch (Exception e) {}

		// send gamescore, load new game
		handler.gameComplete(score);
	} // finishGame
} // ButtonGame



   /*\
   ***
   *** 
   ***
   *** 
   *** 
   *** 
/*******\
\*******/

