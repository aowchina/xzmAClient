package com.minfo.carrepairseller.activity.wallet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.wallet.WalletRecord;
import com.minfo.carrepairseller.entity.wallet.WalletRecordEntiry;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BillCheckActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageView ivBack;
    private ListView listView;
    private PullToRefreshView ptrLayout;
    private CommonAdapter<WalletRecord> commonAdapter;//产品
    private List<WalletRecord> productList = new ArrayList<>();

    private RelativeLayout rlNoMessage;
    private RelativeLayout rlNoMessageClick;
//    private LinearLayout ptrLayout;
    private int page = 1;

    private boolean isrefresh = false;
    private boolean isload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_bill_check);
    }

    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        listView = ((ListView) findViewById(R.id.lv_bill));
        ptrLayout = ((PullToRefreshView) findViewById(R.id.ptr_layout));

        rlNoMessage = (RelativeLayout) findViewById(R.id.rl_no_message);
        rlNoMessageClick = (RelativeLayout) findViewById(R.id.rl_no_message_click);

    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        rlNoMessageClick.setOnClickListener(this);
        tvTitle.setText("账单");

//        for (int i = 0; i < 3; i++) {
//            WalletRecord record = new WalletRecord();
//            switch (i) {
//                case 0:
//                    //record.setMoney("-325.00");
//                    break;
//                case 1:
//                    break;
//                case 2:
//                    break;
//            }
//            productList.add(new WalletRecord());
//
//
//        }

        commonAdapter = new CommonAdapter<WalletRecord>(this, productList, R.layout.item_bill_check) {
            @Override
            public void convert(BaseViewHolder helper, WalletRecord item, int position) {
//                type:
//                1:退款
//                2：付款
//                3：提现
//                money:金额
//                addtime:时间
                String kind = "名称";
                boolean isAdd = true;
                if("1".equals(item.getType())) {
                    kind = "订单退款";
                    isAdd = true;
                }
                else if("2".equals(item.getType())) {
                    kind = "订单付款";
                    isAdd = false;
                }
                else if("3".equals(item.getType())) {
                    kind = "提现";
                    isAdd = false;
                }

                helper.setText(R.id.tv_kind,kind);
                helper.setText(R.id.tv_money,isAdd ? "+￥" +item.getMoney() : "-￥"+item.getMoney());
                helper.setText(R.id.tv_date,item.getAddtime());

            }
        };
        listView.setAdapter(commonAdapter);

        if (isOnline()) {
            //showWait();
            isrefresh = true;
            page = 1;
            reqData();
        } else {
            ToastUtils.show(this, "暂无网络");
        }
        ptrLayout.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                isrefresh = true;
                page = 1;
                reqData();
            }
        });
        ptrLayout.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                isload = true;
                page ++;
                reqData();
            }
        });
//        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//
////                isRefresh = true;
////                page = 1;
////                if (isOnline()) {
////                    showWait();
////                    reqData();
////                } else {
////                    ToastUtils.show(mActivity, "暂无网络");
////                }
//            }
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return super.checkCanDoRefresh(frame, listView, header);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.rl_no_message_click:
                isrefresh = true;
                page = 1;
                reqData();
                break;

        }
    }

    /**
     * 请求账单数据
     * 上行参数:
     */
    private void reqData() {

        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_USER_ZHANGDAN;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + page);
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                showWait();
             
            }


            @Override public void onRequestSuccess(BaseResponse response) {
                Log.e("账单", response.toString());
                cleanWait();
                if(response != null) {
                    if (isrefresh) {
                        productList.clear();
                        isrefresh = false;
                        ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                        ToastUtils.show(mActivity, "刷新成功");
                    }
                    if (isload) {
                        isload = false;
                        ptrLayout.onFooterRefreshComplete();
                    }
                    WalletRecordEntiry entiry = response.getObj(WalletRecordEntiry.class);
                    if(entiry != null) {
                        List<WalletRecord> tempList = entiry.getList();
                        if (tempList != null) {
                            productList.addAll(tempList);
                        }
                        if (productList.size() != 0) {
                            rlNoMessage.setVisibility(View.GONE);
                            ptrLayout.setVisibility(View.VISIBLE);
                        } else {
                            rlNoMessage.setVisibility(View.VISIBLE);
                            ptrLayout.setVisibility(View.GONE);
                        }
                        ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                        ptrLayout.onFooterRefreshComplete();
                        if (page > 1) {
                            if (tempList.size() != 0) {
                                commonAdapter.notifyDataSetChanged();
                                ToastUtils.show(mActivity, "加载成功");
                            } else {
                                ToastUtils.show(mActivity, "无更多内容");
                            }
                        } else {
                            commonAdapter.notifyDataSetChanged();
                        }
                        tempList.clear();
                    }
                    else {
                        ToastUtils.show(mActivity, "数据请求异常");
                        rlNoMessage.setVisibility(View.VISIBLE);
                        ptrLayout.setVisibility(View.GONE);
                    }
                }
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrLayout.onFooterRefreshComplete();
                int errorcode = response.getErrorcode();
                // 11，12,，28,53
                switch (errorcode) {
                    case 8:
                        LG.e("订单列表", "errorcode="+errorcode);
                        break;
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 53:
                        ToastUtils.show(mActivity, "您没有账单信息");
                        rlNoMessage.setVisibility(View.VISIBLE);
                        ptrLayout.setVisibility(View.GONE);
                        break;
                    case 27:
                    case 28:
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        LG.e("订单列表", "errorcode="+errorcode);
                        break;
                }

            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                ptrLayout.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrLayout.onFooterRefreshComplete();
                ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }
}
