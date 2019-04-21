package com.val.wallboy.source;

import com.val.wallboy.events.PubSub;
import com.val.wallboy.model.Image;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ImageDownloader {
    private static final String imgPath = "images/";

    public static void downloadImages(List<Image> newImages) {
        new Thread(() -> {
            newImages.forEach(ImageDownloader::downloadImage);
            System.out.println("All images are downloaded for this section!");
        }).start();
    }

    public static void downloadImage(Image curImg) {
        URL url = null;
        try {
            url = new URL(curImg.url_image);

            String path = curImg.id + "." + curImg.file_type;
            File newImageFile = new File(imgPath + path);
            if (newImageFile.exists()) {
                //System.out.println("Image for \"" + curImg.url_image + "\" already exists!");
            } else {
                System.out.println("Downloading image for: " + curImg.url_image);
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


                newImageFile.createNewFile(); // if file already exists will do nothing
                FileOutputStream fos = new FileOutputStream(newImageFile, false);
                fos.write(response);
                fos.close();
            /*BufferedImage image = ImageIO.read(newImageFile);
            ImageIO.write(image, "bmp", newImageFile);*/
            }

            curImg.setPath(newImageFile.getAbsolutePath());
            PubSub.sendMessage("updateImages", curImg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static boolean fileExists(String path) {
        return new File(path).exists();
    }

}
