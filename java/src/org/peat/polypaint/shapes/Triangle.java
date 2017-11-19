package org.peat.polypaint.shapes;

import org.peat.polypaint.Region;
import org.peat.polypaint.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by peat on 11/17/15.
 */
public class Triangle implements ShapeGenerator {

    @Override
    public BufferedImage draw(BufferedImage image, Region region, Color color) {
        int[] xPoints = Util.randIntArray(region.getXMinimum(), region.getXMinimum() + region.getWidth(), 3);
        int[] yPoints = Util.randIntArray(region.getYMinimum(), region.getYMinimum() + region.getHeight(), 3);

        Polygon polygon = new Polygon(xPoints, yPoints, 3);

        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillPolygon(polygon);
        g.dispose();
        return image;
    }

    @Override
    public String name() {
        return "Triangle";
    }
}
