package com.minfo.carrepair.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.activity.message.MyMessageActivity;
import com.minfo.carrepair.activity.order.OrderListActivity;
import com.minfo.carrepair.activity.personl.AboutUsActivity;
import com.minfo.carrepair.activity.personl.CheckActivity;
import com.minfo.carrepair.activity.personl.CollectActivity;
import com.minfo.carrepair.activity.personl.HelpCenterActivity;
import com.minfo.carrepair.activity.personl.KefuActivity;
import com.minfo.carrepair.activity.personl.LegalNoticeActivity;
import com.minfo.carrepair.activity.personl.MyShowPriceListActivity;
import com.minfo.carrepair.activity.setting.SettingActivity;
import com.minfo.carrepair.activity.shop.StoreActivity;
import com.minfo.carrepair.activity.wallet.WalletActivity;
import com.minfo.carrepair.dialog.ShareView;
import com.minfo.carrepair.entity.personl.User;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonlFragment extends BaseFragment implements View.OnClickListener {


    private ImageView ivSetting, ivXiang;
    private TextView tvNick;
    private LinearLayout llAllOrder, llPayOrder, llFaOrder, llShuoOrder, llPingOrder;
    private RelativeLayout rlPurse, rlMessage, rlStore, rlPurchase, rlShouCang, rlHelp, rlLaw, rlUs, rlShare, rlKefu;
    private User user = new User();

    public PersonlFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_personl, null);
        ivSetting = ((ImageView) view.findViewById(R.id.iv_setting));
        ivXiang = ((ImageView) view.findViewById(R.id.iv_xiang));

        tvNick = ((TextView) view.findViewById(R.id.tv_name));

        llAllOrder = ((LinearLayout) view.findViewById(R.id.ll_all_order));
        llPayOrder = ((LinearLayout) view.findViewById(R.id.ll_pay));
        llFaOrder = ((LinearLayout) view.findViewById(R.id.ll_fahuo));
        llShuoOrder = ((LinearLayout) view.findViewById(R.id.ll_shouhuo));
        llPingOrder = ((LinearLayout) view.findViewById(R.id.ll_ping));

        rlPurse = ((RelativeLayout) view.findViewById(R.id.rl_purse));
        rlMessage = ((RelativeLayout) view.findViewById(R.id.rl_message));
        rlStore = ((RelativeLayout) view.findViewById(R.id.rl_store));
        rlPurchase = ((RelativeLayout) view.findViewById(R.id.rl_purchase));
        rlShouCang = ((RelativeLayout) view.findViewById(R.id.rl_shoucang));
        rlHelp = ((RelativeLayout) view.findViewById(R.id.rl_help));
        rlLaw = ((RelativeLayout) view.findViewById(R.id.rl_law));
        rlUs = ((RelativeLayout) view.findViewById(R.id.rl_us));
        rlShare = ((RelativeLayout) view.findViewById(R.id.rl_share));
        rlKefu = ((RelativeLayout) view.findViewById(R.id.rl_kefu));

        ivSetting.setOnClickListener(this);
        ivXiang.setOnClickListener(this);
//        rlShouCang.setOnClickListener(this);

        llAllOrder.setOnClickListener(this);
        llPayOrder.setOnClickListener(this);
        llFaOrder.setOnClickListener(this);
        llShuoOrder.setOnClickListener(this);
        llPingOrder.setOnClickListener(this);

        rlPurse.setOnClickListener(this);
        rlMessage.setOnClickListener(this);
        rlStore.setOnClickListener(this);
        rlPurchase.setOnClickListener(this);
        rlShouCang.setOnClickListener(this);
        rlHelp.setOnClickListener(this);
        rlLaw.setOnClickListener(this);
        rlShare.setOnClickListener(this);
        rlUs.setOnClickListener(this);
        rlKefu.setOnClickListener(this);

        if (isOnline()) {

            reqServer();

        } else {
            ToastUtils.show(mActivity, "暂无网络,请重新连接");
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting:
                Intent intentSetting = new Intent(mActivity, SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", user);
                intentSetting.putExtra("info", bundle);
                startActivityForResult(intentSetting, 100);
                break;
            case R.id.iv_xiang:
//                startActivity(new Intent(mActivity, CheckActivity.class));
                break;
            case R.id.rl_shoucang:
                startActivity(new Intent(mActivity, CollectActivity.class));
                break;
            case R.id.ll_all_order:
                Intent intent = new Intent(mActivity, OrderListActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.ll_pay:
                Intent intent1 = new Intent(mActivity, OrderListActivity.class);
                intent1.putExtra("type", 0);
                startActivity(intent1);
                break;
            case R.id.ll_fahuo:
                Intent intent2 = new Intent(mActivity, OrderListActivity.class);
                intent2.putExtra("type", 1);
                startActivity(intent2);
                break;
            case R.id.ll_shouhuo:
                Intent intent3 = new Intent(mActivity, OrderListActivity.class);
                intent3.putExtra("type", 2);
                startActivity(intent3);
                break;
            case R.id.ll_ping:
                Intent intent4 = new Intent(mActivity, OrderListActivity.class);
                intent4.putExtra("type", 3);
                startActivity(intent4);
                break;
            case R.id.rl_purse:
                startActivity(new Intent(mActivity, WalletActivity.class));
                break;
            case R.id.rl_message:
                startActivity(new Intent(mActivity, MyMessageActivity.class));
                break;
            case R.id.rl_store:
//                startActivity(new Intent(mActivity, GoodManagerActivity.class));
                startActivity(new Intent(mActivity, StoreActivity.class));
                break;
            case R.id.rl_purchase:
                startActivity(new Intent(mActivity, MyShowPriceListActivity.class));
                break;
            case R.id.rl_help:
                startActivity(new Intent(mActivity, HelpCenterActivity.class));
                break;
            case R.id.rl_law:
                startActivity(new Intent(mActivity, LegalNoticeActivity.class));
                break;
            case R.id.rl_us:
                startActivity(new Intent(mActivity, AboutUsActivity.class));
                break;
            case R.id.rl_share:
                showShareDialog();
                break;
            case R.id.rl_kefu:
                startActivity(new Intent(mActivity, KefuActivity.class));
                break;

        }
    }

    /**
     * 分享弹框
     */
    private void showShareDialog() {
//        ShareView shareView = new ShareView(mActivity);
//
//        DialogUtil.showDialog(mActivity, shareView);
        if(user != null) {
            ShareView shareView = new ShareView(mActivity);
            shareView.setData(user.getDown_url());
            DialogUtil.showDialog(mActivity, shareView);
        } else {
            ToastUtils.show(mActivity, "获取分享数据失败");
        }
    }

    /**
     * 请求个人中心接口
     */
    private void reqServer() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid);

        final String url = getResources().getString(R.string.api_baseurl) + "user/Main.php";
        Log.e(TAG, utils.getBasePostStr() + "*" + Constant.userid);

        httpClient.post(url, params, new RequestListener() {

            @Override
            public void onPreRequest() {
                showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {

                cleanWait();
                if (response != null) {
                    user = response.getObj(User.class);
                    setInfo();
                }
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, msg);
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                Log.e(TAG, errorcode + "");
                if(errorcode == 10) {
                    utils.jumpTo(mActivity, LoginActivity.class);
                    return;
                }
                if (errorcode != 0) {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }
        });
    }

    /**
     * 填充信息
     */
    private void setInfo(){
        tvNick.setText(user.getName());
//        UniversalImageUtils.displayImageUseDefOptions(user.getImg(), ivXiang);
        UniversalImageUtils.displayImageCircle(user.getImg(), ivXiang, 240);
        utils.saveImg(user.getImg());
        utils.saveName(user.getName());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100) {
            reqServer();
        }
    }
}
