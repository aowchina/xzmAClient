package com.minfo.carrepairseller.adapter.shop;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.utils.UniversalImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fei on 16/4/6.
 */
public class ImgAdapter extends PagerAdapter {
    private List<String> mlist;
    private Context mContext;
    private List<View> views = new ArrayList<>();
    public ImgAdapter(Context context, List<String> list ) {
        this.mlist = list;
        mContext = context;
        addDynamicView();
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
//        ((ViewPager) view).removeView((View) object);
        view.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
//        View newV=new View(mContext);
        View v = views.get(position);
        final ImageView imageView = (ImageView)v.findViewById(R.id.iv_pro);
        UniversalImageUtils.displayImageUseDefOptions(mlist.get(position), imageView);
        view.addView(v);
        return v;
    }
    private void addDynamicView() {

        for (int i = 0; i < mlist.size(); i++) {
            View view=LayoutInflater.from(mContext).inflate(R.layout.layout_viewpager_img,null);
            final ImageView imageView = (ImageView)view.findViewById(R.id.iv_pro);
            UniversalImageUtils.displayImageUseDefOptions(mlist.get(i), imageView);
            Log.e("imjg   ",mlist.get(i));

            views.add(view);
        }
    }
}
