package com.sebastianrohe.jchess.settings;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Class to represent different result panels at game end.
 */
public class GamePanel extends JComponent implements MouseListener {

    private static BufferedImage image;

    public GamePanel(int opCode) {
        addMouseListener(this);
        String filepath = "";
        if (opCode == -1) {
            filepath = GamePanelType.WHITE_WINS.getFilePath();
        } else if (opCode == 0) {
            filepath = GamePanelType.DRAW.getFilePath();
        } else if (opCode == 1) {
            filepath = GamePanelType.BLACK_WINS.getFilePath();
        }
        try {
            image = ImageIO.read(new File(filepath));
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    // Paint the image in the middle of the board.
    protected void paintComponent(Graphics graphics) {
        graphics.drawImage(image, (GameInitializer.board.getWidth() - image.getWidth()) / 2,
                (GameInitializer.board.getHeight() - image.getHeight()) / 2, image.getWidth(),
                image.getHeight(), null);
    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        GameInitializer.close();
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
    }

}
