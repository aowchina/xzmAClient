package com.minfo.carrepairseller.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.ProductItem;
import com.minfo.carrepairseller.entity.shop.ShopModel;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;
import com.minfo.carrepairseller.widget.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StoreActivity extends BaseActivity implements View.OnClickListener {

    private static final int PRODUCT_TYPE_ALL = 0;
    private static final int PRODUCT_TYPE_NEW = 1;

    private TextView tvTitle,tvPhone,tvPingJia,tvCount,tvCountAll,tvCountNew,tvProAll,tvProNew,tvNumberAll,tvNUmberNew,tvBuy,tvOem;
    private ImageView ivBack,ivStore;
    private LinearLayout llAll,llNew;
    private RelativeLayout rlCount,rlOem,rlKind;
    private View v1,v2;
    public CommonAdapter<ProductItem> adapter;
    private List<ProductItem> productList = new ArrayList<>();
    private ListView listView;
    private PullToRefreshView ptrStore;

    private boolean isload;
    private boolean isrefresh;
    private int page = 1;
    private ShopModel model;

    private int productType = PRODUCT_TYPE_ALL;

    private String shopId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_store);
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        shopId = intent.getStringExtra("shopid");
        productType = intent.getIntExtra("type", PRODUCT_TYPE_ALL);
        if(shopId == null || shopId.equals("")) {
            ToastUtils.show(mActivity, "无法查看该店铺信息");
            finish();
        }
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        ptrStore = (PullToRefreshView) findViewById(R.id.ptr_store);
        listView = ((ListView) findViewById(R.id.lv_store));

        View viewTop = LayoutInflater.from(mActivity).inflate(R.layout.activity_store_top, null);
        tvPhone = ((TextView) viewTop.findViewById(R.id.tv_phone));
        tvPingJia = ((TextView) viewTop.findViewById(R.id.tv_pingjia));
        tvCount = ((TextView) viewTop.findViewById(R.id.tv_count));
        tvCountAll = ((TextView) viewTop.findViewById(R.id.tv_count_all));
        tvCountNew = ((TextView) viewTop.findViewById(R.id.tv_count_new));
        tvProAll = ((TextView) viewTop.findViewById(R.id.tv_pro_all));
        tvProNew = ((TextView) viewTop.findViewById(R.id.tv_pro_new));

        v1 = ((View) viewTop.findViewById(R.id.v1));
        v2 = ((View) viewTop.findViewById(R.id.v2));


        ivStore = ((ImageView) viewTop.findViewById(R.id.iv_store));
        llAll = ((LinearLayout) viewTop.findViewById(R.id.ll_all));
        llNew = ((LinearLayout) viewTop.findViewById(R.id.ll_new));

        listView.addHeaderView(viewTop);

    }
    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        super.onNewIntent(intent);

        shopId = getIntent().getStringExtra("shopid");
        if(shopId == null || shopId.equals("")) {
            ToastUtils.show(mActivity, "无法查看该店铺信息");
            finish();
        }
        if (isOnline()) {
            showWait();
            page=1;
            reqData();
        }
        else {
            ToastUtils.show(mActivity, "网络异常");
        }
    }
    @Override
    protected void initViews() {
        tvTitle.setText("店铺");
        switch (productType) {
            case PRODUCT_TYPE_ALL:
                setProductAll();
                break;
            case PRODUCT_TYPE_NEW:
                setProductNew();
                break;
        }

        llAll.setOnClickListener(this);
        llNew.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        adapter = new CommonAdapter<ProductItem>(this, productList, R.layout.item_store) {
            @Override
            public void convert(BaseViewHolder helper, ProductItem item, int position) {
                ImageView icon = helper.getView(R.id.iv_product);

                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_price, "￥"+item.getPrice());

                UniversalImageUtils.displayImageUseDefOptions(item.getImg(), icon);
            }
        };
        listView.setAdapter(adapter);
//        setListViewHeightBasedOnChildren(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("details", productList.get(position-1));
                    Log.e("store","store"+productList.get(position-1).getGoodid());
                    intent.putExtra("info", bundle);
                    startActivity(intent);
                    finish();
                }
//                startActivity(new Intent(StoreActivity.this, ProductDetailActivity.class));
            }
        });

        ptrStore.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if(productType == PRODUCT_TYPE_NEW) {
                    ptrStore.onFooterRefreshComplete();
                    return;
                }
                if (isOnline()&&!isload) {
                    isload = true;
                    page++;
                    reqData();
                }
            }
        });
        ptrStore.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {

                if (isOnline()&&!isrefresh) {
                    isrefresh = true;
                    page = 1;
                    reqData();
                }
            }
        });
        ptrStore.setLastUpdated(new Date().toLocaleString());
        ptrStore.setHorizontalScrollBarEnabled(true);

        if (isOnline()) {
            showWait();
            reqData();
        }
        else {
            ToastUtils.show(mActivity,"网络异常");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_all:
                setProductAll();
                page = 1;
                productList.clear();
                reqData();
                break;
            case R.id.ll_new:
                setProductNew();
                page = 1;
                productList.clear();
                reqData();
                break;
        }
    }

    /**
     * 设置全部商品布局
     */
    private void setProductAll(){
        tvProAll.setTextColor(getResources().getColor(R.color.store_text_chose));
        tvCountAll.setTextColor(getResources().getColor(R.color.store_text_chose));
        v1.setBackgroundColor(getResources().getColor(R.color.store_text_chose));

        tvProNew.setTextColor(getResources().getColor(R.color.store_text_choseun));
        tvCountNew.setTextColor(getResources().getColor(R.color.store_text_choseun));
        v2.setBackgroundColor(getResources().getColor(R.color.color_white));
        productType = PRODUCT_TYPE_ALL;
    }
    /**
     * 设置新品布局
     */
    private void setProductNew(){
        tvProNew.setTextColor(getResources().getColor(R.color.store_text_chose));
        tvCountNew.setTextColor(getResources().getColor(R.color.store_text_chose));
        v2.setBackgroundColor(getResources().getColor(R.color.store_text_chose));

        tvProAll.setTextColor(getResources().getColor(R.color.store_text_choseun));
        tvCountAll.setTextColor(getResources().getColor(R.color.store_text_choseun));
        v1.setBackgroundColor(getResources().getColor(R.color.color_white));
        productType = PRODUCT_TYPE_NEW;
    }
    /**
     * 加载数据后，计算listview高度
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 请求店铺信息数据
     * 上行参数: 8段 * userid * 页码
     */
    private void reqData() {


        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_GOODS_SHOP;
        Map<String, String> params = utils.getParams(utils.getBasePostStr()+ "*" +Constant.userid + "*" + shopId + "*" + page);
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                showWait();
            }

            @Override public void onRequestSuccess(BaseResponse response) {
                if(response != null) {
                    Log.e("店铺", response.toString());
                    cleanWait();
                    model = response.getObj(ShopModel.class);
                    bindData();
                }
                else {
                    ToastUtils.show(mActivity, "服务器异常");
                }
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                ptrStore.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrStore.onFooterRefreshComplete();
                int errorcode = response.getErrorcode();
                // 9 10 11 25 300 301
                switch (errorcode) {
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 25:
                    case 28:
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        LG.e("店铺", "errorcode="+errorcode);
                        break;
                }

            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                ptrStore.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ptrStore.onFooterRefreshComplete();
            }
        });
    }

    private void bindData() {
        if(model != null) {
            if (isrefresh) {
                productList.clear();
                isrefresh = false;
                ptrStore.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                ToastUtils.show(mActivity, "刷新成功");
            }
            if (isload) {
                isload = false;
                ptrStore.onFooterRefreshComplete();
            }

            List<ProductItem> listAll = model.getAllGoods();
            List<ProductItem> listNew = model.getNewGoods();
            if(productType == PRODUCT_TYPE_ALL) {
                if(listAll != null && listAll.size() > 0) {
                    productList.addAll(listAll);
                }
            }
            else {
                if(listNew != null && listNew.size() > 0) {
                    productList.addAll(listNew);
                }
            }
            ShopModel.ShopInfo shopInfo = model.getShopInfo();
            if(shopInfo != null) {
                tvPhone.setText(shopInfo.getPhone());
                if (shopInfo.getHaoping()!=null){
                    tvPingJia.setText("好评率: "+ shopInfo.getHaoping()+"%");

                }else {
                    tvPingJia.setText("好评率: 100%");

                }
                UniversalImageUtils.displayImageUseDefOptions(shopInfo.getPicture(), ivStore);
            }
            tvCount.setText(model.getSellCount()+"");
            tvCountAll.setText(model.getAllCount()+"");
            tvCountNew.setText(model.getNewCount()+"");

            adapter.notifyDataSetChanged();
        }
        else {
            ToastUtils.show(mActivity, "服务器异常");
        }
    }
}
