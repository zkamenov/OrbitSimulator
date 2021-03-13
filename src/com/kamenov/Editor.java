/*
 * Copyright (c) 2020. Ian Zachary Kamenov. All Rights Reserved.
 */

package com.kamenov;



import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

public class Editor extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JScrollPane scrollpanel;
    private JPanel jplogo;
    private JButton restoreToDefaultsButton;
    private JPanel panel;

    public Editor() {

        panel = new JPanel();

        try {

            Properties p = new Properties();
            p.load(Editor.class.getResourceAsStream("/CelestialBodies.properties"));
            jplogo.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            for (Object k : p.keySet()) {
                try {
                    System.out.println(Integer.parseInt(p.getProperty((String) k)));
                } catch (Exception ignored) {
                    gbc.gridy += 1;
                    String l = (String) k;
                    System.out.println(p.getProperty(l));
                    JPanel jp = new JPanel();
                    jp.setLayout(new GridBagLayout());


                    JTextField jtf = new JTextField(p.getProperty(l));
                    JButton jb = new JButton("Remove");
                    JButton sb = new JButton("Save");

                    sb.addActionListener(e -> p.setProperty(l,jtf.getText()));

                    jp.add(jb);
                    jp.add(sb);
                    jp.add(jtf);

                    jplogo.add(jp, gbc);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

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
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
        restoreToDefaultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileInputStream f = new FileInputStream(Editor.class.getResourceAsStream("/Backup.properties").toString());
                    FileOutputStream o = new FileOutputStream(new File(Editor.class.getResource("/CelestialBodies.properties").toString()));
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = f.read(buffer)) > 0)
                        o.write(buffer,0,length);

                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


            }
        });
    }

    private void onOK() {
        // add your code here
        Main.main(new String[]{});

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        JFrame dialog = new Editor();
        dialog.pack();
        dialog.setVisible(true);
    }
}
