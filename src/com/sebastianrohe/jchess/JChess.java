package com.sebastianrohe.jchess;

import com.sebastianrohe.jchess.settings.GameInitializer;

/**
 * Class to manage program routine.
 */
public class JChess {

    public static void main(String[] args) {
        GameInitializer gameInitializer = new GameInitializer();
        gameInitializer.setUpGameFrame();
        gameInitializer.setUpGamePanel();
    }

}
