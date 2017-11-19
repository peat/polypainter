package org.peat.polypaint.shapes;

import org.peat.polypaint.Region;
import org.peat.polypaint.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by peat on 11/18/15.
 */
public class Random implements ShapeGenerator {
    private ArrayList<ShapeGenerator> shapes;

    public Random() {
        this.shapes = new ArrayList<>();
        this.shapes.add(new Circle());
        this.shapes.add(new Square());
        this.shapes.add(new Triangle());
        this.shapes.add(new Ring());
    }

    private ShapeGenerator pickShape() {
        return this.shapes.get(Util.exclusiveRandInt(this.shapes.size()));
    }

    @Override
    public BufferedImage draw(BufferedImage image, Region region, Color color) {
        return pickShape().draw(image, region, color);
    }

    @Override
    public String name() {
        return "Random!";
    }
}
