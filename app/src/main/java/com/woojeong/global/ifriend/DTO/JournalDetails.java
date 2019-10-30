package com.woojeong.global.ifriend.DTO;

import java.io.Serializable;

public class JournalDetails implements Serializable {

    String friend_profile, date, images, content;

    public JournalDetails(String friend_profile, String date, String images, String content) {
        this.friend_profile = friend_profile;
        this.date = date;
        this.images = images;
        this.content = content;
    }

    public String getFriend_profile() {
        return friend_profile;
    }

    public void setFriend_profile(String friend_profile) {
        this.friend_profile = friend_profile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
