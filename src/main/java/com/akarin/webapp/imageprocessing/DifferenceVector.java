package com.akarin.webapp.imageprocessing;

import com.akarin.webapp.util.AkarinMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class DifferenceVector {

    private static final int VECTOR_DIFF_FLOOR_VALUE = 1;
    private static final Logger logger = LoggerFactory.getLogger(DifferenceVector.class);

    @SuppressWarnings("ConstantConditions")
    public static int[][][] getVectorizedRgbImage(BufferedImage givenImage) {
        int VECTOR_RANGE = 1;
        int VECTOR_SPAN = VECTOR_RANGE * 2 + 1;
        int height = givenImage.getHeight();
        int width = givenImage.getWidth();
        String debugString = "given image is " + width + "x" + height + System.lineSeparator();

        boolean needToFix = false;
        if ((width % VECTOR_SPAN) != 0) {
            needToFix = true;
            width -= width % VECTOR_SPAN;
        }
        if ((height % VECTOR_SPAN) != 0) {
            needToFix = true;
            height -= height % VECTOR_SPAN;
        }

        if (needToFix) {
            givenImage = ImageProcessingTools.resizeImage(givenImage, width, height);
        }

        int[][][] result = new int[height][width][3];
        PrintWriter writer;
        try {
            writer = new PrintWriter("the-file-name.txt", "UTF-8");
            for (int y = VECTOR_RANGE; y < height; y += VECTOR_SPAN) {
                for (int x = VECTOR_RANGE; x < width; x += VECTOR_SPAN) {
                    StringBuilder internalDebugStr = new StringBuilder("current image index:" + x + ", " + y + System.lineSeparator());
                    int[] diffVert = new int[]{-1, -1, -1};
                    int[] diffHorz = new int[]{-1, -1, -1};
                    int[] diffPost = new int[]{-1, -1, -1};
                    int[] diffNegt = new int[]{-1, -1, -1};

                    // check vertical
                    Color index00 = new Color(givenImage.getRGB(x - VECTOR_RANGE, y - VECTOR_RANGE));
                    Color index10 = new Color(givenImage.getRGB(x, y - VECTOR_RANGE));
                    Color index20 = new Color(givenImage.getRGB(x + VECTOR_RANGE, y - VECTOR_RANGE));

                    Color index01 = new Color(givenImage.getRGB(x - VECTOR_RANGE, y));
                    Color index11 = new Color(givenImage.getRGB(x, y));
                    Color index21 = new Color(givenImage.getRGB(x + VECTOR_RANGE, y));

                    Color index02 = new Color(givenImage.getRGB(x - VECTOR_RANGE, y + VECTOR_RANGE));
                    Color index12 = new Color(givenImage.getRGB(x, y + VECTOR_RANGE));
                    Color index22 = new Color(givenImage.getRGB(x + VECTOR_RANGE, y + VECTOR_RANGE));

                    internalDebugStr.append("COLOR VALUES:").append(System.lineSeparator()).append(index00.toString()).append(" ").append(index10.toString()).append(" ").append(index20.toString()).append(System.lineSeparator()).append(index01.toString()).append(" ").append(index11.toString()).append(" ").append(index21.toString()).append(System.lineSeparator()).append(index02.toString()).append(" ").append(index12.toString()).append(" ").append(index22.toString()).append(System.lineSeparator());

                    diffVert[0] = (int) (AkarinMath.euclideanNorm(new int[]{index10.getRed(), index12.getRed()}));
                    diffVert[1] = (int) (AkarinMath
                            .euclideanNorm(new int[]{index10.getGreen(), index12.getGreen()}));
                    diffVert[2] = (int) (AkarinMath.euclideanNorm(new int[]{index10.getBlue(), index12.getBlue()}));

                    // check horizontal
                    diffHorz[0] = (int) (AkarinMath.euclideanNorm(new int[]{index01.getRed(), index21.getRed()}));
                    diffHorz[1] = (int) (AkarinMath
                            .euclideanNorm(new int[]{index01.getGreen(), index21.getGreen()}));
                    diffHorz[2] = (int) (AkarinMath.euclideanNorm(new int[]{index01.getBlue(), index21.getBlue()}));

                    // check slanting positive
                    diffPost[0] = (int) (AkarinMath.euclideanNorm(new int[]{index02.getRed(), index20.getRed()}));
                    diffPost[1] = (int) (AkarinMath
                            .euclideanNorm(new int[]{index02.getGreen(), index20.getGreen()}));
                    diffPost[2] = (int) (AkarinMath.euclideanNorm(new int[]{index02.getBlue(), index20.getBlue()}));

                    // check slanting negative
                    diffNegt[0] = (int) (AkarinMath.euclideanNorm(new int[]{index00.getRed(), index22.getRed()}));
                    diffNegt[1] = (int) (AkarinMath
                            .euclideanNorm(new int[]{index00.getGreen(), index22.getGreen()}));
                    diffNegt[2] = (int) (AkarinMath.euclideanNorm(new int[]{index00.getBlue(), index22.getBlue()}));

                    int[][] maximVector = new int[][]{
                            {VECTOR_DIFF_FLOOR_VALUE, VECTOR_DIFF_FLOOR_VALUE, VECTOR_DIFF_FLOOR_VALUE}, diffVert,
                            diffHorz, diffPost, diffNegt};

                    int[] maxIndex = new int[]{0, 0, 0};
                    for (int a = 1; a < maximVector.length; a++) {
                        for (int b = 0; b < maximVector[a].length; b++) {
                            if (maximVector[a][b] > maximVector[maxIndex[b]][b]) {
                                maxIndex[b] = a;
                            }
                        }
                    }

                    internalDebugStr.append("maxIndex:").append(Arrays.toString(maxIndex)).append(System.lineSeparator());

                    for (int a = -VECTOR_RANGE; a <= VECTOR_RANGE; a++) {
                        for (int b = -VECTOR_RANGE; b <= VECTOR_RANGE; b++) {
                            internalDebugStr.append("y:").append(y).append(" a:").append(a).append(" x:").append(x).append(" b:").append(b).append(System.lineSeparator());
                            for (int c = 0; c < 3; c++) {
                                switch (maxIndex[c]) {
                                    case 0:
                                        internalDebugStr = new StringBuilder("tangent vector is null" + System.lineSeparator());
                                        if ((a == 0) && (b == 0)) {
                                            result[y + a][x + b][c] = ImageProcessingTools.MAX_RGB_VALUE;
                                        } else {
                                            result[y + a][x + b][c] = ImageProcessingTools.MIN_RGB_VALUE;
                                        }
                                        break;
                                    case 1:
                                        internalDebugStr.append("tangent vector is horizontal").append(System.lineSeparator());
                                        if (a == 0) {
                                            result[y + a][x + b][c] = ImageProcessingTools.MAX_RGB_VALUE;
                                        } else {
                                            result[y + a][x + b][c] = ImageProcessingTools.MIN_RGB_VALUE;
                                        }
                                        break;
                                    case 2:
                                        internalDebugStr.append("tangent vector is vertical").append(System.lineSeparator());
                                        if (b == 0) {
                                            result[y + a][x + b][c] = ImageProcessingTools.MAX_RGB_VALUE;
                                        } else {
                                            result[y + a][x + b][c] = ImageProcessingTools.MIN_RGB_VALUE;
                                        }

                                        break;
                                    case 3:
                                        internalDebugStr.append("tangent vector is negative").append(System.lineSeparator());
                                        if (a == b) {
                                            result[y + a][x + b][c] = ImageProcessingTools.MAX_RGB_VALUE;
                                        } else {
                                            result[y + a][x + b][c] = ImageProcessingTools.MIN_RGB_VALUE;
                                        }
                                        break;
                                    case 4:
                                        internalDebugStr.append("tangent vector is positive").append(System.lineSeparator());
                                        if ((a + b) == 0) {
                                            result[y + a][x + b][c] = ImageProcessingTools.MAX_RGB_VALUE;
                                        } else {
                                            result[y + a][x + b][c] = ImageProcessingTools.MIN_RGB_VALUE;
                                        }
                                        break;
                                }
                            }
                        }
                    }
                    writer.print(internalDebugStr);
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
        }
        return result;
    }
}
