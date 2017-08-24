package io.github.edwinvanrooij.net.endpoints.mexican;

import com.google.gson.JsonObject;
import io.github.edwinvanrooij.camelraceshared.domain.Game;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.domain.mexican.*;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import io.github.edwinvanrooij.net.GameEventHandler;

import javax.websocket.Session;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static io.github.edwinvanrooij.Util.log;

/**
 * Created by eddy
 * on 7/24/17.
 */
public class MexicanEventHandler extends GameEventHandler {
    @Override
    protected boolean handleClientEvent(String event, JsonObject json, Session session) throws Exception {
        if (super.handleClientEvent(event, json, session)) {
            return true;
        }

        switch (event) {
            case Event.KEY_PLAYER_GAME_MODE_VOTE: {
                PlayerGameModeVote vote = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerGameModeVote.class);
                MexicanGame game = (MexicanGame) gameManager.getGameById(vote.getGameId());
                game.newVote(vote.getPlayer().getId(), vote.getGameModeOrdinal());

                sendEvent(Event.KEY_PLAYER_GAME_MODE_VOTE_HANDED_IN, true, session);

                Session gameSession = gameManager.getSessionByGameId(game.getId());
                sendEvent(Event.KEY_PLAYER_GAME_MODE_VOTE, vote, gameSession);

                if (game.everyoneVoted()) {
                    List<Session> playerSessions = gameManager.getPlayerSessionsByGame(game);
                    sendEvents(Event.KEY_EVERYONE_VOTED, "", playerSessions);

                    sendEvent(Event.KEY_EVERYONE_VOTED, game.generateGameState(), gameSession);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                Player playerOnTurn = game.getNextPlayer();
                                Session playerTurnSession = gameManager.getSessionByPlayerId(playerOnTurn.getId());
                                sendEvent(Event.KEY_YOUR_TURN, "", playerTurnSession);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 3000);
                }
                return true;
            }

            case Event.KEY_NEW_THROW: {
                NewPlayerThrow newPlayerThrow = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), NewPlayerThrow.class);
                MexicanGame game = (MexicanGame) gameManager.getGameById(newPlayerThrow.getGameId());
                Player p = game.getPlayer(newPlayerThrow.getPlayer().getId());

                if (game.getCurrentPlayerInTurn() == p) {
                    Throw newThrow = game.newPlayerThrow(p);

                    sendEvent(Event.KEY_NEW_THROW_SUCCESS, true, session);

                    Session gameSession = gameManager.getSessionByGameId(game.getId());
                    NewGameThrow newGameThrow = new NewGameThrow(newThrow, game.generateGameState());
                    sendEvent(Event.KEY_NEW_GAME_THROW, newGameThrow, gameSession);
                } else {
                    sendEvent(Event.KEY_NEW_THROW_SUCCESS, false, session);
                }
                return true;
            }

            case Event.KEY_END_TURN: {
                EndTurn endTurn = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), EndTurn.class);
                MexicanGame game = (MexicanGame) gameManager.getGameById(endTurn.getGameId());
                Player p = game.getPlayer(endTurn.getPlayerId());

                playerEndsTurn(game, p);
                return true;
            }

            default:
                throw new Exception("Could not determine a correct event type for host message.");
        }
    }

    private void playerEndsTurn(MexicanGame game, Player p) throws Exception {
        game.playerStops(p);

        if (game.everyoneIsDone()) {
            MexicanGameResults gameResults = game.generateGameResults();
            Session gameSession = gameManager.getSessionByGameId(game.getId());
            sendEvent(Event.KEY_ALL_RESULTS, gameResults, gameSession);

            // todo; send personalized results
//                        List<Session> playerSessions = gameManager.getPlayerSessionsByGame(game);
//                        sendEvents(Event.KEY_GAME_OVER_PERSONAL_RESULTS, "", playerSessions);
        } else {
            Player nextPlayer = game.getNextPlayer();
            Session playerTurnSession = gameManager.getSessionByPlayerId(nextPlayer.getId());
            sendEvent(Event.KEY_YOUR_TURN, "", playerTurnSession);
        }
    }

    @Override
    protected boolean handleHostEvent(String event, JsonObject json, Session session) throws Exception {
        // Let base handlers handle this event, if possible.
        if (super.handleHostEvent(event, json, session)) {
            return true;
        }

        switch (event) {
            case Event.KEY_MEXICAN_GAME_CREATE: {
                MexicanGame game = (MexicanGame) gameManager.createMexicanGame(session);
                sendEvent(Event.KEY_GAME_CREATED, game, session);
                return true;
            }

            case Event.KEY_GAME_START: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                MexicanGame game = (MexicanGame) gameManager.getGameById(gameId);
                sendEvent(Event.KEY_GAME_STARTED, "", session);

                List<Session> playerSessions = gameManager.getPlayerSessionsByGame(game);
                sendEvents(Event.KEY_GAME_STARTED, "", playerSessions);
                return true;
            }

            case Event.KEY_THROW_ENDED: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                MexicanGame game = (MexicanGame) gameManager.getGameById(gameId);

                Player p = game.getCurrentPlayerInTurn();
                if (game.playerHasThrowsLeft(p)) {
                    Session pSession = gameManager.getSessionByPlayerId(p.getId());
                    sendEvent(Event.KEY_PROMPT_THROW_OPTION, "", pSession);
                } else {
                    playerEndsTurn(game, p);
                }
                return true;
            }

            default:
                throw new Exception("Could not determine a correct event type for host message.");
        }
    }
}
