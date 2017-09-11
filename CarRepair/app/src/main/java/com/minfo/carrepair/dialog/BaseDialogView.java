package com.minfo.carrepair.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class BaseDialogView extends LinearLayout {
    protected Context context;
    protected ExitDialog mExitDialog;

    public interface ExitDialog {
        public void exitDialog();
    }

    public void setExitDialog(ExitDialog exitDialog) {
        mExitDialog = exitDialog;
    }

    public BaseDialogView(Context context) {
        super(context);
        this.context = context;
    }

    public BaseDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public boolean ensureInfo() {
        return false;
    }
}
