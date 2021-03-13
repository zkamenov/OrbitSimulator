/*
 * Copyright (c) 2020. Ian Zachary Kamenov. All Rights Reserved.
 */


package com.kamenov.zachgraphics;

import static java.lang.Math.*;

public class Point3D {
    public double x, y, z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Point3D p) {
        x = p.x;
        y = p.y;
        z = p.z;
    }

    public Point3D rotate(double rx, double ry) {
        return yr(x*cos(rx)+z*sin(rx),y,z*cos(rx)-x*sin(rx),ry);
    }
    private Point3D yr(double x, double y, double z, double ry) {
        return new Point3D(x,y*cos(ry)+z*sin(ry),z*cos(ry)-y*sin(ry));
    }

    public Point3D rotate2(double rx, double ry) {
        return xr(x,y*cos(ry)+z*sin(ry),z*cos(ry)-y*sin(ry),rx);
    }
    private Point3D xr(double x, double y, double z, double rx) {
        return new Point3D(x*cos(rx)+z*sin(rx),y,z*cos(rx)-x*sin(rx));
    }
    public Point3D add(Point3D b) {
        return new Point3D(x+b.x,y+b.y,z+b.z);
    }
    public Point3D subtract(Point3D b) {
        return new Point3D(x-b.x,y-b.y,z-b.z);
    }
}
