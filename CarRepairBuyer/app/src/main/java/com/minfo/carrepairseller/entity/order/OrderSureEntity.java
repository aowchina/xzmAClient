package com.minfo.carrepairseller.entity.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/15.
 */

public class OrderSureEntity implements Serializable {
//    {
//            "user_pid": null,
//            "user_cid": null,
//            "user_qid": null,
//            "user_name": null,
//            "user_tel": null,
//            "user_address": "  ",
//            "goods": [
//        {
//                "id": "20",
//                "goodid": "35",
//                "shopid": "33",
//                "amount": "1",
//                "money": "123456",
//                "orderid": "zj149749177410058646848",
//                "img": "http://192.168.1.120/zjxzm/images/goods/2017/06/15/0_1497491056.jpg",
//                "name": "订单列表详情测试1",
//                "oem": " ",
//                "bname": "奔驰",
//                "sname": "奔驰A",
//                "cname": "奔驰A67"
//        }
//        ],
//        "addtime": "1497491774"
//    }
    @SerializedName("user_pid")
    private String shengid;
    @SerializedName("user_cid")
    private String shiid;
    @SerializedName("user_qid")
    private String xianid;
    @SerializedName("user_name")
    private String name;
    @SerializedName("user_tel")
    private String tel;
    @SerializedName("user_address")
    private String address;
    @SerializedName("addtime")
    private String time;
    private List<GoodInfo> goods;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShengid() {
        return shengid;
    }

    public String getShiid() {
        return shiid;
    }

    public String getXianid() {
        return xianid;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public List<GoodInfo> getGoods() {
        return goods;
    }

    public void setShengid(String shengid) {
        this.shengid = shengid;
    }

    public void setShiid(String shiid) {
        this.shiid = shiid;
    }

    public void setXianid(String xianid) {
        this.xianid = xianid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGoods(List<GoodInfo> goods) {
        this.goods = goods;
    }

    public static class GoodInfo {
        private String id;
        private String goodid;
        private String shopid;
        private String amount;
        private String money;
        private String orderid;
        private String img;
        private String name;
        private String oem;
        private String bname;
        private String sname;
        private String cname;

        public String getId() {
            return id;
        }

        public String getGoodid() {
            return goodid;
        }

        public String getShopid() {
            return shopid;
        }

        public String getAmount() {
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

        public void setAmount(String amount) {
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
