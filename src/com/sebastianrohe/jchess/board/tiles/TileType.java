package com.sebastianrohe.jchess.board.tiles;

import java.awt.*;

/**
 * Manage different tile types.
 */
public enum TileType {

    LIGHT_ACTIVE_TILE {
        @Override
        public Color getTileColor() {
            return new Color(246, 246, 105);
        }

        @Override
        public Dimension getTileSize() {
            return null;
        }
    },

    DARK_ACTIVE_TILE {
        @Override
        public Color getTileColor() {
            return new Color(186, 202, 43);
        }

        @Override
        public Dimension getTileSize() {
            return null;
        }
    },

    LIGHT_BOARD_TILE {
        @Override
        public Color getTileColor() {
            return new Color(238, 238, 212);
        }

        @Override
        public Dimension getTileSize() {
            return null;
        }
    },

    DARK_BOARD_TILE {
        @Override
        public Color getTileColor() {
            return new Color(118, 150, 85);
        }

        @Override
        public Dimension getTileSize() {
            return null;
        }
    },

    GENERAL_BOARD_TILE {
        @Override
        public Color getTileColor() {
            return null;
        }

        @Override
        public Dimension getTileSize() {
            return new Dimension(85, 85);
        }
    };

    // Get color of specific tile type.
    public abstract Color getTileColor();

    // Get dimension of tile type.
    public abstract Dimension getTileSize();

}
