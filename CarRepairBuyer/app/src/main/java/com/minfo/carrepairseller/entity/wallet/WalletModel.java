package com.minfo.carrepairseller.entity.wallet;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/5.
 */

public class WalletModel implements Serializable {

    private String money;
    private List<WalletRecord> records;

    public String getMoney() {
        return money;
    }

    public List<WalletRecord> getRecords() {
        return records;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setRecords(List<WalletRecord> records) {
        this.records = records;
    }
}
