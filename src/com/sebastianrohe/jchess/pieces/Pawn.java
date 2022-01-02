package com.sebastianrohe.jchess.pieces;

import com.sebastianrohe.jchess.move.Move;
import com.sebastianrohe.jchess.board.tiles.Tile;

import javax.swing.ImageIcon;

/**
 * Class to represent a pawn piece on the board.
 * <p>
 * Pawns are special as their allowed movement directions depends on the color,
 * they can jump two spaces only from their starting position and they have a
 * promotional ability if they reach the opposite end of the board.
 */
public class Pawn extends Piece {

    private final float movingDirection;
    private final int startingRow;
    private final int promotionRow;

    public Pawn(boolean isWhitePiece) {
        this.isWhitePiece = isWhitePiece;
        this.movingDirection = isWhitePiece ? -1 : 1;
        this.startingRow = isWhitePiece ? 6 : 1;
        this.promotionRow = isWhitePiece ? 0 : 7;
    }

    public float getMovingDirection() {
        return this.movingDirection;
    }

    public int getStartingRow() {
        return this.startingRow;
    }

    public int getPromotionRow() {
        return this.promotionRow;
    }

    /**
     * Checks if move is legal. Because the various checks here require more than just
     * the coordinates, this method has been effectively merged with checkCoordinates.
     *
     * @param activeTile Current active tile.
     * @param targetTile Current target tile.
     * @return Boolean Returns true if move is legal.
     */
    @Override
    public boolean isLegalMove(Tile activeTile, Tile targetTile) {
        this.activeX = activeTile.getCoordinateX();
        this.targetX = targetTile.getCoordinateX();
        this.activeY = activeTile.getCoordinateY();
        this.targetY = targetTile.getCoordinateY();
        // Make sure the pawn only goes forward, never back.
        if (calculateDirectionCoordinate(targetX, activeX) != getMovingDirection()) {
            return false;
        }
        if (getActiveX() == getStartingRow() && isJumpMove() && !targetTile.isOccupied()) {
            return true;
        } else return (isAttackMove() && targetTile.isOccupied()) || (isEnPassantMove(targetTile) && isAttackMove()) ||
                (isRegularMove() && !targetTile.isOccupied());
    }

    public boolean isAttackMove() {
        return (getActiveY() == getTargetY() + 1 || getActiveY() == getTargetY() - 1) && getActiveX() + getMovingDirection() == getTargetX();
    }

    public boolean isRegularMove() {
        return getActiveY() == getTargetY() && getActiveX() + getMovingDirection() == getTargetX();
    }

    public boolean isJumpMove() {
        return getActiveY() == getTargetY() && getActiveX() + 2 * getMovingDirection() == getTargetX();
    }

    public boolean isEnPassantMove(Tile targetTile) {
        if (isWhitePiece()) {
            return Move.getEnPassantWhiteTarget() == targetTile;
        } else {
            return Move.getEnPassantBlackTarget() == targetTile;
        }
    }

    /**
     * This method is never called for this class.
     */
    @Override
    public boolean checkCoordinatesForMove() {
        return false;
    }

    @Override
    public ImageIcon getPieceIcon() {
        if (isWhitePiece()) {
            return PieceType.WHITE_PAWN.getImageIcon();
        } else {
            return PieceType.BLACK_PAWN.getImageIcon();
        }
    }

}
