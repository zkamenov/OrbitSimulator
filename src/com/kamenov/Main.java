package com.kamenov;

import com.kamenov.zachgraphics.CelestialBody;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        String[][] l;
        try {
            l = Utils.getData();
            if (!args[0].equals("null"))
                l = Utils.getData(args[0]);
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"The program wasn't able to launch due to this error:\n\n" + e.getStackTrace(),"Unable to Launch",JOptionPane.ERROR_MESSAGE);
            return;
        }



        CelestialBody[] celestialBodies = new CelestialBody[l.length];
        int i = 0;
        try {
            for (String[] g : l) {
                BufferedImage map = null;//new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
                URL target;
                String o = System.getProperty("file.separator");
                if (args[0].equals("null"))
                    target = Main.class.getResource("/maps/" + i + ".png");
                else
                    target = new File(new File(args[0]).getParentFile().toString()+o+"maps"+o+i+".png").toURL();
                    if (target != null)
                        System.out.println("Texture map for "+ g[0] + " (" + target + ") loaded!");
                try {
                    //if (new File(Main.class.getResource(target).toString()).exists())
                    map = ImageIO.read(target);

                } catch (Exception e) {
                    //e.printStackTrace();
                }

                if (g[9].equals("null"))
                    celestialBodies[i] = new CelestialBody(g[0], Double.parseDouble(g[1]), Double.parseDouble(g[2]), Double.parseDouble(g[3]), Double.parseDouble(g[4]), Math.toRadians(Double.parseDouble(g[5])), Math.toRadians(Double.parseDouble(g[6])), Math.toRadians(Double.parseDouble(g[7])), Math.toRadians(Double.parseDouble(g[8])), null, new Color(Integer.parseInt(g[10]), Integer.parseInt(g[11]), Integer.parseInt(g[12])), Math.toRadians(Double.parseDouble(g[13])), Double.parseDouble(g[14]), Integer.parseInt(g[15]), map);
                else
                    celestialBodies[i] = new CelestialBody(g[0], Double.parseDouble(g[1]), Double.parseDouble(g[2]), Double.parseDouble(g[3]), Double.parseDouble(g[4]), Math.toRadians(Double.parseDouble(g[5])), Math.toRadians(Double.parseDouble(g[6])), Math.toRadians(Double.parseDouble(g[7])), Math.toRadians(Double.parseDouble(g[8])), celestialBodies[Integer.parseInt(g[9])], new Color(Integer.parseInt(g[10]), Integer.parseInt(g[11]), Integer.parseInt(g[12])), Math.toRadians(Double.parseDouble(g[13])), Double.parseDouble(g[14]), Integer.parseInt(g[15]), map);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"The program wasn't able to launch due to this error:\n\n" + e.getStackTrace(),"Unable to Launch",JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Central(celestialBodies,Integer.parseInt(args[1])).start();
    }
}
