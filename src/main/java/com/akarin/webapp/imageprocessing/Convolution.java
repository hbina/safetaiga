package com.akarin.webapp.imageprocessing;

import com.akarin.webapp.util.Tools;

public class Convolution {

    private static boolean BOOLEAN_DEBUGGING_CONVO = false;

    public static int[][][] convolute(final int[][][] sourceMatrix, final double scalar,
                                      final int[][] convolutionMatrix) {

        /**
         * Need to check if the input convolution matrix is an odd N x N matrix
         */

        // Check if convolution matrix sum up to 0
        int checkZero = 0;
        for (int a = 0; a < convolutionMatrix.length; a++) {
            for (int b = 0; b < convolutionMatrix[a].length; b++) {
                checkZero += convolutionMatrix[a][b];
            }
        }
        if (checkZero != 0) {
            throw new IllegalArgumentException("non-zero matrix");
        }
        final int colorCount = 3;
        final int[][][] tmpMatrix = new int[sourceMatrix.length][sourceMatrix[0].length][colorCount];
        for (int rgbIndex = 0; rgbIndex < colorCount; rgbIndex++) {
            for (int rowSourceIndex = 0; rowSourceIndex < sourceMatrix.length; rowSourceIndex++) {
                for (int colSourceIndex = 0; colSourceIndex < sourceMatrix[rowSourceIndex].length; colSourceIndex++) {
                    double tmpMatrixValue = 0.0f;
                    for (int rowConvIndex = 0; rowConvIndex < convolutionMatrix.length; rowConvIndex++) {
                        for (int colConvIndex = 0; colConvIndex < convolutionMatrix[rowConvIndex].length; colConvIndex++) {
                            final int effectiveRowConvIndex = rowSourceIndex + rowConvIndex
                                    - (convolutionMatrix.length / 2);
                            final int effectiveColConvIndex = colSourceIndex + colConvIndex
                                    - (convolutionMatrix[rowConvIndex].length / 2);
                            if (effectiveRowConvIndex >= 0 && effectiveRowConvIndex < sourceMatrix.length
                                    && effectiveColConvIndex >= 0
                                    && effectiveColConvIndex < sourceMatrix[rowSourceIndex].length) {
                                Tools.coutln("(double) sourceMatrix[" + effectiveRowConvIndex + "]["
                                        + effectiveColConvIndex + "][" + rgbIndex + "]:"
                                        + ((double) sourceMatrix[effectiveRowConvIndex][effectiveColConvIndex][rgbIndex])
                                        + " with "
                                        + (((double) sourceMatrix[effectiveRowConvIndex][effectiveColConvIndex][rgbIndex]
                                        * (double) convolutionMatrix[rowConvIndex][colConvIndex]))
                                        + " and therefore tmpMatrixValue:" + tmpMatrixValue, BOOLEAN_DEBUGGING_CONVO);
                                tmpMatrixValue += (((double) sourceMatrix[effectiveRowConvIndex][effectiveColConvIndex][rgbIndex]
                                        * (double) convolutionMatrix[rowConvIndex][colConvIndex]));
                            }
                        }
                    }
                    // colSourceIndex
                    Tools.coutln("tmpMatrixValue:" + tmpMatrixValue + " scalar:" + scalar + " result:"
                            + (tmpMatrixValue * scalar), BOOLEAN_DEBUGGING_CONVO);
                    tmpMatrix[rowSourceIndex][colSourceIndex][rgbIndex] = (int) (tmpMatrixValue * scalar);

                    // Limit check
                    if (tmpMatrix[rowSourceIndex][colSourceIndex][rgbIndex] < 0) {
                        tmpMatrix[rowSourceIndex][colSourceIndex][rgbIndex] = 0;
                    }
                    if (tmpMatrix[rowSourceIndex][colSourceIndex][rgbIndex] > 255) {
                        tmpMatrix[rowSourceIndex][colSourceIndex][rgbIndex] = 255;
                    }
                }
                // rowSourceIndex
            }
        }
        return tmpMatrix;
    }

    public static class ConvolutionMatrices {
        public static final int[][] EDGE_DETECTION_1 = new int[][]{{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
        public static final int[][] IDENTITY = new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        public static final int[][] EDGE_DETECTION_2 = new int[][]{{1, 0, -1}, {0, 0, 0}, {-1, 0, 1}};
        public static final int[][] EDGE_DETECTION_3 = new int[][]{{0, 1, 0}, {1, -4, 1}, {0, 1, 0}};
        public static final int[][] SHARPEN = new int[][]{{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
    }
}
