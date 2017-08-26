package io.github.edwinvanrooij.net.endpoints.camelrace;

import com.google.gson.JsonObject;
import io.github.edwinvanrooij.camelraceshared.domain.PersonalResultItem;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.domain.camelrace.*;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import io.github.edwinvanrooij.net.GameEventHandler;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

/**
 * Created by eddy
 * on 7/24/17.
 */
public class CamelRaceEventHandler extends GameEventHandler {
    @Override
    protected boolean handleClientEvent(String event, JsonObject json, Session session) throws Exception {
        // Let base handlers handle this event, if possible.
        if (super.handleClientEvent(event, json, session)) {
            return true;
        }

        switch (event) {
            case Event.KEY_PLAYER_NEW_BID: {
                PlayerNewBid playerNewBid = gson.fromJson(json.get(Event.KEY_VALUE).getAsJsonObject().toString(), PlayerNewBid.class);
                Boolean result = gameManager.playerNewBid(playerNewBid.getGameId(), playerNewBid.getPlayer(), playerNewBid.getBid());
                sendEvent(Event.KEY_PLAYER_BID_HANDED_IN, result, session);

                Session gameSession = gameManager.getSessionByGameId(playerNewBid.getGameId());
                sendEvent(Event.KEY_PLAYER_NEW_BID, playerNewBid, gameSession);
                return true;
            }

            default:
                throw new Exception("Could not determine a correct event type for client message.");
        }
    }

    @Override
    protected boolean handleHostEvent(String event, JsonObject json, Session session) throws Exception {
        // Let base handlers handle this event, if possible.
        if (super.handleHostEvent(event, json, session)) {
            return true;
        }

        switch (event) {
            case Event.KEY_CAMELRACE_GAME_CREATE: {
                CamelRaceGame game = (CamelRaceGame) gameManager.createCamelRaceGame(session);
                sendEvent(Event.KEY_GAME_CREATED, game, session);
                return true;
            }

            case Event.KEY_GAME_START: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                CamelRaceGame game = (CamelRaceGame) gameManager.getGameById(gameId);
                CamelRaceGameState currentGameState = game.generateGameState();
                sendEvent(Event.KEY_GAME_STARTED_WITH_STATE, currentGameState, session);

                List<Session> playerSessions = gameManager.getPlayerSessionsByGame(game);
                sendEvents(Event.KEY_GAME_STARTED, "", playerSessions);
                return true;
            }

            case Event.KEY_PICK_CARD: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                CamelRaceGame game = (CamelRaceGame) gameManager.getGameById(gameId);
                Card card = game.pickCard();

                sendEvent(Event.KEY_PICKED_CARD, card, session);
                return true;
            }

            case Event.KEY_CAMEL_WON: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                CamelRaceGame game = (CamelRaceGame) gameManager.getGameById(gameId);
                Camel camel = game.didCamelWinYet();

                if (camel != null) {
                    sendEvent(Event.KEY_CAMEL_DID_WIN, camel, session);
                } else {
                    sendEvent(Event.KEY_CAMEL_DID_NOT_WIN, "", session);
                }
                return true;
            }

            case Event.KEY_MOVE_CARDS_BY_LATEST: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                CamelRaceGame game = (CamelRaceGame) gameManager.getGameById(gameId);
                game.moveCamelAccordingToLastCard();
                List<Camel> newCamelPositions = game.getCamelList();

                sendEvent(Event.KEY_NEW_CAMEL_POSITIONS, newCamelPositions, session);
                return true;
            }

            case Event.KEY_SHOULD_SIDE_CARD_TURN: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                CamelRaceGame game = (CamelRaceGame) gameManager.getGameById(gameId);
                boolean shouldItTurn = game.shouldTurnSideCard();

                if (shouldItTurn) {
                    sendEvent(Event.KEY_SHOULD_SIDE_CARD_TURN_YES, game.getSideCardList(), session);
                } else {
                    sendEvent(Event.KEY_SHOULD_SIDE_CARD_TURN_NO, "", session);
                }
                return true;
            }

            case Event.KEY_NEW_CAMEL_LIST: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                CamelRaceGame game = (CamelRaceGame) gameManager.getGameById(gameId);

                sendEvent(Event.KEY_NEW_CAMEL_LIST, game.newCamelList(), session);
                return true;
            }

            case Event.KEY_GET_ALL_RESULTS: {
                String gameId = json.get(Event.KEY_VALUE).getAsString();
                CamelRaceGame game = (CamelRaceGame) gameManager.getGameById(gameId);

                GameResults gameResults = game.generateGameResults();
                sendEvent(Event.KEY_ALL_RESULTS, gameResults, session);

                for (Map.Entry<Player, Session> entry : gameManager.getPlayerSessionMapByGame(game).entrySet()) {
                    Bid bid = game.getBid(entry.getKey().getId());
                    boolean thisPlayerWon = bid.getType() == game.getWinner().getCardType();
                    PersonalResultItem item = new PersonalResultItem(bid, thisPlayerWon);
                    sendEvent(Event.KEY_GAME_OVER_PERSONAL_RESULTS, item, entry.getValue());
                }
                return true;
            }

            default:
                throw new Exception("Could not determine a correct event type for host message.");
        }
    }
}
