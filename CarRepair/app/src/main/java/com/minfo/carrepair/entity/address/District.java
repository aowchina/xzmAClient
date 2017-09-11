package com.minfo.carrepair.entity.address;

/**
 * Created by fei on 16/2/22.
 */
public class District {
    private String id;
    private String name;

    public District(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public District() {
    }

    @Override
    public String toString() {
        return "District{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
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
}
