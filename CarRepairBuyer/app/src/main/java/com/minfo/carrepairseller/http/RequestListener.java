
package com.minfo.carrepairseller.http;


public interface RequestListener {

    /**
     * 在请求之前调用的方法
     */
    public  void onPreRequest();

    /**
     * 请求成功调用
     * @param response
     */
    public  void onRequestSuccess(BaseResponse response);
    public  void onRequestNoData(BaseResponse response);

    /**
     * 请求失败调用，致命错误
     * @param code
     * @param msg
     */
    public  void onRequestError(int code, String msg);


}
