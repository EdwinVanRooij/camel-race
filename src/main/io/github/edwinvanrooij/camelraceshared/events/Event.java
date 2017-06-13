package io.github.edwinvanrooij.camelraceshared.events;

/**
 * Created by eddy
 * on 6/7/17.
 */
public class Event {
    // region Basic key pair
    public static final String KEY_EVENT_TYPE = "eventType";
    public static final String KEY_EVENT_VALUE = "value";
    // endregion

    // region Event keys
    public static final String KEY_GAME_CREATE = "gameCreate";
    public static final String KEY_GAME_CREATED = "gameCreated";

    public static final String KEY_PLAYER_JOIN = "playerJoin";
    public static final String KEY_PLAYER_JOINED = "playerJoined";

    public static final String KEY_PLAYER_NEW_BID = "playerNewBid";
    public static final String KEY_PLAYER_BID_HANDED_IN = "playerBidHandedIn";

    public static final String KEY_GAME_START = "gameStart";
    public static final String KEY_GAME_STARTED_WITH_STATE = "gameStartedWithState";
    public static final String KEY_GAME_STARTED = "gameStarted";

    public static final String KEY_NEW_ROUND = "newRound";
    public static final String KEY_ROUND_RESULTS = "roundResults";

    public static final String KEY_GET_ALL_RESULTS = "getAllResults";
    public static final String KEY_ALL_RESULTS = "allResults";

    public static final String KEY_GAME_OVER_PERSONAL_RESULTS = "gameOverPersonalResults";

    public static final String KEY_GAME_RESTART = "gameRestart";


    public static final String KEY_PICK_CARD = "pickCard";
    public static final String KEY_PICKED_CARD = "pickedCard";

    public static final String KEY_CAMEL_WON = "camelWon";
    public static final String KEY_CAMEL_DID_WIN = "camelDidWin";
    public static final String KEY_CAMEL_DID_NOT_WIN = "camelDidNotWin";

    public static final String KEY_MOVE_CARDS_BY_LATEST = "moveCardsByLatest";
    public static final String KEY_NEW_CAMEL_POSITIONS = "newCamelPositions";

    public static final String KEY_SHOULD_SIDE_CARD_TURN = "shouldSideCardTurn";
    public static final String KEY_SHOULD_SIDE_CARD_TURN_NO = "shouldSideCardTurnNo";
    public static final String KEY_SHOULD_SIDE_CARD_TURN_YES = "shouldSideCardTurnYes";

    public static final String KEY_NEW_CAMEL_LIST = "newCamelList";
    // endregion

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
    public Event(String eventType) {
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
