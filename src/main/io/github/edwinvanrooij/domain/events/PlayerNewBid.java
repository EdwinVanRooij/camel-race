package io.github.edwinvanrooij.domain.events;

import io.github.edwinvanrooij.domain.Player;
import io.github.edwinvanrooij.domain.engine.Camel;

/**
 * Created by eddy
 * on 6/8/17.
 */
public class PlayerNewBid {
     private String gameId;
     private Player player;
     private Camel camel;
     private int value;

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

     public int getValue() {
          return value;
     }

     public void setValue(int value) {
          this.value = value;
     }

     public Camel getCamel() {
          return camel;
     }

     public PlayerNewBid(String gameId, Player player, Camel camel, int number) {
          this.gameId = gameId;
          this.player = player;
          this.camel = camel;
          this.value = number;
     }
}
