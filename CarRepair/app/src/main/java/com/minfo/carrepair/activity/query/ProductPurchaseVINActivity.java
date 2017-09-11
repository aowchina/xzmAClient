package com.minfo.carrepair.activity.query;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.publish.ShowPriceActivity;
import com.minfo.carrepair.adapter.query.EditGoodPhotoUploadAdapter;
import com.minfo.carrepair.entity.purchase.PurchaseDetail;
import com.minfo.carrepair.entity.purchase.PurchaseItem;
import com.minfo.carrepair.entity.query.PictureItem;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.MyCheck;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import minfo.com.albumlibrary.widget.MyGridView;

/**
 * 配件求购
 */
public class ProductPurchaseVINActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle, tvPinPai, tvCarXi, tvCarXing, tvCar;
    private ImageView ivBack, ivCar;

    private TextView etVIN;
    private TextView etName, etPinPai, etOther;
    private LinearLayout llOther, llPinPai;
    private boolean otherFlag;
    private PurchaseItem purchaseItem;
    private PurchaseDetail purchaseDetail;
    private CheckBox cbYuanChang, cbChaiChe, cbPinPai, cbOther;//原厂 拆车  品牌 其他
    private Button btSure;//确定按钮
    private MyGridView mgvPhoto;//选取照片自定义控件
    private EditGoodPhotoUploadAdapter mAdapter;//选取照片自定义控件适配器    @Override
    private String fromchat;
    private String idBJ;

    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState!=null) {
            fromchat = savedInstanceState.getString("fromchat");
            idBJ = savedInstanceState.getString("bid");
        }
        else {
            fromchat = getIntent().getStringExtra("fromchat");
            idBJ = getIntent().getStringExtra("bid");
        }
        super.onCreate(savedInstanceState, R.layout.activity_product_purchase_vin);
    }

    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        tvPinPai = ((TextView) findViewById(R.id.tv_pinpai));
        tvCarXi = ((TextView) findViewById(R.id.tv_car_xi));
        tvCarXing = ((TextView) findViewById(R.id.tv_car_xing));
        ivCar = ((ImageView) findViewById(R.id.iv_car));
        etVIN = ((TextView) findViewById(R.id.et_vin));
        etName = ((TextView) findViewById(R.id.et_name));
        etPinPai = ((TextView) findViewById(R.id.et_pinpai));
        etOther = ((TextView) findViewById(R.id.et_other));

        mgvPhoto = ((MyGridView) findViewById(R.id.mgv_photo));
        llOther = ((LinearLayout) findViewById(R.id.ll_other));
        llPinPai = ((LinearLayout) findViewById(R.id.ll_pinpai));

        cbYuanChang = ((CheckBox) findViewById(R.id.radioButton1));
        cbChaiChe = ((CheckBox) findViewById(R.id.radioButton2));
        cbPinPai = ((CheckBox) findViewById(R.id.radioButton3));
        cbOther = ((CheckBox) findViewById(R.id.radioButton4));

        btSure = ((Button) findViewById(R.id.bt_ensure));

    }

    @Override
    protected void initViews() {
        tvTitle.setText("配件求购");
        ivBack.setOnClickListener(this);
        mAdapter = new EditGoodPhotoUploadAdapter(this, true);

        mgvPhoto.setAdapter(mAdapter);
        llOther.setOnClickListener(this);
        btSure.setOnClickListener(this);

//        fromchat = getIntent().getStringExtra("fromchat");
//        idBJ = getIntent().getStringExtra("bid");

        if(!MyCheck.isEmpty(idBJ)) {
            if (isOnline()) {
//            showWait();
                reqPurchaseDetails();
            } else {
                ToastUtils.show(this, R.string.no_internet);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.bt_ensure://发布报价
                Intent intent = new Intent(mActivity, ShowPriceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("purchaseitem", purchaseItem);
                intent.putExtra("info", bundle);
                intent.putExtra("fromchat", fromchat);
                startActivityForResult(intent, 10);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 10:
                    String nameOfPei = data.getStringExtra("name");
                    String idOfPei = data.getStringExtra("id");
                    String nameOfCar = data.getStringExtra("nameofcar");
                    Intent intentBackChat = new Intent();
                    intentBackChat.putExtra("name", nameOfPei);
                    intentBackChat.putExtra("id", idOfPei);

                    intentBackChat.putExtra("nameofcar", nameOfCar);
                    setResult(RESULT_OK, intentBackChat);

                    finish();
                    break;
            }

        }

    }

    /**
     * 请求求购详情接口
     */
    private void reqPurchaseDetails() {
        String url = getResources().getString(R.string.api_baseurl) + "goods/BuyDetail.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + idBJ);
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
                    purchaseDetail = response.getObj(PurchaseDetail.class);
                    if (purchaseDetail != null) {
                        purchaseItem = purchaseDetail.getInfo();
                        if (purchaseItem != null) {
                            bindData();

                        }
                    }
                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                switch (errorcode) {
                    case 30:
                        ToastUtils.show(mActivity, "求购信息不存在");
                        break;
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
        tvPinPai.setText(purchaseItem.getBname());
        tvCarXi.setText(purchaseItem.getSname());
        tvCarXing.setText(purchaseItem.getCname());
        etVIN.setText(purchaseItem.getVin());
        etName.setText(purchaseItem.getJname());
        UniversalImageUtils.displayImageUseDefOptions(purchaseItem.getImg(), ivCar);
        if (purchaseItem.getType() != null && purchaseItem.getType().size() > 0) {
            for (int i = 0; i < purchaseItem.getType().size(); i++) {
                if (purchaseItem.getType().get(i).equals("0")) {
                    cbYuanChang.setChecked(true);
                }
                if (purchaseItem.getType().get(i).equals("1")) {
                    cbChaiChe.setChecked(true);
                }
                if (purchaseItem.getType().get(i).equals("2")) {
                    cbPinPai.setChecked(true);
                    llPinPai.setVisibility(View.VISIBLE);
                    etPinPai.setText(purchaseItem.getPinpai());
                }
                if (purchaseItem.getType().get(i).equals("3")) {
                    cbOther.setChecked(true);
                    llOther.setVisibility(View.VISIBLE);
                    etOther.setText(purchaseItem.getOtherpz());
                }

            }

        }


        if (purchaseItem.getPicture() != null && purchaseItem.getPicture().size() > 0) {
            List<PictureItem> list = new ArrayList<>();
            for (String path : purchaseItem.getPicture()) {

                PictureItem issue = new PictureItem();
                issue.setImgUrl(getResources().getString(R.string.img_baseurl) + path);
                issue.setAdd(false);
                issue.setNet(true);
                list.add(issue);
            }

            mAdapter.addAll(list, mAdapter.getCount() - 1);
            if (mAdapter.getCount() > 8) {
                mAdapter.remove(mAdapter.getCount() - 1);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        fromchat = getIntent().getStringExtra("fromchat");
//        idBJ = getIntent().getStringExtra("bid");
        outState.putString("fromchat", fromchat);
        outState.putString("bid", idBJ);
    }
}
