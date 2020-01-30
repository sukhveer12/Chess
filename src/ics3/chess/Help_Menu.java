package ics3.chess;

import javax.swing.*;
import java.awt.*;

/**
 * This class is used to render the help menu screen.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 22nd, 2017
 */
public class Help_Menu extends JPanel {

    // The background image
    private Image bckgndIMG;

    private Button backButton;  // Button to go to the main menu

    /**
     * Constructor for the Help_Menu class
     */
    public Help_Menu() {

        // Initialize the background image
        bckgndIMG = new Image("data\\help_menu_bckgnd.jpg");

        // Initialize the JPanel for the menu screen
        setLocation(0, 0);
        setSize(UI.content_pane_width, UI.content_pane_height);
        setLayout(null);
        setDoubleBuffered(true);

        // Initialize the back button
        backButton = new Button(getWidth() - 165, getHeight() - 100, 160, 80);
        backButton.setButtonColour(Color.DARK_GRAY);
        backButton.setButtonString("BACK");
        add(backButton);
    }

    /**
     * Override the paintComponent() method to render the help menu
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
