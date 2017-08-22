package io.github.edwinvanrooij.net.endpoints.mexican;

import com.google.gson.JsonObject;
import io.github.edwinvanrooij.camelraceshared.domain.mexican.MexicanGame;
import io.github.edwinvanrooij.camelraceshared.domain.mexican.PlayerGameModeVote;
import io.github.edwinvanrooij.camelraceshared.events.Event;
import io.github.edwinvanrooij.net.GameEventHandler;

import javax.websocket.Session;

import java.util.List;

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
                    sendEvents(Event.KEY_GAME_STARTED, "", playerSessions);

                    sendEvent(Event.KEY_GAME_STARTED_WITH_STATE, game.generateGameState(), gameSession);
                }
                return true;
            }

            default:
                throw new Exception("Could not determine a correct event type for host message.");
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

            default:
                throw new Exception("Could not determine a correct event type for host message.");
        }
    }
}
