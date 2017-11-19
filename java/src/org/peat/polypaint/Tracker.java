package org.peat.polypaint;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * Created by peat on 11/17/15.
 */
public class Tracker {
    private long totalAttempts;
    private long totalRejections;
    private long stepAttempts;
    private long stepRejections;
    private long startTimeNS;
    private long elapsedTimeNS;
    private long stepTimeNS;
    private long diffTimeNS;
    private long stepCounter;
    private long difference;
    private long maxDifference;

    private Plan plan;
    private BufferedImage currentImage;

    public Tracker() {
        this.startTimeNS = System.nanoTime();
        this.elapsedTimeNS = 0;
        this.stepCounter = 0;
        this.difference = 0;
        this.maxDifference = 0;
        step();
    }

    public void step() {
        stepTimeNS = System.nanoTime();
        stepAttempts = 0;
        stepRejections = 0;
        stepCounter++;
    }

    public void attempt() {
        elapsedTimeNS = System.nanoTime() - startTimeNS;
        totalAttempts++;
        stepAttempts++;
    }

    public void reject() {
        totalRejections++;
        stepRejections++;
    }

    public long totalAttempts() {
        return totalAttempts;
    }

    public long totalRejections() {
        return totalRejections;
    }

    public long totalElapsedSeconds() {
        return TimeUnit.NANOSECONDS.toSeconds(elapsedTimeNS);
    }

    public long stepElapsedNS() {
        return System.nanoTime() - stepTimeNS;
    }

    public long stepAttempts() {
        return stepAttempts;
    }

    public long stepRejections() {
        return stepRejections;
    }

    public long getDifference() {
        return this.difference;
    }

    public void setDifference(long difference) {
        this.difference = difference;
        if (difference > this.maxDifference) {
            this.maxDifference = difference;
        }
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public BufferedImage getCurrentImage() {
        return this.currentImage;
    }

    public void setCurrentImage(BufferedImage image) {
        this.currentImage = image;
    }

    public double getProgress() {
        if ((maxDifference) == 0) {
            return 0.0;
        }
        return (double) (maxDifference - difference) / (double) maxDifference;
    }

    public double getStepRejectionRate() {
        return ((double) stepRejections / (double) stepAttempts);
    }

    public double getTotalSuccessRate() {
        return (getShapeCount() / (double) totalAttempts());
    }

    public long getShapeCount() {
        return totalAttempts - totalRejections;
    }

    public double getStepProgress() {
        double progress = getStepRejectionRate() / plan.getStepTriggerThreshold();
        if (progress > 1.0)
            progress = 1.0;
        if (progress < 0.0)
            progress = 0.0;

        return progress;
    }

    public long getStepCounter() {
        return this.stepCounter;
    }

    public int shapesPerSecond() {
        if (totalElapsedSeconds() == 0) {
            return 0;
        }
        return (int) (totalAttempts() / totalElapsedSeconds());
    }

    public boolean isEffective() {
        if (stepAttempts < Config.current().getStepSampleThreshold())
            return true;

        if (getStepRejectionRate() < plan.getStepTriggerThreshold())
            return true;

        return false;
    }

    public boolean shouldDiff() {
        long timeNow = System.nanoTime();
        long elapsedMS = TimeUnit.NANOSECONDS.toMillis(timeNow - diffTimeNS);
        if (elapsedMS > Config.current().getDifferenceFullFrequency()) {
            diffTimeNS = timeNow;
            return true;
        }

        return false;
    }

}
