package com.minfo.carrepairseller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用的adapter
 * @author liujing
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context context;
	protected List<T> datas;
	protected int layoutItemId;

	public CommonAdapter(Context context, List<T> datas, int layoutItemId) {
		this.context = context;
		this.datas = datas;
		this.layoutItemId = layoutItemId;
	}
	public void addAll(List<T> datas){
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public T getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {//1定位，2没有定位
		// current menu type
		Map map = new HashMap();
		if(getItem(position) instanceof Map){
			map = (Map) getItem(position);
		}
		String type = ((String) map.get("isLocated"));
		if(type!=null){
			return type.equals("Y")==true?0:1;
		}else{
			return -1;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BaseViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
		convert(viewHolder, getItem(position),position);
		return viewHolder.getConvertView();
	}

	public abstract void convert(BaseViewHolder helper, T item,int position);

	private BaseViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return BaseViewHolder.get(context, convertView, parent, layoutItemId,position);
	}
}
