package com.minfo.carrepairseller.entity.address;

import java.util.List;

/**
 * Created by fei on 16/2/22.
 */
public class City {
    private String id;
    private String name;
    private List<District> area;

    public City(String id, String name, List<District> district) {
        this.id = id;
        this.name = name;
        this.area = district;
    }

    public City() {
    }

    @Override
    public String toString() {
        return "Province{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", city=" + area +
                '}';
    }

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

    public List<District> getArea() {
        return area;
    }

    public void setArea(List<District> area) {
        this.area = area;
    }
}
