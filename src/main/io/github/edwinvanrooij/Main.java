package io.github.edwinvanrooij;

public class Main {

    public static void main(String[] args) {
        CamelRaceEngine engine = new CamelRaceEngine();

        GameState state = engine.generateGameState();
    }
}
