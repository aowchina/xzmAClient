package com.minfo.carrepairseller.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 基于单例的通用的viewHolder
 * @author liujing
 *
 */
public class BaseViewHolder {

	private SparseArray<View> views;
	private View convertView;

	private BaseViewHolder(Context context, ViewGroup parent, int LayoutId,
			int position) {
		this.views = new SparseArray<View>();
		convertView = LayoutInflater.from(context).inflate(LayoutId, parent,
				false);
		convertView.setTag(this);
	}

	public static BaseViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {

		if (convertView == null) {
			return new BaseViewHolder(context, parent, layoutId, position);
		}
		return (BaseViewHolder) convertView.getTag();
	}

	public <T extends View> T getView(int viewId) {

		View view = views.get(viewId);
		if (view == null) {
			view = convertView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return convertView;
	}
	public BaseViewHolder setText(int viewId,String text){
		TextView tvView = getView(viewId);
		tvView.setText(text);
		return this;
	}

}
