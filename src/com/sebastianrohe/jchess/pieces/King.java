package com.sebastianrohe.jchess.pieces;

import com.sebastianrohe.jchess.move.Move;

import javax.swing.ImageIcon;

/**
 * Class to represent a king piece on the board.
 */
public class King extends Piece {

    public King(boolean pieceIsWhite) {
        this.isWhitePiece = pieceIsWhite;
    }

    /**
     * Check moving coordinates. King can perform regular move to adjacent tile or
     * castling move according to current game situation.
     *
     * @return Boolean Returns true if coordinates are valid.
     */
    @Override
    public boolean checkCoordinatesForMove() {
        // Regular move.
        if ((Math.abs(targetX - activeX) <= 1) &&
                (Math.abs(targetY - activeY) <= 1)) {
            return true;
        }
        // Castling - only permitted if a proper flag is still set to true.
        else if (getTargetX() == getActiveX() && Math.abs(targetY - activeY) == 2) {
            // Short castling (right).
            if (getTargetY() - getActiveY() == 2) {
                if (isWhitePiece() && Move.canCastleWhShort()) {
                    return true;
                } else if (!Move.canCastleBlShort()) {
                    return true;
                }
            }
            // Long castling (left).
            else if (getTargetY() - getActiveY() == -2) {
                if (Move.canCastleWhLong()) {
                    return true;
                } else if (!isWhitePiece() & Move.canCastleBlLong()) {
                    return true;
                }
            }
            System.out.println("Can't castle if you've already moved either the king or rook!");
        }
        return false;
    }

    @Override
    public ImageIcon getPieceIcon() {
        if (isWhitePiece()) {
            return PieceType.WHITE_KING.getImageIcon();
        } else {
            return PieceType.BLACK_KING.getImageIcon();
        }
    }

}
