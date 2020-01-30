package ics3.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * This class is the view class that deals with rendering the various components in this game.
 * It extends JFrame since it is where all the rendering is performed. It is also responsible
 * for managing the menu bar, and it provides various methods to customize the menu bar.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 9th, 2017
 */
public class UI extends JFrame {

    // The JMenuBar that is used in the program
    private JMenuBar menuBar;

    // Variables storing the width and height of the content pane (where the components are being rendered)
    public static int content_pane_width;
    public static int content_pane_height;

    /**
     * Constructor for the UI class, which is used to initialize the frame
     *
     * @param frameW    Width of the frame
     * @param frameH    Height of the frame
     */
    public UI(int frameW, int frameH) {
        // Initialize the frame by setting its size, default close operation, and its visibility
        // as well as whether or not it can be resized
        setSize(frameW, frameH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // Construct the menuBar and set it as the JMenuBar of the frame
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
    }

    /**
     * Helper method used to add a menu to the frame's menuBar
     * @param menuToAdd  Menu to add to the menuBar
     */
    public void addMenuToMenuBar(JMenu menuToAdd) {
        menuBar.add(menuToAdd);
    }

    /**
     * Sets the frame visible or invisible, and calculates the content
     * pane width and height whenever this state is changed (which can
     * only be calculated when the frame is visible)
     *
     * @param state Whether or not the frame should be set visible
     */
    @Override
    public void setVisible(boolean state) {
        super.setVisible(state);
        // Calculate the width and height of the content pane using the getInsets() method and the width/height of
        // the frame
        content_pane_width = getContentPane().getWidth();
        content_pane_height = getContentPane().getHeight();
    }

    /**
     * Override the paint(Graphics) method to perform custom painting in the frame
     * Although in this case the super paint() method is called, it is useful to override
     * the method anyways in case any changes are required (such as adding a menu or other
     * custom graphics)
     *
     * @param g Graphics context with which to paint
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

}
