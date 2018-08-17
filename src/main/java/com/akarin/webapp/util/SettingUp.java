package com.akarin.webapp.util;

import com.akarin.webapp.managers.DatabaseManager;
import com.akarin.webapp.managers.FileManager;
import com.akarin.webapp.structure.AnimeObject;
import com.akarin.webapp.imageprocessing.ImageGlobalDifference;
import com.akarin.webapp.imageprocessing.ImageHashing;
import com.akarin.webapp.imageprocessing.ImagePartition;
import com.akarin.webapp.imageprocessing.ImageProcessingTools;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SettingUp {

    public static AnimeObject[] animeArray = new AnimeObject[]{new AnimeObject("eureka", 50)};
    private static FFmpegFrameGrabber g;

    public static void insertPartitionDumpToDatabase() {

        String insertScript = "";
        int[][][] tripleArray;

        Tools.coutln("beginning to insert " + animeArray.length + " anime into the database");

        for (int animeNumber = 0; animeNumber < animeArray.length; animeNumber++) {
            Tools.coutln("animeNumber:" + animeNumber);
            try {
                final int[] tmpPanels = new int[animeArray[animeNumber].getNumberOfEpisodes()];
                for (int a = 1; a <= animeArray[animeNumber].getNumberOfEpisodes(); a++) {
                    tmpPanels[a - 1] = Integer.valueOf(FileManager.readFile(
                            "dev_output/description/" + animeArray[animeNumber].getName() + "_" + a + ".txt"));
                }
                animeArray[animeNumber].setPanels(tmpPanels);
            } catch (final IOException e) {
                Tools.coutln("FAIL READING DESCRIPTION TEXT");
                Tools.coutln(e.getMessage());
            }
            for (int episodeNumber = 1; episodeNumber <= animeArray[animeNumber]
                    .getNumberOfEpisodes(); episodeNumber++) {
                for (int panelNumber = 0; panelNumber < animeArray[animeNumber].getPanels()[episodeNumber
                        - 1]; panelNumber++) {
                    try (Connection connection = DatabaseManager.getConnection()) {

                        final Statement stmt = connection.createStatement();

                        tripleArray = FileManager.parseIntegerPartitionTextOutput("dev_output/text/"
                                + animeArray[animeNumber].getName() + "/" + animeArray[animeNumber].getName() + "_"
                                + episodeNumber + "_" + panelNumber + ".txt");

                        insertScript = ScriptCreator.insertIntoImagedbAnimeRgbInteger(animeArray[animeNumber].getName(),
                                episodeNumber, panelNumber, tripleArray);

                        Tools.coutln("Executing script:" + insertScript);
                        stmt.executeUpdate(insertScript);

                    } catch (final IOException e) {
                        Tools.coutln("id:" + panelNumber);
                        Tools.coutln(e.getMessage());
                    } catch (final SQLException e) {
                        Tools.coutln("id:" + panelNumber);
                        Tools.coutln("query:" + insertScript);
                        Tools.coutln(e.getMessage());
                    } catch (final URISyntaxException e) {
                        Tools.coutln("id:" + panelNumber);
                        Tools.coutln(e.getMessage());
                    }
                }
            }
        }
    }

    public static void createImageInfo() {
        try {
            /**
             * Create required folders
             */
            final File DEV_OUTPUT_IMAGES_OUTPUT_PARTITION = new File("dev_output/images/output/partition");
            DEV_OUTPUT_IMAGES_OUTPUT_PARTITION.mkdirs();

            String animeName = "";
            for (int animeNumber = 0; animeNumber < animeArray.length; animeNumber++) {
                animeName = animeArray[animeNumber].getName();
                for (int episode = 1; episode <= animeArray[animeNumber].getNumberOfEpisodes(); episode++) {
                    final Java2DFrameConverter frameConverter = new Java2DFrameConverter();

                    /**
                     * GLOBAL VARIABLES
                     */
                    int frameIterator = 0; // the frame iterator
                    int panelIterator = 0; // the panel iterator
                    BufferedImage image; // the image
                    Frame frame;

                    g = new FFmpegFrameGrabber("videos/" + animeName + "/" + animeName + "_" + episode + ".mkv");
                    g.start();

                    while ((frame = g.grabImage()) != null) {
                        if ((frameIterator % ImageProcessingTools.FRAME_SKIP) == 0) {
                            image = frameConverter.getBufferedImage(frame);
                            image = ImageProcessingTools.resizeImage(image);

                            if (Partition.activeBool) {
                                Partition.tripleArray = ImagePartition.getPartitionArray(image);

                                if (Partition.writeToDatabase) {
                                    Tools.coutln(animeName + " " + episode + " " + frameIterator);
                                    DatabaseManager.insertPartitionHash(animeName, episode, frameIterator,
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

                            /**
                             * BASIC HISTOGRAM HASHING
                             */
                            if (BasicHistogramHash.activeBool) {
                                try (Connection connection = DatabaseManager.getConnection()) {
                                    final Statement statement = connection.createStatement();
                                    statement.executeUpdate("INSERT INTO imagedb_test (hash) VALUES ('"
                                            + ImageHashing.basicHistogramHash(ImageHashing.getRGBHistogram(image))
                                            + "');");
                                } catch (final SQLException e) {
                                    e.printStackTrace();
                                } catch (final URISyntaxException e) {
                                    e.printStackTrace();
                                }
                            }

                            /**
                             * CHECK PANEL DIFFERENCE
                             */
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
                    FileManager.log("" + panelIterator, "dev_output/description/" + animeName + "_" + episode + ".txt");
                    g.stop();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    public static void prepareDatabase() {
        try {
            DatabaseManager.createTableBoards();
            DatabaseManager.createTableThreads();
            DatabaseManager.createTablePosts();
        } catch (SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static class CheckPanelDifference {
        public static boolean activeBool = false;
        public static boolean writeLogBool = false;

        public static int[][][] oldArray;
        public static int[][][] newArray;
        public static int[][][] panelDifferenceCountArray;

        public static boolean[][][] panelDifferenceArray;
    }

    public static class Partition {
        public static boolean activeBool = true;
        public static boolean writeLogBool = false;
        public static boolean writeToDatabase = true;
        public static boolean printBool = false;

        public static int[][][] tripleArray;

        public static String imageDir;
        public static String textDir;
    }

    public static class GlobalDifference {
        public static boolean activeBool = false;
        public static boolean writeLogBool = false;

        public static int[][][] tripleArray;

        public static String imageDir;
        public static String textDir;
    }

    public static class BasicHistogramHash {
        public static boolean activeBool = false;
        public static boolean writeLogBool = false;
    }

    public static class globalAverageRGB {
        public static float[][][] average = new float[ImageProcessingTools.DIVISOR_VALUE][ImageProcessingTools.DIVISOR_VALUE][3];
    }
}
