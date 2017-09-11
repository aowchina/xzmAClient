package com.minfo.carrepair.entity.showprice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/14.
 */
public class ShowPriceInfo implements Serializable {
    private List<ShowPriceList>setMoneyList;

    public List<ShowPriceList> getSetMoneyList() {
        return setMoneyList;
    }

    public void setSetMoneyList(List<ShowPriceList> setMoneyList) {
        this.setMoneyList = setMoneyList;
    }

    @Override
    public String toString() {
        return "ShowPriceInfo{" +
                "setMoneyList=" + setMoneyList +
                '}';
    }
}
