package com.minfo.carrepairseller.activity.goodmanager;

import android.content.Intent;
import android.os.Bundle;
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
import com.minfo.carrepairseller.fragment.GoodManagerFragment;
import com.minfo.carrepairseller.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class GoodManagerActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvTitle;
    private LinearLayout llSelling;
    private LinearLayout llLaid;
    private LinearLayout llReview;
    private LinearLayout llReviewNopass;
    private ViewPager mViewPager;
    private FragmentViewPagerAdapter mAdapter;
    private List<Fragment> mFragments;

    private TextView[] mTabViews;
    private ImageView mTabline;
    private int mTabLineWidth;
    private int mCurrentPageIndex;
    private static final int[] FRAGMENT_INDEX = { 0, 1, 2, 3};
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_good_manager);
//        setContentView(R.layout.activity_good_manager);
    }

    @Override
    protected void findViews() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        mTabViews = new TextView[4];
        mViewPager = (ViewPager) findViewById(R.id.activity_good_manager_viewpager);
        mTabViews[0] = (TextView) findViewById(R.id.tv_selling);
        mTabViews[1] = (TextView) findViewById(R.id.tv_laid);
        mTabViews[2] = (TextView) findViewById(R.id.tv_review);
        mTabViews[3] = (TextView) findViewById(R.id.tv_review_nopass);
        
        llSelling = (LinearLayout) findViewById(R.id.activity_good_manager_tab_selling);
        llLaid = (LinearLayout) findViewById(R.id.activity_good_manager_tab_laid);
        llReview = (LinearLayout) findViewById(R.id.activity_good_manager_tab_review);
        llReviewNopass = (LinearLayout) findViewById(R.id.activity_good_manager_tab_review_nopass);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("商品管理");
    }

    @Override
    protected void initViews() {
        initTabLine();
//        for(TextView tv : mTabViews) {
//            tv.setOnClickListener(this);
//        }
        ivBack.setOnClickListener(this);
        llSelling.setOnClickListener(this);
        llLaid.setOnClickListener(this);
        llReview.setOnClickListener(this);
        llReviewNopass.setOnClickListener(this);

        mFragments = new ArrayList<>();
        for(int i = 0; i < 4; i ++) {
            BaseFragment fragment = new GoodManagerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.GOOD_STATE, i);
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
    }

    /**
     * 初始化标题选中线宽度
     */
    private void initTabLine() {
        mTabline = (ImageView) findViewById(R.id.activity_good_manager_tab_line);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mTabLineWidth = outMetrics.widthPixels / 4;
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

            case R.id.activity_good_manager_tab_selling:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[0]);
                break;
            case R.id.activity_good_manager_tab_laid:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[1]);
                break;
            case R.id.activity_good_manager_tab_review:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[2]);
                break;
            case R.id.activity_good_manager_tab_review_nopass:
                mViewPager.setCurrentItem(FRAGMENT_INDEX[3]);
                break;
        }
    }
}
