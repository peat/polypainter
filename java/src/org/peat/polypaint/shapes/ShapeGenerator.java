package org.peat.polypaint.shapes;

import org.peat.polypaint.Region;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by peat on 11/17/15.
 */
public interface ShapeGenerator {
    BufferedImage draw(BufferedImage image, Region region, Color color);

    String name();
}
