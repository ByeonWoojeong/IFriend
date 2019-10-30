package com.woojeong.global.ifriend.DTO;

import java.io.Serializable;

public class Profit implements Serializable {

    String cost, day2, status;

    public Profit(String cost, String day2, String status) {
        this.cost = cost;
        this.day2 = day2;
        this.status = status;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
