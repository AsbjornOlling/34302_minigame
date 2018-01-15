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

	// constructor
	public GameHandler(MinigameClient parent) {
		this.parent = parent;
		this.WIDTH = parent.GAMEWIDTH;
		this.HEIGHT = parent.GUIHEIGHT;

		this.setLayout(new GridLayout(1, 1));

		// testing - load button game
		// this.loadGame(new ClickTenTimes(this));
		this.loadGame(new GreenSquareGame(this));
	} // constructor


	// initialize a minigame
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
		// sendGameComplete(pName, sessionID, score)
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
	boolean shouldRun;

	// constructor
	public Game(GameHandler handler) {
		this.handler = handler;
		shouldRun = true;
	} // constructor

	// calculate score and send to handler
	public abstract void finishGame();
	// update on every runloop
	public abstract void update();

	// thread loop
	public void run() {
		while (shouldRun) {
			update();
		} 
		finishGame();
	} // thread loop


	// return full size of handler
	public Dimension getPreferredSize() {
		return new Dimension(handler.WIDTH, handler.HEIGHT);
	} // getPreferredSize
} // Game


// ClickTenTimes: A game about a button
class ClickTenTimes extends Game {
	private boolean shouldRun;
	private JButton tenClickButton;
	private Integer clicksLeft;
	private long startTime;

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
	} // constructor



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
			// exit thread loop
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


/*
 * Game about clicking the correct square
 */
class GreenSquareGame extends Game {
	// game params
	int timeToPlay = 10 * 1000; // in milis
	int gWidth = 8;
	int gHeight = 25;
	int noOfButtons = gHeight*gWidth;

	// game objects
	Random r;
	JButton correctButton;
	long startTime;
	int correctButtonClicks;

	// constructor
	public GreenSquareGame(GameHandler handler) {
		super(handler);
		setLayout(new GridLayout(gHeight, gWidth));
		r = new Random();

		// make buttons
		correctButton = new JButton("CLICKME");
		correctButton.addActionListener(this);
		makeButtons();

		// start counter
		correctButtonClicks = 0;

		// start timer
		startTime = System.currentTimeMillis();
	} // constructor


	// make buttons
	public void makeButtons() {
		System.out.println("DEBUG: Making buttons board");

		// clear gamescreen
		this.removeAll();
		this.revalidate();
		this.repaint();

		// decide true button placement
		int trueButtonNo = r.nextInt(noOfButtons);
		System.out.println("DEBUG: New btn no " + trueButtonNo);
		// make all the buttons
		for ( int i = 0; i < noOfButtons; i++ ) {
			if ( i == trueButtonNo ) {
				// make nice button
				add(correctButton);
			} else {
				// make bullshit button
				JButton badButton = new JButton("DON'T CLICK");
				add(badButton);
			}
		} // loop
	} // makebuttons()


	// update on every loop
	public void update() {
		// check time passed
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - startTime;

		// if playtime exceeded, end game
		if (timePassed > timeToPlay) {
			System.out.println("DEBUG: Gametime exceeded");
			shouldRun = false;
		}
	} // run()


	// show end screen and submit score
	public void finishGame() {
		int score = correctButtonClicks;
		// TODO show end screen
		try { Thread.sleep(gameOverScreenTime); } catch (Exception e) {}

		// send gamescore, load new game
		handler.gameComplete(score);
	} // finishGame


	// handle button events
	public void actionPerformed(ActionEvent e) {
		Object eSource = e.getSource();
		String eSourceClass = eSource.getClass().getName().toString(); 
		if (eSourceClass == "javax.swing.JButton") {
			// if correct button clicked
			if ((JButton) eSource == correctButton ) {
				System.out.println("DEBUG: Correct button clicked.");
				correctButtonClicks++;
			} 
			// if not correct button
			else { 
				System.out.println("DEBUG: Bad button clicked.");
				correctButtonClicks--;
			}

			// make new board regardless of button licked
			System.out.println("DEBUG: making new board of buttons.");
			makeButtons();
		}
	} // actionPerformed
} // GreenSquareGame


   /*\
   ***
   ***
   *** 
   ***
   *** 
   *** 
   *** 
/*******\
\*******/
