package com.android.icon.generator.core;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface AndroidIconGenerator {

    public BufferedImage generateIcon(BufferedImage inputImage, DIP dip) throws IOException;

}
