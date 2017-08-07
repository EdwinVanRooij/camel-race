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

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
    private static SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy_MM_dd");

    private static Properties configProps;
    private static boolean isProduction;

    static {
        try {
            configProps = getConfigProperties();
            isProduction = isProduction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        if (isProduction) {
            String log = String.format("%s\t||\tInfo\t||\t%s\n", generateTimeStamp(), message);
            try (FileWriter fw = new FileWriter(generateFileName(), true)) {
                fw.write(log);
            } catch (IOException ioe) {
                System.err.println("IOException: " + ioe.getMessage());
            }
        }
        System.out.println(String.format("%s\t||\tInfo\t||\t%s", generateTimeStamp(), message));
    }

    public static void logError(Throwable e) {
        if (isProduction) {
            String log = String.format("%s\t||\tError\t||\t%s\n", generateTimeStamp(), e.getMessage());
            try (FileWriter fw = new FileWriter(generateFileName(), true)) {
                fw.write(log);
            } catch (IOException ioe) {
                System.err.println("IOException: " + ioe.getMessage());
            }
        }
        e.printStackTrace();
        System.out.println(String.format("%s\t||\tError\t||\t%s", generateTimeStamp(), e.getMessage()));
    }

    private static String generateTimeStamp() {
        return dateFormat.format(new Date());
    }

    private static String generateFileName() {
        String date = fileDateFormat.format(new Date());
        return String.format("logs/%s.txt", date);
    }

    private static boolean isProduction() throws Exception {
        Properties props = configProps;
        if (props == null) {
            throw new Exception("No properties file found...");
        }
        return Boolean.parseBoolean(props.getProperty(Config.KEY_PROPERTIES_CONFIG_PRODUCTION));
    }

    private static Properties getConfigProperties() {
        try (InputStream input = new FileInputStream(Config.NAME_PROPERTIES_CONFIG)) {

            Properties prop = new Properties();
            prop.load(input);

            return prop;
        } catch (IOException ex) {
            logError(ex);
            return null;
        }
    }

    public static String extractGameType(String gameId) throws Exception {
        String firstChar = gameId.substring(0, 1);
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
