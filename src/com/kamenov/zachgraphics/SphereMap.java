/*
 * Copyright (c) 2021. Ian Zachary Kamenov. All Rights Reserved.
 */

package com.kamenov.zachgraphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import static java.lang.Math.*;

public interface SphereMap {
    static BufferedImage generateSphereMap(BufferedImage visual, double camX, double camY, double axisTilt, double rotateStage, int diameter) {
        rotateStage *= 2 * PI;
        diameter = 2*(int)Math.ceil((double)diameter/2);
        int hd = diameter/2;


        //axisTilt = Math.toDegrees(axisTilt);
        //camX = Math.toDegrees(camX);
        //camY = Math.toRadians(camY);


        BufferedImage bi = new BufferedImage(diameter,diameter,BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < diameter; i++) {
            double arcy = sqrt(hd*hd-(i-hd)*(i-hd));
            double asq = arcy*arcy;
            for (int j = hd-(int)arcy; j < hd+arcy; j++) {
                Point3D p = new Point3D((double)i/hd-1,(double)j/hd-1,sqrt(asq-(j-hd)*(j-hd))/hd).rotate2(camX,camY).rotate2(-rotateStage/PI/2,axisTilt);
                //bi.setRGB(i,(int)j,new Color(255,255,255).getRGB());
                double psy = asin(p.y);
                if (p.z>0)
                    bi.setRGB(i,j,visual.getRGB((int)((atan(p.x/p.z)/PI+1.5)/2*(double)visual.getWidth()), (int)((psy/PI+0.5)*(double)visual.getHeight())));
                else
                    bi.setRGB(i,j,(visual.getRGB((int)((atan(p.x/p.z)/PI+0.5)/2*(double)visual.getWidth()), (int)((psy/PI+0.5)*(double)visual.getHeight()))));
            }
        }
        //System.out.println((int)((atan(p.x/p.z)/PI+0.5)*visual.getWidth()));
        //bi.setRGB(0,0,visual.getRGB(0,0));
        return bi;
    }
}
