package com.val.wallboy;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

import java.util.HashMap;

public interface User32 extends Library {
    int SPI_SETDESKWALLPAPER = 0x0014;
    int SPI_GETDESKWALLPAPER = 0x0073;
    int SPIF_UPDATEINIFILE = 0x01;
    int SPIF_SENDWININICHANGE = 0x02;

    //User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, new HashMap<Object, Object>() {
        {
            put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
            put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
        }
    });

    boolean SystemParametersInfo(int uActtion, int SPIF_UPDATEINIFILE, String path, int SPIF_SENDWININICHANGE);
}