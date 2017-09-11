package com.minfo.carrepairseller.activity.personl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.activity.shop.ProductDetailActivity;
import com.minfo.carrepairseller.adapter.BaseViewHolder;
import com.minfo.carrepairseller.adapter.CommonAdapter;
import com.minfo.carrepairseller.entity.ProductItem;
import com.minfo.carrepairseller.entity.personl.CollectProductModel;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ConstantUrl;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;
import com.minfo.carrepairseller.widget.PullToRefreshView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 我的收藏列表界面
 */
public class CollectActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rlNoMessage;
    private LinearLayout llContent;

    private TextView tvTitle;
    private ImageView ivBack;
    private ListView listView;
//    private PtrClassicFrameLayout ptrLayout;
    private PullToRefreshView pullToRefreshView;
    private CommonAdapter<CollectProductModel.ProductItem> commonAdapter; // 产品
    private List<CollectProductModel.ProductItem> productList = new ArrayList<>();
    private List<CollectProductModel.ProductItem> tempList = new ArrayList<>();


    private boolean isRefresh = false;
    private boolean isLoad = false;
    private int page = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_collect);
    }

    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        listView = ((ListView) findViewById(R.id.lv_collect));
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.ptr_collect);

        rlNoMessage = (RelativeLayout) findViewById(R.id.rl_no_message);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
//        ptrLayout = ((PtrClassicFrameLayout) findViewById(R.id.ptr_layout));
        
    }

    @Override
    protected void initViews() {
        ivBack.setOnClickListener(this);
        tvTitle.setText("收藏");
//        for (int i = 0; i <3 ; i++) {
//            productList.add(new CollectProductModel.ProductItem());
//
//        }

        commonAdapter = new CommonAdapter<CollectProductModel.ProductItem>(this, productList, R.layout.item_product_collect) {
            @Override
            public void convert(BaseViewHolder helper, CollectProductModel.ProductItem item, int position) {

                ImageView imageView = helper.getView(R.id.iv_product);
                TextView tvDel = helper.getView(R.id.tv_quxiao);

                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_price, "￥"+ MyCheck.priceFormatChange(item.getPrice()));
                helper.setText(R.id.tv_pinpai, item.getPinpai()+ "  " + item.getChexi() + "  " + item.getChexing());
//                helper.setText(R.id.tv_xi, item.getChexi());
//                helper.setText(R.id.tv_kuan, item.getChexing());
                UniversalImageUtils.displayImageUseDefOptions(item.getImg(), imageView);
                final int pos = position;
                tvDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(utils.isOnLine()) {
                            reqQuxiaoShouchang(pos);
                        }
                        else {
                            ToastUtils.show(mActivity, R.string.no_internet);
                        }
                    }
                });
//                ImageView imageView = helper.getView(R.id.iv_car);
//                int size = (utils.getScreenWidth() - utils.dp2px(130)) / 4;
//                imageView.setLayoutParams(new LinearLayout.LayoutParams(size, size));
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            }
        };
        listView.setAdapter(commonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                ProductItem item = new ProductItem();
                item.setGoodid(productList.get(position).getGoodid());
                bundle.putSerializable("details", item);
                intent.putExtra("info", bundle);
                startActivity(intent);
                startActivity(new Intent(CollectActivity.this, ProductDetailActivity.class));
            }
        });
        if (isOnline()) {
            //showWait();
//            isRefresh = true;
//            page = 1;
            reqData();
        } else {
            ToastUtils.show(this, "暂无网络");
        }
        
        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                if(!isRefresh) {
                    if (isOnline()) {
                        page = 1;
                        isRefresh = true;
                        showWait();
                        reqData();
                    } else {
                        ToastUtils.show(mActivity, "暂无网络");
                    }
                }
            }
        });
        pullToRefreshView.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if(!isLoad) {
                    if (isOnline()) {
                        page ++;
                        isLoad = true;
                        showWait();
                        reqData();
                    } else {
                        ToastUtils.show(mActivity, "暂无网络");
                    }
                }
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

        }
    }

    /**
     * 请求数据
     */
    private void reqData() {

        String url = getResources().getString(R.string.api_baseurl) + ConstantUrl.URL_USER_COLLECT;
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid );
        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                showWait();
            }


            @Override public void onRequestSuccess(BaseResponse response) {
                Log.e("productList", response.toString());
                cleanWait();
                if(response != null) {
                    CollectProductModel model = response.getObj(CollectProductModel.class);
                    if(model != null) {
                        productList.clear();
                        if (isRefresh) {
                            isRefresh = false;
                            pullToRefreshView.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                            ToastUtils.show(mActivity, "刷新成功");
                        }
                        if (isLoad) {
                            isLoad = false;
                            pullToRefreshView.onFooterRefreshComplete();
                        }

                    tempList = model.getList();
                    if(tempList != null) {
                        productList.addAll(tempList);
                    }
                if(productList.size() != 0) {
                    rlNoMessage.setVisibility(View.GONE);
                    llContent.setVisibility(View.VISIBLE);
                }
                else {
                    rlNoMessage.setVisibility(View.VISIBLE);
                    llContent.setVisibility(View.GONE);
                }
                        pullToRefreshView.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                        pullToRefreshView.onFooterRefreshComplete();
                        if (page > 1) {
                            if (tempList != null && tempList.size() != 0) {
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
                        ToastUtils.show(mActivity, "没有数据");
                    }
                }
                else {
                    ToastUtils.show(mActivity, "服务器异常");
                }
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                pullToRefreshView.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                pullToRefreshView.onFooterRefreshComplete();
                int errorcode = response.getErrorcode();
                // 11，12，27, 28
                switch (errorcode) {
                    case 10:
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 27:
                    case 28:
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        LG.e("收藏列表", "errorcode="+errorcode);
                        break;
                }

            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                pullToRefreshView.onHeaderRefreshComplete("更新于:" + new Date().toLocaleString());
                pullToRefreshView.onFooterRefreshComplete();
                ToastUtils.show(mActivity, R.string.network_error);
            }
        });
    }

    /**
     * 取消收藏接口
     */
    private void reqQuxiaoShouchang(int position) {
        String url = getResources().getString(R.string.api_baseurl) + "user/DeleteCollect.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + productList.get(position).getGoodid());
        Log.e(TAG, params.toString());

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                if (response != null && !response.equals("")) {
                    Log.e("response", response.getData());
                    ToastUtils.show(mActivity, "收藏已删除");
                    isRefresh = true;
                    page = 1;
                    reqData();
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    // 11，12,42
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "用户登录状态异常");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 42:
                        ToastUtils.show(mActivity, "收藏商品失败");
                        break;
                    case 300:
                    case 301:
                    case 9:
                    default:
                        ToastUtils.show(mActivity, "服务器异常，请稍后再试");
                        break;
                }

            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, R.string.network_error);
            }
        });
    }
}
