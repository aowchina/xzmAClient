package com.minfo.carrepair.entity.showprice;

import java.util.List;

/**
 * Created by min-fo-012 on 17/6/24.
 */
public class PriceDetailEntity {
    private String bid;
    private String sellerid;
    private String type;
    private String price;


    private String bname;

    private String sname;

    private String cname;

    private String jname;

    private List<String> picture;

    private String vin;
    private List<ShowPriceItem> tpdetail;

    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

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

    public List<String> getPicture() {
        return picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public List<ShowPriceItem> getTpdetail() {
        return tpdetail;
    }

    public void setTpdetail(List<ShowPriceItem> tpdetail) {
        this.tpdetail = tpdetail;
    }

    @Override
    public String toString() {
        return "PriceDetailEntity{" +
                "bid='" + bid + '\'' +
                ", sellerid='" + sellerid + '\'' +
                ", type='" + type + '\'' +
                ", price='" + price + '\'' +
                ", bname='" + bname + '\'' +
                ", sname='" + sname + '\'' +
                ", cname='" + cname + '\'' +
                ", jname='" + jname + '\'' +
                ", picture=" + picture +
                ", vin='" + vin + '\'' +
                ", tpdetail=" + tpdetail +
                ", img='" + img + '\'' +
                '}';
    }
}
