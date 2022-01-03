package com.sebastianrohe.jchess.pieces;

import javax.swing.*;
import java.net.URL;

/**
 * Manage different piece types.
 */
public enum PieceType {

    BLACK_PAWN {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/bp.png"));
        }
    },

    BLACK_ROOK {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/br.png"));
        }
    },

    BLACK_KNIGHT {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/bn.png"));
        }
    },

    BLACK_BISHOP {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/bb.png"));
        }
    },

    BLACK_KING {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/bk.png"));
        }
    },

    BLACK_QUEEN {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/bq.png"));
        }
    },

    WHITE_PAWN {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/wp.png"));
        }
    },

    WHITE_ROOK {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/wr.png"));
        }
    },

    WHITE_KNIGHT {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/wn.png"));
        }
    },

    WHITE_BISHOP {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/wb.png"));
        }
    },

    WHITE_KING {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/wk.png"));
        }
    },

    WHITE_QUEEN {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon(getClass().getResource("/images/wq.png"));
        }
    };

    // Get piece image icon for board representation.
    public abstract ImageIcon getImageIcon();

}
