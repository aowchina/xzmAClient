package com.minfo.carrepairseller.activity.personl;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.http.BaseResponse;
import com.minfo.carrepairseller.http.RequestListener;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.MyCheck;
import com.minfo.carrepairseller.utils.ToastUtils;

import java.util.Map;

public class ChangPwdActivity extends BaseActivity implements View.OnClickListener {
    private EditText original_password;
    private EditText new_password;
    private EditText onfirm_password;
    private TextView confirm_bnt;
    private TextView cancel_bnt;
    private ImageView ivBack;
    private String onfirmpassword;
    private String newpassword;
    private String originalpassword;
    private TextView tvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_chang_pwd);
    }


    @Override
    protected void findViews() {
        original_password = (EditText) findViewById(R.id.original_password);
        new_password = (EditText) findViewById(R.id.new_password);
        onfirm_password = (EditText) findViewById(R.id.onfirm_password);
        confirm_bnt = (TextView) findViewById(R.id.confirm_bnt);
        cancel_bnt = (TextView) findViewById(R.id.cancel_bnt);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        confirm_bnt.setOnClickListener(this);
        cancel_bnt.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        tvTitle.setText("修改密码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            //确定
            case R.id.confirm_bnt:
                originalpassword = original_password.getText().toString().trim();
                newpassword = new_password.getText().toString().trim();
                onfirmpassword = onfirm_password.getText().toString().trim();

                if (isOnline()) {
                    if (checkInput()) {
                        showWait();
                        reqChangePW();
                    }
                } else {
                    ToastUtils.show(ChangPwdActivity.this, "暂无网络");
                }

                break;
            //取消
            case R.id.cancel_bnt:
                onBackPressed();
                break;

        }

    }

    /**
     * 输入的基本验证
     */
    private boolean checkInput() {

        if (TextUtils.isEmpty(onfirmpassword)) {
            ToastUtils.show(this, "请输入原密码");
            return false;
        }

        if (!MyCheck.isPsw(onfirmpassword)) {
            ToastUtils.show(this, "输入的密码格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(newpassword)) {
            ToastUtils.show(this, "请输入新密码");
            return false;
        }

        if (!MyCheck.isPsw(newpassword)) {
            ToastUtils.show(this, "输入的新密码格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(onfirmpassword)) {
            ToastUtils.show(this, "请输入确认密码");
            return false;
        }

        if (!MyCheck.isPsw(onfirmpassword)) {
            ToastUtils.show(this, "输入的确认密码格式不正确");
            return false;
        }
        return true;
    }

    /**
     * 请求修改密码接口
     */
    private void reqChangePW() {
        Map<String, String> params = utils.getParams(mfu.getBasePostStr() + "*" + Constant.userid + "*" + originalpassword + "*" + newpassword + "*" + onfirmpassword);

        final String url = getResources().getString(R.string.api_baseurl) + "user/EditPsw.php";
        httpClient.post(url, params, new RequestListener() {
            @Override
            public void onPreRequest() {

            }


            @Override
            public void onRequestSuccess(BaseResponse response) {
                cleanWait();
                ToastUtils.show(mActivity, "密码修改成功");

            }


            @Override
            public void onRequestError(int code, String msg) {
                cleanWait();
                ToastUtils.show(mActivity, "请求数据失败");
            }


            @Override
            public void onRequestNoData(BaseResponse response) {
                cleanWait();
                int errorcode = response.getErrorcode();
                if (errorcode == 17) {
                    ToastUtils.show(mActivity, "原密码不正确");

                } else if (errorcode == 18) {
                    ToastUtils.show(mActivity, "两次密码不一致");

                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");

                }
            }
        });
    }

}
