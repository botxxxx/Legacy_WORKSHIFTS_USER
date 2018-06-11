package iccl.workshifts.ubeta;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class ViewActivityWork extends Activity implements OnClickListener {
	private boolean lock = true;
	private String[] sort = { "白班", "白觀", "小夜", "夜觀", "大夜" };
	private String ms = "0", na, tp, pp, st, et, mx, h, m, th, sm;
	private EditText name, people;
	private TextView title, types, stime, etime, maxcon, thour, sum;
	private ImageView yes, no;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_view);
		findViewById();
		load();
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
		MainActivity.main = true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	public void onClick(View v) {
		if (v == yes) {
			save();
		}
		if (v == no) {
			finish();
		}
		if (v == stime) {
			myDialogs(0);
			// new TimePickerFragment().show(getFragmentManager(),"TimePicker");
		}
		if (v == etime) {
			myDialogs(1);
			// new TimePickerFragment().show(getFragmentManager(),"TimePicker");
		}
	}

	private void findViewById() {
		MainActivity.main = false;
		title = (TextView) findViewById(R.id.work_t);
		types = (TextView) findViewById(R.id.work_type);
		name = (EditText) findViewById(R.id.work_name);
		stime = (TextView) findViewById(R.id.work_stime);
		etime = (TextView) findViewById(R.id.work_etime);
		people = (EditText) findViewById(R.id.work_people);
		maxcon = (TextView) findViewById(R.id.work_maxcon);
		thour = (TextView) findViewById(R.id.work_hour);
		sum = (TextView) findViewById(R.id.work_sum);
		yes = (ImageView) findViewById(R.id.wv_yes);
		no = (ImageView) findViewById(R.id.wv_no);
	}

	private void save() {
		String m = "";
		if (chk((TextView) name, na, false)) {
			m += "0" + gs((TextView) name, na, false) + "/";
		}
		if (chk((TextView) people, pp, false)) {
			m += "1" + gs((TextView) people, pp, false) + "/";
		}
		if (chk((TextView) stime, st, true)) {
			m += "2" + gs((TextView) stime, st, true) + "/";
		}
		if (chk((TextView) etime, et, true)) {
			m += "3" + gs((TextView) etime, et, true) + "/";
		}
		if (m.length() == 0) {
			finish();
		} else {
			ms += m;
			MainActivity.setDayItem(this, ms);
			finish();
		}
	}

	private boolean chk(TextView a, String b, boolean Cut) {
		String c = a.getText().toString();
		if (Cut) {
			c = c.substring(0, 2) + c.substring(3, 5);
		}
		if (c.length() != 0) {
			if (c.equals(b)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private String gs(TextView a, String b, boolean Cut) {
		String c = a.getText().toString();
		if (Cut) {
			c = c.substring(0, 2) + c.substring(3, 5);
		}
		return c;
	}

	@SuppressWarnings("deprecation")
	private void load() {
		String m = MainActivity.getDayItem();
		String p = MainActivity.getLine(m, 1, '/'); // work.get(p);
		String s;
		String c = m.substring(2 + p.length());
		// 白班教學回診1|1|08|11|
		int r = 0;
		int awp = Integer.parseInt(p);
		if (awp < MainActivity.Worklist.size()) {
			s = MainActivity.Worklist.get(awp);
			na = MainActivity.getLine(s, r, '|');
			r += na.length() + 1;
			tp = MainActivity.getLine(s, r, '|');
			r += 2;
			pp = MainActivity.getLine(s, r, '|');
			r += pp.length() + 1;
			st = MainActivity.getLine(s, r, '|');
			r += st.length() + 1;
			et = MainActivity.getLine(s, r, '|');
			r += et.length() + 1;
			mx = MainActivity.getLine(s, r, '|');
			r += mx.length() + 1;
			th = MainActivity.getLine(s, r, '|');
			r += th.length() + 1;
			sm = MainActivity.getLine(s, r, '|');
			title.setText(na);
			types.setText(sort[Integer.parseInt(tp)]);
			name.setText(na);
			people.setText(pp);
			stime.setText(st.substring(0, 2) + ":" + st.substring(2, 4));
			etime.setText(et.substring(0, 2) + ":" + et.substring(2, 4));
			maxcon.setText(mx);
			thour.setText(th);
			sum.setText(sm);
			if (c.length() != 0) {
				// MainActivity.toast(this, "cc");
				ArrayList<String> list = new ArrayList<String>();
				int run = 0;
				char key = '/';
				while (c.indexOf(key, run) != -1) {
					String tmp = MainActivity.getLine(c, run, key);
					list.add(tmp); // A||
					run += tmp.length() + 1;
				}
				for (int i = 0; i < list.size(); i++) {
					listload(list.get(i));
				}
			}
			stime.setOnClickListener(this);
			etime.setOnClickListener(this);
			yes.setOnClickListener(this);
			no.setOnClickListener(this);
			ms += (p + '/');
		} else {
			MainActivity.toast(this, "無此班別資訊!");
			new Timer(true).schedule(new TimerTask() {
				public void run() {
					finish();
				}
			}, 3000);
		}
		if (lock) {
			name.setEnabled(false);
			stime.setEnabled(false);
			etime.setEnabled(false);
			people.setEnabled(false);
			yes.setVisibility(View.INVISIBLE);
			types.setTextColor(this.getResources().getColorStateList(R.color.gray));
			name.setTextColor(this.getResources().getColorStateList(R.color.gray));
			stime.setTextColor(this.getResources().getColorStateList(R.color.gray));
			etime.setTextColor(this.getResources().getColorStateList(R.color.gray));
			people.setTextColor(this.getResources().getColorStateList(R.color.gray));
			maxcon.setTextColor(this.getResources().getColorStateList(R.color.gray));
			thour.setTextColor(this.getResources().getColorStateList(R.color.gray));
			sum.setTextColor(this.getResources().getColorStateList(R.color.gray));
		}
	}

	private void listload(String s) {
		String type = s.substring(0, 1);
		String msg = s.substring(1);
		switch (type) {
		case "0":
			na = msg;
			title.setText(na);
			name.setText(na);
			break;
		case "1":
			pp = msg;
			people.setText(pp);
			break;
		case "2":
			st = msg;
			stime.setText(st.substring(0, 2) + ":" + st.substring(2, 4));
			break;
		case "3":
			et = msg;
			etime.setText(et.substring(0, 2) + ":" + et.substring(2, 4));
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void myDialogs(final int s) {
		int hour = 0, min = 0;
		if (s == 0) {
			if (stime.getText().toString().length() != 0) {
				hour = Integer.parseInt(stime.getText().toString().substring(0, 2));
				min = Integer.parseInt(stime.getText().toString().substring(3, 5));
			}
		} else {
			if (etime.getText().toString().length() != 0) {
				hour = Integer.parseInt(etime.getText().toString().substring(0, 2));
				min = Integer.parseInt(etime.getText().toString().substring(3, 5));
			}
		}
		TimePicker mDatePicker = new TimePicker(this);
		mDatePicker.setCurrentHour(hour);
		mDatePicker.setCurrentMinute(min);
		mDatePicker.setIs24HourView(true);
		mDatePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				h = hourOfDay + "";
				if (hourOfDay < 10) {
					h = "0" + h;
				}
				m = minute + "";
				if (minute < 10) {
					m = "0" + m;
				}
			}
		});
		AlertDialog.Builder timeDialog = new AlertDialog.Builder(this);
		timeDialog.setIcon(null);
		timeDialog.setCancelable(false);
		timeDialog.setView(mDatePicker);
		timeDialog.setNegativeButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (s == 0) {
					stime.setText(getH() + ":" + getM());
				} else {
					etime.setText(getH() + ":" + getM());
				}
				// mDatePicker.getCurrentMinute();
			}
		}).setPositiveButton("否", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
			}
		}).create();
		timeDialog.show();
	}

	private String getH() {
		return h;
	}

	private String getM() {
		return m;
	}
}
