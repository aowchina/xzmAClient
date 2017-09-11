package com.minfo.carrepairseller.activity.personl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.adapter.address.CityAdapter;
import com.minfo.carrepairseller.adapter.address.DistrictAdapter;
import com.minfo.carrepairseller.adapter.address.ProvinceAdapter;
import com.minfo.carrepairseller.dialog.person.PictureDialog;
import com.minfo.carrepairseller.entity.address.City;
import com.minfo.carrepairseller.entity.address.District;
import com.minfo.carrepairseller.entity.address.Province;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.DialogUtil;
import com.minfo.carrepairseller.utils.ImgUtils;
import com.minfo.carrepairseller.utils.MyFileUpload;
import com.minfo.carrepairseller.utils.ToastUtils;
import com.minfo.carrepairseller.widget.CityData;

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
    private android.os.Handler handler;

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
        ivBack.setOnClickListener(this);
        ivFront.setOnClickListener(this);
        rlPhptp.setOnClickListener(this);
        ivRear.setOnClickListener(this);

        ivPerson.setOnClickListener(this);

        ivEvidence.setOnClickListener(this);

        tvApply.setOnClickListener(this);

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
                photoFlag = 1;
                openDialog();

                break;
            case R.id.iv_font://正面照
                photoFlag = 2;

                openDialog();
                break;
            case R.id.iv_rear://背面照
                photoFlag = 3;

                openDialog();

                break;
            case R.id.iv_person://手持身份证
                photoFlag = 4;

                openDialog();

                break;
            case R.id.iv_evidence://营业执照
                photoFlag = 5;

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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "test.jpg")));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                switch (resultCode) {
                    case -1://-1表示拍照成功
                        file = new File(Environment.getExternalStorageDirectory() + "/test.jpg");
                        if (file.exists()) {
                            photoClip(Uri.fromFile(file));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case PHOTO_CLIP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photos = extras.getParcelable("data");
                        String img_path = getFilesDir().getAbsolutePath() + "img.jpg";
                        boolean isScuccess = saveBitmap2file(photos, img_path);

                        if (isScuccess) {
                            setPhoto(photos, img_path);

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
        String imgName = getFilesDir() + File.separator + "IMG_" + timeStamp + "pro" + ".jpg";


        Log.e("imgFile.getPath()", imgName + "        " + imgFile.getPath());

        imgUtils.createNewFile(imgName, imgFile.getPath());
        map.put(imgName, new File(imgName));
        switch (photoFlag) {
            case 1:
                iv_com.setImageBitmap(bitmap);
                files.set(0, map);

                break;
            case 2:
                ivFront.setImageBitmap(bitmap);
                files.set(1, map);

                break;
            case 3:
                ivRear.setImageBitmap(bitmap);
                files.set(2, map);

                break;
            case 4:
                ivPerson.setImageBitmap(bitmap);
                files.set(3, map);

                break;
            case 5:
                ivEvidence.setImageBitmap(bitmap);
                files.set(4, map);

                break;
        }


    }

    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 5);
        intent.putExtra("aspectY", 5);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 160);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
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
}
