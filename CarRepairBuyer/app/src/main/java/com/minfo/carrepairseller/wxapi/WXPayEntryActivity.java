package com.minfo.carrepairseller.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.order.OrderSureActivity;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    public static String orderid;
    private IWXAPI api;
//    protected OrderSureActivity mso;
    private static Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, getString(R.string.wxappid));
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {

    }

    public static void setmActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //验证支付的errorcode

            int responseCode = resp.errCode;
            if (responseCode == 0) {
                //              客户端支付成功，请求服务器看是否确实成功
                //				if(mfu.isOnLine(WXPayEntryActivity.this)){
                //					new Thread(){
                //						public void run(){
                //							try {
                //								String postUrl = getResources().getString(R.string.api_baseurl) + "PayNotify.php";
                //								String postStr = mfu.getBasePostStr() + "*" + mfu.getUserid() + "*" + UserPay.wx_myorderid;
                //
                //								String reqStr = mfu.getServerData(postUrl, postStr);
                //								if (reqStr != null) {
                //									JSONObject jsonObj = new JSONObject(reqStr);
                //									int errorcode = jsonObj.getInt("errorcode");
                //									if(errorcode == 0){
                ToastUtils.show(this, "支付成功");
                if(mActivity != null && !mActivity.isFinishing()) {
                    mActivity.finish();
                }

                //									}else{
                //										mfu.sendMsg(UserPay.handler, 403);
                //									}
                //								}else{
                //									mfu.sendMsg(UserPay.handler, 403);
                //								}
                //							}catch (Exception e) {
                //								mfu.sendMsg(UserPay.handler, 403);
                //								e.printStackTrace();
                //							}
                //						}
                //					}.start();
                //				}else{
                //					Toast.makeText(WXPayEntryActivity.this, "请检查您的网络连接", Toast.LENGTH_SHORT).show();
                //				}
            }
            else {
                Toast.makeText(WXPayEntryActivity.this, "支付失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }
            WXPayEntryActivity.this.finish();
        }
    }
}