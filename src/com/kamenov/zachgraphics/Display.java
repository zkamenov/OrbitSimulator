package com.kamenov.zachgraphics;

import com.kamenov.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class Display {
    private JFrame frame;
    private Canvas canvas;

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Display(String name, int width, int height, boolean resizable) {
        frame = new JFrame(name);
        canvas = new Canvas();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(width,height);
        frame.setResizable(resizable);
        frame.setLocationRelativeTo(null);
        frame.add(canvas);
        frame.setVisible(true);
        try {
            //Taskbar.getTaskbar().setIconImage(ImageIO.read(Launcher.class.getResourceAsStream("/WOF4OrbitSimulator.png")));
            frame.setIconImage(ImageIO.read(Launcher.class.getResourceAsStream("/WOF4OrbitSimulator.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
