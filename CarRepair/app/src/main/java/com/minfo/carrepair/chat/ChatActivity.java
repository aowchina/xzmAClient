package com.minfo.carrepair.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.minfo.carrepair.CommonInterface.PermissionListener;
import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.ToastUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends BaseActivity {
    String toChatUsername;
    private EaseChatFragment chatFragment;
    private String userNickName;
    private String userHeadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            Constant.userid = savedInstanceState.getString("userid");
            Constant.psw = savedInstanceState.getString("psw");
            toChatUsername = savedInstanceState.getString("userId");
            userNickName = savedInstanceState.getString("userNickName");
            userHeadImage = savedInstanceState.getString("userHeadImage");
        }
        else {
            toChatUsername = getIntent().getExtras().getString("userId");
            userNickName = getIntent().getExtras().getString("userNickName");

            userHeadImage = getIntent().getExtras().getString("userHeadImage");
        }
        super.onCreate(savedInstanceState, R.layout.activity_chat);
    }

    @Override
    protected void findViews() {
        //get user id

        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        //chatFragment.setArguments(getIntent().getExtras());
        Bundle bundle = new Bundle();
        bundle.putString("userNickName", userNickName);
        bundle.putString("userHeadImage", userHeadImage);
        bundle.putString("userId", toChatUsername);


        chatFragment.setArguments(bundle);
        Log.e("toChatUsername", toChatUsername + "  " + userNickName + "  " + userHeadImage);

        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    protected void initViews() {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
//        }
//        else {
//            requestRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.RECORD_AUDIO/*, Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.CAMERA*/}
//                    , new PermissionListener() {
//                        @Override
//                        public void onGranted() {
////                                    jumbToPhotosActivity(pos);
//                        }
//
//                        @Override
//                        public void onGranted(List<String> grantedPermission) {
////                            reqDate();
//                        }
//
//                        @Override
//                        public void onDenied(List<String> deniedPermission) {
////                                  jumbToPhotosActivity(pos);
//                            String str = "您拒绝的权限将影响";
//                            int i = 0;
//                            for(String string : deniedPermission) {
//                                if(i!=0){
//                                    str += "、";
//                                }
//                                if(string.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                                    str += "相册";
//                                }
//                                else if(string.equals(Manifest.permission.CAMERA)) {
//                                    str += "拍照";
//                                }
//                                else if(string.equals(Manifest.permission.RECORD_AUDIO)) {
//                                    str += "语音";
//                                }
////                                else if(string.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
////                                    str += "定位";
////                                }
//                                i++;
//                            }
//                            str += "功能的使用";
//                            ToastUtils.show(mActivity, str);
//
//                        }
//                    });
//        }
    }
    /**
     * 登录环信
     */
    private void loginHuan() {
        EMClient.getInstance().login("sell" + Constant.userid, Constant.psw, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.e(TAG, "login: onSuccess");

                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
//                LG.e("isConnected"+EMClient.getInstance().isConnected());

            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.e(TAG, "login: onError: " + code);

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        finish();
//        if (EasyUtils.isSingleActivity(this)) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!EMClient.getInstance().isConnected()) {
            ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
            singleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    loginHuan();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putString("userid", Constant.userid);
            outState.putString("psw", Constant.psw);

            outState.putString("userId", toChatUsername);
            outState.putString("userNickName", userNickName);
            outState.putString("userHeadImage", userHeadImage);
        }
    }
}
