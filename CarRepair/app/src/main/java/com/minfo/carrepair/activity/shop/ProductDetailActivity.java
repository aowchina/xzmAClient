package com.minfo.carrepair.activity.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.activity.order.OrderSureActivity;
import com.minfo.carrepair.adapter.shop.ImgAdapter;
import com.minfo.carrepair.dialog.CarTypeListView;
import com.minfo.carrepair.entity.ProductItem;
import com.minfo.carrepair.entity.shop.ProductDetail;
import com.minfo.carrepair.entity.shop.ProductDetailAll;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle, tvName, tvIntro, tvPrice, tvNumber, tvChoseNumber, tvKindNumber, tvNameCar, tvNumberAll, tvNUmberNew, tvBuy, tvOem;
    private ImageView ivBack, ivImg;
    private LinearLayout llKeFu, llStore, llShouCang, llCall;
    private TextView tvShouchang;
    private RelativeLayout rlCount, rlOem, rlKind;
    private ProductItem productItem;
    private String goodid;
    private ViewPager viewPager;
    private ImgAdapter iAdapter;
    private List<String> imgUrl = new ArrayList<>();
    private ProductDetail productDetail;
    private TextView tvJubu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            productItem = (ProductItem) savedInstanceState.getSerializable("details");

        }
        else {
            Bundle bundle;
            bundle = getIntent().getBundleExtra("info");
            if (bundle != null) {
                productItem = (ProductItem) bundle.getSerializable("details");
            }
        }

        super.onCreate(savedInstanceState, R.layout.activity_product_detail);
    }

    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvIntro = ((TextView) findViewById(R.id.tv_intro));
        tvPrice = ((TextView) findViewById(R.id.tv_price));
        tvNumber = ((TextView) findViewById(R.id.tv_number));
        tvJubu = ((TextView) findViewById(R.id.tv_jubu));
        tvShouchang = (TextView) findViewById(R.id.tv_shouchang);

        tvChoseNumber = ((TextView) findViewById(R.id.tv_chose_number));
        tvKindNumber = ((TextView) findViewById(R.id.tv_kind_number));
        tvNameCar = ((TextView) findViewById(R.id.tv_name_car));
        tvNumberAll = ((TextView) findViewById(R.id.tv_number_all));
        tvNUmberNew = ((TextView) findViewById(R.id.tv_new_all));
        tvBuy = ((TextView) findViewById(R.id.tv_buy));
        tvOem = ((TextView) findViewById(R.id.tv_oem_number));

        ivBack = ((ImageView) findViewById(R.id.iv_back));
        viewPager = ((ViewPager) findViewById(R.id.vp_img));

        llKeFu = ((LinearLayout) findViewById(R.id.ll_kefu));
        llStore = ((LinearLayout) findViewById(R.id.ll_store));
        llShouCang = ((LinearLayout) findViewById(R.id.ll_shoucang));
        llCall = ((LinearLayout) findViewById(R.id.ll_call));

        rlCount = ((RelativeLayout) findViewById(R.id.ll_count));
        rlOem = ((RelativeLayout) findViewById(R.id.ll_oem));
        rlKind = ((RelativeLayout) findViewById(R.id.ll_kind));
        tvBuy.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        llStore.setOnClickListener(this);
        llShouCang.setOnClickListener(this);
        rlKind.setOnClickListener(this);

    }
    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        super.onNewIntent(intent);

        tvTitle.setText("商品详情");
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            productItem = (ProductItem) bundle.getSerializable("details");
        }
        if(productItem == null) {
            ToastUtils.show(this, "获取数据信息失败");
            return;
        }
        else {
            goodid = productItem.getGoodid();
        }

        if (isOnline()) {
            showWait();
            reqDetails();

        } else {
            ToastUtils.show(this, R.string.no_internet);
        }

    }
    @Override
    protected void initViews() {
        tvTitle.setText("商品详情");
//        Bundle bundle = new Bundle();
//        bundle = getIntent().getBundleExtra("info");
//        if (bundle != null) {
//            productItem = (ProductItem) bundle.getSerializable("details");
//            goodid = productItem.getGoodid();
//        }
        if(productItem == null) {
            ToastUtils.show(this, "获取数据信息失败");
            return;
        }
        else {
            goodid = productItem.getGoodid();
        }

        if (isOnline()) {

            showWait();
            reqDetails();

        } else {
            ToastUtils.show(this, "暂无网络,请重新连接");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                setResult(RESULT_OK, new Intent());
                onBackPressed();
                break;
            case R.id.tv_buy:
                ToastUtils.show(this, "555");
                startActivity(new Intent(ProductDetailActivity.this, OrderSureActivity.class));
                break;
            case R.id.ll_store:
                startActivity(new Intent(ProductDetailActivity.this, StoreActivity.class));
                break;
            case R.id.ll_shoucang:
                if(tvShouchang.getText().toString().equals("收藏")) {
                    reqShouchang();
                }
                else {
                    reqQuxiaoShouchang();
                }
                break;
            case R.id.ll_kind: // 查看适用车型
                showCarTypeListDialog();
                break;
        }
    }
    /**
     * 显示查看适用车型列表弹框
     */
    private void showCarTypeListDialog() {
        CarTypeListView carTypeListView = new CarTypeListView(mActivity);

        if(productDetail != null) {
            carTypeListView.setData(productDetail.getCarList());
        }
        DialogUtil.showDialog(mActivity, carTypeListView);
    }
    /**
     * 请求商品详情接口
     */
    private void reqDetails() {
        String url = getResources().getString(R.string.api_baseurl) + "goods/Detail.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + productItem.getGoodid());
        Log.e(TAG, params.toString());

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                if (response != null && !response.equals("")) {
                    Log.e("response", response.getData());
                    ProductDetailAll productDetailAll = response.getObj(ProductDetailAll.class);
                    productDetail = productDetailAll.getInfo();
                    if (productDetail != null) {
                        bindData();
                    }
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 26:
                        ToastUtils.show(mActivity, "商品信息获取失败");
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

    //详情数据
    private void bindData() {
        tvName.setText(productDetail.getName());
        tvPrice.setText("￥" + productDetail.getPrice());
        tvNameCar.setText(productDetail.getCar_name());
        imgUrl.clear();
        tvNumber.setText(productDetail.getAmount()+"人购买");
        tvIntro.setText(productDetail.getDetail());
        tvOem.setText(productDetail.getOem());

        if (productDetail.getImg() != null) {
            tvJubu.setText("1" + "/" + productDetail.getImg().size());
            for (int i = 0; i < productDetail.getImg().size(); i++) {
                imgUrl.add(i, getResources().getString(R.string.img_baseurl)+productDetail.getImg().get(i));
            }
            iAdapter = new ImgAdapter(this, imgUrl);
            viewPager.setAdapter(iAdapter);
        }


        //viewpager滑动监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvJubu.setText((position + 1) + "/" + productDetail.getImg().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    /**
     * 添加收藏接口
     */
    private void reqShouchang() {
        String url = getResources().getString(R.string.api_baseurl) + "user/addCollect.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + productItem.getGoodid());
        Log.e(TAG, params.toString());

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                if (response != null && !response.equals("")) {
                    Log.e("response", response.getData());
                    ToastUtils.show(mActivity, "收藏成功");
                    tvShouchang.setText("取消收藏");
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "商品信息获取失败");
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
            }
        });
    }

    /**
     * 添加收藏接口
     */
    private void reqQuxiaoShouchang() {
        String url = getResources().getString(R.string.api_baseurl) + "user/DeleteCollect.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + productItem.getGoodid());
        Log.e(TAG, params.toString());

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                if (response != null && !response.equals("")) {
                    Log.e("response", response.getData());
                    ToastUtils.show(mActivity, "收藏成功");
                    tvShouchang.setText("取消收藏");
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
                        ToastUtils.show(mActivity, "商品信息获取失败");
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);dgoodid = productItem.getGoodid();
//        ProductItem) savedInstanceState.getSerializable("details");
        outState.putSerializable("details", productItem);
    }
}
