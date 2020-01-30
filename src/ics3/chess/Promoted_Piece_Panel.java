package ics3.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This class is used to display the various pieces that a pawn can be
 * promoted to (when it reaches the 1st or 8th rank). It is responsible
 * for determining which piece the user wishes to promote to.
 */
public class Promoted_Piece_Panel extends JPanel implements MouseListener {

    // Images storing each of the pieces that a pawn can be promoted to
    private Image rookImage, knightImage, bishopImage, queenImage;

    /**
     * Constructor for the Promoted_Piece_Panel class
     *
     */
    public Promoted_Piece_Panel() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructor for the Promoted_Piece_Panel class
     *
     * @param xPos    The x value of the top left corner of the piece's bounding rectangle
     * @param yPos    The y value of the top left corner of the piece's bounding rectangle
     * @param width   The width of the chess piece
     * @param height  The height of the chess piece
     */
    public Promoted_Piece_Panel(int xPos, int yPos, int width, int height) {
        setLocation(xPos, yPos);
        setSize(width, height);
        setLayout(null);
        // Add it as a mouseListener, which will allow it to call the appropriate method in the
        // gameflow_controller_engine whenever the user makes a selection
        addMouseListener(this);
        setDoubleBuffered(true);
    }

    /**
     * Helper method used to initialize the chess piece images of the promotion panel
     *
     * @param isBlack   If the pieces to be shown on the promotion panel are black
     */
    public void initializeImages(boolean isBlack) {
        // Set the appropriate colour to the images (based on the isBlack parameter)
        if (isBlack) {
            rookImage = new Image("data\\pieces_images\\black0.png");
            knightImage = new Image("data\\pieces_images\\black1.png");
            bishopImage = new Image("data\\pieces_images\\black2.png");
            queenImage = new Image("data\\pieces_images\\black3.png");
        }
        else {
            rookImage = new Image("data\\pieces_images\\white0.png");
            knightImage = new Image("data\\pieces_images\\white1.png");
            bishopImage = new Image("data\\pieces_images\\white2.png");
            queenImage = new Image("data\\pieces_images\\white3.png");
        }
    }

    /**
     * Override the paintComponent() method to render the promotion panel
     *
     * @param g   The graphics context with which to paint
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Render each of the pieces (the options for what the pawn can be promoted to)
        g2d.drawImage(rookImage.getImage(), 0, 0, getWidth()/4, getHeight(), null);
        g2d.drawImage(knightImage.getImage(), getWidth()/4, 0, getWidth()/4, getHeight(), null);
        g2d.drawImage(bishopImage.getImage(), 2*getWidth()/4, 0, getWidth()/4, getHeight(), null);
        g2d.drawImage(queenImage.getImage(), 3*getWidth()/4, 0, getWidth()/4, getHeight(), null);
    }

    /**
     * Implementation of the mousePressed() method in the MouseListener interface
     *
     * @param me    MouseEvent
     */
    @Override
    public void mousePressed(MouseEvent me) {
        if (me.getX() >= 0 && me.getX() <= getWidth()/4 && me.getY() >= 0 && me.getY() <= getHeight()) {
            Chess.gameflow_controller_engine.handleMousePressedPromotionPanel("rook");
        }
        if (me.getX() >= getWidth()/4 && me.getX() <= 2*getWidth()/4 && me.getY() >= 0 && me.getY() <= getHeight()) {
            Chess.gameflow_controller_engine.handleMousePressedPromotionPanel("knight");
        }
        if (me.getX() >= 2*getWidth()/4 && me.getX() <= 3*getWidth()/4 && me.getY() >= 0 && me.getY() <= getHeight()) {
            Chess.gameflow_controller_engine.handleMousePressedPromotionPanel("bishop");
        }
        if (me.getX() >= 3*getWidth()/4 && me.getX() <= getWidth() && me.getY() >= 0 && me.getY() <= getHeight()) {
            Chess.gameflow_controller_engine.handleMousePressedPromotionPanel("queen");
        }
    }

    /**
     * Implementation of the mouseClicked() method in the MouseListener interface
     *
     * @param me    MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent me) {}
    /**
     * Implementation of the mouseEntered() method in the MouseListener interface
     *
     * @param me    MouseEvent
     */
    @Override
    public void mouseEntered(MouseEvent me) {}
    /**
     * Implementation of the mouseReleased() method in the MouseListener interface
     *
     * @param me    MouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent me) {}
    /**
     * Implementation of the mouseExited() method in the MouseListener interface
     *
     * @param me    MouseEvent
     */
    @Override
    public void mouseExited(MouseEvent me) {}

}
