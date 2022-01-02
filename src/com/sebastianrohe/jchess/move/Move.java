package com.sebastianrohe.jchess.move;

import com.sebastianrohe.jchess.board.Board;
import com.sebastianrohe.jchess.board.tiles.Tile;
import com.sebastianrohe.jchess.pieces.*;
import com.sebastianrohe.jchess.settings.GameInitializer;

import java.util.ArrayList;

/**
 * Class to manage move execution, further calculations and board representation after move. Some of
 * the algorithms used in this class are based on or inspired by FilipFilipov (https://github.com/FilipFilipov).
 */
public class Move {

    protected static Tile activeTile;
    protected static Tile targetTile;
    protected static Tile previousActiveTile;
    protected static Tile previousTargetTile;
    protected static Tile enPassantWhiteTarget;
    protected static Tile enPassantBlackTarget;
    protected static boolean canCastleWhLong = true;
    protected static boolean canCastleWhShort = true;
    protected static boolean canCastleBlLong = true;
    protected static boolean canCastleBlShort = true;
    protected static boolean isWhitesTurn = true;
    protected static boolean isPieceSelected = false;
    protected static boolean whiteInCheck = false;
    protected static boolean blackInCheck = false;

    public static boolean canCastleWhLong() {
        return canCastleWhLong;
    }

    public static boolean canCastleWhShort() {
        return canCastleWhShort;
    }

    public static boolean canCastleBlLong() {
        return canCastleBlLong;
    }

    public static boolean canCastleBlShort() {
        return canCastleBlShort;
    }

    public static Tile getEnPassantWhiteTarget() {
        return enPassantWhiteTarget;
    }

    public static Tile getEnPassantBlackTarget() {
        return enPassantBlackTarget;
    }

    public Tile getActiveTile() {
        return activeTile;
    }

    public Tile getTargetTile() {
        return targetTile;
    }

    public boolean isWhitesTurn() {
        return isWhitesTurn;
    }

    public boolean isPieceSelected() {
        return isPieceSelected;
    }

    public void calculateMove() {
        // Find out what piece will be moving.
        Piece currentPieceForMove = activeTile.getCurrentPiece();
        // Make backups of the current active and target tiles,
        // in case a move fails, and we need to reverse it.
        previousTargetTile = new Tile(targetTile.getCoordinateX(), targetTile.getCoordinateY(), targetTile.hasLightColor());
        previousActiveTile = new Tile(activeTile.getCoordinateX(), activeTile.getCoordinateY(), activeTile.hasLightColor());
        // Check if the start and end position make for a legal move.
        if (currentPieceForMove.isLegalMove(activeTile, targetTile)) {
            // If yes, then get the indexes of any tiles between them.
            ArrayList<Integer> path = currentPieceForMove.getMovingPath(activeTile, targetTile);
            // Check if there is anything on those tiles.
            if (isClearMovingPath(path)) {
                // If the king is trying to move by two spaces, the player
                // may be trying to perform a castling, so check if they can.
                if (currentPieceForMove instanceof King &&
                        (Math.abs(targetTile.getCoordinateY() - activeTile.getCoordinateY()) == 2)) {
                    // There's two distinct types of castling, a long one(left) and a short one(right).
                    int castlingDirection = targetTile.getCoordinateY() - activeTile.getCoordinateY();
                    Tile newRookPosition = tryCastlingMove(castlingDirection);
                    // If castling is successful, make sure it doesn't leave the king in check.
                    if (newRookPosition != null) {
                        if (kingIsSafe()) {
                            executeMove(currentPieceForMove);
                        } else {
                            goBackToSelection();
                            reverseCastlingMove(newRookPosition);
                        }
                    }
                } else {
                    // If the move is possible, make sure it doesn't leave the king in check.
                    tryMove(activeTile.getCurrentPiece());
                    if (kingIsSafe()) {
                        executeMove(currentPieceForMove);
                    } else {
                        goBackToSelection();
                    }
                }
            } else {
                System.err.print("The path is blocked!\n");
            }
        } else {
            System.err.print("That is not a legal move!\n");
        }
    }

    // Castling has a lot of prerequisites, make sure they are all met. Because the move involves
    // two figures, this method also returns the tile where the rook should go to, or null if the move fails.
    public Tile tryCastlingMove(int direction) {
        boolean castleLong = false;
        boolean castleShort = false;
        // Make sure the player isn't trying to castle out of a check.
        if ((isWhitesTurn && whiteInCheck) || (!isWhitesTurn && blackInCheck)) {
            System.out.println("You can't castle out of check!");
        } else if (direction == -2) {
            // If the attempted castling is long, make sure the rook's path is also clear.
            if (Board.getChessBoardTiles()[targetTile.getCoordinateX()][targetTile.getCoordinateY() - 1].isOccupied() || targetTile.isOccupied()) {
                System.out.println("Path between king and rook must be clear!");
            }
            // Then make sure the king's path is also clear.
            else {
                // Perform the move in two steps, by changing the active and target tiles manually.
                targetTile = Board.getChessBoardTiles()[targetTile.getCoordinateX()][targetTile.getCoordinateY() + 1];
                if (hasCastlingPath()) {
                    activeTile = targetTile;
                    targetTile = Board.getChessBoardTiles()[targetTile.getCoordinateX()][targetTile.getCoordinateY() - 1];
                    // We know the second part of the move will never fail, because we already
                    // checked that the target tile is clear, which is why the result here is not saved.
                    tryMove(activeTile.getCurrentPiece());
                    castleLong = true;
                }
            }
        }
        // Short castling - same deal as above except the moves are in the other direction
        // and you don't need to check the rook's path, as the king passes through the same tiles.
        else if (direction == 2) {
            if (targetTile.isOccupied()) {
                System.out.println("Path between king and rook must be clear!");
            } else {
                targetTile = Board.getChessBoardTiles()[targetTile.getCoordinateX()][targetTile.getCoordinateY() - 1];
                if (hasCastlingPath()) {
                    activeTile = targetTile;
                    targetTile = Board.getChessBoardTiles()[targetTile.getCoordinateX()][targetTile.getCoordinateY() + 1];
                    tryMove(activeTile.getCurrentPiece());
                    castleShort = true;
                }
            }
        }
        // Finally, find out where the rook should go.
        // This will return null is the king's move failed, as conveyed by the flags.
        return getRookPosition(castleLong, castleShort);
    }

    // The king cannot move through a check position during castling,
    // so perform the first half of his move and make sure he's not in check.
    // We do not update the visible part of the board yet.
    public boolean hasCastlingPath() {
        tryMove(activeTile.getCurrentPiece());
        if (!kingIsSafe()) {
            // If he is in check reverse the move.
            reverseMove(activeTile, targetTile);
            System.err.println("You can't castle through a check position!");
            return false;
        }
        return true;
    }

    public Tile getRookPosition(boolean castleLong, boolean castleShort) {
        Tile oldRookPosition = null;
        Tile newRookPosition = null;
        // The relative movement is identical for the black and white figures.
        if (targetTile.getCoordinateY() == 6 && castleShort) {
            oldRookPosition = Board.getChessBoardTiles()[targetTile.getCoordinateX()][targetTile.getCoordinateY() + 1];
            newRookPosition = Board.getChessBoardTiles()[targetTile.getCoordinateX()][targetTile.getCoordinateY() - 1];
        } else if (targetTile.getCoordinateY() == 2 && castleLong) {
            oldRookPosition = Board.getChessBoardTiles()[targetTile.getCoordinateX()][targetTile.getCoordinateY() - 2];
            newRookPosition = Board.getChessBoardTiles()[targetTile.getCoordinateX()][targetTile.getCoordinateY() + 1];
        }
        // Only one of the two flags can be true, and only
        // if the king's part of the castling was successful.
        if (castleLong || castleShort) {
            assert newRookPosition != null;
            newRookPosition.setCurrentPiece(oldRookPosition.getCurrentPiece());
            oldRookPosition.clearTile();
        }
        return newRookPosition;
    }

    // Check all the path indexes in the board matrix to make sure the tiles are clear.
    public boolean isClearMovingPath(ArrayList<Integer> path) {
        for (int i = 0; i < path.size(); i += 2) {
            if (Board.getChessBoardTiles()[path.get(i)][path.get(i + 1)].isOccupied()) {
                return false;
            }
        }
        return true;
    }

    // Perform a move and handle some special cases. We already know the move is possible.
    public void tryMove(Piece currentPiece) {
        targetTile.setCurrentPiece(currentPiece);
        activeTile.clearTile();
        // If moving a pawn...
        if (currentPiece instanceof Pawn) {
            // Check if it moved in a diagonal.
            boolean pawnAttackMove = activeTile.getCoordinateY() == targetTile.getCoordinateY() + 1 ||
                    activeTile.getCoordinateY() == targetTile.getCoordinateY() - 1;
            // Check if it's making a two-tile jump, and set the tile
            // that will be opened to an en passant capture on the next move.
            if (Math.abs(targetTile.getCoordinateX() - activeTile.getCoordinateX()) == 2) {
                if (isWhitesTurn) {
                    enPassantBlackTarget = Board.getChessBoardTiles()[targetTile.getCoordinateX() + 1][targetTile.getCoordinateY()];
                } else {
                    enPassantWhiteTarget = Board.getChessBoardTiles()[targetTile.getCoordinateX() - 1][targetTile.getCoordinateY()];
                }
            }
            // If a capture move was performed on an empty tile, we have an
            // en passant capture, and need to manually remove the captured pawn.
            else if (pawnAttackMove && targetTile.getPreviousPiece() == null) {
                Board.getChessBoardTiles()[activeTile.getCoordinateX()][targetTile.getCoordinateY()].clearTile();
            }
        }

        // Update the kings position when it moves.
        if (currentPiece instanceof King) {
            if (isWhitesTurn) {
                GameInitializer.getBoard().whiteKingPosition = targetTile;
            } else {
                GameInitializer.getBoard().blackKingPosition = targetTile;
            }
        }
    }

    public boolean kingIsSafe() {
        // First find where the current player's king is.
        Tile kingTile = isWhitesTurn ? GameInitializer.getBoard().whiteKingPosition : GameInitializer.getBoard().blackKingPosition;
        int kingRow = kingTile.getCoordinateX();
        int kingCol = kingTile.getCoordinateY();
        // We perform two consecutive searches around the king's position.
        // The first is for knights only, because they can threaten him from unusual positions.
        boolean doWideSearch = false;
        for (int i = 0; i < 2; i++) {
            // The first search goes through every tile two spaces away,
            // since knights can only move two titles in any one direction,
            // the second search starts from every tile one space away from the king.
            int searchStart = doWideSearch ? -1 : -2;
            int searchEnd = doWideSearch ? 1 : 2;
            for (int rowDiff = searchStart; rowDiff <= searchEnd; rowDiff++) {
                for (int colDiff = searchStart; colDiff <= searchEnd; colDiff++) {
                    int testRow = kingRow + rowDiff;
                    int testCol = kingCol + colDiff;
                    // From each of the searched tiles, start looking in a straight
                    // line in that direction until you reach the end of the board.
                    while (isInBoardBounds(testRow) && isInBoardBounds(testCol)) {
                        Tile testTile = Board.getChessBoardTiles()[testRow][testCol];
                        // For each tile on the line, check if it's occupied.
                        if (testTile.isOccupied()) {
                            Piece testPiece = testTile.getCurrentPiece();
                            // Check if the piece is an enemy and
                            // it can legally move on the king's position.
                            if ((testPiece.isWhitePiece() != isWhitesTurn) &&
                                    testPiece.isLegalMove(testTile, kingTile)) {
                                // If we're searching for knights, we don't need to have
                                // a clear path, so we've proven the king is in check.
                                if (doWideSearch) {
                                    return false;
                                }
                                // If we're not searching for knights, we should
                                // also make sure the path to the king is not blocked.
                                ArrayList<Integer> path = testPiece.getMovingPath(testTile, kingTile);
                                if (isClearMovingPath(path)) {
                                    return false;
                                }
                            }
                            // If we found any figure on the line, the path
                            // after it is blocked, so we stop extending the line.
                            break;
                        }
                        // While looking for knights, don't extend the line, as knights never move in a straight line.
                        else if (!doWideSearch) {
                            break;
                        }
                        // Otherwise we extend the line.
                        else {
                            testRow += rowDiff;
                            testCol += colDiff;
                        }
                    }
                }
            }
            // Once we've done the knight search, move on to the general search.
            doWideSearch = true;
        }
        return true;
    }

    public boolean isInBoardBounds(int i) {
        return i >= 0 && i <= 7;
    }

    // Update the game state after a successful move.
    public void executeMove(Piece currentPiece) {
        moveFlagReset(currentPiece);
        // If a pawn has reached the opposite end of the board, promote it to queen.
        if (currentPiece instanceof Pawn &&
                ((Pawn) currentPiece).getPromotionRow() == targetTile.getCoordinateX()) {
            targetTile.setCurrentPiece(new Queen(isWhitesTurn));
        }
        // Make the new piece positions visible.
        redrawBoard();
        // Try to initiate a new move.
        isWhitesTurn = !isWhitesTurn;
        // If the player starts the move in check, declare it.
        if (!kingIsSafe()) {
            declareCheck();
        }
        // If the player has no available moves, end the game.
        if (!hasUsefulMovesLeft()) {
            // If there is no check currently declared, the game ends in a draw.
            if (!blackInCheck && !whiteInCheck) {
                System.out.println("Stalemate!");
                GameInitializer.endGame(0);
            } else if (isWhitesTurn) {
                System.out.println("Checkmate for white!");
                GameInitializer.endGame(1);
            } else {
                System.out.println("Checkmate for black!");
                GameInitializer.endGame(-1);
            }
        }
        activeTile = null;
    }

    public void moveFlagReset(Piece currentPiece) {
        // Whoever was in check, they can no longer be in check after a successful move.
        whiteInCheck = false;
        blackInCheck = false;
        isPieceSelected = false;
        // En passant captures are only allowed for one move,
        // so if the other player missed the chance, clear the flags.
        if (enPassantBlackTarget != null && !isWhitesTurn) {
            enPassantBlackTarget = null;
        }
        if (enPassantWhiteTarget != null && isWhitesTurn) {
            enPassantWhiteTarget = null;
        }
        // If the king has been moved (including castling), then that player can no longer castle.
        if (currentPiece instanceof King) {
            if (isWhitesTurn) {
                canCastleWhLong = false;
                canCastleWhShort = false;
            } else {
                canCastleBlLong = false;
                canCastleBlShort = false;
            }
        }
        // If a rook has been moved (without castling), the player can no longer castle in that rook's direction.
        else if (currentPiece instanceof Rook) {
            if (isWhitesTurn) {
                if (activeTile.getCoordinateY() == 0) {
                    canCastleWhLong = false;
                } else {
                    canCastleWhShort = false;
                }
            } else {
                if (activeTile.getCoordinateY() == 0) {
                    canCastleBlLong = false;
                } else {
                    canCastleBlShort = false;
                }
            }
        }
    }

    // Iterate through the board matrix and sync the corresponding tiles in the swing panel.
    public void redrawBoard() {
        for (Tile[] tile : Board.getChessBoardTiles()) {
            for (Tile current : tile) {
                // TODO: Add understandable comment...
                // Color tiles for move history or active selected tile accordingly.
                if (current.getCoordinateX() == previousActiveTile.getCoordinateX() && current.getCoordinateY() ==
                        previousActiveTile.getCoordinateY() || current.isOccupied() && current.getCoordinateX() ==
                        getTargetTile().getCoordinateX() && current.getCoordinateY() == getTargetTile().getCoordinateY()) {
                    current.updateIcon();
                    current.setTileColor();
                    current.setActiveTileColor();
                } else {
                    current.updateIcon();
                    current.setTileColor();
                }
                current.repaint();
            }
        }
    }

    public void declareCheck() {
        if (isWhitesTurn) {
            whiteInCheck = true;
        } else {
            blackInCheck = true;
        }
        System.out.printf("%s king is in check!\n", isWhitesTurn ? "White's" : "Black's");
    }

    // Walks through the board matrix and tries to move every piece of the current player
    // until it finds a move that doesn't end with them in check or it runs out of moves.
    public boolean hasUsefulMovesLeft() {
        for (Tile[] tileRow : Board.getChessBoardTiles()) {
            for (Tile currentTile : tileRow) {
                if (currentTile.isOccupied() && currentTile.getCurrentPiece().isWhitePiece() == isWhitesTurn) {
                    activeTile = currentTile;
                    Piece currentPiece = activeTile.getCurrentPiece();
                    int activeRow = activeTile.getCoordinateX();
                    int activeCol = activeTile.getCoordinateY();
                    // The algorithm here is the same as the check test, except we start with
                    // the initial figure's position known, and the target tile unknown.
                    // Again, the knights move in a very different way so they are handled separately.
                    boolean KnightSearch = currentPiece instanceof Knight;
                    int searchStart = KnightSearch ? -2 : -1;
                    int searchEnd = KnightSearch ? 2 : 1;
                    for (int rowDiff = searchStart; rowDiff <= searchEnd; rowDiff++) {
                        for (int colDiff = searchStart; colDiff <= searchEnd; colDiff++) {
                            int testRow = activeRow + rowDiff;
                            int testCol = activeCol + colDiff;
                            while (isInBoardBounds(testRow) && isInBoardBounds(testCol)) {
                                targetTile = Board.getChessBoardTiles()[testRow][testCol];
                                if (!targetTile.isOccupied() || targetTile.getCurrentPiece().isWhitePiece != isWhitesTurn) {
                                    if (currentPiece.isLegalMove(activeTile, targetTile)) {
                                        // For every possible move, we perform it silently, test if it results in
                                        // check and reverse it if it does. We don't need to check if the path
                                        // is clear, because we expand the search area one tile at a time.
                                        tryMove(currentPiece);
                                        // If the player can avoid a check, end the search.
                                        if (kingIsSafe()) {
                                            reverseMove(activeTile, targetTile);
                                            return true;
                                        } else {
                                            reverseMove(activeTile, targetTile);
                                        }
                                    }
                                }
                                if (targetTile.isOccupied() || KnightSearch) {
                                    break;
                                }
                                testRow += rowDiff;
                                testCol += colDiff;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void reverseCastlingMove(Tile newRookPosition) {
        // Calculate the old rook position from the new one and roll back both tiles.
        if (isWhitesTurn) {
            if (newRookPosition.getCoordinateY() == 3) {
                Board.getChessBoardTiles()[7][0].recoverPreviousPiece();
            } else {
                Board.getChessBoardTiles()[7][7].recoverPreviousPiece();
            }
        } else {
            if (newRookPosition.getCoordinateY() == 3) {
                Board.getChessBoardTiles()[0][0].recoverPreviousPiece();
            } else {
                Board.getChessBoardTiles()[0][7].recoverPreviousPiece();
            }
        }
        newRookPosition.recoverPreviousPiece();
    }

    public void goBackToSelection() {
        reverseMove(previousActiveTile, previousTargetTile);
        isPieceSelected = true;
        System.err.print("That move would leave your king in check!\n");
    }

    public void reverseMove(Tile oldActive, Tile oldTarget) {
        // Because the old active and target field values changed when we attempted the move
        // use the backup tile indexes to find those tiles again from the board matrix and roll
        // them back to their previous state. Finally assign the old active tile to the field.
        Tile oldActiveBoard = Board.getChessBoardTiles()[oldActive.getCoordinateX()][oldActive.getCoordinateY()];
        oldActiveBoard.recoverPreviousPiece();
        activeTile = oldActiveBoard;
        Tile oldActiveTarget = Board.getChessBoardTiles()[oldTarget.getCoordinateX()][oldTarget.getCoordinateY()];
        oldActiveTarget.recoverPreviousPiece();
        // Some pieces may require further actions to reverse the effect of their moves.
        Piece currentPiece = activeTile.getCurrentPiece();
        if (currentPiece instanceof Pawn) {
            // If we reverse a pawn move that would have opened it to an en passant, reset those flags as well.
            if (isWhitesTurn && enPassantBlackTarget != null) {
                enPassantBlackTarget = null;
            } else if (!isWhitesTurn && enPassantWhiteTarget != null) {
                enPassantWhiteTarget = null;
            }
            // Because an en passant capture changes 3 tiles at once,
            // if we're reversing it, we also need to reverse an extra tile.
            boolean pawnCaptureMove = activeTile.getCoordinateY() == targetTile.getCoordinateY() + 1 ||
                    activeTile.getCoordinateY() == targetTile.getCoordinateY() - 1;
            if (pawnCaptureMove && !targetTile.isOccupied()) {
                Board.getChessBoardTiles()[activeTile.getCoordinateX()][targetTile.getCoordinateY()].recoverPreviousPiece();
            }
        }
        // If we reverse a king's move, restore its field in the board as well.
        if (currentPiece instanceof King) {
            if (isWhitesTurn) {
                GameInitializer.getBoard().whiteKingPosition = activeTile;
            } else {
                GameInitializer.getBoard().blackKingPosition = activeTile;
            }
        }
    }

}
