package com.sebastianrohe.jchess.settings;

/**
 * Manage game panel types.
 */
public enum GamePanelType {

    WHITE_WINS {
        @Override
        public String getFilePath() {
            return "images/white_wins.png";
        }
    },

    BLACK_WINS {
        @Override
        public String getFilePath() {
            return "images/black_wins.png";
        }
    },

    DRAW {
        @Override
        public String getFilePath() {
            return "images/draw.png";
        }
    };

    // Get path to file.
    public abstract String getFilePath();

}
