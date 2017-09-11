package com.minfo.carrepairseller.entity.order;

import com.google.gson.annotations.SerializedName;
import com.minfo.carrepairseller.entity.ProductItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/5/15.
 */

public class OrderListEntity implements Serializable {
//商品订单
//{
//    "id": "122",
//        "orderid": "zj149827463010059549612",
//        "money": "1234565",
//        "addtime": "2017-06-24 11:23:50",
//        "kuaidih": "0",
//        "wlname": "0",
//        "paytime": " ",
//        "fhtime": " ",
//        "retime": " ",
//        "amount": "1",
//        "total_money": 1234565,
//        "img": "http://192.168.1.116/zjxzm/images/goods/2017/06/15/0_1497491146.jpg",
//        "name": "订单列表详情测试6",
//        "status": 0
//},
    // 求购订单
//{
//    "id": "4",
//        "bid": "58",
//        "qgorderid": "zjqg149847624710055801636",
//        "type": "1",
//        "price": "455",
//        "addtime": "2017-06-26 19:24:07",
//        "kuaidih": null,
//        "wlname": null,
//        "paytime": " ",
//        "fhtime": " ",
//        "retime": " ",
//        "qg_total_money": 455,
//        "picture": "http://192.168.1.116/zjxzm/images/wantbuy/2017/06/16/0_1497617518.jpg\"]",
//        "jname": "多多少少豆腐乳同意",
//        "status": 0,
//        "biaoshi": 1
//},
    private String id;
    private String address;
    @SerializedName("address_id")
    private String addressId;
    @SerializedName("user_tel")
    private String tel;
    private String name;
    @SerializedName("money")
    private String price;
    @SerializedName("addtime")
    private String time;
    private String wl_number;
    private String defaulttime;
    private String pay_time;
    private String send_time;
    private String finishTime;
    @SerializedName("orderid")
    private String orderNum;
    private List<ProductItem> productItems;
    private int state;
    @SerializedName("amount")
    private int account;
    private String img;
    private String fhtime;
    private String retime;
    @SerializedName("total_money")
    private String totalMoney;

    @SerializedName("qgorderid")
    private String qgOrderid;
    @SerializedName("jname")
    private String qgName;
    @SerializedName("price")
    private String qgPrice;
    @SerializedName("picture")
    private String qgImg;
    @SerializedName("type")
    private String qgType;
    private int biaoshi; // 0，商品下单1，求购下单
    @SerializedName("qg_total_money")
    private String allMoney;

    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }

    public int getBiaoshi() {
        return biaoshi;
    }

    public void setBiaoshi(int biaoshi) {
        this.biaoshi = biaoshi;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getQgOrderid() {
        return qgOrderid;
    }

    public String getQgName() {
        return qgName;
    }

    public String getQgPrice() {
        return qgPrice;
    }

    public String getQgImg() {
        return qgImg;
    }

    public String getQgType() {
        return qgType;
    }

    public void setQgOrderid(String qgOrderid) {
        this.qgOrderid = qgOrderid;
    }

    public void setQgName(String qgName) {
        this.qgName = qgName;
    }

    public void setQgPrice(String qgPrice) {
        this.qgPrice = qgPrice;
    }

    public void setQgImg(String qgImg) {
        this.qgImg = qgImg;
    }

    public void setQgType(String qgType) {
        this.qgType = qgType;
    }

    public String getRetime() {
        return retime;
    }

    public void setRetime(String retime) {
        this.retime = retime;
    }

    public String getFhtime() {
        return fhtime;
    }

    public void setFhtime(String fhtime) {
        this.fhtime = fhtime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getAddress() {
        return address;
    }

    public String getTel() {
        return tel;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public String getWl_number() {
        return wl_number;
    }

    public String getDefaulttime() {
        return defaulttime;
    }

    public String getPay_time() {
        return pay_time;
    }

    public String getSend_time() {
        return send_time;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWl_number(String wl_number) {
        this.wl_number = wl_number;
    }

    public void setDefaulttime(String defaulttime) {
        this.defaulttime = defaulttime;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }


}
