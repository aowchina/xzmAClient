package com.minfo.carrepairseller.entity.showprice;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/14.
 */
public class ShowPriceItem implements Serializable {
    private String type;
    private String price;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ShowPriceItem{" +
                "type='" + type + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
