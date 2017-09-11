package minfo.com.albumlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
	public final static String SDCARD_MNT = File.separator + "mnt"
			+ File.separator + "sdcard";
	public final static String SDCARD = File.separator + "sdcard";

	/**
	 * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
	 * 
	 * @param mUri
	 * @return
	 */
	public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
		String filePath = null;

		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);

		String pre1 = "file://" + SDCARD + File.separator;
		String pre2 = "file://" + SDCARD_MNT + File.separator;

		if (mUriString.startsWith(pre1)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre1.length());
		} else if (mUriString.startsWith(pre2)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre2.length());
		}
		return filePath;
	}

	/**
	 * 通过uri获取文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getAbsoluteImagePath(Activity context, Uri uri) {
		String imagePath = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.managedQuery(uri, proj, // Which columns to
														// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static Bitmap getImageThumbnail(String filePath, int w, int h) {
		int degree = readPictureDegree(filePath);
		Bitmap bitmapOrg = BitmapFactory.decodeFile(filePath);
		Bitmap BitmapOrg = rotaingImageView(degree, bitmapOrg);
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;
		// calculate the scale
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// resize the Bitmap
		matrix.postScale(scaleWidth, scaleHeight);
		// if you want to rotate the Bitmap
		// matrix.postRotate(45);

		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
				height, matrix, true);
		return resizedBitmap;

	}

	// 将Bitmap转换成InputStream
	public static InputStream bitmapToInputStream(Bitmap bm, String type) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if ("image/jpg".equals(type)) {
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		} else {
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		}
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}
	
	// 将Bitmap转换成InputStream
		public static InputStream bitmapToInputStream(Bitmap bm, int quality, String type) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if ("image/jpg".equals(type) || "image/jpeg".equals(type)) {
				bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			} else {
				bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
			}
			InputStream is = new ByteArrayInputStream(baos.toByteArray());
			bm.recycle();
			return is;
		}

	// 将byte[]转换成InputStream
	public static InputStream Byte2InputStream(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return bais;
	}

	// 将InputStream转换成byte[]
	public static byte[] InputStream2Bytes(InputStream is) {
		String str = "";
		byte[] readByte = new byte[1024];
		int readCount = -1;
		try {
			while ((readCount = is.read(readByte, 0, 1024)) != -1) {
				str += new String(readByte).trim();
			}
			return str.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 将Bitmap转换成InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	// 将Bitmap转换成InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	// 将InputStream转换成Bitmap
	public static Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	// Drawable转换成InputStream
	public InputStream Drawable2InputStream(Drawable d) {
		Bitmap bitmap = this.drawable2Bitmap(d);
		return this.Bitmap2InputStream(bitmap);
	}

	// InputStream转换成Drawable
	public Drawable InputStream2Drawable(InputStream is) {
		Bitmap bitmap = this.InputStream2Bitmap(is);
		return this.bitmap2Drawable(bitmap);
	}

	// Drawable转换成byte[]
	public byte[] Drawable2Bytes(Drawable d) {
		Bitmap bitmap = this.drawable2Bitmap(d);
		return this.Bitmap2Bytes(bitmap);
	}

	// byte[]转换成Drawable
	public Drawable Bytes2Drawable(byte[] b) {
		Bitmap bitmap = this.Bytes2Bitmap(b);
		return this.bitmap2Drawable(bitmap);
	}

	// Bitmap转换成byte[]
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// byte[]转换成Bitmap
	public Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	// Drawable转换成Bitmap
	public Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	// Bitmap转换成Drawable
	public Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		Drawable d = (Drawable) bd;
		return d;
	}

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 *
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	public static Bitmap getSmallBitmap(String filePath) {
		return getSmallBitmap(filePath, 480, 800);
	}

	public static Bitmap getSmallBitmap(Context context, Uri uri, int width,
										int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		ParcelFileDescriptor parcelFileDescriptor = null;
		Bitmap image = null;
		try {
			parcelFileDescriptor = context.getContentResolver()
					.openFileDescriptor(uri, "r");
			FileDescriptor fileDescriptor = parcelFileDescriptor
					.getFileDescriptor();
			BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, width, height);
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null,
					options);
			parcelFileDescriptor.close();
		} catch (FileNotFoundException e) {
			ToastUtils.showLong(context, "没有找到文件");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}

	public static Bitmap getSmallBitmap(Context context, Uri uri) {
		return getSmallBitmap(context, uri, 480, 800);
	}

	/**
	 * 压缩图片返回压缩路径
	 * @param path
	 * @return
	 */
	public static String doPictureGood(String path) {

		// 压缩图片的路径
		String new_path = path;
		File file = new File(path);
		Bitmap bm = null;
		if(file.length() / 1024 < 300) {
			bm = BitmapFactory.decodeFile(new_path);
			LG.i("未压缩");
		}
		else {
			bm = getSmallBitmap(path, 480, 800);
		}
		if(bm == null) {
			return new_path;
		}
		int degree = readPictureDegree(path);
		bm = rotaingImageView(degree, bm);
		FileOutputStream fos;
		try {
			if(file.length() / 1024 < 300) {
				fos = new FileOutputStream(new File(
						getDirPath(), file.getName()));
				bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				new_path = getDirPath()+ file.getName();
			}
			else {
				fos = new FileOutputStream(new File(
						getDirPath(), "small_" + file.getName()));
				bm.compress(Bitmap.CompressFormat.JPEG, 60, fos);
				new_path = getDirPath()+"small_" + file.getName();
			}
			bm.recycle();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new_path;
	}
	/**
	 * 压缩图片返回压缩路径
	 * @param path
	 * @return
	 */
	public static String doPicture(String path) {

		// 压缩图片的路径
		String new_path = path;
		File file = new File(path);
		Bitmap bm = null;
		if(file.length() / 1024 < 150) {
			bm = BitmapFactory.decodeFile(new_path);
			LG.i("未压缩");
		}
		else {
			bm = getSmallBitmap(path, 480, 800);
		}
		if(bm == null) {
			return new_path;
		}
		int degree = readPictureDegree(path);
		bm = rotaingImageView(degree, bm);
		FileOutputStream fos;
		try {
			if(file.length() / 1024 < 150) {
				fos = new FileOutputStream(new File(
						getDirPath(), file.getName()));
				bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				new_path = getDirPath()+ file.getName();
			}
			else {
				fos = new FileOutputStream(new File(
						getDirPath(), "small_" + file.getName()));
				bm.compress(Bitmap.CompressFormat.JPEG, 60, fos);
				new_path = getDirPath()+"small_" + file.getName();
			}
			bm.recycle();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new_path;
	}
	/**
	 * 压缩图片返回压缩路径
	 * @param path
	 * @return
	 */
	public static String doPicture(String path, int width, int height) {

		// 压缩图片的路径
		String new_path = path;
		File file = new File(path);
		Bitmap bm = null;
		if(file.length() / 1024 < 150) {
			bm = BitmapFactory.decodeFile(new_path);
			LG.i("未压缩");
		}
		else {
			bm = getSmallBitmap(path, width, height);
		}
		if(bm == null) {
			return new_path;
		}
		int degree = readPictureDegree(path);
		bm = rotaingImageView(degree, bm);
		FileOutputStream fos;
		try {
			if(file.length() / 1024 < 150) {
				fos = new FileOutputStream(new File(
						getDirPath(), file.getName()));
				bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				new_path = getDirPath()+ File.separator+ file.getName();
			}
			else {
				fos = new FileOutputStream(new File(
						getDirPath(), "small_" + file.getName()));
				bm.compress(Bitmap.CompressFormat.JPEG, 60, fos);
				new_path = getDirPath()+ File.separator+"small_" + file.getName();
			}
			bm.recycle();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new_path;
	}
	
	public static InputStream doPicture2InputStream(String path) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		
		Bitmap bm =  BitmapFactory.decodeFile(path, options);
		String type = "image/jpg";
		try {
			if("png".equals(path.substring(path.indexOf(".")+1))) {
				type = "image/png";
			}
		}
		catch(Exception e) {
			
		}
		return bitmapToInputStream(bm, 60, type);
	}
	
	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	/**
	 * 图片按比例大小压缩方法（根据路径获取图片并压缩）
	 * @param srcPath
	 * @return Bitmap
	 */
	public static Bitmap compressImageByRatio(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 800f;//这里设置高度为800f  
        float ww = 480f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放 
        	while(w > hh) {
        		w = w>>1;
        		be = be<<1;
        	}
        } else if (w <= h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            while(h > hh) {
        		h = h>>1;
        		be = be<<1;
        	}
        }  
        LG.i("be="+be);
        if (be <= 1)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }
	
	/**
	 * 获取图片储存路径
	 * @return
	 */
	private static String getDirPath() {
		String dirPath = null;
		// 判断sd卡是否存在
		if(Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED)) {
			dirPath = Environment.getExternalStorageDirectory().toString() + File.separator + "JinRongShu" + File.separator + "images"+ File.separator;//获取跟目录
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
		else {
			LG.i("sd卡不存在！");
		}
		
		return dirPath;
	}
	
	/**
	 * 纠正拍照后照片的旋转角
	 * @param path
	 */
	public static void rotaingPicture(String path) {
		int degree = readPictureDegree(path);
		if(degree == 0) {
			return;
		}
		File file = new File(path);
		Bitmap bm = null;
		
		FileOutputStream fos;
		try {
			if(file.length() / 1024 < 100) {
				bm = BitmapFactory.decodeFile(path);
				LG.i("未压缩");
			}
			else {
				bm = getSmallBitmap(path, 480, 800);
			}
			if(bm == null) {
				return;
			}
			bm = rotaingImageView(degree, bm);
			fos = new FileOutputStream(new File(path));
			if(file.length() / 1024 < 100) {
				bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			}
			else {
				bm.compress(Bitmap.CompressFormat.JPEG, 60, fos);
			}
			bm.recycle();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存资源文件到本地
	 */
	public static String saveResTolocal(Resources res, int resId, String name) {
		String path = null;
		Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(
					getDirPath(), name+".jpg"));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			path = getDirPath()+name + ".jpg";
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return path;
	}
	
}
