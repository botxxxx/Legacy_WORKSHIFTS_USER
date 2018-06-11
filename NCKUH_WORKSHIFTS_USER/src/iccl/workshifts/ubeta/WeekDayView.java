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

	// �W��u�C��色
	private int mLineColor = Color.parseColor("#CCE4F2");// �H��
	// �g�@��g�����C��
	private int mWeedayColor = Color.parseColor("#1F58F3");// ��
	// ���骺�C��
	private int mWeekendColor = Color.parseColor("#fa4451");// ��

	// �u���e��
	private int mStrokeWidth = 4;
	// �r��j�p
	private int mWeekSize = 30;
	private Paint paint;
	private DisplayMetrics mDisplayMetrics;
	private String[] weekString = new String[] { "��", "�@", "�G", "�T", "�|", "��", "��" };

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
		// �i��e�W�U�u
		paint.setStyle(Style.STROKE);
		paint.setColor(mLineColor);
		paint.setStrokeWidth(mStrokeWidth);
		canvas.drawLine(0, 0, width, 0, paint);

		// �e�U��u
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
			if (text.indexOf("��") > -1 || text.indexOf("��") > -1) {
				paint.setColor(mWeekendColor);
			} else {
				paint.setColor(mWeedayColor);
			}
			canvas.drawText(text, startX, startY, paint);
		}
	}

	/**
	 * �]�m���u���C��
	 * 
	 * @param mTopLineColor
	 */
	public void setmTopLineColor(int mTopLineColor) {
		this.mLineColor = mTopLineColor;
	}

	/**
	 * �]�m���u���C�≲
	 * 
	 * @param mBottomLineColor
	 */
	public void setmBottomLineColor(int mBottomLineColor) {
		this.mLineColor = mBottomLineColor;
	}

	/**
	 * �]�m�g�@�ܶg�����C�≲
	 * 
	 * @return
	 */
	public void setmWeedayColor(int mWeedayColor) {
		this.mWeedayColor = mWeedayColor;
	}

	/**
	 * �]�m���骺�C��
	 * 
	 * @param mWeekendColor
	 */
	public void setmWeekendColor(int mWeekendColor) {
		this.mWeekendColor = mWeekendColor;
	}

	/**
	 * �]�m��u�e��
	 * 
	 * @param mStrokeWidth
	 */
	public void setmStrokeWidth(int mStrokeWidth) {
		this.mStrokeWidth = mStrokeWidth;
	}

	/**
	 * �]�m�r��j�p
	 * 
	 * @param mWeekSize
	 */
	public void setmWeekSize(int mWeekSize) {
		this.mWeekSize = mWeekSize;
	}

	/**
	 * �]�m�P�����Φ�
	 */
	public void setWeekString(String[] weekString) {
		this.weekString = weekString;
	}
}
