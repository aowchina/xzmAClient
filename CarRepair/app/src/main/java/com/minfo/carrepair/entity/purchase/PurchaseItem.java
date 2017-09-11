package com.minfo.carrepair.entity.purchase;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/13.
 */
public class PurchaseItem implements Serializable {
    private String bid;
    private String appuid;

    private String bname;

    private String sname;

    private String cname;

    private String jname;

    private List<String> picture;
//    private List<String> picture;

    private String vin;
    private List<String> type;
    private String pinpai;

    private String otherpz;
    private String img;

    public String getPinpai() {
        return pinpai;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public String getOtherpz() {
        return otherpz;
    }

    public void setOtherpz(String otherpz) {
        this.otherpz = otherpz;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAppuid() {
        return appuid;
    }

    public void setAppuid(String appuid) {
        this.appuid = appuid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getJname() {
        return jname;
    }

    public void setJname(String jname) {
        this.jname = jname;
    }


    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }


    public List<String> getPicture() {
        return picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PurchaseItem{" +
                "bid='" + bid + '\'' +
                ", appuid='" + appuid + '\'' +
                ", bname='" + bname + '\'' +
                ", sname='" + sname + '\'' +
                ", cname='" + cname + '\'' +
                ", jname='" + jname + '\'' +
                ", picture=" + picture +
                ", vin='" + vin + '\'' +
                ", type=" + type +
                ", pinpai='" + pinpai + '\'' +
                ", otherpz='" + otherpz + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
