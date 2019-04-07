package com.val.wallboy;


import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;
import com.val.wallboy.model.Image;
import com.val.wallboy.source.SourceControl;
import com.val.wallboy.tests.WinRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        //TODO get access to wallpapers
        //TODO get wallpaper
        //TODO start wait timer

        /*String value = WinRegistry.readString(
                WinRegistry.HKEY_CURRENT_USER,
                "Control Panel\\Desktop",
                "TranscodedImageCache_000");
        System.out.println("value= " + value);
*/


        SourceControl.init();
        ArrayList<Image> images = SourceControl.getWallpapers("popular");
        //WallpaperChanger.change(images.get(5).url_image);
        String path = SourceControl.saveImage(images.get(5).url_image);
        User32.INSTANCE.SystemParametersInfo(0x0014, 0, path, 1);
    }



    public interface User32 extends Library {
        long SPI_SETDESKWALLPAPER = 20;
        long SPIF_UPDATEINIFILE = 0x01;
        long SPIF_SENDWININICHANGE = 0x02;

        //User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, new HashMap<Object, Object>() {
            {
                put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
                put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
            }
        });

        boolean SystemParametersInfo(int SPI_SETDESKWALLPAPER, int SPIF_UPDATEINIFILE, String path, int SPIF_SENDWININICHANGE);
    }
}
