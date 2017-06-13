package io.github.edwinvanrooij.domain.engine;

import io.github.edwinvanrooij.camelraceshared.domain.Camel;
import io.github.edwinvanrooij.camelraceshared.domain.Game;
import io.github.edwinvanrooij.camelraceshared.domain.GameState;
import io.github.edwinvanrooij.camelraceshared.domain.SideCard;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class ClientEmulator implements Runnable {
    @Override
    public void run() {
        Game engine = new Game();

        printMap(engine.generateGameState());

        try {
            for (int i = 0; i < 100; i++) {
//                engine.nextRound();
                printNewRound(i);
                GameState gameState = engine.generateGameState();
                if (gameState.isGameEnded()) {
                    printWinner(gameState.getWinner());
                    printMap(gameState);
                    break;
                } else {
                    printMap(gameState);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printWinner(Camel camel) {
        System.out.println("===================================");
        System.out.println(String.format("============ We have a winner! Camel %s! ============", camel));
        System.out.println("===================================");
    }

    private static void printNewRound(int round) {
        System.out.println();
        System.out.println();
        System.out.println("===================================");
        System.out.println(String.format("============ ROUND %s  ============", ++round));
        System.out.println("===================================");
        System.out.println();
        System.out.println();
    }

    private static void printMap(GameState gameState) {
        printSpace();

        for (SideCard c : gameState.getSideCardList()) {
            System.out.println(c);
        }

        printSpace();

        for (Camel c : gameState.getCamelList()) {
            System.out.println(c);
        }

        printSpace();

//        for (Card c : gameState.getDeck()) {
//            System.out.println(c);
//        }

        printSpace();

        if (gameState.getLastPickedCard() != null) {
            System.out.format("Last picked card: %s\n", gameState.getLastPickedCard());
        } else {
            System.out.println("Game just started, no last picked card yet.");
        }
    }

    private static void printSpace() {
        for (int i = 0; i < 1; i++) {
            System.out.println();
        }
    }
}
