package io.github.edwinvanrooij;

import com.google.gson.Gson;

/**
 * Created by eddy
 * on 6/5/17.
 */
public class Util {
    public static String objectToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
