package com.val.wallboy;


import com.val.wallboy.events.PubSub;
import com.val.wallboy.source.SourceControl;
import com.val.wallboy.utils.TrayController;

import java.util.Timer;

public class Main {
    private static final long min = 60000;

    public static void main(String[] args) {
        long time = 10;
        try {
            time = Long.parseLong(args[0]);
        } catch (Exception e){
            //ignore
        }
        PubSub.resetRegistrar();

        //Load ImageController
        ImageController iControl = new ImageController();

        //Update popular images list
        SourceControl.init();
        iControl.updateImages(SourceControl.getWallpapers("popular"));
        iControl.updateImages(SourceControl.getWallpapers("newest"));
        iControl.updateImages(SourceControl.getWallpapers("highest_rated"));
        iControl.updateImages(SourceControl.getWallpapers("random"));
        iControl.updateImages(SourceControl.getWallpapers("by_views"));
        iControl.updateImages(SourceControl.getWallpapers("by_favorites"));
        iControl.updateImages(SourceControl.getWallpapers("featured"));

        //String path = SourceControl.saveImage(images.get(5).url_image);

        //Set the Wallpaper
        Timer walli = new Timer();
        walli.schedule(new WallThread(iControl), 0, min * time);
        TrayController.registerTray(iControl);
    }


}
