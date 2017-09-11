package com.minfo.carrepairseller.entity.address;

import java.util.List;

/**
 * Created by fei on 16/2/22.
 */
public class Province {
    private String id;
    private String name;
    private List<City> city;

    public Province(String id, String name, List<City> city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public Province() {
    }

    @Override
    public String toString() {
        return "Province{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", city=" + city +
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

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }
}
