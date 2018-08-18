package com.akarin.webapp.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileManager {

    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);
    public static File IMAGES_INPUT_DIR;
    public static File TEXT_OUTPUT_PARTITION_DIR;
    public static File IMAGES_OUTPUT_RESIZED_DIR;
    public static File TEXT_OUTPUT_GLOBALDIFFERENCE_DIR;
    public static File IMAGES_OUTPUT_CONVOLUTION_DIR;

    public static void createFolders() {

        logger.info("Current dir:" + System.getProperty("user.dir"));
        File IMAGES_OTHER_DIR = new File("public" + System.getProperty("file.separator") + "images"
                + System.getProperty("file.separator") + "other");
        IMAGES_INPUT_DIR = new File("public" + System.getProperty("file.separator") + "images"
                + System.getProperty("file.separator") + "input");
        IMAGES_OUTPUT_RESIZED_DIR = new File("public" + System.getProperty("file.separator") + "images"
                + System.getProperty("file.separator") + "output" + System.getProperty("file.separator") + "resized");

        File IMAGES_OUTPUT_PARTITION_DIR = new File("public" + System.getProperty("file.separator") + "images"
                + System.getProperty("file.separator") + "output" + System.getProperty("file.separator") + "partition");
        File IMAGES_OUTPUT_GLOBALDIFFERENCE_DIR = new File("public" + System.getProperty("file.separator") + "images"
                + System.getProperty("file.separator") + "output/globaldifference");

        File IMAGES_OUTPUT_GLOBALDIFFERENCEBINARY_DIR = new File(
                "public" + System.getProperty("file.separator") + "images" + System.getProperty("file.separator")
                        + "output" + System.getProperty("file.separator") + "globaldifferencebinary");

        File IMAGES_OUTPUT_GLOBALDIFFERENCEBINARYRGB_DIR = new File(
                "public" + System.getProperty("file.separator") + "images" + System.getProperty("file.separator")
                        + "output" + System.getProperty("file.separator") + "globaldifferencebinaryRGB");

        File IMAGES_OUTPUT_MINIMIZEDGLOBALDIFFERENCEBINARY_DIR = new File("public" + System.getProperty("file.separator")
                + "images" + System.getProperty("file.separator") + "minimizedglobaldifferencebinary");

        File IMAGES_OUTPUT_HORIZONTALAVERAGERGB_DIR = new File("public" + System.getProperty("file.separator") + "images"
                + System.getProperty("file.separator") + "horizontalaveragergb");

        File IMAGES_OUTPUT_SQUARELOCALAVERAGE_DIR = new File("public" + System.getProperty("file.separator") + "images"
                + System.getProperty("file.separator") + "squarelocalaverage");

        IMAGES_OUTPUT_CONVOLUTION_DIR = new File("public" + System.getProperty("file.separator") + "images"
                + System.getProperty("file.separator") + "convolution");

        File[] files = new File[]{IMAGES_OTHER_DIR,
                IMAGES_OUTPUT_PARTITION_DIR,
                IMAGES_OUTPUT_RESIZED_DIR,
                IMAGES_INPUT_DIR,
                IMAGES_OUTPUT_GLOBALDIFFERENCE_DIR,
                IMAGES_OUTPUT_GLOBALDIFFERENCEBINARY_DIR,
                IMAGES_OUTPUT_GLOBALDIFFERENCEBINARYRGB_DIR,
                IMAGES_OUTPUT_MINIMIZEDGLOBALDIFFERENCEBINARY_DIR,
                IMAGES_OUTPUT_HORIZONTALAVERAGERGB_DIR,
                IMAGES_OUTPUT_SQUARELOCALAVERAGE_DIR,
                IMAGES_OUTPUT_CONVOLUTION_DIR};
        for (File p : files) {
            boolean isDirectoryCreated = p.exists();
            if (!isDirectoryCreated) {
                isDirectoryCreated = p.mkdir();
            }
            if (isDirectoryCreated) {
                logger.info("created the directory " + p.getPath());
            }
        }
    }
}
