package org.peat.polypaint.shapes;

import org.peat.polypaint.Region;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by peat on 11/17/15.
 */
public class Square implements ShapeGenerator {

    @Override
    public BufferedImage draw(BufferedImage image, Region region, Color color) {
        int[] xPoints = {
                region.getXMinimum(),
                region.getXMinimum() + region.getWidth(),
                region.getXMinimum() + region.getWidth(),
                region.getXMinimum()
        };
        int[] yPoints = {
                region.getYMinimum(),
                region.getYMinimum(),
                region.getYMinimum() + region.getHeight(),
                region.getYMinimum() + region.getHeight()
        };
        Polygon polygon = new Polygon(xPoints, yPoints, 4);

        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillPolygon(polygon);
        g.dispose();
        return image;
    }

    @Override
    public String name() {
        return "Square";
    }
}
