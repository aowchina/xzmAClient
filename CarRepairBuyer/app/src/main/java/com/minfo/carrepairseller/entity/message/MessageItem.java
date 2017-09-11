package com.minfo.carrepairseller.entity.message;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/6.
 */
public class MessageItem implements Serializable {
    //    {
//        "picture": "http://192.168.1.106images/car/1.png",
//            "major": "dfghj",
//            "skill": "dfgh",
//            "company": ""
//    }
//    {
//        "sellerid": "1",
//            "tel": "1834597974",
//            "picture": "http://192.168.1.116/images/car/1.png",
//            "name": "sdfhjh",
//            "password": "",
//            "is_rz": "1",
//            "type": "0",
//            "lastvisitDate": "0",
//            "shopid": "0",
//            "state": "2",
//            "publish_up": "0",
//            "publish_down": "0",
//            "addtime": "0"
//    }
    private String picture;
    private String sellerid;

    private String major;

    private String skill;

    private String company;
    private String is_friend;
    private String name;
    private String tel;

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIs_friend() {
        return is_friend;
    }

    public void setIs_friend(String is_friend) {
        this.is_friend = is_friend;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "MessageItem{" +
                "picture='" + picture + '\'' +
                ", sellerid='" + sellerid + '\'' +
                ", major='" + major + '\'' +
                ", skill='" + skill + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
