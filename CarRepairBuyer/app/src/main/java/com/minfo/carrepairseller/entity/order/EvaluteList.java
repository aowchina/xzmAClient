package com.minfo.carrepairseller.entity.order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/22.
 */
public class EvaluteList implements Serializable {
    private List<EvaluteEntity> pinjia;

    public List<EvaluteEntity> getPinjia() {
        return pinjia;
    }

    public void setPinjia(List<EvaluteEntity> pinjia) {
        this.pinjia = pinjia;
    }

    @Override
    public String toString() {
        return "EvaluteList{" +
                "pinjia=" + pinjia +
                '}';
    }
}
