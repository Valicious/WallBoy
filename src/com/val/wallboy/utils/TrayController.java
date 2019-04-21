package com.val.wallboy.utils;

import com.val.wallboy.ImageController;

import java.awt.*;

public class TrayController {
    public static void registerTray(ImageController iControl) {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported !!! ");
            return;
        }
        SystemTray systemTray = SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().getImage("val.png");
        //popupmenu
        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem actionHate = new MenuItem("Hate Image");
        actionHate.addActionListener(e -> {
            //JOptionPane.showMessageDialog(null, "Image is removed");
            iControl.hateImage();
        });
        trayPopupMenu.add(actionHate);

        MenuItem actionLove = new MenuItem("Love Image");
        actionLove.addActionListener(e -> iControl.loveImage());
        //actionLove.addActionListener(e -> JOptionPane.showMessageDialog(null, iControl.loveImage()));
        trayPopupMenu.add(actionLove);

        MenuItem actionNext = new MenuItem("Next Image");
        actionNext.addActionListener(e -> iControl.nextImage());
        trayPopupMenu.add(actionNext);

        MenuItem actionBlackScreen = new MenuItem("Black Screen");
        actionBlackScreen.addActionListener(e -> iControl.blackScreen());
        //actionLove.addActionListener(e -> JOptionPane.showMessageDialog(null, iControl.loveImage()));
        trayPopupMenu.add(actionBlackScreen);

       /* MenuItem actionPrintLove = new MenuItem("Next Image");
        actionPrintLove.addActionListener(e -> iControl.nextImage());
        trayPopupMenu.add(actionPrintLove);

        MenuItem actionPrintJate = new MenuItem("Next Image");
        actionPrintJate.addActionListener(e -> iControl.nextImage());
        trayPopupMenu.add(actionPrintJate);*/

        MenuItem close = new MenuItem("Close");
        close.addActionListener(e -> {
            iControl.saveInstance();
            System.exit(0);
        });
        trayPopupMenu.add(close);

        //setting tray icon
        TrayIcon trayIcon = new TrayIcon(image, "SystemTray Demo", trayPopupMenu);
        //adjust to default size as per system recommendation
        trayIcon.setImageAutoSize(true);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }

    }
}
