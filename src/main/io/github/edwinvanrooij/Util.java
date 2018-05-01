package io.github.edwinvanrooij;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Created by eddy
 * on 7/18/17.
 */
public class Util {

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void logError(Throwable e) {
        System.out.println(String.format("Error: %s", e   ));
    }

    public static String extractGameType(String gameId) throws Exception {
        String firstChar = gameId.substring(0, 1).toLowerCase();
        switch (firstChar) {
            case Const.PREFIX_CAMEL_RACE:
                return Const.KEY_GAME_TYPE_CAMEL_RACE;
            case Const.PREFIX_MEXICAN:
                return Const.KEY_GAME_TYPE_MEXICAN;
            default:
                throw new Exception(String.format("Could not determine which game type relates to game ID '%s'", gameId));
        }
    }

    public static String generateGameId(String prefix) {
        char[] vowels = "aeiouy".toCharArray(); // klinkers
        char[] consonants = "bcdfghjklmnpqrstvwxz".toCharArray(); // medeklinkers

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        sb.append(prefix);
        for (int i = 0; i < 3; i++) {
            char c;
            if (i % 2 == 0) {
                c = vowels[random.nextInt(vowels.length)];
            } else {
                c = consonants[random.nextInt(consonants.length)];
            }
            sb.append(c);
        }
        return sb.toString();
    }

}
