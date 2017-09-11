/*
*LoadingDialog.java
*Created on 2014-9-24 上午10:18 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.minfo.carrepairseller.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.minfo.carrepairseller.R;


public class LoadingDialog {


    private final View ivLoad;
    private Context mContext;

    private View mDialogView;
    private TextView mTxtMsg;

    private Dialog mDialog;



    public LoadingDialog(Context context){

        this.mContext = context;

        mDialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_loading,null);

        mTxtMsg = (TextView) mDialogView.findViewById(R.id.txtMsg);

        ivLoad = mDialogView.findViewById(R.id.iv_load);

        initDialog();
        Animation rotate= AnimationUtils.loadAnimation(context,R.anim.loading);
        LinearInterpolator lin=new LinearInterpolator();
        rotate.setInterpolator(lin);
        ivLoad.startAnimation(rotate);

    }


    private void initDialog() {

        mDialog = new Dialog(mContext, R.style.dialog);
        mDialog.setContentView(mDialogView);
        mDialog.setCanceledOnTouchOutside(false);

    }


    public  void setMessage(CharSequence msg){
        mTxtMsg.setText(msg);
    }

    public  void setMessage(int msg){
        mTxtMsg.setText(msg);
    }




    public  void show(){

        if(mDialog!=null) {
            Animation rotate= AnimationUtils.loadAnimation(mContext,R.anim.loading);
            LinearInterpolator lin=new LinearInterpolator();
            rotate.setInterpolator(lin);
            ivLoad.clearAnimation();
            ivLoad.startAnimation(rotate);
            mDialog.show();
        }
    }


    public  void dismiss(){

        if(mDialog!=null)
            mDialog.dismiss();
    }


    public  boolean isShowing(){

        return  mDialog.isShowing();
    }







}
