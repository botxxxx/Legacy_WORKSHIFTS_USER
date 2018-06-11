package iccl.workshifts.ubeta;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MonthDateView extends View {
	private static int NUM_COLUMNS = 7;
	private static int NUM_ROWS = 6;
	private Paint mPaint;
	private int mWriteColor = Color.parseColor("#FFFFFF");// write
	private int mGraylightColor = Color.parseColor("#CCCCCC");// gray light
	private int mGraydarkColor = Color.parseColor("#A5A5A5");// gray dark
	private int mDarkColor = Color.parseColor("#4C4C4C");// dark
	private int mRedColor = Color.parseColor("#FF0000");// red
	private int mPinkColor = Color.parseColor("#FF4C4C");// pink
	private int mBlueColor = Color.parseColor("#CCE4F2");// blue
	// select_background
	private static int mSelYear, mSelMonth, mSelDay;
	private int mCurrYear, mCurrMonth, mCurrDay;
	private int mColumnSize, mRowSize;
	private double mDaySize = MainActivity.metrics.widthPixels * 0.075;// �r��j�p
	private int mCircle = (MainActivity.metrics.widthPixels / 20) + 5;// �j��
	private int sCircle = mCircle - 10;// �p��
	// private int weekRow;// �O���ĴX��ĴX�g
	private String[][] daysString;
	private int mCircleRadius = 8;
	private TextView tv_date, tv_add;
	private DateClick dateClick;
	private ArrayList<GetPoint> RedPoint = new ArrayList<GetPoint>();
	private ArrayList<GetPoint> Holidays = new ArrayList<GetPoint>();
	private ArrayList<GetPoint> Dfaudays = new ArrayList<GetPoint>();
	private String[] dayOfWeek = { "", "��", "�@", "�G", "�T", "�|", "��", "��" };
	private Context con;
	private int downX = 0, downY = 0;

	public MonthDateView(Context context, AttributeSet attrs) { // ��l��
		super(context, attrs);
		mPaint = new Paint();
		Calendar calendar = Calendar.getInstance();
		mCurrYear = calendar.get(Calendar.YEAR);
		mCurrMonth = calendar.get(Calendar.MONTH);
		mCurrDay = calendar.get(Calendar.DATE);
		mSelYear = MainActivity.getYearDate();
		mSelMonth = MainActivity.getMonthDate();
		mSelDay = MainActivity.getDayDate();
		if (mSelYear != 0 && mSelMonth != -1 && mSelDay != 0) {
			// �]�w��CalendarActivity��ܪ����
			setSelectYearMonth(mSelYear, mSelMonth, mSelDay);
		} else {
			// �]�w������
			setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
		}
		setRedPointList(MainActivity.RedPoint); // TODO �аO��ƨ���RedPoint
		setHolidaysList(MainActivity.Holidays); // TODO �аO��ƨ���Holidays
		setTextView(MainActivity.tv_date, MainActivity.tv_add); // �]�mCalendarActivity��ܤ����
		MainActivity.ViewDate.add(this); // �s�W��View��MainActivity
		con = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		initSize();
		daysString = new String[6][7];// ���gx�C��
		mPaint.setTextSize((int) mDaySize);
		String dayText;
		int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth); // �q�L�~���M����o���몺��l
		int mWeekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth) - 1; // ��e����@�����g�X
		if (mMonthDays + mWeekNumber <= 35) {
			NUM_ROWS = 5; // ���ܦC�Ƶ���
			mRowSize = getHeight() / NUM_ROWS;
		} else {
			NUM_ROWS = 6; // ���ܦC�Ƶ���
			mRowSize = getHeight() / NUM_ROWS;
		}
		int mLestYear = mSelYear;
		int mLestMonth = (mSelMonth - 1);
		int mNextYear = mSelYear;
		int mNextMonth = (mSelMonth + 1);
		if (mLestMonth < 0) { // 0-1 = -1
			mLestYear--;
			mLestMonth = 11;
		}
		if (mNextMonth > 11) { // 11+1 = 12
			mNextYear++;
			mNextMonth = 0;
		}
		int mLestMonthDays = DateUtils.getMonthDays(mLestYear, mLestMonth);
		int Y = 0, M = 0, D = 0, Days = 0;
		boolean L = false, N = false;
		// �e��u
		drawLines(canvas);
		for (int position = 0; position < 42; position++) {
			if (!L) {
				// ������Ĥ@�Ѥ����g��
				if (mWeekNumber != 0) {
					Y = mLestYear;
					M = mLestMonth;
					// ���o�W�Ӥ���̫�@��
					Days = mLestMonthDays - (mWeekNumber - 1);
					if (position > (mWeekNumber - 1)) {
						// ������Ĥ@��
						Y = mSelYear;
						M = mSelMonth;
						Days = (mWeekNumber - 1) * (-1);
						L = true;
					}
				} else {
					Y = mSelYear;
					M = mSelMonth;
					Days = 1;
					L = true;
				}
			}
			if (L && !N && (position + Days) > mMonthDays) {
				// ���������
				// if (Days < 0) {
				Y = mNextYear;
				M = mNextMonth;
				Days = (position - 1) * (-1);
				// } else {
				// Y = mNextYear;
				// M = mNextMonth;
				// Days = ((position + Days) - 2) * (-1);
				// }
				N = true;
			}
			dayText = (position + Days) + "";
			D = position + Days;
			if ((position % 7) == 0 || (position % 7) == 6) {
				Dfaudays.add(new GetPoint(Y, M + 1, D));
			}
			int column = (position) % 7;
			int row = (position) / 7;
			daysString[row][column] = (Y + "/" + M + "/" + D);
			int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayText)) / 2);
			int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
			int startRecX = mColumnSize * column;
			int startRecY = mRowSize * row;
			int endRecX = startRecX + mColumnSize;
			int endRecY = startRecY + mRowSize;

			// ø�s�I����
			if (M == mSelMonth && D == mSelDay) {

				if (mCurrYear == mSelYear && mCurrMonth == mSelMonth && mCurrDay == mSelDay) {
					// Today(select)
					mPaint.setColor(mPinkColor); // �j��:��
					mPaint.setStyle(Style.FILL);
					canvas.drawCircle(((startRecX + endRecX) / 2), ((startRecY + endRecY) / 2), mCircle, mPaint);
					mPaint.setColor(mGraylightColor); // �p��:��
					mPaint.setStyle(Style.FILL);
					canvas.drawCircle(((startRecX + endRecX) / 2), ((startRecY + endRecY) / 2), sCircle, mPaint);
				} else {
					// Current Month Days(select)
					mPaint.setColor(mPinkColor); // �j��:��
					mPaint.setStyle(Style.FILL);
					canvas.drawCircle(((startRecX + endRecX) / 2), ((startRecY + endRecY) / 2), mCircle, mPaint);
					mPaint.setColor(mWriteColor); // �p��:��
					mPaint.setStyle(Style.FILL);
					canvas.drawCircle(((startRecX + endRecX) / 2), ((startRecY + endRecY) / 2), sCircle, mPaint);
				}
			}
			if (Y == mCurrYear && M == mCurrMonth && D == mCurrDay && D != mSelDay) {
				mPaint.setColor(mGraylightColor); // �j��:��
				mPaint.setStyle(Style.FILL);
				canvas.drawCircle(((startRecX + endRecX) / 2), ((startRecY + endRecY) / 2), mCircle, mPaint);
			}
			// ø�s����
			drawCircle(((startRecX + endRecX) / 2), ((startRecY + endRecY) / 2), Y, M, D, canvas);
			// ø�s�ƥ�лx
			drawPoint(row, column, Y, M, D, canvas);
			// ø�s�Ʀr
			if (M == mSelMonth) {
				// �]�w�r���C��:��
				mPaint.setColor(mDarkColor);
				canvas.drawText(dayText, startX, startY, mPaint);
			} else {
				// �]�w�r���C��:��
				mPaint.setColor(mGraydarkColor);
				canvas.drawText(dayText, startX, startY, mPaint);
			}
		}
		if (tv_date != null) {
			if (mCurrYear == mSelYear) {
				tv_date.setTextSize(40);
				tv_date.setText((mSelMonth + 1) + "��" + mSelDay);
			} else {
				tv_date.setTextSize(25);
				tv_date.setText((mSelYear - 1911) + "�~" + (mSelMonth + 1) + "��" + mSelDay);
			}
			// + getmSelWeek(mSelYear, mSelMonth, mSelDay));
			if (MainActivity.getAdd()) {
				tv_add.setVisibility(View.VISIBLE);
			} else {
				tv_add.setVisibility(View.GONE);
			}
		}
	}

	@SuppressLint("ResourceAsColor")
	private void drawLines(Canvas canvas) { // �u
		int rightX = getWidth();
		int BottomY = getHeight();
		int columnCount = 7;
		Path path;
		float startX = 0;
		float endX = rightX;
		mPaint.setStyle(Style.STROKE);
		for (int row = NUM_ROWS; row <= NUM_ROWS; row++) {
			float startY = (row * mRowSize) - (1);
			path = new Path();
			path.moveTo(startX, startY);
			path.lineTo(endX, startY);
			canvas.drawPath(path, mPaint);
		}

		float startY = BottomY - 1;
		float endY = BottomY;
		for (int column = 0; column < columnCount; column++) {
			startX = column * mColumnSize;
			path = new Path();
			path.moveTo(startX, startY);
			path.lineTo(startX, endY);
			canvas.drawPath(path, mPaint);
		}

	}

	private void drawPoint(int row, int column, int year, int month, int day, Canvas canvas) { // �e�I
		if (RedPoint != null && RedPoint.size() > 0) {
			GetPoint dayAndPrice;
			for (int i = 0; i < RedPoint.size(); i++) {
				dayAndPrice = RedPoint.get(i);
				if (dayAndPrice.year == year && dayAndPrice.month == month + 1 && dayAndPrice.day == day) {
					mPaint.setColor(mRedColor);
					mPaint.setStyle(Style.FILL);
					float circleX = (float) (mColumnSize * column + mColumnSize * 0.8);
					float circley = (float) (mRowSize * row + mRowSize * 0.2);
					canvas.drawCircle(circleX, circley, mCircleRadius, mPaint);
				}
			}
		}
	}

	private void drawCircle(float circleX, float circley, int year, int month, int day, Canvas canvas) { // �e��
		boolean Df = false, Ho = false;
		if (Dfaudays != null && Dfaudays.size() > 0) {
			GetPoint DfaudaysPrice;
			for (int i = 0; i < Dfaudays.size(); i++) {
				DfaudaysPrice = Dfaudays.get(i);
				if (DfaudaysPrice.year == year && DfaudaysPrice.month == month + 1 && DfaudaysPrice.day == day) {
					Df = true;
				}
			}
		}
		if (Holidays != null && Holidays.size() > 0) {
			GetPoint HolidaysPrice;
			for (int i = 0; i < Holidays.size(); i++) {
				HolidaysPrice = Holidays.get(i);
				if (HolidaysPrice.year == year && HolidaysPrice.month == month + 1 && HolidaysPrice.day == day) {
					Ho = true;
				}
			}
		}
		if (Df != Ho) {
			mPaint.setColor(mBlueColor);
			mPaint.setStyle(Style.FILL);
			canvas.drawCircle(circleX, circley, sCircle, mPaint);
		}
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventCode = event.getAction();
		switch (eventCode) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			int upX = (int) event.getX();
			int upY = (int) event.getY();
			if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {// �I���ƥ�
				performClick();
				doClickAction((upX + downX) / 2, (upY + downY) / 2);
			}
			break;
		}
		return true;
	}

	// ��l�ƦC�e,�氪
	private void initSize() {
		mColumnSize = getWidth() / NUM_COLUMNS;
		mRowSize = getHeight() / NUM_ROWS;
	}

	// �]�m�~��year,month,day
	private void setSelectYearMonth(int Y, int M, int D) {
		mSelYear = Y;
		mSelMonth = M;
		mSelDay = D;
		setTextView(MainActivity.tv_date, MainActivity.tv_add);
	}

	private void setSelectYearMonth2(String args) {
		// �I���]�m�~��year,month,day
		// boolean RE = false;
		int tmp = 0, Y, M, D;
		String arg = args + "/";
		Y = Integer.parseInt(arg.substring(tmp).substring(0, arg.substring(tmp).indexOf('/')));
		tmp = tmp + (Y + "").length() + 1;
		M = Integer.parseInt(arg.substring(tmp).substring(0, arg.substring(tmp).indexOf('/')));
		tmp = tmp + (M + "").length() + 1;
		D = Integer.parseInt(arg.substring(tmp).substring(0, arg.substring(tmp).indexOf('/')));

		if (mSelYear == Y && mSelMonth == M) {
			// RE = true; // �O�_����

			mSelYear = Y;
			mSelMonth = M;
			mSelDay = D;
			MainActivity.setDate(Y, M, D);
			MainActivity.updateDay(con);
		}

	}

	// TODO �����I���ƥ�param x,param y
	private void doClickAction(int x, int y) {
		int column = x / mColumnSize;
		int row = y / mRowSize;
		setSelectYearMonth2(daysString[row][column]);
		invalidate();
		// ����Acticity�o�e�L�Ӫ��I���B�z�ƥ�
		if (dateClick != null) {
			dateClick.onClickOnDate();
		}
	}

	// // �k�I��,���V��½��
	// public void onLeftClick() {
	// int year = mSelYear;
	// int month = mSelMonth;
	// int day = mSelDay;
	// if (month == 0) { // ����1��,�ܦ�12��
	// year = mSelYear - 1;
	// month = 11;
	// } else {
	// month = month - 1;
	// }
	// setSelectYearMonth(year, month, day);
	// invalidate();
	// }
	//
	// // �k�I��,���V�e½��
	// public void onRightClick() {
	// int year = mSelYear;
	// int month = mSelMonth;
	// int day = mSelDay;
	// if (month == 11) {
	// year = mSelYear + 1;
	// month = 0;
	// } else {
	// month = month + 1;
	// }
	// setSelectYearMonth(year, month, day);
	// invalidate();
	// }

	public int getmSelYear() {
		return mSelYear;
	}

	public int getmSelMonth() {
		return mSelMonth;
	}

	public int getmSelDay() {
		return mSelDay;
	}

	public String getmSelWeek(int Y, int M, int D) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Y, M, D);
		return dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK)];
	}

	public void setmDayColor(int mDayColor) {
		this.mDarkColor = mDayColor;
	}

	public void setmSelectBGColor(int mSelectBGColor) {
		this.mPinkColor = mSelectBGColor;
	}

	public void setmDaySize(int mDaySize) {
		this.mDaySize = mDaySize;
	}

	public void setTextView(TextView date, TextView add) {
		this.tv_date = date;
		this.tv_add = add;
		invalidate(); // ��sview
	}

	// TODO �����~��Date
	public void setRedPointList(ArrayList<GetPoint> RedPoint) {
		this.RedPoint = RedPoint;
	}

	public void setHolidaysList(ArrayList<GetPoint> Holidays) {
		this.Holidays = Holidays;
	}

	public void setmCircleRadius(int mCircleRadius) {
		this.mCircleRadius = mCircleRadius;
	}

	public void setmCircleColor(int mCircleColor) {
		this.mRedColor = mCircleColor;
	}

	public interface DateClick {
		public void onClickOnDate();
	}

	public void setDateClick(DateClick dateClick) {
		this.dateClick = dateClick;
	}

	public void setTodayToView() {
		setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
		invalidate();
	}

	public void setSeldayToView(int Y, int M, int D) {
		setSelectYearMonth(Y, M, D);
		invalidate();
	}
}
