package com.minfo.carrepair.entity.shop;

import com.google.gson.annotations.SerializedName;
import com.minfo.carrepair.entity.ProductItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/14.
 */

public class ShopModel implements Serializable {
    //    {
//        "shopInfo": {
//        "tel": "13645687945",
//                "shopname": "dfsghdjfj",
//                "picture": "http://192.168.1.112/zjxzm/images/car/1.png",
//                "shopid": "33",
//                "rate": "0"
//        },
//        "allGoods": [
//        {
//            "goodid": "23",
//                "name": "配件1",
//                "price": "3125",
//                "img": "http://192.168.1.112/zjxzm/images/goods/2017/06/14/0_1497421127.jpg",
//                "tname": "2"
//        }
//        ],
//        "allCount": 10,
//            "newGoods": [
//        {
//            "goodid": "22",
//                "name": "干活哈哈",
//                "price": "123",
//                "img": "http://192.168.1.112/zjxzm/images/goods/2017/06/14/0_1497420968.jpg",
//                "tname": "发1"
//        }
//        ],
//        "newCount": 10,
//        "sellCount": "1"
//    }
    private int allCount;
    private int sellCount;
    private int newCount;
    private List<ProductItem> allGoods;
    private List<ProductItem> newGoods;
    private ShopInfo shopInfo;

    public int getAllCount() {
        return allCount;
    }

    public int getSellCount() {
        return sellCount;
    }

    public int getNewCount() {
        return newCount;
    }

    public List<ProductItem> getAllGoods() {
        return allGoods;
    }

    public List<ProductItem> getNewGoods() {
        return newGoods;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public void setSellCount(int sellCount) {
        this.sellCount = sellCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
    }

    public void setAllGoods(List<ProductItem> allGoods) {
        this.allGoods = allGoods;
    }

    public void setNewGoods(List<ProductItem> newGoods) {
        this.newGoods = newGoods;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public class ShopInfo implements Serializable {
        @SerializedName("tel")
        private String phone;
        @SerializedName("shopname")
        private String name;
        @SerializedName("picture")
        private String picture;
        @SerializedName("shopid")
        private String id;
        @SerializedName("rate")
        private String haoping;

        public String getPhone() {
            return phone;
        }

        public String getName() {
            return name;
        }

        public String getPicture() {
            return picture;
        }

        public String getId() {
            return id;
        }

        public String getHaoping() {
            return haoping;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setHaoping(String haoping) {
            this.haoping = haoping;
        }
    }
}
