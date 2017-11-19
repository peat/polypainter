package org.peat.polypaint;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by peat on 11/17/15.
 */
public class Util {

    public static Random randomNumberGenerator = new Random();

    public static void setRandomSeed(long seed) {
        randomNumberGenerator = new Random(seed);
    }

    // 0 to max - 1
    public static int exclusiveRandInt(int maximum) {
        return randomNumberGenerator.nextInt(maximum);
    }

    public static int inclusiveRandInt(int maximum) {
        return inclusiveRandInt(0, maximum);
    }

    // inclusive random int
    public static int inclusiveRandInt(int bottom, int maximum) {
        int shiftedMaximum = maximum - bottom;
        int source = randomNumberGenerator.nextInt(shiftedMaximum + 1); // make it inclusive at the top bound
        return source + bottom; // shift back to the original range
    }

    public static int[] randIntArray(int minimum, int maximum, int count) {
        int[] intArray = new int[count];
        for (int idx = 0; idx < count; idx++) {
            intArray[idx] = inclusiveRandInt(minimum, maximum);
        }
        return intArray;
    }

    public static BufferedImage zeroImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, source.getWidth(), source.getHeight());
        g.dispose();
        return b;
    }

    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}
