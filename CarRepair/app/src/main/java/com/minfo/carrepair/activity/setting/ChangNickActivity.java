package com.minfo.carrepair.activity.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.utils.CMethodUtil;
import com.hyphenate.easeui.utils.EaseDialogUtil;
import com.hyphenate.easeui.utils.EnsureCancleInterface;
import com.minfo.carrepair.CommonInterface.PermissionListener;
import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.dialog.person.PictureDialog;
import com.minfo.carrepair.entity.personl.User;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.DialogUtil;
import com.minfo.carrepair.utils.ImgUtils;
import com.minfo.carrepair.utils.MyCheck;
import com.minfo.carrepair.utils.MyFileUpload;
import com.minfo.carrepair.utils.ToastUtils;
import com.minfo.carrepair.utils.UniversalImageUtils;

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

public class ChangNickActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack, ivPhoto; // 返回
    private TextView tvTitle; // 标题

    private Button btEnsure; // 确定按钮
    private EditText etNick;
    private String nickStr;
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
    private android.os.Handler handler;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            user = (User) savedInstanceState.getSerializable("data");
        }
        else {
            Bundle bundle;
            bundle = getIntent().getBundleExtra("info");
            if (bundle != null) {
                user = (User) bundle.getSerializable("data");
            }
        }
        super.onCreate(savedInstanceState, R.layout.activity_chang_nick);
//        setContentView(R.layout.activity_setting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_photo:
                openDialog();
                break;
            case R.id.bt_ensure:
                nickStr = etNick.getText().toString().trim();

                if (isOnline()) {
                    if (checkInput()) {
                        showWait();
                        reqChangeInfo();
                    }
                } else {
                    ToastUtils.show(ChangNickActivity.this, "暂无网络");
                }

                break;
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
            requestRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}
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
//                            jumbToPhotosActivity(pos);
//                            if(deniedPermission.size() == 2) {
//                                ToastUtils.show(mActivity, "您需要开启拍照和读取相册的权限");
//                            }
//                            else if(deniedPermission.get(0).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                                ToastUtils.show(mActivity, "您需要开启读取相册的权限");
//                            }else if(deniedPermission.get(0).equals(Manifest.permission.CAMERA)) {
//                                ToastUtils.show(mActivity, "您需要开启拍照权限");
//                            }
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
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        btEnsure = (Button) findViewById(R.id.bt_ensure);
        etNick = ((EditText) findViewById(R.id.et_nick));

        if (user != null) {
            /**
             * 填充信息
             */
            etNick.setText(user.getName());
//                UniversalImageUtils.displayImageUseDefOptions(user.getImg(), ivPhoto);
            UniversalImageUtils.displayImageCircle(user.getImg(), ivPhoto, 240);


        }


    }

    @Override
    protected void initViews() {
        tvTitle.setText("修改个人信息");
        ivBack.setOnClickListener(this);
        btEnsure.setOnClickListener(this);
        imgUtils = new ImgUtils(this);
        ivPhoto.setOnClickListener(this);

        initHandler();


    }

    /**
     * 输入的基本验证
     */
    private boolean checkInput() {

        if (TextUtils.isEmpty(nickStr)) {
            ToastUtils.show(this, "请输入昵称");
            return false;
        }

//        if (!MyCheck.isNiCheng(nickStr)) {
//            ToastUtils.show(this, "输入的昵称格式不正确");
//            return false;
//        }

        return true;
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
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(CameraUtils.mCurrentFile));
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
//                        if(CameraUtils.mCurrentFile != null) {
//                            photoClip(Uri.fromFile(CameraUtils.mCurrentFile));
//                        }
//                        else {
//                           ToastUtils.show(mActivity, "获取图片失败");
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
                    photoClip(CameraUtils.geturi(mActivity, data));
                }
                break;
            case PHOTO_CLIP:
                if(resultCode == RESULT_OK) {
//                    if (data != null) {
//                        Bundle extras = data.getExtras();
//                        if (extras != null) {
//                        Bitmap photos = extras.getParcelable("data");
//                        String img_path = getFilesDir().getAbsolutePath() + "img.jpg";
//                        boolean isScuccess = saveBitmap2file(photos, img_path);
//
//                        if (isScuccess) {
//                            setPhoto(photos, img_path);
//
//                        }
//                            Bitmap photos = BitmapFactory.decodeFile(CameraUtils.mCurrentPhotoPath);
                            setPhoto(null, CameraUtils.mCurrentPhotoPath);
//                        }
//                    }
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

//        ivPhoto.setImageBitmap(bitmap);
        UniversalImageUtils.removeFromCache("file://"+img_path);
        UniversalImageUtils.displayImageCircle("file://"+img_path, ivPhoto, 240);
        files.add(0, map);


    }

    private void photoClip(Uri uri) {
//        // 调用系统中自带的图片剪裁
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 5);
//        intent.putExtra("aspectY", 5);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 160);
//        intent.putExtra("outputY", 100);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, PHOTO_CLIP);
        Intent intent = CameraUtils.cropPicture(mActivity, uri, 480, 480);
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
                            Log.e("msg",msg.obj.toString());
                            switch (errorcode) {

                                case 36:
                                    ToastUtils.show(ChangNickActivity.this, "昵称格式有误");


                                    break;

                                case 0:
                                    ToastUtils.show(ChangNickActivity.this, "修改成功");
                                    break;
                                default:
                                    ToastUtils.show(ChangNickActivity.this, "服务器繁忙");
                                    break;
                            }
                        } catch (JSONException e) {

                            ToastUtils.show(ChangNickActivity.this, "服务器繁忙");
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtils.show(ChangNickActivity.this, "服务器繁忙");
                    }

                }
            }
        };
    }

    /**
     * 请求修改个人信息接口
     */
    private void reqChangeInfo() {

        final Map<String, String> params = utils.getParams(mfu.getBasePostStr() + "*" + Constant.userid + "*" + utils.convertChinese(nickStr));

        final String url = getResources().getString(R.string.api_baseurl) + "user/EditName.php";

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        outState.putSerializable("data", user);
    }
}
