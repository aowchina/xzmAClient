
package com.minfo.carrepairseller.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.minfo.carrepairseller.R;


public class DownLoadingDialog {



    private  Context mContext;

    private View mDialogView;
    private TextView mTxtMsg;

    private Dialog mDialog;
    private Wr_roundProgressBar wrpb;



    public DownLoadingDialog(Context context){

        this.mContext = context;

        mDialogView =LayoutInflater.from(context)
                .inflate(R.layout.dialog_downloading_bw,null);


       wrpb = (Wr_roundProgressBar) mDialogView.findViewById(R.id.pb_downloading);

        initDialog();

    }


    private void  initDialog(){

        mDialog= new Dialog(mContext,R.style.dialog);
        mDialog.setContentView(mDialogView);
        mDialog.setCanceledOnTouchOutside(true);

    }

    public void setProgress(int progress){
        wrpb.setProgress(progress);
    }

    public  void show(){

        if(mDialog!=null)
            mDialog.show();
    }


    public  void dismiss(){

        if(mDialog!=null)
            mDialog.dismiss();
    }


    public  boolean isShowing(){

        return  mDialog.isShowing();
    }

}
