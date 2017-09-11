package com.minfo.carrepairseller.entity.wl;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/20.
 */
public class WLInfo implements Serializable {
    private WLInfoEntity list;
    private String word;

    public WLInfoEntity getList() {
        return list;
    }

    public void setList(WLInfoEntity list) {
        this.list = list;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "WLInfo{" +
                "list=" + list +
                ", word='" + word + '\'' +
                '}';
    }
}
