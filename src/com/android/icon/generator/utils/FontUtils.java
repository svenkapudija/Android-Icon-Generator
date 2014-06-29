package com.android.icon.generator.utils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

public class FontUtils {

    public static BufferedImage convertStringToImage(File fontFile, char characterUnicode, float fontSize) throws IOException, FontFormatException {
        return convertStringToImage(fontFile, Character.toString(characterUnicode), fontSize);
    }

    public static BufferedImage convertStringToImage(File fontFile, String string, float fontSize) throws IOException, FontFormatException {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = bufferedImage.createGraphics();

        InputStream fontStream = new BufferedInputStream(new FileInputStream(fontFile));
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontSize);
        graphics.setFont(font);

        FontRenderContext fontRenderContext = graphics.getFontMetrics().getFontRenderContext();
        Rectangle2D rectangle = font.getStringBounds(string, fontRenderContext);
        graphics.dispose();

        bufferedImage = new BufferedImage(
                (int) Math.ceil(rectangle.getWidth()),
                (int) Math.ceil(rectangle.getHeight()),
                BufferedImage.TYPE_4BYTE_ABGR
        );
        graphics = bufferedImage.createGraphics();
        graphics.setColor(Color.black);
        graphics.setFont(font);

        FontMetrics fontMetrics = graphics.getFontMetrics();
        int x = 0;
        int y = fontMetrics.getAscent(); // getBaseline()
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString(string, x, y);
        graphics.dispose();

        return bufferedImage;
    }

}
