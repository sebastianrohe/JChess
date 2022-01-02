package com.sebastianrohe.jchess.pieces;

import com.sebastianrohe.jchess.board.tiles.Tile;

import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Class to represent a chess piece.
 */
public abstract class Piece {

    protected int activeX;
    protected int activeY;
    protected int targetX;
    protected int targetY;
    protected int directionX;
    protected int directionY;
    public boolean isWhitePiece;

    public int getActiveX() {
        return this.activeX;
    }

    public int getActiveY() {
        return this.activeY;
    }

    public int getTargetX() {
        return this.targetX;
    }

    public int getTargetY() {
        return this.targetY;
    }

    public int getDirectionX() {
        return this.directionX;
    }

    public int getDirectionY() {
        return this.directionY;
    }

    public boolean isWhitePiece() {
        return this.isWhitePiece;
    }

    public ArrayList<Integer> getMovingPath(Tile active, Tile target) {
        ArrayList<Integer> movingPath = new ArrayList<>();
        this.directionX = calculateDirectionCoordinate(targetX, activeX);
        this.directionY = calculateDirectionCoordinate(targetY, activeY);
        this.activeX += getDirectionX();
        this.activeY += getDirectionY();
        while (true) {
            if (getActiveX() == getTargetX() && getActiveY() == getTargetY()) {
                break;
            } else {
                movingPath.add(getActiveX());
                movingPath.add(getActiveY());
                this.activeX += getDirectionX();
                this.activeY += getDirectionY();
            }
        }
        return movingPath;
    }

    public int calculateDirectionCoordinate(int targetCoordinate, int activeCoordinate) {
        int differenceOfCoordinates = activeCoordinate - targetCoordinate;
        return Integer.compare(0, differenceOfCoordinates);
    }

    public boolean isLegalMove(Tile active, Tile target) {
        this.activeX = active.getCoordinateX();
        this.activeY = active.getCoordinateY();
        this.targetX = target.getCoordinateX();
        this.targetY = target.getCoordinateY();
        return checkCoordinatesForMove();
    }

    public abstract boolean checkCoordinatesForMove();

    public abstract ImageIcon getPieceIcon();

}
