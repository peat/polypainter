package org.peat.polypaint.ui;

import org.peat.polypaint.Plan;
import org.peat.polypaint.Tracker;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by peat on 11/18/15.
 */
public class InfoFrame {
    private final int REFRESH_MS = 100;

    private Tracker tracker;

    private JFrame infoWindow;
    private ScheduledExecutorService updateExecutor;
    private Font labelFont;
    private Font infoFont;

    private JLabel totalAttempts;
    private JLabel totalSuccessRate;
    private JLabel shapesPerSecond;
    private JLabel shapeCount;
    private JLabel difference;
    private JLabel totalElapsed;
    private JLabel step;
    private JLabel stepProgress;
    private JLabel stepShape;
    private JLabel stepSize;
    private JLabel stepOpacity;

    public InfoFrame() {
        JLabel defaultLabel = new JLabel();
        Font defaultLabelFont = defaultLabel.getFont();

        this.labelFont = new Font(defaultLabelFont.getFontName(), Font.BOLD, defaultLabelFont.getSize());
        this.infoFont = new Font(defaultLabelFont.getFontName(), Font.PLAIN, defaultLabelFont.getSize());

        // information defaults
        this.totalAttempts = makeInfo();
        this.totalSuccessRate = makeInfo();
        this.shapesPerSecond = makeInfo();
        this.shapeCount = makeInfo();
        this.difference = makeInfo();
        this.totalElapsed = makeInfo();
        this.step = makeInfo();
        this.stepProgress = makeInfo();
        this.stepShape = makeInfo();
        this.stepSize = makeInfo();
        this.stepOpacity = makeInfo();

        this.updateExecutor = Executors.newScheduledThreadPool(1);
        updateExecutor.scheduleAtFixedRate(this::update, 0, REFRESH_MS, TimeUnit.MILLISECONDS);

        display();
    }

    public GridBagConstraints labelConstraint(int column, int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.EAST;
        c.gridx = column;
        c.gridy = row;
        c.insets = new Insets(2, 20, 2, 5);
        return c;
    }

    public GridBagConstraints infoConstraint(int column, int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = column;
        c.gridy = row;
        c.insets = new Insets(2, 5, 2, 5);
        c.weightx = 1.0;
        return c;
    }


    public JLabel makeLabel(String labelText) {
        JLabel l = new JLabel(labelText);
        l.setFont(this.labelFont);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }

    public JLabel makeInfo() {
        JLabel l = new JLabel();
        l.setFont(infoFont);
        return l;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    private void display() {
        SwingUtilities.invokeLater(() -> {
            infoWindow = new JFrame();
            infoWindow.setTitle("PolyPainter");
            // infoWindow.setPreferredSize(new Dimension(400, 200));
            infoWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            Container contentPane = infoWindow.getContentPane();
            contentPane.setLayout(new GridBagLayout());

            // first and second column: overall data!
            int row = 0;
            int column = 0;

            contentPane.add(makeLabel("Elapsed"), labelConstraint(column, row));
            contentPane.add(totalElapsed, infoConstraint(column + 1, row));
            row++;

            contentPane.add(makeLabel("Rate"), labelConstraint(column, row));
            contentPane.add(shapesPerSecond, infoConstraint(column + 1, row));
            row++;

            contentPane.add(makeLabel("Difference"), labelConstraint(column, row));
            contentPane.add(difference, infoConstraint(column + 1, row));
            row++;

            contentPane.add(makeLabel("Shapes"), labelConstraint(column, row));
            contentPane.add(shapeCount, infoConstraint(column + 1, row));
            row++;

            contentPane.add(makeLabel("Attempts"), labelConstraint(column, row));
            contentPane.add(totalAttempts, infoConstraint(column + 1, row));
            row++;

            contentPane.add(makeLabel("Success"), labelConstraint(column, row));
            contentPane.add(totalSuccessRate, infoConstraint(column + 1, row));
            row++;

            // Third and forth columns: Step data!
            row = 0;
            column = 2;

            contentPane.add(makeLabel("Step: Version"), labelConstraint(column, row));
            contentPane.add(step, infoConstraint(column + 1, row));
            row++;

            contentPane.add(makeLabel("Step: Progress"), labelConstraint(column, row));
            contentPane.add(stepProgress, infoConstraint(column + 1, row));
            row++;

            contentPane.add(makeLabel("Step: Size"), labelConstraint(column, row));
            contentPane.add(stepSize, infoConstraint(column + 1, row));
            row++;

            contentPane.add(makeLabel("Step: Shape"), labelConstraint(column, row));
            contentPane.add(stepShape, infoConstraint(column + 1, row));
            row++;

            contentPane.add(makeLabel("Step: Opacity"), labelConstraint(column, row));
            contentPane.add(stepOpacity, infoConstraint(column + 1, row));
            row++;

            infoWindow.pack();
            infoWindow.setLocationRelativeTo(null);
            infoWindow.setVisible(true);
        });
    }

    public void update() {
        // guard against no data present.
        if (this.tracker == null) return;

        this.totalElapsed.setText(formatElapsed(this.tracker.totalElapsedSeconds()));
        this.shapesPerSecond.setText(formatPerSecond(this.tracker.shapesPerSecond()));
        this.difference.setText(formatLong(this.tracker.getDifference()));
        this.shapeCount.setText(formatLong(this.tracker.getShapeCount()));
        this.totalAttempts.setText(formatLong(this.tracker.totalAttempts()));
        this.totalSuccessRate.setText(formatPercent(this.tracker.getTotalSuccessRate()));
        this.step.setText(formatLong(this.tracker.getStepCounter()));
        this.stepProgress.setText(formatPercent(this.tracker.getStepProgress()));
        this.stepShape.setText(this.tracker.getPlan().getShape().name());
        this.stepSize.setText(formatPrecisePercent(this.tracker.getPlan().getProportion()));
        this.stepOpacity.setText(formatOpacity(this.tracker.getPlan().getOpacity()));

        this.infoWindow.pack();
    }

    private String formatElapsed(long seconds) {
        return String.format("%d seconds", seconds);
    }

    private String formatLong(long value) {
        return String.format("%d", value);
    }

    private String formatInt(int value) {
        return String.format("%d", value);
    }

    private String formatPercent(double value) {
        int percent = (int) (value * 100.0); // it arrives as 0.8 for 80%
        return String.format("%d%%", percent);
    }

    private String formatPrecisePercent(double value) {
        double percent = value * 100.0; // it arrives as 0.8 for 80%
        return String.format("%.2f%%", percent);
    }

    private String formatPerSecond(int rate) {
        return String.format("%d / second", rate);
    }

    private String formatOpacity(int opacity) {
        double percent = (double) opacity / 255.0;
        return formatPercent(percent);
    }

}
