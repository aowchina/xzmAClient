package com.minfo.carrepairseller.entity.query;

import java.io.Serializable;

/**
 * Created by MinFo021 on 17/6/9.
 */

public class ChejiaInfo implements Serializable {
    private String content;
    private String name;

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public void setContent(String title) {
        this.content = title;
    }

    public void setName(String name) {
        this.name = name;
    }
}
