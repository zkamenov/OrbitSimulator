package com.kamenov.zachgraphics;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class ZachGraphics {
    Display d;

    Point3D[] stars;

    BufferStrategy bs;
    Graphics g;
    private Thread t;


    public Dimension getDimensions() {
        return new Dimension(d.getFrame().getWidth(),d.getFrame().getHeight());
    }

    public ZachGraphics(String name, int width, int height, KeyListener kl) {
        d = new Display(name,width,height,true);
        d.getCanvas().addKeyListener(kl);
        t = new Thread();

        stars = new Point3D[10000];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Point3D(((Math.random()-0.5)),((Math.random()-0.5)),((Math.random()-0.5)));
        }
    }

    public void render(Render r, double camX, double camY, double fOV) {
        if (t.isAlive() || !d.getFrame().isDisplayable())
            return;
        t = new Thread(() -> {
            bs = d.getCanvas().getBufferStrategy();
            if (bs == null) {
                d.getCanvas().createBufferStrategy(2);
                return;
            }

            g = bs.getDrawGraphics();
            g.setColor(new Color(2, 0, 5));
            g.fillRect(0, 0, d.getFrame().getWidth(), d.getFrame().getHeight());
            g.translate(d.getFrame().getWidth() / 2, d.getFrame().getHeight() / 2);
            g.setColor(Color.WHITE);
            for (Point3D p : stars) {
                try {
                    g.fillRect(PerspectiveTranslate.trans3D2(p, camX, camY, fOV).x, PerspectiveTranslate.trans3D2(p, camX, camY, fOV).y, 2, 2);
                } catch (NullPointerException ignored) {
                }
            }

            r.render(g);
            bs.show();
            g.dispose();
        });
        t.start();
    }
}
