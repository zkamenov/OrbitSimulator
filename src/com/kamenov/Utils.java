/*
 * Copyright (c) 2020. Ian Zachary Kamenov. All Rights Reserved.
 */

package com.kamenov;

import java.io.*;
import java.util.Properties;
import java.util.Set;

class Utils {
    static String[][] getData() throws IOException {
        Properties props = new Properties();
        InputStream fs = Utils.class.getResourceAsStream("/CelestialBodies.properties");
        props.load(fs);
        fs.close();
        boolean g = true;
        String[][] str = new String[props.keySet().size()][15];
        for (int i = 0; i < props.keySet().size();i++) {
            str[i] = props.getProperty(Integer.toString(i)).split(",");

        }
        return str;
    }
    static String[][] getData(String url) throws IOException {
        Properties props = new Properties();
        InputStream fs = new FileInputStream(url);
        props.load(fs);
        fs.close();
        boolean g = true;
        String[][] str = new String[props.keySet().size()][15];
        for (int i = 0; i < props.keySet().size();i++) {
            str[i] = props.getProperty(Integer.toString(i)).split(",");

        }
        return str;
    }
    static InputStream getFile() {
        return Utils.class.getResourceAsStream("/CelestialBodies.properties");
    }
}
