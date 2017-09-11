package minfo.com.albumlibrary.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;
import java.util.List;

import minfo.com.albumlibrary.R;
import minfo.com.albumlibrary.TreeConstant;
import minfo.com.albumlibrary.utils.CameraUtils;
import minfo.com.albumlibrary.utils.ImageOptions;
import minfo.com.albumlibrary.utils.ImageUtils;
import minfo.com.albumlibrary.utils.LG;
import minfo.com.albumlibrary.utils.ScreenUtils;
import minfo.com.albumlibrary.utils.ToastUtils;

public class PhotosAdapter extends ZkListAdapter<String>{
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	public static final int ITEM_TYPE_ONE = 0;
	public static final int ITEM_TYPE_TWO = 1;
	public static final int ITEM_TYPE_COUNT = 2;
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new LinkedList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;
	/**拍照图片储存路径*/
	private String mImagePath = null;
	int selected = 0;
	int itemSize = 90;
	
	public PhotosAdapter(Context context, List<String> objects, String dirPath, int selected) {
		super(context);
		this.mDirPath = dirPath;
		this.selected = selected;
		itemSize = (ScreenUtils.getScreenWidth(context)-ScreenUtils.dip2px(context, 6)) / 3;
		String add = "str_caramel";
		super.add(add);
		super.addAll(objects);
	}

	@Override
	public int getItemViewType(int position) {
		if(position == 0) {
			return ITEM_TYPE_TWO;
		}
		else {
			return ITEM_TYPE_ONE;
		}
	}

	@Override
	public int getViewTypeCount() {
		return ITEM_TYPE_COUNT;
	}


	@Override
	public View getZkView(int position, View convertView, ViewGroup parent) {
		ViewHolderAdd holderAdd = null;
		ViewHolderPhoto holderPhoto = null;
		int type = getItemViewType(position);
		if(convertView == null) {
			switch (type) {
			case ITEM_TYPE_ONE:
				holderPhoto = new ViewHolderPhoto();
				convertView = getInflater().inflate(R.layout.activity_album_choose_photo_item1, parent, false);
				holderPhoto.imgPhoto = (ImageView) convertView.findViewById(R.id.activity_album_choose_photo_item1_image);
				holderPhoto.imgStatu = (ImageButton) convertView.findViewById(R.id.activity_album_choose_photo_item1_select);
				ViewGroup.LayoutParams params = holderPhoto.imgPhoto.getLayoutParams();
				params.width = itemSize;
				params.height = itemSize;
				holderPhoto.imgPhoto.setLayoutParams(params);
				convertView.setTag(holderPhoto);
				break;
			case ITEM_TYPE_TWO:
				holderAdd = new ViewHolderAdd();
				convertView = getInflater().inflate(R.layout.activity_album_choose_photo_item2, parent, false);
				holderAdd.imgAdd = (ImageView) convertView.findViewById(R.id.activity_album_choose_photo_item2_image);
				ViewGroup.LayoutParams params1 = holderAdd.imgAdd.getLayoutParams();
				params1.width = itemSize;
				params1.height = itemSize;
				holderAdd.imgAdd.setLayoutParams(params1);
				convertView.setTag(holderAdd);
				break;
			}
		}
		else {
			switch(type) {
			case ITEM_TYPE_ONE:
				holderPhoto = (ViewHolderPhoto) convertView.getTag();
				break;
			case ITEM_TYPE_TWO:
				holderAdd = (ViewHolderAdd) convertView.getTag();
				break;
			}
		}
		
		String item = getmObjects().get(position);
		
		switch (type) {
		case ITEM_TYPE_ONE:
			setImg(holderPhoto, item, position);
			break;
		case ITEM_TYPE_TWO:
			setAdd(holderAdd, item, position);
			break;

		}
		
		return convertView;
	}

	private void setImg(final ViewHolderPhoto holderPhoto, final String item, final int position) {
		mImageLoader.displayImage("file://"+mDirPath + "/"+item, holderPhoto.imgPhoto, ImageOptions.getAlbumPictureOptions());
		holderPhoto.imgPhoto.setOnClickListener(new OnClickListener()
			{
				//选择，则将图片变暗，反之则反之
				@Override
				public void onClick(View v)
				{
	
					// 已经选择过该图片
					if (mSelectedImage.contains(mDirPath + "/" + item))
					{
						LG.i(mDirPath + "/" + item);
						mSelectedImage.remove(mDirPath + "/" + item);
						holderPhoto.imgStatu.setImageResource(R.drawable.album_photo_checkbox_normal);
						holderPhoto.imgPhoto.setColorFilter(null);
					} else
					// 未选择该图片
					{
						if(mSelectedImage.size() + selected >= TreeConstant.ALBUM_CHOOSE_PHOTO_MAX) {
							ToastUtils.showShort(getContext(), "上传图片已达上限");
							return;
						}
						mSelectedImage.add(mDirPath + "/" + item);
						LG.i(mDirPath + "/" + item);
						holderPhoto.imgStatu.setImageResource(R.drawable.album_photo_checkbox_checked);
						holderPhoto.imgPhoto.setColorFilter(Color.parseColor("#77000000"));
					}
	
				}
			});
		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item))
		{
			holderPhoto.imgStatu.setImageResource(R.drawable.album_photo_checkbox_checked);
			holderPhoto.imgPhoto.setColorFilter(Color.parseColor("#77000000"));
		}
		else {
			holderPhoto.imgStatu.setImageResource(R.drawable.album_photo_checkbox_normal);
			holderPhoto.imgPhoto.setColorFilter(null);
		}
	}

	private void setAdd(ViewHolderAdd holderAdd, String string, int position) {
		holderAdd.imgAdd.setImageResource(R.drawable.qixiu_xiangji);
		holderAdd.imgAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mSelectedImage.size() + selected >= TreeConstant.ALBUM_CHOOSE_PHOTO_MAX) {
					ToastUtils.showShort(getContext(), "上传图片已达上限");
					return;
				}
				((Activity)getContext()).startActivityForResult(CameraUtils.takePicture(getContext()), CameraUtils.REQUEST_CODE_TAKE_PICTURE);

			}
		});
	}

	class ViewHolderAdd {
		ImageView imgAdd;
	}
	
	class ViewHolderPhoto {
		ImageView imgPhoto;
		ImageButton imgStatu;
	}
	
	/**
	 * 取得拍照图片路径
	 * @return
	 */
	public String getCaramelImagePath() {
		mImagePath = CameraUtils.mCurrentPhotoPath;
		try {
			ImageUtils.rotaingPicture(mImagePath);
		}
		catch (Exception e) {
			e.printStackTrace();
			LG.e("地址还没传过去");
		}
		return mImagePath;
	}
}
