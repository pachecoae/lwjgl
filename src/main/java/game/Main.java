package game;

import engine.GameEngine;
import engine.IGameLogic;

/**
 * Our starting point. Contains the main method and will only create a GameEngine instance and start it.
 */
public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            gameEng.start();
        }
        catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }

}
