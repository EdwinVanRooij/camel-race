package io.github.edwinvanrooij;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.edwinvanrooij.domain.Event;
import io.github.edwinvanrooij.domain.Player;
import io.github.edwinvanrooij.net.PlayerJoinRequest;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Util {
    public static String objectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object jsonToObject(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, Object.class);
    }

    public static Event jsonToEvent(String json) throws Exception {
        Gson gson = new Gson();

        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(json).getAsJsonObject();
        System.out.println(String.format("Root obj: %s", rootObj.toString()));

        String type = rootObj.get("eventType").getAsString();
        System.out.println(String.format("Type: %s", type));

        switch (type) {
            case Event.PLAYER_JOINED:
                return new Event(
                        Event.PLAYER_JOINED,
                        gson.fromJson(
                                rootObj.get("value").getAsJsonObject().toString(), PlayerJoinRequest.class
                        ));
        }

        throw new Exception(String.format("No suitable event found for:\r\nType '%s'\r\nWhole json: '%s'", type, rootObj.toString()));
    }
}
