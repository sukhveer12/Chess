package ics3.chess.pieces;

import ics3.chess.Board_Square;
import ics3.chess.Image;

import java.util.ArrayList;

public class Bishop extends Chess_Piece {

    public Bishop(int bottomLeftX, int bottomLeftY, int width, int height, boolean isBlack) {
        super(bottomLeftX, bottomLeftY, width, height, isBlack, null);
        if (isBlack) {
            try {
                chessPieceImage = new Image("data\\pieces_images\\blackBISHOP.png");
                chessPieceImage.setImageSize(width, height);
            } catch (NullPointerException npe) {
                System.err.println("IMAGE COULD NOT BE FOUND!!!");
                npe.printStackTrace(System.err);
            }
        } else {
            try {
                chessPieceImage = new Image("data\\pieces_images\\whiteBISHOP.png");
                chessPieceImage.setImageSize(width, height);
            } catch (NullPointerException npe) {
                System.err.println("IMAGE COULD NOT BE FOUND!!!");
                npe.printStackTrace(System.err);
            }
        }
    }

    @Override
    public ArrayList<int[]> getPossibleMoves(Board_Square[][] board_squares) {

        ArrayList<int[]> possibleMoves = new ArrayList<>();

        int diagonalSquaresBackwards = Math.min(getLocationOfBoardSquareOccupied()[0], getLocationOfBoardSquareOccupied()[1]);
        int diagonalSquaresForwards = Math.min(board_squares.length - getLocationOfBoardSquareOccupied()[0], board_squares[0].length - getLocationOfBoardSquareOccupied()[1]);

        for (int i = 1; i <= diagonalSquaresBackwards; i++) {
            if (board_squares[getLocationOfBoardSquareOccupied()[0] - i][getLocationOfBoardSquareOccupied()[1] - i].isOccupiedByPiece()) {
                break;
            }
            possibleMoves.add(new int[] {getLocationOfBoardSquareOccupied()[0] - i, getLocationOfBoardSquareOccupied()[1] - i});
        }
        for (int i = 1; i <= diagonalSquaresForwards; i++) {
            if (board_squares[getLocationOfBoardSquareOccupied()[0] + i][getLocationOfBoardSquareOccupied()[1] + i].isOccupiedByPiece()) {
                break;
            }
            possibleMoves.add(new int[] {getLocationOfBoardSquareOccupied()[0] + i, getLocationOfBoardSquareOccupied()[1] + i});
        }

        return possibleMoves;
    }

}
