package com.android.icon.generator;

import com.android.icon.generator.core.AndroidIconGenerator;
import com.android.icon.generator.core.AndroidTheme;
import com.android.icon.generator.actionbar.AndroidActionBarIconGenerator;
import com.android.icon.generator.fontawesome.FontAwesome;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Generator {

    public static void main(String[] args) throws IOException, FontFormatException {
        AndroidIconGenerator holoLightGenerator = new AndroidActionBarIconGenerator(AndroidTheme.HOLO_LIGHT);
        new FontAwesome().generateAllIcons(holoLightGenerator, getOutputDirectory("holo_light"), false);

        AndroidIconGenerator holoDarkGenerator = new AndroidActionBarIconGenerator(AndroidTheme.HOLO_DARK);
        new FontAwesome().generateAllIcons(holoDarkGenerator, getOutputDirectory("holo_dark"), false);
    }

    private static File getOutputDirectory(String directory) {
        String path = System.getProperty("user.home");
        path = FilenameUtils.concat(path, "font-awesome");
        path = FilenameUtils.concat(path, directory);

        return new File(path);
    }

}
