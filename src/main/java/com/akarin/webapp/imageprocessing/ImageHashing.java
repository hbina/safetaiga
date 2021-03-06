package com.akarin.webapp.imageprocessing;

import com.akarin.webapp.structure.IntegerPair;
import com.akarin.webapp.util.Tools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageHashing {

    public static IntegerPair[][] getRGBHistogram(final BufferedImage bufferedImage) {
        final IntegerPair[][] histogram = new IntegerPair[256][3];

        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                final Color colorAtXY = new Color(bufferedImage.getRGB(x, y));
                if (histogram[colorAtXY.getRed()][0] == null) {
                    histogram[colorAtXY.getRed()][0] = new IntegerPair(0, 1);
                } else {
                    histogram[colorAtXY.getRed()][0].b++;
                }

                if (histogram[colorAtXY.getGreen()][1] == null) {
                    histogram[colorAtXY.getGreen()][1] = new IntegerPair(0, 1);
                } else {
                    histogram[colorAtXY.getGreen()][1].b++;
                }

                if (histogram[colorAtXY.getBlue()][2] == null) {
                    histogram[colorAtXY.getBlue()][2] = new IntegerPair(0, 1);
                } else {
                    histogram[colorAtXY.getBlue()][2].b++;
                }
            }
        }

        return histogram;
    }

    public static String basicHistogramHash(final IntegerPair[][] histogram) {
        int sum = 0;
        for (IntegerPair[] aHistogram : histogram) {
            if (aHistogram[0] != null) {
                sum += aHistogram[0].b;
            }
            if (aHistogram[1] != null) {
                sum += aHistogram[1].b;
            }
            if (aHistogram[2] != null) {
                sum += aHistogram[2].b;
            }
        }

        return Tools.toSixtyTwoRadix(sum);
    }

    public static int[] horizontalBinaryHash(final int[][][] array) {
        final int[] hashes = new int[3];
        for (int y = 0; y < array.length; y++) {
            for (int z = 0; z < array[0][0].length; z++) {
                if (array[y][0][z] > ImageProcessingTools.MAX_RGB_VALUE / 2) {
                    hashes[z] += Math.pow((float) 2, (float) (y + 1));
                }
            }
        }

        return hashes;
    }

    public static int[] partitionHash(final int[][][] array) {
        final int[] hashes = new int[3];

        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                for (int z = 0; z < array[y][x].length; z++) {
                    if (array[y][x][z] > ImageProcessingTools.MAX_RGB_VALUE) {
                        array[y][x][z] = ImageProcessingTools.MAX_RGB_VALUE;
                    }
                    // 15 radix
                    final int currentValue = (array[y][x][z] / 25) * ((int) (Math.pow(10f, (float) ((y * 3) + x))));
                    hashes[z] += currentValue;
                }
            }
        }
        return hashes;
    }

    public static int[] distinctPartitionHash(final int[][][] array) {
        final int[] hashes = new int[array.length * array[0].length];
        int tmpValue;

        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                for (int z = 0; z < array[y][x].length; z++) {
                    if (array[y][x][z] > ImageProcessingTools.MAX_RGB_VALUE) {
                        tmpValue = ImageProcessingTools.MAX_RGB_VALUE;
                    } else {
                        tmpValue = array[y][x][z];
                    }
                    hashes[z] += (((tmpValue) / 25) * ((int) (Math.pow(10f, (float) ((y * 3) + x)))));
                }
            }
        }
        return hashes;
    }
}
