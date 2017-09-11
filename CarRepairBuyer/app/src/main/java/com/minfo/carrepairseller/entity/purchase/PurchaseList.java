package com.minfo.carrepairseller.entity.purchase;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/14.
 */
public class PurchaseList implements Serializable {
    private String bid;
    private String appuid;

    private String bname;

    private String sname;

    private String cname;

    private String jname;

    private String picture;

//    private String vin;
    private String type;
//    private String pinpai;

//    private String otherpz;
//    private String img;

    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    @Override
    public String toString() {
        return "PurchaseList{" +
                "bid='" + bid + '\'' +
                ", appuid='" + appuid + '\'' +
                ", bname='" + bname + '\'' +
                ", sname='" + sname + '\'' +
                ", cname='" + cname + '\'' +
                ", jname='" + jname + '\'' +
                ", picture='" + picture + '\'' +
                ", type='" + type + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
