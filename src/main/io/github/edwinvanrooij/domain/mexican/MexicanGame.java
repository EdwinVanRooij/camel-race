package io.github.edwinvanrooij.domain.mexican;

import io.github.edwinvanrooij.domain.Game;
import io.github.edwinvanrooij.domain.Player;

import java.util.*;

/**
 * Created by eddy
 * on 7/27/17.
 */

public class MexicanGame extends Game {

    private Map<Integer, Throw> playerThrowMap; // (player id, throw)
    private Map<Integer, Integer> playersThrowsLeftMap; // (player id, maxTurns)
    private Map<Integer, Integer> playersMaxThrowsMap; // (player id, turnsLeft)
    private Map<Integer, GameMode> playerVoteMap; // (player id, gameModeVoted)

    private int firstPlayerId;

    private DiceThrower thrower;
    private GameMode gameMode;
    private int maxThrows;
    private int stake;

    private List<Integer> playerOrder; // player IDs
    private int playerOrderTurn;

    public MexicanGame(String id) throws Exception {
        super(id);
    }

    @Override
    protected void setInitialState() throws Exception {
        thrower = new DiceThrower();
        playerOrderTurn = 0;
        playerThrowMap = new HashMap<>();
        playersMaxThrowsMap = new HashMap<>();
        playersThrowsLeftMap = new HashMap<>();
        playerVoteMap = new HashMap<>();
        firstPlayerId = 0;
    }

    public void newVote(int playerId, int enumOrdinal) throws Exception {
        playerVoteMap.put(playerId, GameMode.values()[enumOrdinal]);
        if (everyoneVoted()) {
            setGameMode();
            generatePlayerOrder();
        }
    }

    public boolean everyoneVoted() {
        // Check if any player has NOT yet voted. If that's the case, not everybody voted.
        for (Player p : players) {
            if (playerVoteMap.get(p.getId()) == null) {
                System.out.println(String.format("%s with id %s didn't vote yet", p, p.getId()));
                return false;
            }
        }
        return true;
    }

    public Throw newPlayerThrow(Player p) throws Exception {
        int throwsLeft = playersThrowsLeftMap.get(p.getId());
        if (throwsLeft <= 0) {
            throw new Exception("Player is out of throws! Should not have been able to get this far, turn should be passed on to next person before this happens.");
        }
        playersThrowsLeftMap.put(p.getId(), throwsLeft - 1);
        Throw th = thrower.generateThrow();
        playerThrowMap.put(p.getId(), th);

        // Execute code for special throws
        switch (th.getScore()) {
            case 21:
                stake = stake * 2;
                break;
            case 31:
                // Reset throws to amount before this turn, no turn should be used on 31.
                playersThrowsLeftMap.put(p.getId(), throwsLeft);
                // todo; drieman
                break;
            case 11:
                // todo; honderdman
                break;
        }

        // Check if the first person is done throwing
        if (p.getId() == firstPlayerId && throwsLeft - 1 == 0) {
            onFirstPlayerDoneThrowing();
        }
        return th;
    }

    public boolean everyoneIsDone() {
        return playerOrderTurn == playerOrder.size();
    }

    public Player getCurrentPlayerInTurn() throws Exception {
        return getPlayer(playerOrder.get(playerOrderTurn -1));
    }
    public boolean playerHasThrowsLeft(Player p) {
        return playersThrowsLeftMap.get(p.getId()) > 0;
    }

    public void playerStops(Player p) throws Exception {
        if (p.getId() == firstPlayerId) {
            onFirstPlayerDoneThrowing();
        }
    }

    public void setGameMode(GameMode mode) throws Exception {
        gameMode = mode;
        setModeVariables(mode);
    }

    private void setGameMode() throws Exception {
        int normalVotes = 0;
        int hardcoreVotes = 0;

        // Collect the votes
        for (Integer name : playerVoteMap.keySet()) {
            GameMode value = playerVoteMap.get(name);

            if (value == GameMode.NORMAL) {
                normalVotes++;
            } else if (value == GameMode.HARDCORE) {
                hardcoreVotes++;
            } else {
                throw new Exception("Could not determine what gamemode was voted for.");
            }
        }

        // Sanity check
        if (normalVotes == 0 && hardcoreVotes == 0) {
            throw new Exception("Something went wrong while counting the votes. Received zero of both.");
        }

        // It's a reasonably fair vote system. Most votes wins.
        if (normalVotes > hardcoreVotes) {
            // Normal wins
            gameMode = GameMode.NORMAL;
        } else if (hardcoreVotes > normalVotes) {
            // Hardcore wins wins
            gameMode = GameMode.HARDCORE;
        } else if (hardcoreVotes == normalVotes) {
            // Tie. Means the hardcore wins, 'cause YO-LOOOO.
            gameMode = GameMode.HARDCORE;
        } else {
            throw new Exception("Could not determine which gamemode to set. Votes counting was messed up.");
        }

        setModeVariables(gameMode);
    }

    private void setModeVariables(GameMode mode) throws Exception {
        switch (mode) {
            case NORMAL:
                maxThrows = 3;
                stake = 2;
                break;
            case HARDCORE:
                maxThrows = 1;
                stake = 4;
                break;
            default:
                throw new Exception("Could not determine what gamemode this is");
        }

        for (Player p : players) {
            playersThrowsLeftMap.put(p.getId(), maxThrows);
            playersMaxThrowsMap.put(p.getId(), maxThrows);
            playerThrowMap.put(p.getId(), new Throw(0, 0, 0));
        }
    }

    public void generatePlayerOrder() {
        playerOrder = new ArrayList<>();

        // Add a player to the player order
        do {
            // Get a new random index
            Random random = new Random();
            int index = random.nextInt(players.size());
            // Get a random player from the player list
            Player p = players.get(index);

            // If the player is not in the order yet, add it
            if (!playerIsInOrder(p)) {
                playerOrder.add(p.getId());
            }
        }
        // Do all of this, while the order does not have all of the players in the order yet
        while (playerOrder.size() < players.size());

        printPlayerOrder();
    }


    private void printPlayerOrder() {
        System.out.println("-------------");
        System.out.println("Player order");
        System.out.println("-------------");
        for (int order : playerOrder) {
            System.out.println(order);
        }
    }

    public int getThrowsLeft(int playerId) {
        return playersThrowsLeftMap.get(playerId);
    }

    public int getMaxThrows(int playerId) {
        return playersMaxThrowsMap.get(playerId);
    }

    public Player getNextPlayer() throws Exception {
        if (playerOrderTurn == 0) {
            onFirstPlayerWillThrow();
        } else if (playerOrderTurn > 0) {
            onAnotherPlayerWillThrow();
        } else {
            throw new Exception("Player order turn is below 0, should be impossible");
        }

        return getPlayer(playerOrder.get(playerOrderTurn++));
    }

    private void onFirstPlayerWillThrow() throws Exception {
        firstPlayerId = getPlayer(playerOrder.get(playerOrderTurn)).getId();

        playersThrowsLeftMap.put(firstPlayerId, maxThrows);
        playersMaxThrowsMap.put(firstPlayerId, maxThrows);
    }

    private void onFirstPlayerDoneThrowing() throws Exception {
        int maxThrowsOfFirstPlayer = playersMaxThrowsMap.get(firstPlayerId);
        int throwsLeftOfFirstPlayer = playersThrowsLeftMap.get(firstPlayerId);

        // First of all, player one said he's done throwing, so let's make sure of that.
        playersThrowsLeftMap.put(firstPlayerId, 0);

        // If the first player only got to throw once, it's once for all
        if (maxThrowsOfFirstPlayer == 1) {
            setThrowsForOtherPlayers(1);
            System.out.println("Setting throws for other players to 1");
            return;
        }

        // If the first player used all of the throws, set them all for all
        if (throwsLeftOfFirstPlayer == 0) {
            setThrowsForOtherPlayers(maxThrowsOfFirstPlayer);
            return;
        }

        // If the first player did not use all of the throws, subtract the throws left from the max
        if (throwsLeftOfFirstPlayer > 0) {
            int newThrowsForAll = maxThrowsOfFirstPlayer - throwsLeftOfFirstPlayer;
            setThrowsForOtherPlayers(newThrowsForAll);
            return;
        }

        throw new Exception("Could not determine a way to set other player throws after the first player threw.");
    }

    private void onAnotherPlayerWillThrow() throws Exception {

    }

    private void setThrowsForOtherPlayers(int amountOfThrows) {
        for (Player p : players) {
            if (p.getId() == firstPlayerId) {
                continue;
            }
            playersMaxThrowsMap.put(p.getId(), amountOfThrows);
            playersThrowsLeftMap.put(p.getId(), amountOfThrows);
        }
    }

    private boolean playerIsInOrder(Player player) {
        for (int playerId : playerOrder) {
            if (playerId == player.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public MexicanGameState generateGameState() {
        List<PlayerTableRowItem> playerTableRowItems = new ArrayList<>();

        for (Player p : players) {
            PlayerTableRowItem item = new PlayerTableRowItem(
                    p,
                    playerThrowMap.get(p.getId()).getScore(),
                    playersThrowsLeftMap.get(p.getId()));
            playerTableRowItems.add(item);
        }

        return new MexicanGameState(gameMode.ordinal(), stake, playerTableRowItems);
    }

    @Override
    public MexicanGameResults generateGameResults() throws Exception {
        List<PlayerResultItem> playerResultItems = new ArrayList<>();
        for (Player p : players) {
            playerResultItems.add(new PlayerResultItem(p, playerThrowMap.get(p.getId()).getScore()));
        }
        Player loser = getLoser();
        return new MexicanGameResults(stake, loser, playerResultItems);
    }

    public Player getLoser() throws Exception {
        Player loser = null;
        int currentLowest = 9999; // Set initial lowest

        for (Integer playerId : playerThrowMap.keySet()) {
            int score = playerThrowMap.get(playerId).getScore();
            if (scoreIsLowerThan(score, currentLowest)) {
                loser = getPlayer(playerId);
                currentLowest = score;
            }
        }

        return loser;
    }

    private boolean scoreIsLowerThan(int score, int currentLowest) {
        // If score is 21, it's not the lowest
        if (score == 21) {
            return false;
        }

        // Simply compare integers
        if (score < currentLowest) {
            // Score is lower
            return true;
        } else if (score == currentLowest) {
            // Score is equal, we're not specially handling equal scores for now
            return false;
        } else {
            // Score is higher
            return false;
        }
    }

    public enum GameMode {
        NORMAL,
        HARDCORE
    }
}
