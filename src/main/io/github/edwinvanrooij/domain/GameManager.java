package io.github.edwinvanrooij.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by eddy on 6/8/17.
 */
public class GameManager {
    private List<Game> games;

    public GameManager() {
        games = new ArrayList<>();
    }
    public Game createGame() {
        String id = generateUniqueId();
        Game game = new Game(id);
        games.add(game);
        return game;
    }

    private String generateUniqueId() {
        String id;
        do {
            id = generateId();
        } while (gameWithIdExists(id));

        return id;
    }

    private void addPlayerToGame() {

    }

    private String generateId() {
        char[] vowels = "aeiouy".toCharArray(); // klinkers
        char[] consonants = "bcdfghjklmnpqrstvwxz".toCharArray(); // medeklinkers

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            char c;
            if (i % 2 == 1) {
                c = vowels[random.nextInt(vowels.length)];
            } else {
                c = consonants[random.nextInt(consonants.length)];
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private boolean gameWithIdExists(String id) {
        for (Game g : games) {
            if (Objects.equals(g.getId(), id)) {
                return true;
            }
        }
        return false;
    }
}
