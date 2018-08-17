package com.akarin.webapp.imageprocessing;

import java.awt.*;
import java.awt.image.BufferedImage;

class ImageDirectionAverage {
    public static int[][][] getHorizontalAverage(final BufferedImage givenImage) {
        final int height = givenImage.getHeight();
        final int width = givenImage.getWidth();

        final int[][][] result = new int[height][width][3];

        int localHorizontalSumRed;
        int localHorizontalSumGreen;
        int localHorizontalSumBlue;

        Color colorAtXY;

        for (int y = 0; y < height; y++) {
            localHorizontalSumRed = 0;
            localHorizontalSumGreen = 0;
            localHorizontalSumBlue = 0;

            // find the sum for the horizontal axis
            for (int x = 0; x < width; x++) {
                colorAtXY = new Color(givenImage.getRGB(x, y));
                localHorizontalSumRed += colorAtXY.getRed();
                localHorizontalSumGreen += colorAtXY.getGreen();
                localHorizontalSumBlue += colorAtXY.getBlue();
            }

            // get the horizontal average and allocate throughout X-axis
            for (int x = 0; x < width; x++) {
                result[y][x][0] = localHorizontalSumRed / width;
                result[y][x][1] = localHorizontalSumGreen / width;
                result[y][x][2] = localHorizontalSumBlue / width;
            }
        }

        return result;

    }
}
