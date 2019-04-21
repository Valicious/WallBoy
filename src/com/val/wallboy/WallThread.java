package com.val.wallboy;

import com.val.wallboy.events.PubSub;
import com.val.wallboy.model.Image;

import java.util.TimerTask;

public class WallThread extends TimerTask {

    private ImageController ic;

    public WallThread(ImageController ic) {
        this.ic = ic;
        PubSub.subscribe("nextImage", args -> run());
    }

    @Override
    public void run() {
        Image image = ic.getRandomImage();
        if (image == null) {
            System.out.println("No image found. Waiting for image!");
        } else {
            System.out.println("Setting wallpaper to: " + image.url_image);
            User32.INSTANCE.SystemParametersInfo(User32.SPI_SETDESKWALLPAPER, User32.SPIF_UPDATEINIFILE, image.path, User32.SPIF_SENDWININICHANGE);
        }
        System.out.println("Timer Sleeping");
    }
}
