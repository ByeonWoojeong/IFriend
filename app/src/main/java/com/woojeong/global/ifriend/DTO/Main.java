package com.woojeong.global.ifriend.DTO;

import android.util.Log;

import com.woojeong.global.ifriend.R;

public class Main {
    private static final String TAG = "MainDATA";
    String member;
    String la;
    String lo;
    String[] homeImgId;
    String discount;
    String title;
    String name;
    String location;
    String isLike;
    String star;
    String reviewCount;
    String weekdayPrice;
    String weekendPrice;
    String profile;

    public Main(String member,String profile, String la, String lo, String[] homeImgId, String discount, String title, String name, String location, String isLike, String star, String reviewCount, String weekdayPrice, String weekendPrice) {
        this.member = member;
        this.la = la;
        this.lo = lo;
        this.profile = profile;
        this.homeImgId = homeImgId;
        this.discount = discount;
        this.title = title;
        this.name = name;
        this.location = location;
        this.isLike = isLike;
        this.star = star;
        this.reviewCount = reviewCount;
        this.weekdayPrice = weekdayPrice;
        this.weekendPrice = weekendPrice;
    }

    public Main(String member,String profile, String[] homeImgId, String discount, String title, String name, String location, String isLike, String star, String reviewCount, String weekdayPrice, String weekendPrice) {
        this.member = member;
        this.profile = profile;
        this.homeImgId = homeImgId;
        this.discount = discount;
        this.title = title;
        this.name = name;
        this.location = location;
        this.isLike = isLike;
        this.star = star;
        this.reviewCount = reviewCount;
        this.weekdayPrice = weekdayPrice;
        this.weekendPrice = weekendPrice;
    }

    public Main(String member,String profile, String[] homeImgId, String discount, String title, String name, String location, String star, String reviewCount, String weekdayPrice, String weekendPrice) {
        this.member = member;
        this.profile = profile;
        this.homeImgId = homeImgId;
        this.discount = discount;
        this.title = title;
        this.name = name;
        this.location = location;
        this.star = star;
        this.reviewCount = reviewCount;
        this.weekdayPrice = weekdayPrice;
        this.weekendPrice = weekendPrice;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String proflie) {
        this.profile = proflie;
    }

    public String getMember() {
        Log.i(TAG, " " + member);
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getLa() {
        return la;
    }

    public void setLa(String la) {
        this.la = la;
    }

    public String getLo() {
        return lo;
    }

    public void setLo(String lo) {
        this.lo = lo;
    }

    public String[] getHomeImgId() {
        return homeImgId;
    }

    public void setHomeImgId(String[] homeImgId) {
        this.homeImgId = homeImgId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getWeekdayPrice() {
        return weekdayPrice;
    }

    public void setWeekdayPrice(String weekdayPrice) {
        this.weekdayPrice = weekdayPrice;
    }

    public String getWeekendPrice() {
        return weekendPrice;
    }

    public void setWeekendPrice(String weekendPrice) {
        this.weekendPrice = weekendPrice;
    }
}
