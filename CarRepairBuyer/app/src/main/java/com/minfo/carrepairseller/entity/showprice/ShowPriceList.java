package com.minfo.carrepairseller.entity.showprice;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/14.
 */
public class ShowPriceList implements Serializable {
    private String type;
    private String price;
    private String id;
    private String sellerid;

    private String bname;
    private String sname;
    private String cname;
    private String tel;

    private String name;
    private String picture;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "ShowPriceList{" +
                "type='" + type + '\'' +
                ", price='" + price + '\'' +
                ", id='" + id + '\'' +
                ", sellerid='" + sellerid + '\'' +
                ", bname='" + bname + '\'' +
                ", sname='" + sname + '\'' +
                ", cname='" + cname + '\'' +
                ", tel='" + tel + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
