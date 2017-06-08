package io.github.edwinvanrooij;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.edwinvanrooij.domain.Event;
import io.github.edwinvanrooij.domain.eventvalues.PlayerJoinRequest;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Util {

    private static Gson gson = new Gson();
    private static JsonParser parser = new JsonParser();

    public static String objectToJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Event jsonToEvent(String json) throws Exception {
        JsonObject rootObj = parser.parse(json).getAsJsonObject();
        System.out.println(String.format("Whole: %s", rootObj.toString()));

        String type = rootObj.get(Event.KEY_EVENT_TYPE).getAsString();
        System.out.println(String.format("Type: %s", type));

//        switch (type) {
//            case Event.PLAYER_JOINED:
//                return new Event(
//                        Event.PLAYER_JOINED,
//                        gson.fromJson(
//                                rootObj.get(Event.KEY_EVENT_VALUE).getAsJsonObject().toString(), PlayerJoinRequest.class
//                        ));
//        }

        throw new Exception(String.format("No suitable event found for:\r\nType '%s'\r\nWhole json: '%s'", type, rootObj.toString()));
    }
}
