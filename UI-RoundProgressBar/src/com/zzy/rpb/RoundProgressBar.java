package com.zzy.rpb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class RoundProgressBar extends View {
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 圆环的颜色
	 */
	private int roundColor;

	/**
	 * 圆环进度的颜色
	 */
	private int roundProgressColor;

	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int textColor;

	/**
	 * 中间进度百分比的字符串的字体
	 */
	private float textSize;

	/**
	 * 圆环的宽度
	 */
	private float roundWidth;

	/**
	 * 最大进度
	 */
	private int max;

	/**
	 * 当前进度
	 */
	private int progress;
	/**
	 * 是否显示中间的进度
	 */
	private boolean textIsDisplayable;

	/**
	 * 进度的风格，实心或者空心
	 */
	private int style;

	public static final int STROKE = 0;
	public static final int FILL = 1;

	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public RoundProgressBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray mTypedArray = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.RoundProgressBar, defStyle, 0);
		// 获取自定义属性和默认值
		roundColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_roundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		textColor = mTypedArray.getColor(
				R.styleable.RoundProgressBar_textColor, Color.GREEN);
		textSize = mTypedArray.getDimension(
				R.styleable.RoundProgressBar_textSize, 15);
		roundWidth = mTypedArray.getDimension(
				R.styleable.RoundProgressBar_roundWidth, 5);
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(
				R.styleable.RoundProgressBar_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

		mTypedArray.recycle();
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int centre = getWidth() / 2; // 获取圆心的x坐标
		int radius = (int) (centre - roundWidth);// 半径
		paint.setColor(roundColor);
		paint.setStrokeWidth(roundWidth);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true); // 消除锯齿
		canvas.drawCircle(centre, centre, radius, paint);

		// 根据进度画出弧长和显示进度的字体
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体
		// 中间的进度百分比，先转换成float在进行除法运算
		int percent = (int) (((float) progress / (float) max) * 100);
		StringBuffer buffer = new StringBuffer();
		buffer.append(percent + "%");
		// 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

		Rect mTextBound = new Rect();
		paint.getTextBounds(buffer.toString(), 0, buffer.toString().length(),
				mTextBound);
		if (mTextBound.width() > 2 * radius) {
			paint.setTextSize(2 * textSize / 3);
		}
		if (textIsDisplayable && percent != 0 && style == STROKE) {
			canvas.drawText(buffer.toString(), centre - mTextBound.width() / 2,
					centre + mTextBound.height() / 2, paint);
		}

		// 开始画圆弧
		paint.setStrokeWidth(roundWidth);
		paint.setColor(roundProgressColor);
		// 用于定义的圆弧的形状和大小的界限
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius);
		switch (style) {
			case STROKE : {
				paint.setStyle(Paint.Style.STROKE);
				canvas.drawArc(oval, 0, 360 * progress / max, false, paint); // 根据进度画圆弧
				break;
			}
			case FILL : {
				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				if (progress != 0)
					canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // 根据进度画圆弧
				break;
			}
		}
	}

	public synchronized int getMax() {
		return max;
	}

	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("the max progress less than 0");
		}

		this.max = max;
	}

	public synchronized int getProgress() {
		return progress;
	}
	/**
	 * 在非UI线程去刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			throw new IllegalArgumentException("progress less than 0");
		}
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			postInvalidate();
		}
	}

}
