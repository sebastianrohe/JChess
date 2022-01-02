package com.sebastianrohe.jchess.move;

import com.sebastianrohe.jchess.board.tiles.Tile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class to detect when a board tile is clicked to react accordingly.
 */
public class MoveListener extends Move implements ActionListener {

    /**
     * Detects when a board tile is clicked and reacts accordingly.
     *
     * @param actionEvent Specific triggered event.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        targetTile = (Tile) actionEvent.getSource();
        // If there was no tile or piece previously selected.
        if (getActiveTile() == null && !isPieceSelected()) {
            // Check if the selected tile is occupied.
            if (getTargetTile().isOccupied()) {
                // Check if the occupying piece is the right for the current player,
                // and if so select it for moving and indicate it with active color.
                if (getTargetTile().getCurrentPiece().isWhitePiece() == isWhitesTurn()) {
                    activeTile = getTargetTile();
                    getActiveTile().setActiveTileColor();
                    // TODO: Add color indicator on tiles to show possible legal moves pieces.
                    getActiveTile().setActiveTileColor();
                } else {
                    System.err.printf("It's %s turn now!\n", isWhitesTurn() ? "white's" : "black's");
                }
            } else {
                System.err.print("Select a piece first!\n");
            }
        }
        // If a piece was previously selected, and the player clicks, check what they are trying to do.
        else if (getActiveTile() != getTargetTile()) {
            // If they selected another one of their own pieces, make this one the active one, remove the old color and draw a new one.
            if (getTargetTile().isOccupied() && getActiveTile().getCurrentPiece().isWhitePiece() ==
                    getTargetTile().getCurrentPiece().isWhitePiece()) {
                getActiveTile().setTileColor();
                getTargetTile().setActiveTileColor();
                activeTile = getTargetTile();
            }
            // If they selected an enemy piece or an empty tile, try to calculate and execute move.
            else {
                calculateMove();
            }
        }
    }

}
