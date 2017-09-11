package com.minfo.carrepairseller.entity.shop;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/7.
 */

public class ProductDetail implements Serializable {
//    {
//        "goodid": "15",
//            "name": "光辉家具",
//            "img": [
//        "images/goods/2017/06/12/0_1497266888.jpg",
//                "images/goods/2017/06/12/1_1497266888.jpg",
//                "images/goods/2017/06/12/2_1497266888.jpg"
//        ],
//        "price": "523",
//            "oem": "4294967295",
//            "num": "0",
//            "cname": "奔驰A67",
//            "sname": "奔驰A",
//            "bname": "奔驰",
//            "blogo": "images/carbrand/4.jpg",
//            "shopid": "6",
//            "all_goods": "5",
//            "news": "5"
//    }
    private String name;
    private String price;
    private String carid;
    private String typeid;
    private String tname;
    private String car_name;
    private List<String> img;
    private String oem;
    private String tel;
    @SerializedName("bname")
    private String pinpai;
    @SerializedName("sname")
    private String chexi;
    @SerializedName("cname")
    private String chexing;
    @SerializedName("goodid")
    private String goodId;
    @SerializedName("blogo")
    private String icon;
    @SerializedName("all_goods")
    private int allNum;
    @SerializedName("news")
    private int newNum;
    private String shopid;
    private String num;
    @SerializedName("is_collect")
    private int iscollect;
    private String detail;
    private  int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getIscollect() {
        return iscollect;
    }

    public void setIscollect(int iscollect) {
        this.iscollect = iscollect;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    private String sellerid;
    private String sellTel;
    private String sellerPicture;
    private String sellerName;
    private List<CarItem>carList;

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getSellTel() {
        return sellTel;
    }

    public void setSellTel(String sellTel) {
        this.sellTel = sellTel;
    }

    public String getSellerPicture() {
        return sellerPicture;
    }

    public void setSellerPicture(String sellerPicture) {
        this.sellerPicture = sellerPicture;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<CarItem> getCarList() {
        return carList;
    }

    public void setCarList(List<CarItem> carList) {
        this.carList = carList;
    }

    public String getPinpai() {
        return pinpai;
    }

    public String getChexi() {
        return chexi;
    }

    public String getChexing() {
        return chexing;
    }

    public String getGoodId() {
        return goodId;
    }

    public String getIcon() {
        return icon;
    }

    public int getAllNum() {
        return allNum;
    }

    public int getNewNum() {
        return newNum;
    }

    public void setPinpai(String pinpai) {
        this.pinpai = pinpai;
    }

    public void setChexi(String chexi) {
        this.chexi = chexi;
    }

    public void setChexing(String chexing) {
        this.chexing = chexing;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public void setNewNum(int newNum) {
        this.newNum = newNum;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopid() {
        return shopid;
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
                ", oem='" + oem + '\'' +
                ", tel='" + tel + '\'' +
                ", detail='" + detail + '\'' +
                ", pinpai='" + pinpai + '\'' +
                ", chexi='" + chexi + '\'' +
                ", chexing='" + chexing + '\'' +
                ", goodId='" + goodId + '\'' +
                ", icon='" + icon + '\'' +
                ", allNum=" + allNum +
                ", newNum=" + newNum +
                ", shopid='" + shopid + '\'' +
                ", sellerid='" + sellerid + '\'' +
                ", sellTel='" + sellTel + '\'' +
                ", sellerPicture='" + sellerPicture + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", carList=" + carList +
                '}';
    }
}
