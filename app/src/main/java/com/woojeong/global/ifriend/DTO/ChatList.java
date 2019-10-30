package com.woojeong.global.ifriend.DTO;

import java.io.Serializable;

public class ChatList implements Serializable {

    String idx, profile, name, content, date, badge;

    public ChatList(String idx, String profile, String name, String content, String date, String badge) {
        this.idx = idx;
        this.profile = profile;
        this.name = name;
        this.content = content;
        this.date = date;
        this.badge = badge;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}
