package iccl.workshifts.ubeta;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;

public class DateUtils {
	/**
	 * 通過年份和月份得到當月的日子
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthDays(int year, int month) {
		month++;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
				return 29;
			} else {
				return 28;
			}
		default:
			return -1;
		}
	}

	/**
	 * 返回當前月份一號位於週幾
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return 日1 一2 二3 三4 四5 五6 六7
	 */

	public static int getDayWeek(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int getFirstDayWeek(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int daysInGregorianMonth(int y, int m) { // m: 0 ~ 11
		int[] GregorianMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int d = GregorianMonth[m];
		if (m == 1 && isGregorianLeapYear(y)) {
			d++;
		}
		return d;
	}

	public static boolean isGregorianLeapYear(int year) {
		boolean isLeap = false;
		if (year % 4 == 0)
			isLeap = true;
		if (year % 100 == 0)
			isLeap = false;
		if (year % 400 == 0)
			isLeap = true;
		return isLeap;
	}

	public static ArrayList<CalendarBundle> getAvailableCalendars(Context c, ArrayList<CalendarBundle> result) {
		final String[] PROJECTION = new String[] { Calendars._ID, Calendars.CALENDAR_DISPLAY_NAME };

		Cursor cur = null;
		ContentResolver cr = c.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;

		cur = cr.query(uri, PROJECTION, null, null, null);

		while (cur.moveToNext()) {
			result.add(new CalendarBundle(cur.getLong(0), cur.getString(1)));
		}
		return result;
	}
}
