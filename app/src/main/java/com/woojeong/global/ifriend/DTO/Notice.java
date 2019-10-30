package com.woojeong.global.ifriend.DTO;

public class Notice {

    String idx;
    String title;
    String date;

    public Notice(String idx, String title, String date) {
        this.idx = idx;
        this.title = title;
        this.date = date;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
