package iccl.workshifts.ubeta;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ViewActivityAutoFinal extends Activity implements OnClickListener, OnLongClickListener {
	private int fdy = 0, fwb = 0, ws = MainActivity.Worklist.size(), frow = 0, fs = 0;
	private String ddt = "";
	private TextView title;
	private ListView list;
	private int[][] TagDays = new int[6][7];
	private String[][][] daysString = new String[6][7][ws];
	private String[] wm = { "白班", "白觀", "小夜", "夜觀", "大夜", "其他" };
	private String[] was = new String[ws];
	private ArrayList<String> wts = new ArrayList<String>();
	private ArrayList<String> fls = new ArrayList<String>();
	private Handler flashing = new Handler();
	public static ArrayList<View> AutoFinalView = new ArrayList<View>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_view_2);
		findViewById();
		load();
		setView(this);
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	private void findViewById() {
		title = (TextView) findViewById(R.id.af_top);
		list = (ListView) findViewById(R.id.af_list);
		title.setOnClickListener(this);
		// title.setOnLongClickListener(this);
	}

	public void onClick(View v) {
		if (MainActivity.getlink()) {
			if (MainActivity.getFinal()) {
				new AlertDialog.Builder(this).setTitle("上傳值班表?")
						.setPositiveButton("否", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						}).setNegativeButton("是", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								String m = "";
								for (int i = 0; i < MainActivity.Datelist.size(); i++) {
									String str = MainActivity.Datelist.get(i);
									if (!(null == str || "".equals(str))) {
										m += (str + '║');
									}
								}
								// setFls.obtainMessage().sendToTarget();
								MainActivity.out("g10/" + m);
								MainActivity.toast(ViewActivityAutoFinal.this, "處理中.");
								MainActivity.toast(ViewActivityAutoFinal.this, "已上傳.");
								new Timer(true).schedule(new TimerTask() {
									public void run() {
										finish();
									}
								}, 3000);
							}
						}).show();
			} else {
				final EditText editText = new EditText(this);
				editText.setHint("Please Enable E-mail Account");
				editText.setText(MainActivity.email);
				new AlertDialog.Builder(this).setTitle("Send to Mail").setView(editText)
						.setPositiveButton("否", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						}).setNegativeButton("是", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								MainActivity.email = editText.getText().toString();
								MainActivity.out("g9/" + MainActivity.getSQLYM() + editText.getText().toString());
								MainActivity.toast(ViewActivityAutoFinal.this, "已發送.");
							}
						}).show();
			}
		} else {
			MainActivity.toast(this, "無法更新操作.");
		}
	}

	public boolean onLongClick(View v) {
		if (!MainActivity.getFinal()) {
			if (MainActivity.getlink()) {
				new AlertDialog.Builder(this).setTitle("清除自動排班結果?")
						.setPositiveButton("否", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						}).setNegativeButton("是", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								MainActivity.out("g15/" + MainActivity.getSQLdate());
								MainActivity.toast(ViewActivityAutoFinal.this, "處理中.");
								MainActivity.toast(ViewActivityAutoFinal.this, "已清除.");
								new Timer(true).schedule(new TimerTask() {
									public void run() {
										finish();
									}
								}, 3000);
							}
						}).show();
			} else {
				MainActivity.toast(this, "無法更新操作.");
			}
		}
		return true;
	}

	private void load() {
		int w0 = 0, w1 = 0, w2 = 0, w3 = 0, w4 = 0, w5 = 0;
		int f0 = 0, f1 = 0, f2 = 0, f3 = 0, f4 = 0, f5 = 0;
		for (int wt = 0; wt < ws; wt++) {
			int war = 0;
			String wss = MainActivity.Worklist.get(wt);
			String wna = MainActivity.getLine(wss, war, '|');
			war += wna.length() + 1;
			int wps = Integer.parseInt(MainActivity.getLine(wss, war, '|'));
			switch (wps) {
			case 0:
				w0++;
				break;
			case 1:
				w1++;
				break;
			case 2:
				w2++;
				break;
			case 3:
				w3++;
				break;
			case 4:
				w4++;
				break;
			default:
				w5++;
				break;
			}
		}

		for (int wft = 0; wft < ws; wft++) {
			int wfr = 0;
			String wfs = MainActivity.Worklist.get(wft);
			String wfa = MainActivity.getLine(wfs, wfr, '|');
			wfr += wfa.length() + 1;
			int wpf = Integer.parseInt(MainActivity.getLine(wfs, wfr, '|'));
			switch (wpf) {
			case 0:
				if (w0 > 1) {
					f0++;
					was[wft] = wm[0] + f0;
				} else {
					was[wft] = wm[0];
				}
				break;
			case 1:
				if (w1 > 1) {
					f1++;
					was[wft] = wm[1] + f1;
				} else {
					was[wft] = wm[1];
				}
				break;
			case 2:
				if (w2 > 1) {
					f2++;
					was[wft] = wm[2] + f2;
				} else {
					was[wft] = wm[2];
				}
				break;
			case 3:
				if (w3 > 1) {
					f3++;
					was[wft] = wm[3] + f3;
				} else {
					was[wft] = wm[3];
				}
				break;
			case 4:
				if (w4 > 1) {
					f4++;
					was[wft] = wm[4] + f4;
				} else {
					was[wft] = wm[4];
				}
				break;
			default:
				if (w5 > 1) {
					f5++;
					was[wft] = wm[5] + f5;
				} else {
					was[wft] = wm[5];
				}
				break;
			}
			wts.add(was[wft]);
		}

		fwb = DateUtils.getFirstDayWeek(MainActivity.Years, MainActivity.Months) - 1;
		fdy = DateUtils.getMonthDays(MainActivity.Years, MainActivity.Months);
		if (fdy + fwb <= 35) {
			frow = 5; // 改變列數等於五
		} else {
			frow = 6; // 改變列數等於六
		}
		int mLestYear = MainActivity.Years;
		int mLestMonth = (MainActivity.Months - 1);
		int mNextYear = MainActivity.Years;
		int mNextMonth = (MainActivity.Months + 1);
		if (mLestMonth < 0) { // 0-1 = -1
			mLestYear--;
			mLestMonth = 11;
		}
		if (mNextMonth > 11) { // 11+1 = 12
			mNextYear++;
			mNextMonth = 0;
		}
		String[] w = new String[ws];
		int mLestMonthDays = DateUtils.getMonthDays(mLestYear, mLestMonth);
		@SuppressWarnings("unused")
		int Y = 0, M = 0, D = 0, Days = 0;
		boolean L = false, N = false;
		for (int position = 0; position < 42; position++) {
			if (!L) {
				// 本月份第一天不為週日
				if (fwb != 0) {
					Y = mLestYear;
					M = mLestMonth;
					// 取得上個月份最後一天
					Days = mLestMonthDays - (fwb - 1);
					if (position > (fwb - 1)) {
						// 本月份第一天
						Y = MainActivity.Years;
						M = MainActivity.Months;
						Days = (fwb - 1) * (-1);
						L = true;
					}
				} else {
					Y = MainActivity.Years;
					M = MainActivity.Months;
					Days = 1;
					L = true;
				}
			}
			if (L && !N && (position + Days) > fdy) {
				Y = mNextYear;
				M = mNextMonth;
				Days = (position - 1) * (-1);
				N = true;
			}
			D = position + Days;
			int column = (position) % 7;
			int row = (position) / 7;
			if (L) {
				ArrayList<String> Daylist = new ArrayList<String>();
				String mmsg = MainActivity.Datelist.get(D), tmp;
				int run = 0;
				char key = '|';
				// TODO msg += 0 + "" + i + "/|"; 白班,白關,小夜,夜觀,大夜
				// TODO msg += 1 + "" + i + '/' + "11111" + '|'
				// TODO msg += 2 + "" + u + '/' + w + '≠' + '|'
				while (mmsg.indexOf(key, run) != -1) {
					tmp = MainActivity.getLine(mmsg, run, key);
					run += tmp.length() + 1;
					Daylist.add(tmp);
				}
				// boolean[] usa = new boolean[ws];
				for (int ub = 0; ub < ws; ub++) {
					// usa[ub] = false;
					w[ub] = "";
				}
				for (int d = 0; d < Daylist.size(); d++) {
					String msg = Daylist.get(d);
					String t = msg.substring(0, 1);
					run = 1;
					key = '/';
					switch (t) {
					case "2":
						String an = MainActivity.getLine(msg, run, key);
						run += an.length() + 1;
						String ap = MainActivity.getLine(msg, run, key);
						int ps = Integer.parseInt(ap);
						if (ps < MainActivity.Worklist.size()) {
							if (an.equals("99")) {
								an = "缺人"; // name
							}
							if (w[ps].length() > 0) {
								w[ps] = w[ps] + "," + an;
							} else {
								w[ps] = an;
							}
						}
					default:
						break;
					}
				}
			}
			TagDays[row][column] = D;
			for (int rc = 0; rc < ws; rc++) {
				daysString[row][column][rc] = w[rc];
				if (row >= 4) {
					if (D < 20) {
						daysString[row][column][rc] = "";
					}
				}
			}
		}

		// TODO TITLE
		int y = MainActivity.Years - 1911;
		int m = MainActivity.Months + 1;
		if (MainActivity.getFinal()) {
			title.setTextColor(Color.WHITE);
			title.setBackgroundColor(Color.BLACK);
			ddt = "[上傳] " + y + "年" + m + "月份值班表";
			title.setText(ddt);
		} else {
			title.setText(y + "年" + m + "月份急診部醫師工作分配及值班表");
		}

		// TODO DATE
		// for (int i = 1; i <= fdy; i++) {
		//
		// }
	}

	@SuppressWarnings("deprecation")
	private String DayToDate(TextView tv, String s, Context con) {
		if (null == s || "".equals(s)) {
			return "";
		} else {
			if (s.equals("缺人")) {
				tv.setTextColor(con.getResources().getColorStateList(R.color.red));
			}
			if (s.equals(MainActivity.name)) {
				tv.setTextColor(con.getResources().getColorStateList(R.color.red));
			}
			return s;
		}
	}

	@SuppressWarnings("deprecation")
	private String DayToDay(TextView tv, int i, int c, Context con) {
		int d = TagDays[i][c];
		tv.setTextColor(con.getResources().getColorStateList(R.color.red));
		if (i == 0) {
			if (d > 20) {
				tv.setTextColor(con.getResources().getColorStateList(R.color.gray_dr));
			}
		}
		if (i >= 4) {
			if (d < 20) {
				tv.setTextColor(con.getResources().getColorStateList(R.color.gray_dr));
			}
		}
		return d + "";
	}

	@SuppressLint("InflateParams")
	private void setListViewAdapter(int f, ArrayList<View> view, Context con) {
		final int i = f;
		View vi = LayoutInflater.from(con).inflate(R.layout.style_textview6, null);
		// TextView nu = (TextView) vi.findViewById(R.id.textview_nu);
		TextView sun = (TextView) vi.findViewById(R.id.textview_sun);
		TextView mon = (TextView) vi.findViewById(R.id.textview_mon);
		TextView tue = (TextView) vi.findViewById(R.id.textview_tue);
		TextView wed = (TextView) vi.findViewById(R.id.textview_wed);
		TextView thu = (TextView) vi.findViewById(R.id.textview_thu);
		TextView fri = (TextView) vi.findViewById(R.id.textview_fri);
		TextView sat = (TextView) vi.findViewById(R.id.textview_sat);

		sun.setText(DayToDay(sun, i, 0, con));
		mon.setText(DayToDay(mon, i, 1, con));
		tue.setText(DayToDay(tue, i, 2, con));
		wed.setText(DayToDay(wed, i, 3, con));
		thu.setText(DayToDay(thu, i, 4, con));
		fri.setText(DayToDay(fri, i, 5, con));
		sat.setText(DayToDay(sat, i, 6, con));

		view.add(vi);

		for (int s = 0; s < wts.size(); s++) {
			View vi1 = LayoutInflater.from(con).inflate(R.layout.style_textview6, null);
			TextView nu1 = (TextView) vi1.findViewById(R.id.textview_nu);
			TextView sun1 = (TextView) vi1.findViewById(R.id.textview_sun);
			TextView mon1 = (TextView) vi1.findViewById(R.id.textview_mon);
			TextView tue1 = (TextView) vi1.findViewById(R.id.textview_tue);
			TextView wed1 = (TextView) vi1.findViewById(R.id.textview_wed);
			TextView thu1 = (TextView) vi1.findViewById(R.id.textview_thu);
			TextView fri1 = (TextView) vi1.findViewById(R.id.textview_fri);
			TextView sat1 = (TextView) vi1.findViewById(R.id.textview_sat);
			nu1.setText(wts.get(s));
			sun1.setText(DayToDate(sun1, daysString[i][0][s], con));
			mon1.setText(DayToDate(mon1, daysString[i][1][s], con));
			tue1.setText(DayToDate(tue1, daysString[i][2][s], con));
			wed1.setText(DayToDate(wed1, daysString[i][3][s], con));
			thu1.setText(DayToDate(thu1, daysString[i][4][s], con));
			fri1.setText(DayToDate(fri1, daysString[i][5][s], con));
			sat1.setText(DayToDate(sat1, daysString[i][6][s], con));
			view.add(vi1);
		}
	}

	@SuppressLint("InflateParams")
	private void setView(Context con) {
		AutoFinalView.clear();
		AutoFinalView.add(LayoutInflater.from(con).inflate(R.layout.style_textview5, null));
		for (int i = 0; i < frow; i++) {
			setListViewAdapter(i, AutoFinalView, con);
		}
		list.setAdapter(new MyListAdapter(AutoFinalView));
	}

	private void addFls() {
		// ＿▃▅▇
		fls.add(ddt + "＿＿＿");
		fls.add(ddt + "＿＿▃");
		fls.add(ddt + "＿▃▅");
		fls.add(ddt + "▃▅▇");
		fls.add(ddt + "▅▇▅");
		fls.add(ddt + "▇▅▃");
		fls.add(ddt + "▅▃＿");
		fls.add(ddt + "▃＿＿");
	}

	@SuppressLint("HandlerLeak")
	private Handler edtFls = new Handler() {
		public void handleMessage(android.os.Message msg) {
			title.setText(fls.get(fs));
			if (fs != 7) {
				fs++;
			} else {
				fs = 0;
			}
		};
	};

	@SuppressLint("HandlerLeak")
	private Handler setFls = new Handler() {
		public void handleMessage(android.os.Message msg) {
			addFls();
			flashing.removeCallbacks(Run);// 如有運作則停止
			flashing.postDelayed(Run, 500);// 設定1秒後啟動
		};
	};

	private Runnable Run = new Runnable() {
		public void run() {
			edtFls.obtainMessage().sendToTarget();
			flashing.postDelayed(this, 250);
		}
	};
}
