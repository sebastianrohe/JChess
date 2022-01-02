package com.sebastianrohe.jchess.settings;

import com.sebastianrohe.jchess.board.Board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * Class to manage game frame and further configurations.
 */
public class GameInitializer implements ActionListener {

    public static Board board;
    protected static GamePanel gamePanel;
    private static JPanel contentPane;
    private static JButton resetButton;
    private static JFrame frame;

    public static Board getBoard() {
        return board;
    }

    public static JButton getResetButton() {
        return resetButton;
    }

    /**
     * Set up game panel.
     */
    public void setUpGamePanel() {
        // Create a reset button and put it below the board.
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        contentPane.add(panel, BorderLayout.SOUTH);
        resetButton = new JButton("Reset Board");
        resetButton.addActionListener(this);
        panel.add(resetButton);
        // Draw everything in the middle of the screen.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Set up game frame.
     */
    public void setUpGameFrame() {
        // Create top level swing container.
        frame = new JFrame("JChess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setResizable(false);
        // Create a wrapper panel and put it in the window container.
        contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        frame.setContentPane(contentPane);
        // Create the chess board using a GridLayout.
        board = new Board(new GridLayout(8, 8));
        contentPane.add(board, BorderLayout.CENTER);
    }

    // End game.
    public static void endGame(int opCode) {
        gamePanel = new GamePanel(opCode);
        frame.setGlassPane(gamePanel);
        gamePanel.setVisible(true);
    }

    public static void close() {
        frame.dispose();
    }

    // This removes the current game board and starts over with a new one.
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == getResetButton()) {
            contentPane.remove(board);
            board = new Board(new GridLayout(8, 8));
            contentPane.add(board, BorderLayout.CENTER);
            board.revalidate();
            board.repaint();
        }
    }

}