package com.minfo.carrepair.entity;

import java.io.Serializable;

/**
 * Created by min-fo-012 on 17/6/20.
 */
public class WLInfoList implements Serializable {
    private String time;
    private String context;
    private String location;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "WLInfoList{" +
                "time='" + time + '\'' +
                ", context='" + context + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
