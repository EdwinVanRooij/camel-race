package io.github.edwinvanrooij.domain;

/**
 * Created by eddy
 * on 6/8/17.
 */
public class PlayerNotReady {
     private String gameId;
     private Player player;

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

     public PlayerNotReady(String gameId, Player player) {
          this.gameId = gameId;
          this.player = player;
     }
}
