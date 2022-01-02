package com.sebastianrohe.jchess.pieces;

import javax.swing.*;

/**
 * Manage different piece types.
 */
public enum PieceType {

    BLACK_PAWN {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/bp.png");
        }
    },

    BLACK_ROOK {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/br.png");
        }
    },

    BLACK_KNIGHT {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/bn.png");
        }
    },

    BLACK_BISHOP {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/bb.png");
        }
    },

    BLACK_KING {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/bk.png");
        }
    },

    BLACK_QUEEN {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/bq.png");
        }
    },

    WHITE_PAWN {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/wp.png");
        }
    },

    WHITE_ROOK {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/wr.png");
        }
    },

    WHITE_KNIGHT {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/wn.png");
        }
    },

    WHITE_BISHOP {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/wb.png");
        }
    },

    WHITE_KING {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/wk.png");
        }
    },

    WHITE_QUEEN {
        @Override
        public ImageIcon getImageIcon() {
            return new ImageIcon("images/wq.png");
        }
    };

    // Get piece image icon for board representation.
    public abstract ImageIcon getImageIcon();

}
