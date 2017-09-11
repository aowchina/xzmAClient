package com.minfo.carrepair.entity.wallet;

import java.io.Serializable;
import java.util.List;

/**
 * Created by min-fo-012 on 17/6/21.
 */
public class WalletRecordEntiry implements Serializable {
private List<WalletRecord>list;

    public List<WalletRecord> getList() {
        return list;
    }

    public void setList(List<WalletRecord> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "WalletRecordEntiry{" +
                "list=" + list +
                '}';
    }
}
