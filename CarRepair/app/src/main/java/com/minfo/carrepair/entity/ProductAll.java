package com.minfo.carrepair.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/9.
 */
public class ProductAll implements Serializable {
    private List<ProductItem>goods;

    public List<ProductItem> getGoods() {
        return goods;
    }

    public void setGoods(List<ProductItem> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "ProductAll{" +
                "goods=" + goods +
                '}';
    }
}
