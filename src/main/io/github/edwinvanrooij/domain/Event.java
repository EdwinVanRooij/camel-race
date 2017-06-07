package io.github.edwinvanrooij.domain;

/**
 * Created by eddy
 * on 6/7/17.
 */
public class Event {
    public static final String GAME_ID = "gameId";
    public static final String PLAYER_JOINED = "playerJoined";

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
