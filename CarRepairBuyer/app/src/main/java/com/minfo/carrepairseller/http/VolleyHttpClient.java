package com.minfo.carrepairseller.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Map;


public class VolleyHttpClient {

    private VolleySingleton volleySingleton;
    private Context mContext;


    public VolleyHttpClient(Context context) {

        mContext = context;
        volleySingleton = VolleySingleton.getInstance(context);

    }

    public void jsonReq(JsonObjectRequest request){
        volleySingleton.addToRequestQueue(request);
    }


    public void post(String url, Map<String, String> params,final RequestListener listener) {

        request(Request.Method.POST, url, params, listener);
    }


    public void get(String url,final RequestListener listener) {

        request(Request.Method.GET, url, null, listener);
    }

    public void request(int method,String url,Map<String, String> params,final RequestListener listener) {


        if (listener != null)
            listener.onPreRequest();

       BaseRequest request = new BaseRequest(method,url,params,new Response.Listener<BaseResponse>() {

                    public void onResponse(BaseResponse response) {
                        if (listener != null) {
                            if (response.isSuccess()) {
                                listener.onRequestSuccess(response);
                            }else{
                                listener.onRequestNoData(response);
                            }
                        }
                    }
                },

                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        try {
                            String errMsg = null;
                            int errCode = -1;
                            if (error == null) {

                                errMsg = "请求服务器出错，错误代码未知";
                            } else {
                                errMsg = VolleyErrorHelper.getMessage(mContext, error);
                                errCode = error.networkResponse == null ? errCode : error.networkResponse.statusCode;
                            }

                            if (listener != null) {
                                listener.onRequestError(errCode, errMsg);
                            }
                        } catch (Exception e) {
                            Log.e("请求异常", "请求异常");
                            e.printStackTrace();
                        }

                    }
                }
        );
        volleySingleton.addToRequestQueue(request);
    }

}
