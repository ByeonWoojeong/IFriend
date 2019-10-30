package com.woojeong.global.ifriend.DTO;

import java.io.Serializable;

public class Review implements Serializable {

    String profile, name, content, star, date, image, comment, friendImage, commentDate;


    public Review(String profile, String name, String content, String star, String date, String image, String comment, String friendImage, String commentDate) {
        this.profile = profile;
        this.name = name;
        this.content = content;
        this.star = star;
        this.date = date;
        this.image = image;
        this.comment = comment;
        this.friendImage = friendImage;
        this.commentDate = commentDate;
    }

    public Review(String profile, String name, String content, String star, String date, String image, String comment, String commentDate) {
        this.profile = profile;
        this.name = name;
        this.content = content;
        this.star = star;
        this.date = date;
        this.image = image;
        this.comment = comment;
        this.commentDate = commentDate;
    }

    public Review(String profile, String name, String content, String star, String date) {
        this.profile = profile;
        this.name = name;
        this.content = content;
        this.star = star;
        this.date = date;
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

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFriendImage() {
        return friendImage;
    }

    public void setFriendImage(String friendImage) {
        this.friendImage = friendImage;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
