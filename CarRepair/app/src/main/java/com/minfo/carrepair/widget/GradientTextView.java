package com.minfo.carrepair.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.minfo.carrepair.R;


/**
 * TODO: document your custom view class.
 */
public class GradientTextView extends TextView {

    private int mStartColor ;
    private int mEndColor ;


    public GradientTextView(Context context) {
        super(context);
        init(context,null, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs, defStyle);
    }

    private void init(Context context,AttributeSet attrs, int defStyle) {
        mStartColor = context.getResources().getColor(R.color.skin_menu_text_ck);
        mEndColor = context.getResources().getColor(R.color.skin_menu_text_ck);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GradientTextView, defStyle, 0);

        mStartColor = a.getColor(
                R.styleable.GradientTextView_startcolor,
                mStartColor);
        mEndColor = a.getColor(
                R.styleable.GradientTextView_endcolor,
                mEndColor);

        a.recycle();


        // Update TextPaint and text measurements from attributes
        invalidate();
    }
    

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setShader(new LinearGradient(
                0, 0, 0, getHeight(),
                mStartColor, mEndColor,
                Shader.TileMode.CLAMP));
        super.onDraw(canvas);

        
    }
    

    /**
     * 设置渐变色
     * @param startColor
     */
    public void setmStartColor(int startColor) {
        mStartColor = startColor;
    }
    public int getmStartColor() {
        return mStartColor;
    }
    /**
     * 设置渐变色
mE     */
    public void setmEndColor(int endColor) {
        mEndColor = endColor;
        invalidate();
    }
    public int getmEndColor() {
        return mEndColor;
    }
    @Override
    protected void onLayout(boolean changed,
                            int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
//            getPaint().setShader(new LinearGradient(
//                    0, 0, 0, getHeight(),
//                    mStartColor, mEndColor,
//                    Shader.TileMode.CLAMP));
        }
    }
}
