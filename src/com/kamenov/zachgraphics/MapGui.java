/*
 * Copyright (c) 2021. Ian Zachary Kamenov. All Rights Reserved.
 */

package com.kamenov.zachgraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class MapGui extends MouseAdapter {

    private Point pos;

    private boolean trafficLight = false;
    private Display d;

    public MapGui() {
        d = new Display("Click on a point on the map",1000,500, false);
        d.getCanvas().addMouseListener(new MouseAdapter() {
            @Override
            public synchronized void mouseReleased(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("thread notified");
                Point r = MouseInfo.getPointerInfo().getLocation();
                r.translate(-d.getFrame().getX(),-d.getFrame().getY()-d.getFrame().getInsets().top);
                pos = r;
                d.getFrame().dispose();

                trafficLight = true;
                this.notify();
            }
        });
        d.getCanvas().createBufferStrategy(3);
    }

    public Point selectMappoint(BufferedImage i) {


        Thread t = new Thread(() -> click(i));
        t.start();
        Point p = Callus(i);
        //t.interrupt();
        return p;
    }

    private void click(BufferedImage i) {

            while (true) {
                if (d.getCanvas().getBufferStrategy() == null)
                    d.getCanvas().createBufferStrategy(3);

                BufferStrategy bs = d.getCanvas().getBufferStrategy();
                Graphics g = bs.getDrawGraphics();


                g.drawImage(i, 0, 0, 1000, 500, null);

                bs.show();
                g.dispose();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



    }




    private synchronized Point Callus(BufferedImage i) {



        //).start();



        //while (p[0] == null) {
        //    try {
        //        Thread.sleep(10);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //}


            try {
                while (!trafficLight)
                    Thread.sleep(10);//this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("notification confirmed");

        if (Objects.isNull(pos))
            throw new NullPointerException();

        Point p = pos;

        return new Point((int)(p.x / 1000D * i.getWidth()),(int)(p.y / 500D * i.getHeight()));

    }
}
