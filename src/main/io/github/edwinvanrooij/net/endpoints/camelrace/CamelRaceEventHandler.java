package io.github.edwinvanrooij.net.endpoints.camelrace;

import com.google.gson.JsonObject;
import io.github.edwinvanrooij.camelraceshared.domain.*;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import io.github.edwinvanrooij.net.BaseEventHandler;

import javax.websocket.Session;

import java.util.List;
import java.util.Map;

import static io.github.edwinvanrooij.Util.logError;

/**
 * Created by eddy
 * on 7/24/17.
 */
public class CamelRaceEventHandler extends BaseEventHandler {
    @Override
    protected void handleClientEvent(String event, JsonObject json, Session session) throws Exception {
        switch (event) {
            case Event.KEY_PLAYER_JOIN: {
                PlayerJoinRequest playerJoinRequest = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerJoinRequest.class);
                String gameId = playerJoinRequest.getGameId();
                Player player = gameManager.playerJoin(gameId, playerJoinRequest.getPlayer(), session);
                sendEvent(Event.KEY_PLAYER_JOINED, player, session);

                Game game = gameManager.getGameById(gameId);
                sendEvent(Event.KEY_PLAYER_JOINED, player, gameManager.getSessionByGameId(game.getId()));
                break;
            }

            case Event.KEY_PLAYER_NEW_BID: {
                PlayerNewBid playerNewBid = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerNewBid.class);
                Boolean result = gameManager.playerNewBid(playerNewBid.getGameId(), playerNewBid.getPlayer(), playerNewBid.getBid());
                sendEvent(Event.KEY_PLAYER_BID_HANDED_IN, result, session);

                Session gameSession = gameManager.getSessionByGameId(playerNewBid.getGameId());
                sendEvent(Event.KEY_PLAYER_NEW_BID, playerNewBid, gameSession);
                break;
            }

            case Event.KEY_PLAYER_READY: {
                PlayerNewBid playerNewBid = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerNewBid.class);
                Boolean result = gameManager.playerNewBidAndReady(playerNewBid.getGameId(), playerNewBid.getPlayer(), playerNewBid.getBid());
                sendEvent(Event.KEY_PLAYER_READY_SUCCESS, result, session);

                Session gameSession = gameManager.getSessionByGameId(playerNewBid.getGameId());
                sendEvent(Event.KEY_PLAYER_READY, playerNewBid, gameSession);

                if (gameManager.isEveryoneReady(playerNewBid.getGameId())) {
                    sendEvent(Event.KEY_GAME_READY, "", gameSession);
                }
                break;
            }

            case Event.KEY_PLAYER_NOT_READY: {
                PlayerNotReady playerNotReady = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerNotReady.class);
                Boolean result = gameManager.playerNotReady(playerNotReady.getGameId(), playerNotReady.getPlayer());
                sendEvent(Event.KEY_PLAYER_NOT_READY_SUCCESS, result, session);

                Session gameSession = gameManager.getSessionByGameId(playerNotReady.getGameId());
                sendEvent(Event.KEY_PLAYER_NOT_READY, playerNotReady, gameSession);
                break;
            }

            case Event.KEY_PLAYER_ALIVE_CHECK: {
                PlayerAliveCheck playerAliveCheck = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerAliveCheck.class);
                gameManager.playerAliveCheck(playerAliveCheck);

                sendEvent(Event.KEY_PLAYER_ALIVE_CHECK_CONFIRMED, true, session);
                break;
            }

            case Event.KEY_PLAY_AGAIN: {
                PlayAgainRequest playAgainRequest = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayAgainRequest.class);

                String gameId = playAgainRequest.getGameId();
                Game game = gameManager.getGameById(gameId);

                game.playAgain(playAgainRequest.getPlayer().getId(), true);

                sendEvent(Event.KEY_PLAY_AGAIN_SUCCESSFUL, true, session);

                if (game.allPlayAgain()) {
                    restartGame(game);
                }
                break;
            }
            default:
                throw new Exception("Could not determine a correct event type for client message.");
        }
    }

    @Override
    protected void handleHostEvent(String event, JsonObject json, Session session) {
        try {
            switch (event) {
                case Event.KEY_GAME_CREATE: {
                    Game game = gameManager.createGame(session);
                    sendEvent(Event.KEY_GAME_CREATED, game, session);
                    break;
                }

                case Event.KEY_GAME_START: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    GameState currentGameState = game.generateGameState();
                    sendEvent(Event.KEY_GAME_STARTED_WITH_STATE, currentGameState, session);

                    List<Session> playerSessions = gameManager.getPlayerSessionsByGame(game);
                    sendEvents(Event.KEY_GAME_STARTED, "", playerSessions);

                    break;
                }

                case Event.KEY_PICK_CARD: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    Card card = game.pickCard();

                    sendEvent(Event.KEY_PICKED_CARD, card, session);
                    break;
                }

                case Event.KEY_CAMEL_WON: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    Camel camel = game.didCamelWinYet();

                    if (camel != null) {
                        sendEvent(Event.KEY_CAMEL_DID_WIN, camel, session);
                    } else {
                        sendEvent(Event.KEY_CAMEL_DID_NOT_WIN, "", session);
                    }
                    break;
                }

                case Event.KEY_MOVE_CARDS_BY_LATEST: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    game.moveCamelAccordingToLastCard();
                    List<Camel> newCamelPositions = game.getCamelList();

                    sendEvent(Event.KEY_NEW_CAMEL_POSITIONS, newCamelPositions, session);
                    break;
                }

                case Event.KEY_SHOULD_SIDE_CARD_TURN: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    boolean shouldItTurn = game.shouldTurnSideCard();

                    if (shouldItTurn) {
                        sendEvent(Event.KEY_SHOULD_SIDE_CARD_TURN_YES, game.getSideCardList(), session);
                    } else {
                        sendEvent(Event.KEY_SHOULD_SIDE_CARD_TURN_NO, "", session);
                    }
                    break;
                }

                case Event.KEY_NEW_CAMEL_LIST: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);

                    sendEvent(Event.KEY_NEW_CAMEL_LIST, game.newCamelList(), session);
                    break;
                }

                case Event.KEY_GET_ALL_RESULTS: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);

                    GameResults gameResults = game.generateGameResults();
                    sendEvent(Event.KEY_ALL_RESULTS, gameResults, session);

                    for (Map.Entry<Player, Session> entry : gameManager.getPlayerSessionMapByGame(game).entrySet()) {
                        Bid bid = game.getBid(entry.getKey().getId());
                        boolean thisPlayerWon = bid.getType() == game.getWinner().getCardType();
                        PersonalResultItem item = new PersonalResultItem(bid, thisPlayerWon);
                        sendEvent(Event.KEY_GAME_OVER_PERSONAL_RESULTS, item, entry.getValue());
                    }
                    break;
                }

                case Event.KEY_GAME_RESTART: {
                    String gameId = json.get(Event.KEY_VALUE).getAsString();
                    Game game = gameManager.getGameById(gameId);
                    restartGame(game);
                    break;
                }

                default:
                    throw new Exception("Could not determine a correct event type for host message.");
            }
        } catch (Exception e) {
            logError(e);
        }
    }
}
