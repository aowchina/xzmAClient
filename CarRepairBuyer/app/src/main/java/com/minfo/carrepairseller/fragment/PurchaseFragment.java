package com.minfo.carrepairseller.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.purchase.MyShowPriceListActivity;
import com.minfo.carrepairseller.adapter.shop.PurchaseAdapter;
import com.minfo.carrepairseller.entity.purchase.PurChaseEntity;
import com.minfo.carrepairseller.entity.purchase.PurchaseList;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 * 求购界面
 */
public class PurchaseFragment extends BaseFragment implements View.OnClickListener{
    private RelativeLayout rlNoMessage;
    private RelativeLayout rlNoMessageClick;
//    private LinearLayout llContent;

    private int page = 1;
    private PurchaseAdapter adapter;
    private List<PurchaseList> purchaseList = new ArrayList<>();
    private List<PurchaseList> tempList = new ArrayList<>();
    private boolean isRefresh;
    private boolean isLoad;
    private PtrClassicFrameLayout ptrLayout;
    private ListView listView;
    private  PurChaseEntity purChaseEntity;

    public PurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_purchase, null);
        ptrLayout = ((PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout));
        rlNoMessage = (RelativeLayout) view.findViewById(R.id.rl_no_message);
        rlNoMessageClick = (RelativeLayout) view.findViewById(R.id.rl_no_message_click);
//        llContent = (LinearLayout) view.findViewById(R.id.ll_content);
        rlNoMessageClick.setOnClickListener(this);
        listView = ((ListView) view.findViewById(R.id.lv_shop));
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        if (isOnline()) {
            page = 1;
            reqPurchase();
        } else {
            ToastUtils.show(mActivity, "暂无网络");
        }
        adapter = new PurchaseAdapter(mActivity, purchaseList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, MyShowPriceListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("purchase", purchaseList.get(position));
                intent.putExtra("info", bundle);
                startActivity(intent);
            }
        });

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {


                if (isOnline()) {
                    purchaseList.clear();
                    isRefresh = true;
                    page = 1;
                    reqPurchase();
                } else {
                    ToastUtils.show(mActivity, "暂无网络");
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, listView, header);
            }
        });
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
                            reqPurchase();
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

    /****
     * @funcation 请求求购列表接口
     ****/
    public void reqPurchase() {

        String url = getResources().getString(R.string.api_baseurl) + "goods/BuyList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + page);//

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                Log.e("reqPurchaset", response.getData());
                if (response != null) {
                    if (isRefresh) {
                        isRefresh = false;
                        purchaseList.clear();
                    }
                    if(isLoad ) {
                        isLoad = false;
                    }
                    purChaseEntity = response.getObj(PurChaseEntity.class);
                    tempList = purChaseEntity.getWantBuy();
                    if(tempList != null && tempList.size() > 0) {
                        purchaseList.addAll(tempList);
                    }
                    if(purchaseList.size() > 0) {
                        rlNoMessage.setVisibility(View.GONE);
                        ptrLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        rlNoMessage.setVisibility(View.VISIBLE);
                        ptrLayout.setVisibility(View.GONE);
                    }
                    if (page > 1) {
                        if (purchaseList.size() > 0) {
                            ToastUtils.show(mActivity, "加载成功");
                        } else {
                            ToastUtils.show(mActivity, "无更多内容");
                        }
                    }
                }
                ptrLayout.refreshComplete();

                adapter.notifyDataSetChanged();
                if(tempList != null) {
                    tempList.clear();
                }

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                isRefresh = false;
                int errorcode = response.getErrorcode();
                if(errorcode == 10||errorcode == 11) {
                    ToastUtils.show(mActivity,"账号异常，请重新登录");
                    utils.jumpToLoginActivity(mActivity);
                    return;
                }
//
                ToastUtils.show(getActivity(), "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
//
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_no_message_click:
                isRefresh = true;
                page = 1;
                reqPurchase();
                break;
        }
    }
}
