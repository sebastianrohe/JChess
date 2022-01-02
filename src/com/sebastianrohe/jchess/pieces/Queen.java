package com.sebastianrohe.jchess.pieces;

import javax.swing.ImageIcon;

/**
 * Class to represent a queen piece on the board.
 */
public class Queen extends Piece {

    public Queen(boolean pieceIsWhite) {
        this.isWhitePiece = pieceIsWhite;
    }

    /**
     * Check moving coordinates. Combines the movement abilities of rook and bishop.
     *
     * @return Boolean Returns true if coordinates are valid.
     */
    @Override
    public boolean checkCoordinatesForMove() {
        boolean isStraightMove = getActiveX() == getTargetX() || getActiveY() == getTargetY();
        boolean isDiagonalMove = Math.abs(targetX - activeX) == Math.abs(targetY - activeY);
        return isStraightMove || isDiagonalMove;
    }

    @Override
    public ImageIcon getPieceIcon() {
        if (isWhitePiece()) {
            return PieceType.WHITE_QUEEN.getImageIcon();
        } else {
            return PieceType.BLACK_QUEEN.getImageIcon();
        }
    }

}
