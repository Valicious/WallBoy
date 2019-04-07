package com.val.wallboy.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.val.wallboy.model.Image;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SourceControl {
    private static final String scriptAddress = "https://wall.alphacoders.com/api2.0/get.php";
    private static final String authenticationCode = "ea2f8e60a1c3176b270c9eb9641f1c02";

    private static final String urlAuth = "auth=";
    private static final String urlMethod = "method=";
    private static final String urlPage = "page=";
    private static final String urlInfo = "info_level=";

    private static Gson gson;

    public static void init() {
        gson = new GsonBuilder().create();
    }

    public static ArrayList<Image> getWallpapers(String choice) {
        StringBuilder bobURL = new StringBuilder();
        try {

            bobURL.append(scriptAddress)
                    .append("?")
                    .append("auth=")
                    .append(authenticationCode)
                    .append("&")
                    .append(urlMethod);
            switch (choice) {
                case "popular": {
                    bobURL.append("popular");
                }
            }

            URL url = new URL(bobURL.toString());
            URLConnection connection = url.openConnection();
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = in.readLine();
            if (result == null) return null;
            System.out.println(result);

            LinkedTreeMap<String, ArrayList<LinkedTreeMap<String, String>>> root = gson.fromJson(result, LinkedTreeMap.class);
            ArrayList<LinkedTreeMap<String, String>> child = root.get("wallpapers");
            ArrayList<Image> images = new ArrayList<>();
            child.forEach(newImage -> {
                Image newOne = new Image(
                        newImage.get("id"),
                        newImage.get("width"),
                        newImage.get("height"),
                        newImage.get("file_type"),
                        newImage.get("file_size"),
                        newImage.get("url_image"),
                        newImage.get("url_thumb"),
                        newImage.get("url_page")
                );
                images.add(newOne);
            });


            in.close();
            return images;
        } catch (MalformedURLException e) {
            System.out.println("Unable to create new URL for: " + bobURL.toString());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to connect to URL for: " + bobURL.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static String saveImage(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);

            InputStream in = new BufferedInputStream(getInputStream(url));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();

            String path = "images/temp.bmp";
            File newImageFile = new File(path);
            newImageFile.createNewFile(); // if file already exists will do nothing
            FileOutputStream fos = new FileOutputStream(newImageFile, false);
            fos.write(response);
            fos.close();
            /*BufferedImage image = ImageIO.read(newImageFile);
            ImageIO.write(image, "bmp", newImageFile);*/

            return newImageFile.getAbsolutePath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static InputStream getInputStream(URL url) {
        try {
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

            return httpcon.getInputStream();
        } catch (IOException e) {
            String error = e.toString();
            throw new RuntimeException(e);
        }
    }
}
