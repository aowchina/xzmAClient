package com.minfo.carrepairseller.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.query.ChoseCarActivity;
import com.minfo.carrepairseller.adapter.query.EditGoodPhotoUploadAdapter;
import com.minfo.carrepairseller.entity.query.PictureItem;
import com.minfo.carrepairseller.entity.shop.ProductDetail;
import com.minfo.carrepairseller.entity.shop.ProductDetailAll;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ImgUtils;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.MyFileUpload;
import com.minfo.carrepairseller.utils.ToastUtils;

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

/**
 * 配件发布
 */
public class PartsPublishActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack; // 返回按钮
    private TextView tvTitle, tvCar, tvKindName; // 标题

    private LinearLayout llCategory, llCar; // llCategory  选择分类  llCar  选择车型
    private EditText etCode; // 产品编码
    private EditText etPrice; // 编辑价格
    private EditText etPhone; // 编辑电话
    private EditText etInfo; // 编辑产品详情

    private EditText etname; // 配件名称

    private Button btEnsure; // 确认发布按钮
    private ImageView ivPhoto;

    private final int toCarChose = 1;//选择车型
    private final int toPhotoChose = 2;//选取上传图片
    private final int toKindChose = 3;//选择配件类别
    private String catID = "";//已选车型id
    private String count = "";//已选车型数量
    private String kindID = "";//已选配件类别id
    private String name = "";//已选配件类别名称
    private List<PictureItem> selectPhoto = new ArrayList<>();//已选配件图片路径集合
    private String oemStr;//oem号
    private String priceStr;//价格
    private String phoneStr;//手机号
    private String infoStr;//描述信息

    private String nameStr;//配件名称信息

    private List<Map<String, File>> files = new ArrayList<>();
    private ImgUtils imgUtils;
    private android.os.Handler handler;
    private String idProduct = "";//上传  编辑 标志
    private ProductDetail productDetail;
    private ArrayList<String> imgs = new ArrayList<>();
    private String deleteImgs = "";
    private android.os.Handler changeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        idProduct = getIntent().getStringExtra("id");
        if(savedInstanceState != null) {
            idProduct = savedInstanceState.getString("id");
        }
        super.onCreate(savedInstanceState, R.layout.activity_parts_publish);
//        setContentView(R.layout.activity_parts_publish);

    }

    @Override
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCar = (TextView) findViewById(R.id.tv_car_kind);
        tvKindName = (TextView) findViewById(R.id.tv_kind_name);
        llCar = (LinearLayout) findViewById(R.id.ll_choose_car);
        llCategory = (LinearLayout) findViewById(R.id.ll_choose_category);
        etCode = (EditText) findViewById(R.id.et_code);
        etname = (EditText) findViewById(R.id.et_name);

        etPrice = (EditText) findViewById(R.id.et_price);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etInfo = (EditText) findViewById(R.id.et_info);
        ivPhoto = ((ImageView) findViewById(R.id.iv_photo));

        btEnsure = (Button) findViewById(R.id.bt_ensure);
    }

    @Override
    protected void initViews() {
        tvTitle.setText("发布配件");
        ivBack.setOnClickListener(this);
        btEnsure.setOnClickListener(this);
        llCategory.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        llCar.setOnClickListener(this);
        imgUtils = new ImgUtils(this);

        initHandler();
        initChangeHandler();



        if (idProduct != null && !idProduct.equals("")) {
            Log.e("idProduct",idProduct);
            if (isOnline()) {

                showWait();
                reqDetails();

            } else {
                ToastUtils.show(this, "暂无网络,请重新连接");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_ensure://配件发布
                if (idProduct != null && !productDetail.equals("")) {//编辑配件
                    ArrayList<String> imgs = EditGoodPhotoUploadAdapter.imgs;
                    List<String> tempList = new ArrayList();
                    for (int i = 0; i < imgs.size(); i++) {
                        int length = getResources().getString(R.string.img_baseurl).length();

                        tempList.add(imgs.get(i).substring(length));

                    }
                    for (int i = 0; i < tempList.size(); i++) {
                        deleteImgs += tempList.get(i);
                        if (i < tempList.size() - 1) {
                            deleteImgs += ",";
                        }
                    }
                    if (checkInput()) {
                        if (isOnline()) {
                            showWait();
                            reqChange();
                        } else {
                            ToastUtils.show(PartsPublishActivity.this, "暂无网络");
                        }
                    }


                } else {//上传配件
                    if (checkInput()) {
                        if (isOnline()) {
                            showWait();
                            reqUpLoad();
                        } else {
                            ToastUtils.show(PartsPublishActivity.this, "暂无网络");
                        }
                    }
                }


                break;
            case R.id.ll_choose_category://跳到配件类别选择界面
                Intent intentToKind = new Intent(PartsPublishActivity.this, KindOfPeiJianActivity.class);
                startActivityForResult(intentToKind, toKindChose);

                break;
            case R.id.iv_photo://跳到配件图片选择界面

                Intent intentToPhoto = new Intent(PartsPublishActivity.this, ChosePhotoActivity.class);
                intentToPhoto.putStringArrayListExtra("img", imgs);
                startActivityForResult(intentToPhoto, toPhotoChose);


                break;
            case R.id.ll_choose_car://跳到车型选择选择界面
                Intent intentToCar = new Intent(mActivity, ChoseCarActivity.class);
                intentToCar.putExtra("isDuo", true);
                startActivityForResult(intentToCar, toCarChose);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case toCarChose://选择车型
                    catID = data.getStringExtra("id");
                    count = data.getStringExtra("count");
                    tvCar.setText("已选" + count + "款车型");
                    break;
                case toKindChose://选择配件类别
                    kindID = data.getStringExtra("id");
                    name = data.getStringExtra("name");
                    tvKindName.setText(name);
                    break;
                case toPhotoChose://选取上传图片
                    selectPhoto = (List<PictureItem>) data.getSerializableExtra("photo");
                    if (selectPhoto != null) {
                        for (int i = 0; i < selectPhoto.size(); i++) {//循环处理图片
                            File imgFile = new File(selectPhoto.get(i).getImgUrl());

                            Map<String, File> map = new HashMap<>();
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String imgName = getFilesDir() + File.separator + "IMG_" + timeStamp + "pro" + ".jpg";
                            imgUtils.createNewFile(imgName, imgFile.getPath());
                            map.put(imgName, new File(imgName));
                            files.add(map);
                        }

                    }

                    break;
            }
        }
    }

    /**
     * 检查输入信息
     *
     * @return
     */
    private boolean checkInput() {

        oemStr = etCode.getText().toString().trim();
        priceStr = etPrice.getText().toString().trim();
        phoneStr = etPhone.getText().toString().trim();
        infoStr = etInfo.getText().toString().trim();

        nameStr = etname.getText().toString().trim();
        if (TextUtils.isEmpty(nameStr)) {
            ToastUtils.show(this, "配件名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(oemStr)) {
            ToastUtils.show(this, "OEM不能为空");
            return false;
        }


        if (TextUtils.isEmpty(priceStr)) {
            ToastUtils.show(this, "价格不能为空");
            return false;
        }

        if (TextUtils.isEmpty(phoneStr)) {
            ToastUtils.show(this, "手机号不能为空");
            return false;
        }
        if (!MyCheck.isTel(phoneStr)) {
            ToastUtils.show(this, "手机号格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(infoStr)) {
            ToastUtils.show(this, "描述信息不能为空");
            return false;
        }
        if (files == null || files.size() == 0) {
            ToastUtils.show(this, "图片不能为空");
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
                            switch (errorcode) {
                                case 49:
                                    ToastUtils.show(PartsPublishActivity.this, "配件名称不能为空");
                                    break;
                                case 50:
                                    ToastUtils.show(PartsPublishActivity.this, "车款选择失败，请重新操作");
                                    break;
                                case 51:
                                    ToastUtils.show(PartsPublishActivity.this, "配件类别选择失败，请重新操作");
                                    break;
                                case 52:
                                    ToastUtils.show(PartsPublishActivity.this, "OEM号输入格式有误");
                                    break;
                                case 53:
                                    ToastUtils.show(PartsPublishActivity.this, "价格格式有误");
                                    break;
                                case 54:
                                    ToastUtils.show(PartsPublishActivity.this, "手机号格式有误");
                                    break;
                                case 55:
                                    ToastUtils.show(PartsPublishActivity.this, "描述内容不能为空");
                                    break;
                                case 56:
                                    ToastUtils.show(PartsPublishActivity.this, "认证未通过");
                                    break;
                                case 0:
                                    ToastUtils.show(PartsPublishActivity.this, "上传配件成功");
                                    break;
                                default:
                                    ToastUtils.show(PartsPublishActivity.this, "服务器繁忙");
                                    break;
                            }
                        } catch (JSONException e) {

                            ToastUtils.show(PartsPublishActivity.this, "服务器繁忙");
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(PartsPublishActivity.this, "服务器繁忙");
                    }

                }
            }
        };
    }

    /**
     * 请求上传配件接口
     */
    private void reqUpLoad() {
        final Map<String, String> params = utils.getParams(mfu.getBasePostStr() + "*" + Constant.userid + "*" + catID + "*" + kindID + "*" + oemStr + "*" + priceStr + "*" + phoneStr + "*" + utils.convertChinese(infoStr) + "*" + utils.convertChinese(nameStr));
        final String url = getResources().getString(R.string.api_baseurl) + "goods/Add.php";
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyFileUpload fileUpload = new MyFileUpload();
                try {
                    String msg = fileUpload.postForm(url, params, files);

                    if (handler != null) {
                        Message message = handler.obtainMessage(1, msg);
                        handler.sendMessage(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    /**
     * 请求商品详情接口
     */
    private void reqDetails() {
        String url = getResources().getString(R.string.api_baseurl) + "goods/Detail.php";

        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid + "*" + idProduct);
        Log.e(TAG, params.toString());

        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();

                if (response != null && !response.equals("")) {
                    Log.e("response      bianji", response.getData());
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
            }
        });
    }

    //详情数据
    private void bindData() {


        etname.setText(productDetail.getName());
        etPrice.setText(productDetail.getPrice());
        etInfo.setText(productDetail.getDetail());

        tvCar.setText(productDetail.getCar_name());

        tvKindName.setText(productDetail.getTname());
        etPhone.setText(productDetail.getTel());
        etCode.setText(productDetail.getOem());

        if (productDetail.getImg() != null && productDetail.getImg().size() > 0) {
            for (int i = 0; i < productDetail.getImg().size(); i++) {
                Log.e("bianji  ",productDetail.getImg().get(i));
                imgs.add(getResources().getString(R.string.img_baseurl) + productDetail.getImg().get(i));
            }
        }


    }
    /**
     * 初始化编辑handler
     */
    private void initChangeHandler() {

        changeHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    loading.dismiss();
                    if (msg.obj != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            int errorcode = jsonObject.getInt("errorcode");
                            Log.e("errorcode     nbianji ",errorcode+"");
                            switch (errorcode) {
                                case 49:
                                    ToastUtils.show(PartsPublishActivity.this, "配件名称不能为空");
                                    break;
                                case 50:
                                    ToastUtils.show(PartsPublishActivity.this, "车款选择失败，请重新操作");
                                    break;
                                case 51:
                                    ToastUtils.show(PartsPublishActivity.this, "配件类别选择失败，请重新操作");
                                    break;
                                case 52:
                                    ToastUtils.show(PartsPublishActivity.this, "OEM号输入格式有误");
                                    break;
                                case 53:
                                    ToastUtils.show(PartsPublishActivity.this, "价格格式有误");
                                    break;
                                case 54:
                                    ToastUtils.show(PartsPublishActivity.this, "手机号格式有误");
                                    break;
                                case 55:
                                    ToastUtils.show(PartsPublishActivity.this, "描述内容不能为空");
                                    break;
                                case 56:
                                    ToastUtils.show(PartsPublishActivity.this, "认证未通过");
                                    break;
                                case 60:
                                    ToastUtils.show(PartsPublishActivity.this, "该商品不存在");
                                    break;
                                case 0:
                                    ToastUtils.show(PartsPublishActivity.this, "编辑配件成功");
                                    break;
                                default:
                                    ToastUtils.show(PartsPublishActivity.this, "服务器繁忙");
                                    break;
                            }
                        } catch (JSONException e) {

                            ToastUtils.show(PartsPublishActivity.this, "服务器繁忙");
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(PartsPublishActivity.this, "服务器繁忙");
                    }

                }
            }
        };
    }

    /**
     * 请求编辑配件接口
     */
    private void reqChange() {
        final Map<String, String> params = utils.getParams(mfu.getBasePostStr() + "*" + Constant.userid + "*" + catID + "*" + kindID + "*" + oemStr + "*" + priceStr + "*" + phoneStr + "*" + utils.convertChinese(infoStr) + "*" + utils.convertChinese(nameStr) + "*" + idProduct + "*" +deleteImgs);

        final String url = getResources().getString(R.string.api_baseurl) + "goods/Update.php";


        new Thread(new Runnable() {
            @Override
            public void run() {
                MyFileUpload fileUpload = new MyFileUpload();
                try {
                    String msg = fileUpload.postForm(url, params, files);

                    if (changeHandler != null) {
                        Message message = changeHandler.obtainMessage(1, msg);
                        changeHandler.sendMessage(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putString("id", idProduct);
        }
    }
}
