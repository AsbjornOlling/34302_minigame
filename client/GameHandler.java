/*
 * Panel containing game content
 * and utilities to load and render games
 */

import java.util.*;

// gui stuff
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameHandler extends JPanel 
								implements PacketListener, ActionListener {
	MinigameClient parent;

	// window stuff
	final int WIDTH, HEIGHT;
	JButton gameStartBtn;

	// game objects
	int[] gamesList;
	int currentGameIdx;

	// constructor
	public GameHandler(MinigameClient parent) {
		this.parent = parent;
		this.WIDTH = parent.GAMEWIDTH;
		this.HEIGHT = parent.GUIHEIGHT;

		this.setLayout(new GridLayout(1, 1));

		// add packet listener
		String[] hdrs = {"SESSIONJOINED", "GAMESTART", "ROUNDOVER"};
		Mediator.getInstance().addListener(this, hdrs);
	} // constructor


	// show screen before GAMESTART
	public void showLobbyScreen() {
		System.out.println("INFO: Showing lobby screen.");
		this.removeAll();

		// make lobby panel
		JPanel lobPanel = new JPanel();
		lobPanel.setLayout(new BorderLayout());
	
		// show startgame button if host
		if (parent.host) {
			lobPanel.add(new JLabel("YOU'RE THE HOST", 
									JLabel.CENTER), BorderLayout.PAGE_START);

			gameStartBtn = new JButton("START GAME");
			gameStartBtn.addActionListener(this);
			lobPanel.add(gameStartBtn, BorderLayout.PAGE_END);
		} else {
			lobPanel.add(new JLabel("WAITING FOR GAMES TO START", 
									JLabel.CENTER), BorderLayout.PAGE_START);
		}

		// add and draw
		this.add(lobPanel);
		this.revalidate();
		this.repaint();
	} // showLobby

	public void actionPerformed(ActionEvent e) {
		JButton eSource = (JButton) e.getSource();
		if (eSource == gameStartBtn) {
			System.out.println("DEBUG: Startgame button hit.");

			// send packet
			parent.server.sendGameStart();
		}
	}

	// show this screen when all games completed
	public void showGameOverScreen() {
		System.out.println("INFO: Showing gameover screen.");
		this.removeAll();

		// make panel containing gameover screen
		int w = 1;
		int h = 2;
		JPanel GOPanel = new JPanel();
		GOPanel.setLayout(new GridLayout(h, w));
		GOPanel.add(new JLabel("GAME OVER", JLabel.CENTER));
		GOPanel.add(new JLabel("Wait for other players to finish.", JLabel.CENTER));
		this.add(GOPanel);
	} // showGameOver


	// start games
	public void startPlaying() {
		// testing
		showGameOverScreen();

		System.out.println("INFO: Starting a round of minigames");
		// start with first game in list
		currentGameIdx = 0;
		loadNextGame();
	} // start()


	// load next game from list
	public void loadNextGame() {
		int currentGameNo = gamesList[currentGameIdx];
		Game nextGame = getGameNo(currentGameNo);
		loadGame(nextGame);
	} // loadNextGame


	// fetch game with specific GameID
	public Game getGameNo(int no) {
		Game g;
		// one button game
		if ( no == 0 ) {
			System.out.println("INFO: Fetching onebutton game");
			g = new ClickTenTimes(this);
		}
		// many buttons game
		else if ( no == 1 ) {
			System.out.println("INFO: Fetching manybutton game");
			g = new GreenSquareGame(this);
		}
		// hit key game
		else if ( no == 2 ) {
			System.out.println("INFO: Fetching keyboard game");
			g = new HitKeyGame(this);
		}
		// invalid game
		else {
			System.out.println("ERROR: Tried to fetch invalid gameno.");
			g = null;
		}
		return g;
	} // getGameNo


	// initialize a minigame
	public void loadGame(Game game) {
		// clear screen
		this.removeAll();

		// add new game
		this.add(game);

		// start game thread
		Thread t = new Thread(game);
		t.start();

		// draw new gamescreen
		this.revalidate();
		this.repaint();
	} // loadGame


	// send gameComplete message and load the next game
	public void gameComplete(int score) {
		parent.server.sendGameComplete(score);

		currentGameIdx++;
		// detect end of gameslist
		if (currentGameIdx > gamesList.length - 1) {
			System.out.println("INFO: All games complete!");
			showGameOverScreen();
		} else {
			System.out.println("INFO: Minigame over. Loading next one.");
			loadNextGame();
		}
	} // gameComplete

	
	// handle incoming packets
	public void recvPacket(Packet pck) {
		// enter lobby
		if (pck.HEADER.equals("SESSIONJOINED")) {
			gamesList = pck.GAMES;
			showLobbyScreen();
		}
		// start game
		else if (pck.HEADER.equals("GAMESTART")) {
			System.out.println("INFO: Starting new game on command from server.");
			startPlaying();
		}
		// round over
		else if (pck.HEADER.equals("ROUNDOVER")) {
			System.out.println("INFO: Round winner found.");

			// show dialog with winner
			JOptionPane.showMessageDialog(this, 
					"Winner found: "+parent.scoreboard[0][0]);

			// throw player back into lobby
			showLobbyScreen();
		}
	} // recvPacket


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
			this.update();
		} 
		System.out.println("TERMINATING GAME");
		finishGame();
	} // thread loop


	// return full size of handler
	public Dimension getPreferredSize() {
		return new Dimension(handler.WIDTH, handler.HEIGHT);
	} // getPreferredSize
} // Game


/* 
 * GAMEID: 0
 * ClickTenTimes: A game about a button
 */
class ClickTenTimes extends Game {
	private JButton tenClickButton;
	private Integer clicksLeft;
	private long startTime;

	// constructor 
	public ClickTenTimes(GameHandler handler) {
		super(handler);

		// set simple layout
		this.setLayout(new GridLayout(3, 3));

		makeButton();

		// init button
		clicksLeft = 10;

		// get time and start game
		startTime = System.currentTimeMillis();
	} // constructor


	// make btn
	private void makeButton() {
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
	} // makeBtn


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
		System.out.println("DEBUG: Game completed in " + timePassed);
		
		// calculate score
		int score = (int) (100 - (timePassed / 100));

		// show game over screen for some time
		tenClickButton.setText("DONE");

		// sleep for a time
		try { Thread.sleep(gameOverScreenTime); } catch (Exception e) {}

		// send gamescore, load new game
		handler.gameComplete(score);
	} // finishGame
} // ButtonGame


/*
 * GAMEID: 1
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
		correctButton = new JButton("CLICK ME!");
		correctButton.addActionListener(this);
		makeButtons();

		// start counter
		correctButtonClicks = 0;

		// start timer
		startTime = System.currentTimeMillis();
	} // constructor


	// make buttons
	public void makeButtons() {
		System.out.println("DEBUG: Making buttons board.");

		// clear gamescreen
		this.removeAll();

		// decide true button placement
		int trueButtonNo = r.nextInt(noOfButtons);
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

		// draw new board
		this.revalidate();
		this.repaint();
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
		int score = correctButtonClicks * 10;
		System.out.println("DEBUG: Completed game with "+score);

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
			makeButtons();
		}
	} // actionPerformed
} // GreenSquareGame


/*
 * GAMEID: 2
 * Game about hitting a key on your keyboard
 */
class HitKeyGame extends Game implements KeyListener {
	// constant params
	int keysToHit = 5;
	int perfectTime = 10; // in seconds
	String alphabet = "abcdefghijklmopqrstuvxyz";
	String idleText = "Wait...";

	// game objects
	Random r;
	char targetChar;
	JLabel label;
	boolean keyHit;
	int keysHit;
	long totalTime;

	// constructor
	public HitKeyGame(GameHandler handler) {
		super(handler);
		r = new Random();

		// start counters
		keysHit = 0;
		totalTime = 0;

		// make jlabel
		label = new JLabel(idleText);
		this.add(label);

		// accept focus
		setFocusable(true);

		// add keylistener
		addKeyListener(this);
	} // constr


	// run in thread loop
	public void update() {
		// DEMAND FOCUS
		requestFocus();

		// make random char at random interval
		int randInt = r.nextInt(5);
		if (randInt == 0) {
			System.out.println("DEBUG: Generating random char.");
			// get random char
			targetChar = alphabet.charAt(r.nextInt(alphabet.length()));

			// set jlabel text and start timer
			label.setText("QUICK! HIT: " + targetChar);
			long timeStart = System.currentTimeMillis();

			// wait for key to be hit
			keyHit = false;
			while (!keyHit) {
				try { Thread.sleep(10); } catch (Exception e) {}
			} // exit loop when key hit
			System.out.println("DEBUG: Key hit");
			
			// count time and reset 
			long timeStop = System.currentTimeMillis();
			long timeToHit = timeStop - timeStart;
			totalTime += timeToHit;
			targetChar = ' ';
			keysHit++;
			label.setText(idleText);
		} else {
			// ZZZzzzzz....
			try { Thread.sleep(200); } catch (Exception e) {}
		}

		// exit game when no of keys hit
		if (keysHit >= keysToHit) {
			shouldRun = false;
		}
	} // update()


	// when exiting main game loop
	public void finishGame() {
		System.out.println("INFO: Finishing keyboard game");
		System.out.println("DEBUG: Completed keyboard game in "+totalTime);
		label.setText("DONE");

		// calculate score
		int score = (int) (100 - (totalTime / 100));

		try { Thread.sleep(gameOverScreenTime); } catch (Exception e) {}

		handler.gameComplete(score);
	} // finishGame


	public void keyPressed(KeyEvent e) {
		char charHit = e.getKeyChar();
		if (charHit == targetChar) {
			keyHit = true;
		}
		System.out.println("DEBUG: Hit key "+charHit);
	} //keyTyped


	// required unused methods
	public void keyReleased(KeyEvent e) { } 
	public void keyTyped(KeyEvent e) { }
	public void actionPerformed(ActionEvent e) { }
} // hitkeygame


   /*\
   ***
   ***
   *** 
   *** 
 __***__
/*******\
\*******/
