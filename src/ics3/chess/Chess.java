package ics3.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


/**
 * This is the main class for the Chess game (which is the project I chose to undertake for the ICS3U3
 * summative). In this game, two players play against each other on
 * 8x8 board, and the goal of each player is to place the opponent's king
 * in checkmate. There is a tutorial to learn how to play chess within the program
 * (to open this tutorial, press the help button). There are also two levels
 * of difficulty: one setting shows all possible moves, and the other
 * doesn't show any moves (you must find them yourself). There are various options
 * to customize the game (e.g. whether or not the game is timed). This program has been
 * developed according to the MVC paradigm.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 31st, 2017
 */
public class Chess {

    // UI object (represents the view in the MVC paradigm)
    public static UI ui;
    // Controller engines
    public static UI_Controller_Engine ui_controller_engine; // Special controller for the UI
    public static Gameflow_Controller_Engine gameflow_controller_engine; // Special controller for the game

    // The chess board, which consists of the pieces and the board squares
    public static Chess_Board chess_board;

    // The various menu-screens in the application
    public static Main_Menu main_menu;
    public static Help_Menu help_menu;
    public static Gameplay_Menu gameplay_menu;

    // Objects for the two players
    public static Player whitePlayer, blackPlayer;

    /**
     * Constructor for the Chess class
     */
    private Chess() {
        // Initialize the ui
        ui = new UI(Toolkit.getDefaultToolkit().getScreenSize().width - 100, Toolkit.getDefaultToolkit().getScreenSize().height - 50);
        ui.setVisible(true);

        // Initialize the chess board
        chess_board = new Chess_Board(UI.content_pane_width / 2 - (4 * 70) - (4 * 5), UI.content_pane_height / 2 - (4 * 70) - (4 * 5) + 75, 70, 70, 3, "data\\wooden_border_chessboard.jpg", "data\\pieces_images\\", 20);

        // Initialize the various menu-screens in the game
        main_menu = new Main_Menu();
        help_menu = new Help_Menu();
        gameplay_menu = new Gameplay_Menu();

        // Initialize the controller engines
        ui_controller_engine = new UI_Controller_Engine();
        gameflow_controller_engine = new Gameflow_Controller_Engine();

        // Initialize the player objects
        whitePlayer = new Player((int) (0.03 * gameplay_menu.getWidth()), (int) (0.01 * gameplay_menu.getHeight()), 200, 200, 500, 100, 45, "data\\user.png", "data\\clock_img.jpg", 600, null);
        gameplay_menu.add(whitePlayer);
        blackPlayer = new Player((int) (0.80 * gameplay_menu.getWidth()), (int) (0.01 * gameplay_menu.getHeight()), 200, 200, 500, 100, 45, "data\\user.png", "data\\clock_img.jpg", 600, null);
        gameplay_menu.add(blackPlayer);

        // Initialize the menu
        initMenu();

        // Add all the components to the ui
        addComponentsToUI();

        // Start the timer (which effectively starts the application)
        ui_controller_engine.startTimer();
    }

    /**
     * Method used to initialize the menuBar and its constituent menuItems
     */
    private void initMenu() {
        // Menu that contains the settings for the difficulty of the game
        JMenu difficultyMenu = new JMenu("Difficulty");
        difficultyMenu.setMnemonic(KeyEvent.VK_S);
        difficultyMenu.getAccessibleContext().setAccessibleDescription("Set preferences pertaining to the difficulty of the game");

        // CheckBoxMenuItem that allows the user to select whether or not they want to display the high score on the screen
        // Add the ui_controller_engine as the actionListener since it is responsible for determining what happens next
        JCheckBoxMenuItem showPossibleMoves = new JCheckBoxMenuItem("Show possible moves");
        showPossibleMoves.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        showPossibleMoves.getAccessibleContext().setAccessibleDescription("Set whether or not a player's possible moves should be shown");
        showPossibleMoves.setState(true);
        showPossibleMoves.addActionListener(gameflow_controller_engine);

        // Add the showPossibleMoves CheckBoxMenuItem to the difficultyMenu
        difficultyMenu.add(showPossibleMoves);

        // Add the menus to the menuBar of the frame
        ui.addMenuToMenuBar(difficultyMenu);


        // Menu for preferences relating to time limits
        JMenu timeMenu = new JMenu("Time Preferences");
        timeMenu.setMnemonic(KeyEvent.VK_T);
        timeMenu.getAccessibleContext().setAccessibleDescription("Set preferences regarding the time that each player has");

        // CheckBoxMenuItem for whether or not the game is to be timed
        JCheckBoxMenuItem isGameTimed = new JCheckBoxMenuItem("Is game timed");
        isGameTimed.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        isGameTimed.getAccessibleContext().setAccessibleDescription("Set whether or not each player has a time limit");
        isGameTimed.setState(true);
        isGameTimed.addActionListener(gameflow_controller_engine);

        timeMenu.add(isGameTimed);

        // Menu to select the time limit for each player
        JMenu timeSelector = new JMenu("Select time limit");
        timeSelector.setMnemonic(KeyEvent.VK_U);
        timeSelector.getAccessibleContext().setAccessibleDescription("Set the time limit for each player");

        JMenuItem[] selectableTimes = new JMenuItem[8];
        selectableTimes[0] = new JMenuItem("1 min");
        selectableTimes[1] = new JMenuItem("3 min");
        selectableTimes[2] = new JMenuItem("5 min");
        selectableTimes[3] = new JMenuItem("10 min");
        selectableTimes[4] = new JMenuItem("15 min");
        selectableTimes[5] = new JMenuItem("30 min");
        selectableTimes[6] = new JMenuItem("60 min");
        selectableTimes[7] = new JMenuItem("120 min");

        for (JMenuItem jMenuItem : selectableTimes) {
            timeSelector.add(jMenuItem);
            jMenuItem.addActionListener(gameflow_controller_engine);
        }

        timeMenu.add(timeSelector);
        // By default, the timed option is set false
        timeSelector.setEnabled(true);

        ui.addMenuToMenuBar(timeMenu);

        // Menu used to set player preferences
        JMenu playerPreferenceMenu = new JMenu("Player Preferences");
        playerPreferenceMenu.setMnemonic(KeyEvent.VK_U);
        playerPreferenceMenu.getAccessibleContext().setAccessibleDescription("Set player preferences, including name and picture colour");

        // MenuItem for setting the white player's name
        JMenuItem whitePlayerNameSetter = new JMenuItem("Set white player's name");
        whitePlayerNameSetter.addActionListener(ui_controller_engine);
        playerPreferenceMenu.add(whitePlayerNameSetter);

        // Menu item for setting the black player's name
        JMenuItem blackPlayerNameSetter = new JMenuItem("Set black player's name");
        blackPlayerNameSetter.addActionListener(ui_controller_engine);
        playerPreferenceMenu.add(blackPlayerNameSetter);

        ui.addMenuToMenuBar(playerPreferenceMenu);
    }

    /**
     * Helper method for adding components/panels to the UI
     */
    private void addComponentsToUI() {
        ui.add(chess_board);

        ui.add(main_menu);
        ui.add(help_menu);
        ui.add(gameplay_menu);
    }

    /**
     * Entry point to the program, which adds the game to the Event Dispatching Thread
     *
     * @param args Unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Chess();
            }
        });
    }
}