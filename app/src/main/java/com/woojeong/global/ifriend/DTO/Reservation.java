package com.woojeong.global.ifriend.DTO;

import java.io.Serializable;

public class Reservation implements Serializable {

    String reserve, profile, name, period, status, isreview, friend;

    public Reservation(String reserve, String profile, String name, String period, String status) {
        this.reserve = reserve;
        this.profile = profile;
        this.name = name;
        this.period = period;
        this.status = status;
        this.friend = friend;
    }
    public Reservation(String reserve, String profile, String name, String period, String status, String isreview, String friend) {
        this.reserve = reserve;
        this.profile = profile;
        this.name = name;
        this.period = period;
        this.status = status;
        this.isreview = isreview;
        this.friend = friend;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsreview() {
        return isreview;
    }

    public void setIsreview(String isreview) {
        this.isreview = isreview;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
}
