package com.minfo.carrepair.entity.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/20.
 */
public class WLEntityList implements Serializable {
private List<WLEntity>list;

    public List<WLEntity> getList() {
        return list;
    }

    public void setList(List<WLEntity> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "WLEntityList{" +
                "list=" + list +
                '}';
    }
}
