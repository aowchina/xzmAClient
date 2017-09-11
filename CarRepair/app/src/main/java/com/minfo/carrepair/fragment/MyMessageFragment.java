package com.minfo.carrepair.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.chat.ChatActivity;
import com.minfo.carrepair.chat.HuanUtils;
import com.minfo.carrepair.entity.message.MessageItem;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;
import com.minfo.carrepair.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMessageFragment extends BaseFragment implements View.OnClickListener {


    private ListView lvMessage;

    private PullToRefreshView ptrMessage;
    private CommonAdapter<MessageItem> commonAdapter;
    private List<MessageItem> list = new ArrayList<>();
    private List<MessageItem> tempList = new ArrayList<>();
    private TextView tvTitle;
    private ImageView ivBack;
    private int page = 1;
    private int type;

    private boolean isrefresh;
    private boolean isload;
    @Override
    protected View initViews() {
        Bundle bundle = getArguments();
        type = bundle.getInt(Constant.MESSAGE_TYPE, 0);

        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_my_message, null);

        lvMessage = (ListView) view.findViewById(R.id.lv_message);
        ptrMessage = (PullToRefreshView) view.findViewById(R.id.ptr_message);

//        tvTitle = (TextView) view.findViewById(R.id.tv_title);
//        ivBack = (ImageView) view.findViewById(R.id.iv_back);
//
////        tvTitle.setVisibility(View.VISIBLE);
////        ivBack.setVisibility(View.VISIBLE);
//        tvTitle.setText("我的订单");

//        ivBack.setOnClickListener(this);
        ptrMessage.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {

                if (isOnline() && !isload) {
                    isload = true;
                    page++;
                    reqData();
                }
            }
        });
        ptrMessage.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {

                if (isOnline() && !isrefresh) {
                    isrefresh = true;
                    page = 1;
                    reqData();
                }
            }
        });
        ptrMessage.setLastUpdated(new Date().toLocaleString());
        ptrMessage.setHorizontalScrollBarEnabled(true);

        initAdapter();

        lvMessage.setAdapter(commonAdapter);


        lvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (type == 1) {
//                    Intent intent = new Intent(mActivity, OrderSureActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("ordeDetails", list.get(position));
//                    intent.putExtra("info", bundle);
//                    intent.putExtra("orderid", list.get(position).getOrderNum());
//                    startActivityForResult(intent, 0);
//                } else {
//                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("ordeDetails", list.get(position));
//                    intent.putExtra("info", bundle);
//                    intent.putExtra("type", type);
//
//                    startActivityForResult(intent, 0);
//                }
                Intent intent = new Intent(mActivity, ChatActivity.class);

                // it's single chat
                intent.putExtra("userNickName", list.get(position).getName());//缺昵称
                intent.putExtra("userHeadImage", list.get(position).getPicture());
                intent.putExtra(HuanUtils.EXTRA_USER_ID,"buy"+list.get(position).getAppuid());

                startActivity(intent);
            }
        });

        if (isOnline()) {

            reqData();
        }
        else {
            ToastUtils.show(mActivity, "网络异常");
        }

        return view;
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        commonAdapter = new CommonAdapter<MessageItem>(mActivity, list, R.layout.content_my_message) {
            @Override
            public void convert(BaseViewHolder helper, MessageItem item, final int position) {
                ImageView icon = helper.getView(R.id.iv_icon);
//                helper.setText(R.id.tv_name, item.getName());
//                helper.setText(R.id.tv_xinyu_zhi, item.getPercent() + "%");
//                helper.setText(R.id.tv_xinyu_content, "("+"报价"+item.getBaojia() +"次,"+ "卖"+item.getSellnum()+"单"+")");
//                helper.setText(R.id.tv_zhuying, "$" + item.getInfo());

                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_zhuying, item.getSkill());
                UniversalImageUtils.displayImageUseDefOptions(item.getPicture(), icon);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (isOnline()) {
            if (!isrefresh) {
                page = 1;
                isrefresh = true;
                reqData();
            }
        }
        else {
            ToastUtils.show(mActivity, "网络异常");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_back:
//                mActivity.finish();
//                break;
        }
    }

    /**
     * 请求订单列表数据
     * 上行参数: 8段 * userid * 订单状态(0待付款，1待发货，2收获，3已完成) * 页码
     */
    private void reqData() {
//        for(int i = 0; i < 5; i ++) {
//            MessageItem entity = new MessageItem();
//            entity.setName("ddd"+i);
//            entity.setBaojia(8*100+5);
//            entity.setSellnum(i*20);
//            entity.setInfo("原厂，拆件");
//            entity.setPercent(99+"");
//            list.add(entity);
//        }
//        commonAdapter.notifyDataSetChanged();
//        if(true) {
//            cleanWait();
//            return;
//        }

        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_MY_MESSAGE_HY;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid+ "*" + page);
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                showWait();
            }


            @Override public void onRequestSuccess(BaseResponse response) {
                Log.e("GoodManger", response.toString());
                cleanWait();
                if (isrefresh) {
                    list.clear();
                    isrefresh = false;
                    ptrMessage.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                    ToastUtils.show(mActivity,"刷新成功");
                }
                if (isload) {
                    isload = false;
                    ptrMessage.onFooterRefreshComplete();
                }
                tempList.clear();

                tempList = response.getList(MessageItem.class);
                if(tempList != null) {
                    list.addAll(tempList);
                }
                ptrMessage.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrMessage.onFooterRefreshComplete();
                if (page > 1) {
                    if (tempList!=null&&tempList.size() >= 0) {
                        commonAdapter.notifyDataSetChanged();
                        ToastUtils.show(mActivity, "加载成功");
                    }
                    else {
                        ToastUtils.show(mActivity, "无更多内容");
                    }
                }
                else {
                    commonAdapter.notifyDataSetChanged();
                }
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                isrefresh=false;
                isload=false;
                ptrMessage.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrMessage.onFooterRefreshComplete();
                int errorcode = response.getErrorcode();
                if (errorcode == 12) {
                    ToastUtils.show(mActivity, "账号异常，请重新登录");
                    utils.jumpTo(mActivity, LoginActivity.class);
                }
                else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                isrefresh=false;
                isload=false;
                ptrMessage.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrMessage.onFooterRefreshComplete();
            }
        });
    }
//    /**
//     * 上下架商品提示
//     */
//    private void showEnsureGone(final int pos) {
//        TipView tipView = new TipView(mActivity);
//        tipView.setMessage("是否上架商品？").setOnClickListenerEnsure(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                reqOrderSure(pos);
//
//            }
//        });
//        DialogUtil.showSelfDialog(mActivity, tipView);
//    }
//
//    private void showEnsureCancle(final int pos, String orderId) {
//        TipView tipView = new TipView(mActivity);
//        tipView.setMessage("是否要删除商品"+orderId+"?").setOnClickListenerEnsure(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                reqDelGood(pos);
//
//            }
//        });
//        DialogUtil.showSelfDialog(mActivity, tipView);
//    }
//
//    /**
//     * @funcation上下架商品
//     **/
//    public void reqOrderSure(final int position) {
//        MessageItem ordeDetails = list.get(position);
//        String url = getResources().getString(R.string.api_baseurl) + "order/FinishBtn.php";
//        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + ordeDetails.getGoods_num() + "*" +type);
////        LG.e(TAG, ordeDetails.getOrderNum().toString() + " " + Constant.userid + "*" + ordeDetails.getOrderNum() + "*" + type);
//        httpClient.post(url, params, new RequestListener() {
//            @Override
//            public void onPreRequest() {
//
//            }
//
//            @Override
//            public void onRequestSuccess(BaseResponse response) {
//                cleanWait();
//                ToastUtils.show(mActivity, "操作成功");
//                list.remove(position);
//                commonAdapter.notifyDataSetChanged();
////                finish();
//
//
//            }
//
//            @Override
//            public void onRequestNoData(BaseResponse response) {
//                cleanWait();
//                int errorcode = response.getErrorcode();
//                if (errorcode == 12||errorcode == 13) {
//                    ToastUtils.show(mActivity, "账号异常，请重新登录");
//                    utils.jumpTo(mActivity, LoginActivity.class);
//                }else if(errorcode == 34 || errorcode == 35){
//                    ToastUtils.show(mActivity, "该订单不存在");
//                } else {
//                    ToastUtils.show(mActivity, "服务器繁忙");
//                }
//            }
//
//            @Override
//            public void onRequestError(int code, String msg) {
//                cleanWait();
//            }
//        });
//    }
//
//    /**
//     * @funcation 取消订单接口
//     **/
//    public void reqDelGood(final int position) {
//        MessageItem ordeDetails = list.get(position);
//        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_message_MANAGER_DEL_message;
//        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + ordeDetails.getGoods_num());
//        httpClient.post(url, params, new RequestListener() {
//            @Override
//            public void onPreRequest() {
//
//            }
//
//            @Override
//            public void onRequestSuccess(BaseResponse response) {
//                cleanWait();
//                LG.e("555  ", response.getData());
//                ToastUtils.show(mActivity, "取消订单成功");
//                list.remove(position);
//                commonAdapter.notifyDataSetChanged();
////                finish();
////                Data = response.getObj(OrdeDetails_Entity.class);
////                binData(Data);
//            }
//
//            @Override
//            public void onRequestNoData(BaseResponse response) {
//                cleanWait();
//                int errorcode = response.getErrorcode();
//                // 9,35(订单非法),34(订单不存在),61(订单状态不能取消),12
//                switch (errorcode) {
//                    case 12:
//                        ToastUtils.show(mActivity, "账号异常，请重新登录");
////                        Utils.intent2Class(mActivity, Login.class);
//                        break;
//                    case 34:
//                    case 35: // 订单ID非法
//                        ToastUtils.show(mActivity, "订单已失效");
//                        break;
//                    case 61:
//                    case 67: // 已支付的订单不能取消
//                        ToastUtils.show(mActivity, "已支付商品，不能取消订单");
//                        break;
//                    default:
//                        ToastUtils.show(mActivity, "服务器繁忙");
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onRequestError(int code, String msg) {
//                cleanWait();
//            }
//        });
//    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LG.e("消息", "isVisibleToUser="+isVisibleToUser);
    }
}
