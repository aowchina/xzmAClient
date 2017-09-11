package com.minfo.carrepair.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.widget.TextView;


public class CircleViewRed extends TextView {

	private Paint mBgPaint = new Paint();

	PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG); 

	public CircleViewRed(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CircleViewRed(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		//mBgPaint.setColor(Color.RED);
		mBgPaint.setARGB(255,216,94,135);
		mBgPaint.setAntiAlias(true);
	}

	public CircleViewRed(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//mBgPaint.setColor(Color.RED);
		mBgPaint.setARGB(255,216,94,135);
		mBgPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measuredWidth = getMeasuredWidth();
		int measuredHeight = getMeasuredHeight();
		int max = Math.max(measuredWidth, measuredHeight);
		setMeasuredDimension(max, max);
	}

	@Override
	public void setBackgroundColor(int color) {
		// TODO Auto-generated method stub
		mBgPaint.setColor(color);
	}

	/**
	 * 设置通知个数显示
	 * @param text
	 */
	public void setNotifiText(int text){
		//		if(text>99){
		//			String string = 99+"+";
		//			setText(string);
		//			return;
		//		}
		setText(text+"");
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.setDrawFilter(pfd);
		canvas.drawCircle(getWidth()/2, getHeight()/2, Math.max(getWidth(), getHeight())/2, mBgPaint);
		super.draw(canvas);
	}
}
