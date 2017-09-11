package com.minfo.carrepairseller.activity.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.publish.ShowPriceActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.purchase.PurchaseList;
import com.minfo.carrepairseller.entity.showprice.ShowPriceInfo;
import com.minfo.carrepairseller.entity.showprice.ShowPriceList;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


public class MyShowPriceListActivity extends BaseActivity implements View.OnClickListener {
    private int page = 1;
    private List<ShowPriceList> purchaseList = new ArrayList<>();
    private List<ShowPriceList> tempList = new ArrayList<>();
    private ShowPriceInfo showPriceInfo = new ShowPriceInfo();
    private boolean isRefresh;
    private boolean isLoad;
    private PtrClassicFrameLayout ptrLayout;
    private ListView listView;
    private TextView tvTitle;
    private ImageView ivBack;
    private CommonAdapter<ShowPriceList> commonAdapter;
    private PurchaseList purchaseEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_my_show_price_list);
    }


    @Override
    protected void findViews() {
        ptrLayout = ((PtrClassicFrameLayout) findViewById(R.id.ptr_layout));
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));

        listView = ((ListView) findViewById(R.id.lv_shop));
    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        tvTitle.setText("报价者列表");
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            purchaseEntity = (PurchaseList) bundle.getSerializable("purchase");
            if (purchaseEntity != null) {
                if (isOnline()) {
                    purchaseList.clear();
                    showWait();
                    reqShowPriceList();
                } else {
                    ToastUtils.show(this, "暂无网络,请重新连接");
                }
            }
        }

        commonAdapter = new CommonAdapter<ShowPriceList>(mActivity, purchaseList, R.layout.item_showprice_single) {
            @Override
            public void convert(BaseViewHolder helper, ShowPriceList item, final int position) {
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_type, item.getType());
                helper.setText(R.id.tv_price, "¥ " + item.getPrice());
                helper.setText(R.id.tv_pinpai, item.getBname());
                helper.setText(R.id.tv_xi, item.getSname());
                helper.setText(R.id.tv_kuan, item.getCname());
                ImageView icon = helper.getView(R.id.iv_product);
                UniversalImageUtils.displayImageUseDefOptions(item.getPicture(), icon);
            }
        };
        listView.setAdapter(commonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, ShowPriceActivity.class);
                intent.putExtra("id", purchaseList.get(position).getId());
                startActivity(intent);
            }
        });

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                purchaseList.clear();
                isRefresh = true;
                page = 1;
                if (isOnline()) {
                    showWait();
                    reqShowPriceList();
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
                            reqShowPriceList();
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
     * @funcation 请求报价列表接口
     ****/
    public void reqShowPriceList() {

        String url = getResources().getString(R.string.api_baseurl) + "goods/SetMoneyList.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + page + "*" + purchaseEntity.getBid());//

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loading.dismiss();
                Log.e("onRequestSuccess", response.getData());
                if (response != null) {
                    if (isRefresh) {
                        isRefresh = false;
                    }
                    showPriceInfo = response.getObj(ShowPriceInfo.class);
                    tempList = showPriceInfo.getSetMoneyList();
                    purchaseList.addAll(tempList);

                    if (page > 1) {
                        if (purchaseList.size() > 0) {
                            ToastUtils.show(MyShowPriceListActivity.this, "加载成功");
                        } else {
                            ToastUtils.show(MyShowPriceListActivity.this, "无更多内容");
                        }
                    }
                }
                ptrLayout.refreshComplete();

                commonAdapter.notifyDataSetChanged();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                loading.dismiss();
                isRefresh = false;
                ToastUtils.show(MyShowPriceListActivity.this, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                loading.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
}
