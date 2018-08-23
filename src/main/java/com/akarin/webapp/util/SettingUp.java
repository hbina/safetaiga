package com.akarin.webapp.util;

import com.akarin.webapp.databases.ImageProcessingDb;
import com.akarin.webapp.databases.TextboardDb;
import com.akarin.webapp.imageprocessing.ImageGlobalDifference;
import com.akarin.webapp.imageprocessing.ImageHashing;
import com.akarin.webapp.imageprocessing.ImagePartition;
import com.akarin.webapp.imageprocessing.ImageProcessingTools;
import com.akarin.webapp.managers.AkarinLogging;
import com.akarin.webapp.managers.DatabaseManager;
import com.akarin.webapp.structure.AnimeObject;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SettingUp {

    private static final AnimeObject[] animeArray = new AnimeObject[]{new AnimeObject("eureka", 50)};
    private static final Logger logger = LoggerFactory.getLogger(SettingUp.class);

    public static void insertPartitionDumpToDatabase() {

        String insertScript = "";
        int[][][] tripleArray;

        logger.info("beginning to insert " + animeArray.length + " anime into the database");

        for (int animeNumber = 0; animeNumber < animeArray.length; animeNumber++) {
            logger.info("animeNumber:" + animeNumber);
            try {
                final int[] tmpPanels = new int[animeArray[animeNumber].getNumberOfEpisodes()];
                for (int a = 1; a <= animeArray[animeNumber].getNumberOfEpisodes(); a++) {
                    tmpPanels[a - 1] = Integer.valueOf(AkarinLogging.readFile(
                            "dev_output/description/" + animeArray[animeNumber].getName() + "_" + a + ".txt"));
                }
                animeArray[animeNumber].setPanels(tmpPanels);
            } catch (final IOException e) {
                logger.info("FAIL READING DESCRIPTION TEXT");
                logger.info(e.getMessage());
            }
            for (int episodeNumber = 1; episodeNumber <= animeArray[animeNumber]
                    .getNumberOfEpisodes(); episodeNumber++) {
                for (int panelNumber = 0; panelNumber < animeArray[animeNumber].getPanels()[episodeNumber
                        - 1]; panelNumber++) {
                    try (Connection connection = DatabaseManager.getConnection()) {

                        final Statement stmt = connection.createStatement();

                        tripleArray = AkarinLogging.parseIntegerPartitionTextOutput("dev_output/text/"
                                + animeArray[animeNumber].getName() + "/" + animeArray[animeNumber].getName() + "_"
                                + episodeNumber + "_" + panelNumber + ".txt");

                        insertScript = ScriptCreator.insertIntoImagedbAnimeRgbInteger(animeArray[animeNumber].getName(),
                                episodeNumber, panelNumber, tripleArray);

                        logger.info("Executing script:" + insertScript);
                        stmt.executeUpdate(insertScript);

                    } catch (final IOException | URISyntaxException e) {
                        logger.info("id:" + panelNumber);
                        logger.info(e.getMessage());
                    } catch (final SQLException e) {
                        logger.info("id:" + panelNumber);
                        logger.info("query:" + insertScript);
                        logger.info(e.getMessage());
                    }
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void createImageInfo() {
        try {
            final File DEV_OUTPUT_IMAGES_OUTPUT_PARTITION = new File("dev_output/images/output/partition");
            boolean isDirectoryExists = DEV_OUTPUT_IMAGES_OUTPUT_PARTITION.exists();
            if (!isDirectoryExists) {
                isDirectoryExists = DEV_OUTPUT_IMAGES_OUTPUT_PARTITION.mkdirs();
            }
            if (isDirectoryExists) {
                logger.info("the directory " + DEV_OUTPUT_IMAGES_OUTPUT_PARTITION.getPath() + " was created");
            }

            String animeName;
            for (AnimeObject anAnimeArray : animeArray) {
                animeName = anAnimeArray.getName();
                for (int episode = 1; episode <= anAnimeArray.getNumberOfEpisodes(); episode++) {
                    final Java2DFrameConverter frameConverter = new Java2DFrameConverter();

                    int frameIterator = 0; // the frame iterator
                    int panelIterator = 0; // the panel iterator
                    BufferedImage image; // the image
                    Frame frame;

                    FFmpegFrameGrabber g = new FFmpegFrameGrabber("videos/" + animeName + "/" + animeName + "_" + episode + ".mkv");
                    g.start();

                    while ((frame = g.grabImage()) != null) {
                        if ((frameIterator % ImageProcessingTools.FRAME_SKIP) == 0) {
                            image = frameConverter.getBufferedImage(frame);
                            image = ImageProcessingTools.resizeImage(image);

                            if (Partition.activeBool) {
                                Partition.tripleArray = ImagePartition.getPartitionArray(image);

                                if (Partition.writeToDatabase) {
                                    logger.info(animeName + " " + episode + " " + frameIterator);
                                    ImageProcessingDb.insertPartitionHash(animeName, episode, frameIterator,
                                            ImageHashing.partitionHash(Partition.tripleArray));
                                }

                                if (Partition.writeLogBool) {
                                    Partition.imageDir = "dev_output/images/output/partition/" + animeName + "/"
                                            + animeName + "_" + episode + "_" + panelIterator + ".png";
                                    Partition.textDir = "dev_output/text/partition/" + animeName + "/" + animeName + "_"
                                            + episode + "_" + panelIterator + ".txt";
                                }
                            }

                            if (GlobalDifference.activeBool) {
                                GlobalDifference.tripleArray = ImageGlobalDifference.getGlobalDifference(image);

                                if (GlobalDifference.writeLogBool) {
                                    GlobalDifference.imageDir = "dev_output/images/output/globaldifference/" + animeName
                                            + "/" + animeName + "_" + episode + "_" + panelIterator + ".png";
                                    GlobalDifference.textDir = "dev_output/text/globaldifference/" + animeName + "/"
                                            + animeName + "_" + episode + "_" + panelIterator + ".txt";
                                }
                            }

                            if (BasicHistogramHash.activeBool) {
                                try (Connection connection = DatabaseManager.getConnection()) {
                                    Statement statement = connection.createStatement();
                                    statement.executeUpdate("INSERT INTO imagedb_test (hash) VALUES ('"
                                            + ImageHashing.basicHistogramHash(ImageHashing.getRGBHistogram(image))
                                            + "');");
                                } catch (final SQLException | URISyntaxException e) {
                                    logger.warn(e.getMessage());
                                }
                            }

                            if (CheckPanelDifference.activeBool) {

                                if (panelIterator == 0) {
                                    CheckPanelDifference.oldArray = ImageProcessingTools
                                            .getArrayFromBufferedImage(image);
                                    CheckPanelDifference.panelDifferenceCountArray = new int[CheckPanelDifference.oldArray.length][CheckPanelDifference.oldArray[0].length][3];
                                } else {
                                    CheckPanelDifference.newArray = ImageProcessingTools
                                            .getArrayFromBufferedImage(image);
                                    CheckPanelDifference.panelDifferenceArray = ImageProcessingTools
                                            .checkArrayDifference(CheckPanelDifference.oldArray,
                                                    CheckPanelDifference.newArray);
                                    for (int y = 0; y < CheckPanelDifference.oldArray.length; y++) {
                                        for (int x = 0; x < CheckPanelDifference.oldArray[y].length; x++) {
                                            for (int z = 0; z < CheckPanelDifference.oldArray[y][x].length; z++) {
                                                if (CheckPanelDifference.panelDifferenceArray[y][x][z]) {
                                                    CheckPanelDifference.panelDifferenceCountArray[y][x][z]++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            panelIterator++;
                        }
                        frameIterator++;
                    }
                    AkarinLogging.log("" + panelIterator, "dev_output/description/" + animeName + "_" + episode + ".txt");
                    g.stop();
                }
            }
        } catch (final IOException e) {
            logger.warn(e.getMessage());
        }

    }

    public static void prepareDatabase() {
        try {
            TextboardDb.createTableBoards();
            TextboardDb.createTableThreads();
            TextboardDb.createTablePosts();
        } catch (SQLException | URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }

    static class CheckPanelDifference {
        static final boolean activeBool = false;
        public static boolean writeLogBool = false;

        static int[][][] oldArray;
        static int[][][] newArray;
        static int[][][] panelDifferenceCountArray;

        static boolean[][][] panelDifferenceArray;
    }

    static class Partition {
        static final boolean activeBool = true;
        static final boolean writeLogBool = false;
        static final boolean writeToDatabase = true;
        public static boolean printBool = false;

        static int[][][] tripleArray;

        static String imageDir;
        static String textDir;
    }

    static class GlobalDifference {
        static final boolean activeBool = false;
        static final boolean writeLogBool = false;

        static int[][][] tripleArray;

        static String imageDir;
        static String textDir;
    }

    static class BasicHistogramHash {
        static final boolean activeBool = false;
        public static boolean writeLogBool = false;
    }

    private static class globalAverageRGB {
        public static float[][][] average = new float[ImageProcessingTools.DIVISOR_VALUE][ImageProcessingTools.DIVISOR_VALUE][3];
    }
}
