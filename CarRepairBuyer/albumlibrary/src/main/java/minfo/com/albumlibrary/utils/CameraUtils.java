package minfo.com.albumlibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

//import com.weiqiji.jinrongtree.PreferencesManager;
//import com.weiqiji.jinrongtree.bean.AppInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraUtils {
	public final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "xzmbuyer" + File.separator + "images" + File.separator;
	public static final int REQUEST_CODE_TAKE_PICTURE = 101;
	public static final int REQUEST_CODE_SELECT_PICTURE = 102;
	public static final int REQUEST_CODE_SELECT_PICTURES = 103;
	public static final int REQUEST_CODE_CROP_PICTURE = 104;
	public static final int REQUEST_CODE_TAKE_PICTURE2 = 105;
	public static final int REQUEST_CODE_SELECT_PICTURE2 = 106;
	public static final int REQUEST_CODE_SELECT_PICTURES2 = 107;
	public static final int REQUEST_CODE_CROP_PICTURE2 = 108;
	public static String mCurrentPhotoPath;
	public static File mCurrentFile;
	public static int num = 0;

	public static Intent selectPictureContent(Context context) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		return intent;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static Intent selectPictureDocument(Context context) {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.setType("image/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		return intent;
	}
	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 * @param imageUri
	 * @author yaoxing
	 * @date 2014-10-12
	 */
	@TargetApi(19)
	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	public static Intent cropPicture(Context context, Uri uri, int width,
									 int height) {
		mCurrentFile = createImageFile(context);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("output", Uri.fromFile(mCurrentFile));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", width);// 裁剪框比例
		intent.putExtra("aspectY", height);
		intent.putExtra("outputX", width);// 输出图片大小
		intent.putExtra("outputY", height);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		return intent;
	}

	public static Intent cropPicture(Context context, Uri uri) {
		return cropPicture(context, uri, 200, 200);
	}

	public static Intent selectPicture(Context context) {
		/*
		 * if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.
		 * KITKAT){ return selectPictureDocument(context); }else{ return
		 * selectPictureContent(context); }
		 */
		return selectPictureContent(context);
	}

	/**
	 * 调用相机拍照 可选择已安装相机
	 * 
	 * @param context
	 * @return
	 */
	public static Intent takePicture(Context context) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mCurrentFile = createImageFile(context);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(mCurrentFile));
		return takePictureIntent;
	}

//	/**
//	 * 调用系统相机拍照1
//	 *
//	 * @param context
//	 * @return
//	 */
//	public static Intent takePictureSYSCamera(Context context) {
//		PreferencesManager manager = PreferencesManager.getInstance(context);
//		String caram = manager.getSYSCameraPackage();
//		Intent takePictureIntent = null;
//		if (caram != "") {
//			String activityName = "";
//			if (caram.equals("com.android.gallery3d")) {
//				activityName = "com.android.camera.CameraLauncher";
//			} else {
//				String c[] = caram.split("\\.");
//				String str = c[c.length - 1];
//				str = str.replaceFirst(str.substring(0, 1), str.substring(0, 1)
//						.toUpperCase());
//				activityName = caram + "." + str;
//			}
//			try {
//				takePictureIntent = new Intent();
//				ComponentName comp = new ComponentName(caram, activityName);
//				takePictureIntent.setComponent(comp);
//				takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//				mCurrentFile = createImageFile(context);
//				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//						Uri.fromFile(mCurrentFile));
//
//			} catch (Exception e) {
//				takePictureIntent = takePictureBySYSCamera(context);
//
//			}
//
//		} else {
//			takePictureIntent = takePictureBySYSCamera(context);
//		}
//
//		return takePictureIntent;
//	}

	/**
	 * 调用系统相机拍照2
	 * 
	 * @param context
	 * @return
	 */
	public static Intent takePictureBySYSCamera(Context context) {
		// Intent takePictureIntent = new
		// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Intent takePictureIntent = new Intent(
				"android.media.action.IMAGE_CAPTURE");
		takePictureIntent.addCategory(Intent.CATEGORY_DEFAULT);
		mCurrentFile = createImageFile(context);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(mCurrentFile));
		takePictureIntent.putExtra("actionId", "camera");

		return takePictureIntent;
	}

	private static File createImageFile(Context context) {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			ToastUtils.showShort(context, "无法保存图片，请检查SD开是否挂载");
			return null;
		}
//		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss",
//				Locale.ENGLISH).format(new Date());
		String timeStamp = "carme";
		// 照片命名
		String cropFileName = "xzmb_" + timeStamp;
		mCurrentPhotoPath = FILE_SAVEPATH + cropFileName+(num++) + ".jpg";
		File file = new File(mCurrentPhotoPath);
		return file;
	}

//	/**
//	 * 获取系统相机包名
//	 *
//	 * @param context
//	 * @return
//	 */
//	public static String getCaramName(Context context) {
//		String caramName = "";
//		List<PackageInfo> packages = context.getPackageManager()
//				.getInstalledPackages(0);
//		for (int i = 0; i < packages.size(); i++) {
//			PackageInfo packageInfo = packages.get(i);
//			AppInfo tmpInfo = new AppInfo();
//			tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
//					context.getPackageManager()).toString();
//			tmpInfo.packageName = packageInfo.packageName;
//			tmpInfo.versionName = packageInfo.versionName;
//			tmpInfo.versionCode = packageInfo.versionCode;
//			tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context
//					.getPackageManager());
//			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
//				if (tmpInfo.packageName.contains("camera")) {
//					caramName = tmpInfo.packageName;
//				} else if ("".equals(caramName)
//						&& tmpInfo.packageName.contains("gallery3d")) {
//					caramName = tmpInfo.packageName;
//				}
//
//			}
//
//		}
//		return caramName;
//	}

	/**
	 * 解决小米手机上获取图片路径为null的情况
	 * @param intent
	 * @return
	 */
	public static Uri geturi(Context context, android.content.Intent intent) {
		Uri uri = intent.getData();
		String type = intent.getType();
		if (uri.getScheme().equals("file") && (type.contains("image/"))) {
			String path = uri.getEncodedPath();
			if (path != null) {
				path = Uri.decode(path);
				ContentResolver cr = context.getContentResolver();
				StringBuffer buff = new StringBuffer();
				buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
						.append("'" + path + "'").append(")");
				Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[]{MediaStore.Images.ImageColumns._ID},
						buff.toString(), null, null);
				int index = 0;
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
					// set _id value
					index = cur.getInt(index);
				}
				if (index == 0) {
					// do nothing
				} else {
					Uri uri_temp = Uri
							.parse("content://media/external/images/media/"
									+ index);
					if (uri_temp != null) {
						uri = uri_temp;
					}
				}
			}
		}
		return uri;
	}
}
