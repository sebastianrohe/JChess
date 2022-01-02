package com.sebastianrohe.jchess.board.tiles;

import com.sebastianrohe.jchess.pieces.Piece;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Class to represent tile on the board.
 */
public class Tile extends JButton {

    private final int coordinateX;
    private final int coordinateY;
    private final boolean hasLightColor;
    private Piece currentPiece;
    private Piece previousPiece;
    private ImageIcon pieceImageIcon;

    /**
     * Constructor. Initializes configuration and coloring of tile.
     *
     * @param coordinateX   int x coordinate of tile.
     * @param coordinateY   int y coordinate of tile.
     * @param hasLightColor Boolean to check if tile is light or not.
     */
    public Tile(int coordinateX, int coordinateY, boolean hasLightColor) {
        super();
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.hasLightColor = hasLightColor;
        this.currentPiece = null;
        this.previousPiece = null;
        this.pieceImageIcon = null;
        this.configureTile();
    }

    // Get x coordinate.
    public int getCoordinateX() {
        return this.coordinateX;
    }

    // Get y coordinate.
    public int getCoordinateY() {
        return this.coordinateY;
    }

    // Determines if tile is light or not.
    public boolean hasLightColor() {
        return this.hasLightColor;
    }

    // Get current piece on tile.
    public Piece getCurrentPiece() {
        return this.currentPiece;
    }

    /**
     * Sets new current chess piece on the tile.
     * The previous current piece is converted to the new previous piece.
     *
     * @param newCurrentPiece Chess piece which will be the new occupying piece on the tile.
     */
    public void setCurrentPiece(Piece newCurrentPiece) {
        this.previousPiece = this.currentPiece;
        this.currentPiece = newCurrentPiece;
    }

    // Get the piece which was previously occupying this tile.
    public Piece getPreviousPiece() {
        return this.previousPiece;
    }

    // Returns true if tile is occupied by a piece.
    public boolean isOccupied() {
        return this.currentPiece != null;
    }

    // Sets color of active tile for active move and move previous move history.
    public void setActiveTileColor() {
        Color activeSquareColor;
        if (this.hasLightColor) {
            activeSquareColor = TileType.LIGHT_ACTIVE_TILE.getTileColor();
        } else {
            activeSquareColor = TileType.DARK_ACTIVE_TILE.getTileColor();
        }
        this.setBackground(activeSquareColor);
    }

    // Sets colors of tiles on the board.
    public void setTileColor() {
        if (!this.hasLightColor) {
            Color darkSquareColor = TileType.DARK_BOARD_TILE.getTileColor();
            this.setBackground(darkSquareColor);
        } else {
            Color lightSquareColor = TileType.LIGHT_BOARD_TILE.getTileColor();
            this.setBackground(lightSquareColor);
        }
    }

    // Sets the size of each tile and executes further configurations.
    public void configureTile() {
        Dimension tileDimension = TileType.GENERAL_BOARD_TILE.getTileSize();
        this.setPreferredSize(tileDimension);
        this.setFocusPainted(false);
        this.setBorder(null);
        this.setTileColor();
    }

    // Performs a rollback and restores the previous current piece.
    public void recoverPreviousPiece() {
        this.currentPiece = this.previousPiece;
    }

    // Removes current piece from the tile and stores it as previous piece.
    public void clearTile() {
        this.previousPiece = getCurrentPiece();
        this.currentPiece = null;
    }

    // Update the icon on the tile if there is a chess piece on it.
    public void updateIcon() {
        if (this.currentPiece != null) {
            this.pieceImageIcon = getCurrentPiece().getPieceIcon();
        } else {
            this.pieceImageIcon = null;
        }
    }

    // Get image icon to represent pieces on the chess board.
    @Override
    public ImageIcon getIcon() {
        return this.pieceImageIcon;
    }

}