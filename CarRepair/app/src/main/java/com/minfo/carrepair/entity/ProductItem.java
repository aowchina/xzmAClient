package com.minfo.carrepair.entity;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/5/31.
 */

public class ProductItem implements Serializable{
    private String goodid;
    private String name;
    private String price;
    private String img;
    private String carid;
    private String car_name;
    private String tname;

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "goodid='" + goodid + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", img='" + img + '\'' +
                ", carid='" + carid + '\'' +
                ", car_name='" + car_name + '\'' +
                '}';
    }
}
