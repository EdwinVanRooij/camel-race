package io.github.edwinvanrooij.domain.mexican;


import io.github.edwinvanrooij.domain.Player;

/**
 * Created by eddy
 * on 6/8/17.
 */
public class PlayerGameModeVote {
     private String gameId;
     private Player player;
     private int gameModeOrdinal;

     public String getGameId() {
          return gameId;
     }

     public void setGameId(String gameId) {
          this.gameId = gameId;
     }

     public Player getPlayer() {
          return player;
     }

     public void setPlayer(Player player) {
          this.player = player;
     }

     public int getGameModeOrdinal() {
          return gameModeOrdinal;
     }

     public void setGameModeOrdinal(int gameModeOrdinal) {
          this.gameModeOrdinal = gameModeOrdinal;
     }

     public PlayerGameModeVote(String gameId, Player player, int gameModeOrdinal) {

          this.gameId = gameId;
          this.player = player;
          this.gameModeOrdinal = gameModeOrdinal;
     }
}

