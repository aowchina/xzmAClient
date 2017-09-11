package com.minfo.carrepair.entity.shop;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/7.
 */

public class ProductDetail implements Serializable {
    private String name;
    private String price;
    private String carid;
    private String typeid;
    private String ptid;
    private String tname;
    private String car_name;
    private List<String> img;
    private String oem;
    private String tel;
    private String detail;
    private String num;
    private List<CarItem>carList;
    private int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPtid() {
        return ptid;
    }

    public void setPtid(String ptid) {
        this.ptid = ptid;
    }

    public List<CarItem> getCarList() {
        return carList;
    }

    public void setCarList(List<CarItem> carList) {
        this.carList = carList;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getOem() {
        return oem;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", carid='" + carid + '\'' +
                ", typeid='" + typeid + '\'' +
                ", tname='" + tname + '\'' +
                ", car_name='" + car_name + '\'' +
                ", img=" + img +
                '}';
    }
}
