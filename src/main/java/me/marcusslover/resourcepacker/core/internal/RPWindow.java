package me.marcusslover.resourcepacker.core.internal;

import javax.swing.*;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.net.URL;

public class RPWindow extends JFrame {

    private final int width;
    private final int height;

    /*Temp*/
    private Image background = null;
    private JProgressBar progress = null;

    public RPWindow(int width, int height) {
        super("ResourcePacker");
        this.width = width * 2;
        this.height = height * 2;
    }

    public JProgressBar getProgress() {
        return progress;
    }

    public void init() {
        try { //Images.
            URL logo = getClass().getResource("/logo/rp.png");
            if (logo != null) {
                Image image = new ImageIcon(logo).getImage();
                if (image != null) {
                    setIconImage(image);
                }
            }
            URL background = getClass().getResource("/logo/rp_background.png");
            if (background != null) {
                Image image = new ImageIcon(background).getImage();
                if (image != null) {
                    this.background = image;
                }
            }
        } catch (Exception ignored) {
        }

        JPanel jPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        jPanel.setPreferredSize(new Dimension(width, height));
        jPanel.setLayout(new BorderLayout());
        jPanel.setUI(new BasicPanelUI());
        setContentPane(jPanel);

        progress = new JProgressBar(0, 1, 100);
        progress.setPreferredSize(new Dimension(width, 27));
        progress.setBackground(new Color(255, 255, 255, 100));
        progress.setUI(new BasicProgressBarUI());
        progress.setForeground(new Color(34, 87, 21));

        jPanel.add(progress, BorderLayout.SOUTH);
        pack();

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);

        setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }
}