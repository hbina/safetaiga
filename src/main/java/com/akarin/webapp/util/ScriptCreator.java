package com.akarin.webapp.util;

import com.akarin.webapp.imageprocessing.ImageProcessingTools;

import java.util.concurrent.ThreadLocalRandom;

public class ScriptCreator {

    /**
     * CREATE IMAGE DATABASE
     */
    public final static String CREATE_IMAGEDB_ANIME_RGB_INTEGER = "CREATE TABLE IF NOT EXISTS imagedb_anime_rgb_integer (name TEXT, episode INT, panel INT, pixel_rgb INT["
            + ImageProcessingTools.DIVISOR_VALUE + "][" + ImageProcessingTools.DIVISOR_VALUE
            + "][3], PRIMARY KEY(name, episode, panel));";

    public final static String CREATE_IMAGEDB_ANIME_RGB_FLOAT = "CREATE TABLE IF NOT EXISTS imagedb_anime_rgb_float (name TEXT, episode INT, panel INT, pixel_rgb real["
            + ImageProcessingTools.DIVISOR_VALUE + "][" + ImageProcessingTools.DIVISOR_VALUE
            + "][3], PRIMARY KEY(name, episode, panel));";

    public final static String CREATE_IMAGEDB_USER_IMAGE_REQUEST = "CREATE TABLE IF NOT EXISTS imagedb_user_image_request (image_id SERIAL, request_ip TEXT, pixel_rgb real["
            + ImageProcessingTools.DIVISOR_VALUE + "][" + ImageProcessingTools.DIVISOR_VALUE
            + "][3], PRIMARY KEY(image_id));";

    public final static String CREATE_IMAGEDB_USER_IMAGE_REQUEST_FLOAT = "CREATE TABLE IF NOT EXISTS imagedb_user_image_request_float (image_id SERIAL, request_ip TEXT, pixel_rgb real["
            + ImageProcessingTools.DIVISOR_VALUE + "][" + ImageProcessingTools.DIVISOR_VALUE
            + "][3], PRIMARY KEY(image_id));";

    public final static String CREATE_IMAGEDB_USER_IMAGE_REQUEST_BYTE = "CREATE TABLE IF NOT EXISTS imagedb_user_image_request_byte (image_id SERIAL, request_ip TEXT, imagefile bytea);";
    public final static String CREATE_IMAGEDB_ANIME_BASIC_HISTOGRAM_HASH = "CREATE TABLE IF NOT EXISTS imagedb_anime_basic_histogram_hash (name TEXT, episode INT, panel INT, hash TEXT, PRIMARY KEY(name, episode, panel));";
    public final static String CREATE_IMAGEDB_ANIME_BASIC_HISTOGRAM_HASH_2 = "CREATE TABLE IF NOT EXISTS imagedb_anime_basic_histogram_hash (name TEXT, episode INT, panel INT, hash TEXT, PRIMARY KEY(name, episode, panel));";

    /**
     * DROP TEXTBOARD DATABASE
     */
    public final static String DROP_BOARDS = "DROP TABLE IF EXISTS boards;";
    public final static String DROP_THREADS = "DROP TABLE IF EXISTS threads;";
    public final static String DROP_POSTS = "DROP TABLE IF EXISTS posts;";

    /**
     * DROP IMAGE DATABASE
     */
    public final static String DROP_IMAGEDB_ANIME_RGB_INTEGER = "DROP TABLE IF EXISTS imagedb_anime_rgb_integer;";
    public final static String DROP_IMAGEDB_ANIME_RGB_FLOAT = "DROP TABLE IF EXISTS imagedb_anime_rgb_float;";
    public final static String DROP_IMAGEDB_USER_IMAGE_REQUEST_INTEGER = "DROP TABLE IF EXISTS imagedb_user_image_request_integer;";
    public final static String DROP_IMAGEDB_USER_IMAGE_REQUEST_FLOAT = "DROP TABLE IF EXISTS imagedb_user_image_request_float;";
    public final static String DROP_IMAGEDB_ANIME_BASIC_HISTOGRAM_HASH = "DROP TABLE IF EXISTS imagedb_anime_basic_histogram_hash;";

    public final static String SELECT_ALL_FROM_BOARDS = "SELECT * FROM boards;";
    public final static String SELECT_ALL_FROM_THREADS = "SELECT * FROM threads;";
    public final static String SELCT_ALL_FROM_POSTS = "SELECT * FROM posts;";

    public final static String SELECT_ALL_FROM_IMAGEDB_INTEGER = "SELECT * FROM imagedb_anime_rgb_integer;";
    public final static String SELECT_ALL_FROM_IMAGEDB_USER_IMAGE_REQUEST_INTEGER = "SELECT * FROM imagedb_user_image_request_integer;";

    public final static String SELECT_ALL_FROM_IMAGEDB_FLOAT = "SELECT * FROM imagedb_anime_rgb_float;";
    public final static String SELECT_ALL_FROM_IMAGEDB_USER_IMAGE_REQUEST_FLOAT = "SELECT * FROM imagedb_user_image_request_float;";

    public final static String CLEAR_IMAGEDB_ANIME_BASIC_HISTOGRAM_HASH = "DELETE FROM imagedb_anime_basic_histogram_hash";

    public static String insertBasicHistogramHash(final String name, final int episode, final int panel,
                                                  final String hash) {
        return "INSERT INTO imagedb_anime_basic_histogram_hash (name, episode, panel, hash) VALUES ('"
                + name + "','" + episode + "','" + panel + "','" + hash + "');";
    }

    public static String insertIntoImagedbAnimeRgbInteger(final String name, final int episode, final int panel,
                                                          final int[][][] tripleArray) {
        String script = "INSERT INTO imagedb_anime_rgb_integer (name, episode, panel, pixel_rgb) VALUES ('" + name
                + "','" + episode + "', '" + panel + "', ";
        final String RGBArray = convertTripleArrayToQueryString(tripleArray);
        script += RGBArray + ");";
        return script;
    }

    public static String selectAverageOfImageDb() {

        StringBuilder selectString = new StringBuilder("SELECT ");
        for (int a = 1; a <= ImageProcessingTools.DIVISOR_VALUE; a++) {
            for (int b = 1; b <= ImageProcessingTools.DIVISOR_VALUE; b++) {
                for (int c = 1; c <= 3; c++) {
                    selectString.append("AVG(pixel_rgb[").append(a).append("][ ").append(b).append("][").append(c).append("]) AS \"").append(a).append(":").append(b).append(":").append(c).append("\"");
                    if (!(a == ImageProcessingTools.DIVISOR_VALUE && b == ImageProcessingTools.DIVISOR_VALUE && c == 3)) {
                        selectString.append(", ");
                    }
                }
            }
        }

        selectString.append(" FROM imagedb_anime_rgb_integer;");

        return selectString.toString();
    }

    public static String getMinMaxOfImageDb() {

        StringBuilder script = new StringBuilder("SELECT ");
        for (int a = 1; a <= ImageProcessingTools.DIVISOR_VALUE; a++) {
            for (int b = 1; b <= ImageProcessingTools.DIVISOR_VALUE; b++) {
                for (int c = 1; c <= 3; c++) {
                    script.append("MIN(pixel_rgb[").append(a).append("][").append(b).append("][").append(c).append("]) AS \"MIN:").append(a).append(":").append(b).append(":").append(c).append("\", ");
                    script.append("MAX(pixel_rgb[").append(a).append("][").append(b).append("][").append(c).append("]) AS \"MAX:").append(a).append(":").append(b).append(":").append(c).append("\"");
                    if (!(a == ImageProcessingTools.DIVISOR_VALUE && b == ImageProcessingTools.DIVISOR_VALUE && c == 3)) {
                        script.append(", ");
                    }
                }
            }
        }

        script.append(" FROM imagedb_anime_rgb_integer;");
        return script.toString();
    }

    public static String insertIntoImageDbUserImageRequest(final String ipAddress, final int[][][] tripleArray) {
        return "INSERT INTO imagedb_user_image_request (request_ip, pixel_rgb) VALUES ('" + ipAddress
                + "', " + convertTripleArrayToQueryString(tripleArray) + ");";
    }

    public static String findMatchingImageDataBruteForce(final int[][][] tripleArray) {

        StringBuilder script = new StringBuilder("SELECT name, episode, panel FROM imagedb_anime_rgb_integer WHERE ");

        for (int a = 2; a <= (ImageProcessingTools.DIVISOR_VALUE - 1); a++) { // Y-axis
            for (int b = 2; b <= (ImageProcessingTools.DIVISOR_VALUE - 1); b++) { // X-axis
                for (int c = 1; c <= 3; c++) {
                    script.append("(pixel_rgb[").append(a).append("][").append(b).append("][").append(c).append("] BETWEEN ").append("(").append(tripleArray[a - 1][b - 1][c - 1]).append(" - ").append(ImageProcessingTools.BUFFER_VALUE).append(") AND (").append(tripleArray[a - 1][b - 1][c - 1]).append(" + ").append(ImageProcessingTools.BUFFER_VALUE).append("))");
                    if (c != 3) {
                        script.append("AND ");
                    }
                }
            }
        }

        script.append(";");
        return script.toString();
    }

    public static String findMatchingImageDataRandomized(final int[][][] tripleArray) {

        StringBuilder script = new StringBuilder("SELECT name, episode, panel FROM imagedb_anime_rgb_integer WHERE ");

        for (int a = 1; a <= ImageProcessingTools.TRIAL_VALUE; a++) {

            final int x = ThreadLocalRandom.current().nextInt(0, ImageProcessingTools.DIVISOR_VALUE);
            final int y = ThreadLocalRandom.current().nextInt(0, ImageProcessingTools.DIVISOR_VALUE);

            for (int c = 0; c < 3; c++) {
                script.append("(pixel_rgb[").append(x + 1).append("][").append(y + 1).append("][").append(c + 1).append("] BETWEEN ").append("(").append(tripleArray[x][y][c]).append(" - ").append(ImageProcessingTools.BUFFER_VALUE).append(") AND (").append(tripleArray[x][y][c]).append(" + ").append(ImageProcessingTools.BUFFER_VALUE).append("))");
                if (c != 2) {
                    script.append("AND ");
                }

            }
        }

        script.append(";");
        return script.toString();
    }

    public static String findMatchingImageDataRandomizedV2(final int[][][] tripleArray) {

        StringBuilder script = new StringBuilder("SELECT name, episode, panel FROM imagedb_anime_rgb_integer WHERE ");

        final int x = ThreadLocalRandom.current().nextInt(0,
                (ImageProcessingTools.DIVISOR_VALUE - ImageProcessingTools.TRIAL_VALUE));
        final int y = ThreadLocalRandom.current().nextInt(0,
                (ImageProcessingTools.DIVISOR_VALUE - ImageProcessingTools.TRIAL_VALUE));

        for (int a = 0; a < ImageProcessingTools.TRIAL_VALUE; a++) {
            for (int b = 0; b < ImageProcessingTools.TRIAL_VALUE; b++) {
                for (int c = 0; c < 3; c++) {
                    script.append("(pixel_rgb[").append(x + a + 1).append("][").append(y + b + 1).append("][").append(c + 1).append("] BETWEEN ").append("(").append(tripleArray[x + a][y + b][c]).append(" - ").append(ImageProcessingTools.BUFFER_VALUE).append(") AND (").append(tripleArray[x + a][y + b][c]).append(" + ").append(ImageProcessingTools.BUFFER_VALUE).append("))");
                    if (c != 2) {
                        script.append("AND ");
                    }

                }
            }
        }

        script.append(";");
        return script.toString();
    }

    public static String findMatchingImageDataIncremental(final int x, final int y, final int z, final int value) {

        String script = "SELECT name, episode, panel FROM imagedb_anime_rgb_integer WHERE ";

        script += "(pixel_rgb[" + (x + 1) + "][" + (y + 1) + "][" + (z + 1) + "] BETWEEN " + "(" + value + " - "
                + ImageProcessingTools.BUFFER_VALUE + ") AND (" + value + " + " + ImageProcessingTools.BUFFER_VALUE
                + "))";

        script += ";";
        return script;
    }

    public static String findMatchingImageDataIncrementalRGB(final int x, final int y, final int[] array) {

        StringBuilder script = new StringBuilder("SELECT name, episode, panel FROM imagedb_anime_rgb_integer WHERE ");

        for (int a = 0; a < 3; a++) {
            script.append("(pixel_rgb[").append(x + 1).append("][").append(y + 1).append("][").append(a + 1).append("] BETWEEN ").append("(").append(array[a]).append(" - ").append(ImageProcessingTools.BUFFER_VALUE).append(") AND (").append(array[a]).append(" + ").append(ImageProcessingTools.BUFFER_VALUE).append("))");
            if (a < 2) {
                script.append(" AND ");
            }
        }
        script.append(";");
        return script.toString();
    }

    private static String convertTripleArrayToQueryString(final int[][][] tripleArray) {

        StringBuilder script = new StringBuilder("'{");
        for (int a = 0; a < tripleArray.length; a++) { // y-axis
            script.append("{");
            for (int b = 0; b < tripleArray[a].length; b++) { // x-axis
                script.append("{");
                for (int c = 0; c < tripleArray[a][b].length; c++) {
                    script.append(tripleArray[a][b][c]);
                    if (c < (tripleArray[a][b].length - 1)) {
                        script.append(",");
                    }
                }
                if (b < (tripleArray[a].length - 1)) {
                    script.append("},");
                } else {
                    script.append("}");
                }
            }
            if (a < (tripleArray.length - 1)) {
                script.append("},");
            } else {
                script.append("}");
            }
        }
        script.append("}'");
        return script.toString();
    }
}
