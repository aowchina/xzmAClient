package com.minfo.carrepairseller.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.minfo.carrepairseller.R;

/*
 * 圆环进度条
 * @author wangrui
 * @date 2015-03-20
 */
public class Wr_roundProgressBar extends View{
	private Paint paint;               //画笔对象
	
	private int roundColor;            //圆环颜色
	private int roundProgressColor;    //圆环进度颜色
	private int textColor;             //进度文字（n%）的颜色
	private float textSize;            //进度文字的大小
	private float roundWidth;          //圆环宽度
	
	private int progress;              //当前进度
	private int max;                   //最大进度
	
	private boolean textIsDisplayable; //是否显示进度
	
	private int style;                 //进度的风格，实心或者空心
    public static final int STROKE = 0;  
    public static final int FILL = 1; 
	
	public Wr_roundProgressBar(Context context){
		this(context, null);
	}
	
	public Wr_roundProgressBar(Context context, AttributeSet attrs){
		this(context, attrs, 0); 
	}
	
	public Wr_roundProgressBar(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint();
		
		roundWidth = context.getResources().getDimension(R.dimen.ring_width);
		roundColor = context.getResources().getColor(R.color.white);
		roundProgressColor = context.getResources().getColor(R.color.red);
		
		textSize = context.getResources().getDimension(R.dimen.ring_width);
		textColor = context.getResources().getColor(R.color.white);
		
		style = 0;
		textIsDisplayable = true;
		
		max = 100;
	}
	
	@SuppressLint("DrawAllocation")
	@Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);
        
        int centre = getWidth()/2;                        //获取圆心的x坐标  
        int radius = (int) (centre - roundWidth/2);       //圆环的半径
        paint.setColor(roundColor);                       //设置圆环的颜色  
        paint.setStyle(Paint.Style.STROKE);               //设置空心  
        paint.setStrokeWidth(roundWidth);                 //设置圆环的宽度  
        paint.setAntiAlias(true);                         //消除锯齿   
        canvas.drawCircle(centre, centre, radius, paint); //画出圆环 
        
        paint.setStrokeWidth(0);   
        paint.setColor(textColor);  
        paint.setTextSize(textSize);  
        paint.setTypeface(Typeface.DEFAULT_BOLD);         //设置字体
        
        //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        int percent = (int)(((float)progress / (float)max) * 100);
        
        //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        float textWidth = paint.measureText(percent + "%");  
          
        if(textIsDisplayable && percent != 0 && style == STROKE){
        	//画出进度百分比
            canvas.drawText(percent + "%", centre-textWidth/2, centre+textSize/2, paint);  
        }
        
        //设置进度是实心还是空心  
        paint.setStrokeWidth(roundWidth);       //设置圆环的宽度  
        paint.setColor(roundProgressColor);     //设置进度的颜色
        
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(centre-radius, centre-radius, centre+radius, centre+radius);    
          
        switch (style) {  
	        case STROKE:
	            paint.setStyle(Paint.Style.STROKE);  
	            canvas.drawArc(oval, 0, 360 * progress / max, false, paint);  //根据进度画圆弧  
	            break;
	        case FILL:
	            paint.setStyle(Paint.Style.FILL_AND_STROKE);  
	            if(progress !=0)  
	                canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧  
	            break;
        }
    }
	
	public synchronized int getMax() {  
        return max;  
    }  
  
    //设置进度的最大值 
    public synchronized void setMax(int max) {  
        if(max < 0){  
            throw new IllegalArgumentException("max not less than 0");  
        }  
        this.max = max;  
    }  
  
    //获取进度.需要同步 
    public synchronized int getProgress() {  
        return progress;  
    }  
  
    /** 
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 
     * 刷新界面调用postInvalidate()能在非UI线程刷新 
     * @param progress 
     */  
    public synchronized void setProgress(int progress) {  
        if(progress < 0){  
            throw new IllegalArgumentException("progress not less than 0");  
        }  
        if(progress > max){  
            progress = max;  
        }  
        if(progress <= max){  
            this.progress = progress;  
            postInvalidate();  
        }  
          
    }  
    
    public int getCricleColor() {  
        return roundColor;  
    }  
  
    public void setCricleColor(int cricleColor) {  
        this.roundColor = cricleColor;  
    }  
  
    public int getCricleProgressColor() {  
        return roundProgressColor;  
    }  
  
    public void setCricleProgressColor(int cricleProgressColor) {  
        this.roundProgressColor = cricleProgressColor;  
    }  
  
    public int getTextColor() {  
        return textColor;  
    }  
  
    public void setTextColor(int textColor) {  
        this.textColor = textColor;  
    }  
  
    public float getTextSize() {  
        return textSize;  
    }  
  
    public void setTextSize(float textSize) {  
        this.textSize = textSize;  
    }  
  
    public float getRoundWidth() {  
        return roundWidth;  
    }  
  
    public void setRoundWidth(float roundWidth) {  
        this.roundWidth = roundWidth;  
    }
}
