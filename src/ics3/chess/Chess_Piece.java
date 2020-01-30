package ics3.chess;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Model class for each chess piece on the chess board. It is responsible
 * for rendering the chess piece, as well as storing other important properties
 * of the piece.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since April 17th, 2017
 */
public class Chess_Piece {

    // Coordinates of the top left corner of the object
    private int xPos, yPos;
    // Width and height of the object
    private int width;
    private int height;

    private boolean hasBeenCaptured; // Whether or not the chess piece has been captured
    private boolean hasBeenSelected; // Whether the player1 has selected this piece
    private boolean highlighted;
    private int[] locationOfBoardSquareOccupied; // What square is this disk present on

    // Used for pawns (initially they can move 2 squares forward, but afterwards only 1), and for kings and rooks
    // (the "castling" move can only be performed if it is the first move for both those pieces)
    private boolean isFirstMove;

    // The image of the chess piece
    private Image chessPieceImage;

    // Whether or not the piece is a black piece
    private boolean isBlack;

    // String storing the identity of the chess piece (e.g. "pawn", "rook")
    private String pieceIdentity;

    // The rotate angle (used when changing the perspective of the chess board)
    private double rotateAngle;

    /**
     * Default constructor of the Chess_Piece class
     */
    public Chess_Piece() {
        this(0, 0, 0, 0, false, null);
    }

    /**
     * Constructor of the Ball_Model class
     *
     * @param xPos    The x value of the top left corner of the piece's bounding rectangle
     * @param yPos    The y value of the top left corner of the piece's bounding rectangle
     * @param width   The width of the chess piece
     * @param height  The height of the chess piece
     * @param isBlack Whether or not the chess piece is a black piece
     * @param imgLoc  The location of the chess piece image
     */
    public Chess_Piece(int xPos, int yPos, int width, int height, boolean isBlack, String imgLoc) {
        // Initialize the above variables to their default values
        hasBeenSelected = false;
        highlighted = false;
        locationOfBoardSquareOccupied = new int[]{-1, -1};
        this.isBlack = isBlack;
        isFirstMove = true;
        rotateAngle = 0;
        // Read the piece image
        try {
            chessPieceImage = new ics3.chess.Image(imgLoc);
            setObjectSize(width, height);
        } catch (NullPointerException npe) {
            System.out.println("NO IMAGE SPECIFIED...");
        }
        // Set the position
        setObjectPosition(xPos, yPos);
    }

    /**
     * Setter method for the position of the chess piece
     *
     * @param x X-coordinate of the top left corner of the chess piece
     * @param y Y-coordinate of the top left corner of the chess piece
     */
    public void setObjectPosition(int x, int y) {
        xPos = x;
        yPos = y;
        locationOfBoardSquareOccupied[0] = -1;
        locationOfBoardSquareOccupied[1] = -1;
    }

    /**
     * Setter method for the size of the object
     *
     * @param width  The width of the object
     * @param height The height of the object
     */
    private void setObjectSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Getter method for the position of the piece (top-left corner)
     *
     * @return int[]     The position of the object
     */
    public int[] getObjectPosition() {
        return new int[]{xPos, yPos};
    }

    /**
     * Getter method for the size of the chess piece
     *
     * @return int[]     The size of the object
     */
    public int[] getObjectSize() {
        return new int[]{width, height};
    }

    /**
     * Getter method for whether or not a piece has been moved yet
     * (e.g. whether or not it is its first move)
     *
     * @return boolean  Whether or not it is the first move of the piece
     */
    public boolean getIsFirstMove() {
        return isFirstMove;
    }

    /**
     * Setter method for whether or not a piece has been moved yet
     *
     * @param firstMove Whether or not it is the first move of the piece
     */
    public void setIsFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    /**
     * Getter method for the rotate angle (from the center
     * of the chess piece)
     *
     * @return double   The rotate angle from the center of the chess piece
     */
    public double getRotateAngle() {
        return rotateAngle;
    }

    /**
     * Setter method for the rotate angle (from the center)
     *
     * @param rotateAngle The rotate angle from the center of the chess piece
     */
    public void setRotateAngle(double rotateAngle) {
        this.rotateAngle = rotateAngle;
    }

    /**
     * Getter method for whether or not the piece has been captured
     *
     * @return boolean  Whether or not the piece has been captured
     */
    public boolean isHasBeenCaptured() {
        return hasBeenCaptured;
    }

    /**
     * Setter method for whether or not the piece has been captured
     *
     * @param hasBeenCaptured Whether or not the piece has been captured
     */
    public void setHasBeenCaptured(boolean hasBeenCaptured) {
        this.hasBeenCaptured = hasBeenCaptured;
    }

    /**
     * Used to assign a piece identity to a piece by decoding a passed-in number
     * Note: 0/7 - rook  1/6 - knight  2/5 - bishop  3 - queen  4 - king  -1 - pawn
     *
     * @param number The "code" that
     */
    public void assignPieceIdentityFromNumber(int number) {
        switch (number) {
            // A rook can be 0 or 7
            case 0:
            case 7:
                pieceIdentity = "rook";
                break;
            // A knight can be 1 or 6
            case 1:
            case 6:
                pieceIdentity = "knight";
                break;
            // A bishop can be 2 or 5
            case 2:
            case 5:
                pieceIdentity = "bishop";
                break;
            // A queen is 3
            case 3:
                pieceIdentity = "queen";
                break;
            // A king is 4
            case 4:
                pieceIdentity = "king";
                break;
            // A pawn is 1
            case -1:
                pieceIdentity = "pawn";
                break;
        }
    }

    /**
     * Getter method for the piece identity (e.g. "rook", "queen")
     *
     * @return String   The string that represents the identity of the piece
     */
    public String getPieceIdentity() {
        return pieceIdentity;
    }

    /**
     * Setter method for the identity of the chess piece
     * (e.g. "king", "queen")
     *
     * @param identity The identity of the chess piece
     */
    public void setPieceIdentity(String identity) {
        pieceIdentity = identity;
        // If the piece is black, assign the appropriate piece colour to the piece
        if (getIsBlack()) {
            switch (pieceIdentity) {
                case "rook":
                    chessPieceImage.changeImage("data\\pieces_images\\black0.png");
                    break;
                case "knight":
                    chessPieceImage.changeImage("data\\pieces_images\\black1.png");
                    break;
                case "bishop":
                    chessPieceImage.changeImage("data\\pieces_images\\black2.png");
                    break;
                case "queen":
                    chessPieceImage.changeImage("data\\pieces_images\\black3.png");
                    break;
                case "king":
                    chessPieceImage.changeImage("data\\pieces_images\\black4.png");
                    break;
                case "pawn":
                    chessPieceImage.changeImage("data\\pieces_images\\blackPAWN.png");
                    break;
            }
        }
        // Otherwise if the piece is white, assign the appropriate piece colour to the piece
        else {
            switch (pieceIdentity) {
                case "rook":
                    chessPieceImage.changeImage("data\\pieces_images\\white0.png");
                    break;
                case "knight":
                    chessPieceImage.changeImage("data\\pieces_images\\white1.png");
                    break;
                case "bishop":
                    chessPieceImage.changeImage("data\\pieces_images\\white2.png");
                    break;
                case "queen":
                    chessPieceImage.changeImage("data\\pieces_images\\white3.png");
                    break;
                case "king":
                    chessPieceImage.changeImage("data\\pieces_images\\white4.png");
                    break;
                case "pawn":
                    chessPieceImage.changeImage("data\\pieces_images\\whitePAWN.png");
                    break;
            }
        }
    }

    /**
     * Getter method for whether or not the piece has been selected
     *
     * @return boolean  Whether or not the piece has been selected
     */
    public boolean getHasBeenSelected() {
        return hasBeenSelected;
    }

    /**
     * Setter method for whether or not the piece has been selected
     *
     * @param state Whether or not the piece is to be selected
     */
    public void setHasBeenSelected(boolean state) {
        hasBeenSelected = state;
    }

    /**
     * Getter method for whether or not the piece is a black piece
     *
     * @return boolean  Whether or not the piece is a black piece
     */
    public boolean getIsBlack() {
        return isBlack;
    }

    /**
     * Getter method for whether or not the piece has been highlighted
     *
     * @return boolean  Whether or not the piece has been highlighted
     */
    public boolean getHighlighted() {
        return highlighted;
    }

    /**
     * Setter method for whether or not the piece has been highlighted
     *
     * @param highlighted Whether or not the piece has been highlighted
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * Getter method for the location of the board square that the piece has occupied
     * Note: the int[] returned is of the following format: ([0] -> row  [1] -> column)
     *
     * @return int[]    The row-column location of the board square occupied
     */
    public int[] getLocationOfBoardSquareOccupied() {
        return new int[]{locationOfBoardSquareOccupied[0], locationOfBoardSquareOccupied[1]};
    }

    /**
     * Setter method for the new board square that the chess piece is to occupy
     *
     * @param newLocation New location to move the piece to ([0] -> row  [1] -> column)
     */
    public void setLocationOfBoardSquareOccupied(int[] newLocation) {
        locationOfBoardSquareOccupied = newLocation.clone();
    }

    /**
     * Render method to draw the chess piece
     *
     * @param g2d The graphics context with which to paint
     */
    public void render(Graphics2D g2d) {
        // Apply the transform (the rotation)
        g2d.transform(AffineTransform.getRotateInstance(rotateAngle, xPos + width / 2, yPos + height / 2));

        // Draw the chess piece image
        g2d.drawImage(chessPieceImage.getImage(), xPos, yPos, width, height, null);

        // If the chess piece has been selected, then draw a red oval to indicate that
        if (hasBeenSelected) {
            g2d.setColor(Color.RED);
            g2d.fillOval(xPos + width / 2 - 10, yPos + height / 2 - 10, 20, 20);
        }
        // If the chess piece has been highlighted as a spot where another chess piece can go,
        // then draw a gray oval to indicate that
        if (highlighted) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval(xPos + width / 2 - 5, yPos + height / 2 - 5, 10, 10);
        }

        // Reverse the transformation so that the rendering of other chess pieces is not skewed
        g2d.transform(AffineTransform.getRotateInstance(-rotateAngle, xPos + width / 2, yPos + height / 2));

    }

}