package org.peat.polypaint;

import org.peat.polypaint.shapes.*;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by peat on 11/19/15.
 */
public class Config {

    // Plan
    private static final double REGION_PROPORTION_START = 1.0;
    private static final double REGION_PROPORTION_STEP = 0.9; // how much we scale proportion when we step
    private static final double STEP_REJECTION_THRESHOLD = 0.9; // % rejection rate when we start seeing diminishing returns
    private static final int OPACITY_START = 60;
    private static final double OPACITY_STEP = 0.0; // how much we scale opacity when we step
    private static final ShapeGenerator RENDER_SHAPE = new Triangle();

    // Tracker
    private static final long STEP_SAMPLE_THRESHOLD = 100; // valid number of samples before effectiveness can be determined.

    // Region
    private static final int REGION_MINIMUM_DIMENSION = 2;

    // Main
    private static final String RENDER_IMAGE_PATH = "../images/yosemite.small.jpg";
    private static final String RENDER_OUTPUT_PATH = "../output/";
    private static final double RENDER_IMPROVEMENT_THRESHOLD = 0.01; // 0.0 = the images were the same, > is better, < worse
    private static final double RENDER_PROPORTION_LIMIT = 0.0001; // 1/100th of 1%
    private static final int RENDER_ATTEMPT_LIMIT = 50000000;
    private static final long RANDOM_SEED = 1447977085903191000L;
    private static final int DIFFERENCE_FULL_FREQUENCY = 1000; // calculate the full difference every n milliseconds
    private static final int DIFFERENCE_SAMPLE_FREQUENCY = 3;

    // Shapes
    private static final double CIRCLE_SCALE = 0.5;
    private static final float RING_STROKE_RATIO = 5.0f;

    private double regionProportionStart;
    private double regionProportionStep;
    private int opacityStart;
    private double opacityStep;
    private double stepRejectionThreshold;
    private long stepSampleThreshold;
    private int regionMinimumDimension;
    private ShapeGenerator renderShape;
    private String renderImagePath;
    private String renderOutputPath;
    private int renderAttemptLimit;
    private double renderProportionLimit;
    private double renderImprovementThreshold;
    private int differenceFullFrequency;
    private int differenceSampleFrequency;
    private double circleScale;
    private float ringStrokeRatio;
    private long randomSeed;

    private static Config current;

    // Creates a configuration containing all default values.
    public Config() {
        regionProportionStart = REGION_PROPORTION_START;
        regionProportionStep = REGION_PROPORTION_STEP;
        opacityStart = OPACITY_START;
        opacityStep = OPACITY_STEP;
        stepRejectionThreshold = STEP_REJECTION_THRESHOLD;
        stepSampleThreshold = STEP_SAMPLE_THRESHOLD;
        regionMinimumDimension = REGION_MINIMUM_DIMENSION;
        renderShape = RENDER_SHAPE;
        renderAttemptLimit = RENDER_ATTEMPT_LIMIT;
        renderProportionLimit = RENDER_PROPORTION_LIMIT;
        renderImagePath = RENDER_IMAGE_PATH;
        renderOutputPath = RENDER_OUTPUT_PATH;
        renderImprovementThreshold = RENDER_IMPROVEMENT_THRESHOLD;
        differenceFullFrequency = DIFFERENCE_FULL_FREQUENCY;
        differenceSampleFrequency = DIFFERENCE_SAMPLE_FREQUENCY;
        circleScale = CIRCLE_SCALE;
        ringStrokeRatio = RING_STROKE_RATIO;
        randomSeed = RANDOM_SEED;
    }

    public static Config current() {
        if (current == null) {
            current = new Config();
        }
        return current;
    }

    public String identifier() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(regionProportionStart);
        buffer.append(regionProportionStep);
        buffer.append(opacityStart);
        buffer.append(opacityStep);
        buffer.append(stepRejectionThreshold);
        buffer.append(stepSampleThreshold);
        buffer.append(regionMinimumDimension);
        buffer.append(renderShape.name());
        buffer.append(renderAttemptLimit);
        buffer.append(renderProportionLimit);
        buffer.append(renderImagePath);
        buffer.append(renderOutputPath);
        buffer.append(renderImprovementThreshold);
        buffer.append(differenceFullFrequency);
        buffer.append(differenceSampleFrequency);
        buffer.append(circleScale);
        buffer.append(ringStrokeRatio);
        buffer.append(randomSeed);

        String digest = "00000000"; // placeholder
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(buffer.toString().getBytes());
            byte[] rawDigest = md.digest();
            digest = String.format("%064x", new BigInteger(1, rawDigest));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return digest.substring(0, 8);
    }

    public String generateDatestamp() {
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        return formatter.format(currentDate);

    }

    public String generateConfigPath() {
        return renderOutputPath + "/" + identifier() + ".plan";
    }

    public String generateImagePath() {
        return renderOutputPath + "/" + generateDatestamp() + "." + identifier() + ".png";
    }

    public void write() {
        Properties p = new Properties();
        p.setProperty("regionProportionStart", formatFloat(regionProportionStart));
        p.setProperty("regionProportionStep", formatFloat(regionProportionStep));
        p.setProperty("opacityStart", formatInt(opacityStart));
        p.setProperty("opacityStep", formatFloat(opacityStep));
        p.setProperty("stepRejectionThreshold", formatFloat(stepRejectionThreshold));
        p.setProperty("stepSampleThreshold", formatFloat(stepSampleThreshold));
        p.setProperty("regionMinimumDimension", formatInt(regionMinimumDimension));
        p.setProperty("renderShape", renderShape.name());
        p.setProperty("renderAttemptLimit", formatInt(renderAttemptLimit));
        p.setProperty("renderProportionLimit", formatFloat(renderProportionLimit));
        p.setProperty("renderImagePath", renderImagePath);
        p.setProperty("renderImprovementThreshold", formatFloat(renderImprovementThreshold));
        p.setProperty("differenceFullFrequency", formatInt(differenceFullFrequency));
        p.setProperty("differenceSampleFrequency", formatInt(differenceSampleFrequency));
        p.setProperty("circleScale", formatFloat(circleScale));
        p.setProperty("ringStrokeRatio", formatFloat(ringStrokeRatio));
        p.setProperty("randomSeed", formatLong(randomSeed));

        try {
            File file = new File(generateConfigPath());
            FileOutputStream fileOut = new FileOutputStream(file);
            p.store(fileOut, "Configuration #" + identifier());
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatFloat(double value) {
        return String.format("%f", value);
    }

    private String formatInt(int value) {
        return String.format("%d", value);
    }

    private String formatLong(long value) {
        return String.format("%d", value);
    }

    public double getRegionProportionStart() {
        return regionProportionStart;
    }

    public double getRegionProportionStep() {
        return regionProportionStep;
    }

    public int getOpacityStart() {
        return opacityStart;
    }

    public double getOpacityStep() {
        return opacityStep;
    }

    public double getStepRejectionThreshold() {
        return stepRejectionThreshold;
    }

    public long getStepSampleThreshold() {
        return stepSampleThreshold;
    }

    public int getRegionMinimumDimension() {
        return regionMinimumDimension;
    }

    public ShapeGenerator getRenderShape() {
        return renderShape;
    }

    public String getRenderImagePath() {
        return renderImagePath;
    }

    public int getRenderAttemptLimit() {
        return renderAttemptLimit;
    }

    public int getDifferenceFullFrequency() {
        return differenceFullFrequency;
    }

    public int getDifferenceSampleFrequency() {
        return differenceSampleFrequency;
    }

    public double getCircleScale() {
        return circleScale;
    }

    public double getRenderImprovementThreshold() {
        return renderImprovementThreshold;
    }

    public double getRenderProportionLimit() {
        return renderProportionLimit;
    }

    public String getRenderOutputPath() {
        return renderOutputPath;
    }

    public float getRingStrokeRatio() {
        return ringStrokeRatio;
    }

    public long getRandomSeed() {
        return randomSeed;
    }
}
