package ics3.chess;

import javax.swing.*;
import java.awt.*;

/**
 * This class is used to render the main menu screen.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 22nd, 2017
 */
public class Main_Menu extends JPanel {

    // The background image
    private Image bckgndIMG;

    private Button startButton; // Button to start playing the game
    private Button helpButton;  // Button to open the tutorial

    public Main_Menu() {
        // Initialize the background image
        bckgndIMG = new Image("data\\main_menu_bckgnd.png");

        // Initialize the start and help buttons

        startButton = new Button(25, UI.content_pane_height / 2 - 195 , 195, 80);
        startButton.setButtonColour(Color.DARK_GRAY);
        startButton.setButtonString("START");
        add(startButton);

        helpButton = new Button(25, UI.content_pane_height / 2 - 95, 155, 80);
        helpButton.setButtonColour(Color.DARK_GRAY);
        helpButton.setButtonString("HELP");
        add(helpButton);

        // Initialize the JPanel for the menu screen
        setLocation(0, 0);
        setSize(UI.content_pane_width, UI.content_pane_height);
        setLayout(null);
        setDoubleBuffered(true);
    }

    /**
     * Override the paintComponent() method to render the main menu
     *
     * @param g   The graphics context with which to paint
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Render the background image
        g2d.drawImage(bckgndIMG.getImage(), 0, 0, getWidth(), getHeight(), null);
    }
}