package com.minfo.carrepair.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.activity.publish.ShowPriceActivity;
import com.minfo.carrepair.activity.query.ProductPurchaseVINActivity;
import com.minfo.carrepair.adapter.shop.PurchaseAdapter;
import com.minfo.carrepair.entity.purchase.PurChaseEntity;
import com.minfo.carrepair.entity.purchase.PurchaseList;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * A simple {@link Fragment} subclass.
 * 求购列表界面
 */
public class PurchaseFragment extends BaseFragment implements PurchaseAdapter.BaoJiaListener, View.OnClickListener {
    private RelativeLayout rlNoMessage;
    private RelativeLayout rlNoMessageClick;

    private int page = 1;
    private PurchaseAdapter adapter;
    private List<PurchaseList> purchaseList = new ArrayList<>();
    private List<PurchaseList> tempList = new ArrayList<>();
    private PurChaseEntity purChaseEntity = new PurChaseEntity();
    private boolean isRefresh;
    private boolean isLoad;
    private PtrClassicFrameLayout ptrLayout;
    private ListView listView;

    public PurchaseFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_purchase, null);
        ptrLayout = ((PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout));
        rlNoMessage = (RelativeLayout) view.findViewById(R.id.rl_no_message);
        rlNoMessageClick = (RelativeLayout) view.findViewById(R.id.rl_no_message_click);

        listView = ((ListView) view.findViewById(R.id.lv_shop));
        rlNoMessageClick.setOnClickListener(this);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        if (isOnline()) {
            page = 1;
//            showWait();
            reqPurchaset();
        } else {
            ToastUtils.show(mActivity, "暂无网络");
        }
        adapter = new PurchaseAdapter(mActivity, purchaseList, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, ProductPurchaseVINActivity.class);
                intent.putExtra("bid", purchaseList.get(position).getBid());
                startActivity(intent);
            }
        });

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                if (isOnline()) {
                    if(!isRefresh) {
                        purchaseList.clear();
                        isRefresh = true;
                        page = 1;
                        reqPurchaset();
                    }

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
                            if(!isLoad) {
                                page++;
                                isLoad = true;
                                reqPurchaset();
                            }
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
    public void reqPurchaset() {

        String url = getResources().getString(R.string.api_baseurl) + "goods/BuyList.php";
        Map<String, String> params = utils.getParams(hdUtils.getBasePostStr() + "*" + Constant.userid + "*" + page);//

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
                    if(isLoad) {
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

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                isRefresh = false;
                int errorcode = response.getErrorcode();
                if(errorcode == 10) {
                    utils.jumpTo(mActivity, LoginActivity.class);
                    return;
                }
//
                ToastUtils.show(getActivity(), "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(getActivity(), "连接服务器失败，请检查您的网络！");
            }
        });
    }

    @Override
    public void baoJiaJump(PurchaseList item) {
        Intent intent = new Intent(mActivity, ShowPriceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("purchaselist", item);
        intent.putExtra("info", bundle);
        startActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            if(!isRefresh) {
                isRefresh = true;
                page = 1;
                reqPurchaset();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_no_message_click:
                if(!isRefresh) {
                    isRefresh = true;
                    page = 1;
                    reqPurchaset();
                }
                break;
        }
    }
}
