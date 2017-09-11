package com.minfo.carrepairseller.utils;

/**
 * Created by MinFo021 on 17/6/27.
 */

public class TypeChangeUtil {
    /**
     * 根据type返回值获取类型值
     * @param type
     */
    public static String getTypeFrom(String type) {
        String strType = "";
        if(type != null) {
            String[] ps = {"原厂", "拆车", "品牌", "其他"};
            String types[] = type.split(",");
            for (int i = 0; i < types.length; i++) {
                if (Integer.parseInt(types[i]) > ps.length - 1)
                    continue;
                strType += ps[Integer.parseInt(types[i])];
                strType += " ";
            }
        }
        return strType;
    }
}
