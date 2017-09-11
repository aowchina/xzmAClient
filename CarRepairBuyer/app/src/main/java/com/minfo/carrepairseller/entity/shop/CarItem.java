package com.minfo.carrepairseller.entity.shop;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/27.
 */
public class CarItem implements Serializable {
    private String carid;
    private String cname;

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "CarItem{" +
                "carid='" + carid + '\'' +
                ", cname='" + cname + '\'' +
                '}';
    }
}
