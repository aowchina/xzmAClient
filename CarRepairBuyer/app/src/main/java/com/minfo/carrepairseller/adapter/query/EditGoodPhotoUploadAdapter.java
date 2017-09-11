package com.minfo.carrepairseller.adapter.query;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.entity.query.PictureItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.util.ArrayList;

import minfo.com.albumlibrary.adapter.ZkListAdapter;
import minfo.com.albumlibrary.utils.ImageOptions;
import minfo.com.albumlibrary.utils.ScreenUtils;

public class EditGoodPhotoUploadAdapter extends ZkListAdapter<PictureItem> {
	private ImageLoader mImageLoader;
	public static final int ITEM_TYPE_ONE = 0;
	public static final int ITEM_TYPE_TWO = 1;
	public static final int ITEM_TYPE_COUNT = 2;
//	private Context context;
	private int width;
	private int height;
	public static ArrayList<String> imgs = new ArrayList<>();
	private boolean visibileFlag;

	public int getWidth(){
		return width;
	}
	public EditGoodPhotoUploadAdapter(Context context) {
		super(context);
//		this.context = context;
		width = (ScreenUtils.getScreenWidth(context) - ScreenUtils.dip2px(context, 50))/4;
		height = width;
		mImageLoader = ImageLoader.getInstance();
		PictureItem mItem = new PictureItem();
		mItem.setAdd(true);
		super.add(mItem);
	}
	public EditGoodPhotoUploadAdapter(Context context, boolean visibileFlag) {
		super(context);
//		this.context = context;
		width = (ScreenUtils.getScreenWidth(context) - ScreenUtils.dip2px(context, 50)) / 4;
		height = width;
		mImageLoader = ImageLoader.getInstance();
		PictureItem mItem = new PictureItem();
		mItem.setAdd(true);
		this.visibileFlag = visibileFlag;
		super.add(mItem);

	}


	static class AddViewHolder {
		public ImageView mImg;
	}

	static class ViewHolder {
		public ImageView mImg;
		public ImageView mStatus;
	}

	@Override
	public int getItemViewType(int position) {
		boolean click = getItem(position).isAdd();
		if (click) {
			return ITEM_TYPE_ONE;
		} else {
			return ITEM_TYPE_TWO;
		}
	}

	@Override
	public int getViewTypeCount() {
		return ITEM_TYPE_COUNT;
	}

	@Override
	public View getZkView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		// 自定义视图
		ViewHolder mViewHolder = null;
		AddViewHolder mAddViewHolder = null;
		// 初始化视图
		if (convertView == null) {
			switch (type) {
			case ITEM_TYPE_ONE:
				convertView = getInflater().inflate(
						R.layout.component_select_picture_item_add, null);
				mAddViewHolder = new AddViewHolder();
				mAddViewHolder.mImg = (ImageView) convertView
						.findViewById(R.id.component_select_picture_item_add_img);
				ViewGroup.LayoutParams params = mAddViewHolder.mImg.getLayoutParams();
				params.height = height;
				params.width = width;
				mAddViewHolder.mImg.setLayoutParams(params);
				convertView.setTag(mAddViewHolder);
				break;
			case ITEM_TYPE_TWO:
				convertView = getInflater().inflate(
						R.layout.component_select_picture_item, null);
				mViewHolder = new ViewHolder();
				mViewHolder.mImg = (ImageView) convertView
						.findViewById(R.id.component_select_picture_item_img);
				mViewHolder.mStatus = (ImageView) convertView
						.findViewById(R.id.component_select_picture_item_status);
				if (visibileFlag){
					mViewHolder.mStatus.setVisibility(View.GONE);
				}else {
					mViewHolder.mStatus.setVisibility(View.VISIBLE);

				}
				ViewGroup.LayoutParams params1 = mViewHolder.mImg.getLayoutParams();
				params1.height = height;
				params1.width = width;
				mViewHolder.mImg.setLayoutParams(params1);
				convertView.setTag(mViewHolder);
				break;

			default:
				break;
			}

		} else {
			switch (type) {
			case ITEM_TYPE_ONE:
				mAddViewHolder = (AddViewHolder) convertView.getTag();
				break;
			case ITEM_TYPE_TWO:
				mViewHolder = (ViewHolder) convertView.getTag();
				break;

			default:
				break;
			}

		}
		PictureItem mItem = getItem(position);
		switch (type) {
		case ITEM_TYPE_ONE:
			setAdd(mAddViewHolder, mItem, position);
			break;
		case ITEM_TYPE_TWO:
			setImg(mViewHolder, mItem, position);
			break;

		default:
			break;
		}
		return convertView;
	}

	private void setAdd(AddViewHolder mViewHolder, PictureItem mItem,
			int position) {
		mViewHolder.mImg
				.setBackgroundResource(R.drawable.dangan_add_photo);
		if (visibileFlag){
			mViewHolder.mImg.setVisibility(View.GONE);
		}else {
			mViewHolder.mImg.setVisibility(View.VISIBLE);

		}
	}

	private void setImg(ViewHolder mViewHolder,final PictureItem mItem,
			final int position) {
		String url = "";
		if(mItem.isNet()) {
			url = mItem.getImgUrl();
			//UniversalImageUtils.displayImageUseDefOptions(item.getIcon(), imageView);

		}
		else {
			url = "file://" + mItem.getImgUrl();
			removeFromCache(url, mImageLoader);
		}
		// 判断是否选中
		mImageLoader.displayImage(url, mViewHolder.mImg,
				ImageOptions.getAlbumPictureOptions());
		if (visibileFlag){
			mViewHolder.mStatus.setVisibility(View.GONE);
		}else {
			mViewHolder.mStatus.setVisibility(View.VISIBLE);

		}
		mViewHolder.mStatus.setVisibility(View.VISIBLE);
		mViewHolder.mStatus.setBackgroundResource(R.drawable.qx_close);
		mViewHolder.mStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mItem.isNet()){
					imgs.add(mItem.getImgUrl());
				}
				getmObjects().remove(position);
				if(getCount() == 7) {
					if(!getItem(getCount()-1).isAdd()) {
						PictureItem mItem = new PictureItem();
						mItem.setAdd(true);
						getmObjects().add(mItem);
					}
				}
				notifyDataSetChanged();
			}
		});
	}

	/**
	 * 清除单张图片缓存
	 * @param url
	 */
	public static void removeFromCache(String url, ImageLoader imageLoader) {
		DiskCacheUtils.removeFromCache(url, imageLoader.getDiskCache());
		MemoryCacheUtils.removeFromCache(url, imageLoader.getMemoryCache());
	}
}
