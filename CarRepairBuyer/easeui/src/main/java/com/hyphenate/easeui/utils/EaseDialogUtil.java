package com.hyphenate.easeui.utils;

import android.content.Intent;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;

/**
 * Created by MinFo021 on 17/8/3.
 */

public class EaseDialogUtil {
    /**
     * 简单信息提示框
     * @param title
     * @param msg
     * @param btnPositiveName
     * @param btnNegativeName
     * @return
     * @author Jacky
     */
    public static void showMsgDialog(
            Context mContext,
            String title,
            String msg,
            String btnPositiveName,
            String btnNegativeName,
            final EnsureCancleInterface listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(btnPositiveName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    listener.ensure();
                }
//                mDialog.dismiss();
            }
        });
        // 取消
        builder.setNegativeButton(btnNegativeName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null) {
                    listener.cancle();
                }
//                mDialog.dismiss();
            }
        });
        Dialog mDialog = builder.create();
        mDialog.show();

    }

}
