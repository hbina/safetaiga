package com.akarin.webapp.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageProcessingTools {

    /**
     * TODO: USE DATABASE TO ASSIGN THESE VALUES
     */
    public static final int IMAGE_WIDTH = 200;
    public static final int IMAGE_HEIGHT = 200;
    public static final int DIVISOR_VALUE = 3;
    public static final int BUFFER_VALUE = 5;
    public static final int TRIAL_VALUE = 1;
    public static final int FRAME_SKIP = 1;
    public static final int MIN_RGB_VALUE = 0;
    public static final int MAX_RGB_VALUE = 255;
    public static final int VECTOR_DIRECTIONS = 4;

    public static BufferedImage resizeImage(final BufferedImage originalImage, final int imageWidth,
                                            final int imageHeight) {

        final int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_RGB : originalImage.getType();
        final BufferedImage resizedImage = new BufferedImage(imageWidth, imageHeight, type);
        final Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, imageWidth, imageHeight, null);
        g.dispose();

        return resizedImage;
    }

    public static BufferedImage resizeImage(final BufferedImage originalImage) {
        return resizeImage(originalImage, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    public static void resizeImageWithHint(final String fileName) throws IOException {
        final BufferedImage originalImage = ImageIO.read(new File("images/input/" + fileName));

        final int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        final BufferedImage resizedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, type);

        final Graphics2D g = resizedImage.createGraphics();

        g.drawImage(originalImage, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ImageIO.write(resizedImage, "png", new File("images/input/resizedwithhint/" + fileName));
    }

    public static BufferedImage getBufferedImageGivenArray(final int[][][] givenArray) {
        final BufferedImage bufferedImage = new BufferedImage(givenArray[0].length, givenArray.length,
                BufferedImage.TYPE_INT_RGB);

        // Assign RGB color to the new image
        for (int y = 0; y < givenArray.length; y++) { // Y-axis
            for (int x = 0; x < givenArray[0].length; x++) { // X-axis
                final Color newColor = new Color(givenArray[y][x][0], givenArray[y][x][1], givenArray[y][x][2]);
                bufferedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return bufferedImage;
    }

    /**
     * Check if the int values in oldArray and newArray are the same, returns an
     * array of boolean of the same size. If the index (X,Y) != (X',Y') then it will
     * return true, returns false otherwise
     *
     * @param oldArray -- The old array to be checked
     * @param newArray -- The new array to be checked
     * @return Returns an double array of equality between old and new array
     */
    public static boolean[][][] checkArrayDifference(final int[][][] oldArray, final int[][][] newArray) {

        // check if the sizes of oldArray and newArray are identical
        if ((oldArray.length != newArray.length) || (oldArray[0].length != newArray[0].length)
                || (oldArray[0][0].length != newArray[0][0].length)) {
            throw new IllegalArgumentException("oldArray and newArray sizes are not the same.");
        }
        final boolean[][][] changeBool = new boolean[oldArray.length][oldArray[0].length][oldArray[0][0].length];
        for (int y = 0; y < oldArray.length; y++) { // Y-axis
            for (int x = 0; x < oldArray[y].length; x++) { // X-axis
                for (int z = 0; z < oldArray[y][x].length; z++) {
                    changeBool[y][x][z] = Math.abs(oldArray[y][x][z] - newArray[y][x][z]) > BUFFER_VALUE;
                }
            }
        }

        return changeBool;
    }

    public static int[][][] getArrayFromBufferedImage(final BufferedImage image) {

        final int[][][] array = new int[image.getHeight()][image.getWidth()][3];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                final Color colorAtXY = new Color(image.getRGB(x, y));
                array[y][x][0] = colorAtXY.getRed();
                array[y][x][1] = colorAtXY.getGreen();
                array[y][x][2] = colorAtXY.getBlue();
            }
        }

        return array;
    }

    /**
     * Convert a bufferedImage to bytes array
     *
     * @param bufferedImage -- BufferedImage to be converted
     * @return -- converted BufferedImage in its bytes array form
     */
    public static byte[] extractBytes(final BufferedImage bufferedImage) {
        // get DataBufferBytes from Raster
        final WritableRaster raster = bufferedImage.getRaster();
        final DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
        return (data.getData());
    }

    /**
     * Convert an imagefile to bytes array
     *
     * @param ImageName -- The name of the image file to be converted to bytes array
     * @return -- The bytes array of the given image file
     * @throws IOException -- if the file cannot be specified
     */
    public static byte[] extractBytes(final String ImageName) throws IOException {
        // open image
        final File imgPath = new File(ImageName);
        final BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        final WritableRaster raster = bufferedImage.getRaster();
        final DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        return (data.getData());
    }
}