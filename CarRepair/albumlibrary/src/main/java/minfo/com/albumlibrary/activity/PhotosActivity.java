package minfo.com.albumlibrary.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import minfo.com.albumlibrary.R;
import minfo.com.albumlibrary.TreeConstant;
import minfo.com.albumlibrary.adapter.PhotosAdapter;
import minfo.com.albumlibrary.bean.ImageFloder;
import minfo.com.albumlibrary.utils.CameraUtils;
import minfo.com.albumlibrary.utils.LG;
import minfo.com.albumlibrary.utils.ToastUtils;

public class PhotosActivity extends Activity implements ListImageDirPopupWindow.OnImageDirSelected {

	private ProgressDialog mProgressDialog;

	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	/**
	 * 所有的图片
	 */
	private List<String> mImgs = new LinkedList<String>();
	/**
	 * 所有的图片文件
	 */
	private List<File> mImgFiles = new LinkedList<File>();
	private GridView mGirdView;
	private PhotosAdapter mAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	private TextView mChooseDir;
	private TextView mImageCount;
	int totalCount = 0;

	private int mScreenHeight;

	private ListImageDirPopupWindow mListImageDirPopupWindow;
//	String item0 = "str_caramel"; 
	// PopuWindow起始位置控件
	View vPopBegin;
	TextView id_choose_dir;
	int selected_num = 0;
	boolean isFinish = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_choose_photo);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		selected_num = getIntent().getIntExtra(TreeConstant.ALBUM_SELECTED_NUM, 0);
		initView();
		getImages();
		initEvent();

	}
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mProgressDialog.dismiss();
			// 为View绑定数据
			data2View();
			// 初始化展示文件夹的popupWindw
			initListDirPopupWindw();
		}
	};

	/**
	 * 为View绑定数据
	 */
	private void data2View()
	{
		if (mImgDir == null)
		{
			Toast.makeText(getApplicationContext(), "一张图片没扫描到，拍一张吧",
					Toast.LENGTH_SHORT).show();
			mAdapter = new PhotosAdapter(this, mImgs,
					"", selected_num);
			mGirdView.setAdapter(mAdapter);
			mImageCount.setText(totalCount + "张");
			return;
		}
		mImgFiles.clear();
		File[] imgfile = mImgDir.listFiles(filefiter);
	    mImgFiles.addAll(Arrays.asList(imgfile));
	    Collections.sort(mImgFiles, new FileComparator());
		mImgs.clear();
//		mImgs.add(item0);
		for(File file : mImgFiles) {
			mImgs.add(file.getName());
		}
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new PhotosAdapter(this, mImgs,
				mImgDir.getAbsolutePath(), selected_num);
		mGirdView.setAdapter(mAdapter);
		mImageCount.setText(totalCount + "张");
	};

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.activity_album_choose_photo_pop, null));


		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = PhotosActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				LG.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath))
					{
						continue;
					} else
					{
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = 0;
					String[] files = parentFile.list(new FilenameFilter()
					{
						@Override
						public boolean accept(File dir, String filename)
						{
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					});
					if(files != null) {
						picSize = files.length;
					}
					else {
						continue;
					}
					totalCount += picSize;

					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize)
					{
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}

	/**
	 * 初始化View
	 */
	private void initView()
	{
		vPopBegin = findViewById(R.id.v_pop_begin);
		id_choose_dir = (TextView) findViewById(R.id.id_choose_dir);
		mGirdView = (GridView) findViewById(R.id.id_gridView);
		mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) findViewById(R.id.id_total_count);

	}
	
	public void titleBarBack(View v) {
		finish();
	}

	public void titleBarEnsure(View v) {
		if(isFinish) {
			ToastUtils.showShort(this, "已完成");
			return;
		}
		isFinish = true;
		if(PhotosAdapter.mSelectedImage != null) {
		ArrayList<String> items = new ArrayList<String>();
		for(String str :PhotosAdapter.mSelectedImage){
			items.add(str);
		}
		PhotosAdapter.mSelectedImage.clear();
		Intent intent = new Intent();
		intent.putStringArrayListExtra(TreeConstant.ALBUM_CHOOSE_PHOTOS_PATH, items);
		setResult(RESULT_OK, intent);
		isFinish = false;
		finish();
		}
	}

	private void initEvent()
	{
		/**
		 * 为底部的布局设置点击事件，弹出popupWindow
		 */
		id_choose_dir.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
//				mListImageDirPopupWindow
//						.setAnimationStyle(R.style.anim_popup_dir);
				mListImageDirPopupWindow.showAsDropDown(vPopBegin, 0, 0);

				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .5f;
				getWindow().setAttributes(lp);
			}
		});
	}

	@Override
	public void selected(ImageFloder floder)
	{

		mImgDir = new File(floder.getDir());
		mImgFiles.clear();
		File[] imgfile = mImgDir.listFiles(filefiter);
	    mImgFiles.addAll(Arrays.asList(imgfile));
	    Collections.sort(mImgFiles, new FileComparator());
		mImgs.clear();
//		mImgs.add(item0);
		for(File file : mImgFiles) {
			mImgs.add(file.getName());
		}
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new PhotosAdapter(PhotosActivity.this, mImgs,
				mImgDir.getAbsolutePath(), selected_num);
		mGirdView.setAdapter(mAdapter);
		mImageCount.setText(floder.getCount() + "张");
		mChooseDir.setText(floder.getName());
		mListImageDirPopupWindow.dismiss();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case CameraUtils.REQUEST_CODE_TAKE_PICTURE:
			if(resultCode == RESULT_OK) {
				try {
					if (mAdapter.getCaramelImagePath() == null) {
						Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				catch (Exception e) {
					Toast.makeText(getApplicationContext(), "获取照片异常，请重新再试", Toast.LENGTH_SHORT).show();
					return;
				}
//				if(mAdapter.getCaramelImagePath() == null) {
//					Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
//					return;
//				}
			
			ArrayList<String> items = new ArrayList<>();
			items.add(mAdapter.getCaramelImagePath());
			Intent intent = new Intent();
			intent.putStringArrayListExtra(TreeConstant.ALBUM_CHOOSE_PHOTOS_PATH, items);
			setResult(RESULT_OK, intent);
			finish();
			}
			else {
				ToastUtils.showShort(this, "取消");
				return;
			}
			break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	 private FileFilter filefiter = new FileFilter(){
		  
		 @SuppressLint("DefaultLocale") @Override
		 public boolean accept(File f) {
		     String tmp = f.getName().toLowerCase();
		     if(tmp.endsWith(".png")||tmp.endsWith(".jpg")  
		             ||tmp.endsWith(".jpeg")){  
		         return true;  
		     }  
		     return false;  
		 }  
		       
		   };  
		     
	private class FileComparator implements Comparator<File> {
		   
		 @Override
		public int compare(File lhs, File rhs) {
			 return lhs.lastModified() == rhs.lastModified() ? 0 : (lhs.lastModified() < rhs.lastModified() ? 1 : -1);
		 } 
	}

}
