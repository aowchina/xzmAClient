package com.minfo.carrepair.activity.query;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.activity.shop.ProductDetailActivity;
import com.minfo.carrepair.adapter.BaseViewHolder;
import com.minfo.carrepair.adapter.CommonAdapter;
import com.minfo.carrepair.entity.ProductItem;
import com.minfo.carrepair.entity.query.PartsItem;
import com.minfo.carrepair.entity.query.PinpaiModel;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ConstantUrl;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 配件和OEM查询输入oem或配件名号查询配件
 * queryType 0，配件名查询；1，OEM号查询
 */
public class ProductQueryActivity extends BaseActivity implements View.OnClickListener {
    private int page = 1;
    private List<PartsItem> PartsItemList = new ArrayList<>();
    private List<PartsItem> tempList = new ArrayList<>();
    private boolean isRefresh;
    private boolean isLoad;
    private PtrClassicFrameLayout ptrLayout;
    private ListView listView;
    public CommonAdapter<PartsItem> adapter;
    private EditText etQuery;
    private TextView tvQuery;
    private ImageView ivBack;
    private LinearLayout llQuery;
    private String strQuery;
    private int queryType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_product_query);
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        if(intent == null) {
            LG.e("配件查询", "未接到type参数");
            finish();
            return;
        }
        queryType = intent.getIntExtra(Constant.QUERY_TYPE, 0);

        ptrLayout = ((PtrClassicFrameLayout) findViewById(R.id.ptr_layout));
        listView = ((ListView) findViewById(R.id.lv_product));
        etQuery = ((EditText) findViewById(R.id.et_query));
        llQuery = ((LinearLayout) findViewById(R.id.ll_query));
        tvQuery = ((TextView) findViewById(R.id.tv_query));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        switch (queryType) {
            case 0:
                etQuery.setHint("请输入您要查询的配件名称");
                break;
            case 1:
                etQuery.setHint("请输入您要查询的配件OEM号");
                break;
        }

    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        tvQuery.setOnClickListener(this);

//        for (int i = 0; i < 3; i++) {
//            PartsItemList.add(new PartsItem());
//
//        }
        adapter = new CommonAdapter<PartsItem>(this, PartsItemList, R.layout.item_product_query) {
            @Override
            public void convert(BaseViewHolder helper, PartsItem item, int position) {
                ImageView icon = helper.getView(R.id.iv_product);
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_king, item.getPinpai());
                helper.setText(R.id.tv_oem, "OEM: "+item.getOem());
                helper.setText(R.id.tv_price, "￥"+item.getPrice());

                UniversalImageUtils.displayImageUseDefOptions(item.getIcon(), icon);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                ProductItem item = new ProductItem();
                item.setGoodid(PartsItemList.get(position).getGoodid());
                bundle.putSerializable("details", item);
                intent.putExtra("info", bundle);
//                startActivity(intent);
                startActivity(intent);
            }
        });
        if (isOnline()) {
            //showWait();
            //reqData();
        } else {
            ToastUtils.show(ProductQueryActivity.this, "暂无网络");
        }
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

//                isRefresh = true;
//                page = 1;
//                if (isOnline()) {
//                    showWait();
//                    reqData();
//                } else {
//                    ToastUtils.show(mActivity, "暂无网络");
//                }
                if(isOnline()) {
                    preQuery();
                }
                else {
                    ToastUtils.show(mActivity, R.string.no_internet);
                }
                ptrLayout.refreshComplete();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, listView, header);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_query:
                preQuery();
                break;

        }
    }

    /**
     * 处理查询点击事件
     */
    private void preQuery() {
        PartsItemList.clear();
        adapter.notifyDataSetChanged();

        strQuery = etQuery.getText().toString();
        if(strQuery == null || strQuery.equals("")) {
            String msg = (queryType == 0 ? "请输入您要查询的配件名称" : "请输入您要查询的OEM号");
            ToastUtils.show(mActivity, msg);
            return;
        }
        switch (queryType) {
            case 0:
                reqQueryByName();
                break;
            case 1:
                reqQueryByOEM();
                break;
        }
    }
    /**
     * 配件名查询接口
     */
    private void reqQueryByName() {
        Map<String, String> params = utils.getParams(utils.getBasePostStr()+"*"+Constant.userid+"*"+utils.convertChinese(strQuery));
        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_QUERY_PEIJIAN;
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }
            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if(response != null) {
                    LG.e("配件查询", "response="+response);
                    PartsItemList.clear();
                    List<PartsItem> items = response.getList(PartsItem.class);
                    if(items != null && items.size() > 0) {
                        PartsItemList.addAll(items);
                    }else {
                        ToastUtils.show(mActivity, "未找到该名称的配件");
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    ToastUtils.show(mActivity,"服务器异常");
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // errorcode为 12,17,18 时，给予对应错误提示；
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 17:
                        ToastUtils.show(mActivity,"配件名不能为空");
                        break;
                    case 18:
                        ToastUtils.show(mActivity,"此配件暂未上架");
                        break;
                    default:
                        ToastUtils.show(mActivity,"服务器异常");
                        LG.e("配件查询", "errorcode="+errorcode);
                        break;
                }
                PartsItemList.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity,"服务器异常");
                LG.e("配件查询", "errorcode="+code+"msg="+msg);
                PartsItemList.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }
    /**
     * OEM号查询接口
     */
    private void reqQueryByOEM() {

        Map<String, String> params = utils.getParams(utils.getBasePostStr()+"*"+Constant.userid+"*"+strQuery);
        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_QUERY_OEM;
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if(response != null) {
                    LG.e("配件查询", "response="+response);
                    PartsItemList.clear();
                    List<PartsItem> items = response.getList(PartsItem.class);
                    if(items != null && items.size() > 0) {
                        PartsItemList.addAll(items);
                    }else {
                        ToastUtils.show(mActivity, "未找到该OEM号的配件");
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    ToastUtils.show(mActivity,"服务器异常");
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // errorcode为 12,15,16 时，给予对应错误提示；
                switch (errorcode) {
                    case 12:
                        ToastUtils.show(mActivity,"账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 15:
                        ToastUtils.show(mActivity,"oem号不能为空");
                        break;
                    case 16:
                        ToastUtils.show(mActivity,"此oem号的商品暂未上架");
                        break;
                    default:
                        ToastUtils.show(mActivity,"服务器异常");
                        LG.e("配件查询", "errorcode="+errorcode);
                        break;

                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity,"服务器异常");
                LG.e("配件查询", "errorcode="+code+"msg="+msg);
            }
        });
    }
}
