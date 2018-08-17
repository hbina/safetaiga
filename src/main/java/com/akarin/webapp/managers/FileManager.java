package com.akarin.webapp.managers;

import com.akarin.webapp.util.Tools;
import com.akarin.webapp.imageprocessing.ImageProcessingTools;

import java.io.*;

public class FileManager {

    private static boolean logging = true;

    public static String readFile(final String filename) throws IOException {

        String readString = "";
        final BufferedReader br = new BufferedReader(new FileReader(filename));
        String sCurrentLine;

        while ((sCurrentLine = br.readLine()) != null) {
            readString += sCurrentLine;
        }

        // always close file reader
        br.close();

        return readString;
    }

    public static int[][][] parseIntegerPartitionTextOutput(final String filename) throws IOException {

        final BufferedReader br = new BufferedReader(new FileReader(filename));
        String currentLine = "";
        String numberString = "";
        final int[][][] partitionArrayRGB = new int[ImageProcessingTools.DIVISOR_VALUE][ImageProcessingTools.DIVISOR_VALUE][3];
        int x = 0;
        int y = 0;
        int z = 0;
        int xInt = 0; // xLong is a counter to how many integers we have
        // iterated in the string
        while ((currentLine = br.readLine()) != null) { // y-axis of text
            Tools.coutln(currentLine, false);
            final char[] currentLineCharArray = currentLine.toCharArray();
            for (int a = 0; a < currentLineCharArray.length; a++) { // x-axis of
                // text
                if (currentLineCharArray[a] == ' ') {
                    z = xInt % 3;
                    partitionArrayRGB[y][x][z] = Integer.valueOf(numberString);
                    numberString = "";
                    xInt++;
                    if (z == 2) {
                        x++;
                    }
                } else {
                    numberString += currentLineCharArray[a];
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
        String currentLine = "";
        String numberString = "";
        final float[][][] partitionArrayRGB = new float[ImageProcessingTools.DIVISOR_VALUE][ImageProcessingTools.DIVISOR_VALUE][3];
        int x = 0;
        int y = 0;
        int z = 0;
        int xInt = 0; // xLong is a counter to how many integers we have
        // iterated in the string
        while ((currentLine = br.readLine()) != null) { // y-axis of text
            Tools.coutln(currentLine, false);
            final char[] currentLineCharArray = currentLine.toCharArray();
            for (int a = 0; a < currentLineCharArray.length; a++) { // x-axis of
                // text
                if (currentLineCharArray[a] == ' ') {
                    z = xInt % 3;
                    partitionArrayRGB[y][x][z] = Float.valueOf(numberString);
                    numberString = "";
                    xInt++;
                    if (z == 2) {
                        x++;
                    }
                } else {
                    numberString += currentLineCharArray[a];
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
        if (logging) {
            Tools.coutln("\nFROM:WriteFile:START:writeStringToFile");
            Tools.coutln("writing to:" + pathFile);
            FileWriter write;
            try {
                write = new FileWriter(pathFile);
                final PrintWriter print_line = new PrintWriter(write);
                print_line.printf("%s", text);
                print_line.close();
            } catch (final IOException e) {
                Tools.coutln("FAILURE WRITING FILE" + "\n" + "pathFile:" + pathFile + "\n" + "text:" + text);
                Tools.coutln("message" + e.getMessage());
            }
            Tools.coutln("END:writeStringToFile\n");
        }
    }

    public static void writeTripleArrayToString(final int[][][] tripleArray, final String pathFile) {
        Tools.coutln("writing to:" + pathFile);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(pathFile)));
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
        } catch (final Exception e) {
            Tools.coutln(e.getMessage());
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeTripleArrayToString(final float[][][] partitionArrayRGB, final String pathFile) {
        Tools.coutln("writing to:" + pathFile);
        BufferedWriter writer = null;
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
        } catch (final Exception e) {
            Tools.coutln(e.getMessage());
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}