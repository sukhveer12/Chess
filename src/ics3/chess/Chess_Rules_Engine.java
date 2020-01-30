package ics3.chess;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Model class that is used to implement the rules of the Chess game. This is a Singleton
 * class that only has one instance in the program.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 10th, 2017
 */
public class Chess_Rules_Engine {

    // The only instance of the Chess_Rules_Engine
    private static Chess_Rules_Engine rulesEngineInstance = new Chess_Rules_Engine();

    // Getter method for the instance of the Chess_Rules_Engine
    public static Chess_Rules_Engine getInstance() {
        return rulesEngineInstance;
    }

    // ArrayList storing all possible moves that can be made by all pieces
    private ArrayList<int[]>[] possibleMoves;

    /**
     * Constructor for the Chess_Rules_Engine class
     */
    private Chess_Rules_Engine() {
        // Initialize the possibleMoves array
        // It is of length 16 since each player has 16 pieces (maximum), and all the possible
        // moves for those 16 pieces are stored in this array.
        possibleMoves = new ArrayList[16];
    }

    /**
     * Method used to determine if a certain move is valid
     *
     * @param chess_pieces      The array storing all the chess pieces
     * @param movingPieceIndex  The index of the moving piece
     * @param board_squares     The 2D array storing all the board squares
     * @param newPieceLocation  The new piece location
     * @param isBlackMove       Whether or not it is the black player's move
     * @return boolean          Whether or not the move is valid/legal
     * @throws Exception        If the location of the board square occupied by the moving chess piece is (-1,-1)
     *                          that means that the chess piece location hasn't been updated
     */
    private boolean isMoveValid(Chess_Piece[] chess_pieces, int movingPieceIndex, Board_Square[][] board_squares, int[] newPieceLocation, boolean isBlackMove) throws Exception {
        Chess_Piece movingChessPiece = chess_pieces[movingPieceIndex];
        if (movingChessPiece.getLocationOfBoardSquareOccupied()[0] == -1 || movingChessPiece.getLocationOfBoardSquareOccupied()[1] == -1) {
            throw new Exception("VARIABLE 'locationOfBoardSquareOccupied' HAS NOT BEEN UPDATED!!!");
        }

        // The index of the piece in the new location
        int indexOfPieceInNewLocation = -1;

        // If the moving piece and the piece in the new location are the same colour, then return false (since you cannot capture your own piece)
        if (board_squares[newPieceLocation[0]][newPieceLocation[1]].isOccupiedByPiece()) {
            indexOfPieceInNewLocation = board_squares[newPieceLocation[0]][newPieceLocation[1]].getIndexOfOccupyingPiece();
            if (chess_pieces[indexOfPieceInNewLocation].getIsBlack() == movingChessPiece.getIsBlack()) {
                return false;
            }
        }

        // The row and column delta variables storing how many rows up/down and columns left/right the piece is moving
        int rowDelta = newPieceLocation[0] - movingChessPiece.getLocationOfBoardSquareOccupied()[0];
        int columnDelta = newPieceLocation[1] - movingChessPiece.getLocationOfBoardSquareOccupied()[1];

        // Used to check if a castling (a special move between the king and rook) is taking place
        if (movingChessPiece.getPieceIdentity().equals("king") && movingChessPiece.getIsFirstMove() && rowDelta == 0 && indexOfPieceInNewLocation == -1) {
            // King goes right
            if (columnDelta == 2) {
                int rookIndexToCastleWith = board_squares[movingChessPiece.getLocationOfBoardSquareOccupied()[0]][movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 3].getIndexOfOccupyingPiece();
                Chess_Piece rookToCastleWith = chess_pieces[rookIndexToCastleWith];
                if (rookToCastleWith.getIsFirstMove() && !board_squares[movingChessPiece.getLocationOfBoardSquareOccupied()[0]][movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 1].isOccupiedByPiece()) {
                    boolean oneStepLegal = !checkIfKingInCheck(chess_pieces, movingPieceIndex, board_squares, new int[]{movingChessPiece.getLocationOfBoardSquareOccupied()[0], movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 1}, isBlackMove, -1);
                    boolean twoStepLegal = !checkIfKingInCheck(chess_pieces, movingPieceIndex, board_squares, new int[]{movingChessPiece.getLocationOfBoardSquareOccupied()[0], movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 2}, isBlackMove, -1);
                    wasMoveACastle = true;
                    if (isBlackMove) {
                        rookBeingCastledWithAndDirection[0] = -rookIndexToCastleWith;
                        boardSquareLocationsToCastle[0][0] = movingChessPiece.getLocationOfBoardSquareOccupied()[0];
                        boardSquareLocationsToCastle[0][1] = movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 2;
                        System.out.println(boardSquareLocationsToCastle[0][0] + ":" + boardSquareLocationsToCastle[0][1]);
                    } else {
                        rookBeingCastledWithAndDirection[1] = -rookIndexToCastleWith;
                        boardSquareLocationsToCastle[1][0] = movingChessPiece.getLocationOfBoardSquareOccupied()[0];
                        boardSquareLocationsToCastle[1][1] = movingChessPiece.getLocationOfBoardSquareOccupied()[1] + 2;
                        System.out.println(boardSquareLocationsToCastle[1][0] + ":" + boardSquareLocationsToCastle[1][1]);
                    }
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
                    if (isBlackMove) {
                        rookBeingCastledWithAndDirection[2] = rookIndexToCastleWith;
                        boardSquareLocationsToCastle[2][0] = movingChessPiece.getLocationOfBoardSquareOccupied()[0];
                        boardSquareLocationsToCastle[2][1] = movingChessPiece.getLocationOfBoardSquareOccupied()[1] - 2;
                        System.out.println(boardSquareLocationsToCastle[2][0] + ":" + boardSquareLocationsToCastle[2][1]);
                    } else {
                        rookBeingCastledWithAndDirection[3] = rookIndexToCastleWith;
                        boardSquareLocationsToCastle[3][0] = movingChessPiece.getLocationOfBoardSquareOccupied()[0];
                        boardSquareLocationsToCastle[3][1] = movingChessPiece.getLocationOfBoardSquareOccupied()[1] - 2;
                        System.out.println(boardSquareLocationsToCastle[3][0] + ":" + boardSquareLocationsToCastle[3][1]);
                    }
                    return oneStepLegal && twoStepLegal;
                }
            }
        }

        // If the move pattern is invalid, return false
        if (!isMovePatternValid(chess_pieces[movingPieceIndex], rowDelta, columnDelta, board_squares[newPieceLocation[0]][newPieceLocation[1]])) {
            return false;
        }

        // If the piece is not a knight and is jumping over another piece, return false
        if (!chess_pieces[movingPieceIndex].getPieceIdentity().equals("knight")) {
            if (checkIfPieceInWay(chess_pieces[movingPieceIndex].getLocationOfBoardSquareOccupied(), new int[]{rowDelta, columnDelta}, board_squares)) {
                return false;
            }
        }

        // If the king isn't in check, then return true, else return false
        return !checkIfKingInCheck(chess_pieces, movingPieceIndex, board_squares, newPieceLocation, isBlackMove, indexOfPieceInNewLocation);
    }

    /**
     * Method that checks if the move pattern of a specific piece is correct (e.g. if a bishop is moving in its
     * predefined way).
     *
     * @param chess_piece   The chess_piece that is being moved
     * @param rowDelta      The row delta of movement (how many squares left/right)
     * @param columnDelta   The column delta of movement (how many squares up/down)
     * @param targetBoardSquare The square being moved to
     * @return boolean      Whether or not the move pattern is valid
     */
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

    /**
     * Method checks if a piece is in the way of movement of the moving piece
     *
     * @param currentBoardLocationPiece     The current [row,column] location of the board square occupied by the moving piece
     * @param RCDelta       The row column delta of movement
     * @param board_squares The 2D board_squares array
     * @return boolean      Whether or not a piece is in the way of the movement of the moving piece
     */
    private boolean checkIfPieceInWay(int[] currentBoardLocationPiece, int RCDelta[], Board_Square[][] board_squares) {
        // Cycle through every row-delta, and increase column-delta by the corresponding amount (ratio method) and check
        // if there is a piece in the way

        // Find the row delta and column delta per square of movement
        int perRowDelta = RCDelta[0] / Math.max(Math.abs(RCDelta[0]), Math.abs(RCDelta[1]));
        int perColumnDelta = RCDelta[1] / Math.max(Math.abs(RCDelta[0]), Math.abs(RCDelta[1]));
        // Iterate through each square in between the current location and target location and check if there is a piece
        // in the way.
        int passNumber = 1;
        while (passNumber < Math.max(Math.abs(RCDelta[0]), Math.abs(RCDelta[1]))) {
            if (board_squares[currentBoardLocationPiece[0] + (passNumber * perRowDelta)][currentBoardLocationPiece[1] + (passNumber * perColumnDelta)].isOccupiedByPiece()) {
                return true;
            }
            passNumber++;
        }
        // If the iteration completes and no piece was in the way, return false
        return false;
    }

    // Properties pertaining to castling (a special move between the king and rook)
    private boolean wasMoveACastle = false;
    private int rookBeingCastledWithAndDirection[] = new int[4];
    private int[][] boardSquareLocationsToCastle = new int[4][2];

    /**
     * Getter method for whether or not the most recent move was a castle
     *
     * @return boolean  Whether or not the most recent move was a castle
     */
    public boolean getWasMoveACastle() {
        return wasMoveACastle;
    }

    /**
     * Setter method for whether or not the most recent move was a castle
     *
     * @param state     Whether or not the most recent move was a castle
     */
    public void setWasMoveACastle(boolean state) {
        this.wasMoveACastle = state;
    }

    /**
     * Getter method for a certain element of the rookBeingCastledWithAndDirection array
     *
     * @return int  The rook being castled with and the direction that the rook moves
     */
    public int getRookBeingCastledWithAndDirection(int index) {
        return rookBeingCastledWithAndDirection[index];
    }

    /**
     * Checks if a certain move is part of one of the possible castle moves
     *
     * @param newKingLocation   The location where the king is being moved
     * @return boolean      Whether or not the inputted move is a castling move
     */
    public boolean checkIfOneOfCastleMoves(int[] newKingLocation) {
        for (int[] castleLocation : boardSquareLocationsToCastle) {
            if (Arrays.equals(castleLocation, newKingLocation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method checks whether or not the player's (whose turn it currently is) king is in check
     *
     * @param chess_pieces  The chess_pieces array
     * @param movingPieceIndex The index of the moving piece (in the chess_pieces array)
     * @param board_squares     The board_squares 2D array
     * @param newPieceLocation  The location to which the piece is to be moved
     * @param isBlackMove       Whether or not it is the black player's move
     * @param indexOfPieceInNewLocation Index of the piece that is occupying the new location
     * @return boolean      Whether or not the player's king is in check
     */
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
     * @param isBlackMove   Whether or not it is the black player's move
     * @param chess_pieces  The chess_pieces array
     * @param board_squares The board_squares 2D array
     * @return boolean      Whether or not the king is in check
     */
    private boolean isKingInCheck(boolean isBlackMove, Chess_Piece[] chess_pieces, Board_Square[][] board_squares) {
        int rowDelta, columnDelta;

        if (isBlackMove) {
            int[] boardSquareLocationKing = {chess_pieces[4].getLocationOfBoardSquareOccupied()[0], chess_pieces[4].getLocationOfBoardSquareOccupied()[1]};
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
     * Method that determines all possible moves that can be made with a given piece
     *
     * @param chess_pieces  The chess_pieces array
     * @param pieceIndex    The index of the moving piece (in the chess_pieces array)
     * @param board_squares The board_squares 2D array
     * @return ArrayList<int[]>  The possible locations to which the specified piece can move to
     */
    private ArrayList<int[]> findPossibleMovesForPiece(Chess_Piece[] chess_pieces, int pieceIndex, Board_Square[][] board_squares) {
        ArrayList<int[]> possibleMoveLocations = new ArrayList<>();
        for (int r = 0; r < board_squares.length; r++) {
            for (int c = 0; c < board_squares[r].length; c++) {
                try {
                    if (isMoveValid(chess_pieces, pieceIndex, board_squares, new int[]{r, c}, chess_pieces[pieceIndex].getIsBlack())) {
                        possibleMoveLocations.add(new int[]{r, c});
                    }
                } catch (Exception e) {
                }
            }
        }
        return possibleMoveLocations;
    }

    /**
     * Method to find and store all the possible moves for a player in the possibleMoves array
     *
     * @param chess_pieces  The chess_pieces array
     * @param board_squares The board_squares 2D array
     * @param isBlack       Whether or not it is the black player's turn
     */
    public void findAndStoreAllPossibleMovesForPlayer(Chess_Piece[] chess_pieces, Board_Square[][] board_squares, boolean isBlack) {
        if (isBlack) {
            for (int i = 0; i < 16; i++) {
                possibleMoves[i] = findPossibleMovesForPiece(chess_pieces, i, board_squares);
            }
        } else {
            for (int i = 0; i < 16; i++) {
                possibleMoves[i] = findPossibleMovesForPiece(chess_pieces, i + 16, board_squares);
            }
        }
    }

    /**
     * Getter method for all the possible moves for a specific piece
     *
     * @param pieceIndex    The index of the piece (in the chess_pieces array) for which the possible moves are to be returned
     * @return ArrayList<int[]>  The possible locations to which the specified piece can move to
     */
    public ArrayList<int[]> findPossibleMovesForPiece(int pieceIndex) {
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

    /**
     * Method checks whether the game is over. Returns true if it is, else it returns false
     *
     * @return boolean  Whether or not the game is over
     */
    public boolean isGameOver() {
        for (ArrayList<int[]> possibleMove : possibleMoves) {
            if (possibleMove.size() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Whether or not the pawn is upgradeable/promotable (which occurs
     * when the pawn reaches the 1st or 8th rank)
     *
     * @param pawn  The pawn that might get upgraded
     * @param targetRank   The rank that the pawn is moving to
     * @return boolean      Whether or not the pawn is promotable
     */
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
