package io.github.edwinvanrooij.domain;

/**
 * Created by eddy
 * on 6/7/17.
 */
public class Event {
    // Basic key pair
    public static final String KEY_EVENT_TYPE = "eventType";
    public static final String KEY_EVENT_VALUE = "value";


    // Event keys
    public static final String KEY_PLAYER_JOIN = "playerJoin";
    public static final String KEY_PLAYER_JOINED = "playerJoined";

    public static final String KEY_PLAYER_LEAVE = "playerLeave";
    public static final String KEY_PLAYER_LEFT = "playerLeft";

    public static final String KEY_GAME_CREATE = "gameCreate";
    public static final String KEY_GAME_CREATED = "gameCreated";

    public static final String KEY_GAME_START = "gameStart";
    public static final String KEY_GAME_STARTED = "gameStarted";

    public static final String KEY_NEW_ROUND = "newRound";


    // Key of the event type
    private String eventType;
    private Object value;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Event(String eventType, Object value) {
        this.eventType = eventType;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType='" + eventType + '\'' +
                ", value=" + value +
                ", type=" + value.getClass().toString() +
                '}';
    }
}
