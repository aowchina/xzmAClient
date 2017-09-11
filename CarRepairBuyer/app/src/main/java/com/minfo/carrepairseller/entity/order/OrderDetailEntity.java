package com.minfo.carrepairseller.entity.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/15.
 */

public class OrderDetailEntity implements Serializable {
//    {
//        "goods": [
//        {
//            "id": "13",
//                "goodid": "46",
//                "shopid": "33",
//                "amount": "1",
//                "money": "1234565",
//                "orderid": "zj149749172310051572029",
//                "img": "http://192.168.1.120/zjxzm/images/goods/2017/06/15/0_1497491159.jpg",
//                "name": "订单列表详情测试8",
//                "oem": " ",
//                "bname": "奔驰",
//                "sname": "奔驰B",
//                "cname": "气温气温"
//        }
//        ],
//        "pid": "0",
//            "cid": "0",
//            "qid": "0",
//            "sname": "",
//            "stel": "",
//            "info": "",
//            "kuaidih": "",
//            "wlname": "",
//            "address": "  ",
//            "orderid": "zj149749172310051572029",
//            "addtime": "2017-06-15 09:55:23"
//    }
    private String pid;
    private String cid;
    private String qid;
    private String sname;
    private String stel;
    private String info;
    private String kuaidih;
    private String wlname;
    private String address;
    private String orderid;
    private String addtime;
    private String paytime;

    private String fhtime;
    private String retime;

    private String sendtime;
    private String finishtime;
    private List<GoodInfo> goods;
    @SerializedName("total_money")
    private String totalMoney;
    private String money;
    private String qgorderid;

    public String getQgorderid() {
        return qgorderid;
    }

    public void setQgorderid(String qgorderid) {
        this.qgorderid = qgorderid;
    }

    public String getFhtime() {
        return fhtime;
    }

    public void setFhtime(String fhtime) {
        this.fhtime = fhtime;
    }

    public String getRetime() {
        return retime;
    }

    public void setRetime(String retime) {
        this.retime = retime;
    }

    public String getPaytime() {
        return paytime;
    }

    public String getSendtime() {
        return sendtime;
    }

    public String getFinishtime() {
        return finishtime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public String getMoney() {
        return money;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<GoodInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodInfo> goods) {
        this.goods = goods;
    }

    public String getPid() {
        return pid;
    }

    public String getCid() {
        return cid;
    }

    public String getQid() {
        return qid;
    }

    public String getSname() {
        return sname;
    }

    public String getStel() {
        return stel;
    }

    public String getInfo() {
        return info;
    }

    public String getKuaidih() {
        return kuaidih;
    }

    public String getWlname() {
        return wlname;
    }

    public String getAddress() {
        return address;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setStel(String stel) {
        this.stel = stel;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setKuaidih(String kuaidih) {
        this.kuaidih = kuaidih;
    }

    public void setWlname(String wlname) {
        this.wlname = wlname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public static class GoodInfo implements Serializable{
        private String id;
        private String goodid;
        private String shopid;
        private int amount;
        private String money;
        private String orderid;
        private String img;
        private String name;
        private String oem;
        private String bname;
        private String sname;
        private String cname;
        private String type;
        @SerializedName("total_money")
        private String totalMoney;
        @SerializedName("jname")
        private String goodname;
        private String picture;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTotalMoney(String totalMoney) {
            this.totalMoney = totalMoney;
        }

        public void setGoodname(String goodname) {
            this.goodname = goodname;
        }

        public String getType() {
            return type;
        }

        public String getTotalMoney() {
            return totalMoney;
        }

        public String getGoodname() {
            return goodname;
        }

        public String getId() {
            return id;
        }

        public String getGoodid() {
            return goodid;
        }

        public String getShopid() {
            return shopid;
        }

        public int getAmount() {
            return amount;
        }

        public String getMoney() {
            return money;
        }

        public String getOrderid() {
            return orderid;
        }

        public String getImg() {
            return img;
        }

        public String getName() {
            return name;
        }

        public String getOem() {
            return oem;
        }

        public String getBname() {
            return bname;
        }

        public String getSname() {
            return sname;
        }

        public String getCname() {
            return cname;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setGoodid(String goodid) {
            this.goodid = goodid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setOem(String oem) {
            this.oem = oem;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }
    }
}
