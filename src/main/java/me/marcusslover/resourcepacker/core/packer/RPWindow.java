/*
 * MIT License
 *
 * Copyright (c) 2021 MarcusSlover
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.marcusslover.resourcepacker.core.packer;

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
