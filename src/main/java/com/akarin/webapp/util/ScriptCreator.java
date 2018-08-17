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
        final String script = "INSERT INTO imagedb_anime_basic_histogram_hash (name, episode, panel, hash) VALUES ('"
                + name + "','" + Integer.toString(episode) + "','" + Integer.toString(panel) + "','" + hash + "');";
        return script;
    }

    public static String insertIntoImagedbAnimeRgbInteger(final String name, final int episode, final int panel,
                                                          final int[][][] tripleArray) {
        String script = "INSERT INTO imagedb_anime_rgb_integer (name, episode, panel, pixel_rgb) VALUES ('" + name
                + "','" + Integer.toString(episode) + "', '" + Integer.toString(panel) + "', ";
        final String RGBArray = convertTripleArrayToQueryString(tripleArray);
        script += RGBArray + ");";
        return script;
    }

    public static String selectAverageOfImageDb() {

        String selectString = "SELECT ";
        for (int a = 1; a <= ImageProcessingTools.DIVISOR_VALUE; a++) {
            for (int b = 1; b <= ImageProcessingTools.DIVISOR_VALUE; b++) {
                for (int c = 1; c <= 3; c++) {
                    selectString += "AVG(pixel_rgb[" + a + "][ " + b + "][" + c + "]) AS \"" + a + ":" + b + ":" + c
                            + "\"";
                    if (a == ImageProcessingTools.DIVISOR_VALUE && b == ImageProcessingTools.DIVISOR_VALUE && c == 3) {
                        selectString += "";
                    } else {
                        selectString += ", ";
                    }
                }
            }
        }

        selectString += " FROM imagedb_anime_rgb_integer;";

        return selectString;
    }

    public static String getMinMaxOfImageDb() {

        String script = "SELECT ";
        for (int a = 1; a <= ImageProcessingTools.DIVISOR_VALUE; a++) {
            for (int b = 1; b <= ImageProcessingTools.DIVISOR_VALUE; b++) {
                for (int c = 1; c <= 3; c++) {
                    script += "MIN(pixel_rgb[" + a + "][" + b + "][" + c + "]) AS \"MIN:" + a + ":" + b + ":" + c
                            + "\", ";
                    script += "MAX(pixel_rgb[" + a + "][" + b + "][" + c + "]) AS \"MAX:" + a + ":" + b + ":" + c
                            + "\"";
                    if (a == ImageProcessingTools.DIVISOR_VALUE && b == ImageProcessingTools.DIVISOR_VALUE && c == 3) {
                        script += "";
                    } else {
                        script += ", ";
                    }
                }
            }
        }

        script += " FROM imagedb_anime_rgb_integer;";
        return script;
    }

    public static String insertIntoImageDbUserImageRequest(final String ipAddress, final int[][][] tripleArray) {
        final String script = "INSERT INTO imagedb_user_image_request (request_ip, pixel_rgb) VALUES ('" + ipAddress
                + "', " + convertTripleArrayToQueryString(tripleArray) + ");";
        return script;
    }

    public static String findMatchingImageDataBruteForce(final int[][][] tripleArray) {

        String script = "SELECT name, episode, panel FROM imagedb_anime_rgb_integer WHERE ";

        for (int a = 2; a <= (ImageProcessingTools.DIVISOR_VALUE - 1); a++) { // Y-axis
            for (int b = 2; b <= (ImageProcessingTools.DIVISOR_VALUE - 1); b++) { // X-axis
                for (int c = 1; c <= 3; c++) {
                    script += "(pixel_rgb[" + a + "][" + b + "][" + c + "] BETWEEN " + "("
                            + tripleArray[a - 1][b - 1][c - 1] + " - " + ImageProcessingTools.BUFFER_VALUE + ") AND ("
                            + tripleArray[a - 1][b - 1][c - 1] + " + " + ImageProcessingTools.BUFFER_VALUE + "))";
                    if (a == (ImageProcessingTools.DIVISOR_VALUE - 1) && b == (ImageProcessingTools.DIVISOR_VALUE - 1)
                            && c == 3) {
                        script += "";
                    } else {
                        script += "AND ";
                    }
                }
            }
        }

        script += ";";
        return script;
    }

    public static String findMatchingImageDataRandomized(final int[][][] tripleArray) {

        String script = "SELECT name, episode, panel FROM imagedb_anime_rgb_integer WHERE ";

        for (int a = 1; a <= ImageProcessingTools.TRIAL_VALUE; a++) {

            final int x = ThreadLocalRandom.current().nextInt(0, ImageProcessingTools.DIVISOR_VALUE);
            final int y = ThreadLocalRandom.current().nextInt(0, ImageProcessingTools.DIVISOR_VALUE);

            for (int c = 0; c < 3; c++) {
                script += "(pixel_rgb[" + (x + 1) + "][" + (y + 1) + "][" + (c + 1) + "] BETWEEN " + "("
                        + tripleArray[x][y][c] + " - " + ImageProcessingTools.BUFFER_VALUE + ") AND ("
                        + tripleArray[x][y][c] + " + " + ImageProcessingTools.BUFFER_VALUE + "))";
                if (a == ImageProcessingTools.TRIAL_VALUE && c == 2) {
                    script += "";
                } else {
                    script += "AND ";
                }

            }
        }

        script += ";";
        return script;
    }

    public static String findMatchingImageDataRandomizedV2(final int[][][] tripleArray) {

        String script = "SELECT name, episode, panel FROM imagedb_anime_rgb_integer WHERE ";

        final int x = ThreadLocalRandom.current().nextInt(0,
                (ImageProcessingTools.DIVISOR_VALUE - ImageProcessingTools.TRIAL_VALUE));
        final int y = ThreadLocalRandom.current().nextInt(0,
                (ImageProcessingTools.DIVISOR_VALUE - ImageProcessingTools.TRIAL_VALUE));

        for (int a = 0; a < ImageProcessingTools.TRIAL_VALUE; a++) {
            for (int b = 0; b < ImageProcessingTools.TRIAL_VALUE; b++) {
                for (int c = 0; c < 3; c++) {
                    script += "(pixel_rgb[" + (x + a + 1) + "][" + (y + b + 1) + "][" + (c + 1) + "] BETWEEN " + "("
                            + tripleArray[x + a][y + b][c] + " - " + ImageProcessingTools.BUFFER_VALUE + ") AND ("
                            + tripleArray[x + a][y + b][c] + " + " + ImageProcessingTools.BUFFER_VALUE + "))";
                    if (a == (ImageProcessingTools.TRIAL_VALUE - 1) && b == (ImageProcessingTools.TRIAL_VALUE - 1)
                            && c == 2) {
                        script += "";
                    } else {
                        script += "AND ";
                    }

                }
            }
        }

        script += ";";
        return script;
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

        String script = "SELECT name, episode, panel FROM imagedb_anime_rgb_integer WHERE ";

        for (int a = 0; a < 3; a++) {
            script += "(pixel_rgb[" + (x + 1) + "][" + (y + 1) + "][" + (a + 1) + "] BETWEEN " + "(" + array[a] + " - "
                    + ImageProcessingTools.BUFFER_VALUE + ") AND (" + array[a] + " + "
                    + ImageProcessingTools.BUFFER_VALUE + "))";
            if (a < 2) {
                script += " AND ";
            }
        }
        script += ";";
        return script;
    }

    public static String convertTripleArrayToQueryString(final int[][][] tripleArray) {
        /**
         * create string for the RGBs
         */

        String script = "'{";
        for (int a = 0; a < tripleArray.length; a++) { // y-axis
            script += "{";
            for (int b = 0; b < tripleArray[a].length; b++) { // x-axis
                script += "{";
                for (int c = 0; c < tripleArray[a][b].length; c++) {
                    script += tripleArray[a][b][c];
                    if (c < (tripleArray[a][b].length - 1)) {
                        script += ",";
                    }
                }
                if (b < (tripleArray[a].length - 1)) {
                    script += "},";
                } else {
                    script += "}";
                }
            }
            if (a < (tripleArray.length - 1)) {
                script += "},";
            } else {
                script += "}";
            }
        }
        script += "}'";
        return script;
    }
}
