package com.example.khabrinews;

public class MediaObject {

    private String title;
    private String media_url;
    private String image_uri;
    private String description;

    public MediaObject() {
    }

    public MediaObject(String title, String media_url, String image_uri, String description) {
        this.title = title;
        this.media_url = media_url;
        this.image_uri = image_uri;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
