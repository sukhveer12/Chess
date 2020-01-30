package ics3.chess;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is the "ui_controller_engine" class described in the MVC paradigm. It deals with the flow of the
 * program as it is the interface between the Model objects and the UI. It determines what actions
 * should be performed when. It determines what should be done when the button is pressed. It also has
 * a method that updates all the objects' properties.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 17th, 2017
 */

public class UI_Controller_Engine implements ActionListener {

    // Timer object used for running the program
    private Timer timer;

    /**
     * Default constructor of the UI_Controller_Engine class
     * It sets up the timer object, which is used to update the application
     */
    public UI_Controller_Engine() {
        // Set up the timer that will be responsible for updating the program
        // It has a delay of 1 second, since every second the clock needs to be updated
        // (since the second hand is supposed to move after 1 second)
        timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the updateApplication() method, which performs the necessary updating
                updateApplication();
            }
        });

        initScreen("main menu");
    }

    /**
     * This method repaints the ui and performs any movements and collisions
     * (i.e. "runs the application")
     */
    private void updateApplication() {
        // Repaint the ui
        Chess.ui.repaint();
        if (Chess.gameplay_menu.isVisible()) {
            Chess.gameflow_controller_engine.updatePlayerTime();
        }
    }

    /**
     * @param screenToDisplay Represents the screen which is to be displayed
     */
    private void initScreen(String screenToDisplay) {
        // Based on what screen is to be displayed, various
        // components' are set visibile and others invisible.
        switch (screenToDisplay) {
            case "main menu":
                Chess.main_menu.setVisible(true);
                Chess.help_menu.setVisible(false);
                Chess.gameplay_menu.setVisible(false);
                Chess.chess_board.setVisible(false);
                break;
            case "help menu":
                Chess.main_menu.setVisible(false);
                Chess.help_menu.setVisible(true);
                Chess.gameplay_menu.setVisible(false);
                Chess.chess_board.setVisible(false);
                break;
            case "gameplay menu":
                Chess.main_menu.setVisible(false);
                Chess.help_menu.setVisible(false);
                Chess.gameplay_menu.setVisible(true);
                Chess.chess_board.setVisible(true);
                break;
            default:
                System.err.println("No behaviour implemented for parameter " + screenToDisplay + " in UI_Controller_Engine.initScreen()");
        }
    }

    /**
     * Method that starts the timer (which has private access)
     */
    public void startTimer() {
        timer.start();
    }

    /**
     * Perform the appropriate action every time the mouse is pressed on a button
     * This is another interface between the ui_controller_engine and the ui (including objects,
     * such as buttons, which handle user input) since this function is called by the ui
     * (or the helper objects) every time the mouse is pressed
     */
    public void handleMousePressedButton(Button pressedButton) {
        // Perform the appropriate action based on what button is pressed
        // Note: this method determines the appropriate behaviour based on the label of the button
        switch (pressedButton.getButtonString()) {
            case "START":
                if (Chess.whitePlayer.getPlayerName() == null) {
                    String desiredNameForWhitePlayer = JOptionPane.showInputDialog(Chess.ui, "Enter white player's name:");
                    if (desiredNameForWhitePlayer == null || desiredNameForWhitePlayer.equals("")) {
                        desiredNameForWhitePlayer = "White";
                    }
                    Chess.whitePlayer.setPlayerName(desiredNameForWhitePlayer);
                }
                if (Chess.blackPlayer.getPlayerName() == null) {
                    String desiredNameForBlackPlayer = JOptionPane.showInputDialog(Chess.ui, "Enter black player's name:");
                    if (desiredNameForBlackPlayer == null || desiredNameForBlackPlayer.equals("")) {
                        desiredNameForBlackPlayer = "Black";
                    }
                    Chess.blackPlayer.setPlayerName(desiredNameForBlackPlayer);
                }
                if (!Chess.gameflow_controller_engine.isGameOver()) {
                    int wantToRestart = JOptionPane.showConfirmDialog(Chess.ui, "A game is in progress. Continue the current game?");
                    // If the user wants to start a new game
                    if (wantToRestart == 1) {
                        Chess.gameplay_menu.setGameoverComponentsVisible(false);
                        Chess.gameflow_controller_engine.restartGame();
                    }
                    // Else if they cancel the request to go to the gameplay menu
                    else if (wantToRestart == 2) {
                        break;
                    }
                    System.out.println(wantToRestart);
                } else {
                    Chess.gameplay_menu.setGameoverComponentsVisible(false);
                    Chess.gameflow_controller_engine.restartGame();
                }
                initScreen("gameplay menu");
                break;
            case "HELP":
                initScreen("help menu");
                break;
            case "RESTART":
                Chess.gameplay_menu.setGameoverComponentsVisible(false);
                Chess.gameflow_controller_engine.restartGame();
                break;
            case "BACK":
                initScreen("main menu");
                break;
            default:
                System.err.println("BEHAVIOUR HASN'T BEEN IMPLEMENTED FOR " + pressedButton.getButtonString() + " BUTTON");
        }
    }

    /**
     * Implementation of the actionPerformed() method in the ActionListener interface. It is used
     * whenever a menu is pressed.
     *
     * @param e     The ActionEvent that was passed in by the menubar
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItemPressed = (JMenuItem) (e.getSource());
        switch (menuItemPressed.getText()) {
            case "Set white player's name":
                String desiredNameForWhitePlayer = JOptionPane.showInputDialog(Chess.ui, "Enter white player's name:");
                if (desiredNameForWhitePlayer == null || desiredNameForWhitePlayer.equals("")) {
                    desiredNameForWhitePlayer = "White";
                }
                Chess.whitePlayer.setPlayerName(desiredNameForWhitePlayer);
                break;
            case "Set black player's name":
                String desiredNameForBlackPlayer = JOptionPane.showInputDialog(Chess.ui, "Enter black player's name:");
                if (desiredNameForBlackPlayer == null || desiredNameForBlackPlayer.equals("")) {
                    desiredNameForBlackPlayer = "Black";
                }
                Chess.blackPlayer.setPlayerName(desiredNameForBlackPlayer);
                break;
            default:
                System.err.println("BEHAVIOUR FOR MENU BUTTON '" + menuItemPressed.getText() + "' NOT IMPLEMENTED IN UI CONTROLLER");
        }
    }
}
