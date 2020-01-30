package ics3.chess;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Model class for the player clock which is used to ensure that each player only gets a certain amount
 * of time to play the game. It is responsible for updating and displaying the time.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since May 20th, 2017
 */
public class Gameflow_Controller_Engine implements ActionListener {

    // Whether or not it is the black player's turn
    private boolean isBlackTurn;

    // Index of the selected piece (in the Chess_Board.chess_pieces array)
    // It is -1 if no piece is selected
    private int indexOfSelectedPiece;

    // Whether or not the game is over
    private boolean isGameOver;

    // If the game ended because of a checkmate (it could also be because a player ran out of time)
    private boolean lostDueToCheckmate;

    // Whether or not a pawn promotion is pending (which occurs when a pawn reaches the 1st or 8th rank)
    private boolean pawnPromotionPending;

    // The square that the selected piece to be moved to
    private int[] targetSquarePendingToBeMovedTo;

    // Whether or not the user has selected the legal moves to be shown
    private boolean showMoves;

    // Whether or not the user wants the game to be timed
    private boolean isGameTimed;

    // The time limit (in seconds) for each player to play the game
    private int timeLimitSeconds;

    /**
     * Constructor for the Gameflow_Controller_Engine class
     */
    public Gameflow_Controller_Engine() {
        // Initialize isBlackTurn to false, since white always starts
        isBlackTurn = false;

        // No piece has been selected yet
        indexOfSelectedPiece = -1;

        // By default, the time limit is 600 seconds = 10 min
        timeLimitSeconds = 600;

        pawnPromotionPending = false;

        isGameOver = true;
        lostDueToCheckmate = false;

        // By default, the game is timed and moves are shown
        showMoves = true;
        isGameTimed = true;

        // Find and store all possible moves for the first move of the game
        Chess_Rules_Engine.getInstance().findAndStoreAllPossibleMovesForPlayer(Chess.chess_board.getChessPieces(), Chess.chess_board.getBoardSquares(), isBlackTurn);
    }

    /**
     * Getter method returns whether or not it's the black player's turn
     *
     * @return boolean  If it is the black player's turn
     */
    public boolean isBlackTurn() {
        return isBlackTurn;
    }

    /**
     * Setter method to set which player's turn it is
     *
     * @param blackTurn Whether or not it is the black player's turn
     */
    public void setBlackTurn(boolean blackTurn) {
        isBlackTurn = blackTurn;
    }

    /**
     * Getter method returns whether or not the game is over
     *
     * @return boolean  Whether or not the game is over
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Getter method returns whether or not the user wishes to see all the possible moves for a selected piece
     *
     * @return boolean  Whether or not the user wishes to see all the possible moves for a selected piece
     */
    public boolean isShowMoves() {
        return showMoves;
    }

    /**
     * Setter method for whether or not the user wishes to see all the possible moves for a selected piece
     *
     * @param showMoves Whether or not the legal moves should be shown to the players
     */
    public void setShowMoves(boolean showMoves) {
        this.showMoves = showMoves;
    }

    /**
     * Getter method for the index (in the Chess_Board.chess_pieces array) of the currently selected piece
     *
     * @return int  The index of the currently selected piece
     */
    public int getIndexOfSelectedPiece() {
        return indexOfSelectedPiece;
    }

    /**
     * Getter method returns whether or not the game ended due to a checkmate
     *
     * @return boolean  Whether or not the game ended due to a checkmate
     */
    public boolean isLostDueToCheckmate() {
        return lostDueToCheckmate;
    }

    /**
     * Updates the player time if the user has set the game to be timed
     */
    public void updatePlayerTime() {
        if (isGameTimed && !isGameOver) {
            // If it's the black player's turn, then update his/her time
            if (isBlackTurn()) {
                Chess.blackPlayer.getPlayerClock().updateTimeElapsed();
                // If the time is up, then the game is over
                if (Chess.blackPlayer.getPlayerClock().getTimeRemainingSeconds() <= 0) {
                    performGameoverAlgorithm(true, false);
                }
            }
            // Otherwise, update the white player's time
            else {
                Chess.whitePlayer.getPlayerClock().updateTimeElapsed();
                // If the time is up, then the game is over
                if (Chess.whitePlayer.getPlayerClock().getTimeRemainingSeconds() <= 0) {
                    performGameoverAlgorithm(false, false);
                }
            }
        }
    }

    /**
     * Performs the appropriate behaviour when the game is over
     *
     * @param didBlackLose Whether or not the black player lost the game
     * @param wasCheckmate Whether or not the loss was due to a checkmate (loss can also occur if time is up)
     */
    public void performGameoverAlgorithm(boolean didBlackLose, boolean wasCheckmate) {
        isGameOver = true;
        lostDueToCheckmate = wasCheckmate;
        Chess.gameplay_menu.setGameoverComponentsVisible(true);
        if (didBlackLose) {
            Chess.whitePlayer.setWonGame(true);
        } else {
            Chess.blackPlayer.setWonGame(true);
        }
    }

    /**
     * Resets the position of the pieces, the time remaining for each player, and other variables in order
     * to "restart" the game
     */
    public void restartGame() {
        // Set the game over variables false
        isGameOver = false;
        lostDueToCheckmate = false;
        // White must always start
        isBlackTurn = false;
        // Reset the chess board
        Chess.chess_board.resetBoard();
        // Reset the player clocks
        Chess.whitePlayer.getPlayerClock().setTimeLimit(timeLimitSeconds);
        Chess.blackPlayer.getPlayerClock().setTimeLimit(timeLimitSeconds);
        // Clear the shown possible moves
        clearShownPossibleMoves();
        // Recalculate all possible moves
        Chess_Rules_Engine.getInstance().findAndStoreAllPossibleMovesForPlayer(Chess.chess_board.getChessPieces(), Chess.chess_board.getBoardSquares(), isBlackTurn);
    }

    /**
     * Helper method that is used to determine the appropriate behaviour when a board square is clicked on
     *
     * @param board_square                         The board square that was clicked on
     * @param rowColumnLocationOfTargetBoardSquare The row/column location of the board square that was pressed on
     */
    public void handleMousePressedBoardSquare(Board_Square board_square, int[] rowColumnLocationOfTargetBoardSquare) {
        if (isGameOver()) {
            return;
        }
        // If a pawn promotion is pending, do not accept any moves on the chess board, since the player must first click on
        // the piece they want to promote the pawn into (done in a different panel, and calls a different method in this class)
        if (!pawnPromotionPending) {
            // Select a piece if no piece has yet been selected
            if (indexOfSelectedPiece == -1) {
                // If the board square has a piece on it, then select that piece, only if the piece that is being selected
                // is a piece belonging to the player whose turn it is
                if (board_square.isOccupiedByPiece()) {
                    int indexOfPieceInTargetSquare = board_square.getIndexOfOccupyingPiece();
                    if (Chess.chess_board.getChessPiece(indexOfPieceInTargetSquare).getIsBlack() == isBlackTurn()) {
                        Chess.chess_board.getChessPiece(indexOfPieceInTargetSquare).setHasBeenSelected(true);
                        indexOfSelectedPiece = indexOfPieceInTargetSquare;
                        if (showMoves) {
                            showPossibleMoveLocations();
                        }
                    }
                }
            }
            // Else (if a piece has been selected and a move is attempting to be made), if the move is legal, make the move
            else {
                // If the move is legal
                if (Chess_Rules_Engine.getInstance().isMovePartOfPossibleMoves(indexOfSelectedPiece, rowColumnLocationOfTargetBoardSquare)) {
                    // If a pawn promotion is pending
                    if (Chess.chess_board.getChessPiece(indexOfSelectedPiece).getPieceIdentity().equals("pawn") && Chess_Rules_Engine.getInstance().isPawnUpgradeable(Chess.chess_board.getChessPiece(indexOfSelectedPiece), rowColumnLocationOfTargetBoardSquare[0])) {
                        pawnPromotionPending = true;
                        targetSquarePendingToBeMovedTo = rowColumnLocationOfTargetBoardSquare;
                        // Set the promotion panel visible
                        Chess.gameplay_menu.initializePromotionPanel(Chess.chess_board.getChessPiece(indexOfSelectedPiece).getIsBlack());
                        Chess.gameplay_menu.setPromotionPanelVisible(true);
                    }
                    // Otherwise make the move
                    else {
                        moveSelectedChessPieceToSquare(rowColumnLocationOfTargetBoardSquare);

                        indexOfSelectedPiece = -1;
                        setBlackTurn(!isBlackTurn());
                        clearShownPossibleMoves();

                        Chess_Rules_Engine.getInstance().findAndStoreAllPossibleMovesForPlayer(Chess.chess_board.getChessPieces(), Chess.chess_board.getBoardSquares(), isBlackTurn);
                        if (Chess_Rules_Engine.getInstance().isGameOver()) {
                            performGameoverAlgorithm(isBlackTurn(), true);
                        } else {
                            Chess.chess_board.startRotate();
                        }
                    }
                }
                // Otherwise (if the move is illegal), deselect the piece and clear the shown possible moves
                else {
                    Chess.chess_board.getChessPieces()[indexOfSelectedPiece].setHasBeenSelected(false);
                    indexOfSelectedPiece = -1;
                    if (showMoves) {
                        clearShownPossibleMoves();
                    }
                }
            }
        }
    }

    /**
     * Helper method used to determine the appropriate behaviour when the promotion panel registers a MouseEvent
     *
     * @param pieceToPromoteTo The piece that the user selected to promote the pawn to
     */
    public void handleMousePressedPromotionPanel(String pieceToPromoteTo) {
        // Set the identity of the pawn to the chess piece which the user wishes to promote their pawn to
        Chess.chess_board.getChessPiece(indexOfSelectedPiece).setPieceIdentity(pieceToPromoteTo);
        pawnPromotionPending = false;
        // Finish making the move (the move was paused because the program was waiting for the user to indicate
        // which piece to promote to)
        moveSelectedChessPieceToSquare(targetSquarePendingToBeMovedTo);
        targetSquarePendingToBeMovedTo = null;
        // Set the promotion panel invisible
        Chess.gameplay_menu.setPromotionPanelVisible(false);
        // Recalculate all possible moves and, if necessary, end the game
        Chess_Rules_Engine.getInstance().findAndStoreAllPossibleMovesForPlayer(Chess.chess_board.getChessPieces(), Chess.chess_board.getBoardSquares(), isBlackTurn);
        if (Chess_Rules_Engine.getInstance().isGameOver()) {
            performGameoverAlgorithm(isBlackTurn(), true);
        } else {
            Chess.chess_board.startRotate();
        }
    }

    /**
     * Method used to move the selected chess piece to a certain square on the chess board. This method
     * will automatically capture a piece in the target square if necessary.
     *
     * @param locationOfBoardSquareToMoveTo The location to move the selected chess piece to
     */
    private void moveSelectedChessPieceToSquare(int[] locationOfBoardSquareToMoveTo) {
        int columnDelta = locationOfBoardSquareToMoveTo[1] - Chess.chess_board.getChessPiece(indexOfSelectedPiece).getLocationOfBoardSquareOccupied()[1];
        // If there is a square on the location to move to, capture that square
        if (Chess.chess_board.getBoardSquare(locationOfBoardSquareToMoveTo[0], locationOfBoardSquareToMoveTo[1]).isOccupiedByPiece()) {
            int indexOfOccupyingPiece = Chess.chess_board.getBoardSquare(locationOfBoardSquareToMoveTo[0], locationOfBoardSquareToMoveTo[1]).getIndexOfOccupyingPiece();
            Chess.chess_board.getChessPieces()[indexOfOccupyingPiece].setLocationOfBoardSquareOccupied(new int[]{-1, -1});
            Chess.chess_board.getChessPieces()[indexOfOccupyingPiece].setObjectPosition(-100, -100);
            Chess.chess_board.getChessPieces()[indexOfOccupyingPiece].setHasBeenCaptured(false);
        }
        // Remove the piece from its current square
        int[] currentSquareOccupiedByPiece = Chess.chess_board.getChessPieces()[indexOfSelectedPiece].getLocationOfBoardSquareOccupied();
        Chess.chess_board.getBoardSquare(currentSquareOccupiedByPiece[0], currentSquareOccupiedByPiece[1]).setOccupiedByPiece(false);
        Chess.chess_board.getBoardSquare(currentSquareOccupiedByPiece[0], currentSquareOccupiedByPiece[1]).setIndexOfOccupyingPiece(-1);

        // Move_Chain the piece to the new square
        int[] newPieceXYPosition = Chess.chess_board.getBoardSquare(locationOfBoardSquareToMoveTo[0], locationOfBoardSquareToMoveTo[1]).getObjectPosition();
        Chess.chess_board.getChessPieces()[indexOfSelectedPiece].setObjectPosition(newPieceXYPosition[0], newPieceXYPosition[1]);
        Chess.chess_board.getChessPieces()[indexOfSelectedPiece].setLocationOfBoardSquareOccupied(locationOfBoardSquareToMoveTo);

        // Give the square the updated properties resulting from the placement of the piece on that square
        Chess.chess_board.getBoardSquare(locationOfBoardSquareToMoveTo[0], locationOfBoardSquareToMoveTo[1]).setOccupiedByPiece(true);
        Chess.chess_board.getBoardSquare(locationOfBoardSquareToMoveTo[0], locationOfBoardSquareToMoveTo[1]).setIndexOfOccupyingPiece(indexOfSelectedPiece);

        Chess.chess_board.getChessPieces()[indexOfSelectedPiece].setIsFirstMove(false);
        Chess.chess_board.getChessPieces()[indexOfSelectedPiece].setHasBeenSelected(false);

        // This code is used to perform a castle (special move involving the king and rook) if the user has made that move
        if (Chess.chess_board.getChessPiece(indexOfSelectedPiece).getPieceIdentity().equals("king") && Chess_Rules_Engine.getInstance().getWasMoveACastle()) {
            if (Chess_Rules_Engine.getInstance().checkIfOneOfCastleMoves(locationOfBoardSquareToMoveTo)) {
                int rookBeingCastledWithAndDirection = -1;
                if (isBlackTurn) {
                    if (columnDelta == 2) {
                        rookBeingCastledWithAndDirection = Chess_Rules_Engine.getInstance().getRookBeingCastledWithAndDirection(0);
                    } else if (columnDelta == -2) {
                        rookBeingCastledWithAndDirection = Chess_Rules_Engine.getInstance().getRookBeingCastledWithAndDirection(2);
                    }
                } else {
                    if (columnDelta == 2) {
                        rookBeingCastledWithAndDirection = Chess_Rules_Engine.getInstance().getRookBeingCastledWithAndDirection(1);
                    } else if (columnDelta == -2) {
                        rookBeingCastledWithAndDirection = Chess_Rules_Engine.getInstance().getRookBeingCastledWithAndDirection(3);
                    }
                }
                System.out.println(rookBeingCastledWithAndDirection);
                boolean doesRookMoveLeft = rookBeingCastledWithAndDirection < 0;
                // If the rook moves to the left for the castle (i.e. king-side castling)
                if (doesRookMoveLeft) {
                    int kingIndex = indexOfSelectedPiece;
                    indexOfSelectedPiece = -rookBeingCastledWithAndDirection;
                    moveSelectedChessPieceToSquare(new int[]{Chess.chess_board.getChessPiece(kingIndex).getLocationOfBoardSquareOccupied()[0], Chess.chess_board.getChessPiece(kingIndex).getLocationOfBoardSquareOccupied()[1] - 1});
                    Chess_Rules_Engine.getInstance().setWasMoveACastle(false);
                }
                // If the rook moves to the left for the castle (i.e. king-side castling)
                else {
                    int kingIndex = indexOfSelectedPiece;
                    indexOfSelectedPiece = rookBeingCastledWithAndDirection;
                    moveSelectedChessPieceToSquare(new int[]{Chess.chess_board.getChessPiece(kingIndex).getLocationOfBoardSquareOccupied()[0], Chess.chess_board.getChessPiece(kingIndex).getLocationOfBoardSquareOccupied()[1] + 1});
                    Chess_Rules_Engine.getInstance().setWasMoveACastle(false);
                }
            }
        }
    }

    /**
     * Used to show all the legal moves that a player can make (i.e the places that the selected
     * piece can move to
     */
    private void showPossibleMoveLocations() {
        // Cycle through all the legal moves for the selected piece and show them to the user
        for (int[] squareToMoveTo : Chess_Rules_Engine.getInstance().findPossibleMovesForPiece(indexOfSelectedPiece)) {
            if (Chess.chess_board.getBoardSquare(squareToMoveTo[0], squareToMoveTo[1]).isOccupiedByPiece()) {
                Chess.chess_board.getChessPieces()[Chess.chess_board.getBoardSquare(squareToMoveTo[0], squareToMoveTo[1]).getIndexOfOccupyingPiece()].setHighlighted(true);
            } else {
                Chess.chess_board.getBoardSquare(squareToMoveTo[0], squareToMoveTo[1]).setHighlighted(true);
            }
        }
    }

    /**
     * Used to clear/hide the shown possible moves every time a move is made (since the next
     * set of legal moves will be different
     */
    private void clearShownPossibleMoves() {
        // Cycle through all the shown moves and "unshow" them
        for (Board_Square[] row_of_board_squares : Chess.chess_board.getBoardSquares()) {
            for (Board_Square board_square : row_of_board_squares) {
                if (board_square.isOccupiedByPiece()) {
                    Chess.chess_board.getChessPieces()[board_square.getIndexOfOccupyingPiece()].setHighlighted(false);
                } else {
                    board_square.setHighlighted(false);
                }
            }
        }
    }

    /**
     * Implementation of the actionPerformed() method in the ActionListener interface. It is used
     * whenever a menu button or a combo box is pressed.
     *
     * @param e The ActionEvent that was passed in by the menubar
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the ActionEvent source's text, which identifies the source
        JMenuItem menuItemPressed = (JMenuItem) (e.getSource());
        // Based on what the source is, perform the appropriate behaviour
        switch (menuItemPressed.getText()) {
            case "Show possible moves":
                showMoves = !showMoves;
                if (!showMoves) {
                    clearShownPossibleMoves();
                } else {
                    showPossibleMoveLocations();
                }
                break;
            case "Is game timed":
                isGameTimed = !isGameTimed;
                Chess.ui.getJMenuBar().getMenu(1).getItem(1).setEnabled(isGameTimed);
                Chess.whitePlayer.getPlayerClock().setVisible(isGameTimed);
                Chess.blackPlayer.getPlayerClock().setVisible(isGameTimed);
                break;
            // If the user has made a selection for the time limit, then assign that time limit
            // by calling the decodeAndAssignDesiredTimeLimit() helper method
            case "1 min":
                decodeAndAssignDesiredTimeLimit("1 min");
                break;
            case "3 min":
                decodeAndAssignDesiredTimeLimit("3 min");
                break;
            case "5 min":
                decodeAndAssignDesiredTimeLimit("5 min");
                break;
            case "10 min":
                decodeAndAssignDesiredTimeLimit("10 min");
                break;
            case "15 min":
                decodeAndAssignDesiredTimeLimit("15 min");
                break;
            case "30 min":
                decodeAndAssignDesiredTimeLimit("30 min");
                break;
            case "60 min":
                decodeAndAssignDesiredTimeLimit("60 min");
                break;
            case "120 min":
                decodeAndAssignDesiredTimeLimit("120 min");
                break;
            // If the source is none of the above, then that means that a button's behaviour hasn't been implemented
            default:
                System.err.println("BEHAVIOUR FOR MENU BUTTON '" + menuItemPressed.getText() + "' NOT IMPLEMENTED IN GAMEFLOW CONTROLLER");
        }
    }

    /**
     * Used to set the time limit for each player
     *
     * @param selectedTimeMinutes Time limit (in minutes) for each player
     */
    public void decodeAndAssignDesiredTimeLimit(String selectedTimeMinutes) {
        // Get rid of the ' min' at the end
        selectedTimeMinutes = selectedTimeMinutes.substring(0, selectedTimeMinutes.length() - 4);
        int timeLimitDesiredMinutes;
        // Try to parse the input as a number, and if the input is invalid, default to 10
        try {
            timeLimitDesiredMinutes = Integer.parseInt(selectedTimeMinutes);
        } catch (NumberFormatException nfe) {
            timeLimitDesiredMinutes = 10;
        }
        // Assign the timeLimit to the playerClocks for each player (each takes in number of seconds as parameters)
        timeLimitSeconds = timeLimitDesiredMinutes * 60;
        Chess.whitePlayer.getPlayerClock().setTimeLimit(timeLimitSeconds);
        Chess.blackPlayer.getPlayerClock().setTimeLimit(timeLimitSeconds);
    }

}