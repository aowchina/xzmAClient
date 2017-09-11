package com.minfo.carrepairseller.activity.order;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;
import com.minfo.carrepairseller.adapter.FragmentViewPagerAdapter;
import com.minfo.carrepairseller.adapter.OnExtraPageChangeListener;
import com.minfo.carrepairseller.fragment.BaseFragment;
import com.minfo.carrepairseller.fragment.OrderListFragment;
import com.minfo.carrepairseller.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseActivity implements View.OnClickListener{
    private ImageView ivBack;
    private TextView tvTitle;
    //    private LinearLayout llAll;
    private LinearLayout llWaitPay;
    private LinearLayout llWaitSend;
    private LinearLayout llWaitReceive;
    private LinearLayout llWaitAccess;
    private LinearLayout llFinish;
    private ViewPager mViewPager;
    private FragmentViewPagerAdapter mAdapter;
    private List<Fragment> mFragments;

    private TextView[] mTabViews;
    private ImageView mTabline;
    private int mTabLineWidth;
    private int mCurrentPageIndex;
    private static final int[] FRAGMENT_INDEX = { 0, 1, 2, 3 ,4};
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_order_list);
//        setContentView(R.layout.activity_order_list);
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        mTabViews = new TextView[5];
        mViewPager = (ViewPager) findViewById(R.id.activity_order_list_viewpager);
        mTabViews[0] = (TextView) findViewById(R.id.tv_wait_pay);
        mTabViews[1] = (TextView) findViewById(R.id.tv_wait_send);
        mTabViews[2] = (TextView) findViewById(R.id.tv_wait_receive);
        mTabViews[3] = (TextView) findViewById(R.id.tv_wait_assess);
        mTabViews[4] = (TextView) findViewById(R.id.tv_finish);

//        llAll = (LinearLayout) findViewById(R.id.activity_order_list_tab_all);
        llWaitPay = (LinearLayout) findViewById(R.id.activity_order_list_tab_wait_pay);
        llWaitSend = (LinearLayout) findViewById(R.id.activity_order_list_tab_wait_send);
        llWaitReceive = (LinearLayout) findViewById(R.id.activity_order_list_tab_wait_receive);
        llWaitAccess = (LinearLayout) findViewById(R.id.activity_order_list_tab_wait_assess);
        llFinish = (LinearLayout) findViewById(R.id.activity_order_list_tab_finish);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("我的订单");
    }

    @Override
    protected void initViews() {
        initTabLine();
//        for(TextView tv : mTabViews) {
//            tv.setOnClickListener(this);
//        }
        ivBack.setOnClickListener(this);
        llFinish.setOnClickListener(this);
        llWaitPay.setOnClickListener(this);
        llWaitSend.setOnClickListener(this);
        llWaitReceive.setOnClickListener(this);
        llWaitAccess.setOnClickListener(this);

        mFragments = new ArrayList<>();
        for(int i = 0; i < 5; i ++) {
            BaseFragment fragment = new OrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.ORDER_STATE, i);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }
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

                FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) mTabline
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
    }

    /**
     * 初始化标题选中线宽度
     */
    private void initTabLine() {
        mTabline = (ImageView) findViewById(R.id.activity_order_list_tab_line);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mTabLineWidth = outMetrics.widthPixels / 5;
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
            case R.id.activity_order_list_tab_wait_pay:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[0]);
                break;
            case R.id.activity_order_list_tab_wait_send:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[1]);
                break;
            case R.id.activity_order_list_tab_wait_receive:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[2]);
                break;
            case R.id.activity_order_list_tab_wait_assess:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[3]);
                break;
            case R.id.activity_order_list_tab_finish:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[4]);
                break;
        }
    }
}
