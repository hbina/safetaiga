package com.akarin.webapp.managers;

import com.akarin.webapp.structure.AnimePanel;
import com.akarin.webapp.structure.IntegerPair;
import com.akarin.webapp.util.ScriptCreator;
import com.akarin.webapp.imageprocessing.ImageProcessingTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ImageSearchManager {

    private static final boolean BOOL_SCRIPT = false;
    private static final boolean BOOL_MATCHING_NAME = true;
    private static final Logger logger = LoggerFactory.getLogger(ImageSearchManager.class);

    public static void insertImageDataToDatabase(final String ipAddress, final BufferedImage image) {
        final String script = "INSERT INTO imagedb_user_image_request_byte (request_ip, imagefile) VALUES (?,?)";
        try (Connection connection = DatabaseManager.getConnection()) {

            final PreparedStatement pst = connection.prepareStatement(script);
            pst.setString(1, ipAddress);
            pst.setBytes(2, ImageProcessingTools.extractBytes(image));
            pst.executeUpdate();

        } catch (final SQLException | URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }

    public static void findMatchingImageDataRandomized(final Map<String, Object> model, final int[][][] array) {
        try (Connection connection = DatabaseManager.getConnection()) {

            final Statement stmt = connection.createStatement();

            final Map<String, IntegerPair> matchResultMap = new HashMap<>();
            final ArrayList<AnimePanel> values = new ArrayList<>();
            int maxIndex = 0;
            int valueIterator = -1; // use this to check if the algorithm even found any

            final String findMatchingImageDataRandomized = ScriptCreator.findMatchingImageDataRandomized(array);
            logger.info(findMatchingImageDataRandomized, BOOL_SCRIPT);

            final ResultSet rs = stmt.executeQuery(findMatchingImageDataRandomized);

            while (rs.next()) {
                logger.info("matching name:" + rs.getString("name") + " " + rs.getString("episode") + " "
                                + rs.getString("panel") + "@" + ((Integer.valueOf(rs.getString("panel")) / (24 * 60)) + ":"
                                + (Integer.valueOf(rs.getString("panel")) % (24))),
                        BOOL_MATCHING_NAME);

                final String key = rs.getString("name") + ":" + rs.getString("episode");
                if (matchResultMap.containsKey(key)) {
                    matchResultMap.get(key).b++;

                    // check if this is larger than the weight that we currently
                    // have. If so replace.

                    if (matchResultMap.get(key).b > matchResultMap.get(values.get(maxIndex).getKey(2)).b) {
                        maxIndex = matchResultMap.get(key).a;
                    }
                } else {
                    valueIterator++;
                    final AnimePanel animePanel = new AnimePanel(rs.getString("name"), rs.getString("episode"),
                            rs.getString("panel"));
                    matchResultMap.put(animePanel.getKey(2), new IntegerPair(valueIterator, 0));
                    values.add(animePanel);
                }
            }

            if (values.isEmpty()) {
                logger.info("Test 1: None found");
                model.put("test_1_boolean", false);
            } else {
                logger.info("Test 1: Found");
                model.put("test_1_boolean", true);
                model.put("test_1_weight", matchResultMap.get(values.get(maxIndex).getKey(2)).b);
                model.put("test_1_result", values.get(maxIndex));
                model.put("test_1_time", "" + (values.get(maxIndex).getPanel() / (24 * 60)) + ":"
                        + (values.get(maxIndex).getPanel() % (24)));
            }
        } catch (final SQLException | URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }

    public static void findMatchingImageDataRandomizedV2(final Map<String, Object> model, final int[][][] array) {
        try (Connection connection = DatabaseManager.getConnection()) {
            final Statement stmt = connection.createStatement();
            final Map<String, IntegerPair> matchResultMap = new HashMap<>();
            final ArrayList<AnimePanel> values = new ArrayList<>();
            int maxIndex = 0;
            int valueIterator = -1; // use this to check if the algorithm even
            // found
            // any

            final String findMatchingImageDataRandomizedV2 = ScriptCreator.findMatchingImageDataRandomizedV2(array);
            logger.info(findMatchingImageDataRandomizedV2, BOOL_SCRIPT);
            final ResultSet rs = stmt.executeQuery(findMatchingImageDataRandomizedV2);

            while (rs.next()) {
                logger.info("matching name:" + rs.getString("name") + " " + rs.getString("episode") + " "
                                + rs.getString("panel") + "@" + ((Integer.valueOf(rs.getString("panel")) / (24 * 60)) + ":"
                                + (Integer.valueOf(rs.getString("panel")) % (24))),
                        BOOL_MATCHING_NAME);

                final String key = rs.getString("name") + ":" + rs.getString("episode");
                if (matchResultMap.containsKey(key)) { // the fact that this is
                    // true
                    // means that there are
                    // at
                    // least 1 relation that
                    // exist in the map
                    matchResultMap.get(key).b++;

                    // check if this is larger than the weight that we currently
                    // have. If so replace.

                    if (matchResultMap.get(key).b > matchResultMap.get(values.get(maxIndex).getKey(2)).b) {
                        maxIndex = matchResultMap.get(key).a;
                    }
                } else {
                    valueIterator++;
                    final AnimePanel animePanel = new AnimePanel(rs.getString("name"), rs.getString("episode"),
                            rs.getString("panel"));
                    matchResultMap.put(animePanel.getKey(2), new IntegerPair(valueIterator, 0));
                    values.add(animePanel);
                }
            }

            if (values.isEmpty()) {
                logger.info("Test 2: None found");
                model.put("test_2_boolean", false);
            } else {
                logger.info("Test 2: Found");
                model.put("test_2_boolean", true);
                model.put("test_2_weight", matchResultMap.get(values.get(maxIndex).getKey(2)).b);
                model.put("test_2_result", values.get(maxIndex));
                model.put("test_5_time", "" + (values.get(maxIndex).getPanel() / (24 * 60)) + ":"
                        + (values.get(maxIndex).getPanel() % (24)));
            }
        } catch (final SQLException | URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }

    public static void findMatchingImageDataIncremental(final Map<String, Object> model, final int[][][] array) {
        try (Connection connection = DatabaseManager.getConnection()) {
            final Statement stmt = connection.createStatement();

            boolean test3Found = false;
            final Map<String, AnimePanel> matchResultMap = new HashMap<>();
            AnimePanel[] values;
            ResultSet rs;

            String findMatchingImageDataIncremental;

            for (int a = 0; a < ImageProcessingTools.DIVISOR_VALUE; a++) {
                for (int b = 0; b < ImageProcessingTools.DIVISOR_VALUE; b++) {
                    for (int c = 0; c < 3; c++) {
                        findMatchingImageDataIncremental = ScriptCreator.findMatchingImageDataIncremental(a, b, c,
                                array[a][b][c]);
                        logger.info("Execute Query:" + findMatchingImageDataIncremental, BOOL_SCRIPT);

                        rs = stmt.executeQuery(findMatchingImageDataIncremental);

                        while (rs.next()) {
                            final AnimePanel panelData = new AnimePanel(rs.getString("name"), rs.getInt(2),
                                    rs.getInt(3));
                            if (!(matchResultMap.containsKey(panelData.getKey()))) {
                                matchResultMap.put(panelData.getKey(), panelData);
                            } else {
                                test3Found = true;
                                matchResultMap.get(panelData.getKey()).incrementWeight();
                            }

                        }
                    }
                }
            }

            if (!test3Found) {
                logger.info("Test 3: None found");
                model.put("test_3_boolean", false);
            } else {
                logger.info("Test 3: Found");
                model.put("test_3_boolean", true);
                values = new AnimePanel[matchResultMap.size()];

                int index = 0;
                for (final Map.Entry<String, AnimePanel> mapEntry : matchResultMap.entrySet()) {
                    values[index] = mapEntry.getValue();
                    index++;
                }

                int maxIndex = -1;
                int maxValue = -1;
                for (int a = 0; a < values.length; a++) {
                    if (values[a].getWeight() > maxValue) {
                        maxValue = values[a].getWeight();
                        maxIndex = a;
                    }
                }
                if (maxIndex != -1) {
                    logger.info(maxValue + " " + values[maxIndex].getKey());
                    model.put("test_3_weight", maxValue);
                    model.put("test_3_result", new AnimePanel(values[maxIndex].getKey().split(":")[0],
                            values[maxIndex].getKey().split(":")[1], values[maxIndex].getKey().split(":")[2]));

                    model.put("test_5_time", "" + (Integer.valueOf(values[maxIndex].getKey().split(":")[2]) / (24 * 60))
                            + ":" + (Integer.valueOf(values[maxIndex].getKey().split(":")[2]) % (24)));
                } else {
                    logger.info("maxIndex is -1");
                }
            }
        } catch (final SQLException | URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }

    public static void findMatchingImageDataIncrementalRGB(final Map<String, Object> model, final int[][][] array) {
        try (Connection connection = DatabaseManager.getConnection()) {
            final Statement stmt = connection.createStatement();
            boolean test4Found = false;
            String findMatchingImageDataIncrementalRGB;
            final Map<String, AnimePanel> matchResult = new HashMap<>();
            AnimePanel[] values;
            ResultSet rs;

            for (int a = 0; a < ImageProcessingTools.DIVISOR_VALUE; a++) {
                for (int b = 0; b < ImageProcessingTools.DIVISOR_VALUE; b++) {
                    findMatchingImageDataIncrementalRGB = ScriptCreator.findMatchingImageDataIncrementalRGB(a, b,
                            array[a][b]);

                    rs = stmt.executeQuery(findMatchingImageDataIncrementalRGB);

                    while (rs.next()) {
                        logger.info("matching name:" + rs.getString("name") + " " + rs.getString("episode") + " "
                                + rs.getString("panel"), false);
                        final String key = "" + rs.getString("name") + ":" + rs.getInt(2) + ":" + rs.getInt(3);
                        if (!(matchResult.containsKey(key))) {
                            matchResult.put(key, new AnimePanel("" + rs.getString("name"), rs.getInt(2), rs.getInt(3)));
                        } else {
                            test4Found = true;
                            matchResult.get(key).incrementWeight();
                        }

                    }
                }
            }

            if (!test4Found) {
                logger.info("Test 4: None found");
                model.put("test_4_boolean", false);
            } else {
                logger.info("Test 4: Found");
                model.put("test_4_boolean", true);
                values = new AnimePanel[matchResult.size()];

                int index = 0;
                for (final Map.Entry<String, AnimePanel> mapEntry : matchResult.entrySet()) {
                    values[index] = mapEntry.getValue();
                    index++;
                }

                int maxIndex = -1;
                int maxValue = -1;
                for (int a = 0; a < values.length; a++) {
                    if (values[a].getWeight() > maxValue) {
                        maxValue = values[a].getWeight();
                        maxIndex = a;
                    }
                }
                if (maxIndex != -1) {
                    logger.info(maxValue + " " + values[maxIndex].getKey());
                    model.put("test_4_weight", maxValue);
                    model.put("test_4_result", new AnimePanel(values[maxIndex].getKey().split(":")[0],
                            values[maxIndex].getKey().split(":")[1], values[maxIndex].getKey().split(":")[2]));

                    model.put("test_5_time", "" + (Integer.valueOf(values[maxIndex].getKey().split(":")[2]) / (24 * 60))
                            + ":" + (Integer.valueOf(values[maxIndex].getKey().split(":")[2]) % (24)));
                } else {
                    logger.info("maxIndex is -1");
                }
            }
        } catch (final SQLException | URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }

    public static void findMatchingImageDataBruteForce(final Map<String, Object> model, final int[][][] array) {
        try (Connection connection = DatabaseManager.getConnection()) {
            final Statement stmt = connection.createStatement();

            AnimePanel result;

            final String findMatchingImageDataBruteForce = ScriptCreator.findMatchingImageDataBruteForce(array);
            logger.info(findMatchingImageDataBruteForce, BOOL_SCRIPT);

            final ResultSet rs = stmt.executeQuery(findMatchingImageDataBruteForce);

            if (rs.next()) {
                logger.info("Test 5: Found");

                logger.info("matching name:" + rs.getString("name") + " " + rs.getString("episode") + " "
                                + rs.getString("panel") + "@" + ((Integer.valueOf(rs.getString("panel")) / (24 * 60)) + ":"
                                + (Integer.valueOf(rs.getString("panel")) % (24))),
                        BOOL_MATCHING_NAME);

                result = new AnimePanel(rs.getString("name"), rs.getInt(2), rs.getInt(3));
                model.put("test_5_boolean", true);
                model.put("test_5_result", result);
                model.put("test_5_time", "" + (result.getPanel() / (24 * 60)) + ":" + (result.getPanel() % (24)));
            } else {
                logger.info("TEST 5: No match found");
            }
        } catch (final SQLException | URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }
}
