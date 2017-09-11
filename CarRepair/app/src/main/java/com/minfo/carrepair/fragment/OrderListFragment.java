package com.minfo.carrepair.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.activity.order.OrderDetailActivity;
import com.minfo.carrepair.activity.order.OrderDetailQGActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.dialog.TipView;
import com.minfo.carrepair.entity.order.OrderListEntity;
import com.minfo.carrepair.entity.order.OrderListModel;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.MyCheck;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.TypeChangeUtil;
import com.minfo.carrepair.utils.UniversalImageUtils;
import com.minfo.carrepair.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by MinFo021 on 17/5/31.
 * 订单列表界面 (0待付款，1待发货，2待处理(收货)，3已完成,4待评价)
 */

public class OrderListFragment extends BaseFragment implements View.OnClickListener {
    private ListView lvOrder;

    private PullToRefreshView ptrOrder;
    private CommonAdapter<OrderListEntity> orderAdapter;
    private List<OrderListEntity> orderList = new ArrayList<>();
    private List<OrderListEntity> goodList = new ArrayList<>();
    private List<OrderListEntity> qgList = new ArrayList<>();
    private TextView tvTitle;
    private ImageView ivBack;
    private int page = 1;
    private int type;

    private boolean isrefresh;
    private boolean isload;

    private boolean isFirst = true;
    private boolean isGetData = false;
    private RelativeLayout rlNoMessage;
    private RelativeLayout rlNoMessageClick;
    private LinearLayout llOrderList;
    private boolean ishiden = false;

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LG.e("订单列表", "接受到广播");
            // 0 支付成功 1 取消订单成功，2 发货成功 3 确认收货成功 4评价成功
            if (intent.getAction().equals(Constant.NOTIFY_NAME)) {
//                if(MyApplication.id != -1)
//                    reqNewsDetail(MyApplication.id);
//                MyApplication.id = -1;
                int state = intent.getIntExtra(Constant.NOTIFY_STATE, -1);

                switch (state) {
                    case 0:
                        if (type == 0) {
                            reReqData();
                        }
                        break;
                    case 1:
                        if (type == 0 || type == 1) {
                            reReqData();
                        }
                        break;
                    case 2:
                        if (type == 2) {
                            reReqData();
                        }
                        break;
                    case 3:
                        if (type == 3) {
                            reReqData();
                        }
                        break;
                    case 4:
                        if (type == 4) {
                            reReqData();
                        }
                        break;

                }
            }

        }
    };

    private void reReqData() {
        if (isOnline()) {
            if(!isrefresh) {
                page = 1;
                isrefresh = true;
                reqData();
            }
        }
        else {
            if (!ishiden) ToastUtils.show(mActivity, R.string.no_internet);
        }
    }

    @Override
    protected View initViews() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.NOTIFY_NAME);
        mActivity.registerReceiver(myReceiver, intentFilter);
        Bundle bundle = getArguments();
        type = bundle.getInt(Constant.ORDER_STATE, 0);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_order_list, null);
        lvOrder = (ListView) view.findViewById(R.id.lv_order);
        ptrOrder = (PullToRefreshView) view.findViewById(R.id.ptr_order);
        rlNoMessage = (RelativeLayout) view.findViewById(R.id.rl_no_message);
        rlNoMessageClick = (RelativeLayout) view.findViewById(R.id.rl_no_message_click);
        llOrderList = (LinearLayout) view.findViewById(R.id.ll_order_list);

//        tvTitle = (TextView) view.findViewById(R.id.tv_title);
//        ivBack = (ImageView) view.findViewById(R.id.iv_back);
//
////        tvTitle.setVisibility(View.VISIBLE);
////        ivBack.setVisibility(View.VISIBLE);
//        tvTitle.setText("我的订单");

//        ivBack.setOnClickListener(this);
        rlNoMessageClick.setOnClickListener(this);
        ptrOrder.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {

                if (isOnline() && !isload) {
                    isload = true;
                    page++;
                    reqData();
                }
            }
        });
        ptrOrder.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {

                if (isOnline() && !isrefresh) {
                    isrefresh = true;
                    page = 1;
                    reqData();
                }
            }
        });
        ptrOrder.setLastUpdated(new Date().toLocaleString());
        ptrOrder.setHorizontalScrollBarEnabled(true);

        initAdapter();

        lvOrder.setAdapter(orderAdapter);


        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int biaoshi = orderList.get(position).getBiaoshi();
                if (biaoshi == 1) {
                    Intent intent = new Intent(mActivity, OrderDetailQGActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("orderDetails", orderList.get(position));
//                    intent.putExtra("info", bundle);
                    intent.putExtra("orderid", orderList.get(position).getQgOrderid());
                    intent.putExtra("type", type);
                    startActivityForResult(intent, 0);
                } else {
                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("orderDetails", orderList.get(position));
//                    intent.putExtra("info", bundle);
                    boolean isQiugouo = biaoshi == 1 ? true : false;
                    intent.putExtra("orderid", orderList.get(position).getOrderNum());
                    intent.putExtra("isqiugou", isQiugouo);
                    intent.putExtra("type", type);
                    startActivityForResult(intent, 0);
                }
//                if (type == 0) {
//                    Intent intent = new Intent(mActivity, OrderSureActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("orderDetails", orderList.get(position));
//                    intent.putExtra("info", bundle);
//                    intent.putExtra("orderid", orderList.get(position).getOrderNum());
//                    startActivityForResult(intent, 0);
//                }
//                else {
//                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);
////                    Bundle bundle = new Bundle();
////                    bundle.putSerializable("orderDetails", orderList.get(position));
////                    intent.putExtra("info", bundle);
//                    intent.putExtra("orderid", orderList.get(position).getOrderNum());
//                    intent.putExtra("type", type);
//
//                    startActivityForResult(intent, 0);
//                }

            }
        });

        if (isOnline()) {
            reqData();
        } else {
            if (!ishiden) ToastUtils.show(mActivity, "网络异常");
        }

        return view;
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        orderAdapter = new CommonAdapter<OrderListEntity>(mActivity, orderList, R.layout.content_order_list) {
            @Override
            public void convert(BaseViewHolder helper, OrderListEntity item, final int position) {

//                LinearLayout llItem = helper.getView(R.id.ll_order_list_item);
//                TextView tvDelete = helper.getView(R.id.tv_delete);
//                TextView tvPay = helper.getView(R.id.tv_pay);
//                String time = item.getTime();
//                if(time != null && time.length() > 10) {
//                    time = time.substring(0, 10);
//                }
//                helper.setText(R.id.tv_ordertime, time);
//            helper.setText(R.id.tv_ordertime, item.getCreate_time());
//                helper.setText(R.id.tv_ordernum, "订单号: " + item.getOrderNum());
//                helper.setText(R.id.tv_pro_count, "共" + item.getAccount() + "件商品，");
//                helper.setText(R.id.tv_pro_count_money, "共计" + item.getPrice() + "元");
//                LinearLayout llContiner = ((LinearLayout) helper.getView(R.id.ll_continer));
//
//                for (int i = 0; i < item.getProductItems().size(); i++) {
//                    View view = LayoutInflater.from(mActivity).inflate(R.layout.item_order_list, null);
//                    ImageView img = (ImageView) view.findViewById(R.id.iv_product);
//                    TextView tvName = (TextView) view.findViewById(R.id.tv_name);
//                    TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
//                    TextView tvAmount = (TextView) view.findViewById(R.id.tv_proamount);
//                    TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
//                    //UniversalImageUtils.displayImageUseDefOptions(item.getProductItems().get(i).getSimg(), img);
//                    tvName.setText(item.getProductItems().get(i).getName());
//                    tvTime.setText(item.getTime());
////                    tvAmount.setText("x" + item.getProductItems().get(i).);
//                    tvPrice.setText("¥ " + item.getProductItems().get(i).getPrice());
//                    llContiner.addView(view);
//
//                }
                ImageView img = helper.getView(R.id.iv_product);
                TextView tvPay = helper.getView(R.id.tv_pay);
                int biaoshi = 0;
                if (item.getQgOrderid() != null && !"".equals(item.getQgOrderid())) {
                    biaoshi = 1;
                }

                switch (biaoshi) {
                    case 0:
                        helper.setText(R.id.tv_ordernum, "订单号: " + item.getOrderNum());
                        helper.setText(R.id.tv_name, item.getName());
                        helper.setText(R.id.tv_time, item.getTime());
                        helper.setText(R.id.tv_proamount, "x" + item.getAccount());
                        helper.setText(R.id.tv_price, "￥" + MyCheck.priceFormatChange(item.getPrice()));
                        UniversalImageUtils.displayImageUseDefOptions(item.getImg(), img);
                        break;
                    case 1:
                        helper.setText(R.id.tv_ordernum, "订单号: " + item.getQgOrderid());
                        helper.setText(R.id.tv_name, item.getQgName());
                        helper.setText(R.id.tv_time, item.getTime());
                        helper.setText(R.id.tv_price, TypeChangeUtil.getTypeFrom(item.getQgType()));
                        helper.setText(R.id.tv_proamount, "共 ￥" + MyCheck.priceFormatChange(item.getTotalMoney()));
                        UniversalImageUtils.displayImageUseDefOptions(item.getQgImg(), img);
                        break;
                }
//                if (type == 0) {
//                    helper.setText(R.id.tv_time, item.getTime());
//
//                } else if (type == 1) {
//                    helper.setText(R.id.tv_time, item.getPay_time());
//
//                } else if (type == 2) {
//                    helper.setText(R.id.tv_time, item.getFhtime());
//
//                } else if (type == 3) {
//                    helper.setText(R.id.tv_time, item.getRetime());
//
//                } else if (type == 4) {
//                    helper.setText(R.id.tv_time, item.getRetime());
//
//                }

//                switch (type) {
//                    case 0: // 待支付
//                        tvPay.setText("立即支付");
//                        break;
//                    case 1: // 待发货
//                        tvPay.setText("取消订单");
//                        break;
//                    case 2: // 待收货
//                        tvPay.setText("确认收货");
//                        break;
//                    case 3: // 待评价
//                        tvPay.setText("去评价");
//                        break;
//                    case 4: // 已完成
//                        tvPay.setText("已完成");
//                        break;
//                }
//
//                tvPay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (type == 0){
//                            Intent intent = new Intent(mActivity, OrderSureActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("orderDetails", orderList.get(position));
//                            intent.putExtra("info", bundle);
//                            intent.putExtra("orderid", orderList.get(position).getOrderNum());
//                            mActivity.startActivity(intent);
//                        } else if(type == 2) {
//                            showEnsureGone(position);
//                        }
//                        else if(type == 1) {
//                            showEnsureCancle(position, orderList.get(position).getOrderNum());
//                        }
//                        else if(type == 3) {
//                            if(!ishiden)ToastUtils.show(mActivity, "去评价");
//                            mActivity.startActivity(new Intent(mActivity, AssessActivity.class));
//                        }
////                        else{
////                            Intent intent = new Intent(mContext, Orde_Details.class);
////                            Bundle bundle = new Bundle();
////
////                            bundle.putSerializable("ordeDetails", Data.get(position));
////                            intent.putExtra("info", bundle);
////                            intent.putExtra("type", type);
////                            mContext.startActivity(intent);
////                        }
//                    }
//                });
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (isOnline()) {
            page = 1;
            isrefresh = true;
            if (!ishiden) showWait();
            reqData();
        } else {
            if (!ishiden) ToastUtils.show(mActivity, "网络异常");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_back:
//                mActivity.finish();
//                break;
            case R.id.rl_no_message_click:
                page = 1;
                isrefresh = true;
                reqData();
                break;
        }
    }

    /**
     * 请求订单列表数据
     * 上行参数: 8段 * userid * 订单状态(0待付款，1待发货，2收获，3待评价，4已完成) * 页码
     */
    private void reqData() {
//        for(int i = 0; i < 5; i ++) {
//            OrderListEntity entity = new OrderListEntity();
//            entity.setName("ddd"+i);
//            entity.setOrderNum("dsafsd334"+i);
//            entity.setState(type);
//            entity.setPrice(1*5+1+"");
//            List<ProductItem> items = new ArrayList<>();
//            for(int j = 0; j < i; j++) {
//                ProductItem item = new ProductItem();
////                item.set(5);
//                item.setPrice(j*2+"");
//                //item.setTime("2017/05/31 "+j);
//                items.add(item);
//            }
//            entity.setProductItems(items);
//            orderList.add(entity);
//        }
//        orderAdapter.notifyDataSetChanged();
//        if(true) {
//            cleanWait();
//            return;
//        }

        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_ORDER_LIST;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + type + "*" + page);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                if (!ishiden) showWait();
                isGetData = false;
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.e("OrderList", response.toString());
                cleanWait();
                isGetData = true;
                orderList.clear();
                if (isrefresh) {

                    goodList.clear();
                    qgList.clear();
                    isrefresh = false;
                    ptrOrder.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                    if (!ishiden) ToastUtils.show(mActivity, "刷新成功");
                }
                if (isload) {
                    isload = false;
                    ptrOrder.onFooterRefreshComplete();
                }
                OrderListModel model = response.getObj(OrderListModel.class);
                if (model != null) {
                    List<OrderListEntity> listgoods = model.getShop();
                    List<OrderListEntity> listqiugou = model.getQiugou();
//                    goodList = model.getShop();
//                    qgList = model.getQiugou();
                    if (listgoods != null) {
                        goodList.addAll(listgoods);
                    }
                    if (listqiugou != null) {
                        qgList.addAll(listqiugou);
                    }

                    orderList.addAll(goodList);
                    orderList.addAll(qgList);

                    if (orderList.size() != 0) {
                        rlNoMessage.setVisibility(View.GONE);
                        llOrderList.setVisibility(View.VISIBLE);
                    } else {
                        rlNoMessage.setVisibility(View.VISIBLE);
                        llOrderList.setVisibility(View.GONE);
                    }
                    ptrOrder.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                    ptrOrder.onFooterRefreshComplete();

                    orderAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                ptrOrder.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrOrder.onFooterRefreshComplete();
                int errorcode = response.getErrorcode();
                // 11，12，27, 28
                switch (errorcode) {
                    case 11:
                    case 12:
                        if (!ishiden) ToastUtils.show(mActivity, "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 8:
                        LG.e("订单列表", "errorcode=" + errorcode);
                        break;
                    case 27:
                    case 28:
                    default:
                        if (!ishiden) ToastUtils.show(mActivity, "服务器繁忙");
                        LG.e("订单列表", "errorcode=" + errorcode);
                        break;
                }

            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ptrOrder.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrOrder.onFooterRefreshComplete();
                if (!ishiden) ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    /**
     * 确认收货提示
     */
    private void showEnsureGone(final int pos) {
        TipView tipView = new TipView(mActivity);
        tipView.setMessage("是否确定收货？").setOnClickListenerEnsure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reqOrderSure(pos);

            }
        });
        DialogUtil.showSelfDialog(mActivity, tipView);
    }

    private void showEnsureCancle(final int pos, String orderId) {
        TipView tipView = new TipView(mActivity);
        tipView.setMessage("是否要取消订单" + orderId + "?").setOnClickListenerEnsure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //reqOrderCancle(pos);

            }
        });
        DialogUtil.showSelfDialog(mActivity, tipView);
    }

    /**
     * @funcation 确认收货接口
     **/
    public void reqOrderSure(final int position) {
        OrderListEntity ordeDetails = orderList.get(position);
        String url = getResources().getString(R.string.api_baseurl) + "order/FinishBtn.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + ordeDetails.getOrderNum() + "*" + type);
        LG.e(TAG, ordeDetails.getOrderNum().toString() + " " + Constant.userid + "*" + ordeDetails.getOrderNum() + "*" + type);
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if (!ishiden) ToastUtils.show(mActivity, "操作成功");
                orderList.remove(position);
                orderAdapter.notifyDataSetChanged();
//                finish();


            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 12 || errorcode == 13) {
                    if (!ishiden) ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity, LoginActivity.class);
                } else if (errorcode == 34 || errorcode == 35) {
                    if (!ishiden) ToastUtils.show(mActivity, "该订单不存在");
                } else {
                    if (!ishiden) ToastUtils.show(mActivity, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                if (!ishiden) ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    /**
     * @funcation 取消订单接口
     **/
    public void reqOrderCancle(final int position) {
        OrderListEntity ordeDetails = orderList.get(position);
        String url = getResources().getString(R.string.api_baseurl);
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + ordeDetails.getOrderNum());
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                LG.e("555  ", response.getData());
                if (!ishiden) ToastUtils.show(mActivity, "取消订单成功");
                orderList.remove(position);
                orderAdapter.notifyDataSetChanged();
//                finish();
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
                        if (!ishiden) ToastUtils.show(mActivity, "账号异常，请重新登录");
//                        Utils.intent2Class(mActivity, Login.class);
                        break;
                    case 34:
                    case 35: // 订单ID非法
                        if (!ishiden) ToastUtils.show(mActivity, "订单已失效");
                        break;
                    case 61:
                    case 67: // 已支付的订单不能取消
                        if (!ishiden) ToastUtils.show(mActivity, "已支付商品，不能取消订单");
                        break;
                    default:
                        if (!ishiden) ToastUtils.show(mActivity, "服务器繁忙");
                        break;
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                if (!ishiden) ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
        }
//        else {
//            page = 1;
//            isrefresh = true;
//            reqData();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(myReceiver);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        ishiden = !isVisibleToUser;
        Log.e("订单列表", "ishiden=" + ishiden);
    }
}
