package com.val.wallboy;

import com.val.wallboy.events.PubSub;
import com.val.wallboy.model.Image;
import com.val.wallboy.source.ImageDownloader;
import com.val.wallboy.source.SourceControl;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;

public class ImageController implements Serializable {
    static final long serialVersionUID = 3845048656209131150L;
    private final String path = "IC.obj";
    private HashMap<String, Image> images;
    private HashSet<String> urlBlacklist;
    private HashSet<String> urlLiked;
    private Image curImg;
    private Random rng;

    public ImageController() {
        ImageController thiss = SourceControl.loadInstance(path);
        if (thiss != null) {
            images = thiss.images;
            urlBlacklist = thiss.urlBlacklist;
            urlLiked = thiss.urlLiked;
        } else {
            images = new HashMap<>();
            urlBlacklist = new HashSet<>();
            urlLiked = new HashSet<>();
        }
        rng = new SecureRandom();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveInstance));
        PubSub.subscribe("updateImages", args -> {
            Image newOne = (Image) args[0];
            this.images.put(newOne.url_image, newOne);
        });

        List<String> temp = new ArrayList<>();
        images.forEach((url, img) -> {
            if (!ImageDownloader.fileExists(img.path) && !img.path.equals("")) {
                temp.add(url);
                urlBlacklist.add(img.url_image);
                System.out.println("Image is now hated: " + img.url_image);
            }
        });
        temp.forEach(images::remove);
    }

    public void updateImages(ArrayList<Image> images) {
        List<Image> newImages = new ArrayList<>();
        images.forEach(curImage -> {
            if (urlBlacklist.contains(curImage.url_image)) return;
            if (!this.images.containsKey(curImage.url_image))
                // this.images.put(curImage.url_image, curImage);
                newImages.add(curImage);

        });
        ImageDownloader.downloadImages(newImages);
    }

    public Image getRandomImage() {
        int chance = rng.nextInt(100);
        try {
            if (chance < 10 && urlLiked.size() > 0) {
                chance = rng.nextInt(urlLiked.size());
                curImg = images.get(urlLiked.toArray(new String[0])[chance]);
                return curImg;
            } else if (images.size() > 0) {
                chance = rng.nextInt(images.size());
                curImg = new ArrayList<>(images.values()).get(chance);
                return curImg;
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(chance);
            throw e;
        }
    }

    public void saveInstance() {
        SourceControl.saveInstance(this, path);
        System.out.println("Saved Application instance");
    }

    public String loveImage() {
        if (urlLiked.contains(curImg.url_image)) return "Image is already loved :)";
        urlLiked.add(curImg.url_image);
        System.out.println("Image is now loved: " + curImg.url_image);
        return "Image is now loved :)";
    }

    public void hateImage() {
        urlBlacklist.add(curImg.url_image);
        images.remove(curImg.url_image);
        System.out.println("Image is now hated: " + curImg.url_image);
        SourceControl.deleteImage(curImg);
        PubSub.sendMessage("nextImage");
    }

    public void nextImage() {
        PubSub.sendMessage("nextImage");
    }

    public void blackScreen() {
        SourceControl.deleteImage(curImg);
        curImg.path = "";
        ImageDownloader.downloadImage(curImg);
        PubSub.sendMessage("nextImage");
    }
}
