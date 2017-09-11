package com.minfo.carrepair.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.publish.AskToBuyActivity;
import com.minfo.carrepair.activity.publish.PartsPublishActivity;
import com.minfo.carrepair.fragment.GoodManagerFirstFragment;
import com.minfo.carrepair.fragment.PersonlFragment;
import com.minfo.carrepair.fragment.PublishFragment;
import com.minfo.carrepair.fragment.PurchaseFragment;
import com.minfo.carrepair.fragment.QueryFragment;
import com.minfo.carrepair.utils.Constant;
import com.minfo.carrepair.utils.LG;
import com.minfo.carrepair.widget.CircleViewRed;
import com.minfo.carrepair.widget.GradientTextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout rlPublish; // 发布容器
    private ImageView ivPublishParts; // 发布安钮
    private ImageView ivBuy; // 求购按钮
    private ImageView ivBack; // 发布的返回按钮需隐藏
    private TextView tvTitle; // 发布标题

    private boolean isExit;
    private LinearLayout ll_continer;
    private LinearLayout llQuery;
    private LinearLayout llShop;
    private LinearLayout llPublish;
    private LinearLayout llPerson;
    private LinearLayout llPurchase;

    private ImageView ivQuery;
    private ImageView ivPerson;
    private ImageView ivPublish;
    private ImageView ivShop;
    private ImageView ivPurchase;

    private QueryFragment queryFragment;
//    private ShopFragment shopFragment;
    private GoodManagerFirstFragment goodManagerFragment;
    private PersonlFragment personFragment;
    private PublishFragment publishFragment;
    private PurchaseFragment purchaseFragment;

    private GradientTextView tvQuery;
    private GradientTextView tvShop;
    private GradientTextView tvPerson;
    private GradientTextView tvPurchase;
    private GradientTextView tvPublish;


    private MessageReceiver messageReceiver;
    private CircleViewRed tvOrderNum;
    private int selected = 1;

    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String messageReceive = intent.getStringExtra("message");
            Log.e("Recevier1", "接收到:" + messageReceive);
            //if (messageReceive.equals("您的订单已被接受~")) {
            setOrderMessage();

            //}
            //abortBroadcast();
        }

    }

    //报价消息初始化
    private void setOrderMessage() {

        if (utils.getOrderMessage() == 0) {

            tvOrderNum.setVisibility(View.INVISIBLE);
        } else {

            if (utils.getOrderMessage() > 99) {
                tvOrderNum.setText("99+");
            } else {
                tvOrderNum.setText(utils.getOrderMessage() + "");
            }

            tvOrderNum.setVisibility(View.VISIBLE);
        }



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            LG.e("mainactivity", "savedInstanceState="+savedInstanceState);
            Constant.userid = savedInstanceState.getString("userid");
            Constant.psw = savedInstanceState.getString("psw");
            selected = savedInstanceState.getInt("select", 1);
            if(selected == 5) {
                selected = 1;
            }
        }
        super.onCreate(savedInstanceState, R.layout.activity_main);
    }

    @Override
    protected void findViews() {
        ll_continer = ((LinearLayout) findViewById(R.id.ll_continer));
        llQuery = (LinearLayout) findViewById(R.id.ll_query);
        llPurchase = (LinearLayout) findViewById(R.id.ll_shop);
        llShop = (LinearLayout) findViewById(R.id.ll_person);
        llPublish = (LinearLayout) findViewById(R.id.ll_publish);
        llPerson = (LinearLayout) findViewById(R.id.ll_purchase);

        ivQuery = (ImageView) findViewById(R.id.iv_query_bt);
        ivPublish = (ImageView) findViewById(R.id.iv_publish_bt);
        ivShop = (ImageView) findViewById(R.id.iv_shop_ic);
        ivPurchase = (ImageView) findViewById(R.id.iv_purchase_bt);
        ivPerson = (ImageView) findViewById(R.id.iv_person_bt);

        tvQuery = ((GradientTextView) findViewById(R.id.tv_query));
        tvPurchase = ((GradientTextView) findViewById(R.id.tv_purchase));
        tvShop = ((GradientTextView) findViewById(R.id.tv_shop));
        tvPerson = ((GradientTextView) findViewById(R.id.tv_person));
        tvPublish = ((GradientTextView) findViewById(R.id.tv_publish));
        tvOrderNum = ((CircleViewRed) findViewById(R.id.tv_order_num));

        rlPublish = (RelativeLayout) findViewById(R.id.rl_publish);
        ivPublishParts = (ImageView) findViewById(R.id.iv_seller);
        ivBuy = (ImageView) findViewById(R.id.iv_buyer);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    protected void initViews() {
        // 注册一个接收消息的BroadcastReceiver
        messageReceiver = new MessageReceiver();
        IntentFilter intentFilterMessage = new IntentFilter();
        intentFilterMessage.addAction("receivemessage");
        registerReceiver(messageReceiver, intentFilterMessage);


        tvTitle.setText("发布");
        ivBack.setVisibility(View.GONE);

        rlPublish.setOnClickListener(this);
        ivPublishParts.setOnClickListener(this);
        ivBuy.setOnClickListener(this);

        llQuery.setOnClickListener(this);
        llShop.setOnClickListener(this);
        llPerson.setOnClickListener(this);
        llPurchase.setOnClickListener(this);
        llPublish.setOnClickListener(this);
        setSelect(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_publish:
                if(rlPublish != null) {
                    rlPublish.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_query:
                setSelect(1);
                break;
            case R.id.ll_shop:
                setSelect(2);
                break;
            case R.id.ll_publish:
                setSelect(5);
                break;
            case R.id.ll_purchase:
                setSelect(3);
                break;
            case R.id.ll_person:
                setSelect(4);
                break;
            case R.id.iv_buyer:
                startActivity(new Intent(mActivity, AskToBuyActivity.class));
                break;
            case R.id.iv_seller:
                startActivity(new Intent(mActivity, PartsPublishActivity.class));
                break;

        }
    }
    //显示fragment的处理
    private void setSelect(int i) {
        selected = i;
        resetBottom();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(i != 5) {
            hideFragment(transaction);
        }
        switch (i) {


            case 1:

                //Utils.CommitPageFlagToShared(MainActivity.this, 1);
                if (queryFragment == null) {
                    queryFragment = new QueryFragment();
                    transaction.add(R.id.ll_continer, queryFragment);
                } else {
                    transaction.show(queryFragment);
                }
                ivQuery.setImageResource(R.mipmap.qixiu_chacha_ch);
                tvQuery.setmStartColor(getResources().getColor(R.color.skin_menu_text_start));
                tvQuery.setmEndColor(getResources().getColor(R.color.skin_menu_text_end));
                break;
            case 2:

//                //Utils.CommitPageFlagToShared(MainActivity.this, 2);
//                if (shopFragment == null) {
//                    shopFragment = new ShopFragment();
//                    transaction.add(R.id.ll_continer, shopFragment);
//                } else {
//                    transaction.show(shopFragment);
//                }
                if(goodManagerFragment == null) {
                    goodManagerFragment = new GoodManagerFirstFragment();
                    transaction.add(R.id.ll_continer, goodManagerFragment);
                }
                else {
                    transaction.show(goodManagerFragment);
                }
                ivShop.setImageResource(R.mipmap.qixiu_shangcheng_ch);
                tvShop.setmStartColor(getResources().getColor(R.color.skin_menu_text_start));
                tvShop.setmEndColor(getResources().getColor(R.color.skin_menu_text_end));
                break;
            case 5:
                //Utils.CommitPageFlagToShared(MainActivity.this, 3);
//                if (publishFragment == null) {
//                    publishFragment = new PublishFragment();
//                    transaction.add(R.id.ll_continer, publishFragment);
//                } else {
//                    transaction.show(publishFragment);
//                }
//                tvPublish.setmStartColor(getResources().getColor(R.color.skin_menu_text_start));
//                tvPublish.setmEndColor(getResources().getColor(R.color.skin_menu_text_end));
                if(rlPublish != null) {
                    rlPublish.setVisibility(View.VISIBLE);
                }
                break;

            case 3:
                //Utils.CommitPageFlagToShared(MainActivity.this, 3);
                if (purchaseFragment == null) {
                    purchaseFragment = new PurchaseFragment();
                    transaction.add(R.id.ll_continer, purchaseFragment);
                } else {
                    transaction.show(purchaseFragment);

                }
                ivPurchase.setImageResource(R.mipmap.qixiu_qiugou_ch);
                tvPurchase.setmStartColor(getResources().getColor(R.color.skin_menu_text_start));
                tvPurchase.setmEndColor(getResources().getColor(R.color.skin_menu_text_end));
                utils.setOrderMessage(0);
                setOrderMessage();

                break;
            case 4:
                //Utils.CommitPageFlagToShared(MainActivity.this, 4);
                if (personFragment == null) {
                    personFragment = new PersonlFragment();
                    transaction.add(R.id.ll_continer, personFragment);
                } else {
                    transaction.show(personFragment);

                }
                ivPerson.setImageResource(R.mipmap.qixiu_wode_ch);
                tvPerson.setmStartColor(getResources().getColor(R.color.skin_menu_text_start));
                tvPerson.setmEndColor(getResources().getColor(R.color.skin_menu_text_end));
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    //隐藏fragment的处理
    private void hideFragment(FragmentTransaction transaction) {
        if (queryFragment != null) {
            transaction.hide(queryFragment);
        }

//        if (shopFragment != null) {
//            transaction.hide(shopFragment);
//        }
        if (goodManagerFragment != null) {
            transaction.hide(goodManagerFragment);
        }
        if (personFragment != null) {
            transaction.hide(personFragment);
        }

        if(purchaseFragment != null) {
            transaction.hide(purchaseFragment);
        }
        if(publishFragment != null) {
            transaction.hide(publishFragment);
        }

        if(rlPublish != null) {
            rlPublish.setVisibility(View.GONE);
        }

    }

    //重置底部图片
    private void resetBottom() {
        ivQuery.setImageResource(R.mipmap.qixiu_chacha);
        ivShop.setImageResource(R.mipmap.qixiu_shangcheng);
        ivPurchase.setImageResource(R.mipmap.qixiu_qiugou);
        ivPerson.setImageResource(R.mipmap.qixiu_wode);

        tvPerson.setmStartColor(getResources().getColor(R.color.skin_menu_text_ck));
        tvPerson.setmEndColor(getResources().getColor(R.color.skin_menu_text_ck));
        tvShop.setmStartColor(getResources().getColor(R.color.skin_menu_text_ck));
        tvShop.setmEndColor(getResources().getColor(R.color.skin_menu_text_ck));
        tvQuery.setmStartColor(getResources().getColor(R.color.skin_menu_text_ck));
        tvQuery.setmEndColor(getResources().getColor(R.color.skin_menu_text_ck));
        tvPublish.setmStartColor(getResources().getColor(R.color.skin_menu_text_ck));
        tvPublish.setmEndColor(getResources().getColor(R.color.skin_menu_text_ck));
        tvPurchase.setmStartColor(getResources().getColor(R.color.skin_menu_text_ck));
        tvPurchase.setmEndColor(getResources().getColor(R.color.skin_menu_text_ck));
    }

    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                appManager.removeAllActivity();
//                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                isExit = true;
                Toast.makeText(this, "2秒内再按一次退出", Toast.LENGTH_SHORT).show();
                Timer tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(outState != null) {
            outState.putString("userid", Constant.userid);
            outState.putString("psw", Constant.psw);
            outState.putInt("select", selected);
        }
//        super.onSaveInstanceState(outState);
    }
}
