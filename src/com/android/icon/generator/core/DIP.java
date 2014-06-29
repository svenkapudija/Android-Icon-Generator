package com.android.icon.generator.core;

/**
 * Density-independent Pixels - An abstract unit that is based on the physical density of the screen.
 *
 * @see <a href="http://developer.android.com/guide/topics/resources/more-resources.html#Dimension">http://developer.android.com/guide/topics/resources/more-resources.html#Dimension</a>
 */
public enum DIP {

    LDPI(0.5),
    MDPI(1),
    HDPI(1.5),
    XHDPI(2),
    XXHDPI(3),
    XXXHDPI(4);

    private double scale;

    DIP(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }
}