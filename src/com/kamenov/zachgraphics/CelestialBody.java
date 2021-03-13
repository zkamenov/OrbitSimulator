/*
 * Copyright (c) 2020. Ian Zachary Kamenov. All Rights Reserved.
 */

package com.kamenov.zachgraphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Objects;

import static java.lang.Math.*;

public class CelestialBody {
    Point3D pos;
    public double mass, perihelion, apohelion, inclination, argumnentOfPerihelion, longitudeOfAscendingNode, meanAnomaly, size, axisTilt, tidalResonance,
            semiMajorAxis, semiMinorAxis, eccentricity, eccentricAnomaly, vel; //Calculated Values
    public String name;
    public BufferedImage map;
    public CelestialBody sphereOfInfluence;
    public Color color;
    public static boolean lowQuality = false;
    public int observept;

    public CelestialBody(CelestialBody c) {
        name = c.name;
        mass = c.mass;
        size = c.size;
        perihelion = c.perihelion;
        apohelion = c.apohelion;
        inclination = c.inclination;
        argumnentOfPerihelion = c.argumnentOfPerihelion;
        longitudeOfAscendingNode = c.longitudeOfAscendingNode;
        meanAnomaly = c.meanAnomaly;
        sphereOfInfluence = c.sphereOfInfluence;
        color = c.color;
        axisTilt = c.axisTilt;
        tidalResonance = c.tidalResonance;
        map = c.map;
        pos = new Point3D(c.pos);
        observept = c.observept;
    }

    public CelestialBody(String name, double mass, double size, double perihelion, double apohelion, double inclination, double argumnentOfPerihelion, double longitudeOfAscendingNode, double meanAnomaly, CelestialBody sphereOfInfluence, Color color, double axisTilt, double tidalResonance, int observept, BufferedImage map) {
        this.name = name;
        this.size = size;
        this.mass = mass;
        this.perihelion = perihelion;
        this.apohelion = apohelion;
        this.inclination = inclination;
        this.argumnentOfPerihelion = argumnentOfPerihelion;
        this.longitudeOfAscendingNode = longitudeOfAscendingNode;
        this.meanAnomaly = meanAnomaly;
        this.sphereOfInfluence = sphereOfInfluence;
        this.color = color;
        this.axisTilt = axisTilt;
        this.tidalResonance = tidalResonance;
        this.map = map;
        this.observept = observept;
    }

    public Circle render(Graphics g, Point3D camOff, double camX, double camY, double fOV, Circle[] c, int mP, CelestialBody object3, Dimension window) {
        g.setColor(color);



        Point3D z = new Point3D(pos.x-camOff.x,pos.y-camOff.y,pos.z-camOff.z);
        Point p = PerspectiveTranslate.trans3D2(z,camX,camY, fOV);



        double r = pos.subtract(camOff).rotate(camX,camY).z;
        for (Circle i : c)
            if (i != null && p != null && i.isPointInCircle(new Point(p.x,p.y)))
                if (i.z < r)
                    return new Circle(p.x, p.y, (int) ceil(size / (z.rotate(camX, camY).z/*-2*size*/) * fOV) / 2, r);

        double w3 = 1-Math.sqrt(z.rotate(camX,camY).z)/(Math.sqrt(z.rotate(camX,camY).z)+1);


        Point3D v = pos.subtract(camOff);

        double viewX, viewY;

        viewX = v.z > 0 ? -atan(v.x/v.z) : -atan(v.x/v.z)+PI;

        //if ((camY+90) % 360 > 180)
            viewY = -atan(v.y/sqrt(v.x*v.x + v.z*v.z));
        //else {
        //    viewY = atan(v.y / sqrt(v.x * v.x + v.z * v.z)) + PI;
        //    viewX += PI;
        //}
        if (z.rotate(camX, camY).z != 0 && p != null && abs(p.x) - (int) (size / (z.rotate(camX, camY).z/*-2*size*/) * fOV) / 2 < window.width/2 && abs(p.y) - (int) (size / (z.rotate(camX, camY).z/*-2*size*/) * fOV) / 2 < window.height/2) {
            if (map != null)
                g.drawImage(SphereMap.generateSphereMap(map, viewX, viewY, axisTilt + inclination, Objects.isNull(sphereOfInfluence) ? (object3.meanAnomaly * object3.tidalResonance)/tidalResonance : meanAnomaly * tidalResonance, Math.min(mP, (int) Math.ceil(size / (z.rotate(camX, camY).z/*-2*size*/) * fOV))), p.x - (int) (size / (z.rotate(camX, camY).z/*-2*size*/) * fOV) / 2, p.y - (int) (size / (z.rotate(camX, camY).z/*-2*size*/) * fOV) / 2, (int) Math.ceil(size / (z.rotate(camX, camY).z/*-2*size*/) * fOV), (int) Math.ceil(size / (z.rotate(camX, camY).z/*-2*size*/) * fOV), null);
            else
                g.fillOval(p.x - (int) (size / (z.rotate(camX, camY).z/*-2*size*/) * fOV) / 2, p.y - (int) (size / (z.rotate(camX, camY).z/*-2*size*/) * fOV) / 2, (int) Math.ceil(size / (z.rotate(camX, camY).z/*-2*size*/) * fOV), (int) Math.ceil(size / (z.rotate(camX, camY).z/*-2*size*/) * fOV));


            return new Circle(p.x, p.y, (int) ceil(size / (z.rotate(camX, camY).z/*-2*size*/) * fOV) / 2, r);
        }
        return new Circle(999999999,1,0,1);
    }
    public void renderOrbit(Graphics g, Point3D camOff, double camX, double camY, double fOV, Circle[] c, Dimension window) {
        g.setColor(color);
        int qlt = 361;
        if (lowQuality)
            qlt = 36;
        for (int i = 361/qlt; i < 361; i+= 361/qlt) {
            g.setColor(new Color((int) (color.getRed() * pow(0.998, i - 1)), (int) (color.getGreen() * pow(0.998, i - 1)), (int) (color.getBlue() * pow(0.998, i - 1))));

            Point3D arca, arcb;

            Point3D c1 = new Point3D(semiMajorAxis * (cos(Math.toRadians(-i) + eccentricAnomaly) - eccentricity), 0,
                    semiMinorAxis * sin(Math.toRadians(-i) + eccentricAnomaly));

            Point3D c2 = new Point3D(c1.x * cos(argumnentOfPerihelion) + c1.z * sin(argumnentOfPerihelion), c1.y,
                    c1.z * cos(argumnentOfPerihelion) - c1.x * sin(argumnentOfPerihelion));

            Point3D c3 = new Point3D(c2.x, c2.z * sin(inclination),
                    c2.z * cos(inclination));
            Point3D c4 = new Point3D(c3.x * cos(longitudeOfAscendingNode) + c3.z * sin(longitudeOfAscendingNode), c3.y,
                    c3.z * cos(longitudeOfAscendingNode) - c3.x * sin(longitudeOfAscendingNode));
            if (sphereOfInfluence == null)
                arca = c4;
            else
                arca = new Point3D(c4.x + sphereOfInfluence.pos.x, c4.y + sphereOfInfluence.pos.y, c4.z + sphereOfInfluence.pos.z);

            //TODO: calc ArcB

            c1 = new Point3D(semiMajorAxis * (cos(Math.toRadians(-i + 361/qlt) + eccentricAnomaly) - eccentricity), 0,
                    semiMinorAxis * sin(Math.toRadians(-i + 361/qlt) + eccentricAnomaly));

            c2 = new Point3D(c1.x * cos(argumnentOfPerihelion) + c1.z * sin(argumnentOfPerihelion), c1.y,
                    c1.z * cos(argumnentOfPerihelion) - c1.x * sin(argumnentOfPerihelion));

            c3 = new Point3D(c2.x, c2.z * sin(inclination),
                    c2.z * cos(inclination));
            c4 = new Point3D(c3.x * cos(longitudeOfAscendingNode) + c3.z * sin(longitudeOfAscendingNode), c3.y,
                    c3.z * cos(longitudeOfAscendingNode) - c3.x * sin(longitudeOfAscendingNode));
            if (sphereOfInfluence == null)
                arcb = c4;
            else
                arcb = new Point3D(c4.x + sphereOfInfluence.pos.x, c4.y + sphereOfInfluence.pos.y, c4.z + sphereOfInfluence.pos.z);

            boolean draw = true;


            Point p1 = PerspectiveTranslate.trans3D2(arca.subtract(camOff), camX, camY,fOV);
            Point p2 = PerspectiveTranslate.trans3D2(arcb.subtract(camOff), camX, camY,fOV);

            //double r1 = pos.subtract(arca.subtract(camOff)).rotate(camX,camY).z;
            //double r2 = pos.subtract(arcb.subtract(camOff)).rotate(camX,camY).z;

            //for (Circle j : c)
            //    try {
            //        if (j != null && p1 != null && p2 != null && (j.isPointInCircle(new Point(p1.x, p1.y)) || j.isPointInCircle(new Point(p2.x, p2.y))))
            //            if (j.z < r1 || j.z < r2)
            //                return;
            //    } catch (NullPointerException ignored) {}

            if (p1 != null && p2!= null && (abs(p1.x) < window.getWidth()/2 && abs(p1.y) < window.getHeight()/2 || abs(p2.x) < window.getWidth()/2 && abs(p2.y) < window.getHeight()/2))
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    public void renderHelion(Graphics g, Point3D camoff, double camX, double camY, double fOV) {
        g.setColor(color);
        if (sphereOfInfluence == null)
            return;
        Point3D p = sphereOfInfluence.pos.add(new Point3D(perihelion,0,0).rotate(argumnentOfPerihelion,inclination).rotate(longitudeOfAscendingNode,0)).subtract(camoff);
        Point3D a = sphereOfInfluence.pos.add(new Point3D(-apohelion,0,0).rotate(argumnentOfPerihelion,inclination).rotate(longitudeOfAscendingNode,0)).subtract(camoff);
        Point e = PerspectiveTranslate.trans3D2(p,camX,camY, fOV);
        Point f = PerspectiveTranslate.trans3D2(a,camX,camY, fOV);
        if (e != null) {
            g.fillRect(e.x-2,e.y-2,4,4);
            g.drawString("Perihelion", e.x, e.y);
        }
        if (f != null) {
            g.fillRect(f.x-2,f.y-2,4,4);
            g.drawString("Aphelion", f.x+2, f.y-2);
        }

    }

    public void tick(double timeWarp) {

        //Find calculated values
        semiMajorAxis = (perihelion+apohelion)/2;
        eccentricity = (apohelion/semiMajorAxis)-1;
        semiMinorAxis = semiMajorAxis*sqrt(1-eccentricity*eccentricity);

        //TODO: Draw Orbit
        if (sphereOfInfluence == null)
            meanAnomaly += 0/(6.28318*sqrt(pow(semiMajorAxis,3)));
        else
            meanAnomaly += timeWarp/(6.28318*sqrt(pow(semiMajorAxis,3)/sphereOfInfluence.mass));

        eccentricAnomaly = eccentricAnomaly(eccentricity, meanAnomaly, 100);

        Point3D c1 = new Point3D(semiMajorAxis*(cos(eccentricAnomaly)-eccentricity),0,
                                 semiMinorAxis*sin(eccentricAnomaly));

        Point3D c2 = new Point3D(c1.x*cos(argumnentOfPerihelion) + c1.z*sin(argumnentOfPerihelion),c1.y,
                                    c1.z*cos(argumnentOfPerihelion) - c1.x*sin(argumnentOfPerihelion));

        Point3D c3 = new Point3D(c2.x, c2.z*sin(inclination),
                                       c2.z*cos(inclination));
        Point3D c4 = new Point3D(c3.x*cos(longitudeOfAscendingNode) + c3.z*sin(longitudeOfAscendingNode),c3.y,
                c3.z*cos(longitudeOfAscendingNode) - c3.x*sin(longitudeOfAscendingNode));
        if (sphereOfInfluence == null)
            pos = c4;
        else
            pos = new Point3D(c4.x + sphereOfInfluence.pos.x,c4.y + sphereOfInfluence.pos.y,c4.z + sphereOfInfluence.pos.z);
        double r = sqrt(c4.x*c4.x+c4.y*c4.y+c4.z*c4.z);
        if (sphereOfInfluence == null)
            vel = 0;
        else
            vel = sqrt((2/r-1/semiMajorAxis)*sphereOfInfluence.mass)*30.3/2749.712445;
            //vel = (2*PI*semiMajorAxis)/(6.28318*sqrt(pow(semiMajorAxis,3)/sphereOfInfluence.mass))*(1 - 1*Math.pow(eccentricity,2)/4 - 3*Math.pow(eccentricity,4)/64 - 5*Math.pow(eccentricity,6)/256 - 175*Math.pow(eccentricity,8)/16384);
    }

    private double eccentricAnomaly(double eccentricity, double meanAnomaly, int iterations) {
        double eccentricAnomaly = meanAnomaly;
        for (int i = 0; i < iterations; i++) {
            eccentricAnomaly = meanAnomaly + eccentricity*sin(eccentricAnomaly);
        }
        return eccentricAnomaly;
    }

    public Point3D getPos() {
        return pos;
    }

    public void setSOI(CelestialBody cb) {
        sphereOfInfluence = cb;
    }

    private double Circle(double j) {
        return Math.sqrt(1-j*j);
    }
}
