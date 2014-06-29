package com.android.icon.generator.utils;

import com.android.icon.generator.core.DIP;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SaveFileUtils {

    public static void saveAsDrawable(BufferedImage image, File file, DIP dip) throws IOException {
        if(!file.exists()) {
            file.mkdirs();
        }

        String outputPath = FilenameUtils.getFullPath(file.getAbsolutePath());
        outputPath = FilenameUtils.concat(outputPath, FilenameUtils.getBaseName(file.getAbsolutePath()));
        outputPath = FilenameUtils.concat(outputPath, "drawable-" + dip.toString().toLowerCase());
        outputPath = FilenameUtils.concat(outputPath, FilenameUtils.getBaseName(file.getAbsolutePath()) + ".png");

        File outputFile = new File(outputPath);
        if(!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
        }

        ImageIO.write(image, "png", outputFile);
    }

}
