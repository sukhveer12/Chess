package ics3.chess;

import java.awt.*;

/**
 * This class is a model class for a single board square on the Chess_Board.
 * It is responsible for rendering the board square, as well as determining
 * if it was clicked on.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 5th, 2017
 */
public class Board_Square {

    // Coordinates of the top left corner of the object
    private int xPos;
    private int yPos;
    // Width and height of the object
    protected int width;
    protected int height;

    // Colour of the object (used during rendering)
    private Color objectColour;

    private boolean isOccupiedByPiece;
    private int indexOfOccupyingPiece; // -1 means no piece there

    private boolean highlighted;

    /**
     * Default constructor for the Board_Square object
     */
    public Board_Square() {
        this(0, 0, 0);
    }

    /**
     * @param x      The x value of the top left corner of the square's bounding rectangle
     * @param y      The y value of the top left corner of the square's bounding rectangle
     * @param length The side length of the square
     */
    public Board_Square(int x, int y, int length) {
        super();
        // Set the position and size
        setObjectPosition(x, y);
        setObjectSize(length, length);
        // Initialize the above variables to their default values
        isOccupiedByPiece = false;
        indexOfOccupyingPiece = -1;
        highlighted = false;
    }

    /**
     * Getter method for the position of the square (top-left corner)
     *
     * @return int[]     The position of the square
     */
    public int[] getObjectPosition() {
        return new int[]{xPos, yPos};
    }

    /**
     * Setter method for the position of the board square
     *
     * @param x The new x coordinate of the top left corner of the square
     * @param y The new y coordinate of the top left corner of the square
     */
    private void setObjectPosition(int x, int y) {
        xPos = x;
        yPos = y;
    }

    /**
     * Getter method for the position of the square (top-left corner)
     *
     * @return int[]     The size of the square
     */
    public int[] getObjectSize() {
        return new int[]{width, height};
    }

    /**
     * Setter method for the size of the square
     *
     * @param width  The width of the square
     * @param height The height of the square
     */
    private void setObjectSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Setter method for the colour of the square
     *
     * @param color The colour of the object
     */
    public void setColour(Color color) {
        objectColour = color;
    }

    /**
     * Getter method for whether or not the square is occupied by a piece
     *
     * @return boolean  Whether or not the square is occupied by a piece
     */
    public boolean isOccupiedByPiece() {
        return isOccupiedByPiece;
    }

    /**
     * Setter method for whether or not the square is occupied by a piece
     *
     * @param occupiedByPiece   Whether or not the square is occupied by a piece
     */
    public void setOccupiedByPiece(boolean occupiedByPiece) {
        isOccupiedByPiece = occupiedByPiece;
    }

    /**
     * Getter method for the index of the chess piece that is occupying the board square
     *
     * @return int  The index of the occupying chess piece
     */
    public int getIndexOfOccupyingPiece() {
        return indexOfOccupyingPiece;
    }

    /**
     * Setter method for the index of the chess piece that is occupying the board square
     *
     * @param indexOfOccupyingPiece     The index of the occupying chess piece
     */
    public void setIndexOfOccupyingPiece(int indexOfOccupyingPiece) {
        this.indexOfOccupyingPiece = indexOfOccupyingPiece;
    }

    /**
     * Getter method for whether or not the square is highlighted
     *
     * @return boolean  Whether or not the square is highlighted
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * Setter method for whether or not the square is highlighted
     *
     * @param highlighted Whether or not the square is highlighted
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * Determine whether or not the mouse was pressed on the board square
     *
     * @param mouseX The x-coordinate of the mouse press
     * @param mouseY The y-coordinate of the mouse press
     * @return boolean  Whether or not the mouse was pressed on the board square
     */
    public boolean determineIfMousePressed(double mouseX, double mouseY) {
        return mouseX >= xPos && mouseX <= xPos + width && mouseY >= yPos && mouseY <= yPos + height;
    }

    /**
     * Renders the board square
     *
     * @param g2d The graphics context with which to paint the board square
     */
    public void render(Graphics2D g2d) {
        g2d.setColor(objectColour);
        // Draw the board square
        g2d.fillRect(xPos, yPos, width, height);
        // If the board square is highlighted, then draw a gray square in its center
        if (highlighted) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(xPos + width / 2 - 5, yPos + height / 2 - 5, 10, 10);
        }
    }

}