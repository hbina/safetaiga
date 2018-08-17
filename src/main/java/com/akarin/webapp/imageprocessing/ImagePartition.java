package com.akarin.webapp.imageprocessing;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePartition {

    public static int[][][] getPartitionArray(final BufferedImage givenImage) {
        final int width = givenImage.getWidth();
        final int height = givenImage.getHeight();

        final int blockSizeX = width / ImageProcessingTools.DIVISOR_VALUE;
        final int blockSizeY = height / ImageProcessingTools.DIVISOR_VALUE;

        // Array for the average values of partitioned images
        final int[][][] result = new int[ImageProcessingTools.DIVISOR_VALUE][ImageProcessingTools.DIVISOR_VALUE][3];

        // Variables for iterating through the image array
        int blockStartX = 0;
        int blockStartY = 0;
        final int blockCardinality = blockSizeX * blockSizeY;

        // Placeholder variables when iterating the image
        int partitionTotalValueRed = 0;
        int partitionTotalValueGreen = 0;
        int partitionTotalValueBlue = 0;

        for (int a = 0; a < ImageProcessingTools.DIVISOR_VALUE; a++) { // Y-axis
            for (int b = 0; b < ImageProcessingTools.DIVISOR_VALUE; b++) { // X-axis
                // do stuff in the partition
                for (int c = 0; c < blockSizeY; c++) { // Y-axis
                    for (int d = 0; d < blockSizeX; d++) { // X-axis
                        final Color colorAtXY = new Color(givenImage.getRGB(d + blockStartX, c + blockStartY));
                        partitionTotalValueRed += colorAtXY.getRed();
                        partitionTotalValueGreen += colorAtXY.getGreen();
                        partitionTotalValueBlue += colorAtXY.getBlue();
                    }
                }

                // reaching here means we are done with a block

                // now assigning the average to the partitionArrays
                result[b][a][0] = partitionTotalValueRed / blockCardinality;
                result[b][a][1] = partitionTotalValueGreen / blockCardinality;
                result[b][a][2] = partitionTotalValueBlue / blockCardinality;

                // reset partitionTotalValues
                partitionTotalValueRed = 0;
                partitionTotalValueGreen = 0;
                partitionTotalValueBlue = 0;

                // move to the next X block
                blockStartX += blockSizeX;
            }

            // move to the next Y block
            blockStartY += blockSizeY;

            // reset blockStartX
            blockStartX = 0;
        }
        return result;
    }

    public static BufferedImage getPartitionedBufferedImage(final int[][][] givenArray) {
        final BufferedImage bufferedImage = new BufferedImage(ImageProcessingTools.IMAGE_WIDTH,
                ImageProcessingTools.IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);

        // Parse required information about the image
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();

        final int blockSizeX = width / ImageProcessingTools.DIVISOR_VALUE;
        final int blockSizeY = height / ImageProcessingTools.DIVISOR_VALUE;

        // Variables for iterating through the image array
        int blockStartX = 0;
        int blockStartY = 0;

        // Assign RGB color to the new image
        for (int a = 0; a < ImageProcessingTools.DIVISOR_VALUE; a++) { // Y-axis
            for (int b = 0; b < ImageProcessingTools.DIVISOR_VALUE; b++) { // X-axis
                // do stuff in the partition
                for (int c = 0; c < blockSizeY; c++) { // Y-axis
                    for (int d = 0; d < blockSizeX; d++) { // X-axis
                        final Color newColor = new Color(givenArray[b][a][0], givenArray[b][a][1], givenArray[b][a][2]);
                        bufferedImage.setRGB((d + blockStartX), (c + blockStartY), newColor.getRGB());
                    }
                }
                // move to the next X block
                blockStartX += blockSizeX;
            }
            // move to the next Y block
            blockStartY += blockSizeY;
            // reset blockStartX
            blockStartX = 0;
        }
        return bufferedImage;
    }
}
