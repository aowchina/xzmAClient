package com.minfo.carrepair.entity;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/23.
 */
public class LoginEntity implements Serializable {
    private String nickname;
    private String userid;

    private String picture;
    private String tel;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "LoginEntity{" +
                "nickname='" + nickname + '\'' +
                ", userid='" + userid + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
