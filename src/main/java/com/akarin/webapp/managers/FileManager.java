package com.akarin.webapp.managers;

import com.akarin.webapp.imageprocessing.ImageProcessingTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileManager {

    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

    public static String readFile(final String filename) throws IOException {

        StringBuilder readString = new StringBuilder();
        final BufferedReader br = new BufferedReader(new FileReader(filename));
        String sCurrentLine;

        while ((sCurrentLine = br.readLine()) != null) {
            readString.append(sCurrentLine);
        }

        // always close file reader
        br.close();

        return readString.toString();
    }

    public static int[][][] parseIntegerPartitionTextOutput(final String filename) throws IOException {

        final BufferedReader br = new BufferedReader(new FileReader(filename));
        String currentLine;
        StringBuilder numberString = new StringBuilder();
        final int[][][] partitionArrayRGB = new int[ImageProcessingTools.DIVISOR_VALUE][ImageProcessingTools.DIVISOR_VALUE][3];
        int x = 0;
        int y = 0;
        int z;
        int xInt = 0; // xLong is a counter to how many integers we have
        // iterated in the string
        while ((currentLine = br.readLine()) != null) { // y-axis of text
            logger.info(currentLine, false);
            final char[] currentLineCharArray = currentLine.toCharArray();
            for (char aCurrentLineCharArray : currentLineCharArray) { // x-axis of
                // text
                if (aCurrentLineCharArray == ' ') {
                    z = xInt % 3;
                    partitionArrayRGB[y][x][z] = Integer.valueOf(numberString.toString());
                    numberString = new StringBuilder();
                    xInt++;
                    if (z == 2) {
                        x++;
                    }
                } else {
                    numberString.append(aCurrentLineCharArray);
                }
            }
            xInt = 0;
            x = 0;
            y++;
        }

        // always close file reader
        br.close();

        return partitionArrayRGB;
    }

    public static float[][][] parseFloatPartitionTextOutput(final String filename) throws IOException {

        final BufferedReader br = new BufferedReader(new FileReader(filename));
        String currentLine;
        StringBuilder numberString = new StringBuilder();
        final float[][][] partitionArrayRGB = new float[ImageProcessingTools.DIVISOR_VALUE][ImageProcessingTools.DIVISOR_VALUE][3];
        int x = 0;
        int y = 0;
        int z;
        int xInt = 0; // xLong is a counter to how many integers we have
        // iterated in the string
        while ((currentLine = br.readLine()) != null) { // y-axis of text
            logger.info(currentLine, false);
            final char[] currentLineCharArray = currentLine.toCharArray();
            for (char aCurrentLineCharArray : currentLineCharArray) { // x-axis of
                // text
                if (aCurrentLineCharArray == ' ') {
                    z = xInt % 3;
                    partitionArrayRGB[y][x][z] = Float.valueOf(numberString.toString());
                    numberString = new StringBuilder();
                    xInt++;
                    if (z == 2) {
                        x++;
                    }
                } else {
                    numberString.append(aCurrentLineCharArray);
                }
            }
            xInt = 0;
            x = 0;
            y++;
        }

        // always close file reader
        br.close();

        return partitionArrayRGB;
    }

    public static void log(final String text, final String pathFile) {
        logger.info("\nFROM:WriteFile:START:writeStringToFile");
        logger.info("writing to:" + pathFile);
        FileWriter write;
        try {
            write = new FileWriter(pathFile);
            final PrintWriter printLine = new PrintWriter(write);
            printLine.printf("%s", text);
            printLine.close();
        } catch (final IOException e) {
            logger.warn("FAILURE WRITING FILE" + "\n" + "pathFile:" + pathFile + "\n" + "text:" + text);
            logger.warn(e.getMessage());
        }
        logger.info("END:writeStringToFile\n");
    }


    public static void writeTripleArrayToString(final int[][][] tripleArray, final String pathFile) {
        logger.info("writing to:" + pathFile);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathFile)));
            for (int a = 0; a < tripleArray.length; a++) {
                for (int b = 0; b < tripleArray[a].length; b++) {
                    for (int c = 0; c < tripleArray[a][b].length; c++) {
                        writer.write(tripleArray[a][b][c] + " ");
                    }
                }

                if (a < (tripleArray.length - 1)) {
                    writer.newLine();
                }
            }
            writer.close();
        } catch (final Exception e) {
            logger.warn(e.getMessage());

        }
    }

    public static void writeTripleArrayToString(final float[][][] partitionArrayRGB, final String pathFile) {
        logger.info("writing to:" + pathFile);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(new File(pathFile)));
            for (int a = 0; a < partitionArrayRGB.length; a++) {
                for (int b = 0; b < partitionArrayRGB[a].length; b++) {
                    for (int c = 0; c < partitionArrayRGB[a][b].length; c++) {
                        writer.write(partitionArrayRGB[a][b][c] + " ");
                    }
                }

                if (a < (partitionArrayRGB.length - 1)) {
                    writer.newLine();
                }
            }
            writer.close();
        } catch (final Exception e) {
            logger.warn(e.getMessage());
        }}
}