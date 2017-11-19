package org.peat.polypaint;

import org.peat.polypaint.shapes.*;

/**
 * Created by peat on 11/16/15.
 */
public class Plan {
    private double proportion;
    private double stepProportion;
    private int opacity;
    private double stepOpacity;
    private ShapeGenerator shape;
    private double stepTriggerThreshold;

    public Plan() {
        this.proportion = Config.current().getRegionProportionStart();
        this.stepProportion = Config.current().getRegionProportionStep();
        this.opacity = Config.current().getOpacityStart();
        this.stepOpacity = Config.current().getOpacityStep();
        this.shape = Config.current().getRenderShape();
        this.stepTriggerThreshold = Config.current().getStepRejectionThreshold();
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public double getStepProportion() {
        return stepProportion;
    }

    public void setStepProportion(double stepProportion) {
        this.stepProportion = stepProportion;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public double getStepOpacity() {
        return stepOpacity;
    }

    public void setStepOpacity(double stepOpacity) {
        this.stepOpacity = stepOpacity;
    }

    public ShapeGenerator getShape() {
        return this.shape;
    }

    public void setShape(ShapeGenerator shape) {
        this.shape = shape;
    }

    public double getStepTriggerThreshold() {
        return stepTriggerThreshold;
    }

    public void setStepTriggerThreshold(double stepTriggerThreshold) {
        this.stepTriggerThreshold = stepTriggerThreshold;
    }

    public void step() {
        this.proportion = this.proportion * this.stepProportion;

//        int targetOpacity = (int) ((double) this.opacity * this.stepOpacity);
//
//        // if opacity and targetOpacity are the same (rounding error) we'll bump it by one and call it good.
//        if (targetOpacity == this.opacity) {
//            if (this.stepOpacity > 1.0)
//                targetOpacity++;
//            if (this.stepOpacity < 1.0)
//                targetOpacity--;
//        }
//
//        // opacity has inclusive bounds of 0-255
//        if (targetOpacity > 255)
//            targetOpacity = 255;
//        if (targetOpacity < 0)
//            targetOpacity = 0;
//
//        // this.opacity = targetOpacity;
    }

    @Override
    public String toString() {
        return "Plan[proportion=" + proportion + ", opacity=" + opacity + "]";
    }
}
