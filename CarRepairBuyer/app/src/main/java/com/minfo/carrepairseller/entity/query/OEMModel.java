package com.minfo.carrepairseller.entity.query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by MinFo021 on 17/6/13.
 */

public class OEMModel implements Serializable {
    private EPCDetailItem object;
    private List<EPCTop > list;

    public EPCDetailItem getObject() {
        return object;
    }

    public List<EPCTop> getList() {
        return list;
    }

    public void setObject(EPCDetailItem object) {
        this.object = object;
    }

    public void setList(List<EPCTop> list) {
        this.list = list;
    }
}
