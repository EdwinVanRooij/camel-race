package io.github.edwinvanrooij.domain;


import io.github.edwinvanrooij.domain.camelrace.CamelRaceGameState;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eddy
 * on 6/5/17.
 */
public abstract class Game {

    private static AtomicInteger nextId = new AtomicInteger();

    private String id;
    private transient HashMap<Integer, Boolean> readyMap; // player ID with ready map
    private transient HashMap<Integer, Boolean> playAgainMap; // players who want to play again, true if they do
    private transient HashMap<Player, Long> aliveCheckMap; // player with last alive check timestamp

    protected List<Player> players;

    public String getId() {
        return id;
    }

    public synchronized List<Player> getPlayers() {
        return players;
    }

    public Game(String id) throws Exception {
        this.id = id;
        initVariables();
        setInitialState();

        int interval = 5; // in seconds
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    startAliveChecker();
                } catch (InterruptedException | NullPointerException e ) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 5000, interval * 1000);
    }

    /**
     * This will delete the inactive players which are still in the game
     *
     * @throws InterruptedException
     */
    private void startAliveChecker() throws InterruptedException {
        List<Player> playersToRemove = new ArrayList<>();
        for (Player player : getPlayers()) {

            if (aliveCheckMap.get(player) == null) {
                System.out.println(String.format("Player %s does not exist in aliveCheckMap.", player));
                return;
            }
            long lastCheck = aliveCheckMap.get(player);
            long currentTime = System.nanoTime();
            long elapsedTimeInSeconds = (currentTime - lastCheck) / 1000000000;
            if (elapsedTimeInSeconds > 4) {
                playersToRemove.add(player);
            }
        }
        players.removeAll(playersToRemove);
    }

    public void playerAliveCheck(Player player) {
        aliveCheckMap.put(player, System.nanoTime());
    }

    public boolean everyoneIsReady() {
        try {
            for (Player player : players) {
                try {
                    // Check if every player has a positive entry in the ready map
                    if (!readyMap.get(player.getId())) {
                        // Player is not ready
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Player was probably not once added to the map, meaning it's never been at true
            return false;
        }
        // Only if all checks were passed, everyone is really ready
        return true;
    }
    public void ready(Player player, boolean ready) {
        readyMap.put(player.getId(), ready);
    }

    public Player addPlayer(Player player) {
        int uniqueId = nextId.incrementAndGet();

        Player playerWithId = new Player(uniqueId, player.getName());

        players.add(playerWithId);
        return playerWithId;
    }

    public Player getPlayer(int id) throws Exception {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        throw new Exception("No player found with ID " + id);
    }

    public void restart() throws Exception {
        readyMap.clear();
        playAgainMap.clear();

        setInitialState();
    }

    public void playAgain(int playerId, boolean playAgain) {
        playAgainMap.put(playerId, playAgain);
    }

    /**
     * Checks if all players have signed up to play again.
     *
     * @return true if they all wanna play again
     */
    public boolean allPlayAgain() {
        // Check for every player if the play again is at true
        for (Player player : getPlayers()) {
            // If the player is not in the map, or the value is not at true, not everyone wants to play again
            try {
                if (!playAgainMap.get(player.getId())) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    protected void initVariables() {
        players = new ArrayList<>();
        readyMap = new HashMap<>();
        playAgainMap = new HashMap<>();
        aliveCheckMap = new HashMap<>();

    }

    protected abstract void setInitialState() throws Exception;

    public abstract Object generateGameState();

    public abstract Object generateGameResults() throws Exception;
}
