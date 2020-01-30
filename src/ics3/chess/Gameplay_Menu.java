package ics3.chess;

import javax.swing.*;
import java.awt.*;

/**
 * This class is used to render the gameplay menu screen.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 22nd, 2017
 */
public class Gameplay_Menu extends JPanel {

    // Panel used by the user to indicate which piece they wish to promote their pawn to
    // Is set visible/invisible as needed by the gameflow_controller_engine
    private Promoted_Piece_Panel promoted_piece_panel;

    // The background image
    private Image bckgndIMG;

    private Button backButton;  // Button to come back from the help menu
    private Button restartButton; // Button to restart the game (i.e. "new game")

    // Images for the users and the turn arrows for the users
    private Image user1Image, user2Image;
    private Image user1TurnArrow, user2TurnArrow;

    // Images displayed during a checkmate or a "time up" (every time the game ends)
    private Image checkmateImage, timeUpImage;

    // The font used during rendering
    private Font font;

    /**
     * Constructor for the Gameplay_Menu class
     */
    public Gameplay_Menu() {
        // Initialize the JPanel for the menu screen
        setLocation(0, 0);
        setSize(UI.content_pane_width, UI.content_pane_height);
        setLayout(null);
        setDoubleBuffered(true);

        // Initialize the background image
        bckgndIMG = new Image("data\\gameplay_bckgnd.jpg");

        // Initialize the back and restart buttons

        backButton = new Button(UI.content_pane_width - 185, UI.content_pane_height - 200, 170, 80);
        backButton.setButtonColour(Color.DARK_GRAY);
        backButton.setButtonString("BACK");
        add(backButton);

        restartButton = new Button(UI.content_pane_width - 265, UI.content_pane_height - 110, 255, 80);
        restartButton.setButtonColour(Color.DARK_GRAY);
        restartButton.setButtonString("RESTART");
        add(restartButton);
        restartButton.setVisible(false);

        // Initialize the promoted piece panel
        int promotedPiecePanelXPos = Chess.chess_board.getX() + Chess.chess_board.getWidth();
        int promotedPiecePanelYPos = Chess.chess_board.getY() + Chess.chess_board.getHeight();
        int promotedPiecePanelXSize = 4 * Chess.chess_board.getBoardSquare(0, 0).width;
        int promotedPiecePanelYSize = Chess.chess_board.getBoardSquare(0, 0).height;
        promoted_piece_panel = new Promoted_Piece_Panel(promotedPiecePanelXPos, promotedPiecePanelYPos, promotedPiecePanelXSize, promotedPiecePanelYSize);
        add(promoted_piece_panel);
        setPromotionPanelVisible(false);

        // Initialize the user images
        user1Image = new Image("data\\user.png");
        user1TurnArrow = new Image("data\\turn_arrow_left.png");
        user2Image = new Image("data\\user.png");
        user2TurnArrow = new Image("data\\turn_arrow_right.png");

        // Initialize the font used during rendering
        font = new Font("Times New Roman", Font.PLAIN, 55);

        // Initialize the checkmate image
        checkmateImage = new Image("data\\checkmate_image.png");

        // Initialize the time up image
        timeUpImage = new Image("data\\time_up.png");
    }

    /**
     * Helper method to initialize the promotion panel and its images
     *
     * @param isBlack   Whether or not it is the black player's turn
     */
    public void initializePromotionPanel(boolean isBlack) {
        promoted_piece_panel.initializeImages(isBlack);
        if (isBlack) {
            promoted_piece_panel.setLocation(Chess.chess_board.getX() + Chess.chess_board.getWidth(), Chess.chess_board.getY() + Chess.chess_board.getHeight());
        } else {
            promoted_piece_panel.setLocation(Chess.chess_board.getX() - promoted_piece_panel.getWidth(), Chess.chess_board.getY() - promoted_piece_panel.getHeight());
        }
    }

    /**
     * Setter method used to set the visibility of the promotion panel
     *
     * @param state     Whether or not the promotion panel is to be set visible
     */
    public void setPromotionPanelVisible(boolean state) {
        promoted_piece_panel.setVisible(state);
    }

    /**
     * Setter method for the gameover components (including the restart button)
     *
     * @param state     Whether or not the gameover components should be set visible
     */
    public void setGameoverComponentsVisible(boolean state) {
        restartButton.setVisible(state);
    }

    /**
     * Override the paintComponent() method to render the gameplay menu
     *
     * @param g   The graphics context with which to paint
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Render the background
        g2d.drawImage(bckgndIMG.getImage(), 0, 0, getWidth(), getHeight(), null);

        // Render the user images
        g2d.drawImage(user1Image.getImage(), (int) (0.03 * getWidth()), (int) (0.01 * getHeight()), 200, 200, null);
        g2d.drawImage(user2Image.getImage(), (int) (0.80 * getWidth()), (int) (0.01 * getHeight()), 200, 200, null);

        // Render the appropriate turn arrow to indicate which player's turn it is
        if (Chess.gameflow_controller_engine.isBlackTurn()) {
            g2d.drawImage(user2TurnArrow.getImage(), (int) (0.77 * getWidth()), (int) (0.35 * getHeight()), 100, 100, null);
        } else {
            g2d.drawImage(user1TurnArrow.getImage(), (int) (0.13 * getWidth()), (int) (0.35 * getHeight()), 100, 100, null);
        }

        // Render the appropriate image when the game is over
        if (Chess.gameflow_controller_engine.isGameOver()) {
            // If the game ended due to a checkmate, display the checkmate image
            if (Chess.gameflow_controller_engine.isLostDueToCheckmate()) {
                g2d.drawImage(checkmateImage.getImage(), (int) (0.5 * getWidth() - 320), (int) (0.02 * getHeight()), 620, 150, null);
            }
            // Otherwise, the game ended due to a "time up", so render that
            else {
                g2d.drawImage(timeUpImage.getImage(), (int) (0.5 * getWidth() - 320), (int) (0.02 * getHeight()), 620, 150, null);
            }
            // Display who won the game
            g2d.setColor(Color.ORANGE);
            g2d.setFont(font);
            if (Chess.whitePlayer.didPlayerWinGame()) {
                g2d.drawString(Chess.whitePlayer.getPlayerName() + " WON", (int) (0.5 * getWidth() - 320), (int) (0.01 * getHeight()) + 150 + font.getSize());
            } else {
                g2d.drawString(Chess.blackPlayer.getPlayerName() + " WON", (int) (0.5 * getWidth() - 320), (int) (0.01 * getHeight()) + 150 + font.getSize());
            }
        }
    }
}
