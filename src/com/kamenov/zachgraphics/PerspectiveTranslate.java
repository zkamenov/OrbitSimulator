package com.kamenov.zachgraphics;

import java.awt.*;

public interface PerspectiveTranslate {
    static Point trans3D2(Point3D p, double camx, double camy, double fOV) {
        p = p.rotate(camx,camy);

        if (p.z > 0)
            return new Point((int)(p.x/p.z*fOV),(int)(p.y/p.z*fOV));
        else
            return null;
    }
}
