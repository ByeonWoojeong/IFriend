package com.woojeong.global.ifriend.DTO;

import java.io.Serializable;

public class Child implements Serializable {

    String number;
    String name;
    String age;
    String gender;
    String image;

    public Child(String number, String name, String age, String gender, String image) {
        this.number = number;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.image = image;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
