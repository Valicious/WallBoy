package com.val.wallboy.model;

public class Image {
    public String id;
    public String width;
    public String height;
    public String file_type;
    public String file_size;
    public String url_image;
    public String url_thumb;
    public String url_page;

    public String path;

    public Image(String id, String width, String height, String file_type, String file_size, String url_image, String url_thumb, String url_page) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.file_size = file_size;
        this.file_type = file_type;
        this.url_image = url_image;
        this.url_thumb = url_thumb;
        this.url_page = url_page;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
