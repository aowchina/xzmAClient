package com.minfo.carrepair.activity.wallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.dialog.EditMoneyView;
import com.minfo.carrepair.entity.wallet.WalletModel;
import com.minfo.carrepair.entity.wallet.WalletRecord;
import com.minfo.carrepair.entity.wallet.WalletRecordEntiry;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.ToastUtils;
import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import minfo.com.albumlibrary.utils.LG;

/**
 * 我的钱包
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener{
    private static final int SSO_COMPLETE = 0;
    private static final int SSO_CANCLE = 1;
    private static final int SSO_ERROR = 2;

    private ImageView ivBack; // 返回按钮
    private TextView tvTitle, tvRight; // 标题
    private TextView tvMoney; // 钱包余额
    private Button mButton; //  提现按钮

    private LinearLayout llEmpty; // 无提现记录布局
    private ListView listView;
    private RadioGroup radioGroup; // 提现方式选择
    private int outWay = 1; // 1是微信提现，2是支付宝提现，默认为微信提现
    private List<WalletRecord> tempList = new ArrayList<>();

    private List<WalletRecord> records = new ArrayList<>();
    private CommonAdapter<WalletRecord> commonAdapter;

    private WalletModel walletModel;
    private float outMoney;
    private String alipayAccount;

    private boolean isClick = false;
    private PlatformDb platformDb;
    private boolean isComplete;
    private WalletRecordEntiry walletRecordEntiry;
    private int page = 1;
    private boolean isLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_wallet);
//        setContentView(R.layout.activity_wallet);
        MobSDK.init(this);
    }

    @Override
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMoney = (TextView) findViewById(R.id.tv_wallet_money);
        mButton = (Button) findViewById(R.id.bt_tixian);

        llEmpty = (LinearLayout) findViewById(R.id.ll_no_message);
        listView = (ListView) findViewById(R.id.lv_wallet);
        radioGroup = (RadioGroup) findViewById(R.id.rg_out_way);
        tvRight = ((TextView) findViewById(R.id.tv_right));

    }

    @Override
    protected void initViews() {
        tvTitle.setText("我的钱包");
        tvRight.setText("账单");
        tvRight.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(this);
        mButton.setOnClickListener(this);
        tvRight.setOnClickListener(this);


        commonAdapter = new CommonAdapter<WalletRecord>(mActivity, records, R.layout.content_wallet) {
            @Override
            public void convert(BaseViewHolder helper, WalletRecord item, int position) {
                helper.setText(R.id.tv_date, item.getPaytime());
                if (item.getType().equals("1")) {
                    helper.setText(R.id.tv_tixian_to, "提现到支付宝");
                } else if (item.getType().equals("2")) {
                    helper.setText(R.id.tv_tixian_to, "提现到微信");
                }
                helper.setText(R.id.tv_money, item.getTxmoney());

            }
        };
        listView.setAdapter(commonAdapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {
                    outWay = 1;
                } else if (checkedId == R.id.radioButton2) {
                    outWay = 2;
                }

                LG.e("钱包", "outWay=" + (outWay == 1 ? "微信" : "支付宝"));
            }
        });
        if (isOnline()) {
            showWait();
            reqData();


        } else {
            ToastUtils.show(mActivity, "暂无网络,请重新连接");
        }
        //上拉加载数据
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        //加载更多功能的代码
                        if (isOnline()) {
                            page++;
                            isLoad = true;
                            reqTiXianData();
                        } else {
                            ToastUtils.show(mActivity, "暂无网络");
                        }

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_tixian:
                showEditDialog();
                break;
            case R.id.tv_right:
                startActivity(new Intent(WalletActivity.this, BillCheckActivity.class));
                break;
        }
    }

    /**
     * 显示提现金额编辑弹框
     */
    private void showEditDialog() {

        if (walletModel == null) {
            ToastUtils.show(mActivity, "无法获取到提现数据");
            return;
        }
        EditMoneyView dialogView = new EditMoneyView(mActivity, Float.valueOf("" + walletModel.getMoney()));

        if (outWay == 1) {
            dialogView.setEtAlipayVisiable(false);
            dialogView.setOnClickEnsureListener(new EditMoneyView.OnClickEnsureListener() {
                @Override
                public void ensure(float money) {
                    outMoney = money;
//                    reqTiXian();
//                    ToastUtils.show(mActivity, "提现微信");

//                   Platform platform = ShareSDK.getPlatform(Wechat.NAME);
//                   platform.setPlatformActionListener(mActivity);
//                   platform.showUser(null);
                    logByWX();

                }
            });
        } else {
            dialogView.setEtAlipayVisiable(true);
            dialogView.setOnClickEnsureListener(new EditMoneyView.OnClickEnsureListener2() {
                @Override
                public void ensure(String alipay, float money) {
                    alipayAccount = alipay;
                    outMoney = money;
                    reqTiXian();
                    ToastUtils.show(mActivity, "提现支付宝");
                }
            });
        }
        Dialog dialog = DialogUtil.showSelfDialog(mActivity, dialogView);
        dialog.show();
    }

    //请求接口(余额接口)
    private void reqData() {
        if (!isOnline()) {
            ToastUtils.show(mActivity, "暂无网络");
            return;
        }

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid);
        String postUrl = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_USER_WALLET;
        httpClient.post(postUrl, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if (response != null) {
//                    radioGroup.setVisibility(View.GONE);
//                    ivOutMoney.setClickable(true);
//                    ivOutMoney.setBackgroundResource(R.drawable.wallect_shape);

                    walletModel = response.getObj(WalletModel.class);
                    if (walletModel != null) {
//                        radioGroup.setVisibility(View.VISIBLE);
                        //double sumMoney = Double.parseDouble(new DecimalFormat("##0.00").format(wallect.getMoney()));
                        tvMoney.setText("￥" + walletModel.getMoney());
                        LG.e("我的钱包", "wallect == not null" + "  " + walletModel.getMoney());
                        reqTiXianData();

                    } else {
                        LG.e("我的钱包", "wallect == null");
                    }
                } else {
                    ToastUtils.show(mActivity, "请求数据失败");
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 10:
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        break;
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                        ToastUtils.show(mActivity, "网络异常！");
                        break;
                    case 105:
                        ToastUtils.show(mActivity, "服务器繁忙！");
                        break;
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }
        });

    }


    //请求接口
    private void reqTiXian() {

        if (!isOnline()) {
            ToastUtils.show(mActivity, "暂无网络");
            return;
        }
        showWait();
        Map<String, String> params;
        String postUrl;
        if (outWay == 1) {
//                    params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + outMoney + "*" + unionid);
//            params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + outMoney + "*" + "oTelMwHOYIjMPmK2EIHaweokXFiU");
            params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + outMoney + "*" + platformDb.getUserId());
            postUrl = getResources().getString(R.string.api_baseurl) + "user/txToWx.php";
        } else {
            params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + outMoney + "*" + alipayAccount);
            postUrl = getResources().getString(R.string.api_baseurl) + "user/txToZfb.php";

        }
        httpClient.post(postUrl, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                if (response != null) {
                    ToastUtils.show(mActivity, "提现成功！");
                    reqData();
                } else {
                    ToastUtils.show(mActivity, "请求数据失败");
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    // 12，38，40，43，44，45，46，47，48
                    case 12:
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        break;
                    case 38:
                    case 40:
                        ToastUtils.show(mActivity, "您的余额无法提现！");
                        break;
                    case 39:
                        ToastUtils.show(mActivity, "余额提现失败，请稍后再试");
                        break;
                    case 44:
                        ToastUtils.show(mActivity, "单笔额度超限！");
                        break;
                    case 45:
                        ToastUtils.show(mActivity, "收款账号不存在！");
                        break;
                    case 48:
                        ToastUtils.show(mActivity, "单笔额度不能超过5万！");
                        break;
                    case 55:
                        ToastUtils.show(mActivity, "提现金额不合理，提现金额范围（1~20000）");
                        break;
                    case 57:
                        ToastUtils.show(mActivity, "您的余额不足");
                        break;
                    case 63:
                        ToastUtils.show(mActivity, "提现失败，请确认您的网络畅通");
                        break;
                    case 101:
                    case 102:
                    case 103:
                    case 104:
                        ToastUtils.show(mActivity, "网络异常！");
                        break;
                    case 105:
                        ToastUtils.show(mActivity, "服务器繁忙！");
                        break;
                    default:
                        LG.e("钱包", "errorcode=."+errorcode);
                        ToastUtils.show(mActivity, "提现失败");
                        break;
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "连接服务器失败，请稍后再试！");
            }
        });

    }

    /**
     * 请求提现纪录接口
     */
    private void reqTiXianData() {

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + page);
        String postUrl = getResources().getString(R.string.api_baseurl) + "user/txRecord.php";
        httpClient.post(postUrl, params, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if (response != null) {
                    Log.e("response", response.getData());
                    walletRecordEntiry = response.getObj(WalletRecordEntiry.class);
                    if (walletRecordEntiry != null) {
                        records.clear();
                        records.addAll(walletRecordEntiry.getList());
                        if (records != null && records.size() > 0) {
                            listView.setVisibility(View.VISIBLE);
                            llEmpty.setVisibility(View.GONE);
                            commonAdapter.notifyDataSetChanged();
                        }
                        if (page > 1) {
                            if (records.size() > 0) {
                                ToastUtils.show(WalletActivity.this, "加载成功");
                            } else {
                                ToastUtils.show(WalletActivity.this, "无更多内容");
                            }
                        }

                    }

                } else {
                    ToastUtils.show(mActivity, "请求数据失败");
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 12:
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        break;

                    case 53:
                        ToastUtils.show(mActivity, "您没有提现记录！");
                        break;
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙！");
                        break;
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }
        });

    }

    /**
     * 微信账号登录
     *
     * @param
     */
    public void logByWX() {
        LG.e("第三方登录", "isClick=" + isClick);
        if (isClick) {
            return;
        }
        isClick = true;
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        platform.setPlatformActionListener(this);
        platform.showUser(null);
        ToastUtils.show(mActivity, "正在获取您的账号信息...");
    }

    /**
     * 取消第三方登录
     *
     * @param platform
     * @param action
     */
    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = SSO_CANCLE;
        msg.arg1 = action;
        msg.obj = platform;
        mLogHandler.sendMessage(msg);
        LG.e("三方登录", platform.getName());
    }

    /**
     * 授权成功
     *
     * @param platform
     * @param action
     * @param res
     */
    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        isComplete = true;
        Message msg = new Message();
        msg.what = SSO_COMPLETE;
        msg.arg1 = action;
        msg.obj = platform;
//        try{
//            unionid = (String) res.get("unionid");
//        }
//        catch(Exception e) {
//            unionid = null;
//            LG.i("unionid = null");
//        }
        mLogHandler.sendMessage(msg);
        LG.e("三方登录", platform.getName());
        LG.i("111" + res);
        LG.i("222" + platform.getDb());
        LG.i("333" + platform.getDb().get("account"));
    }

    /**
     * 授权失败
     *
     * @param platform
     * @param action
     * @param error
     */
    @Override
    public void onError(Platform platform, int action, Throwable error) {
        Message msg = new Message();
        msg.what = SSO_ERROR;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = platform;
        Bundle data = new Bundle();
        if (error.toString().contains("NotExistException")) {
            data.putString("msg", "请先安装微信客户端");
        } else {
            data.putString("msg", "授权失败，请稍后再试");
        }
        msg.setData(data);
        mLogHandler.sendMessage(msg);
        LG.e("三方登录", platform.getName());
    }

    private Handler mLogHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Platform platform = (Platform) msg.obj;
            if (platform != null) {
                switch (msg.what) {
                    case SSO_COMPLETE:
                        platformDb = platform.getDb();
                        if (platformDb != null) {
//                            loginother(platformDb);
                            reqTiXian();
                        } else {
                            ToastUtils.show(mActivity, "异常");
                        }
                        break;
                    case SSO_CANCLE:
                        ToastUtils.show(mActivity, "登录取消");
                        break;
                    case SSO_ERROR:
                        Bundle bundle = msg.getData();
                        String error = bundle.getString("msg");
                        if (!isComplete) {
                            ToastUtils.show(mActivity, "" + error);
                        }
                        break;
                    default:
                        break;
                }
            }
            isClick = false;
            super.handleMessage(msg);
        }

    };

}
