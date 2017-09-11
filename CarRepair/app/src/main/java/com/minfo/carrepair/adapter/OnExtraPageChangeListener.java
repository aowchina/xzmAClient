package com.minfo.carrepair.adapter;

public interface OnExtraPageChangeListener {
	public void onExtraPageScrolled(int position, float positionOffset,
									int positionOffsetPx);
	public void onExtraPageSelected(int position);
	public void onExtraPageScrollStateChanged(int arg0);
}
