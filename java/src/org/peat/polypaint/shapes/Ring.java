package org.peat.polypaint.shapes;

import org.peat.polypaint.Config;
import org.peat.polypaint.Region;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by peat on 11/19/15.
 */
public class Ring extends Circle {

    protected float strokeWidth(int diameter) {
        return ((float) diameter) / Config.current().getRingStrokeRatio();
    }

    @Override
    public BufferedImage draw(BufferedImage image, Region region, Color color) {
        int diameter = scale(region.getWidth());
        float strokeWidth = strokeWidth(diameter);

        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.setStroke(new BasicStroke(strokeWidth));
        g.drawOval(region.getXMinimum(), region.getYMinimum(), diameter, diameter);
        g.dispose();
        return image;    }

    @Override
    public String name() {
        return "Ring";
    }
}
