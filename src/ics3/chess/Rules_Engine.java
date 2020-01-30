package ics3.chess;

import java.util.ArrayList;
import java.util.Arrays;

public class Rules_Engine {

    private static Rules_Engine rulesEngineInstance = new Rules_Engine();

    public static Rules_Engine getInstance() {
        return rulesEngineInstance;
    }

    // ArrayList storing all possible moves that can be made by all pieces
    private ArrayList<int[]>[] possibleMoves;

    private Rules_Engine() {
        possibleMoves = new ArrayList[16];
    }

    public boolean isMoveValid(Chess_Piece[] chess_pieces, int movingPieceIndex, Board_Square[][] board_squares, int[] newPieceLocation, boolean isBlackMove) throws Exception {
        Chess_Piece movingChessPiece = chess_pieces[movingPieceIndex];
        if (movingChessPiece.getLocationOfBoardSquareOccupied()[0] == -1 || movingChessPiece.getLocationOfBoardSquareOccupied()[1] == -1) {
            throw new Exception("VARIABLE 'locationOfBoardSquareOccupied' HAS NOT BEEN UPDATED!!!");
        }

        // The index of the piece in the new location
        int indexOfPieceInNewLocation = -1;

        // If the pieces are the same colour, then you cannot capture the piece, so return false
        if (board_squares[newPieceLocation[0]][newPieceLocation[1]].isOccupiedByPiece()) {
            indexOfPieceInNewLocation = board_squares[newPieceLocation[0]][newPieceLocation[1]].getIndexOfOccupyingPiece();
            if (chess_pieces[indexOfPieceInNewLocation].getIsBlack() == movingChessPiece.getIsBlack()) {
                return false;
            }
        }

        int rowDelta = newPieceLocation[0] - movingChessPiece.getLocationOfBoardSquareOccupied()[0];
        int columnDelta = newPieceLocation[1] - movingChessPiece.getLocationOfBoardSquareOccupied()[1];

        if (movingChessPiece.getPieceIdentity().equals("king") && movingChessPiece.getIsFirstMove() && rowDelta == 0 && indexOfPieceInNewLocation == -1) {
            // King goes right
            if (columnDelta == 2) {
                int rookIndexToCastleWith = board_squares[movingChessPiece.getLocationOfBoardSquareOccupied()[0]][movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 3].getIndexOfOccupyingPiece();
                Chess_Piece rookToCastleWith = chess_pieces[rookIndexToCastleWith];
                if (rookToCastleWith.getIsFirstMove() && !board_squares[movingChessPiece.getLocationOfBoardSquareOccupied()[0]][movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 1].isOccupiedByPiece()) {
                    boolean oneStepLegal = !checkIfKingInCheck(chess_pieces, movingPieceIndex, board_squares, new int[]{movingChessPiece.getLocationOfBoardSquareOccupied()[0], movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 1}, isBlackMove, -1);
                    boolean twoStepLegal = !checkIfKingInCheck(chess_pieces, movingPieceIndex, board_squares, new int[]{movingChessPiece.getLocationOfBoardSquareOccupied()[0], movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 2}, isBlackMove, -1);
                    wasMoveACastle = true;
                    rookBeingCastledWithAndDirection = -rookIndexToCastleWith;
                    return oneStepLegal && twoStepLegal;
                }
            }
            // King goes left
            else if (columnDelta == -2) {
                int rookIndexToCastleWith = board_squares[movingChessPiece.getLocationOfBoardSquareOccupied()[0]][movingChessPiece.getLocationOfBoardSquareOccupied()[1] - 4].getIndexOfOccupyingPiece();
                Chess_Piece rookToCastleWith = chess_pieces[rookIndexToCastleWith];
                if (rookToCastleWith.getIsFirstMove() && !board_squares[movingChessPiece.getLocationOfBoardSquareOccupied()[0]][movingChessPiece.getLocationOfBoardSquareOccupied()[1] - 1].isOccupiedByPiece()) {
                    boolean oneStepLegal = !checkIfKingInCheck(chess_pieces, movingPieceIndex, board_squares, new int[]{movingChessPiece.getLocationOfBoardSquareOccupied()[0], movingChessPiece.getLocationOfBoardSquareOccupied()[1] - 1}, isBlackMove, -1);
                    boolean twoStepLegal = !checkIfKingInCheck(chess_pieces, movingPieceIndex, board_squares, new int[]{movingChessPiece.getLocationOfBoardSquareOccupied()[0], movingChessPiece.getLocationOfBoardSquareOccupied()[1] - 2}, isBlackMove, -1);
                    wasMoveACastle = true;
                    rookBeingCastledWithAndDirection = rookIndexToCastleWith;
                    return oneStepLegal && twoStepLegal;
                }
            }
        }

        if (!isMovePatternValid(chess_pieces[movingPieceIndex], rowDelta, columnDelta, board_squares[newPieceLocation[0]][newPieceLocation[1]])) {
            //System.out.println("INVALID MOVE PATTERN");
            return false;
        }

        if (!chess_pieces[movingPieceIndex].getPieceIdentity().equals("knight")) {
            if (checkIfPieceInWay(chess_pieces[movingPieceIndex].getLocationOfBoardSquareOccupied(), new int[]{rowDelta, columnDelta}, board_squares)) {
                //System.out.println("JUMPING OVER PIECE");
                return false;
            }
        }

        return !checkIfKingInCheck(chess_pieces, movingPieceIndex, board_squares, newPieceLocation, isBlackMove, indexOfPieceInNewLocation);
    }

    private boolean isMovePatternValid(Chess_Piece chess_piece, int rowDelta, int columnDelta, Board_Square targetBoardSquare) {
        if (rowDelta == 0 && columnDelta == 0) {
            return false;
        }
        switch (chess_piece.getPieceIdentity()) {
            case "rook":
                // If one of left/right delta changes, but not both
                if (columnDelta * rowDelta == 0) {
                    return true;
                }
                break;
            case "knight":
                if ((Math.abs(columnDelta) == 1 && Math.abs(rowDelta) == 2) || (Math.abs(columnDelta) == 2 && Math.abs(rowDelta) == 1)) {
                    return true;
                }
                break;
            case "bishop":
                if (Math.abs(columnDelta) == Math.abs(rowDelta)) {
                    return true;
                }
                break;
            case "queen":
                // Move_Chain of a rook
                if (columnDelta * rowDelta == 0) {
                    return true;
                }
                // Move_Chain of a bishop
                if (Math.abs(columnDelta) == Math.abs(rowDelta)) {
                    return true;
                }
                break;
            case "king":
                // If the king moves more than 1 square away, then it's invalid
                if (Math.abs(columnDelta) > 1 || Math.abs(rowDelta) > 1) {
                    break;
                }
                // Move_Chain horizontally or vertically
                if (columnDelta * rowDelta == 0) {
                    return true;
                }
                // Move_Chain diagonally
                if (Math.abs(columnDelta) == Math.abs(rowDelta)) {
                    return true;
                }
                break;
            case "pawn":
                if (!chess_piece.getIsBlack()) {
                    rowDelta = -rowDelta;
                }
                // If it goes too far diagonally (more than 1 square)
                if (Math.abs(columnDelta) > 1) {
                    break;
                }
                // Moving diagonally one square
                else if (Math.abs(columnDelta) == 1 && rowDelta == 1) {
                    // Only returns true if there is a piece to be captured at the new location
                    if (targetBoardSquare.isOccupiedByPiece()) {
                        return true;
                    }
                } else {

                    if (rowDelta == 1 && !targetBoardSquare.isOccupiedByPiece()) {
                        return true;
                    }
                    if (rowDelta == 2 && chess_piece.getIsFirstMove() && columnDelta == 0 && !targetBoardSquare.isOccupiedByPiece()) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    // Returns true if piece in way, else false
    private boolean checkIfPieceInWay(int[] currentBoardLocationPiece, int RCDelta[], Board_Square[][] board_squares) {
        int perRowDelta = RCDelta[0] / Math.max(Math.abs(RCDelta[0]), Math.abs(RCDelta[1]));
        int perColumnDelta = RCDelta[1] / Math.max(Math.abs(RCDelta[0]), Math.abs(RCDelta[1]));
        int passNumber = 1;
        while (passNumber < Math.max(Math.abs(RCDelta[0]), Math.abs(RCDelta[1]))) {
            if (board_squares[currentBoardLocationPiece[0] + (passNumber * perRowDelta)][currentBoardLocationPiece[1] + (passNumber * perColumnDelta)].isOccupiedByPiece()) {
                return true;
            }
            passNumber++;
        }
        // Cycle through every row-delta, and increase column-delta by the corresponding amount (ratio method)
        return false;
    }

    private boolean wasMoveACastle = false;
    private int rookBeingCastledWithAndDirection = -1;

    public boolean getWasMoveACastle() {
        return wasMoveACastle;
    }

    public void setWasMoveACastle(boolean state) {
        this.wasMoveACastle = state;
    }

    public int getRookBeingCastledWithAndDirection() {
        return rookBeingCastledWithAndDirection;
    }

    public void setRookBeingCastledWithAndDirection(int rookBeingCastledWithAndDirection) {
        this.rookBeingCastledWithAndDirection = rookBeingCastledWithAndDirection;
    }

    private boolean checkIfKingInCheck(Chess_Piece[] chess_pieces, int movingPieceIndex, Board_Square[][] board_squares, int[] newPieceLocation, boolean isBlackMove, int indexOfPieceInNewLocation) {
// Remove the pre-existing piece from the target square
        if (indexOfPieceInNewLocation != -1) {
            chess_pieces[indexOfPieceInNewLocation].setLocationOfBoardSquareOccupied(new int[]{-1, -1});
        }

        // Remove the piece from the old location
        int[] oldLocationOfMovingPiece = new int[2];
        oldLocationOfMovingPiece[0] = chess_pieces[movingPieceIndex].getLocationOfBoardSquareOccupied()[0];
        oldLocationOfMovingPiece[1] = chess_pieces[movingPieceIndex].getLocationOfBoardSquareOccupied()[1];

        board_squares[oldLocationOfMovingPiece[0]][oldLocationOfMovingPiece[1]].setOccupiedByPiece(false);
        board_squares[oldLocationOfMovingPiece[0]][oldLocationOfMovingPiece[1]].setIndexOfOccupyingPiece(-1);

        // Move_Chain it to the new location
        chess_pieces[movingPieceIndex].setLocationOfBoardSquareOccupied(newPieceLocation);

        board_squares[newPieceLocation[0]][newPieceLocation[1]].setOccupiedByPiece(true);
        board_squares[newPieceLocation[0]][newPieceLocation[1]].setIndexOfOccupyingPiece(movingPieceIndex);

        boolean isCheck = false;

        if (isKingInCheck(isBlackMove, chess_pieces, board_squares)) {
            //System.out.println("IN CHECK");
            isCheck = true;
        }

        // Remove the piece from the new location
        // Put the piece back in the old location
        chess_pieces[movingPieceIndex].setLocationOfBoardSquareOccupied(oldLocationOfMovingPiece);
        board_squares[oldLocationOfMovingPiece[0]][oldLocationOfMovingPiece[1]].setOccupiedByPiece(true);
        board_squares[oldLocationOfMovingPiece[0]][oldLocationOfMovingPiece[1]].setIndexOfOccupyingPiece(movingPieceIndex);

        // Move_Chain original piece back (if applicable)
        if (indexOfPieceInNewLocation != -1) {
            chess_pieces[indexOfPieceInNewLocation].setLocationOfBoardSquareOccupied(new int[]{newPieceLocation[0], newPieceLocation[1]});
            board_squares[newPieceLocation[0]][newPieceLocation[1]].setIndexOfOccupyingPiece(indexOfPieceInNewLocation);
        }
        // Otherwise unoccupy the new location square
        else {
            board_squares[newPieceLocation[0]][newPieceLocation[1]].setOccupiedByPiece(false);
            board_squares[newPieceLocation[0]][newPieceLocation[1]].setIndexOfOccupyingPiece(-1);
        }
        return isCheck;
    }

    /**
     * Checks whether or not the king is in check (returns true if it is)
     *
     * @param isBlackMove
     * @param chess_pieces
     * @param board_squares
     * @return boolean      Whether or not the king is in check
     */
    private boolean isKingInCheck(boolean isBlackMove, Chess_Piece[] chess_pieces, Board_Square[][] board_squares) {
        int rowDelta, columnDelta;

        if (isBlackMove) {
            int[] boardSquareLocationKing = {chess_pieces[4].getLocationOfBoardSquareOccupied()[0], chess_pieces[4].getLocationOfBoardSquareOccupied()[1]};
            //System.out.println(boardSquareLocationKing[0] + ":" + boardSquareLocationKing[1]);
            for (int i = 16; i < chess_pieces.length; i++) {
                if (i == 28) {
                    continue;
                }
                rowDelta = boardSquareLocationKing[0] - chess_pieces[i].getLocationOfBoardSquareOccupied()[0];
                columnDelta = boardSquareLocationKing[1] - chess_pieces[i].getLocationOfBoardSquareOccupied()[1];
                if (!isMovePatternValid(chess_pieces[i], rowDelta, columnDelta, board_squares[boardSquareLocationKing[0]][boardSquareLocationKing[1]])) {
                    continue;
                }
                if (checkIfPieceInWay(chess_pieces[i].getLocationOfBoardSquareOccupied(), new int[]{rowDelta, columnDelta}, board_squares)) {
                    continue;
                }
                return true;
            }
        } else {
            int[] boardSquareLocationKing = {chess_pieces[28].getLocationOfBoardSquareOccupied()[0], chess_pieces[28].getLocationOfBoardSquareOccupied()[1]};
            //System.out.println(boardSquareLocationKing[0] + ":" + boardSquareLocationKing[1]);
            for (int i = 0; i < 16; i++) {
                if (i == 4) {
                    continue;
                }
                rowDelta = boardSquareLocationKing[0] - chess_pieces[i].getLocationOfBoardSquareOccupied()[0];
                columnDelta = boardSquareLocationKing[1] - chess_pieces[i].getLocationOfBoardSquareOccupied()[1];
                if (!isMovePatternValid(chess_pieces[i], rowDelta, columnDelta, board_squares[boardSquareLocationKing[0]][boardSquareLocationKing[1]])) {
                    continue;
                }
                if (checkIfPieceInWay(chess_pieces[i].getLocationOfBoardSquareOccupied(), new int[]{rowDelta, columnDelta}, board_squares)) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Getter method for all possible moves that can be made with a given piece
     *
     * @param chess_pieces
     * @param pieceIndex
     * @param board_squares
     * @return
     */
    private ArrayList<int[]> getPossibleMovesForPiece(Chess_Piece[] chess_pieces, int pieceIndex, Board_Square[][] board_squares) {
        ArrayList<int[]> possibleMoveLocations = new ArrayList<>();
        for (int r = 0; r < board_squares.length; r++) {
            for (int c = 0; c < board_squares[r].length; c++) {
                try {
                    if (isMoveValid(chess_pieces, pieceIndex, board_squares, new int[]{r, c}, chess_pieces[pieceIndex].getIsBlack())) {
                        possibleMoveLocations.add(new int[]{r, c});
                    }
                } catch (Exception e) {
                    //System.out.println(chess_pieces[pieceIndex].getLocationOfBoardSquareOccupied()[0]);
                    //e.printStackTrace(System.err);
                }
            }
        }
        return possibleMoveLocations;
    }

    public void findAndStoreAllPossibleMovesForPlayer(Chess_Piece[] chess_pieces, Board_Square[][] board_squares, boolean isBlack) {
        if (isBlack) {
            for (int i = 0; i < 16; i++) {
                // MAY HAVE A PROBLEM WITH POINTING TO THE SAME OBJECTS
                possibleMoves[i] = getPossibleMovesForPiece(chess_pieces, i, board_squares);
            }
        } else {
            for (int i = 0; i < 16; i++) {
                // MAY HAVE A PROBLEM WITH POINTING TO THE SAME OBJECTS
                possibleMoves[i] = getPossibleMovesForPiece(chess_pieces, i + 16, board_squares);
            }
        }
    }

    public ArrayList<int[]> getPossibleMovesForPiece(int pieceIndex) {
        // If it's more than 16, then it is a white piece, so decrease it by 16
        if (pieceIndex >= 16) {
            pieceIndex -= 16;
        }
        return possibleMoves[pieceIndex];
    }

    /**
     * @param indexOfMovingPiece The index of the moving chess piece in the chess_pieces array
     * @param target_location    The new location to which the square will be moved
     * @return boolean              Whether or not the move is legal
     */
    public boolean isMovePartOfPossibleMoves(int indexOfMovingPiece, int[] target_location) {
        // If it's more than 16, then it is a white piece, so decrease it by 16
        if (indexOfMovingPiece >= 16) {
            indexOfMovingPiece -= 16;
        }
        for (int[] possibleLocation : possibleMoves[indexOfMovingPiece]) {
            if (Arrays.equals(target_location, possibleLocation)) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver() {
        for (ArrayList<int[]> possibleMove : possibleMoves) {
            if (possibleMove.size() > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isPawnUpgradeable(Chess_Piece pawn, int targetRank) {
        // If the pawn is black
        if (pawn.getIsBlack()) {
            // If the black pawn is to be placed in the 1st rank (index 7), then it is upgradable
            return targetRank == 7;
        }
        // Otherwise if it's white
        else {
            // If the white pawn to be placed in the 8th rank (index 0), then it is upgradable
            return targetRank == 0;
        }
    }

}
