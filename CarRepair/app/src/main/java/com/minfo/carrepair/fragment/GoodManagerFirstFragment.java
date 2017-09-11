package com.minfo.carrepair.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepair.R;
import com.minfo.carrepair.adapter.FragmentViewPagerAdapter;
import com.minfo.carrepair.adapter.OnExtraPageChangeListener;
import com.minfo.carrepair.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodManagerFirstFragment extends BaseFragment implements View.OnClickListener{
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

    public GoodManagerFirstFragment() {
        // Required empty public constructor
    }

    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.fragment_good_manager_first, null);
        Bundle bundle = getArguments();
        if(bundle != null) {
            type = bundle.getInt("type", 0);
        }
        mTabViews = new TextView[4];
        mViewPager = (ViewPager) view.findViewById(R.id.activity_good_manager_viewpager);
        mTabViews[0] = (TextView) view.findViewById(R.id.tv_selling);
        mTabViews[1] = (TextView) view.findViewById(R.id.tv_laid);
        mTabViews[2] = (TextView) view.findViewById(R.id.tv_review);
        mTabViews[3] = (TextView) view.findViewById(R.id.tv_review_nopass);

        llSelling = (LinearLayout) view.findViewById(R.id.activity_good_manager_tab_selling);
        llLaid = (LinearLayout) view.findViewById(R.id.activity_good_manager_tab_laid);
        llReview = (LinearLayout) view.findViewById(R.id.activity_good_manager_tab_review);
        llReviewNopass = (LinearLayout) view.findViewById(R.id.activity_good_manager_tab_review_nopass);

        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText("商品管理");


        initTabLine(view);
//        for(TextView tv : mTabViews) {
//            tv.setOnClickListener(this);
//        }
        ivBack.setVisibility(View.GONE);
//        ivBack.setOnClickListener(this);
        llSelling.setOnClickListener(this);
        llLaid.setOnClickListener(this);
        llReview.setOnClickListener(this);
        llReviewNopass.setOnClickListener(this);

        mFragments = new ArrayList<>();
        for(int i = 0; i < 4; i ++) {
            BaseFragment fragment = new GoodManagerFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putInt(Constant.GOOD_STATE, i);
            fragment.setArguments(bundle1);
            mFragments.add(fragment);
        }
        mAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(),
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

            }

            @Override
            public void onExtraPageScrollStateChanged(int arg0) {

            }
        });

        mViewPager.setCurrentItem(type); // 默认选中全部
        return view;
    }

    /**
     * 初始化标题选中线宽度
     */
    private void initTabLine(View view) {
        mTabline = (ImageView) view.findViewById(R.id.activity_good_manager_tab_line);
        Display display = mActivity.getWindow().getWindowManager().getDefaultDisplay();
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
