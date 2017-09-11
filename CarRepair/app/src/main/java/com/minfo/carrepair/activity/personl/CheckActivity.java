package com.minfo.carrepair.activity.personl;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hyphenate.easeui.utils.CMethodUtil;
import com.hyphenate.easeui.utils.EaseDialogUtil;
import com.hyphenate.easeui.utils.EnsureCancleInterface;
import com.minfo.carrepair.CommonInterface.PermissionListener;
import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.login.LoginActivity;
import com.minfo.carrepair.adapter.address.CityAdapter;
import com.minfo.carrepair.adapter.address.DistrictAdapter;
import com.minfo.carrepair.adapter.address.ProvinceAdapter;
import com.minfo.carrepair.dialog.person.PictureDialog;
import com.minfo.carrepair.entity.address.City;
import com.minfo.carrepair.entity.address.District;
import com.minfo.carrepair.entity.address.Province;
import com.minfo.carrepair.entity.personl.AuthInfo;
import com.minfo.carrepair.http.BaseResponse;
import com.minfo.carrepair.http.RequestListener;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.ImgUtils;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.MyCheck;
import com.minfo.carrepair.utils.MyFileUpload;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;
import com.minfo.carrepair.widget.CityData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import minfo.com.albumlibrary.utils.CameraUtils;

/**
 * 实名认证
 */
public class CheckActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvTitle;
    private EditText etCompany, etName, etRange, etNumber, etMajor;
    private ImageView ivFront, ivRear, ivPerson, ivEvidence, iv_com;

    //相机常量
    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;
    //上传头像变量
    File file;
    private List<Map<String, File>> files = new ArrayList<>();
    private Bitmap photos;
    private File imgFile;
    private String head_img_path;
    private ImgUtils imgUtils;
    private TextView tvApply;

    private String ProvinceId = "0";
    private String CityId = "0";
    private String DistrictId = "0";
    private ProvinceAdapter padapter;
    private CityAdapter cadapter;
    private DistrictAdapter dadapter;
    private List<Province> provinceList;
    private int provincePos;
    private int cityPos;
    private Spinner province;
    private Spinner city;
    private Spinner district;
    private String strAddress;
    private String pid;
    private String cid;
    private String qid;
    private List<City> cityList;
    private List<District> districtList;
    List<City> tempCity;
    List<District> tempDistrict;
    private int proPos;
    private int disPos;
    private boolean firstProvince = true, firstCity = true, firstDistrict = true;
    private String usernameStr, companyStr, rangeStr, numberStr, majorStr;
    private RelativeLayout rlPhptp;
    private int photoFlag = 0;
    private final static int FLAG_TOXIANG = 1; // 头像
    private final static int FLAG_ZHENGMIAN = 2; // 身份证正面
    private final static int FLAG_FANMIAN = 3; // 身份证反面
    private final static int FLAG_SHOUCHI = 4; // 手持身份证
    private final static int FLAG_YINGYEZHAO = 5; // 营业执照
    private android.os.Handler handler;
    private View view;
    private Map<String, String> mapPath = new HashMap<>();
    private String[] imgNames = {"touxiang","shenfenzhengz","shenfenzhengf","shouchi","yinyezhao"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_check);
    }

    @Override
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        tvApply = ((TextView) findViewById(R.id.tv_apply));
        etCompany = ((EditText) findViewById(R.id.et_name_company));
        etName = ((EditText) findViewById(R.id.et_name));
        etRange = ((EditText) findViewById(R.id.et_range));
        etNumber = ((EditText) findViewById(R.id.et_number));
        etMajor = ((EditText) findViewById(R.id.et_major));
        rlPhptp = ((RelativeLayout) findViewById(R.id.rl_photo));
        ivFront = ((ImageView) findViewById(R.id.iv_font));
        ivRear = ((ImageView) findViewById(R.id.iv_rear));
        ivPerson = ((ImageView) findViewById(R.id.iv_person));
        ivEvidence = ((ImageView) findViewById(R.id.iv_evidence));
        iv_com = ((ImageView) findViewById(R.id.iv_com));

        province = ((Spinner) findViewById(R.id.province));
        city = ((Spinner) findViewById(R.id.city));
        district = ((Spinner) findViewById(R.id.district));
        view = ((View) findViewById(R.id.view));
        ivBack.setOnClickListener(this);
        ivFront.setOnClickListener(this);
        rlPhptp.setOnClickListener(this);
        ivRear.setOnClickListener(this);

        ivPerson.setOnClickListener(this);

        ivEvidence.setOnClickListener(this);

        tvApply.setOnClickListener(this);
        iv_com.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        imgUtils = new ImgUtils(this);
        files.add(new HashMap<String, File>());
        files.add(new HashMap<String, File>());
        files.add(new HashMap<String, File>());
        files.add(new HashMap<String, File>());
        files.add(new HashMap<String, File>());
        initHandler();
        if (isOnline()) {
            //showWait();
            reqData();
        } else {
            ToastUtils.show(this, "暂无网络");
        }
        tvTitle.setText("认证");
        //城市选择部分
        provinceList = CityData.readProvinces(this);//获取本地数据
        province.setPrompt("省");
        city.setPrompt("城市");
        district.setPrompt("地区");


        padapter = new ProvinceAdapter(this, provinceList);
        province.setAdapter(padapter);

        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initCity(position);
                provincePos = position;
                ProvinceId = provinceList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City city = provinceList.get(provincePos).getCity().get(position);
                cityPos = position;
                if (city.getArea() != null) {
                    initDistrict(city.getArea());
                    CityId = city.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                District district = provinceList.get(provincePos).getCity().get(cityPos).getArea().get(position);
                DistrictId = district.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * 检查输入信息
     *
     * @return
     */
    private boolean checkInput() {


        usernameStr = etName.getText().toString().trim();
        companyStr = etCompany.getText().toString().trim();
        rangeStr = etRange.getText().toString().trim();
        numberStr = etNumber.getText().toString().trim();
        majorStr = etMajor.getText().toString().trim();
        if (TextUtils.isEmpty(usernameStr)) {
            ToastUtils.show(this, "公司名称不能为空");
            return false;
        }


        if (TextUtils.isEmpty(companyStr)) {
            ToastUtils.show(this, "姓名不能为空");
            return false;
        }

        if (TextUtils.isEmpty(rangeStr)) {
            ToastUtils.show(this, "经营范围不能为空");
            return false;
        }
        if (TextUtils.isEmpty(numberStr)) {
            ToastUtils.show(this, "专长不能为空");
            return false;
        }
        for(int i = 0; i < 5; i++) {
            String path = mapPath.get(imgNames[i]);
            if(MyCheck.isEmpty(path)) {
                switch (i) {
                    case 0:
                        ToastUtils.show(this, "请选择上传的头像图片");
                        break;
                    case 1:
                        ToastUtils.show(this, "请选择身份证正面图片");
                        break;
                    case 2:
                        ToastUtils.show(this, "请选择身份证反面图片");
                        break;
                    case 3:
                        ToastUtils.show(this, "请选择手持身份证图片");
                        break;
                    case 4:
                        ToastUtils.show(this, "请选择营业执照图片");
                        break;
                }
                return false;
            }
        }
        return true;
    }

    //城市初始化

    public void initCity(int position) {
        cadapter = new CityAdapter(this, provinceList.get(position).getCity());
        city.setAdapter(cadapter);
        if (firstCity) {
            firstCity = false;
            city.setSelection(cityPos, true);
        }
    }

    //区初始化
    public void initDistrict(List<District> d) {
        dadapter = new DistrictAdapter(this, d);
        district.setAdapter(dadapter);
        if (firstDistrict) {
            firstDistrict = false;
            district.setSelection(disPos, true);
        }
    }

    /**
     * 弹出相册 照相机 选择
     */
    private void openDialog() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            openPhotoChoose();
        }
        else {
            requestRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}
                    , new PermissionListener() {
                        @Override
                        public void onGranted() {
                            openPhotoChoose();
                        }

                        @Override
                        public void onGranted(List<String> grantedPermission) {
//                            reqDate();

                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {

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
     * 图片选择
     */
    private void openPhotoChoose() {
        PictureDialog pictureDialog = new PictureDialog(this);
        pictureDialog.setOnChooseListener(new PictureDialog.OnChooseListener() {
            @Override
            public void choose(int flag) {
                switch (flag) {
                    case PictureDialog.FLAG_ALBUME:
                        getPicFromPhoto();
//
                        break;
                    case PictureDialog.FLAG_CAMARE:
                        callCamera();
//
                        break;

                }
            }
        });
        DialogUtil.showDialog(this, pictureDialog).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.rl_photo://头像
                photoFlag = FLAG_TOXIANG;
                openDialog();

                break;
            case R.id.iv_font://正面照
                photoFlag = FLAG_ZHENGMIAN;

                openDialog();
                break;
            case R.id.iv_rear://背面照
                photoFlag = FLAG_FANMIAN;

                openDialog();

                break;
            case R.id.iv_person://手持身份证
                photoFlag = FLAG_SHOUCHI;

                openDialog();

                break;
            case R.id.iv_evidence://营业执照
                photoFlag = FLAG_YINGYEZHAO;

                openDialog();

                break;
            case R.id.tv_apply:
                if (checkInput()) {
                    if (isOnline()) {
                        showWait();
                        reqApply();
                    } else {
                        ToastUtils.show(CheckActivity.this, "暂无网络");
                    }
                }
                break;
            case R.id.iv_com:
                photoFlag = FLAG_TOXIANG;
                openDialog();
                break;

        }

    }

    /**
     * 调用系统相册
     */
    private void getPicFromPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    /**
     * 调用拍照功能
     */
    public void callCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "test.jpg")));
        Intent intent = CameraUtils.takePicture(mActivity);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                switch (resultCode) {
                    case -1://-1表示拍照成功
//                        file = new File(Environment.getExternalStorageDirectory() + "/test.jpg");
//                        if (file.exists()) {
//                            photoClip(Uri.fromFile(file));
//                        }
                        if(CameraUtils.mCurrentFile != null) {
                            photoClip(Uri.fromFile(CameraUtils.mCurrentFile));
                        }
                        else {
                            showWait();
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        CameraUtils.mCurrentFile = new File(CameraUtils.mCurrentPhotoPath);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        cameraHandler.sendEmptyMessage(1204);

                                    }

                                    cameraHandler.sendEmptyMessage(1204);
                                }
                            });
                        }
                        break;
                    default:
                        break;
                }
                break;
            case PHOTO_REQUEST:
                if (data != null) {
//                    photoClip(data.getData());
                    photoClip(CameraUtils.geturi(mActivity,data));
                }
                break;
            case PHOTO_CLIP:
                if(resultCode == RESULT_OK) {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
//                        Bitmap photos = extras.getParcelable("data");
//                        String img_path = getFilesDir().getAbsolutePath() + "img.jpg";
//                        boolean isScuccess = saveBitmap2file(photos, img_path);
//
//                        if (isScuccess) {
//                            setPhoto(photos, img_path);
//
//                        }
                            Bitmap photos = BitmapFactory.decodeFile(CameraUtils.mCurrentPhotoPath);
                            setPhoto(photos, CameraUtils.mCurrentPhotoPath);
                        }
                    }
                }
                break;
            default:
                break;
        }

    }

        /**
         * 设置图片
         */
    private void setPhoto(Bitmap bitmap, String img_path) {
        File imgFile = new File(img_path);

        Map<String, File> map = new HashMap<>();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgName = getFilesDir() + File.separator + photoFlag+"IMG_" + timeStamp + "pro" + ".jpg";


        Log.e("imgFile.getPath()", imgName + "        " + imgFile.getPath());

        imgUtils.createNewFile(imgName, imgFile.getPath());
        map.put(imgName, new File(imgName));
        switch (photoFlag) {
            case FLAG_TOXIANG:
                iv_com.setImageBitmap(bitmap);
                files.set(0, map);
                mapPath.put(imgNames[0],imgName);
                break;
            case FLAG_ZHENGMIAN:
                ivFront.setImageBitmap(bitmap);
                files.set(1, map);
                mapPath.put(imgNames[1],imgName);
                break;
            case FLAG_FANMIAN:
                ivRear.setImageBitmap(bitmap);
                files.set(2, map);
                mapPath.put(imgNames[2],imgName);
                break;
            case FLAG_SHOUCHI:
                ivPerson.setImageBitmap(bitmap);
                files.set(3, map);
                mapPath.put(imgNames[3],imgName);
                break;
            case FLAG_YINGYEZHAO:
                ivEvidence.setImageBitmap(bitmap);
                files.set(4, map);
                mapPath.put(imgNames[4],imgName);
                break;
        }


    }

    private void photoClip(Uri uri) {
        // 身份证85.6 54
        int width = utils.getScreenWidth();
        int height = utils.getScreenHeight();
        int aspectX = width;
        int aspectY = width;
        int outputX = width/2;
        int outputY = width/2;

        switch (photoFlag) {
            case FLAG_TOXIANG:
                break;
            case FLAG_ZHENGMIAN:
            case FLAG_FANMIAN:
                aspectX = width;
                aspectY = (int)(width/86.0*54);
                outputX = aspectX;
                outputY = aspectY;
                break;
            case FLAG_SHOUCHI:
                aspectX = width;
                aspectY = (int)(width*3.0/4);
                outputX = width;
                outputY = (int)(width*3.0/4);
                break;
            case FLAG_YINGYEZHAO:
                aspectX = width;
                aspectY = height;
                outputX = width /2;
                outputY = height /2;
                break;
        }

//        // 调用系统中自带的图片剪裁
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
////        intent.putExtra("circleCrop", "false");
//
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
//        intent.putExtra("return-data", true);
        Intent intent = CameraUtils.cropPicture(mActivity, uri, aspectX, aspectY);
        startActivityForResult(intent, PHOTO_CLIP);
    }

    private static boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
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
                            Log.e("errorcode", "errorcode  " + errorcode);
                            switch (errorcode) {
                                case 13:
                                    ToastUtils.show(CheckActivity.this, "证件号格式有误");
                                    break;
                                case 16:
                                    ToastUtils.show(CheckActivity.this, "用户不存在");
                                    break;
                                case 36:
                                    ToastUtils.show(CheckActivity.this, "姓名格式有误");

                                case 37:
                                    ToastUtils.show(CheckActivity.this, "公司名称格式有误");
                                    break;
                                case 38:
                                    ToastUtils.show(CheckActivity.this, "经营范围格式有误");
                                    break;
                                case 39:
                                    ToastUtils.show(CheckActivity.this, "专长成功");
                                    break;
                                case 0:
                                    ToastUtils.show(CheckActivity.this, "认证成功,等待审核");
                                    break;
                                default:
                                    ToastUtils.show(CheckActivity.this, "服务器繁忙");
                                    break;
                            }
                        } catch (JSONException e) {

                            ToastUtils.show(CheckActivity.this, "服务器繁忙");
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(CheckActivity.this, "服务器繁忙");
                    }

                }
            }
        };
    }

    /**
     * 请求认证接口
     */
    private void reqApply() {
        final Map<String, String> params = utils.getParams(mfu.getBasePostStr() + "*" + Constant.userid + "*" + utils.convertChinese(companyStr) + "*" + utils.convertChinese(usernameStr) + "*" + numberStr + "*" + ProvinceId + "*" + CityId + "*" + DistrictId + "*" + utils.convertChinese(rangeStr) + "*" + utils.convertChinese(majorStr));

        final String url = getResources().getString(R.string.api_baseurl) + "user/Auth.php";


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
    private AuthInfo authInfo;
    /**
     * 请求认证数据
     * 上行参数:
     */
    private void reqData() {

        String url = getResources().getString(R.string.api_baseurl) +"user/AuthInfo.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.userid  );
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {
                showWait();
            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if (response != null) {
                    if (response.equals("")){
                        tvApply.setText("确定申请");
                    }else {
                        authInfo=response.getObj(AuthInfo.class);
                        if (authInfo!=null){
                            bindData();
                        }
                    }


                }
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();

                int errorcode = response.getErrorcode();
                // 11，12,，28,53
                switch (errorcode) {
                    case 8:
                        LG.e("认证", "errorcode=" + errorcode);
                        break;
                    case 11:
                    case 12:
                        ToastUtils.show(mActivity, "账号异常，请重新登录");
                        utils.jumpTo(mActivity, LoginActivity.class);
                        break;
                    case 53:
                        ToastUtils.show(mActivity, "您没有账单信息");

                        break;
                    case 27:
                    case 28:
                    default:
                        ToastUtils.show(mActivity, "服务器繁忙");
                        LG.e("认证", "errorcode=" + errorcode);
                        break;
                }

            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();

                ToastUtils.show(mActivity, "连接服务器失败，请检查你的网络");
            }
        });
    }

    /**
     * 绑定数据
     */
    private void bindData(){

        if ("0".equals(authInfo.getState())){//审核中
            tvApply.setText("审核中");
            setLocalData();
            setData();
            tvApply.setClickable(false);
        }
        else if ("2".equals(authInfo.getState())){//审核失败
            tvApply.setText("重新申请");
        }
        else if ("1".equals(authInfo.getState())){//审核通过
            tvApply.setText("确定申请");
            setLocalData();
            view.setVisibility(View.GONE);
            setData();
            tvApply.setVisibility(View.GONE);
        }

    }

    private void setData(){
        ivFront.setClickable(false);
        rlPhptp.setClickable(false);
        ivPerson.setClickable(false);
        ivEvidence.setClickable(false);
        ivRear.setClickable(false);
        tvApply.setClickable(false);
        iv_com.setClickable(false);
        ivFront.setClickable(false);
        ivFront.setClickable(false);

        etCompany.setText(authInfo.getCompany());
        etName.setText(authInfo.getSname());
        etRange.setText(authInfo.getMajor());
        etNumber.setText(authInfo.getNumber());
        etMajor.setText(authInfo.getSkill());

        etCompany.setEnabled(false);
        etName.setEnabled(false);
        etRange.setEnabled(false);
        etNumber.setEnabled(false);
        etMajor.setEnabled(false);
        UniversalImageUtils.removeFromCache(authInfo.getPicture());
        UniversalImageUtils.removeFromCache(authInfo.getCardfront());
        UniversalImageUtils.removeFromCache(authInfo.getCardback());
        UniversalImageUtils.removeFromCache(authInfo.getCardhand());
        UniversalImageUtils.removeFromCache(authInfo.getLicense());
        UniversalImageUtils.displayImageUseDefOptions(authInfo.getPicture(), iv_com);
        UniversalImageUtils.displayImageUseDefOptions(authInfo.getCardfront(),ivFront);
        UniversalImageUtils.displayImageUseDefOptions(authInfo.getCardback(), ivRear);
        UniversalImageUtils.displayImageUseDefOptions(authInfo.getCardhand(), ivPerson);
        UniversalImageUtils.displayImageUseDefOptions(authInfo.getLicense(), ivEvidence);


    }
    //填充地区接收数据
    private void setLocalData() {

        pid = authInfo.getPid()+"";
        cid = authInfo.getCid()+"";
        qid = authInfo.getQid()+"";
//        addressStr = getIntent().getStringExtra("addressid");
//        address = getIntent().getStringExtra("address");
//        phone1 = getIntent().getStringExtra("phone");
//        name = getIntent().getStringExtra("name");
//        pid = getIntent().getStringExtra("pid");
//        cid = getIntent().getStringExtra("cid");
//        qid = getIntent().getStringExtra("qid");
//            tvLocal.setText("编辑收货地址");
            proPos = 0;
            //匹配省id
            for (int i = 0; i < provinceList.size(); i++) {
                if (provinceList.get(i).getId().equals(pid)) {
                    proPos = i;
                }
            }
            if (firstProvince) {
                firstProvince = false;
                province.setSelection(proPos, true);
            }
            cityPos = 0;
            //匹配市id

            tempCity = provinceList.get(proPos).getCity();

            if (tempCity != null) {
                for (int i = 0; i < tempCity.size(); i++) {
                    if (tempCity.get(i).getId().equals(cid)) {
                        cityPos = i;
                    }
                }
            }
//
            tempDistrict = tempCity.get(cityPos).getArea();
            disPos = 0;
            //匹配地区id
            if (tempDistrict != null) {
                for (int i = 0; i < tempDistrict.size(); i++) {
                    if (tempDistrict.get(i).getId().equals(qid)) {
                        disPos = i;
                    }
                }

            }
//
        province.setEnabled(false);
        city.setEnabled(false);
        district.setEnabled(false);
    }

    private Handler cameraHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1204:
                    cleanWait();
                    if(CameraUtils.mCurrentFile != null) {
                        photoClip(Uri.fromFile(CameraUtils.mCurrentFile));
                    } else {
                        ToastUtils.show(mActivity, "获取图片失败");
                    }
                    break;
            }
        }
    };
}
