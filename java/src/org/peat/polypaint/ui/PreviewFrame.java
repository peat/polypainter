package org.peat.polypaint.ui;

import java.awt.*;
import java.awt.image.*;
import java.util.concurrent.*;
import javax.swing.*;

/**
 * Created by peat on 11/16/15.
 */
public class PreviewFrame {
    private static final int REFRESH_MS = 100;

    private BufferedImage image;
    private JLabel imageView;
    private JFrame mainWindow;
    private ScheduledExecutorService updateExecutor;

    public PreviewFrame(BufferedImage startingImage) {
        this.image = startingImage;

        SwingUtilities.invokeLater(() -> {
            mainWindow = new JFrame();
            mainWindow.setTitle("PolyPainter - Render Preview");

            Container contentPane = mainWindow.getContentPane();

            imageView = new JLabel(new ImageIcon(image));
            contentPane.add(imageView);

            mainWindow.pack();
            mainWindow.setLocationRelativeTo(null);
            mainWindow.setVisible(true);

            updateExecutor = Executors.newScheduledThreadPool(1);
            updateExecutor.scheduleAtFixedRate(this::update, 0, REFRESH_MS, TimeUnit.MILLISECONDS);
        });
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    private void update() {
        this.imageView.setIcon(new ImageIcon(this.image));
    }
}
