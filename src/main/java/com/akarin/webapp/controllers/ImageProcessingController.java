package com.akarin.webapp.controllers;

import com.akarin.webapp.util.Reference;
import com.akarin.webapp.util.Tools;
import com.akarin.webapp.util.ViewUtil;
import com.akarin.webapp.imageprocessing.DifferenceVector;
import com.akarin.webapp.imageprocessing.ImageProcessingTools;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

//import java.akarin.akarin.util.ArrayList;

public class ImageProcessingController {

    public static Route serveImageUpload = new Route() {
        @Override
        public Object handle(final Request request, final Response response) {
            Tools.coutln("serveImageUpload");
            final Map<String, Object> model = new HashMap<String, Object>();
            /**
             * model.put("imagefile", "/images/other/image_placeholder.jpg");
             * model.put("imagemessage", "your uploaded image will replace the empty image
             * below:"); model.put("partitionArrayRGB", new
             * int[ImageProcessing.DIVISOR_VALUE][ImageProcessing.DIVISOR_VALUE][3]);
             **/
            return ViewUtil.render(request, model, Reference.Templates.IMAGE_PROCESSING, "Image Upload", "OK");
        }
    };

    public static Route handleImageUpload = new Route() {
        @Override
        public Object handle(final Request request, final Response response) throws Exception {
            Tools.coutln("handleImageUpload");
            final Map<String, Object> model = new HashMap<String, Object>();

            // Begin to evaluate time taken to process the image
            final long tStart = System.currentTimeMillis();

            /**
             * Get uploaded image file
             */
            final Path tempFile = Files.createTempFile(Tools.IMAGES_INPUT_DIR.toPath(), "", ".png");
            Tools.coutln("handleImageUpload:tempFile created at " + tempFile.toString());
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            try (InputStream input = request.raw().getPart("uploaded_file").getInputStream()) {
                Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (final Exception e) {
                e.printStackTrace();
                Tools.coutln("END:handleImageUpload" + System.lineSeparator());
                return ViewUtil.renderErrorMessage(request, e.getMessage(),
                        Reference.CommonStrings.LINK_IMAGEPROCESSING, Reference.Names.IMAGEPROCESSING);
            }

            /**
             * Prepare required variables
             */
            BufferedImage originalImage, resizedImage;
            BufferedImage vectorImage;
            int[][][] vectorArray;

            // Remove any file type post fix
            final String filename = tempFile.getFileName().toString().substring(0,
                    tempFile.getFileName().toString().length() - 4);

            // Files directory
            final String outputOriginalImage = Tools.IMAGES_INPUT_DIR.toPath() + System.getProperty("file.separator")
                    + filename + ".png";
            final String outputResizedImage = Tools.IMAGES_OUTPUT_RESIZED_DIR.toPath() + System.getProperty("file.separator")
                    + filename + ".png";
            final String outputConvolutionImage = Tools.IMAGES_OUTPUT_CONVOLUTION_DIR.toPath()
                    + System.getProperty("file.separator") + filename + ".png";

            originalImage = ImageIO.read(new File(outputOriginalImage));

            // Resize the image
            resizedImage = ImageProcessingTools.resizeImage(originalImage, 500, 500);

            // Save resized image for future inquiries
            ImageIO.write(resizedImage, "png", new File(outputResizedImage));

            // /**
            // * CONVOLUTION IMAGE
            // *
            // * Divide the image into several equally sized boxes and find the average RGB
            // * values for each box then assign them to the box
            // */
            // // Get resized image partitioning RGB array
            vectorArray = DifferenceVector.getVectorizedRgbImage(resizedImage);
            // convolutionArray = Convolution.convolute(convolutionArray, 1.0f,
            // Convolution.ConvolutionMatrices.EDGE_DETECTION_1);
            // // Partition the resized image based on the partitioning array
            vectorImage = ImageProcessingTools.getBufferedImageGivenArray(vectorArray);
            //
            // // Save the partitioned image for future inquiries
            ImageIO.write(vectorImage, "png", new File(outputConvolutionImage));

            /**
             * Original image
             */
            model.put("ORIGINAL_IMAGE_MESSAGE", "The original image, resized:");
            // 7 to remove the substring 'public/'
            Tools.coutln("Original image directory:" + outputOriginalImage);
            model.put("ORIGINAL_IMAGE_FILE", outputResizedImage.substring(7));
            /**
             * Partitioned image
             */
            model.put("CONVOLUTION_IMAGE_MESSAGE", "Convoluted image");
            // 7 to remove the substring 'public/'
            Tools.coutln("Convolution image directory:" + outputConvolutionImage);
            model.put("CONVOLUTION_IMAGE_FILE", outputConvolutionImage.substring(7));

            final long tEnd = System.currentTimeMillis();
            final long tDelta = tEnd - tStart;
            final double elapsedSeconds = tDelta / 1000.0;
            Tools.coutln("Image processing takes " + elapsedSeconds + " seconds.");

            return ViewUtil.render(request, model, Reference.Templates.IMAGE_UPLOAD, Reference.Names.IMAGEPROCESSING,
                    "OK");
        }
    };
}
