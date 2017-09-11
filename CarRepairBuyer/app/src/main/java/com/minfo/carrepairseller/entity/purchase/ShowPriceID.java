package com.minfo.carrepairseller.entity.purchase;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/14.
 */
public class ShowPriceID implements Serializable {
    private String setMoneyId;

    public String getSetMoneyId() {
        return setMoneyId;
    }

    public void setSetMoneyId(String setMoneyId) {
        this.setMoneyId = setMoneyId;
    }

    @Override
    public String toString() {
        return "ShowPriceID{" +
                "setMoneyId='" + setMoneyId + '\'' +
                '}';
    }
}
