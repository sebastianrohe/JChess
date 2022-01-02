package com.sebastianrohe.jchess.pieces;

import javax.swing.ImageIcon;

/**
 * Class to represent a rook piece on the board.
 */
public class Rook extends Piece {

    public Rook(boolean pieceIsWhite) {
        this.isWhitePiece = pieceIsWhite;
    }

    /**
     * Check moving coordinates. Rook can only move on the same row or column.
     *
     * @return Boolean Returns true if coordinates are valid.
     */
    @Override
    public boolean checkCoordinatesForMove() {
        return getActiveX() == getTargetX() || getActiveY() == getTargetY();
    }

    @Override
    public ImageIcon getPieceIcon() {
        if (isWhitePiece()) {
            return PieceType.WHITE_ROOK.getImageIcon();
        } else {
            return PieceType.BLACK_ROOK.getImageIcon();
        }
    }

}
