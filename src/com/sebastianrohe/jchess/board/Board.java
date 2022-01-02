package com.sebastianrohe.jchess.board;

import com.sebastianrohe.jchess.board.tiles.Tile;
import com.sebastianrohe.jchess.move.MoveListener;
import com.sebastianrohe.jchess.pieces.*;

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Class combines the internal representation of the board - a matrix of
 * tiles, with the display components.
 */
public class Board extends JPanel {

    private final static Tile[][] CHESS_BOARD_TILES = new Tile[8][8];
    public Tile whiteKingPosition;
    public Tile blackKingPosition;

    /**
     * Constructor. Creates chess board.
     */
    public Board(LayoutManager layoutManager) {
        super(layoutManager);
        boolean isLightTile = true;
        boolean isWhitePiece = false;
        // Prepare own listener to intercept clicks on the board tiles.
        MoveListener moveListener = new MoveListener();
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                // Generate an empty tile with the required data.
                Tile chessBoardTile = new Tile(row, column, isLightTile);
                // Add it to internal matrix.
                CHESS_BOARD_TILES[row][column] = chessBoardTile;
                // Fill in everything that's not a pawn.
                if (row == 0 || row == 7) {
                    setPiecesOnRow(column, chessBoardTile, isWhitePiece);
                }
                // Fill in the pawns.
                else if (row == 1 || row == 6) {
                    chessBoardTile.setCurrentPiece(new Pawn(isWhitePiece));
                    chessBoardTile.updateIcon();
                    // At this point we're done with the black pieces.
                    if (column == 7) {
                        isWhitePiece = true;
                    }
                }
                // Attach the listener to the new tile, and add it to the swing panel, so it can be drawn.
                chessBoardTile.addActionListener(moveListener);
                this.add(chessBoardTile);
                // To get a chess board pattern don't change the tile color after the end of each row.
                if (column != 7) {
                    isLightTile = !isLightTile;
                }
            }
        }
        // Keep track of the king positions.
        this.whiteKingPosition = CHESS_BOARD_TILES[7][4];
        this.blackKingPosition = CHESS_BOARD_TILES[0][4];
    }

    public static Tile[][] getChessBoardTiles() {
        return CHESS_BOARD_TILES;
    }

    /**
     * Set piece in row on board.
     *
     * @param column         Column number.
     * @param chessBoardTile Tile on the chessboard.
     * @param isWhitePiece   Boolean to check if piece is white.
     */
    private void setPiecesOnRow(int column, Tile chessBoardTile, boolean isWhitePiece) {
        if (column == 0 || column == 7) {
            chessBoardTile.setCurrentPiece(new Rook(isWhitePiece));
        } else if (column == 1 || column == 6) {
            chessBoardTile.setCurrentPiece(new Knight(isWhitePiece));
        } else if (column == 2 || column == 5) {
            chessBoardTile.setCurrentPiece(new Bishop(isWhitePiece));
        } else if (column == 3) {
            chessBoardTile.setCurrentPiece(new Queen(isWhitePiece));
        } else if (column == 4) {
            chessBoardTile.setCurrentPiece(new King(isWhitePiece));
        }
        chessBoardTile.updateIcon();
    }

}