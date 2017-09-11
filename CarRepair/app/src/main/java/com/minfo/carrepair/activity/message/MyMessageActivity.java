package com.minfo.carrepair.activity.message;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.minfo.carrepair.CommonInterface.PermissionListener;
import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.activity.MyApplication;
import com.minfo.carrepair.adapter.FragmentViewPagerAdapter;
import com.minfo.carrepair.adapter.OnExtraPageChangeListener;
import com.minfo.carrepair.chat.ConversationListFragment;
import com.minfo.carrepair.chat.HuanUtils;
import com.minfo.carrepair.fragment.BaseFragment;
import com.minfo.carrepair.fragment.MyMessageFragment;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyMessageActivity extends BaseActivity implements View.OnClickListener {
    //环信
    private ConversationListFragment conversationListFragment;

    private ImageView ivBack;
    private TextView tvTitle;
    private LinearLayout llnew;
    private LinearLayout llSeller;
    private LinearLayout llMyfriend;
    private ViewPager mViewPager;
    private FragmentViewPagerAdapter mAdapter;
    private List<Fragment> mFragments;

    private TextView[] mTabViews;
    private ImageView mTabline;
    private int mTabLineWidth;
    private int mCurrentPageIndex;
    private static final int[] FRAGMENT_INDEX = {0, 1, 2};
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        if(savedInstanceState != null) {
            Constant.userid = savedInstanceState.getString("userid");
            Constant.psw = savedInstanceState.getString("psw");
            type = savedInstanceState.getInt("type", 0);
        }
        super.onCreate(savedInstanceState, R.layout.activity_my_message);
//        setContentView(R.layout.activity_my_message);
    }

    @Override
    protected void findViews() {

        mTabViews = new TextView[2];
        mViewPager = (ViewPager) findViewById(R.id.activity_my_message_viewpager);
        mTabViews[0] = (TextView) findViewById(R.id.tv_new);
//        mTabViews[1] = (TextView) findViewById(R.id.tv_seller);
        mTabViews[1] = (TextView) findViewById(R.id.tv_myfriend);


        llnew = (LinearLayout) findViewById(R.id.activity_my_message_tab_new);
        llSeller = (LinearLayout) findViewById(R.id.activity_my_message_tab_seller);
        llMyfriend = (LinearLayout) findViewById(R.id.activity_my_message_tab_myfriend);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("我的消息");
    }

    @Override
    protected void initViews() {
        initTabLine();
//        for(TextView tv : mTabViews) {
//            tv.setOnClickListener(this);
//        }
        ivBack.setOnClickListener(this);
        llnew.setOnClickListener(this);
        llSeller.setOnClickListener(this);
        llMyfriend.setOnClickListener(this);

        if(ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            requestRunTimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}
                    , new PermissionListener() {
                        @Override
                        public void onGranted() {
                            init();
                        }

                        @Override
                        public void onGranted(List<String> grantedPermission) {

                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
                            String str = "您拒绝的权限将影响";
//                                    int i = 0;
                            for(String string : deniedPermission) {
//
                                if(string.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) || string.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    if(!str.contains("存储")) {
                                        str += "查看您的聊天历史记录";
                                    }
                                }

                            }
                            ToastUtils.show(mActivity, str);
                            init();
                        }
                    });
        }

    }


    private void init() {
        mFragments = new ArrayList<>();
//        for(int i = 0; i < 3; i ++) {
//            BaseFragment fragment = new MyMessageFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt(Constant.MESSAGE_TYPE, i);
//            fragment.setArguments(bundle);
//            mFragments.add(fragment);
//        }

        //消息列表
        mFragments = new ArrayList<>();
        conversationListFragment = new ConversationListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(Constant.MESSAGE_TYPE, 0);
        conversationListFragment.setArguments(bundle1);
        mFragments.add(conversationListFragment);

//        //配件商列表
//        BaseFragment fragment = new MyMessageFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(Constant.MESSAGE_TYPE, 1);
//        fragment.setArguments(bundle);
//        mFragments.add(fragment);
        //好友列表
        BaseFragment fragment2 = new MyMessageFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(Constant.MESSAGE_TYPE, 1);
        fragment2.setArguments(bundle2);
        mFragments.add(fragment2);
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),
                mViewPager, mFragments);
        mAdapter.setOnExtraPageChangeListener(new OnExtraPageChangeListener() {

            @Override
            public void onExtraPageSelected(int position) {
                resetTextView();
                if (position == FRAGMENT_INDEX[position]) {
                    mTabViews[position].setTextColor(getResources().getColor(
                            R.color.red));
                }
                mCurrentPageIndex = position;
                /*LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
						.getLayoutParams();
				lp.leftMargin = (int) (mCurrentPageIndex * mTabLineWidth);
				mTabline.setLayoutParams(lp);*/
            }

            @Override
            public void onExtraPageScrolled(int position, float positionOffset,
                                            int positionOffsetPx) {

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mTabline
                        .getLayoutParams();
                if (mCurrentPageIndex == FRAGMENT_INDEX[position]) {
                    lp.leftMargin = (int) (mCurrentPageIndex * mTabLineWidth + positionOffset
                            * mTabLineWidth);
                } else {
                    lp.leftMargin = (int) (mCurrentPageIndex * mTabLineWidth + (positionOffset - 1)
                            * mTabLineWidth);
                }
                mTabline.setLayoutParams(lp);
//                Message msg = new Message();
//                msg.what = TAB_HANDLER;
//                Bundle bundle = new Bundle();
//                bundle.putFloat("positionOffset", positionOffset);
//                bundle.putInt("position", position);
//                bundle.putInt("positionOffsetPx", positionOffsetPx);
//                msg.setData(bundle);
//                mTabHandler.sendMessage(msg);
				/*LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
						.getLayoutParams();
				if (mCurrentPageIndex == FRAGMENT_INDEX[position]) {
					lp.leftMargin = (int) (mCurrentPageIndex * mTabLineWidth + positionOffset
							* mTabLineWidth);
				} else {
					lp.leftMargin = (int) (mCurrentPageIndex * mTabLineWidth + (positionOffset - 1)
							* mTabLineWidth);
				}
				mTabline.setLayoutParams(lp);*/
            }

            @Override
            public void onExtraPageScrollStateChanged(int arg0) {

            }
        });

        mViewPager.setCurrentItem(type); // 默认选中全部
//        mViewPager.setOffscreenPageLimit(3);
        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();

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

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                if (type == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
            }
        });
    }

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HuanUtils.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(HuanUtils.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (type == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
                String action = intent.getAction();


                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {


                /**
                 * 待修改
                 */
                //有问题


                MyApplication.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
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

    /**
     * 初始化标题选中线宽度
     */
    private void initTabLine() {
        mTabline = (ImageView) findViewById(R.id.activity_my_message_tab_line);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mTabLineWidth = outMetrics.widthPixels / 2;
        ViewGroup.LayoutParams lp = mTabline.getLayoutParams();
        lp.width = mTabLineWidth;
        mTabline.setLayoutParams(lp);
    }

    /**
     * 重置标题颜色
     */
    protected void resetTextView() {
        int len = mTabViews.length;
        for (int i = 0; i < len; i++) {
            mTabViews[i].setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.activity_my_message_tab_new:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[0]);
                break;
            case R.id.activity_my_message_tab_seller:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[1]);
                break;
            case R.id.activity_my_message_tab_myfriend:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[1]);
                break;
        }
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
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putString("userid", Constant.userid);
            outState.putString("psw", Constant.psw);
            outState.putInt("type", type);
        }
    }
}
