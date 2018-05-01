package io.github.edwinvanrooij.domain.camelrace;


import io.github.edwinvanrooij.domain.Player;

/**
 * Created by eddy
 * on 6/8/17.
 */
public class PlayerNewBid {
     private String gameId;
     private Player player;
     private Bid bid;

     public String getGameId() {
          return gameId;
     }

     public void setGameId(String gameId) {
          this.gameId = gameId;
     }

     public Bid getBid() {
          return bid;
     }

     public void setBid(Bid bid) {
          this.bid = bid;
     }

     public Player getPlayer() {
          return player;
     }

     public void setPlayer(Player player) {
          this.player = player;
     }


     public PlayerNewBid(String gameId, Player player, Bid bid) {
          this.gameId = gameId;
          this.player = player;
          this.bid = bid;
     }
}
