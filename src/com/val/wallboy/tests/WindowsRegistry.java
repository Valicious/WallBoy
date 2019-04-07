package com.val.wallboy.tests;

import com.sun.jna.platform.win32.Advapi32Util;

import static com.sun.jna.platform.win32.WinReg.HKEY_CURRENT_USER;

/**
 * @author Oleg Ryaboy, based on work by Miguel Enriquez
 */
public class WindowsRegistry {

    public static void main(String[] args) {
       // String path =
        byte[] values = Advapi32Util.registryGetBinaryValue(
                HKEY_CURRENT_USER,
                "Control Panel\\Desktop",
                "TranscodedImageCache"
        );

        System.out.println(ByteArrayToString(values));

        /*Advapi32Util.registrySetBinaryValue(HKEY_CURRENT_USER,
                "Control Panel\\Desktop",
                "TranscodedImageCache",
                path
        );*/
    }

    public static String ByteArrayToString(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}