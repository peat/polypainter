package org.peat.polypaint;

import java.awt.image.BufferedImage;

/**
 * Created by peat on 11/16/15.
 */
public class Region {
    private int xCenter;
    private int yCenter;
    private int width;
    private int height;

    // Note: These may be outside of the image box! Use the 'constrained' class method to constrain a given
    // Region to a specific image.
    private int xMinimum;
    private int yMinimum;
    private int xMaximum;
    private int yMaximum;

    public Region(int xCenter, int yCenter, int dimension) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.width = dimension;
        this.height = dimension;

        this.xMinimum = calculateMinimum(xCenter);
        this.yMinimum = calculateMinimum(yCenter);
        this.xMaximum = calculateMaximum(xCenter);
        this.yMaximum = calculateMaximum(yCenter);
    }

    public Region(int xMinimum, int yMinimum, int xMaximum, int yMaximum) {
        this.xMinimum = xMinimum;
        this.yMinimum = yMinimum;
        this.xMaximum = xMaximum;
        this.yMaximum = yMaximum;

        this.width = xMaximum - xMinimum;
        this.height = yMaximum - yMinimum;

        this.xCenter = xMinimum + (this.width / 2);
        this.yCenter = yMinimum + (this.height / 2);
    }

    public static Region constrained(Region region, BufferedImage image) {
        int xLimit = image.getWidth();
        int yLimit = image.getHeight();

        int minX = 0;
        int minY = 0;
        int maxX = xLimit;
        int maxY = yLimit;

        if (region.xMinimum > 0) {
            minX = region.xMinimum;
        }

        if (region.xMaximum < xLimit) {
            maxX = region.xMaximum;
        }

        if (region.yMinimum > 0) {
            minY = region.yMinimum;
        }

        if (region.yMaximum < yLimit) {
            maxY = region.yMaximum;
        }

        return new Region(minX, minY, maxX, maxY);
    }

    public static Region random(BufferedImage image, double proportion) {
        // find a random point within the image; this guarantees we'll hit the edges of the region from time to time.
        int xCenter = Util.inclusiveRandInt(image.getWidth() - 1);
        int yCenter = Util.inclusiveRandInt(image.getHeight() - 1);

        // ensure our max dimension won't overflow the image width or height
        int maxDimension = image.getWidth();
        if (maxDimension > image.getHeight()) {
            maxDimension = image.getHeight();
        }

        // determine how large the box is going to be, bounded by the min/max proportions.
        int dimension = (int) (maxDimension * proportion);

        // ensure we never have a region smaller than configured above.
        if (dimension < Config.current().getRegionMinimumDimension()) {
            dimension = Config.current().getRegionMinimumDimension();
        }

        return new Region(xCenter, yCenter, dimension);
    }

    private int calculateMinimum(int value) {
        return value - (width / 2);
    }

    private int calculateMaximum(int value) {
        return value + (width / 2);
    }

    public int getXMinimum() {
        return xMinimum;
    }

    public int getXMaximum() {
        return xMaximum;
    }

    public int getXCenter() {
        return xCenter;
    }

    public int getYMinimum() {
        return yMinimum;
    }

    public int getYMaximum() {
        return yMaximum;
    }

    public int getYCenter() {
        return yCenter;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Region[xMinimum=" + xMinimum +
                ", yMinimum=" + yMinimum +
                ", xMaximum=" + xMaximum +
                ", yMaximum=" + yMaximum +
                ", xCenter=" + xCenter +
                ", yCenter=" + yCenter +
                ", width=" + width +
                ", height=" + height + "]";
    }
}
