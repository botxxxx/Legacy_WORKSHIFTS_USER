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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ViewActivityWorkMore extends Activity implements OnClickListener, OnItemSelectedListener {
	private ArrayList<View> V = new ArrayList<View>();
	private String na, pp, st, et, mx, h, m, th, sm;
	private EditText name, people, maxcon, thour, sum;
	private TextView stime, etime;
	private ImageView yes, no;
	private Spinner types;
	private boolean RE = false;
	private int tp = 0, tr = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_view_m);
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
		if (RE) {
			MoreActivity.setView(this);
			MainActivity.day_upload(this);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	public void onClick(View v) {
		if (v == yes) {
			if (MainActivity.getlink()) {
				// save();
			} else {
				MainActivity.toast(this, "無法更新操作.");
			}
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
		V.add(name = (EditText) findViewById(R.id.wvm_name));
		V.add(people = (EditText) findViewById(R.id.wvm_people));
		V.add(stime = (TextView) findViewById(R.id.wvm_stime));
		V.add(etime = (TextView) findViewById(R.id.wvm_etime));
		V.add(maxcon = (EditText) findViewById(R.id.wvm_maxcon));
		V.add(thour = (EditText) findViewById(R.id.wvm_time));
		V.add(sum = (EditText) findViewById(R.id.wvm_sum));
		types = (Spinner) findViewById(R.id.wvm_type);
		yes = (ImageView) findViewById(R.id.wvm_yes);
		no = (ImageView) findViewById(R.id.wvm_no);
	}

	@SuppressWarnings("unused")
	private void save() {
		boolean re = false, ns = false;
		String ms = "g6/0" + na + '/', s;
		String[] m = { na, pp, st, et, mx, th, sm };
		for (int i = 0; i < V.size(); i++) {
			if (i != 2 && i != 3) {
				s = ((EditText) V.get(i)).getText().toString();
			} else {
				s = ((TextView) V.get(i)).getText().toString();
				s = s.substring(0, 2) + s.substring(3, 5);
			}
			if (s.indexOf('/') == -1 && s.indexOf('|') == -1 && s.indexOf('║') == -1) {
				if (s.length() != 0) {
					if (i == 0) {
						ms += s + '|' + (tp + "") + '|';
					} else {
						ms += s + '|';
					}
					if (!s.equals(m[i])) {
						if (i == 0) {
							for (int j = 0; j < MainActivity.Worklist.size(); j++) {
								if (MainActivity.getLine(MainActivity.Worklist.get(j), 0, '|').equals(s)) {
									ns = true;
									break;
								}
							}
							if (ns) {
								MainActivity.toast(this, "代號重複.");
								re = false;
								break;
							} else {
								re = true;
							}
						} else {
							re = true;
						}
					}
				} else {
					re = false;
					break;
				}
			} else {
				MainActivity.toast(this, s + " 無法使用特殊字元.");
				re = false;
				break;
			}
		}
		if (!ns && tp != tr) {
			re = true;
		}
		if (re) {
			MainActivity.out(ms);
			MainActivity.toast(this, "處理中.");
			MainActivity.toast(this, "已更新.");
			RE = true;
			new Timer(true).schedule(new TimerTask() {
				public void run() {
					finish();
				}
			}, 3000);
		} else {
			if (!ns) {
				finish();
			}
		}
	}

	private void load() {
		// 白班教學回診1|1|08|11|0|
		int r = 0;
		String s = MainActivity.Worklist.get(MoreActivity.getItem());
		na = MainActivity.getLine(s, r, '|');
		r += na.length() + 1;
		tr = Integer.parseInt(MainActivity.getLine(s, r, '|'));
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
		// title.setText(na);
		name.setText(na);
		people.setText(pp);
		stime.setText(st.substring(0, 2) + ":" + st.substring(2, 4));
		etime.setText(et.substring(0, 2) + ":" + et.substring(2, 4));
		maxcon.setText(mx);
		thour.setText(th);
		sum.setText(sm);
		stime.setOnClickListener(this);
		etime.setOnClickListener(this);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
		types.setOnItemSelectedListener(this);
		ArrayList<String> typeList = new ArrayList<String>();
		typeList.add("白班");
		typeList.add("白觀");
		typeList.add("小夜");
		typeList.add("夜觀");
		typeList.add("大夜");
		ArrayAdapter<String> Spp_adapter = new ArrayAdapter<String>(this, R.layout.style_spinner, typeList);
		Spp_adapter.setDropDownViewResource(R.layout.style_spinner);
		types.setAdapter(Spp_adapter);
		types.setSelection(tr);
		for (int i = 0; i < V.size(); i++) {
			((TextView) V.get(i)).setEnabled(false);
		}
		types.setEnabled(false);
		yes.setVisibility(View.INVISIBLE);
	}

	@SuppressWarnings("deprecation")
	private void myDialogs(final int s) {
		int hour = 0, min = 0;
		if (s == 0) {
			hour = Integer.parseInt(stime.getText().toString().substring(0, 2));
			min = Integer.parseInt(stime.getText().toString().substring(3, 5));
		} else {
			hour = Integer.parseInt(etime.getText().toString().substring(0, 2));
			min = Integer.parseInt(etime.getText().toString().substring(3, 5));
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

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		tp = position;
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		Toast.makeText(this, "您沒有選擇任何班別", Toast.LENGTH_LONG).show();
	}
}
