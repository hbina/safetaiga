package com.akarin.webapp.imageprocessing;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageGlobalDifference {
    public static int[][][] getGlobalDifferenceBinary(final BufferedImage givenImage) {

        final int width = givenImage.getWidth(); // Y-axis
        final int height = givenImage.getHeight(); // X-axis

        final int[][][] result = new int[height][width][3];
        int globalSum = 0;

        for (int y = 0; y < height; y++) { // y-axis
            for (int x = 0; x < width; x++) { // x-axis
                final Color colorAtXY = new Color(givenImage.getRGB(x, y));
                globalSum += (colorAtXY.getRed() + colorAtXY.getGreen() + colorAtXY.getBlue());

                result[y][x][0] = colorAtXY.getRed();
                result[y][x][1] = colorAtXY.getGreen();
                result[y][x][2] = colorAtXY.getBlue();
            }
        }

        final int globalAverage = globalSum / (width * height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final int localValue = result[y][x][0] + result[y][x][1] + result[y][x][2];
                if (localValue < globalAverage) {
                    for (int a = 0; a < result[0][0].length; a++) {
                        result[y][x][a] = ImageProcessingTools.MIN_RGB_VALUE;
                    }
                } else {
                    for (int a = 0; a < result[0][0].length; a++) {
                        result[y][x][a] = ImageProcessingTools.MAX_RGB_VALUE;
                    }
                }
            }
        }
        return result;
    }

    public static int[][][] getGlobalDifferenceBinaryRgb(final BufferedImage givenImage) {
        final int width = givenImage.getWidth(); // Y-axis
        final int height = givenImage.getHeight(); // X-axis

        final int[][][] result = new int[height][width][3];
        final int[] RGBglobalSum = new int[]{0, 0, 0};

        Color colorAtXY;

        for (int y = 0; y < height; y++) { // y-axis
            for (int x = 0; x < width; x++) { // x-axis
                colorAtXY = new Color(givenImage.getRGB(x, y));
                RGBglobalSum[0] += colorAtXY.getRed();
                RGBglobalSum[1] += colorAtXY.getGreen();
                RGBglobalSum[2] += colorAtXY.getBlue();
            }
        }

        final int[] globalAverage = new int[]{RGBglobalSum[0] / (width * height),
                RGBglobalSum[1] / (width * height), RGBglobalSum[2] / (width * height)};

        for (int y = 0; y < height; y++) { // y-axis
            for (int x = 0; x < width; x++) { // x-axis
                colorAtXY = new Color(givenImage.getRGB(x, y));
                if (colorAtXY.getRed() < globalAverage[0]) {
                    result[y][x][0] = ImageProcessingTools.MIN_RGB_VALUE;
                } else {
                    result[y][x][0] = ImageProcessingTools.MAX_RGB_VALUE;
                }
                if (colorAtXY.getGreen() < globalAverage[1]) {
                    result[y][x][1] = ImageProcessingTools.MIN_RGB_VALUE;
                } else {
                    result[y][x][1] = ImageProcessingTools.MAX_RGB_VALUE;
                }
                if (colorAtXY.getBlue() < globalAverage[2]) {
                    result[y][x][2] = ImageProcessingTools.MIN_RGB_VALUE;
                } else {
                    result[y][x][2] = ImageProcessingTools.MAX_RGB_VALUE;
                }
            }
        }
        return result;

    }

    public static int[][][] getGlobalDifference(final BufferedImage givenImage) {
        final int width = givenImage.getWidth(); // X-axis
        final int height = givenImage.getHeight(); // Y-axis

        final int[][][] result = new int[height][width][3];
        float globalSumRed = 0, globalSumGreen = 0, globalSumBlue = 0;

        for (int y = 0; y < height; y++) { // y-axis
            for (int x = 0; x < width; x++) { // x-axis
                final Color colorAtXY = new Color(givenImage.getRGB(x, y));
                globalSumRed += colorAtXY.getRed();
                globalSumGreen += colorAtXY.getGreen();
                globalSumBlue += colorAtXY.getBlue();

                result[y][x][0] = colorAtXY.getRed();
                result[y][x][1] = colorAtXY.getGreen();
                result[y][x][2] = colorAtXY.getBlue();
            }
        }

        final int globalAverageRed = Math.round(globalSumRed / (width * height));
        final int globalAverageGreen = Math.round(globalSumGreen / (width * height));
        final int globalAverageBlue = Math.round(globalSumBlue / (width * height));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result[y][x][0] = result[y][x][0] - globalAverageRed;
                result[y][x][1] = result[y][x][1] - globalAverageGreen;
                result[y][x][2] = result[y][x][2] - globalAverageBlue;

                // Filter out negatives
                if (result[y][x][0] < 0) {
                    result[y][x][0] = 0;
                }

                if (result[y][x][1] < 0) {
                    result[y][x][1] = 0;
                }
                if (result[y][x][2] < 0) {
                    result[y][x][2] = 0;
                }
            }
        }

        return result;
    }
}
