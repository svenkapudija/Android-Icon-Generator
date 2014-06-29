package com.android.icon.generator.utils;

import org.imgscalr.Scalr;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

public class ImageUtils {

    public static BufferedImage setOpacity(BufferedImage image, int opacityPercentage) {
        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, opacityPercentage / 100.0f));
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();

        return image;
    }

    public static BufferedImage padImage(BufferedImage image, int padding) throws IOException {
        return Scalr.pad(image, padding, new Color(0, 0, 0, 0));
    }

    public static BufferedImage resizeImage(BufferedImage inputImage, int width, int height, boolean exactSize) throws IOException {
        boolean isLandscape = inputImage.getWidth() > inputImage.getHeight();

        BufferedImage outputImage = Scalr.resize(
                inputImage,
                Scalr.Method.QUALITY,
                isLandscape ? Scalr.Mode.FIT_TO_WIDTH : Scalr.Mode.FIT_TO_HEIGHT,
                width,
                height,
                Scalr.OP_ANTIALIAS
        );
        
        if(exactSize && (outputImage.getWidth() < width || outputImage.getHeight() < height)) {
            outputImage = resizeCanvas(outputImage, width, height);
        }

        return outputImage;
    }

    public static BufferedImage colorImage(BufferedImage image, Color color) {
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                pixels[0] = color.getRed();
                pixels[1] = color.getGreen();
                pixels[2] = color.getBlue();
                raster.setPixel(xx, yy, pixels);
            }
        }
        return image;
    }

    public static BufferedImage resizeCanvas(BufferedImage image, int width, int height) {
        int x = 0;
        int y = 0;

        if(image.getWidth() < width) {
            x = (width-image.getWidth())/2;
        }

        if(image.getHeight() < height) {
            y = (height-image.getHeight())/2;
        }

        BufferedImage tmpImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = tmpImage.getGraphics();
        graphics.drawImage(image, x, y, null);
        graphics.dispose();

        return tmpImage;
    }

}
