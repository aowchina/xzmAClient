package com.minfo.carrepairseller.entity.publish;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/8.
 */
public class ProductKindSecond implements Serializable {
    private String id;
    private String name;

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

    @Override
    public String toString() {
        return "ProductKindSecond{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
