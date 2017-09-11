package com.minfo.carrepair.entity.query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fei on 15/9/11.
 *
 * @funcation 产品实体类
 */
public class Product_Details_Entity implements Serializable {

    /**
     * bimg : http://hondo.min-fo.com/images/product/big/b1.png
     * detail : [{"img":"http://hondo.min-fo.com/images/product/detail/d1.jpg","img_height":885,"img_width":750},{"img":"http://hondo.min-fo.com/images/product/detail/d2.jpg","img_height":1067,"img_width":750}]
     * weight : 1000
     * max_one : 8
     * goods_num : hd001
     * act_status : 1
     * act_id : 1
     * intro : 商品1简介内容
     * id : 1
     * unit : 0
     * act_price : 0.90
     * dimg : ["images\/product\/detail\/d1.jpg","images\/product\/detail\/d2.jpg"]
     * price : 1.00
     * name : 普通商品1
     * vedio :
     */
    private String bimg;
    private List<DetailEntity> detail;
    private String weight;
    private String max_one;
    private String goods_num;
    private String act_status;
    private String act_id;
    private String intro;
    private String id;
    private String unit;
    private String act_price;
    private String dimg;
    private String price;
    private String name;
    private String vedio;
    private String unitname;
    private String simg;
    private int state;


    public int getState() {
        return state;
    }


    public void setState(int state) {
        this.state = state;
    }


    public String getSimg() {
        return simg;
    }


    public void setSimg(String simg) {
        this.simg = simg;
    }


    public void setBimg(String bimg) { this.bimg = bimg;}


    public void setDetail(List<DetailEntity> detail) { this.detail = detail;}


    public void setWeight(String weight) { this.weight = weight;}


    public void setMax_one(String max_one) { this.max_one = max_one;}


    public void setGoods_num(String goods_num) { this.goods_num = goods_num;}


    public void setAct_status(String act_status) {
        this.act_status = act_status;
    }


    public void setAct_id(String act_id) { this.act_id = act_id;}


    public void setIntro(String intro) { this.intro = intro;}


    public void setId(String id) { this.id = id;}


    public void setUnit(String unit) { this.unit = unit;}


    public void setAct_price(String act_price) { this.act_price = act_price;}


    public void setDimg(String dimg) { this.dimg = dimg;}


    public void setPrice(String price) { this.price = price;}


    public void setName(String name) { this.name = name;}


    public void setVedio(String vedio) { this.vedio = vedio;}


    public String getBimg() { return bimg;}


    public List<DetailEntity> getDetail() { return detail;}


    public String getWeight() { return weight;}


    public String getMax_one() { return max_one;}


    public String getGoods_num() { return goods_num;}


    public String getAct_status() { return act_status;}


    public String getAct_id() { return act_id;}


    public String getIntro() { return intro;}


    public String getId() { return id;}


    public String getUnit() { return unit;}


    public String getAct_price() { return act_price;}


    public String getDimg() { return dimg;}


    public String getPrice() { return price;}


    public String getName() { return name;}


    public String getVedio() { return vedio;}


    public String getUnitname() {
        return unitname;
    }


    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }


    public static class DetailEntity {
        /**
         * img : http://hondo.min-fo.com/images/product/detail/d1.jpg
         * img_height : 885
         * img_width : 750
         */
        private String img;
        private int img_height;
        private int img_width;


        public void setImg(String img) { this.img = img;}


        public void setImg_height(int img_height) {
            this.img_height = img_height;
        }


        public void setImg_width(int img_width) { this.img_width = img_width;}


        public String getImg() { return img;}


        public int getImg_height() { return img_height;}


        public int getImg_width() { return img_width;}
    }


    @Override
    public String toString() {
        return "Product_Details_Entity{" +
                "bimg='" + bimg + '\'' +
                ", detail=" + detail +
                ", weight='" + weight + '\'' +
                ", max_one='" + max_one + '\'' +
                ", goods_num='" + goods_num + '\'' +
                ", act_status='" + act_status + '\'' +
                ", act_id='" + act_id + '\'' +
                ", intro='" + intro + '\'' +
                ", id='" + id + '\'' +
                ", unit='" + unit + '\'' +
                ", act_price='" + act_price + '\'' +
                ", dimg='" + dimg + '\'' +
                ", price='" + price + '\'' +
                ", name='" + name + '\'' +
                ", vedio='" + vedio + '\'' +
                '}';
    }
}
