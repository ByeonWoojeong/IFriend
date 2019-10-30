package com.woojeong.global.ifriend.DTO;

import java.io.Serializable;

public class JournalList implements Serializable {

    String reserve, mother_idx, mother_name, profile, friend_idx, friend_name, status, period, review;

    //시터용, 엄마용
    public JournalList(String reserve, String idx, String name, String profile, String status, String period, String review) {
        this.reserve = reserve;
        this.mother_idx = idx;
        this.mother_name = name;
        this.profile = profile;
        this.status = status;
        this.period = period;
        this.review = review;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getMother_idx() {
        return mother_idx;
    }

    public void setMother_idx(String mother_idx) {
        this.mother_idx = mother_idx;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }


    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFriend_idx() {
        return friend_idx;
    }

    public void setFriend_idx(String friend_idx) {
        this.friend_idx = friend_idx;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
