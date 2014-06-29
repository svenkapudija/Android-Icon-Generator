package com.android.icon.generator.actionbar;

import com.android.icon.generator.core.AndroidIconGenerator;
import com.android.icon.generator.core.AndroidTheme;
import com.android.icon.generator.core.DIP;
import com.android.icon.generator.utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AndroidActionBarIconGenerator implements AndroidIconGenerator {

    private AndroidTheme style;

    public AndroidActionBarIconGenerator(AndroidTheme style) {
        if(style == null) {
            throw new IllegalArgumentException("Style can't be null.");
        }

        this.style = style;
    }

    @Override
    public BufferedImage generateIcon(BufferedImage inputImage, DIP dip) throws IOException {
        int widthDp = 32;
        int heightDp = 32;
        int paddingDp = 8/2;

        String colorHex = null;
        int opacity = 0;
        switch (style) {
            case HOLO_LIGHT:
                colorHex = "#333333";
                opacity = 60;
                break;
            case HOLO_DARK:
                colorHex = "#ffffff";
                opacity = 80;
                break;
        }

        int widthPx = (int) (widthDp* dip.getScale());
        int heightPx = (int) (heightDp* dip.getScale());
        int paddingPx = (int) (paddingDp* dip.getScale());

        BufferedImage outputImage = ImageUtils.resizeImage(inputImage, widthPx - paddingPx * 2, heightPx - paddingPx * 2, true);
        outputImage = ImageUtils.padImage(outputImage, paddingPx);
        outputImage = ImageUtils.colorImage(outputImage, Color.decode(colorHex));
        outputImage = ImageUtils.setOpacity(outputImage, opacity);

        return outputImage;
    }

}
