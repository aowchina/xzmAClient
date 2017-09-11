package com.minfo.carrepair.entity.shop;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/9.
 */
public class ProductDetailAll implements Serializable {
    private ProductDetail info;

    public ProductDetail getInfo() {
        return info;
    }

    public void setInfo(ProductDetail info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "ProductDetailAll{" +
                "info=" + info +
                '}';
    }
}
