package com.minfo.carrepairseller.activity.shop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.utils.CMethodUtil;
import com.hyphenate.easeui.utils.EaseDialogUtil;
import com.hyphenate.easeui.utils.EnsureCancleInterface;
import com.minfo.carrepairseller.CommonInterface.PermissionListener;
import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.activity.order.OrderSureActivity;
import com.minfo.carrepairseller.adapter.shop.ImgAdapter;
import com.minfo.carrepairseller.chat.ChatActivity;
import com.minfo.carrepairseller.chat.HuanUtils;
import com.minfo.carrepairseller.dialog.CarTypeListView;
import com.minfo.carrepairseller.dialog.ChooseGoodsNum;
import com.minfo.carrepairseller.entity.ProductItem;
import com.minfo.carrepairseller.entity.shop.ProductDetail;
import com.minfo.carrepairseller.entity.shop.XiadanModel;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.DialogUtil;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle, tvName, tvIntro, tvPrice, tvNumber, tvChoseNumber, tvKindNumber, tvNameCar, tvNumberAll, tvNUmberNew, tvBuy, tvOem;
    private TextView tvShouchang;
    private ImageView ivShoucang;
    private ImageView ivBack, ivImg;
    private LinearLayout llKeFu, llStore, llShouCang, llCall;
    private LinearLayout llAll, llNew;
    private RelativeLayout rlCount, rlOem, rlKind;
    private ProductItem productItem;
    private String goodid;
    private ViewPager viewPager;
    private ImgAdapter iAdapter;
    private List<String> imgUrl = new ArrayList<>();
    private ProductDetail productDetail;
    private TextView tvJubu;
    private int chooseNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle;
        bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            productItem = (ProductItem) bundle.getSerializable("details");
        }

        if(savedInstanceState!= null) {
            LG.e("商品详情", "被回收了");
            productItem = (ProductItem) savedInstanceState.getSerializable("details");
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
        ivShoucang = (ImageView) findViewById(R.id.iv_shoucang);
        ivImg = (ImageView) findViewById(R.id.iv_logo);

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

        llAll = (LinearLayout) findViewById(R.id.ll_all);
        llNew = (LinearLayout) findViewById(R.id.ll_new);

        rlCount = ((RelativeLayout) findViewById(R.id.ll_count));
        rlOem = ((RelativeLayout) findViewById(R.id.ll_oem));
        rlKind = ((RelativeLayout) findViewById(R.id.ll_kind));
        tvBuy.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        llStore.setOnClickListener(this);
        llShouCang.setOnClickListener(this);
        rlCount.setOnClickListener(this);
        rlKind.setOnClickListener(this);
        llKeFu.setOnClickListener(this);
        llCall.setOnClickListener(this);
        ivImg.setOnClickListener(this);
        llAll.setOnClickListener(this);
        llNew.setOnClickListener(this);

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
            ToastUtils.show(this, "暂无网络,请重新连接");
        }

    }

    @Override
    protected void initViews() {
        tvTitle.setText("商品详情");
//        Bundle bundle = new Bundle();
//        bundle = getIntent().getBundleExtra("info");
//        if (bundle != null) {
//            productItem = (ProductItem) bundle.getSerializable("details");
//        }

        if(productItem == null) {
            ToastUtils.show(this, "获取数据信息失败");
            return;
        }
        else {
            goodid = productItem.getGoodid();
        }
        if(MyCheck.isEmpty(Constant.userid)) {
            Constant.userid = utils.getUserid();
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
                //setResult(RESULT_OK, new Intent());
                onBackPressed();
                break;
            case R.id.tv_buy:
//                ToastUtils.show(this, "555");
                if(productDetail != null) {
                    if(utils.isOnLine()) {
                        reqxiadan();
                    }
                    else {
                        ToastUtils.show(mActivity, R.string.no_internet);
                    }
                }
                else {
                    ToastUtils.show(mActivity, "获取商品信息失败，请稍后再试");
                }
                break;
            case R.id.iv_logo:
                if(productDetail != null) {
                    Intent intent = new Intent(ProductDetailActivity.this, StoreActivity.class);
                    intent.putExtra("shopid", productDetail.getShopid());
                    startActivity(intent);
                    finish();
                }
                else {
                    ToastUtils.show(mActivity, "无法查看该店铺，请稍后再试");
                }
                break;
            case R.id.ll_store:
                if(productDetail != null) {
                    Intent intent = new Intent(ProductDetailActivity.this, StoreActivity.class);
                    intent.putExtra("shopid", productDetail.getShopid());
                    startActivity(intent);
                    finish();
                }
                else {
                    ToastUtils.show(mActivity, "无法查看该店铺，请稍后再试");
                }
                break;
            case R.id.ll_shoucang:
                if(tvShouchang.getText().toString().equals("收藏")) {
                    if(utils.isOnLine()) {
                        reqShouchang();
                    }
                    else {
                        ToastUtils.show(mActivity, R.string.no_internet);
                    }
                }
                else {
                    reqQuxiaoShouchang();
                }
                break;
            case R.id.ll_count:
                showChoseGoodNumDialog();
                break;
            case R.id.ll_kind: // 查看适用车型
                showCarTypeListDialog();
                break;
            case R.id.ll_kefu:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    jumpToKefu();
                }
                else {
                    requestRunTimePermission(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION}
                            , new PermissionListener() {
                                @Override
                                public void onGranted() {
//                                    jumbToPhotosActivity(pos);
                                    jumpToKefu();
                                }

                                @Override
                                public void onGranted(List<String> grantedPermission) {
//                            reqDate();


                                }

                                @Override
                                public void onDenied(List<String> deniedPermission) {
//                                  jumbToPhotosActivity(pos);
                                    String str = "您拒绝的权限将影响";
                                    String str1 = "";
                                    int i = 0;
                                    for(String string : deniedPermission) {
                                        if(i!=0){
                                            str += "、";
                                            str1 +="、";
                                        }
                                        if(string.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                            str += "相册";
                                        }
                                        else if(string.equals(Manifest.permission.CAMERA)) {
                                            str += "拍照";
                                        }
                                        else if(string.equals(Manifest.permission.RECORD_AUDIO)) {
                                            str += "语音";
                                            str1 += "录音";
                                        }
                                        else if(string.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                            str += "定位";
                                            str1 += "定位";
                                        }
                                    }

                                    str += "功能的使用！";
//                                    ToastUtils.show(mActivity, str);
                                    //权限被拒绝
                                    EaseDialogUtil.showMsgDialog(mActivity, "提示", str+"是否要开启"+str1+"权限？", "去设置", "取消", new EnsureCancleInterface() {
                                        @Override
                                        public void ensure() {
                                            Intent intent= CMethodUtil.getAppDetailSettingIntent(mActivity);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void cancle() {
                                            jumpToKefu();
                                        }
                                    });

                                }
                            });
                }
                break;
            case R.id.ll_call:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    jumpToCall();
                }
                else {
                    requestRunTimePermission(new String[]{Manifest.permission.CALL_PHONE}
                            , new PermissionListener() {
                                @Override
                                public void onGranted() {
                                    jumpToCall();
                                }

                                @Override
                                public void onGranted(List<String> grantedPermission) {
//                                  reqDate();
                                }

                                @Override
                                public void onDenied(List<String> deniedPermission) {
//                                 jumbToPhotosActivity(pos);

                                   if(deniedPermission.get(0).equals(Manifest.permission.CALL_PHONE)) {
                                        ToastUtils.show(mActivity, "请开启您拨打电话的权限");
                                    }

                                }
                            });
                }
                break;
            case R.id.ll_all:
                if(productDetail != null) {
                    Intent intentall = new Intent(ProductDetailActivity.this, StoreActivity.class);
                    intentall.putExtra("shopid", productDetail.getShopid());
                    intentall.putExtra("type", 0);
                    startActivity(intentall);
                    finish();
                }
                else {
                    ToastUtils.show(mActivity, "无法查看该店铺，请稍后再试");
                }
                break;
            case R.id.ll_new:
                if(productDetail != null) {
                    Intent intentnew = new Intent(ProductDetailActivity.this, StoreActivity.class);
                    intentnew.putExtra("shopid", productDetail.getShopid());
                    intentnew.putExtra("type", 1);
                    startActivity(intentnew);
                    finish();
                }
                else {
                    ToastUtils.show(mActivity, "无法查看该店铺，请稍后再试");
                }
                break;
        }
    }

    /**
     * 跳转客服
     */
    private void jumpToKefu() {
        if(productDetail != null) {
            Intent intent = new Intent(ProductDetailActivity.this, ChatActivity.class);

            // it's single chat
            intent.putExtra("userNickName", productDetail.getSellerName());//缺昵称
            intent.putExtra("userHeadImage", productDetail.getSellerPicture());
            intent.putExtra(HuanUtils.EXTRA_USER_ID, "sell" + productDetail.getSellerid());
            startActivity(intent);
        }else {
            ToastUtils.show(mActivity, "获取客服信息失败，请稍后再试");
        }
    }

    /**
     * 跳转客服
     */
    private void jumpToCall() {
        if(productDetail != null) {
            if (productDetail.getTel() != null && !productDetail.getTel().equals("")) {
                call(productDetail.getTel());

            } else {
                ToastUtils.show(this, "该用户暂时没有预留电话");

            }
        }else {
            ToastUtils.show(mActivity, "获取商家电话失败，请稍后再试");
        }
    }

    /**
     * 调用拨号功能
     * @param phone 电话号码
     */
    private void call(String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(mActivity.checkSelfPermission(Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        }else{
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }

    }
    /**
     * 显示选择商品数量弹框
     */
    private void showChoseGoodNumDialog() {
        ChooseGoodsNum chooseGoodsNum = new ChooseGoodsNum(mActivity);
        chooseGoodsNum.setEnsureOnclik(new ChooseGoodsNum.EnsureOnClick() {
            @Override
            public void ensure(int num) {
                tvChoseNumber.setText(num+"件");
                chooseNum = num;
            }
        });
        chooseGoodsNum.setNum(chooseNum);
        if(productDetail != null) {
            chooseGoodsNum.setPrice(productDetail.getPrice());
        }
        DialogUtil.showDialog(mActivity, chooseGoodsNum);
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
//                    ProductDetailAll productDetailAll = response.getObj(ProductDetailAll.class);
                    productDetail = response.getObj(ProductDetail.class);
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
                    case 12:
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
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
            }
        });
    }

    //详情数据
    private void bindData() {
        tvName.setText(productDetail.getName());
        tvPrice.setText("￥"+ MyCheck.priceFormatChange(productDetail.getPrice()));
        tvNameCar.setText(productDetail.getSellerName());
        tvNumberAll.setText(productDetail.getAllNum()+"");
        tvNUmberNew.setText(productDetail.getNewNum()+"");
        tvNumber.setText(productDetail.getAmount()+"人购买");
        tvIntro.setText(productDetail.getDetail());
        tvOem.setText(productDetail.getOem());
        tvShouchang.setText("取消收藏");
        if(productDetail.getIscollect() == 0) {
            tvShouchang.setText("收藏");
            ivShoucang.setImageResource(R.mipmap.qixiu_shoucang);
        }
        else {
            tvShouchang.setText("已收藏");
            ivShoucang.setImageResource(R.mipmap.qixiu_shoucang1);
        }
        imgUrl.clear();

        if (productDetail.getImg() != null) {
            tvJubu.setText("1" + "/" + productDetail.getImg().size());
            for (int i = 0; i < productDetail.getImg().size(); i++) {
                imgUrl.add(i, getResources().getString(R.string.img_baseurl)+productDetail.getImg().get(i));
            }
            iAdapter = new ImgAdapter(this, imgUrl);
            viewPager.setAdapter(iAdapter);

        }
        if(productDetail.getCarList() != null) {
            tvKindNumber.setText("适用" + productDetail.getCarList().size() + "款车型");
        }
        UniversalImageUtils.displayImageCircle(productDetail.getSellerPicture(), ivImg, 124);
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
     * 商品下单接口
     */
    private void reqxiadan() {
        String url = getResources().getString(R.string.api_baseurl) + "order/AddToOrder.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + productItem.getGoodid()+"*"+productDetail.getShopid()+"*"+productDetail.getPrice()+"*"+chooseNum);
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
                    XiadanModel model = response.getObj(XiadanModel.class);
                    if(model != null) {
                        Intent intent = new Intent(mActivity, OrderSureActivity.class);
                        intent.putExtra("orderid", model.getOrderid());
                        intent.putExtra("addtime", model.getAddtime());
                        startActivity(intent);
                    }
                    //                    ProductDetailAll productDetailAll = response.getObj(ProductDetailAll.class);
//                    productDetail = productDetailAll.getInfo();
//                    if (productDetail != null) {
//                        bindData();
//                    }
                }
                else {
                    ToastUtils.show(mActivity, "服务器异常，请稍后再试");
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                // 11，12,31，32，33，34，35
                switch (errorcode) {
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "用户信息异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 31: // 商品id不合法
                    case 32: // 店铺id 不合法
                    case 35: // 此商品不存在
                        ToastUtils.show(mActivity, "商品信息获取失败，请稍后再试");
                        break;
                    case 33: // 价格不是整数或小数
                        ToastUtils.show(mActivity, "商品价格异常");
                        break;
                    case 34: // 商品数量不合理
                        ToastUtils.show(mActivity, "请选择至少1件商品");
                        break;
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
    private void reqShouchang() {
        String url = getResources().getString(R.string.api_baseurl) + "user/addCollect.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + productItem.getGoodid());
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
                    ToastUtils.show(mActivity, "收藏成功");
                    tvShouchang.setText("取消收藏");
                    ivShoucang.setImageResource(R.mipmap.qixiu_shoucang1);
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                switch (errorcode) {
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

    /**
     * quxi收藏接口
     */
    private void reqQuxiaoShouchang() {
        String url = getResources().getString(R.string.api_baseurl) + "user/DeleteCollect.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + productItem.getGoodid());
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
                    tvShouchang.setText("收藏");
                    ivShoucang.setImageResource(R.mipmap.qixiu_shoucang);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        outState.putSerializable("details", productItem);
    }
}
