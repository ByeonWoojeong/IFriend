package com.woojeong.global.ifriend.DTO;

public class SearchName {

    String name;
    String member;

    public SearchName(String name, String member) {
        this.name = name;
        this.member = member;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

}
