package com.minfo.carrepairseller.activity.query;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.utils.CMethodUtil;
import com.hyphenate.easeui.utils.EaseDialogUtil;
import com.hyphenate.easeui.utils.EnsureCancleInterface;
import com.minfo.carrepairseller.CommonInterface.PermissionListener;
import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.adapter.query.EditGoodPhotoUploadAdapter;
import com.minfo.carrepairseller.entity.query.ChexingItem;
import com.minfo.carrepairseller.entity.query.PictureItem;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ImgUtils;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.MyFileUpload;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.utils.UniversalImageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import minfo.com.albumlibrary.TreeConstant;
import minfo.com.albumlibrary.activity.PhotosActivity;
import minfo.com.albumlibrary.utils.LG;
import minfo.com.albumlibrary.widget.MyGridView;

/**
 * 配件求购
 */
public class ProductPurchaseVINActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int ALBUM_CHOOSE_PHOTOS_CODE2 = 1002;//选取照片返回值
    private static final int TO_CAR = 1003;//选取车型返回值

    private TextView tvTitle, tvPinPai, tvCarXi, tvCarXing, tvCar, tvChange;
    private ImageView ivBack, ivCar;
    private EditText etVIN;//车架号输入
    private EditText etName, etPinPai, etOther;//配件名称 品牌名称   其他
    private MyGridView mgvPhoto;//选取照片自定义控件
    private EditGoodPhotoUploadAdapter mAdapter;//选取照片自定义控件适配器
    private LinearLayout llOther, llPinPai;//品牌布局   其他布局
//    private ChexingItem chexingItem;//回传车款实体类
    private Button btSure;//确定按钮
    private List<Map<String, File>> files = new ArrayList<>();//选取照片记录集合
    private ImgUtils imgUtils;//照片处理类
    private android.os.Handler handler;//上传照片handler
    private String nameStr = "";//配件名称
    private String otherStr = "";//其他输入内容
    private String pinpaiStr = "";//品牌输入内容
    private String pinzhiStr = "";//品质选取
    private String vinNum = ""; // 车架号
    private CheckBox cbYuanChang, cbChaiChe, cbPinPai, cbOther;//原厂 拆车  品牌 其他
    private String fromchat;
    private ChexingItem chexingItem; // 车型
    private String strOemName;
    private String strOemNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_product_purchase_vin);
    }


    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        tvChange = ((TextView) findViewById(R.id.tv_change));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        tvPinPai = ((TextView) findViewById(R.id.tv_pinpai));
        tvCarXi = ((TextView) findViewById(R.id.tv_car_xi));
        tvCarXing = ((TextView) findViewById(R.id.tv_car_xing));
        ivCar = ((ImageView) findViewById(R.id.iv_car));
        etVIN = ((EditText) findViewById(R.id.et_vin));
        etName = ((EditText) findViewById(R.id.et_name));
        etPinPai = ((EditText) findViewById(R.id.et_pinpai));
        etOther = ((EditText) findViewById(R.id.et_other));
        cbYuanChang = ((CheckBox) findViewById(R.id.radioButton1));
        cbChaiChe = ((CheckBox) findViewById(R.id.radioButton2));

        cbPinPai = ((CheckBox) findViewById(R.id.radioButton3));

        cbOther = ((CheckBox) findViewById(R.id.radioButton4));
        mgvPhoto = ((MyGridView) findViewById(R.id.mgv_photo));
        llOther = ((LinearLayout) findViewById(R.id.ll_other));
        llPinPai = ((LinearLayout) findViewById(R.id.ll_pinpai));
        btSure = ((Button) findViewById(R.id.bt_ensure));
        fromchat = getIntent().getStringExtra("fromchat");


    }

    @Override
    protected void initViews() {
        tvTitle.setText("配件求购");
        initIntentData();

        ivBack.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        mAdapter = new EditGoodPhotoUploadAdapter(this);
        mgvPhoto.setAdapter(mAdapter);
        mgvPhoto.setOnItemClickListener(this);
        llOther.setOnClickListener(this);
        btSure.setOnClickListener(this);
        //品牌 checkbox监听
        cbPinPai.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llPinPai.setVisibility(View.VISIBLE);

                } else {
                    llPinPai.setVisibility(View.GONE);

                }
            }
        });
        //其他 checkbox监听
        cbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llOther.setVisibility(View.VISIBLE);

                } else {
                    llOther.setVisibility(View.GONE);

                }
            }
        });
        imgUtils = new ImgUtils(this);

        initHandler();
    }

    /**
     * 初始化数据
     */
    private void initIntentData() {
        Intent intent = getIntent();
        if(intent != null) {
            strOemName = intent.getStringExtra("oemname");
            strOemNum = intent.getStringExtra("oemnum");
            String strVin = intent.getStringExtra("vin");
            if(strVin != null && !"".equals(strVin)) {
                etVIN.setText(strVin);
            }
            chexingItem = (ChexingItem) intent.getSerializableExtra(Constant.CAR_TYPE);
            if(strOemName != null && !strOemName.equals("")) {
                etName.setText(strOemName);
            }
            if(chexingItem != null) {
                tvPinPai.setText("品牌: "+chexingItem.getPinpai());
                tvCarXi.setText("车系: "+chexingItem.getChexi());
                tvCarXing.setText("车型: "+chexingItem.getName());
                etVIN.setText(chexingItem.getVin());

                if(chexingItem.getIcon() !=null && !"".equals(chexingItem.getIcon())) {
                    ivCar.setVisibility(View.VISIBLE);
                    UniversalImageUtils.displayImageUseDefOptions(chexingItem.getIcon(), ivCar);
                }
                else {
                    ivCar.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_change://切换车款
                Intent intentToCar = new Intent(mActivity, ChoseCarActivity.class);
                intentToCar.putExtra("isPurchase", true);
                startActivityForResult(intentToCar, TO_CAR);
                break;
            case R.id.bt_ensure://发布求购
                setPhoto();
                if (checkInput()) {
                    if (isOnline()) {
                        showWait();

                        reqPurchase();
                    } else {
                        ToastUtils.show(ProductPurchaseVINActivity.this, "暂无网络");
                    }
                }
                break;

        }
    }

    /**
     * 获取选取品质
     */
    private void setPinZhi() {
        pinzhiStr = "";
        String[] arr = new String[4];
        if (cbYuanChang.isChecked()) {
            arr[0] = "0";
        } else {
            arr[0] = "";

        }
        if (cbChaiChe.isChecked()) {
            arr[1] = "1";

        } else {
            arr[1] = "";

        }
        if (cbPinPai.isChecked()) {
            arr[2] = "2";

        } else {
            arr[2] = "";

        }
        if (cbOther.isChecked()) {
            arr[3] = "3";

        } else {
            arr[3] = "";

        }
        if (arr[0] != null && !arr[0].equals("")) {
            pinzhiStr = "0";

        }
        if (arr[1] != null && !arr[1].equals("")) {
            if(MyCheck.isEmpty(pinzhiStr)) {
                pinzhiStr += "1";
            }
            else {
                pinzhiStr += ",1";
            }

        }
        if (arr[2] != null && !arr[2].equals("")) {
            if(MyCheck.isEmpty(pinzhiStr)) {
                pinzhiStr += "2";
            }
            else {
                pinzhiStr += ",2";
            }

        }
        if (arr[3] != null && !arr[3].equals("")) {
            if(MyCheck.isEmpty(pinzhiStr)) {
                pinzhiStr += "3";
            }
            else {
                pinzhiStr += ",3";
            }

        }
    }

    /**
     * 获取选择图片
     */
    private void setPhoto() {
        files.clear();
        for (int i = 0; i < mAdapter.getmObjects().size(); i++) {
            if (!mAdapter.getmObjects().get(i).isAdd()) {
                if (!mAdapter.getmObjects().get(i).isNet()) {
                    File imgFile = new File(mAdapter.getmObjects().get(i).getImgUrl());
                    Map<String, File> map = new HashMap<>();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imgName = getFilesDir() + File.separator + i + "IMG_" + timeStamp + "pro" + ".jpg";
                    imgUtils.createNewFile(imgName, imgFile.getPath());
                    map.put(imgName, new File(imgName));
                    files.add(map);
                    Log.e("ffff", files.toString());
                }
            }
        }
    }


    /**
     * 检查输入信息
     *
     * @return
     */
    private boolean checkInput() {

        nameStr = etName.getText().toString().trim();
        otherStr = etOther.getText().toString().trim();
        pinpaiStr = etPinPai.getText().toString().trim();
        vinNum = etVIN.getText().toString().trim();
        setPinZhi();

        if (TextUtils.isEmpty(nameStr)) {
            ToastUtils.show(this, "配件名称不能为空");
            return false;
        }

        if(chexingItem == null) {
            ToastUtils.show(this, "请选择车款");
            return false;
        }

        if(MyCheck.isEmpty(pinzhiStr)) {
            ToastUtils.show(this, "请选择品质范围");
            return false;
        }
        if(!MyCheck.isVin(vinNum)) {
            ToastUtils.show(this, "请填写17位车架号");
            return false;
        }
        return true;
    }

    /**
     * 初始化handler
     */
    private void initHandler() {

        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    loading.dismiss();
                    if (msg.obj != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            int errorcode = jsonObject.getInt("errorcode");


                            Log.e("msg.obj.toString()  ", msg.obj.toString());
                            switch (errorcode) {
                                case 12:
                                    ToastUtils.show(mActivity,"账号异常，请重新登录");
                                    utils.jumpTo(mActivity, LoginActivity.class);
                                    break;
                                case 60:
                                    ToastUtils.show(ProductPurchaseVINActivity.this, "VIN号输入格式有误");
                                    break;
                                case 61:
                                    ToastUtils.show(ProductPurchaseVINActivity.this, "配件名称格式有误");
                                    break;
                                case 63:
                                    ToastUtils.show(ProductPurchaseVINActivity.this, "该配件求购信息已存在");
                                    break;
                                case 0:
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    String id = data.getString("id");
                                    ToastUtils.show(ProductPurchaseVINActivity.this, "配件求购成功");
                                    if (fromchat!=null&&fromchat.equals("fromchat")){
                                        Intent intentBackChat = new Intent();
                                        intentBackChat.putExtra("name", nameStr);
                                        intentBackChat.putExtra("id", id);

                                        intentBackChat.putExtra("nameofcar", chexingItem.getName());
                                        setResult(RESULT_OK, intentBackChat);

                                    }
                                    finish();
                                    break;
                                default:
                                    ToastUtils.show(ProductPurchaseVINActivity.this, "服务器繁忙");
                                    break;
                            }
                        } catch (JSONException e) {

                            ToastUtils.show(ProductPurchaseVINActivity.this, "服务器繁忙");
                            e.printStackTrace();
                        }
                    } else {

                        ToastUtils.show(ProductPurchaseVINActivity.this, "服务器繁忙");
                    }

                }
            }
        };
    }

    /**
     * 请求求购接口
     */
    private void reqPurchase() {
        final Map<String, String> params = utils.getParams(mfu.getBasePostStr() + "*" + Constant.userid + "*" + utils.convertChinese(chexingItem.getPinpai()) + "*" + utils.convertChinese(chexingItem.getChexi()) + "*" + utils.convertChinese(chexingItem.getName()) + "*" + vinNum + "*" + utils.convertChinese(nameStr) + "*" + pinzhiStr + "*" + utils.convertChinese(pinpaiStr) + "*" + utils.convertChinese(otherStr) + "*" + chexingItem.getIcon());

        final String url = getResources().getString(R.string.api_baseurl) + "goods/WantToBuy.php";

        //开启线程 上传图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyFileUpload fileUpload = new MyFileUpload();
                try {
                    String msg = fileUpload.postForm(url, params, files);
                    Log.e("msg. msg ", url+"    555555    "+msg);

                    if (handler != null) {
                        Message message = handler.obtainMessage(1, msg);
                        handler.sendMessage(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("msg. msg ", url + "    99999    " );
//                    if (handler != null) {
//                        Message message = handler.obtainMessage(1, msg);
//                        handler.sendMessage(message);
//                    }
                }
            }
        }).start();


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            jumbToPhotosActivity(position);
        }
        else {
            final int pos = position;
            requestRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}
                    , new PermissionListener() {
                        @Override
                        public void onGranted() {
                            jumbToPhotosActivity(pos);
                        }

                        @Override
                        public void onGranted(List<String> grantedPermission) {
//                            reqDate();


                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
//                            jumbToPhotosActivity(pos);String str = "";
                            String strToast = "你需要开启";
                            int i = 0;
                            for(String str : deniedPermission) {
                                if(i++ != 0) {
                                    strToast += "、";
                                }
                                if(Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(str)) {
                                    strToast += "手机存储";
                                }
                                else if(Manifest.permission.CAMERA.equals(str)) {
                                    strToast += "相机";
                                }
                            }
                            //权限被拒绝
                            EaseDialogUtil.showMsgDialog(mActivity, "提示", strToast+"才能使用此功能", "去设置", "取消", new EnsureCancleInterface() {
                                @Override
                                public void ensure() {
                                    Intent intent= CMethodUtil.getAppDetailSettingIntent(mActivity);
                                    startActivity(intent);
                                }

                                @Override
                                public void cancle() {

                                }
                            });
                        }
                    });
        }
    }

    /**
     * 跳转相册选择
     * @param position
     */
    private void jumbToPhotosActivity(int position) {
        if (mAdapter.getItem(position).isAdd()) {
            Intent intent = new Intent(ProductPurchaseVINActivity.this,
                    PhotosActivity.class);
            intent.putExtra(TreeConstant.ALBUM_SELECTED_NUM,
                    mAdapter.getCount());
            startActivityForResult(intent,
                    ALBUM_CHOOSE_PHOTOS_CODE2);
        } else {
//            minfo.com.albumlibrary.utils.ToastUtils.showShort(ProductPurchaseVINActivity.this, "跳转界面");

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case ALBUM_CHOOSE_PHOTOS_CODE2://选取图片返回处理
                    if (data.getStringArrayListExtra(TreeConstant.ALBUM_CHOOSE_PHOTOS_PATH) != null) {
                        List<String> photos = data
                                .getStringArrayListExtra(TreeConstant.ALBUM_CHOOSE_PHOTOS_PATH);
                        List<PictureItem> list22 = new ArrayList<>();
                        for (String path : photos) {
                            PictureItem issue22 = new PictureItem();
                            LG.i("item2 url=" + path);
                            issue22.setImgUrl(path);
                            issue22.setAdd(false);
                            issue22.setNet(false);
                            list22.add(issue22);
                        }
                        mAdapter.addAll(list22, mAdapter.getCount() - 1);
                        if (mAdapter.getCount() > 8) {
                            mAdapter.remove(mAdapter.getCount() - 1);
                        }

                    }
                    break;
                case TO_CAR://选取车款返回处理
                    Bundle bundle = new Bundle();
                    bundle = data.getBundleExtra("info");
                    if (bundle != null) {
                        chexingItem = (ChexingItem) bundle.getSerializable("car");
                        if (chexingItem != null) {
                            if(chexingItem.getIcon() !=null && !"".equals(chexingItem.getIcon())) {
                                ivCar.setVisibility(View.VISIBLE);
                                UniversalImageUtils.displayImageUseDefOptions(chexingItem.getIcon(), ivCar);
                            }
                            else {
                                ivCar.setVisibility(View.GONE);
                            }

                            tvPinPai.setText("品牌: "+chexingItem.getPinpai());
                            tvCarXi.setText("品牌: "+chexingItem.getChexi());
                            tvCarXing.setText("品牌: "+chexingItem.getName());
                            etVIN.setText(chexingItem.getVin());
                        }
                    }
                    break;
            }

        }

    }
}
