package io.github.edwinvanrooij;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.edwinvanrooij.camelraceshared.domain.Player;
import io.github.edwinvanrooij.camelraceshared.events.*;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Util {

    public static final int MAX_IDLE_TIMEOUT = 30; // in minutes

    private static Gson gson = new Gson();
    private static JsonParser parser = new JsonParser();

    public static String objectToJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Event jsonToEvent(String json) throws Exception {
        JsonObject wholeJson = parser.parse(json).getAsJsonObject();

        String type = wholeJson.get(Event.KEY_EVENT_TYPE).getAsString();

        Event event = new Event(type);

        switch (type) {
            case Event.KEY_GAME_CREATE:
                // N/A
                break;
            case Event.KEY_PLAYER_JOIN:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PlayerJoinRequest.class)
                );
                break;
            case Event.KEY_PLAYER_NEW_BID:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PlayerNewBid.class)
                );
                break;
            case Event.KEY_PLAYER_READY:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PlayerNewBid.class)
                );
                break;
            case Event.KEY_PLAYER_NOT_READY:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PlayerNotReady.class)
                );
                break;
            case Event.KEY_PLAY_AGAIN:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PlayAgainRequest.class)
                );
                break;
            case Event.KEY_PLAYER_ALIVE_CHECK:
                event.setValue(
                        gson.fromJson(wholeJson.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PlayerAliveCheck.class)
                );
                break;
            case Event.KEY_GAME_START:
                event.setValue(
                        wholeJson.get(Event.KEY_EVENT_VALUE).getAsString()
                );
                break;
            case Event.KEY_GAME_RESTART:
                event.setValue(
                        wholeJson.get(Event.KEY_EVENT_VALUE).getAsString()
                );
                break;
            case Event.KEY_NEW_ROUND:
                event.setValue(
                        wholeJson.get(Event.KEY_EVENT_VALUE).getAsString()
                );
                break;
            case Event.KEY_GET_ALL_RESULTS:
                event.setValue(
                        wholeJson.get(Event.KEY_EVENT_VALUE).getAsString()
                );
                break;
            case Event.KEY_PICK_CARD:
                event.setValue(
                        wholeJson.get(Event.KEY_EVENT_VALUE).getAsString()
                );
                break;
            case Event.KEY_CAMEL_WON:
                event.setValue(
                        wholeJson.get(Event.KEY_EVENT_VALUE).getAsString()
                );
                break;
            case Event.KEY_SHOULD_SIDE_CARD_TURN:
                event.setValue(
                        wholeJson.get(Event.KEY_EVENT_VALUE).getAsString()
                );
                break;
            case Event.KEY_MOVE_CARDS_BY_LATEST:
                event.setValue(
                        wholeJson.get(Event.KEY_EVENT_VALUE).getAsString()
                );
                break;
            case Event.KEY_NEW_CAMEL_LIST:
                event.setValue(
                        wholeJson.get(Event.KEY_EVENT_VALUE).getAsString()
                );
                break;
            default:
                throw new Exception(String.format("No suitable event found for:\r\nType '%s'\r\nWhole json: '%s'", type, wholeJson.toString()));
        }
        return event;
    }
}
