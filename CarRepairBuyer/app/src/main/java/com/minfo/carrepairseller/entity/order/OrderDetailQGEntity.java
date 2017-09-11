package com.minfo.carrepairseller.entity.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/27.
 */

public class OrderDetailQGEntity implements Serializable {
//    {
//        "total_money": 620,
//            "goods": [],
//        "addtime": "2017-06-27 15:39:19",
//            "pid": "110000",
//            "cid": "110100",
//            "qid": "110101",
//            "sname": "李高云",
//            "stel": "13681491011",
//            "kuaidih": "",
//            "wlname": "",
//            "retime": " ",
//            "address": "北京 北京市 东城区 东城国际贸易广场",
//            "orderid": null
//    }
    private String totalMoney;
    private String time;
    private String sname;
    private String stel;
    private String saddress;
    private String wlname;
    private String orderid;

    public class GoodInfo {
//        {
//            "bname": "奔驰",
//                "sname": "奔驰A",
//                "cname": "奔驰A67",
//                "jname": "我的求购",
//                "picture": "http://192.168.1.112/zjxzm/images/wantbuy/2017/06/27/0_1498545297.jpg",
//                "type": "0,1,2",
//                "price": "200,120,300",
//                "total_money": 620
//        }
         @SerializedName("bname")
         private String pinpai;
        @SerializedName("sname")
         private String chexi;
        @SerializedName("cname")
         private String chexing;
        @SerializedName("jname")
         private String name;
         private String img;
        private String type;
        @SerializedName("total_money")
         private String totalMoney;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTotalMoney(String totalMoney) {
            this.totalMoney = totalMoney;
        }

        public String getTotalMoney() {
            return totalMoney;
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

    public void setImg(String img) {
        this.img = img;
    }
}
}
