package ics3.chess;

import javax.swing.*;
import java.awt.*;

/**
 * This class is used to store the data for each player, as well as
 * display the player's name, icon, and their clock.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 25th, 2017
 */
public class Player extends JComponent {

    // The player image
    private Image playerImage;

    // The player clock for each player
    private Player_Clock player_clock;

    // Whether or not the player has won the game
    private boolean wonGame;

    // The height of the player image
    private int heightPlayerImage;

    // The name of the player
    private String playerName;
    // The height of the text (the one that displays the player's name)
    private int heightText;

    // The font with which rendering is done
    private Font textFont;

    /**
     * Default constructor for the Player class
     */
    public Player() {
        this(0, 0, 0, 0, 0, 0, 0, "", "", 0, "");
    }

    /**
     * Constructor for the Player class
     *
     * @param xPos                  The x-coordinate of the top left corner of the JComponent
     * @param yPos                  The y-coordinate of the top left corner of the JComponent
     * @param width                 The width of the JComponent
     * @param heightPlayerImage     The height of the player image
     * @param yPosClock             The y-coordinate of the top left corner of the clock image
     * @param heightClock           The height of the clock image
     * @param heightText            The height of the text (where the player's name is displayed)
     * @param playerImgLoc          The location of the player image
     * @param clockImgLoc           The location of the clock image
     * @param playerTimeLimitSeconds    The time limit for each player (in seconds)
     * @param playerName    The name of the player
     */
    public Player(int xPos, int yPos, int width, int heightPlayerImage, int yPosClock, int heightClock, int heightText, String playerImgLoc, String clockImgLoc, int playerTimeLimitSeconds, String playerName) {
        // Initialize the above properties based on the parameters passed in to the constructor
        this.playerName = playerName;
        playerImage = new Image(playerImgLoc);

        this.heightPlayerImage = heightPlayerImage;
        this.heightText = heightText;

        // At the beginning, the player hasn't won the game
        wonGame = false;

        // Initialize the player clock and set it visible by default
        player_clock = new Player_Clock(0, yPosClock, width, heightClock, clockImgLoc, playerTimeLimitSeconds, "data\\clock_font\\digital-7.ttf");
        add(player_clock);
        player_clock.setVisible(true);

        // Initialize the font
        textFont = new Font("Times New Roman", Font.PLAIN, heightText);

        // Initialize the JComponent
        setLocation(xPos, yPos);
        setSize(width, heightPlayerImage + yPosClock + heightClock + heightText);
        setLayout(null);
        setDoubleBuffered(true);
    }

    /**
     * Getter method for the player clock
     *
     * @return Player_Clock     The timer/clock for the player
     */
    public Player_Clock getPlayerClock() {
        return player_clock;
    }

    /**
     * Getter method for whether or not the player won the game
     *
     * @return boolean  Whether or not the player won the game
     */
    public boolean didPlayerWinGame() {
        return wonGame;
    }

    /**
     * Setter method for whether or not the player won the game
     *
     * @param state     Whether or not the player won the game
     */
    public void setWonGame(boolean state) {
        wonGame = state;
    }

    /**
     * Getter method for the player's name
     *
     * @return String   The player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Setter method for the player's name
     *
     * @param playerName    The player's name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Override the paintComponent() method to render the chess board and the pieces
     *
     * @param g The graphics context with which to paint
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Render the player image
        g2d.drawImage(playerImage.getImage(), 0, 0, getWidth(), heightPlayerImage, null);

        // Display the player's name
        g2d.setColor(Color.ORANGE);
        g2d.setFont(textFont);
        g2d.drawString(playerName, 0, heightPlayerImage + heightText);
    }
}
