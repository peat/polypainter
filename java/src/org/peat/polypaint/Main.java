package org.peat.polypaint;

import org.peat.polypaint.ui.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;


public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Loading " + Config.current().getRenderImagePath());
        Util.setRandomSeed(Config.current().getRandomSeed());

        // the three images we track: original source image, the current best image, and a candidate image.
        BufferedImage originalImage;
        BufferedImage currentImage;
        BufferedImage candidateImage;

        // set up the tracker. This collects stats information about the rendering.
        Tracker tracker = new Tracker();

        // Set up our render plan, register it with the tracker. This has the initial configuration for
        // shape attributes, and the Tracker will change it over time as it steps over certain thresholds.
        Plan plan = new Plan();
        tracker.setPlan(plan);

        // read in the source image, set up a blank canvas.
        originalImage = ImageIO.read(new File(Config.current().getRenderImagePath()));
        currentImage = Util.zeroImage(originalImage);

        // save the difference between our original and blank images, so that we can measure progress later on!
        tracker.setDifference(imageDiff(originalImage, currentImage));

        // set up our windows ...
        PreviewFrame previewFrame = new PreviewFrame(currentImage);
        InfoFrame infoFrame = new InfoFrame();

        // off to the races! loop until we hit a stopping threshold
        while (keepRunning(tracker, plan)) {
            tracker.attempt();

            // check where we're at in our plan; move on to the next if it's time!
            if (!tracker.isEffective()) {
                plan.step();
                tracker.step();
            }

            // Pick a semi-random region with our current constraints.
            Region targetRegion = Region.random(currentImage, plan.getProportion());

            // Pick the color from the master image.
            Color color = selectCenterColor(originalImage, targetRegion, plan);

            // Add the shape to the image, and return it.
            candidateImage = plan.getShape().draw(Util.copyImage(currentImage), targetRegion, color);

            // if we've improved the local region, update the overall image.
            double candidateChange = regionChange(originalImage, currentImage, candidateImage, targetRegion);
            if (candidateChange >= Config.current().getRenderImprovementThreshold()) {
                currentImage = candidateImage;
            } else {
                tracker.reject();
            }

            // periodically do a diff on the whole image; this is kind of expensive, and could be done in a separate thread!
            if (tracker.shouldDiff()) {
                tracker.setDifference(imageDiff(originalImage, currentImage));
            }

            // push data into our windows
            infoFrame.setTracker(tracker);
            previewFrame.setImage(currentImage);
        }
        System.out.println("Done!");

        // write out the config
        Config.current().write();

        // write out the image!
        File outputFile = new File(Config.current().generateImagePath());
        ImageIO.write(currentImage, "png", outputFile);
    }

    public static Color selectCenterColor(BufferedImage image, Region region, Plan plan) {
        Color sample = new Color(image.getRGB(region.getXCenter(), region.getYCenter()));
        return new Color(sample.getRed(), sample.getGreen(), sample.getBlue(), plan.getOpacity());
    }

    // adds up the difference between the R, G, and B color channels.
    public static int colorDiff(Color a, Color b) {
        int diffRed = Math.abs(a.getRed() - b.getRed());
        int diffBlue = Math.abs(a.getBlue() - b.getBlue());
        int diffGreen = Math.abs(a.getGreen() - b.getGreen());
        return diffRed + diffBlue + diffGreen;
    }

    public static int brightnessDiff(Color a, Color b) {
        return Math.abs(a.getRGB() - b.getRGB());
    }

    // adds up the pixel by pixel difference between two images of the exact same dimensions
    public static long imageDiff(BufferedImage a, BufferedImage b) {
        int sampleRate = Config.current().getDifferenceSampleFrequency();
        int width = a.getWidth();
        int height = a.getHeight();
        long diff = 0;
        for (int xIdx = 0; xIdx < width; xIdx += sampleRate) {
            for (int yIdx = 0; yIdx < height; yIdx += sampleRate) {
                Color aPixel = new Color(a.getRGB(xIdx, yIdx));
                Color bPixel = new Color(b.getRGB(xIdx, yIdx));
                diff += colorDiff(aPixel, bPixel);
            }
        }
        return diff;
    }

    public static long regionDiff(BufferedImage a, BufferedImage b, Region r) {
        BufferedImage a2 = a.getSubimage(r.getXMinimum(), r.getYMinimum(), r.getWidth(), r.getHeight());
        BufferedImage b2 = b.getSubimage(r.getXMinimum(), r.getYMinimum(), r.getWidth(), r.getHeight());

        return imageDiff(a2, b2);
    }

    // returned values greater than 0.0 represent an improvement, lower are regressive
    public static double regionChange(BufferedImage original, BufferedImage current, BufferedImage candidate, Region region) {
        Region r = Region.constrained(region, original);

        long currentDiff = regionDiff(original, current, r);
        long candidateDiff = regionDiff(original, candidate, r);

        return ((double) currentDiff / (double) candidateDiff) - 1.0;
    }

    public static boolean keepRunning(Tracker tracker, Plan plan) {
        if (tracker.totalAttempts() >= Config.current().getRenderAttemptLimit()) {
            System.out.println("Finished because we hit our maximum number of attempts: " + Config.current().getRenderAttemptLimit());
            return false;
        }
        if (plan.getOpacity() <= 0) {
            System.out.println("Finished because we hit zero opacity.");
            return false;
        }
        if (plan.getProportion() < Config.current().getRenderProportionLimit()) {
            System.out.println("Finished because we hit our minimum shape proportion: " + Config.current().getRenderProportionLimit());
            return false;
        }
        return true;
    }


}
