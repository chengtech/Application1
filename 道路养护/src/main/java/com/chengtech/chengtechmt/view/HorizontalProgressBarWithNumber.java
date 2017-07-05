package com.chengtech.chengtechmt.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.chengtech.chengtechmt.R;

public class HorizontalProgressBarWithNumber extends ProgressBar {

	private static final int DEFAULT_TEXT_SIZE = 10;
	private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;
	private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xFFd3d6da;
	private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 2;
	private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2;
	private static final int DEFAULT_SIZE_TEXT_OFFSET = 10;

	/**
	 * 画笔
	 */
	protected Paint mPaint = new Paint();

	/**
	 * 字体颜色
	 */
	protected int mTextColor = DEFAULT_TEXT_COLOR;

	/**
	 * 字体大小
	 */
	protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);

	/**
	 * 文字移动距离
	 */
	protected int mTextOffset = dp2px(DEFAULT_SIZE_TEXT_OFFSET);

	/**
	 * 完成时进度条的高度
	 */
	protected int mReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR);

	/**
	 * 完成时进度条的颜色
	 */
	protected int mReachedBarColor = DEFAULT_TEXT_COLOR;

	/**
	 * 未完成时进度条的颜色
	 */
	protected int mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR;

	/**
	 * 未完成时进度条的高度
	 */
	protected int mUnReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR);

	protected int mRealWidth;

	protected boolean mIfDrawText = true;

	protected static final int VISIBLE = 0;

	public HorizontalProgressBarWithNumber(Context context) {
		this(context, null);
	}

	public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public HorizontalProgressBarWithNumber(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		setHorizontalScrollBarEnabled(true);

		obtainStyledAttributes(attrs, context);

		mPaint.setTextSize(mTextSize);
		mPaint.setColor(mTextColor);

	}

	private void obtainStyledAttributes(AttributeSet attrs, Context context) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.HorizontalProgressBarWithNumber);
		mTextColor = ta
				.getColor(
						R.styleable.HorizontalProgressBarWithNumber_progress_text_color,
						DEFAULT_TEXT_COLOR);
		mTextSize = (int) ta.getDimension(
				R.styleable.HorizontalProgressBarWithNumber_progress_text_size,
				mTextSize);

		mReachedBarColor = ta
				.getColor(
						R.styleable.HorizontalProgressBarWithNumber_progress_reached_color,
						mTextColor);
		mUnReachedBarColor = ta
				.getColor(
						R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color,
						DEFAULT_COLOR_UNREACHED_COLOR);
		mReachedProgressBarHeight = (int) ta
				.getDimension(
						R.styleable.HorizontalProgressBarWithNumber_progress_reached_bar_height,
						mReachedProgressBarHeight);
		mUnReachedProgressBarHeight = (int) ta
				.getDimension(
						R.styleable.HorizontalProgressBarWithNumber_progress_unreached_bar_height,
						mUnReachedProgressBarHeight);
		mTextOffset = (int) ta
				.getDimension(
						R.styleable.HorizontalProgressBarWithNumber_progress_text_offset,
						mTextOffset);

		int textVisible = ta  
                .getInt(R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility,  
                        VISIBLE);  
        if (textVisible != VISIBLE)  
        {  
            mIfDrawText = false;  
        }  
		ta.recycle();
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		 int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
		  
		    if (heightMode != MeasureSpec.EXACTLY)  
		    {  
		  
		        float textHeight = (mPaint.descent() + mPaint.ascent());  
		        int exceptHeight = (int) (getPaddingTop() + getPaddingBottom() + Math  
		                .max(Math.max(mReachedProgressBarHeight,  
		                        mUnReachedProgressBarHeight), Math.abs(textHeight)));  
		  
		        heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight,  
		                MeasureSpec.EXACTLY);  
		    }  
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {

        canvas.save();  
        //画笔平移到指定paddingLeft， getHeight() / 2位置，注意以后坐标都为以此为0，0  
        canvas.translate(getPaddingLeft(), getHeight() / 2);  
  
        boolean noNeedBg = false;  
        //当前进度和总值的比例  
        float radio = getProgress() * 1.0f / getMax();  
        //已到达的宽度  
        float progressPosX = (int) (mRealWidth * radio);  
        //绘制的文本  
        String text = getProgress() + "%";  
  
        //拿到字体的宽度和高度  
        float textWidth = mPaint.measureText(text);  
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;  
  
        //如果到达最后，则未到达的进度条不需要绘制  
        if (progressPosX + textWidth > mRealWidth)  
        {  
            progressPosX = mRealWidth - textWidth;  
            noNeedBg = true;  
        }  
  
        // 绘制已到达的进度  
        float endX = progressPosX - mTextOffset / 2;  
        if (endX > 0)  
        {  
            mPaint.setColor(mReachedBarColor);  
            mPaint.setStrokeWidth(mReachedProgressBarHeight);  
            canvas.drawLine(0, 0, endX, 0, mPaint);  
        }  
      
        // 绘制文本  
        if (mIfDrawText)  
        {  
            mPaint.setColor(mTextColor);  
            canvas.drawText(text, progressPosX, -textHeight, mPaint);  
        }  
  
        // 绘制未到达的进度条  
        if (!noNeedBg)  
        {  
            float start = progressPosX + mTextOffset / 2 + textWidth;  
            mPaint.setColor(mUnReachedBarColor);  
            mPaint.setStrokeWidth(mUnReachedProgressBarHeight);  
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);  
        }  
  
        canvas.restore();  
	}
	
	 @Override  
	    protected void onSizeChanged(int w, int h, int oldw, int oldh)  
	    {  
	        super.onSizeChanged(w, h, oldw, oldh);  
	        mRealWidth = w - getPaddingRight() - getPaddingLeft();  
	  
	    }  
	
	
	
	
	
	protected int sp2px(int val) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, val, getResources().getDisplayMetrics());
	}

	protected int dp2px(int val) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, val, getResources().getDisplayMetrics());
	}


}
