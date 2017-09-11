package com.minfo.carrepair.entity.personl;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/24.
 */
public class AuthInfo implements Serializable {
    private String sellerid;
    private String sname;
    private String shopid;
    private String major;
    private String skill;
    private String picture;
    private String type;
    private String number;
    private String pid;
    private String cid;
    private String qid;
    private String address;
    private String cardfront;

    private String cardback;

    private String cardhand;

    private String license;

    private String addtime;
    @SerializedName("status")
    private String state;

    private String publish_up;
    private String publish_down;

    private String company;

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardfront() {
        return cardfront;
    }

    public void setCardfront(String cardfront) {
        this.cardfront = cardfront;
    }

    public String getCardback() {
        return cardback;
    }

    public void setCardback(String cardback) {
        this.cardback = cardback;
    }

    public String getCardhand() {
        return cardhand;
    }

    public void setCardhand(String cardhand) {
        this.cardhand = cardhand;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPublish_up() {
        return publish_up;
    }

    public void setPublish_up(String publish_up) {
        this.publish_up = publish_up;
    }

    public String getPublish_down() {
        return publish_down;
    }

    public void setPublish_down(String publish_down) {
        this.publish_down = publish_down;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "sellerid='" + sellerid + '\'' +
                ", sname='" + sname + '\'' +
                ", shopid='" + shopid + '\'' +
                ", major='" + major + '\'' +
                ", skill='" + skill + '\'' +
                ", picture='" + picture + '\'' +
                ", type='" + type + '\'' +
                ", number='" + number + '\'' +
                ", pid='" + pid + '\'' +
                ", cid='" + cid + '\'' +
                ", qid='" + qid + '\'' +
                ", address='" + address + '\'' +
                ", cardfront='" + cardfront + '\'' +
                ", cardback='" + cardback + '\'' +
                ", cardhand='" + cardhand + '\'' +
                ", license='" + license + '\'' +
                ", addtime='" + addtime + '\'' +
                ", state='" + state + '\'' +
                ", publish_up='" + publish_up + '\'' +
                ", publish_down='" + publish_down + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
