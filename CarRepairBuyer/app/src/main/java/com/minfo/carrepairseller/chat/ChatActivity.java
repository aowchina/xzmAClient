package com.minfo.carrepairseller.chat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.minfo.carrepairseller.CommonInterface.PermissionListener;
import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.utils.Constant;
import com.minfo.carrepairseller.utils.ToastUtils;

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
        toChatUsername = getIntent().getExtras().getString("userId");
        userNickName = getIntent().getExtras().getString("userNickName");
        userHeadImage = getIntent().getExtras().getString("userHeadImage");
        if(savedInstanceState != null) {
            Constant.userid = savedInstanceState.getString("userid");
            Constant.psw = savedInstanceState.getString("psw");
            toChatUsername = savedInstanceState.getString("userId");
            userNickName = savedInstanceState.getString("userNickName");
            userHeadImage = savedInstanceState.getString("userHeadImage");
        }
        super.onCreate(savedInstanceState, R.layout.activity_chat);
    }

    @Override
    protected void findViews() {
        //get user id
        Log.e("chatactity", "login: chatactity");
        toChatUsername = getIntent().getExtras().getString("userId");
        userNickName = getIntent().getExtras().getString("userNickName");
        userHeadImage = getIntent().getExtras().getString("userHeadImage");

        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        Bundle bundle=new Bundle();
        bundle.putString("userNickName", userNickName);
        bundle.putString("userHeadImage", userHeadImage);
        bundle.putString("userId", toChatUsername);
        Log.e("toChatUsername",  toChatUsername+"  "+userNickName+"  "+userHeadImage);

        chatFragment.setArguments(bundle);
        //pass parameters to chat fragment
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void initViews() {

//        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
//        singleThreadPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                loginHuan();
//            }
//        });
    }

    /**
     * 登录环信
     */
    private void loginHuan() {
        EMClient.getInstance().login("buy" + Constant.userid, Constant.psw, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.e(TAG, "login: onSuccess");

                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

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
    public String getToChatUsername(){
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
