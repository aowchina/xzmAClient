package com.minfo.carrepair.activity.query;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.activity.BaseActivity;
import com.minfo.carrepair.adapter.FragmentViewPagerAdapter;
import com.minfo.carrepair.adapter.OnExtraPageChangeListener;
import com.minfo.carrepair.entity.query.ChexiItem;
import com.minfo.carrepair.entity.query.PinpaiEntity;
import com.minfo.carrepair.entity.query.PinpaiItem;
import com.minfo.carrepair.fragment.PinPaiChoseFragment;
import com.minfo.carrepair.fragment.XiChoseFragment;
import com.minfo.carrepair.fragment.XingChoseFragment;

import java.util.ArrayList;
import java.util.List;

public class ChoseCarActivity extends BaseActivity implements View.OnClickListener, PinPaiChoseFragment.JumpFragmentListener, XiChoseFragment.JumpFromXiFragmentListener {

//    protected PagerSlidingTabStrip mTabStrip;
//    protected ViewPagerFragmentAdapter mTabsAdapter;
//    private LinearLayout llAll;
    private ImageView ivBack;
    private TextView tvRight;
    private LinearLayout llRight;
    private LinearLayout llPinpai;
    private LinearLayout llChexi;
    private LinearLayout llChexing;
//    private LinearLayout llWaitAccess;
    private ViewPager mViewPager;
    private FragmentViewPagerAdapter mAdapter;
    private List<Fragment> mFragments;

    private TextView[] mTabViews;
    private ImageView mTabline;
    private int mTabLineWidth;
    private int mCurrentPageIndex = 0;
    private static final int[] FRAGMENT_INDEX = {2, 3, 4};
    PinPaiChoseFragment pinPaiChoseFragment; // 品牌
    XiChoseFragment choseFragment; // 车系
    XingChoseFragment xingChoseFragment; // 车型
    private boolean isDuo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_chose_car);
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        if(intent != null) {
            isDuo = intent.getBooleanExtra("isDuo", false);
        }
//        mTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_tabstrip);

//        mViewPager = (ViewPager) findViewById(R.id.pager);

//        mTabsAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), mTabStrip, mViewPager);
//        setScreenPageLimit();
//        onSetupTabAdapter(mTabsAdapter);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvRight = (TextView) findViewById(R.id.tv_right);
        llRight = (LinearLayout) findViewById(R.id.activity_chose_car_tab_right);

        mTabViews = new TextView[3];
        mViewPager = (ViewPager) findViewById(R.id.activity_chose_car_pager);
//        mTabViews[0] = (TextView) findViewById(R.id.tv_all);
        mTabViews[0] = (TextView) findViewById(R.id.tv_pp);
        mTabViews[1] = (TextView) findViewById(R.id.tv_cx);
        mTabViews[2] = (TextView) findViewById(R.id.tv_cxing);
//        mTabViews[4] = (TextView) findViewById(R.id.tv_wait_assess);

//        llAll = (LinearLayout) findViewById(R.id.activity_chose_car_tab_all);
        llPinpai = (LinearLayout) findViewById(R.id.activity_chose_car_tab_pp);
        llChexi = (LinearLayout) findViewById(R.id.activity_chose_car_tab_cx);
        llChexing = (LinearLayout) findViewById(R.id.activity_chose_car_tab_cxing);
//        llWaitAccess = (LinearLayout) findViewById(R.id.activity_chose_car_tab_wait_assess);
        if(isDuo) {
            llRight.setVisibility(View.VISIBLE);
        }
        else {
            llRight.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initViews() {
        initTabLine();
//        for(TextView tv : mTabViews) {
//            tv.setOnClickListener(this);
//        }
        ivBack.setOnClickListener(this);
//        llAll.setOnClickListener(this);
        llPinpai.setOnClickListener(this);
        llChexi.setOnClickListener(this);
        llChexing.setOnClickListener(this);
        tvRight.setOnClickListener(this);
//        llWaitAccess.setOnClickListener(this);

        mFragments = new ArrayList<>();
        pinPaiChoseFragment = new PinPaiChoseFragment();
        choseFragment = new XiChoseFragment();
        xingChoseFragment = new XingChoseFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isDuo", isDuo);
        xingChoseFragment.setArguments(bundle);

        mFragments.add(pinPaiChoseFragment);
        mFragments.add(choseFragment);
        mFragments.add(xingChoseFragment);
//        for(int i = 0; i < 5; i ++) {
//            BaseFragment fragment = new OrderListFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt(Constant.ORDER_STATE, i);
//            fragment.setArguments(bundle);
//            mFragments.add(fragment);
//        }
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),
                mViewPager, mFragments);
        mAdapter.setOnExtraPageChangeListener(new OnExtraPageChangeListener() {

            @Override
            public void onExtraPageSelected(int position) {
                resetTextView();
//                if (position == FRAGMENT_INDEX[position]) {
                    mTabViews[position].setTextColor(getResources().getColor(
                            R.color.black));
//                }
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
                if (mCurrentPageIndex == position) {
                    lp.leftMargin = (int) (FRAGMENT_INDEX[mCurrentPageIndex] * mTabLineWidth + positionOffset
                            * mTabLineWidth);
                } else {
                    lp.leftMargin = (int) (FRAGMENT_INDEX[mCurrentPageIndex] * mTabLineWidth + (positionOffset - 1)
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

        mViewPager.setCurrentItem(0); // 默认选中全部
    }

    /**
     * 初始化标题选中线宽度
     */
    private void initTabLine() {
        mTabline = (ImageView) findViewById(R.id.activity_chose_car_tab_line);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mTabLineWidth = outMetrics.widthPixels / 7;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mTabline.getLayoutParams();
        lp.width = mTabLineWidth;
        lp.leftMargin = FRAGMENT_INDEX[mCurrentPageIndex] * mTabLineWidth;
        mTabline.setLayoutParams(lp);
    }
    /**
     * 重置标题颜色
     */
    protected void resetTextView() {
        int len = mTabViews.length;
        for (int i = 0; i < len; i++) {
            mTabViews[i].setTextColor(getResources().getColor(R.color.gray));
        }
    }
//    protected void onSetupTabAdapter(ViewPagerFragmentAdapter adapter) {
//        String[] title = getResources().getStringArray(R.array.event_viewpage_arrays);
//        adapter.addTab(title[0], "pinpai", PinPaiChoseFragment.class, new Bundle());
//        adapter.addTab(title[1], "xi", XiChoseFragment.class, new Bundle());
//        adapter.addTab(title[2], "xing", XingChoseFragment.class, new Bundle());
//
//    }
//
//
//    protected void setScreenPageLimit() {
//        mViewPager.setOffscreenPageLimit(3);
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        mTabsAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void jumpFragment() {
//        mViewPager.setCurrentItem(1);
//    }
//
//    @Override
//    public void jumpFromXiFragment() {
//        mViewPager.setCurrentItem(2);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
//            case R.id.activity_chose_car_tab_all:
//                mViewPager.setCurrentItem(FRAGMENT_INDEX[0]);
//                break;
            case R.id.activity_chose_car_tab_pp:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.activity_chose_car_tab_cx:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.activity_chose_car_tab_cxing:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.tv_right:
                xingChoseFragment.getCheckId();
                break;
//            case R.id.activity_chose_car_tab_wait_assess:
//                mViewPager.setCurrentItem(FRAGMENT_INDEX[4]);
//                break;
        }
    }

    @Override
    public void jumpFragment(PinpaiItem pinpaiItem) {
        choseFragment.setPinpaiItem(pinpaiItem);
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void jumpFromXiFragment(ChexiItem item) {
        xingChoseFragment.setChexiItem(item);
        mViewPager.setCurrentItem(2);
    }
}
