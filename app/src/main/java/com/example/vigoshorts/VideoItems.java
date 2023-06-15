package com.example.vigoshorts;

public class VideoItems {

    public String id, title, description, video;

    public VideoItems() {
    }

    public VideoItems(String id, String title, String description, String video) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideo() {
        return video;
    }
}
