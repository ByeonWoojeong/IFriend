package com.woojeong.global.ifriend.DTO;

public class SearchAddress {
    String lng;
    String lat;
    String addr;
    String addr1;

    public SearchAddress(String lng, String lat, String addr, String addr1) {
        this.lng = lng;
        this.lat = lat;
        this.addr = addr;
        this.addr1 = addr1;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }
}
