package io.github.edwinvanrooij;

public class Main {

    public static void main(String[] args) {
        CamelRaceEngine engine = new CamelRaceEngine();

        printMap(engine.generateGameState());

        try {
            for (int i = 0; i < 10; i++) {
                engine.nextRound();
                printNewRound(i);
                printMap(engine.generateGameState());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printNewRound(int round) {
        System.out.println();
        System.out.println();
        System.out.println("===================================");
        System.out.println(String.format("============ ROUND %s  ============",++ round));
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
