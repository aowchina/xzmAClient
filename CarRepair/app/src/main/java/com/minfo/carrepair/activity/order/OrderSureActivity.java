package com.minfo.carrepair.activity.order;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.address.AddressListActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.dialog.ChoosePaymentView;
import com.minfo.carrepair.dialog.TipView;
import com.minfo.carrepair.entity.order.OrderSureEntity;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.MD5;
import com.minfo.carrepair.utils.PayResult;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;
import com.minfo.carrepair.wxapi.WXPayEntryActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import minfo.com.albumlibrary.utils.LG;

public class OrderSureActivity extends BaseActivity implements View.OnClickListener {

    private TextView center_title_text;
    private TextView pay_amount;

    private ImageView ivBack;
    private ListView listview;
    //    private RelativeLayout rlPayment; // 支付方式
//    private TextView tvPayment; // 支付方式
    private RelativeLayout rladdressInfo;
    private TextView paybtn;
    private TextView companyName;
    private TextView companyPhone;
    private TextView companyAddress; //

    //    private String addressId;
    private OrderSureEntity orderSureEntity;
    //    private OrdeList_Entity underOrdeEntity;
    private List<OrderSureEntity.GoodInfo> list = new ArrayList<>();
    private CommonAdapter<OrderSureEntity.GoodInfo> msladapter;
    //支付宝常量
    private static final int SDK_PAY_FLAG = 1;

    //微信常量
    private IWXAPI wxapi;
    private String wx_prepayid = "";
    private String wx_prepay_nonestr = "";
    private PayReq wx_payReq;
    private String wxAppid = "wx6dd4b8261beb4d4a";


    private boolean isBank;//银行卡支付标志
    private boolean isAlipay = true;//支付宝支付标志
    private boolean isYue = false;//余额支付标志
    private boolean isWeichat;//微信支付支付标志
    private String orderid;//微信获取的订单号
    private String name;
    private String tel;
    private String address;
    private TextView pay_cancle;

    private TextView tvTime; // 下单时间
    private TextView tvOrderNum; // 订单号
    private String orderId;
//    private String addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_order_sure);
//        setContentView(R.layout.activity_order_sure);
    }

    @Override
    protected void findViews() {
        orderId = getIntent().getStringExtra("orderid");

        //标题
        ivBack = (ImageView) findViewById(R.id.iv_back);
        center_title_text = (TextView) findViewById(R.id.tv_title);
        pay_amount = (TextView) findViewById(R.id.pay_amount);

        listview = (ListView) findViewById(R.id.lv_products);
        paybtn = (TextView) findViewById(R.id.pay_btn);
        pay_cancle = (TextView) findViewById(R.id.pay_cancle);
        ivBack.setVisibility(View.VISIBLE);
        center_title_text.setText("确认订单");

        View viewTop = LayoutInflater.from(this).inflate(R.layout.content_order_sure_top, null);
        companyName = ((TextView) viewTop.findViewById(R.id.tv_name));
        companyPhone = ((TextView) viewTop.findViewById(R.id.tv_phone));
        companyAddress = ((TextView) viewTop.findViewById(R.id.tv_address));

//        tvOrderNum.setVisibility(View.VISIBLE);
        rladdressInfo = (RelativeLayout) viewTop.findViewById(R.id.rl_address);
        rladdressInfo.setOnClickListener(this);

        View viewBottom = LayoutInflater.from(mActivity).inflate(R.layout.content_order_sure_bottom, null);
        tvTime = ((TextView) viewBottom.findViewById(R.id.tv_time));
//        tvTime.setVisibility(View.VISIBLE);
        tvOrderNum = ((TextView) viewBottom.findViewById(R.id.tv_code));

        listview.addHeaderView(viewTop);
        listview.addFooterView(viewBottom);
    }

    @Override
    protected void initViews() {

        ivBack.setOnClickListener(this);
        pay_cancle.setOnClickListener(this);
        paybtn.setOnClickListener(this);


        msladapter = new CommonAdapter<OrderSureEntity.GoodInfo>(mActivity, list, R.layout.item_order_product) {
            @Override
            public void convert(BaseViewHolder helper, OrderSureEntity.GoodInfo item, int position) {
                ImageView iv = helper.getView(R.id.iv_product);
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_guige, item.getBname() + "     " + item.getSname() + "   " + item.getCname());
                helper.setText(R.id.tv_price, item.getMoney());
//                helper.setText(R.id.tv_num, item.getNum()+"");

                UniversalImageUtils.displayImage(item.getImg(), iv);
            }
        };

        listview.setAdapter(msladapter);

        initData();
    }

    //初始化数据
    private void initData() {
        tvOrderNum.setText("订单号: " + orderId);
        if (isOnline()) {
            showWait();
//            reqAddressList();
            reqOrderInfo();
        }
    }


//    @Override protected void onResume() {
//        super.onResume();
//        if (isOnline()) {
//            showWait();
//            reqOrderInfo();
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: // 修改地址
                if (resultCode == RESULT_OK) {
                    LG.e("确认订单", data.getStringExtra("addressId"));
//                    addressId = data.getStringExtra("addressId");
                    reqOrderInfo();
                }
                break;
        }
    }

    private void rqdata() {
        cleanWait();

        orderSureEntity = new OrderSureEntity();
        long time = System.currentTimeMillis();
//        orderSureEntity.setTime(time + "");
        orderSureEntity.setAddress("博雅CC商业广场中心");
        orderSureEntity.setTel("15813681200");
        orderSureEntity.setName("购物者");
//        List<ProductItem> listGoods = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            ProductItem good = new ProductItem();
////            good.setGoods_num(i+"");
////            good.setId(i+"");
////            good.setName("商品"+i);
////            good.setPrice(100+i+"");
////            good.setNum(i);
////            listGoods.add(good);
//        }
//        orderSureEntity.setProductItems(listGoods);

        //控件赋值
        companyName.setText(orderSureEntity.getName() + "");
        companyPhone.setText(orderSureEntity.getTel());
        companyAddress.setText(orderSureEntity.getAddress());
//        pay_amount.setText("￥" + orderSureEntity.getPrice());
//        tvTime.setText("下单时间：" + orderSureEntity.getTime());
////        tvOrderNum.setText("订单号:"+orderSureEntity.);
//
//        list.addAll(orderSureEntity.getProductItems());
        msladapter.notifyDataSetChanged();
//        if (orderSureEntity.getGoods() != null && orderSureEntity.getGoods().size() > 0) {
//            msladapter = new Make_Sure_ListAdapter(mActivity, orderSureEntity.getGoods());
//            makesureorde_lv.setAdapter(msladapter);
//        }
    }

    private String getAllMoney() {
        float allPrice = 0f;//购买商品需要的总金额
        for (int i = 0; i < orderSureEntity.getGoods().size(); i++) {
            allPrice += Integer.parseInt(orderSureEntity.getGoods().get(i).getAmount()) * Float.parseFloat(orderSureEntity.getGoods().get(i).getMoney());
        }
        return new DecimalFormat("#0.00").format(allPrice);
    }


    //支付宝异步返回结果
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    Log.e("payResult", (String) msg.obj);

                    String resultStatus = payResult.getResultStatus();
                    Log.e("resultStatus", resultStatus + " " + resultInfo);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        cleanWait();
                        ToastUtils.show(mActivity, "支付成功");
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态)
                        if (TextUtils.equals(resultStatus, "8000")) {
                            cleanWait();
                            Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            cleanWait();
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:

                    break;
            }
        }
    };


    //请求订单的地址信息及数据
    private void reqOrderInfo() {
        boolean isDebug = false;
        if (isDebug) {
            rqdata();
            return;
        }
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + orderId);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ORDER_MAKE_SURE;
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                int errorcode = response.getErrorcode();
                cleanWait();
                Log.e("Detail", response.getData());
                orderSureEntity = response.getObj(OrderSureEntity.class);
                list.clear();
                //控件赋值
                companyName.setText(orderSureEntity.getName());
                companyPhone.setText(orderSureEntity.getTel());
                companyAddress.setText(orderSureEntity.getAddress());
                pay_amount.setText("￥" + getAllMoney());
                tvTime.setText("下单时间：" + "");
//                addressId = orderSureEntity.getAddressId();
//
//
                if (orderSureEntity.getGoods() != null && orderSureEntity.getGoods().size() > 0) {
//                    msladapter = new Make_Sure_ListAdapter(mActivity, orderSureEntity.getGoods());
//                    makesureorde_lv.setAdapter(msladapter);
                    list.addAll(orderSureEntity.getGoods());

                }
                msladapter.notifyDataSetChanged();
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
                if (errorcode == 11 || errorcode == 12) {
                    ToastUtils.show(mActivity, "帐号异常,请重新登录");
//                    utils.intent2Class(mActivity, Login.class);
//                    appManager.finishAllActivity();
                } else if (errorcode == 14) {
                    ToastUtils.show(mActivity, "该订单不存在");
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }
        });
    }

    private void callAlipay(final String orderInfo) {
//        final String orderInfo = "alipay_sdk=alipay-sdk-php-20161101&app_id=2017051507246370&biz_content=%7B%22subject%22%3A%22App%5Cu652f%5Cu4ed8%22%2C%22out_trade_no%22%3A%22aborad149457002023374340825%22%2C%22timeout_express%22%3A%2260m%22%2C%22total_amount%22%3A0.01%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F192.168.1.102%2Fsellaborad%2Fapi%2Forder%2FZfbNotify.php&sign_type=RSA&timestamp=2017-05-24+18%3A36%3A45&version=1.0&sign=C8RpraWgFIqEybuYoTWcJQKk%2BiZxwJ%2BtAdb3kJM5EGhOCE1LjFVqWPXNJ0N8GgwpVPMarbskE%2BcOwzkWt%2FGEHZui8G84Vd5z6bsP0yX3B3fR6HNC%2BhfrOqUejRQ8xRTxU%2FPEAFVJt6PCZwlEnkCHswELmMzQ2EOhMTp9e9d9j5c%3D";

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(orderInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    // 请求绑定收获地址
    private void reqAlipay(String name, String tel, String address) {

//        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" +
//                orderSureEntity.getOrderNum() + "*" + name + "*" + tel +
//                "*" + address);
        // 8段 * userid * order_id * 订单备注 * 地址id
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" +
                orderId);
        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ORDER_BEFORE_ALIPAY;
//                "order/ToPay.php";
        LG.e("确认订单", "url="+url);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();

                LG.e("确认订单", "" + response.getData());
                if (response != null) {
                    String orderInfo = response.getData();
                    if (orderInfo != null) {
                        callAlipay(orderInfo);
                    } else {
                        ToastUtils.show(mActivity, "获取订单信息失败");
                    }
                } else {
                    ToastUtils.show(mActivity, "获取订单信息失败");
                }
//                pay();
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "服务器请求失败，请检查您的网络");
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 12，13，34，35，37，41，61，70
                switch (errorcode) {
                    case 12: // 用户未登录
                        ToastUtils.show(mActivity, "帐号异常,请重新登录");
//                        Utils.intent2Class(mActivity, Login.class);
//                        appManager.finishAllActivity();
                        break;
                    case 13: // 手机号格式不符合要求
                        ToastUtils.show(mActivity, "收货人手机号填写错误");
                        break;
                    case 34: // 订单不存在或已被删除
                    case 35: // 订单ID非法
                        ToastUtils.show(mActivity, "该订单已失效");
                        break;
                    case 70: // 省id不存在
                        ToastUtils.show(mActivity, "该地区内没有服务商");
                        break;
                    case 37: // 详细地址不符合要求
                        ToastUtils.show(mActivity, "收货人详细地址填写错误");
                        break;
                    case 41: // 由于商品价格发生变更，该订单已失效
                        ToastUtils.show(mActivity, "订单中有商品价格发生变更，请重新选择");
                        break;
                    case 61: // 姓名不合理
                        ToastUtils.show(mActivity, "收货人姓名填写错误");
                        break;
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        break;

                }

            }
        });
    }


    //重置支付方式布局
    private void reSetPayMethod() {
        isWeichat = false;
        isAlipay = false;
        isBank = false;
        isYue = false;
//        ivBank.setImageResource(R.mipmap.unchose_order);
//        ivAlipay.setImageResource(R.mipmap.unchose_order);
//        ivWeichat.setImageResource(R.mipmap.unchose_order);
    }


//    /**
//     * call alipay sdk pay. 调用SDK支付
//     */
//    public void pay() {
//
//        //商品名称
//        String subject = orderSureEntity.getProductItems().get(0).getName();
//        //商品金额
//        String price = orderSureEntity.getPrice();
//        //商品详情
//        String body = orderSureEntity.getProductItems().get(0).getInfo();
//        /**
//         * @key
//         * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
//         */
//        String orderInfo = Pay_Utils.getOrderInfo(subject, body, price, orderSureEntity.getId());
//        // 对订单做RSA 签名
//        String sign = Pay_Utils.sign(orderInfo);
//        try {
//            // 仅需对sign 做URL编码
//            sign = URLEncoder.encode(sign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        catch (Exception e) {
//            LG.e("确认订单", "商户私钥格式不对");
//            e.printStackTrace();
//            ToastUtils.show(mActivity, "商户私钥格式不对");
//            return;
//        }
//        // 完整的符合支付宝参数规范的订单信息
//        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" +
//                Pay_Utils.getSignType();
//
//        Runnable payRunnable = new Runnable() {
//            @Override public void run() {
//                // 构造PayTask 对象
//                PayTask alipay = new PayTask(mActivity);
//                // 调用支付接口，获取支付结果
//                String result = alipay.pay(payInfo);
//
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//        // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }


    //请求订单信息 微信
    private void reqWXOrderInfo() {
        Map<String, String> params = utils.getParams(
                utils.getBasePostStr() + "*" + Constant.userid + "*" + orderId + "*" + utils.convertChinese(name) + "*" + tel + "*" + utils.convertChinese(address));

        final String url = getResources().getString(R.string.api_baseurl) + "order/WxPay.php";
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();

                //保存用户信息,并跳转至主页面
                wxapi = WXAPIFactory.createWXAPI(mActivity, null);
                wxapi.registerApp(wxAppid);
                try {
                    cleanWait();
                    JSONObject jsonObject = new JSONObject(response.toString());
                    wx_prepayid = jsonObject.getString("pid");
                    orderid = jsonObject.getString("orderid");
                    wx_prepay_nonestr = jsonObject.getString("nonce_str");
                    WXPayEntryActivity.orderid = orderid;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                reqWxPay();
            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "服务器请求失败，请检查您的网络");
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 14 || errorcode == 16) {
                    ToastUtils.show(mActivity, "帐号异常,请重新登录");
//                    Utils.intent2Class(mActivity, Login.class);
//                    appManager.finishAllActivity();
                } else if (errorcode == 17) {
                    ToastUtils.show(mActivity, "订单中有活动已结束商品，请重新选择");
                } else if (errorcode == 18) {
                    ToastUtils.show(mActivity, "订单中有活动已售尽商品，请重新选择");
                } else if (errorcode == 19) {
                    ToastUtils.show(mActivity, "订单中有活动已超出个人限购商品，请重新选择");
                } else if (errorcode == 20) {
                    ToastUtils.show(mActivity, "订单中有商品价格发生变更，请重新选择");
                } else if (errorcode == 50) {
                    ToastUtils.show(mActivity, "该订单已失效");
                } else if (errorcode == 105) {
                    ToastUtils.show(mActivity, "该地区内没有服务商");
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }
        });
    }


    /**
     * 调起微信客户端支付
     */
    private void reqWxPay() {
        wx_payReq = new PayReq();

        wx_payReq.appId = getString(R.string.wxappid);
        wx_payReq.partnerId = getString(R.string.wxmchid);
        wx_payReq.prepayId = wx_prepayid;
        wx_payReq.packageValue = "Sign=WXPay";
        wx_payReq.nonceStr = wx_prepay_nonestr;
        wx_payReq.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        wx_payReq.sign = getAppSign();
        wxapi.sendReq(wx_payReq);
    }


    private String getAppSign() {
        StringBuilder sb = new StringBuilder();
        sb.append("appid=").append(wx_payReq.appId);
        sb.append("&noncestr=").append(wx_payReq.nonceStr);
        sb.append("&package=").append(wx_payReq.packageValue);
        sb.append("&partnerid=").append(wx_payReq.partnerId);
        sb.append("&prepayid=").append(wx_payReq.prepayId);
        sb.append("&timestamp=").append(wx_payReq.timeStamp);
        sb.append("&key=").append(getString(R.string.wxkey));

        return MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
//                utils.CommitPageFlagToShared(mActivity, 4);
                finish();
                break;
//            case R.id.payment:
//                showPaymentDialog();
//                break;

            case R.id.pay_btn:
//                if (addressId == null || addressId.equals("")) {
//                    showDialogBindAddress();
//                    return;
//                }
                if("暂未设置".equals(orderSureEntity.getAddress()) || "".equals(orderSureEntity.getAddress())) {
                    showDialogBindAddress();
                    return;
                }
                showPaymentDialog();
                break;
            case R.id.rl_address:
                Intent intent = new Intent(mActivity, AddressListActivity.class);
                intent.putExtra("jumpWhere", 1);
                startActivityForResult(intent, 1);
                break;
            case R.id.pay_cancle:
//                if (isOnline()) {
//                    showWait();
//                    reqOrderSure();
//                }
                showEnsureGone();
                break;
        }
    }

    /**
     * 提交订单
     */
    private void ensureOrder() {
        //请求请求绑定收获地址
        name = companyName.getText().toString();
        tel = companyPhone.getText().toString();
        address = companyAddress.getText().toString();
        if (checkInfo(name, tel, address)) {
            showDialogBindAddress();
        } else {
            if (isAlipay) {
                if (isOnline()) {
                    reqAlipay(utils.convertChinese(name), tel, utils.convertChinese(address));
                }
            } else if (isWeichat) {
                reqWXOrderInfo();
            } else if(isYue) {
                reqYuePay();
            }
            else {
                ToastUtils.show(mActivity, "请选择支付方式！");
            }
        }
    }

    /**
     * 显示选择支付方式弹框
     */
    private void showPaymentDialog() {
        // 初始化Dialog布局
        ChoosePaymentView choosePaymentView = new ChoosePaymentView(mActivity);
        choosePaymentView.setOnChooseListener(new ChoosePaymentView.OnChooseListener() {
            @Override
            public void choose(int payment) {
                switch (payment) {
                    case ChoosePaymentView.FLAG_ALIPAY:
                        reSetPayMethod();
                        isAlipay = true;
                        ensureOrder();
                        break;
                    case ChoosePaymentView.FLAG_YUE:
                        reSetPayMethod();
                        isYue = true;
                        ensureOrder();
                        break;
                    case ChoosePaymentView.FLAG_WECHAT:
                        reSetPayMethod();
                        ensureOrder();
                        break;
                }
            }
        });
        // 填充布局，显示Dialog
        DialogUtil.showDialog(mActivity, choosePaymentView).show();
    }

    /**
     * 取消订单提示
     */
    private void showEnsureGone() {
        TipView tipView = new TipView(mActivity);
        tipView.setMessage("是否要取消订单？").setOnClickListenerEnsure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    showWait();
                    //reqOrderCancle();
                }
            }
        });
        DialogUtil.showSelfDialog(mActivity, tipView);
    }

    /**
     * @funcation 取消订单接口
     **/
    public void reqOrderCancle() {
        String url = getResources().getString(R.string.api_baseurl) ;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + orderId);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                LG.e("555  ", response.getData());
                ToastUtils.show(mActivity, "取消订单成功");
                finish();
//                Data = response.getObj(OrdeDetails_Entity.class);
//                binData(Data);
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 9,35(订单非法),34(订单不存在),61(订单状态不能取消),12
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
//                        Utils.intent2Class(mActivity, Login.class);
                        break;
                    case 34:
                    case 35: // 订单ID非法
                        ToastUtils.show(mActivity, "订单已失效");
                        break;
                    case 61:
                    case 67: // 已支付的订单不能取消
                        ToastUtils.show(mActivity, "已支付商品，不能取消订单");
                        break;
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        break;
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
            }
        });
    }

    //验证收货地址是否为空
    private boolean checkInfo(String name, String tel, String address) {
        if (TextUtils.isEmpty(name)) {
            ToastUtils.show(mActivity, "姓名为空");
            return true;
        }
        if (TextUtils.isEmpty(tel)) {
            ToastUtils.show(mActivity, "手机号为空");
            return true;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.show(mActivity, "地址为空");
            return true;
        }
        return false;
    }


    //弹出对话框询问用户是否去绑定地址
    private void showDialogBindAddress() {
        new AlertDialog.Builder(mActivity).setMessage("您还未绑定地址,是否跳转至收货地址页面进行绑定地址?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(mActivity, AddressListActivity.class);
                intent.putExtra("jumpWhere", 1);
                startActivityForResult(intent, 1);
            }
        }).setNegativeButton("取消", null).create().show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        utils.CommitPageFlagToShared(mActivity, 4);
    }

    /**
     * 请求收货地址列表接口
     */
    private void reqAddressList() {

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ADDRESS_LIST;
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
//                cleanWait();
                if (response != null) {
                    int errorcode = response.getErrorcode();

//                    List<AddressItem> addresses = response.getList(AddressItem.class);
//                    for (AddressItem item : addresses) {
//                        if (item.getState() == 1) {
//                            addressId = item.getId();
//                            LG.e("确认订单", "addressId=" + addressId);
//                            break;
//                        }
//                    }
                }
            }


            @Override
            public void onRequestError(int code, String msg) {
//                cleanWait();
                ToastUtils.show(mActivity, msg);
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
//                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 12 || errorcode == 18) {
//                    ToastUtils.show(mActivity,"账号异常，请重新登录");
                    LG.e("请求地址列表", "账号异常，请重新登录" + errorcode);
//                    utils.jumpTo(mActivity, LoginActivity.class);
                } else if (errorcode == 15) {
//                    ToastUtils.show(mActivity,"该地址不存在");
                    LG.e("请求地址列表", "该地址不存在" + errorcode);
//                    utils.jumpTo(mActivity, LoginActivity.class);
                } else if (errorcode == 11) {
//                    ToastUtils.show(mActivity,"账号异常");
                    LG.e("请求地址列表", "账号异常" + errorcode);
                } else {
//                    ToastUtils.show(mActivity,"服务器繁忙");
                    LG.e("请求地址列表", "服务器繁忙" + errorcode);
                }
            }
        });
    }

    /**
     * 余额支付接口
     */
    private void reqYuePay() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid+"*"+orderId);

        final String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ORDER_YUE_PAY;
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if (response != null) {
                    ToastUtils.show(mActivity, "支付成功");
                    finish();
//                    List<AddressItem> addresses = response.getList(AddressItem.class);
//                    for (AddressItem item : addresses) {
//                        if (item.getState() == 1) {
//                            addressId = item.getId();
//                            LG.e("确认订单", "addressId=" + addressId);
//                            break;
//                        }
//                    }
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

                //11，12,29，30，37，38，39，40
                switch (errorcode) {
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        LG.e("请求地址列表", "账号异常，请重新登录" + errorcode);
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 29:
                    case 30:
                        ToastUtils.show(mActivity, "订单异常");
                        break;
                    case 37:
                        ToastUtils.show(mActivity, "请先选择地址");
                        break;
                    case 39:
                        ToastUtils.show(mActivity, "余额支付失败，请重试");
                        break;
                    case 38:
                    case 40:
                        ToastUtils.show(mActivity, "余额不足");
                        break;
                    default:
                        ToastUtils.show(mActivity, "支付失败，请稍后再试！");
                        break;
                }
            }
        });
    }
}
