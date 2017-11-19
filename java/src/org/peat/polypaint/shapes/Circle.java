package org.peat.polypaint.shapes;

import org.peat.polypaint.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by peat on 11/17/15.
 */
public class Circle implements ShapeGenerator {

    protected static int scale(int dimension) {
        double minimum = (double) dimension * Config.current().getCircleScale();
        return Util.inclusiveRandInt((int) minimum, dimension);
    }

    @Override
    // Circles don't take up the entire region;
    public BufferedImage draw(BufferedImage image, Region region, Color color) {
        int diameter = scale(region.getWidth());

        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillOval(region.getXMinimum(), region.getYMinimum(), diameter, diameter);
        g.dispose();
        return image;
    }

    @Override
    public String name() {
        return "Circle";
    }

}
