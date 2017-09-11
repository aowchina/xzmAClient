package com.minfo.carrepairseller.adapter.query;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.entity.query.Product_Details_Entity;
import com.minfo.carrepairseller.http.VolleyHttpClient;
import com.minfo.carrepairseller.widget.LoadingDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth zhao
 * @function 产品详细列表适配
 */
public class ClassificationDetailAdapter extends BaseAdapter {
    private String TAG = "ClassificationDetailAdapter";
    private LinearLayout details_body;
    private LayoutInflater layoutInflater = null;

    private Context mCtx;
    private List<Product_Details_Entity> mlist;

    private int num;
    private ViewGroup anim_mask_layout;
    private ImageView buyImg;
    private ImageView buyIcon;
    private Map<Integer, Boolean> isFrist;//加载图片动画是否第一次
    //接口
    private Map<String, String> params = new HashMap();
    private VolleyHttpClient httpClient;
    //加载框
    private static LoadingDialog dialog;
    private boolean isCanClick = true;



    public ClassificationDetailAdapter(Context ctx, List<Product_Details_Entity> list) {

        mCtx = ctx;
        mlist = list;

        layoutInflater = LayoutInflater.from(ctx);
        isFrist = new HashMap<Integer, Boolean>();
        dialog = new LoadingDialog(mCtx);
        dialog.setMessage("加载中");
    }


    @Override
    public int getCount() {
        if (mlist != null) {
            return mlist.size();
        }
        else {
            return 0;
        }
    }


    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint("NewApi") @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassificationDetails classificationDetails = null;
        if (convertView == null) {
            classificationDetails = new ClassificationDetails();
            convertView = layoutInflater.inflate(R.layout.item_epc_detail, null);
            classificationDetails.ivImg = (ImageView) convertView.findViewById(R.id.iv_epc);
            classificationDetails.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);


            convertView.setTag(classificationDetails);
        }
        else {
            classificationDetails
                    = (ClassificationDetails) convertView.getTag();
        }

                classificationDetails.tvTitle.setText("name");

//        UniversalImageUtils.displayImageUseDefOptions(mlist.get(position).getSimg(), classificationDetails.classificationdetails_img);


        return convertView;
    }


    private class ClassificationDetails {
        private ImageView ivImg;
        private TextView tvTitle;
    }


}
