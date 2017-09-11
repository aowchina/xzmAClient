
package com.minfo.carrepair.http;

import android.text.TextUtils;

import com.minfo.carrepair.jni.JniClient;
import com.minfo.carrepair.utils.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class BaseResponse {

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;

    private Integer errorcode;

    private String msg;

    private String data;


    public Integer getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public boolean isSuccess() {
        return errorcode == SUCCESS;
    }


    public <T> T getObj(Class<T> clz) {

        if (TextUtils.isEmpty(data)) {
            return null;
        } else {
            return JSONUtil.fromJson(data, clz);
        }
    }

    /**
     * 需要解密
     * @param clz
     * @param type false：不需要解密  true：需要解密
     * @param <T>
     * @return
     */
    public <T> T getObj(Class<T> clz, boolean type) {

        if (TextUtils.isEmpty(data)) {
            return null;
        } else {
            if(!type){
                return JSONUtil.fromJson(data, clz);
            }else {
                try {
                    JSONObject jsonObj = new JSONObject(data);
                    String DataJson = "";
                    int count = jsonObj.getInt("cnt");
                    for (int i = 0; i < count; i++) {
                        String strTemp = jsonObj.getString(i + "");
                        if (strTemp.length() != 0) {
                            DataJson += JniClient.GetDecodeStr(strTemp);
                        }
                    }
                    data = DataJson;
                    return JSONUtil.fromJson(data, clz);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }


    public <T> List<T> getList(Class<T> clz) {
        if (TextUtils.isEmpty(data))
            return null;
        List<T> list = new ArrayList<T>();
        Type listType = type(List.class, clz);
        list = JSONUtil.fromJson(data, listType);
        return list;
    }

    /**
     * 需要解密
     * @param clz
     * @param type false:不需要解密  true:需要解密
     * @param <T>
     * @return
     */
    public <T> List<T> getList(Class<T> clz, boolean type) {
        if (TextUtils.isEmpty(data)) {
            return null;
        } else {
            List<T> list = new ArrayList<T>();
            Type listType = type(List.class, clz);
            if(!type){
                list = JSONUtil.fromJson(data, listType);
                return list;
            }else {
                try {
                    JSONObject jsonObj = new JSONObject(data);
                    String DataJson = "";
                    int count = jsonObj.getInt("cnt");
                    for (int i = 0; i < count; i++) {
                        String strTemp = jsonObj.getString(i + "");
                        if (strTemp.length() != 0) {
                            DataJson += JniClient.GetDecodeStr(strTemp);
                        }
                    }
                    data = DataJson;
                    list = JSONUtil.fromJson(data, listType);
                    return list;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }


    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }

        };
    }

    @Override
    public String toString() {
        return getData();
    }
}
