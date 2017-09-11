package com.minfo.carrepairseller.entity.shop;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 16/7/1.
 */
public class Product implements Serializable{
    private String name;
    private String id;
    private String simg;
    private String goods_num;
    private String intro;
    private String price;
    private String typeid;
    private int isSk; // 0,普通产品 1，秒杀产品
    private int session; // 0



    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSimg() {
        return simg;
    }

    public void setSimg(String simg) {
        this.simg = simg;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public int getIsSk() {
        return isSk;
    }

    public void setIsSk(int isSk) {
        this.isSk = isSk;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", simg='" + simg + '\'' +
                ", goods_num='" + goods_num + '\'' +
                ", intro='" + intro + '\'' +
                ", price='" + price + '\'' +
                ", typeid='" + typeid + '\'' +
                '}';
    }
}
