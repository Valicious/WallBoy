package com.val.wallboy.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.val.wallboy.model.Image;

import java.io.*;
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
                    .append(urlAuth)
                    .append(authenticationCode)
                    .append("&")
                    .append(urlMethod);
            switch (choice) {
                case "popular": {
                    bobURL.append("popular");
                    break;
                }
                case "newest": {
                    bobURL.append("newest");
                    break;
                }
                case "highest_rated": {
                    bobURL.append("highest_rated");
                    break;
                }
                case "random": {
                    bobURL.append("random");
                    break;
                }
                case "by_views": {
                    bobURL.append("by_views");
                    break;
                }
                case "by_favorites": {
                    bobURL.append("by_favorites");
                    break;
                }
                case "featured": {
                    bobURL.append("featured");
                    break;
                }
            }

            URL url = new URL(bobURL.toString());
            URLConnection connection = url.openConnection();
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = in.readLine();
            if (result == null) return null;
            //System.out.println(result);

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

    public static <T> T loadInstance(String path) {
        try {
            ObjectInputStream inObject = new ObjectInputStream(new FileInputStream(path));
            T obj = (T) inObject.readObject();
            inObject.close();
            System.out.println(obj.getClass().toString() + " Loaded.");
            return obj;
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException on: " + path);
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void saveInstance(T controller, String path) {
        try {
            ObjectOutputStream outObject = new ObjectOutputStream(new FileOutputStream(path));
            outObject.writeObject(controller);
            outObject.close();
            System.out.println(controller.getClass().toString() + " saved.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteImage(Image curImg) {
        File file = new File(curImg.path);
        if (file.delete()) {
            System.out.println("Image deleted: " + curImg.path);
        } else {
            System.out.println("Failed to delete image");
        }
    }
}
