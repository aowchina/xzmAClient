package com.minfo.carrepairseller.entity.order;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/26.
 */

public class OrderSureQGEntity implements Serializable {
//    {
//        "qg_address_id": "6687",
//            "qg_user_name": "李高云",
//            "qg_user_tel": "13681491011",
//            "qg_user_address": "北京 北京市 东城国际贸易广场",
//            "qg_user_pid": "110000",
//            "qg_user_cid": "110100",
//            "qg_user_qid": "110101",
//            "total_money": 455,
//            "goods": {
//        "0": {
//            "bname": "奔驰",
//                    "sname": "奔驰B",
//                    "cname": "气温气温",
//                    "jname": "多多少少豆腐乳同意",
//                    "picture": "http://192.168.1.116/zjxzm/images/wantbuy/2017/06/16/0_1497617518.jpg\"]"
//        },
//        "type": "1",
//                "price": "455",
//                "total_money": 455
//    },
//        "addtime": "2017-06-26 19:05:45"
//    }
    @SerializedName("qg_user_name")
    private String name;
    @SerializedName("qg_user_tel")
    private String tel;
    @SerializedName("qg_user_address")
    private String address;
    @SerializedName("qg_address_id")
    private String addressId;
    private List<GoodInfo> goods;
    @SerializedName("addtime")
    private String time;
    @SerializedName("total_money")
    private String totalMoney;

    public List<GoodInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodInfo> goods) {
        this.goods = goods;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getAddressId() {
        return addressId;
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

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public class GoodInfo {
        @SerializedName("jname")
        private String name;
        private String price;
        @SerializedName("picture")
        private String img;
        private String type;
        @SerializedName("total_money")
        private String totalMoney;
        private String bname;
        private String sname;
        private String cname;

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

        public String getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            this.totalMoney = totalMoney;
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

        public String getType() {
            return type;
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

        public void setType(String type) {
            this.type = type;
        }
    }
}
