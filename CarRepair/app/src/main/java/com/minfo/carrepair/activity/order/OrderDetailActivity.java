package com.minfo.carrepair.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.dialog.SendGoodView;
import com.minfo.carrepair.dialog.TipView;
import com.minfo.carrepair.entity.order.OrderDetailEntity;
import com.minfo.carrepair.entity.order.WLEntityList;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.MyCheck;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import minfo.com.albumlibrary.utils.LG;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView ivBack;
    //    private TextView lookWuliu;
    private TextView name;
    private TextView phone;
    private TextView address;
    private TextView code, ordeDetails_wl_name;
    private TextView time;
    private ListView lvDetail;
    private TextView tvLeft; // 左边按钮
    private TextView tvRight; // 右边按钮
    private TextView tvMoney; // 总钱数
    private RelativeLayout rlBottom;
    private CommonAdapter<OrderDetailEntity.GoodInfo> commonAdapter;

    private OrderDetailEntity ordeDetails;
    private List<OrderDetailEntity.GoodInfo> productItems = new ArrayList<>();

    private TextView ordeDetails_wl, orde_end_time, paytime, sendtime;
    private int type;
    private String orderId;
    private String price;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_order_detail);
//        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void findViews() {
        orderId = getIntent().getStringExtra("orderid");

        type = getIntent().getIntExtra("type", 0);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("订单详情");
        ivBack.setVisibility(View.VISIBLE);
//        lookWuliu = (TextView) findViewById(R.id.check_logistics_btn);
        lvDetail = (ListView) findViewById(R.id.lv_orderdetails);
//        tv_sure = (TextView) findViewById(R.id.tv_sure);
        rlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right_ensure);
        tvMoney = (TextView) findViewById(R.id.pay_amount);

        View viewTop = LayoutInflater.from(mActivity).inflate(R.layout.content_order_detail, null);
        ordeDetails_wl = (TextView) viewTop.findViewById(R.id.ordeDetails_wl);
        orde_end_time = (TextView) viewTop.findViewById(R.id.orde_end_time);
        paytime = (TextView) viewTop.findViewById(R.id.pay_time);
        sendtime = (TextView) viewTop.findViewById(R.id.send_time);
        name = (TextView) viewTop.findViewById(R.id.company_name);
        phone = (TextView) viewTop.findViewById(R.id.company_phone);
        address = (TextView) viewTop.findViewById(R.id.company_address);
        code = (TextView) viewTop.findViewById(R.id.ordeDetails_code);
        time = (TextView) viewTop.findViewById(R.id.ordeDetails_time);
        ordeDetails_wl = (TextView) viewTop.findViewById(R.id.ordeDetails_wl);
        ordeDetails_wl_name = (TextView) viewTop.findViewById(R.id.ordeDetails_wl_name);

        lvDetail.addHeaderView(viewTop);
    }

    @Override
    public void initViews() {

        ivBack.setOnClickListener(this);
//        lookWuliu.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
//        tv_sure.setOnClickListener(this);
//        if(ordeDetails != null) {
//            code.setText(orderId);
//        }

        switch (type) {
            case 2: // 待收货
//                tv_sure.setVisibility(View.VISIBLE);
                paytime.setVisibility(View.VISIBLE);
                orde_end_time.setVisibility(View.GONE);
                sendtime.setVisibility(View.VISIBLE);
                ordeDetails_wl.setVisibility(View.VISIBLE);
                ordeDetails_wl_name.setVisibility(View.GONE);

                tvLeft.setVisibility(View.VISIBLE);
                tvRight.setVisibility(View.GONE);
                tvLeft.setText("查看物流");
                tvRight.setText("确认收货");
                break;
            case 1: // 待发货
                paytime.setVisibility(View.VISIBLE);
                sendtime.setVisibility(View.GONE);
                ordeDetails_wl.setVisibility(View.GONE);
                orde_end_time.setVisibility(View.GONE);
                ordeDetails_wl_name.setVisibility(View.GONE);
                tvLeft.setVisibility(View.GONE);
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setText("发货");
                break;
            case 3: // 待评价
                paytime.setVisibility(View.VISIBLE);
                orde_end_time.setVisibility(View.VISIBLE);
                sendtime.setVisibility(View.VISIBLE);
                ordeDetails_wl.setVisibility(View.VISIBLE);
                tvLeft.setVisibility(View.GONE);
                tvRight.setVisibility(View.GONE);
                ordeDetails_wl_name.setVisibility(View.VISIBLE);
                tvRight.setText("去评价");
                break;
            case 4: // 已完成
//                tv_sure.setVisibility(View.GONE);
                paytime.setVisibility(View.VISIBLE);
                orde_end_time.setVisibility(View.VISIBLE);
                sendtime.setVisibility(View.VISIBLE);
                ordeDetails_wl.setVisibility(View.VISIBLE);
                tvLeft.setVisibility(View.GONE);
                tvRight.setVisibility(View.GONE);
                ordeDetails_wl_name.setVisibility(View.VISIBLE);
                break;
            case 0:
                paytime.setVisibility(View.VISIBLE);
                sendtime.setVisibility(View.GONE);
                ordeDetails_wl.setVisibility(View.GONE);
                orde_end_time.setVisibility(View.GONE);
                ordeDetails_wl_name.setVisibility(View.GONE);
                tvLeft.setVisibility(View.GONE);
                tvRight.setVisibility(View.GONE);
                break;


        }
        commonAdapter = new CommonAdapter<OrderDetailEntity.GoodInfo>(mActivity, productItems, R.layout.item_order_product) {
            @Override
            public void convert(BaseViewHolder helper, OrderDetailEntity.GoodInfo item, int position) {

                ImageView iv = helper.getView(R.id.iv_product);
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_guige, item.getBname() + "     " + item.getSname() + "   " + item.getCname());
                helper.setText(R.id.tv_price, "￥"+ MyCheck.priceFormatChange(item.getMoney()));
                helper.setText(R.id.tv_num, "x"+item.getAmount() + "");

                UniversalImageUtils.displayImageUseDefOptions(item.getImg(), iv);
            }
        };
        lvDetail.setAdapter(commonAdapter);
//        getData();
        if(utils.isOnLine()) {
            reqOrdeDetails();
        }
        else {
            ToastUtils.show(mActivity, "请检查您的网络");
        }
    }

    public void getData() {

        for (int i = 0; i < 5; i++) {
            OrderDetailEntity.GoodInfo entity = new OrderDetailEntity.GoodInfo();
            productItems.add(entity);
        }
        commonAdapter.notifyDataSetChanged();
//        if (isOnline()) {
//            showWait();
//            reqOrdeDetails();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_left:
//                utils.jumpTo(mActivity, Check_logistics.class);

                switch (type) {
                    case 2: // 待收货
//                tv_sure.setVisibility(View.VISIBLE);
//                        showEnsureGone();
                        Intent intent = new Intent(OrderDetailActivity.this, WLInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("wlinfo", ordeDetails);
                        intent.putExtra("info", bundle);
                        startActivity(intent);
                        break;
//                    case 1: // 待发货
//                        showEnsureCancle();
//                        break;
//                    case 3: // 待评价
//                        ToastUtils.show(mActivity, "去评价");
//                        break;
//                    case 4: // 已完成
//                        break;
                }
                break;
            case R.id.tv_right_ensure://确认收货,取消订单
//                if(type == 1) {
//                    showEnsureCancle();
//                }
//                else {
//                    showEnsureGone();
//                }
                switch (type) {
                    case 2: // 待收货
//                tv_sure.setVisibility(View.VISIBLE);
                        showEnsureGone();
                        break;
                    case 1: // 待发货
//                        showEnsureCancle();
                        if (isOnline()) {
                            showWait();
                            reqWL();
                        }

                        break;
                    case 3: // 待评价
//                        ToastUtils.show(mActivity, "去评价");
                        break;
                    case 4: // 已完成
                        break;
                }
                break;
        }
    }

    /**
     * 确认收货提示
     */
    private void showEnsureGone() {
        TipView tipView = new TipView(mActivity);
        tipView.setMessage("是否确定收货？").setOnClickListenerEnsure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    showWait();
                    //reqOrderSure();
                }
            }
        });
        DialogUtil.showSelfDialog(mActivity, tipView);
    }

    /**
     * 发货提示弹框
     */
    private void showSendGood() {
//        TipView tipView = new TipView(mActivity);
//        tipView.setMessage("是否要取消订单"+orderId+"?").setOnClickListenerEnsure(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                reqOrderCancle();
//
//            }
//        });
//        DialogUtil.showSelfDialog(mActivity, tipView);
//        order/GoGoods.php
        SendGoodView sendGoodView = new SendGoodView(mActivity, wLEntityList.getList());
        //sendGoodView.setData(wLEntityList.getList());
        sendGoodView.setOnClickEnsureListener(new SendGoodView.OnClickEnsureListener() {
            @Override
            public void ensure(String wlName, String wlCode) {
                if(isOnline()) {
                    reqOrderSend(wlName, wlCode);
                }
                else {
                    ToastUtils.show(mActivity, R.string.no_internet);
                }
            }
        });
        DialogUtil.showSelfDialog(mActivity, sendGoodView);
    }


    //物流接口
    public static final String EXPRESS_API_HOST = "http://www.kuaidi100.com/query?type=";
//    String url = "http://www.kuaidiapi.cn/rest/?uid=53630&key=9a905eedbdb348d5966c9c4eac7cdea9&order=530433075434&id=zhongtong"; 网站-爱快递(http://www.kuaidiapi.cn/)

    //H5页面:http://m.kuaidi100.com/index_all.html?type=zhongtong&postid=530433075434
    public void Serach(final String comId, final String expressNo, final Handler hd) {

        StringBuffer sb = new StringBuffer();
        sb.append(EXPRESS_API_HOST).append(comId).append("&postid=").append(expressNo);

        String url = sb.toString();

        httpClient.get(url, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = response.getData();
                hd.sendMessage(msg);
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    /**
     * @funcation 请求订单详情接口
     **/
    public void reqOrdeDetails() {
        String url = getResources().getString(R.string.api_baseurl) + "order/Detail.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + orderId);
        LG.e(TAG, "orderId=" + orderId);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if (response != null) {
                    LG.e(TAG + "555  ", response.getData());
                    ordeDetails = response.getObj(OrderDetailEntity.class);
                    if (ordeDetails != null) {
                        binData(ordeDetails);
                    }
                }


            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 11，12,29,30
                if (errorcode == 11 || errorcode == 12) {
                    ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity, LoginActivity.class);
                } else if (errorcode == 29 || errorcode == 30) {
                    ToastUtils.show(mActivity, "该订单不存在或已取消");
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    /**
     * @funcation 发货接口
     * 8段 * sellerid(卖家的用户id) * orderid * 快递单号 * 物流名称
     **/
    public void reqOrderSend(String wlName, String wlCode) {

        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ORDER_SEND;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + orderId + "*" + wlCode + "*" + wlName);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                LG.e("555  ", response.getData());
                ToastUtils.show(mActivity, "发货成功");
                Intent intentMessage = new Intent();
                intentMessage.putExtra(Constant.NOTIFY_STATE, Constant.NOTIFY_TYPE2);
                intentMessage.setAction(Constant.NOTIFY_NAME);
                sendBroadcast(intentMessage);
                finish();
//                Data = response.getObj(OrdeDetails_Entity.class);
//                binData(Data);
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 11，12,29，30，50，51
                switch (errorcode) {
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 29:
                    case 30: // 订单ID非法
                        ToastUtils.show(mActivity, "订单已失效");
                        break;
                    case 50:
                        ToastUtils.show(mActivity, "快递单号不能为空");
                        break;
                    case 51: //
                        ToastUtils.show(mActivity, "请选择物流");
                        break;
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        break;
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    private WLEntityList wLEntityList = new WLEntityList();

    /**
     * @funcation 请求物流公司数据接口
     **/
    public void reqWL() {

        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ORDER_WL;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if (response != null) {
                    LG.e("555  ", response.getData());
                    wLEntityList = response.getObj(WLEntityList.class);
                    if (wLEntityList != null) {
                        if (wLEntityList.getList() != null && wLEntityList.getList().size() > 0)
                            showSendGood();

                    }

                }
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
                ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    private void binData(OrderDetailEntity data) {
        if(type == 4){
            if(MyCheck.isEmpty(data.getFhtime()) || " ".equals(data.getFhtime())) {
                sendtime.setVisibility(View.GONE);
                ordeDetails_wl.setVisibility(View.GONE);
                ordeDetails_wl_name.setVisibility(View.GONE);
                orde_end_time.setVisibility(View.GONE);
            }
        }
        name.setText(data.getSname());
        phone.setText(data.getStel());
        address.setText(data.getAddress());
        code.setText("订单编号：" + orderId);
        ordeDetails_wl.setText("物流单号：" + data.getKuaidih());
        time.setText("下单时间：" + data.getAddtime());
        paytime.setText("支付时间：" + data.getPaytime());
        sendtime.setText("发货时间：" + data.getFhtime());
        orde_end_time.setText("收货时间：" + data.getRetime());
        ordeDetails_wl_name.setText("物流名称：" + data.getWlname());

//        tvMoney.setText(new DecimalFormat("#0.00").format(Double.parseDouble(data.getTotalMoney())));
        //        orde_end_time.setText("确认收货时间：" +data.getDefaulttime()+"天后自动确认收货");

//        odAdapter = new Orde_Details_ListAdapter(this, data);
//        lvDetail.setAdapter(odAdapter);
        if (data.getGoods() != null && data.getGoods().size() > 0) {
//                    msladapter = new Make_Sure_ListAdapter(mActivity, orderDetailEntity.getGoods());
//                    makesureorde_lv.setAdapter(msladapter);
            productItems.addAll(data.getGoods());
            price = new DecimalFormat("#0.00").format(Double.parseDouble(data.getGoods().get(0).getMoney()) * data.getGoods().get(0).getAmount());

        }
        tvMoney.setText("￥" + price);
        commonAdapter.notifyDataSetChanged();
    }
}
