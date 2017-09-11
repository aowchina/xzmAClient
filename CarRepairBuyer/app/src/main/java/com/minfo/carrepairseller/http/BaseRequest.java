/*
*BaseRequest.java
*Created on 2014-9-25 上午10:38 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.minfo.carrepairseller.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.minfo.carrepairseller.BuildConfig;
import com.minfo.carrepairseller.jni.JniClient;
import com.minfo.carrepairseller.utils.LG;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class BaseRequest extends Request<BaseResponse> {

    static {
        System.loadLibrary("CARREPAIRSELLER");
    }

    public static final String TAG="BaseRequest";

    private  Response.Listener<BaseResponse> mListener;
    private Map<String,String> mParams;

    public BaseRequest(int method, String url, Map<String,String> params, Response.Listener listener, Response.ErrorListener Errorlistener) {
        super(method, url, Errorlistener);

        mListener = listener;
        this.mParams =params;
        setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



    @Override
    protected Response<BaseResponse> parseNetworkResponse(NetworkResponse response) {

        try {

            String jsonString  = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.e(TAG, "respone:" + jsonString);
            if (BuildConfig.DEBUG)
                Log.e(TAG, "respone:" + jsonString);

            BaseResponse baseResponse = parseJson(jsonString);
            LG.e("BaseRequest", "baseResponse="+baseResponse);
            return Response.success(baseResponse, HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return  null;


    }

    @Override
    protected void deliverResponse(BaseResponse response) {

        mListener.onResponse(response);
    }



    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }



    private BaseResponse parseJson(String json)
    {
        int errorcode =-1;
        String msg=null;
        String data=null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            errorcode =  jsonObject.getInt("errorcode");
            data =jsonObject.getString("data");
            if(data.length()>0){

                JSONObject jsonObj = new JSONObject(data);
                String DataJson = "";
                int count = jsonObj.getInt("cnt");
                for(int i=0;i<count;i++){
                    String strTemp = jsonObj.getString(i+"");
                    if(strTemp.length() != 0){
                        DataJson += JniClient.GetDecodeStr(strTemp);
                    }
                }
                data = DataJson;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseResponse response = new BaseResponse();
        response.setErrorcode(errorcode);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }
}
