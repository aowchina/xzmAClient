package com.minfo.carrepairseller.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.dialog.BaseDialogView;

/**
 * @description dialog工具类
 * Created by MinFo021 on 17/3/2.
 *
 */

public class DialogUtil {
    /**
     * 创建向上渐入向下渐出的dialog
     * @param context
     * @param view 自定义布局
     * @return
     */
//    @SuppressWarnings("deprecation")
//    public static Dialog showDialogBottom(Context context, BaseDialogView view) {
//
//        final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
//        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        view.setExitDialog(new BaseDialogView.ExitDialog() {
//
//            @Override
//            public void exitDialog() {
//                dlg.dismiss();
//            }
//        });
//        WindowManager m = ((Activity) context).getWindowManager();
//        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
//
//        dlg.setCanceledOnTouchOutside(true);
//        dlg.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        dlg.show();
//        Window w = dlg.getWindow();
//        WindowManager.LayoutParams lp = w.getAttributes();
//        lp.x = 0;
//        final int cMakeBottom = -1000;
//        lp.y = cMakeBottom;
//        lp.gravity = Gravity.BOTTOM;
//        lp.width = d.getWidth();
//        dlg.onWindowAttributesChanged(lp);
//        return dlg;
//    }
//
//    /**
//     * 自定义弹框
//     * @param context
//     * @return
//     */
//    public static Dialog showDialogCenter(Context context,BaseDialogView dialogView){
//        final Dialog dialog =  new Dialog(context, R.style.updatedialog);
//        dialogView.setExitDialog(new BaseDialogView.ExitDialog() {
//
//            @Override
//            public void exitDialog() {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setContentView(dialogView, new LayoutParams(LayoutParams.MATCH_PARENT,
//                LayoutParams.WRAP_CONTENT));
////
////        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
//////p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
////        //宽度设置为全屏
////        WindowManager m =((Activity) context).getWindowManager();
////        DisplayMetrics dm = new DisplayMetrics();
////        m.getDefaultDisplay().getMetrics(dm);
////        p.width  = (int)(dm.widthPixels *0.7);
////        dialog.getWindow().setAttributes(p);
//        dialog.show();
//        return dialog;
//    }
    @SuppressWarnings("deprecation")
    public static Dialog showDialog(Context context, BaseDialogView view) {

        final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setExitDialog(new BaseDialogView.ExitDialog() {

            @Override
            public void exitDialog() {
                dlg.dismiss();
            }
        });
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高

        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dlg.show();
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        lp.width = d.getWidth();
        dlg.onWindowAttributesChanged(lp);
        return dlg;
    }

    /**
     * 自定义弹框
     * @param context
     * @return
     */
    public static Dialog showSelfDialog(Context context,BaseDialogView dialogView){
        final Dialog dialog =  new Dialog(context, R.style.updatedialog);
        dialogView.setExitDialog(new BaseDialogView.ExitDialog() {

            @Override
            public void exitDialog() {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();
        return dialog;
    }
}
