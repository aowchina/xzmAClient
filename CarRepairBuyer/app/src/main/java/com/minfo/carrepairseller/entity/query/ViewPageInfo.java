package com.minfo.carrepairseller.entity.query;

import android.os.Bundle;

/**
 * Created by liujing on 16/1/18.
 */
public class ViewPageInfo {
    public final String tag;
    public final Class<?> clss;
    public final Bundle args;
    public final String title;

    public ViewPageInfo(String _title, String _tag, Class<?> _class, Bundle _args) {
        title = _title;
        tag = _tag;
        clss = _class;
        args = _args;
    }
}
