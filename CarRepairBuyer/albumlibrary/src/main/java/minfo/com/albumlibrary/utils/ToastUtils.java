package minfo.com.albumlibrary.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static void showLong(Context context, int resId) {
        showLong(context, context.getString(resId));
    }
    public static void showShort(Context context, String message){
    	Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void showShort(Context context, int resId){
    	showShort(context, context.getString(resId));
    }
}
