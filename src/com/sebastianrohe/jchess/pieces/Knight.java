package com.sebastianrohe.jchess.pieces;

import com.sebastianrohe.jchess.board.tiles.Tile;

import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Class to represent a knight piece on the board.
 */
public class Knight extends Piece {

    public Knight(boolean pieceIsWhite) {
        this.isWhitePiece = pieceIsWhite;
    }

    /**
     * Check moving coordinates. Knights move in one of 8 L-shaped path, all of witch can be
     * generalized to one tile horizontally, two tiles vertically or vice-versa.
     *
     * @return Boolean Returns true if coordinates are valid.
     */
    @Override
    public boolean checkCoordinatesForMove() {
        boolean isOneTwoMove = Math.abs(targetX - activeX) == 1 && Math.abs(targetY - activeY) == 2;
        boolean isTwoOneMove = Math.abs(targetX - activeX) == 2 && Math.abs(targetY - activeY) == 1;
        return isOneTwoMove || isTwoOneMove;
    }

    /**
     * Get moving path. Because a knight's path can't be blocked, mapping and checking it
     * is pointless, so this method is overridden to return an empty list.
     *
     * @param active Current active tile.
     * @param target Current target tile.
     * @return ArrayList<> Empty list.
     */
    @Override
    public ArrayList<Integer> getMovingPath(Tile active, Tile target) {
        return new ArrayList<>();
    }

    @Override
    public ImageIcon getPieceIcon() {
        if (isWhitePiece()) {
            return PieceType.WHITE_KNIGHT.getImageIcon();
        } else {
            return PieceType.BLACK_KNIGHT.getImageIcon();
        }
    }

}
