package com.akarin.webapp.imageprocessing;

import java.awt.*;
import java.awt.image.BufferedImage;

class ImageEffects {
    public static int[][][] getSquareLocalAverage(final BufferedImage givenImage) {
        final int height = givenImage.getHeight();
        final int width = givenImage.getWidth();

        final int[][][] result = new int[height][width][3];
        final int range = 20;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;

                int countRed = 0;
                int countGreen = 0;
                int countBlue = 0;

                for (int yIterator = y - range; yIterator < y + range; yIterator++) {
                    for (int xIterator = x - range; xIterator < x + range; xIterator++) {
                        if ((yIterator >= 0) && (yIterator < height) && (xIterator >= 0) && (xIterator < width)) {

                            final Color colorAtXY = new Color(givenImage.getRGB(xIterator, yIterator));

                            sumRed += colorAtXY.getRed();
                            sumGreen += colorAtXY.getGreen();
                            sumBlue += colorAtXY.getBlue();

                            countRed++;
                            countGreen++;
                            countBlue++;
                        }

                    }
                }

                result[y][x][0] = sumRed / countRed;
                result[y][x][1] = sumGreen / countGreen;
                result[y][x][2] = sumBlue / countBlue;
            }

        }

        return result;
    }
    public static int[][][] getCircleLocalAverage(final BufferedImage givenImage) {
        final int height = givenImage.getHeight();
        final int width = givenImage.getWidth();

        final int[][][] result = new int[height][width][3];
        final int range = 20;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                final int sumRed = 0;
                final int sumGreen = 0;
                final int sumBlue = 0;

                final int countRed = 0;
                final int countGreen = 0;
                final int countBlue = 0;

                //result[y][x][0] = sumRed / countRed;
                //result[y][x][1] = sumGreen / countGreen;
                //result[y][x][2] = sumBlue / countBlue;
            }

        }

        return result;
    }
}
