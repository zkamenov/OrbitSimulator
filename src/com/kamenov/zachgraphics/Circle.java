/*
 * Copyright (c) 2020. Ian Zachary Kamenov. All Rights Reserved.
 */

package com.kamenov.zachgraphics;

import java.awt.*;

public class Circle {
    public int x,y,radius;
    public double z;
    public Circle(int x, int y, int radius, double z) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.z = z;
    }

    boolean isPointInCircle(Point p) {
        return (radius>Math.sqrt(Math.pow(p.x-x,2)+Math.pow(p.y-y,2)));
    }
}
