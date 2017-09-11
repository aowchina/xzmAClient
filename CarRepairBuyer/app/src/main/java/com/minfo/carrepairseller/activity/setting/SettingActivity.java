package com.minfo.carrepairseller.activity.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.activity.address.AddressListActivity;
import com.minfo.carrepairseller.activity.login.LoginActivity;
import com.minfo.carrepairseller.activity.personl.ChangPwdActivity;
import com.minfo.carrepairseller.activity.personl.CheckActivity;
import com.minfo.carrepairseller.dialog.VersionUpdateView;
import com.minfo.carrepairseller.entity.personl.User;
import com.minfo.carrepairseller.entity.setting.UpdateInfo;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.DialogUtil;
import com.minfo.carrepairseller.utils.DownLoadingDialog;
import com.minfo.carrepairseller.utils.LG;
import com.minfo.carrepairseller.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack; // 返回
    private TextView tvTitle; // 标题
    private RelativeLayout rlNickname;
    private RelativeLayout rlAddress;
    private RelativeLayout rlTrueName;
    private RelativeLayout rlPassword;
    private RelativeLayout rlVersion;

    private ImageView ivLogout; // 退出按钮
    private User user = new User();

    //版本更新
    private String apkName = "";
    private String downloadUrl = "";
    private int newVersionCode = 1;
    private int versionCode = 1;
    private String versionName;
    private String newVersionName;
    private String downloadPath = "";
    private DownLoadingDialog downLoadingDialog;

    private UpdateInfo updateInfo; // 版本信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_setting);
//        setContentView(R.layout.activity_setting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_nicheng:
                Intent intentSetting = new Intent(mActivity, ChangNickActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.rl_address:
                startActivity(new Intent(mActivity, AddressListActivity.class));
                break;
            case R.id.rl_true_name:
                startActivity(new Intent(mActivity, CheckActivity.class));
                break;
            case R.id.rl_password:
                startActivity(new Intent(mActivity, ChangPwdActivity.class));
                break;
            case R.id.iv_logout:
                utils.setUserid(mActivity, 0);
                appManager.removeAllActivity();
                break;
            case R.id.rl_version: // 版本更新检查
                //获取包管理者对象
                PackageManager pm = getPackageManager();
                try {
                    //获取包的详细信息
                    PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
                    //获取版本号和版本名称
                    versionCode = info.versionCode;
                    versionName = info.versionName;
                    LG.e("版本号："+info.versionCode);
                    LG.e("版本名称："+info.versionName);
                    checkVersionInfo();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    ToastUtils.show(mActivity, "获取版本号失败，请稍后再试");
                }
                break;
        }
    }

    @Override
    protected void findViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLogout = (ImageView) findViewById(R.id.iv_logout);

        rlNickname = (RelativeLayout) findViewById(R.id.rl_nicheng);
        rlAddress = (RelativeLayout) findViewById(R.id.rl_address);
        rlTrueName = (RelativeLayout) findViewById(R.id.rl_true_name);
        rlPassword = (RelativeLayout) findViewById(R.id.rl_password);
        rlVersion = (RelativeLayout) findViewById(R.id.rl_version);
    }

    @Override
    protected void initViews() {
        tvTitle.setText("设置");
        ivBack.setOnClickListener(this);
        ivLogout.setOnClickListener(this);

        rlNickname.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
        rlTrueName.setOnClickListener(this);
        rlPassword.setOnClickListener(this);
        rlVersion.setOnClickListener(this);
    }

    /**
     * @funcation 检查版本更新
     **/
    public void checkVersionInfo() {

        String url = getResources().getString(R.string.api_baseurl) + "user/Update.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + versionCode);

        httpClient.post(url, params, new RequestListener() {
            @Override public void onPreRequest() {
                showWait();
            }


            @Override public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                if(response != null) {
                    updateInfo = response.getObj(UpdateInfo.class);

                    if (updateInfo != null) {
                        newVersionCode = updateInfo.getNum();
                        newVersionName = updateInfo.getVersionName();
                        refreshDialog(updateInfo.getUrl());
                    }
                }
                //				companyCode.setText("编号:"+user.getArea());
            }


            @Override public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 21) {
                    ToastUtils.show(mActivity, "已经是最新版本");
//                    utils.jumpTo(mActivity, LoginActivity.class);
                }else  if (errorcode == 68) { // 版本xml文件不存在
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
                else  if (errorcode == 69) { // 版本号不是整型
                    ToastUtils.show(mActivity, "获取版本号失败");
                }
                else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }
            }


            @Override public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "服务器繁忙");
            }
        });
    }

    /**
     * 根据版本更新状态显示不同的dialog布局
     */
    private void refreshDialog(final String downloadUrl) {

        VersionUpdateView updateView = new VersionUpdateView(mActivity);

//        if(versionCode < newVersionCode) {
            // 有更新
            updateView.setVersionMessage(newVersionCode+"", versionCode+"").setOnDownListener(new VersionUpdateView.OnDownListener() {
                @Override
                public void down() {
                    new DownLoadTask().execute(downloadUrl);
                }
            });
            DialogUtil.showSelfDialog(mActivity, updateView);
//        }
//        else {
//            ToastUtils.show(mActivity, "您当前已是最新版本~");
//        }

    }


    /**
     * 更新下载
     */
    class DownLoadTask extends AsyncTask<String, Integer, Boolean> {
        @Override protected Boolean doInBackground(String... params) {
            String urlStr = params[0];
            int progress = 0;
            try {
                downloadPath = Environment.getExternalStorageDirectory() + "/" + "download";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(downloadPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                apkName = mActivity.getPackageName() + ".apk";
                File apkFile = new File(downloadPath, apkName);
                if (apkFile.exists()) {
                    apkFile.delete();
                }
                apkFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    publishProgress(progress);
                    if (numread <= 0) {
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (true);
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }


        @Override protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                File apkfile = new File(downloadPath, apkName);
                if (!apkfile.exists()) {
                    return;
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
                startActivity(i);
//                android.os.Process.killProcess(android.os.Process.myPid());
                downLoadingDialog.dismiss();
            }
            else{
                downLoadingDialog.dismiss();
            }
        }

        @Override protected void onPreExecute() {
            downLoadingDialog = new DownLoadingDialog(mActivity);
            downLoadingDialog.show();
            super.onPreExecute();
        }

        @Override protected void onProgressUpdate(Integer... values) {
            downLoadingDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }
    }
}
