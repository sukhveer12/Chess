package ics3.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * This class is used to render the chess board and all the pieces on it.
 * It is also responsible for determining if the mouse was pressed on it,
 * and it will call the appropriate method in the gameflow_controller_engine
 * in order to ensure the program responds correctly.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 2nd, 2017
 */
public class Chess_Board extends JComponent implements MouseListener {

    // The array of box objects used to render the grid
    // and the variables representing attributes of the
    // grid, including width, height, X, Y, etc.
    private Board_Square[][] board_squares;

    private Chess_Piece[] chess_pieces;

    private Image chessBoardBorderImage;
    private int borderThickness;

    private boolean isRotating;
    private long timeRotateStarted;
    private double rotateAngle;
    private double rotateSpeed;

    private int squarePieceWidth, pieceHeight, spacingBetweenSquares;
    private String imgLocPieces;

    private AffineTransform affineTransform;

    /**
     * Default constructor for the Chess_Board class
     */
    public Chess_Board() {
        this(0, 0, 0, 0, 0, "", "", 0);
    }

    /**
     * Constructor for the Chess_Board class
     *
     * @param topLeftXCoordinate    The x value of the top left corner of the board's bounding rectangle
     * @param topLeftYCoordinate    The y value of the top left corner of the board's bounding rectangle
     * @param squarePieceWidth      The width of the board
     * @param pieceHeight           The height of the board
     * @param spacingBetweenSquares The spacing between board squares that are touching one another
     * @param imgLocBoardBorder     The location of the wooden border image
     * @param imgLocPieces          The folder containing the images of the chess pieces
     * @param borderThickness       The thickness of the wooden border of the chess board
     */
    public Chess_Board(int topLeftXCoordinate, int topLeftYCoordinate, int squarePieceWidth, int pieceHeight, int spacingBetweenSquares, String imgLocBoardBorder, String imgLocPieces, int borderThickness) {
        this.borderThickness = borderThickness;

        this.squarePieceWidth = squarePieceWidth;
        this.pieceHeight = pieceHeight;
        this.spacingBetweenSquares = spacingBetweenSquares;
        this.imgLocPieces = imgLocPieces;

        // Call the helper methods to initialize the board squares and the chess pieces
        initializeBoardSquares(this.squarePieceWidth, this.spacingBetweenSquares);
        initializeChessPieces(this.squarePieceWidth, this.pieceHeight, this.spacingBetweenSquares, this.imgLocPieces);

        // Initialize the chess board border image
        chessBoardBorderImage = new Image(imgLocBoardBorder);

        // Set the size and location of the JPanel (the chess board)
        setLocation(topLeftXCoordinate - borderThickness, topLeftYCoordinate - borderThickness);
        setSize(8 * squarePieceWidth + 7 * spacingBetweenSquares + 2 * borderThickness, 8 * squarePieceWidth + 7 * spacingBetweenSquares + 2 * borderThickness);

        // Add this panel as a MouseListener
        addMouseListener(this);
        // Set this panel double buffered (used for background management)
        setDoubleBuffered(true);

        // Initialize the rotation properties and the affineTransform
        rotateAngle = 0;
        affineTransform = AffineTransform.getRotateInstance(rotateAngle, getWidth() / 2, getHeight() / 2);
        isRotating = false;
        timeRotateStarted = 0;
        rotateSpeed = -(Math.PI / 180.0) / 4.0;
    }

    /**
     * Helper method to initialize the board squares 2D-array
     *
     * @param squareLength          The side length of each square of the board
     * @param spacingBetweenSquares The spacing between adjacent squares on the board
     */
    private void initializeBoardSquares(int squareLength, int spacingBetweenSquares) {
        board_squares = new Board_Square[8][8];

        // i represents each row
        for (int i = 0; i < board_squares.length; i++) {

            // ii represents each square in the row
            for (int ii = 0; ii < board_squares[i].length; ii++) {

                // Construct each board square by calculating the appropriate (x,y) position and assigning it
                board_squares[i][ii] = new Board_Square(borderThickness + (squareLength + spacingBetweenSquares) * ii, borderThickness + (squareLength + spacingBetweenSquares) * i, squareLength);
                if ((7 * i + ii) % 2 == 0) {
                    board_squares[i][ii].setColour(Color.getHSBColor(118, 92, 73));
                } else {
                    board_squares[i][ii].setColour(Color.getHSBColor(30, 93, 37));
                }
            }
        }
    }

    /**
     * Helper method to initialize the chess_pieces array
     *
     * @param squareSideLength      The side length of each square
     * @param pieceHeight           The height of each piece
     * @param spacingBetweenSquares The spacing between adjacent squares
     * @param imgLocPrefix          The folder in which the piece images are stored
     */
    private void initializeChessPieces(int squareSideLength, int pieceHeight, int spacingBetweenSquares, String imgLocPrefix) {
        // Initialize the chess_pieces array
        chess_pieces = new Chess_Piece[32];
        // Initialize the 1st row (8th rank) of chess pieces
        for (int i = 0; i < 8; i++) {
            chess_pieces[i] = new Chess_Piece(borderThickness + (squareSideLength + spacingBetweenSquares) * (i % 8), borderThickness, squareSideLength, pieceHeight, true, imgLocPrefix + "black" + i + ".png");
            chess_pieces[i].assignPieceIdentityFromNumber(i);
            chess_pieces[i].setLocationOfBoardSquareOccupied(new int[]{0, i});
            getBoardSquare(0, i).setOccupiedByPiece(true);
            getBoardSquare(0, i).setIndexOfOccupyingPiece(i);
        }
        // Initialize the 2nd row (7th rank) of chess pieces
        for (int i = 8; i < 16; i++) {
            chess_pieces[i] = new Chess_Piece(borderThickness + (squareSideLength + spacingBetweenSquares) * (i % 8), borderThickness + (squareSideLength + spacingBetweenSquares), squareSideLength, pieceHeight, true, imgLocPrefix + "blackPAWN.png");
            chess_pieces[i].assignPieceIdentityFromNumber(-1);
            chess_pieces[i].setLocationOfBoardSquareOccupied(new int[]{1, i - 8});
            getBoardSquare(1, i - 8).setOccupiedByPiece(true);
            getBoardSquare(1, i - 8).setIndexOfOccupyingPiece(i);
        }
        // Initialize the 7th row (2nd rank) of chess pieces
        for (int i = 16; i < 24; i++) {
            chess_pieces[i] = new Chess_Piece(borderThickness + (squareSideLength + spacingBetweenSquares) * (i % 8), borderThickness + 6 * (squareSideLength + spacingBetweenSquares), squareSideLength, pieceHeight, false, imgLocPrefix + "whitePAWN.png");
            chess_pieces[i].assignPieceIdentityFromNumber(-1);
            chess_pieces[i].setLocationOfBoardSquareOccupied(new int[]{6, i - 16});
            getBoardSquare(6, i - 16).setOccupiedByPiece(true);
            getBoardSquare(6, i - 16).setIndexOfOccupyingPiece(i);
        }
        // Initialize the 8th row (1st rank) of chess pieces
        for (int i = 24; i < 32; i++) {
            chess_pieces[i] = new Chess_Piece(borderThickness + (squareSideLength + spacingBetweenSquares) * (i % 8), borderThickness + 7 * (squareSideLength + spacingBetweenSquares), squareSideLength, pieceHeight, false, imgLocPrefix + "white" + Integer.toString(i - 24) + ".png");
            chess_pieces[i].assignPieceIdentityFromNumber(i - 24);
            chess_pieces[i].setLocationOfBoardSquareOccupied(new int[]{7, i - 24});
            getBoardSquare(7, i - 24).setOccupiedByPiece(true);
            getBoardSquare(7, i - 24).setIndexOfOccupyingPiece(i);
        }
    }

    /**
     * Method used to reset the chess board and the pieces (used during a restart)
     */
    public void resetBoard() {
        initializeBoardSquares(squarePieceWidth, spacingBetweenSquares);
        initializeChessPieces(squarePieceWidth, pieceHeight, spacingBetweenSquares, imgLocPieces);
        rotateAngle = 0;
        rotateSpeed = -(Math.PI / 180.0) / 4.0;
        affineTransform = AffineTransform.getRotateInstance(rotateAngle, getWidth() / 2, getHeight() / 2);
    }

    /**
     * Method used to start rotating the square (used to change the perspective)
     */
    public void startRotate() {
        rotateSpeed = -rotateSpeed;
        isRotating = true;
        timeRotateStarted = System.currentTimeMillis();
    }

    /**
     * Method used to rotate the chess board to show the perspective of the player whose turn it is
     */
    private void rotateBoard() {
        // If the rotation is pending
        if (isRotating) {
            // Increase the rotate angle
            rotateAngle += rotateSpeed * (System.currentTimeMillis() - timeRotateStarted);
            timeRotateStarted = System.currentTimeMillis();
            // If the angle has gone more than 180 degrees, then stop rotating
            if (rotateAngle >= Math.PI) {
                rotateAngle = Math.PI;
                for (Chess_Piece chess_piece : chess_pieces) {
                    chess_piece.setRotateAngle(rotateAngle);
                }
                isRotating = false;
            }
            // If the angle has gone less than 0 degrees, stop rotating
            else if (rotateAngle <= 0) {
                rotateAngle = 0;
                for (Chess_Piece chess_piece : chess_pieces) {
                    chess_piece.setRotateAngle(rotateAngle);
                }
                isRotating = false;
            }
            // Update the affineTransform to match the rotateAngle
            affineTransform = AffineTransform.getRotateInstance(rotateAngle, getWidth() / 2, getHeight() / 2);
        }
    }

    /**
     * Getter method for the board_squares array
     *
     * @return Board_Square[][]  The board_squares array
     */
    public Board_Square[][] getBoardSquares() {
        return board_squares;
    }

    /**
     * Getter method for the chess pieces array
     *
     * @return Chess_Piece[]    The chess pieces array
     */
    public Chess_Piece[] getChessPieces() {
        return chess_pieces;
    }

    /**
     * Getter method for a specific board square in the board_squares array
     *
     * @param row       The row in which the desired board square is located
     * @param column    The column in which the desired board square is located
     * @return Board_Square     The board square at the [row,column] location specified
     */
    public Board_Square getBoardSquare(int row, int column) {
        return board_squares[row][column];
    }

    /**
     * Getter method for a specific chess piece in the chess_pieces array
     *
     * @param index     The index of the desired chess piece
     * @return Chess_Piece  The chess piece at the index specified
     */
    public Chess_Piece getChessPiece(int index) {
        return chess_pieces[index];
    }

    /**
     * Getter method for the rotate angle from the center of the board square
     *
     * @return double   The rotate angle
     */
    public double getRotateAngle() {
        return rotateAngle;
    }

    /**
     * Setter method for the rotate angle from the center of the board square
     *
     * @param rotateAngle   The rotate angle
     */
    public void setRotateAngle(double rotateAngle) {
        this.rotateAngle = rotateAngle;
    }

    /**
     * Override the paintComponent() method to render the chess board and the pieces
     *
     * @param g The graphics context with which to paint
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Rotate the board to the correct angle
        rotateBoard();

        // Set the transform to show the correct perspective when rendering the chess board
        g2d.transform(affineTransform);

        // Draw the chess board border
        g2d.drawImage(chessBoardBorderImage.getImage(), 0, 0, getWidth(), getHeight(), null);

        // Draw the board squares
        for (Board_Square[] rowOfBoardSquares : board_squares) {
            for (Board_Square boardSquare : rowOfBoardSquares) {
                boardSquare.render(g2d);
            }
        }
        // Draw the chess pieces
        for (Chess_Piece chess_piece : chess_pieces) {
            chess_piece.render(g2d);
        }
    }

    /**
     * Helper method that reverses any transformations on a mouse press
     *
     * @param me    MouseEvent
     * @return Point2D  The untransformed mouse press coordinates
     */
    private Point2D untransformMousePress(MouseEvent me) {
        Point2D untransformedMousePress;
        // Try to invert the transformation on the mouse press coordinates
        try {
            untransformedMousePress = affineTransform.inverseTransform(new Point2D.Double(me.getX(), me.getY()), null);
        } catch (NoninvertibleTransformException nte) {
            nte.printStackTrace(System.err);
            untransformedMousePress = new Point2D.Double(me.getX(), me.getY());
        }
        return untransformedMousePress;
    }

    /**
     * Implementation of the mousePressed() method in the MouseListener interface
     *
     * @param me MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent me) {
        Point2D untransformedMousePress = untransformMousePress(me);
        for (int rowNumber = 0; rowNumber < board_squares.length; rowNumber++) {
            for (int columnNumber = 0; columnNumber < board_squares[rowNumber].length; columnNumber++) {
                if (board_squares[rowNumber][columnNumber].determineIfMousePressed(untransformedMousePress.getX(), untransformedMousePress.getY())) {
                    Chess.gameflow_controller_engine.handleMousePressedBoardSquare(board_squares[rowNumber][columnNumber], new int[]{rowNumber, columnNumber});
                    break;
                }
            }
        }
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