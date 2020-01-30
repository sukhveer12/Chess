package ics3.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class is used for rendering all the buttons in the application.
 * It is also responsible for listening for MouseEvents and then calling the
 * appropriate methods in the controller engines (which will perform the appropriate
 * behaviour in response to the button press)
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 5th, 2017
 */
public class Button extends JComponent implements MouseListener {

    // Variables storing the position and size of the button
    private int buttonX, buttonY, buttonSX, buttonSY;

    // Variable storing the colour and font which are to be used during rendering
    private Color buttonColour;
    private Font font;

    // String storing the label of the button
    private String buttonString;

    /**
     * Default constructor for the Button class
     */
    public Button() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructor for the Button class that assigns initial values to the above properties
     *
     * @param x  The x value of the top left corner of the button's bounding rectangle
     * @param y  The y value of the top left corner of the button's bounding rectangle
     * @param sx The width (x-size) of the button
     * @param sy The height (y-size) of the button
     */
    public Button(int x, int y, int sx, int sy) {
        buttonX = x;
        buttonY = y;
        buttonSX = sx;
        buttonSY = sy;

        buttonColour = Color.CYAN;

        buttonString = "DEFAULT";
        font = new Font("Times New Roman", Font.PLAIN, 55);

        addMouseListener(this);

        setLocation(buttonX, buttonY);
        setSize(getPreferredSize());
    }

    /**
     * Setter method for the colour
     *
     * @param c The desired colour of the button
     */
    public void setButtonColour(Color c) {
        buttonColour = c;
    }

    /**
     * Getter method for the label of the button
     *
     * @return String   The label of the button
     */
    public String getButtonString() {
        return buttonString;
    }

    /**
     * Setter method for the button label
     *
     * @param txt The label of the button
     */
    public void setButtonString(String txt) {
        buttonString = txt;
    }

    /**
     * Setter method for the font
     *
     * @param font The font that is used when rendering the button label
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Getter method for the size of the button
     *
     * @return Dimension    The size (width and height) of the button
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(buttonSX, buttonSY);
    }

    /**
     * Used to render the button
     *
     * @param g The graphics context with which to paint the button
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(buttonColour);

        // Draw the button box
        g2d.fillRect(0, 0, buttonSX, buttonSY);

        // Display the button text
        g2d.setColor(Color.WHITE);
        g2d.setFont(font);
        g2d.drawString(buttonString, 10, font.getSize());
    }

    /**
     * Implementation of the mousePressed() method in the MouseListener interface
     *
     * @param me MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent me) {
        Chess.ui_controller_engine.handleMousePressedButton(this);
    }

    /**
     * Implementation of the mouseClicked() method in the MouseListener interface
     *
     * @param me MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent me) {
    }

    /**
     * Implementation of the mouseEntered() method in the MouseListener interface
     *
     * @param me MouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent me) {
    }

    /**
     * Implementation of the mouseReleased() method in the MouseListener interface
     *
     * @param me MouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent me) {
    }

    /**
     * Implementation of the mouseExited() method in the MouseListener interface
     *
     * @param me MouseEvent
     */
    @Override
    public void mouseExited(MouseEvent me) {
    }

}
