package io.github.edwinvanrooij.domain;

/**
 * Created by eddy
 * on 6/8/17.
 */
public class PlayerReady {
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

     public PlayerReady(String gameId, Player player) {
          this.gameId = gameId;
          this.player = player;
     }
}
