/*
 * Copyright (c) 2020. Ian Zachary Kamenov. All Rights Reserved.
 */

package com.kamenov;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;

public class Launcher extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton launchButton;
    private JTextField a128TextField;
    private JTextPane creditsSolarSystemScopeTextPane;
    private JLabel aztx;
    private JLabel abz;
    private JLabel arz;
    private JLabel aba;
    private JPanel jp1;
    private JPanel jp2;

    public Launcher() {
        setContentPane(contentPane);
        setName("World Orbit Fortune 4");
        getRootPane().setDefaultButton(buttonOK);

        try {
            //Taskbar.getTaskbar().setIconImage(ImageIO.read(Launcher.class.getResourceAsStream("/WOF4OrbitSimulator.png")));
            setIconImage(ImageIO.read(Launcher.class.getResourceAsStream("/WOF4OrbitSimulator.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        launchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLaunch();
            }
        });
    }

    private void onLaunch() {
        new Thread(() -> {
            Main.main(new String[]{"null",a128TextField.getText()});
        }).start();
    }

    private void onOK() {

        JFileChooser jfs = new JFileChooser();
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("Properties File", "properties");
        //jfs.setFileFilter(filter);
        int i = jfs.showOpenDialog(this);
        new Thread(() -> {

            if (i == JFileChooser.APPROVE_OPTION)
                Main.main(new String[]{jfs.getSelectedFile().getPath(),a128TextField.getText()});
        }).start();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        Launcher dialog = new Launcher();
        dialog.pack();
        dialog.setVisible(true);
    }
}
