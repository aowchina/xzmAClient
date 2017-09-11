package com.minfo.carrepairseller.entity.publish;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/8.
 */
public class ProductKindFirst implements Serializable {
    private String id;
    private String name;
    private List<ProductKindSecond>child;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductKindSecond> getChild() {
        return child;
    }

    public void setChild(List<ProductKindSecond> child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "ProductKindFirst{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", child=" + child +
                '}';
    }
}