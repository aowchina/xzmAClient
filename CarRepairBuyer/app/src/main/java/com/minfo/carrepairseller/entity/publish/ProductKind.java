package com.minfo.carrepairseller.entity.publish;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/8.
 */
public class ProductKind implements Serializable {
    private List<ProductKindFirst>type;

    public List<ProductKindFirst> getType() {
        return type;
    }

    public void setType(List<ProductKindFirst> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProductKind{" +
                "type=" + type +
                '}';
    }
}
