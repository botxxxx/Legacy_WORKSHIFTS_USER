package iccl.workshifts.ubeta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class WeekDayView extends View {

	// ¤W¾î½uÃC¦âœè‰²
	private int mLineColor = Color.parseColor("#CCE4F2");// ²HÂÅ
	// ¶g¤@¨ì¶g¤­ªºÃC¦â
	private int mWeedayColor = Color.parseColor("#1F58F3");// ÂÅ
	// ¤»¤éªºÃC¦â
	private int mWeekendColor = Color.parseColor("#fa4451");// ¬õ

	// ½uªº¼e«×
	private int mStrokeWidth = 4;
	// ¦rÅé¤j¤p
	private int mWeekSize = 30;
	private Paint paint;
	private DisplayMetrics mDisplayMetrics;
	private String[] weekString = new String[] { "¤é", "¤@", "¤G", "¤T", "¥|", "¤­", "¤»" };

	public WeekDayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDisplayMetrics = getResources().getDisplayMetrics();
		paint = new Paint();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (heightMode == MeasureSpec.AT_MOST) {
			heightSize = mDisplayMetrics.densityDpi * 30;
		}
		if (widthMode == MeasureSpec.AT_MOST) {
			widthSize = mDisplayMetrics.densityDpi * 300;
		}
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		// ¶i¦æµe¤W¤U½u
		paint.setStyle(Style.STROKE);
		paint.setColor(mLineColor);
		paint.setStrokeWidth(mStrokeWidth);
		canvas.drawLine(0, 0, width, 0, paint);

		// µe¤U¾î½u
		paint.setColor(mLineColor);
		canvas.drawLine(0, height, width, height, paint);
		paint.setStyle(Style.FILL);
		paint.setTextSize(mWeekSize * mDisplayMetrics.scaledDensity);
		int columnWidth = width / 7;
		for (int i = 0; i < weekString.length; i++) {
			String text = weekString[i];
			int fontWidth = (int) paint.measureText(text);
			int startX = columnWidth * i + (columnWidth - fontWidth) / 2;
			int startY = (int) (height / 2 - (paint.ascent() + paint.descent()) / 2);
			if (text.indexOf("¤é") > -1 || text.indexOf("¤»") > -1) {
				paint.setColor(mWeekendColor);
			} else {
				paint.setColor(mWeedayColor);
			}
			canvas.drawText(text, startX, startY, paint);
		}
	}

	/**
	 * ³]¸m³»½uªºÃC¦â
	 * 
	 * @param mTopLineColor
	 */
	public void setmTopLineColor(int mTopLineColor) {
		this.mLineColor = mTopLineColor;
	}

	/**
	 * ³]¸m©³½uªºÃC¦â‰²
	 * 
	 * @param mBottomLineColor
	 */
	public void setmBottomLineColor(int mBottomLineColor) {
		this.mLineColor = mBottomLineColor;
	}

	/**
	 * ³]¸m¶g¤@¦Ü¶g¤­ªºÃC¦â‰²
	 * 
	 * @return
	 */
	public void setmWeedayColor(int mWeedayColor) {
		this.mWeedayColor = mWeedayColor;
	}

	/**
	 * ³]¸m¤»¤éªºÃC¦â
	 * 
	 * @param mWeekendColor
	 */
	public void setmWeekendColor(int mWeekendColor) {
		this.mWeekendColor = mWeekendColor;
	}

	/**
	 * ³]¸mÃä½u¼e«×
	 * 
	 * @param mStrokeWidth
	 */
	public void setmStrokeWidth(int mStrokeWidth) {
		this.mStrokeWidth = mStrokeWidth;
	}

	/**
	 * ³]¸m¦rÅé¤j¤p
	 * 
	 * @param mWeekSize
	 */
	public void setmWeekSize(int mWeekSize) {
		this.mWeekSize = mWeekSize;
	}

	/**
	 * ³]¸m¬P´Áªº§Î¦¡
	 */
	public void setWeekString(String[] weekString) {
		this.weekString = weekString;
	}
}
