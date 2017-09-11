package com.minfo.carrepair.entity.showprice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/14.
 */
public class ShowPriceList implements Serializable {
    private String typelist;
    private String pricelist;
    private String id;
    private String bname;
    private String sname;
    private String cname;
    private String img;
    private String vin;
    private String jname;
    private String picture;

    private List<ShowPriceItem>tpdetail;

    public String getTypelist() {
        return typelist;
    }

    public void setTypelist(String typelist) {
        this.typelist = typelist;
    }

    public String getPricelist() {
        return pricelist;
    }

    public void setPricelist(String pricelist) {
        this.pricelist = pricelist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getJname() {
        return jname;
    }

    public void setJname(String jname) {
        this.jname = jname;
    }

    public List<ShowPriceItem> getTpdetail() {
        return tpdetail;
    }

    public void setTpdetail(List<ShowPriceItem> tpdetail) {
        this.tpdetail = tpdetail;
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
                "typelist='" + typelist + '\'' +
                ", pricelist='" + pricelist + '\'' +
                ", id='" + id + '\'' +
                ", bname='" + bname + '\'' +
                ", sname='" + sname + '\'' +
                ", cname='" + cname + '\'' +
                ", img='" + img + '\'' +
                ", vin='" + vin + '\'' +
                ", jname='" + jname + '\'' +
                ", tpdetail=" + tpdetail +
                '}';
    }
}
