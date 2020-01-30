package ics3.chess.pieces;

import ics3.chess.Board_Square;
import ics3.chess.Image;

import java.util.ArrayList;

public class Pawn extends Chess_Piece {

    private boolean isFirstMove;

    public Pawn(int bottomLeftX, int bottomLeftY, int width, int height, boolean isBlack) {
        super(bottomLeftX, bottomLeftY, width, height, isBlack, null);
        if (isBlack) {
            try {
                chessPieceImage = new Image("data\\pieces_images\\blackPAWN.png");
                chessPieceImage.setImageSize(width, height);
            } catch (NullPointerException npe) {
                System.err.println("IMAGE COULD NOT BE FOUND!!!");
                npe.printStackTrace(System.err);
            }
        } else {
            try {
                chessPieceImage = new Image("data\\pieces_images\\whitePAWN.png");
                chessPieceImage.setImageSize(width, height);
            } catch (NullPointerException npe) {
                System.err.println("IMAGE COULD NOT BE FOUND!!!");
                npe.printStackTrace(System.err);
            }
        }
        isFirstMove = false;
    }

    public boolean getIsFirstMove() {
        return isFirstMove;
    }

    public void setIsFirstMove(boolean firstMove) {
        isFirstMove = firstMove;
    }

    @Override
    public ArrayList<int[]> getPossibleMoves(Board_Square[][] board_squares) {

        ArrayList<int[]> possibleMoves = new ArrayList<>();

        if (getIsBlack()) {
            // If no piece is in front
            if (!board_squares[getLocationOfBoardSquareOccupied()[0] + 1][getLocationOfBoardSquareOccupied()[1]].isOccupiedByPiece()) {
                // Moving one forward is a possible move
                possibleMoves.add(new int[]{1, 0});
                // Move_Chain two forward if no piece is in that spot
                if (getIsFirstMove() && !board_squares[getLocationOfBoardSquareOccupied()[0] + 2][getLocationOfBoardSquareOccupied()[1]].isOccupiedByPiece()) {
                    possibleMoves.add(new int[]{2, 0});
                }
            }
            // If there is a piece diagonally to the left
            if (board_squares[getLocationOfBoardSquareOccupied()[0] + 1][getLocationOfBoardSquareOccupied()[1] - 1].isOccupiedByPiece()) {
                possibleMoves.add(new int[]{1, -1});
            }
            // If there is a piece diagonally to the right
            if (board_squares[getLocationOfBoardSquareOccupied()[0] + 1][getLocationOfBoardSquareOccupied()[1] + 1].isOccupiedByPiece()) {
                possibleMoves.add(new int[]{1, 1});
            }
        } else {
            // If no piece is in front
            if (!board_squares[getLocationOfBoardSquareOccupied()[0] - 1][getLocationOfBoardSquareOccupied()[1]].isOccupiedByPiece()) {
                // Moving one forward is a possible move
                possibleMoves.add(new int[]{-1, 0});
                // Move_Chain two forward if no piece is in that spot
                if (getIsFirstMove() && !board_squares[getLocationOfBoardSquareOccupied()[0] - 2][getLocationOfBoardSquareOccupied()[1]].isOccupiedByPiece()) {
                    possibleMoves.add(new int[]{-2, 0});
                }
            }
            // If there is a piece diagonally to the left
            if (board_squares[getLocationOfBoardSquareOccupied()[0] - 1][getLocationOfBoardSquareOccupied()[1] - 1].isOccupiedByPiece()) {
                possibleMoves.add(new int[]{-1, -1});
            }
            // If there is a piece diagonally to the right
            if (board_squares[getLocationOfBoardSquareOccupied()[0] - 1][getLocationOfBoardSquareOccupied()[1] + 1].isOccupiedByPiece()) {
                possibleMoves.add(new int[]{-1, 1});
            }
        }

        return possibleMoves;
    }

}
