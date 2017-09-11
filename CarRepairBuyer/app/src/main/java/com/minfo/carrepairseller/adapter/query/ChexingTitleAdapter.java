package com.minfo.carrepairseller.adapter.query;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.entity.query.ChexingEntity;

import java.util.List;

/**
 * Created by MinFo021 on 17/6/8.
 */

public class ChexingTitleAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mCtx;
    private List<ChexingEntity> mList;
    private int selectedPosition = -1;

    public ChexingTitleAdapter(Context ctx, List<ChexingEntity> list) {
        mCtx = ctx;
        mList = list;
        mInflater= LayoutInflater.from(mCtx);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.item_epc_title, null);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.ll_continer);
        TextView gradientTextView = (TextView) convertView.findViewById(R.id.gt_kind);

//		TextView title = (TextView) convertView.findViewById(R.id.textview_title);
//		View redLine = (View) convertView.findViewById(R.id.view_line_red);
//
//		if(selectedPosition == position){
//			title.setTextColor(Color.BLUE);
//			redLine.setVisibility(View.VISIBLE);
//			layout.setBackgroundColor(Color.rgb(245,245,245));
//		}else{
//			title.setTextColor(Color.BLACK);
//			redLine.setVisibility(View.GONE);
//			layout.setBackgroundColor(Color.TRANSPARENT);
//		}
//		title.setText(mList.get(position).getName());


        gradientTextView.setText(mList.get(position).getName());
        if (position == selectedPosition) {

            layout.setBackgroundColor(Color.TRANSPARENT);

        } else {

            layout.setBackgroundColor(Color.rgb(245, 245, 245));

        }


        return convertView;
    }
}
