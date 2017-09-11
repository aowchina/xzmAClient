package com.minfo.carrepairseller.entity.personl;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/19.
 */

public class CollectProductModel implements Serializable {
//    {
//        "list": [
//        {
//            "bname": "奔驰",
//                "sname": "奔驰B",
//                "cname": "气温气温",
//                "name": "订单列表详情测试7",
//                "hprice": "0",
//                "img": "http://192.168.1.120/zjxzm/images/goods/2017/06/15/0_1497491153.jpg"
//        }
//        ]
//    }
    private List<ProductItem> list;

    public List<ProductItem> getList() {
        return list;
    }

    public void setList(List<ProductItem> list) {
        this.list = list;
    }

    public class ProductItem {
        @SerializedName("bname")
        private String pinpai;
        @SerializedName("sname")
        private String chexi;
        @SerializedName("cname")
        private String chexing;
        private String name;
        private String price;
        private String goodid;
        private String img;

        public String getGoodid() {
            return goodid;
        }

        public void setGoodid(String goodid) {
            this.goodid = goodid;
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

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImg() {
        return img;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
}
