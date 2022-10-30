/*
 * Copyright (c) 2020. Ian Zachary Kamenov. All Rights Reserved.
 */

package com.kamenov;

import com.kamenov.zachgraphics.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Math.*;
import static java.lang.System.exit;
import static java.lang.Thread.sleep;
import static java.awt.Color.*;

class Central {

    private int mP;
    private Thread t;

    private KeyListener kl = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_I) {
                observe = !observe;
                if (observe)
                    new Thread(() -> {observeCoord = new MapGui().selectMappoint(bodies[trackedObject].map);
                    System.out.println("variable confirmed");}).start();
                }
            if (e.getKeyCode() == KeyEvent.VK_UP)
                up = true;
            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                down = true;
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
                left = true;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                right = true;
            if (e.getKeyCode() == KeyEvent.VK_E)
                away = true;
            if (e.getKeyCode() == KeyEvent.VK_Q)
                toward = true;
            if (e.getKeyCode() == KeyEvent.VK_O)
                timewarp*=0.5;
            if (e.getKeyCode() == KeyEvent.VK_P)
                timewarp*=2;
            if (e.getKeyCode() == KeyEvent.VK_M) {
                trackedObject += 1;
                trackedObject %= bodies.length;
            }
            if (e.getKeyCode() == KeyEvent.VK_N) {
                trackedObject -= 1;
                trackedObject += bodies.length;
                trackedObject %= bodies.length;
            }
            if (e.getKeyCode() == KeyEvent.VK_G)
                showOrbit = !showOrbit;
            if (e.getKeyCode() == KeyEvent.VK_L)
                CelestialBody.lowQuality = !CelestialBody.lowQuality;
            if (e.getKeyCode() == KeyEvent.VK_F)
                sunGlare = !sunGlare;
            if (e.getKeyCode() == KeyEvent.VK_K)
                showStats = !showStats;

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP)
                up = false;
            if (e.getKeyCode() == KeyEvent.VK_DOWN)
                down = false;
            if (e.getKeyCode() == KeyEvent.VK_LEFT)
                left = false;
            if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                right = false;
            if (e.getKeyCode() == KeyEvent.VK_E)
                away = false;
            if (e.getKeyCode() == KeyEvent.VK_Q)
                toward = false;
        }
    };

    private double camX, camY, zoom, timewarp, fieldOfView;
    private Point3D camOff;
    private Point observeCoord;

    private boolean running = false, observe = false, up = false, down = false, left = false, right = false, toward = false, away = false, showOrbit = true, sunGlare = true, showStats = true;
    private CelestialBody[] bodies;
    private Circle[] circles;

    private int trackedObject;

    ZachGraphics w;
    Render rm;


    Central(CelestialBody[] cb, int maxPixel) {
        observeCoord = new Point(0,0);
        bodies = cb;

        camOff = new Point3D(0,0,-150);

        mP = maxPixel;
        camX = 0;
        camY = -0.5;
        zoom = 50;
        trackedObject = 0;
        timewarp = 0.01;
        w = new ZachGraphics("World Orbit Fortune 4 Simulator by Napkin Nexter",1000,1000, kl);
        fieldOfView = 1000;

        t = new Thread();

        rm = (Graphics g) -> {
            //if (!t.isAlive()) {
            //    t = new Thread(() -> {
                    if (bodies[0] == null)
                        return;
                    CelestialBody[] bodies = new CelestialBody[this.bodies.length];// = this.bodies;

                    for (int i = 0; i < bodies.length; i++) {
                        bodies[i] = new CelestialBody(this.bodies[i]);
                    }

                    //double camX = this.camX, camY = this.camY;
                    camOff = bodies[trackedObject].getPos().add(new Point3D(0, 0, -zoom).rotate2(-camX, -camY));
                    circles = new Circle[bodies.length];
                    Point3D camOff = new Point3D(this.camOff);
                    double camX = this.camX, camY = this.camY;




                    g.setColor(new Color(255, 255, 220, 100));
                    if (sunGlare) {
                        if (circles != null && circles[0] != null) {
                            g.fillOval(circles[0].x - 5, circles[0].y - 5, 10, 10);
                            g.fillRect(circles[0].x - 5, circles[0].y - 1, 10, 2);
                            g.fillRect(circles[0].x - 1, circles[0].y - 5, 2, 10);
                        }
                    }
                    if (showStats) {
                        g.setColor(bodies[trackedObject].color);
                        this.bodies[trackedObject].renderHelion(g, camOff, camX, camY, fieldOfView);
                    }
                    if (showOrbit)
                        for (int i = 0; i < bodies.length; i++)
                            if (zoom < bodies[i].apohelion * 250 && zoom > bodies[i].perihelion / 10)
                                this.bodies[i].renderOrbit(g, camOff, camX, camY, fieldOfView, circles, w.getDimensions());
                        //for (CelestialBody b : bodies)
                        //    if (zoom < b.apohelion * 250 && zoom > b.perihelion / 10)
                        //        b.renderOrbit(g, camOff, camX, camY, fieldOfView, circles);


                    int i = 0;
                    for (CelestialBody b : bodies) {
                        //try {
                        circles[i] = b.render(g, camOff, camX, camY, fieldOfView, circles, mP, bodies[3], w.getDimensions());
                        //} catch (NullPointerException ignored) {}
                        i++;
                    }
                        if (showStats && trackedObject < bodies.length) {
                            //try {
                            g.setColor(bodies[trackedObject].color);
                            g.drawString(bodies[trackedObject].name, w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 25);
                            g.drawString("Perihelion (gigameters (1,000 km)): " + bodies[trackedObject].perihelion, w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 45);
                            g.drawString("Aphelion (gm): " + bodies[trackedObject].apohelion, w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 65);
                            g.drawString("Semi Major Axis (gm): " + bodies[trackedObject].semiMajorAxis, w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 85);
                            g.drawString("Semi Minor Axis (gm): " + bodies[trackedObject].semiMinorAxis, w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 105);
                            g.drawString("Inclination (º): " + Math.toDegrees(bodies[trackedObject].inclination), w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 125);
                            g.drawString("Argument of Perihelion (º): " + Math.toDegrees(bodies[trackedObject].argumnentOfPerihelion), w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 145);
                            g.drawString("Longitude of Ascending Node (º): " + Math.toDegrees(bodies[trackedObject].longitudeOfAscendingNode), w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 165);
                            g.drawString("Mean Anomaly (º): " + Math.toDegrees(bodies[trackedObject].meanAnomaly), w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 185);
                            g.drawString("Eccentric Anomaly (º): " + Math.toDegrees(bodies[trackedObject].eccentricAnomaly), w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 205);
                            g.drawString("Orbital Velocity (km/s): " + Math.toDegrees(bodies[trackedObject].vel), w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 225);
                            try {
                                g.drawString("Sphere of Influence: " + Optional.of(bodies[trackedObject].sphereOfInfluence.name).orElse("None"), w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 245);
                            } catch (NullPointerException ignored) {
                                g.drawString("Sphere of Influence: None", w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 245);
                            }
                            g.drawString("Diameter (gm): " + bodies[trackedObject].size, w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 285);
                            g.drawString("Mass (Earths): " + bodies[trackedObject].mass, w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 305);
                            g.drawString("Mass (teragrams (1,000,000,000 kg)): " + (bodies[trackedObject].mass * 5972370000000000L), w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 325);
                            //g.drawString("Axis tilt ()" + (bodies[trackedObject].mass * 5972370000000000L), w.getDimensions().width / -2 + 15, (int) w.getDimensions().getHeight() / -2 + 325);
                            //} catch (ArrayIndexOutOfBoundsException ignored) {
                            //}

                    }
                //});
                //t.start();

            //}
        };

    }



    public synchronized void start() {

        running = true;
        while (running) {
            tick();
            render();
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        exit(0);
    }

    private synchronized void stop() {
        running = false;

    }
    private void tick() {
        if (observe && observeCoord != null) {
            //Point3D p = new Point3D(Math.sin((double)observeCoord.x/(double)bodies[trackedObject].map.getWidth()*PI*2D)*Math.cos((double)observeCoord.y/(double)bodies[trackedObject].map.getHeight()*PI*2D),Math.sin((double)observeCoord.y/bodies[trackedObject].map.getHeight()*PI*2),Math.cos((double)observeCoord.x/bodies[trackedObject].map.getWidth()*PI*2)*Math.cos((double)observeCoord.y/bodies[trackedObject].map.getHeight()*PI*2)).rotate((Objects.isNull(bodies[trackedObject].sphereOfInfluence) ? -(bodies[3].meanAnomaly * bodies[3].tidalResonance)/bodies[trackedObject].tidalResonance : -bodies[trackedObject].meanAnomaly * bodies[trackedObject].tidalResonance),bodies[trackedObject].axisTilt);
            if (!Objects.isNull(bodies[trackedObject].map)) {
                Point3D p = new Point3D(0, 0, 1).rotate2((double) -observeCoord.x / (double) bodies[trackedObject].map.getWidth() * PI * 2D - PI/2, (double) -observeCoord.y / (double) bodies[trackedObject].map.getHeight() * PI + PI/2).rotate((Objects.isNull(bodies[trackedObject].sphereOfInfluence) ? -(bodies[3].meanAnomaly * bodies[3].tidalResonance) / bodies[trackedObject].tidalResonance : -bodies[trackedObject].meanAnomaly * bodies[trackedObject].tidalResonance), 2 * PI * bodies[trackedObject].axisTilt + bodies[trackedObject].inclination);

                camX = (p.z > 0 ? bodies[trackedObject].observept*atan(p.x / p.z) : bodies[trackedObject].observept*atan(p.x / p.z) + PI);

                //if ((camY+90) % 360*8 > 180)
                camY = (-atan(p.y / sqrt(p.x * p.x + p.z * p.z)));
            }
            //System.out.println((double)-observeCoord.y/(double)bodies[trackedObject].map.getHeight()*PI);
        }

        if (up && camY > -Math.PI/2+0.01) {
            camY -= 0.01;
            observeCoord.y -= 1;
        }
        if (down && camY < Math.PI/2-0.01) {
            camY += 0.01;
            observeCoord.y += 1;
        }
        if (left) {
            camX -= 0.01;
            observeCoord.x -= 1;
        }

        if (right) {
            camX += 0.01;
            observeCoord.x += 1;
        }
        if (toward)
            zoom*=0.99;
        if (away)
            zoom*=1.01111;

        for (CelestialBody c : bodies) {
            c.tick(timewarp);
        }

        //camX += 0.01;

        //try {
            //camOff = bodies[trackedObject].getPos().add(new Point3D(0, 0, -zoom).rotate2(-camX, -camY));
        //} catch (ArrayIndexOutOfBoundsException ignored) {}

    }

    private void render() {
        w.render(rm,camX,camY,fieldOfView);
    }
}
