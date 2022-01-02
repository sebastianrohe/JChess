package com.sebastianrohe.jchess.pieces;

import javax.swing.ImageIcon;

/**
 * Class to represent a bishop piece on the board.
 */
public class Bishop extends Piece {

    public Bishop(boolean pieceIsWhite) {
        this.isWhitePiece = pieceIsWhite;
    }

    /**
     * Check move coordinates. Bishops can move only diagonally.
     *
     * @return Boolean Returns true if move is valid.
     */
    @Override
    public boolean checkCoordinatesForMove() {
        return Math.abs(targetX - activeX) == Math.abs(targetY - activeY);
    }

    @Override
    public ImageIcon getPieceIcon() {
        if (isWhitePiece()) {
            return PieceType.WHITE_BISHOP.getImageIcon();
        } else {
            return PieceType.BLACK_BISHOP.getImageIcon();
        }
    }

}
