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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ViewActivityWorkAdd extends Activity implements OnClickListener, OnItemSelectedListener {
	private boolean RE = false;
	private int tr = 0;
	private String h, na, st, et, pp, mx, th, sm;
	private Spinner types;
	private EditText name, people, maxcon;
	private TextView stime, etime, thour, sum;
	private ImageView yes, no;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_add_m);
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
			save();
		}
		if (v == no) {
			finish();
		}
		if (v == stime) {
			myDialogs(0);
		}
		if (v == etime) {
			myDialogs(1);
		}
	}

	private void findViewById() {
		types = (Spinner) findViewById(R.id.wa_type);
		name = (EditText) findViewById(R.id.wa_name);
		stime = (TextView) findViewById(R.id.wa_stime);
		etime = (TextView) findViewById(R.id.wa_etime);
		people = (EditText) findViewById(R.id.wa_people);
		maxcon = (EditText) findViewById(R.id.wa_maxcon);
		thour = (TextView) findViewById(R.id.wa_time);
		sum = (TextView) findViewById(R.id.wa_sum);
		yes = (ImageView) findViewById(R.id.wa_yes);
		no = (ImageView) findViewById(R.id.wa_no);
	}

	private void save() {
		boolean ns = false, re = false;
		if (chk((TextView) name, 0)) {
			if (chk((TextView) stime, 1)) {
				if (chk((TextView) etime, 2)) {
					if (chk((TextView) people, 3)) {
						if (chk((TextView) maxcon, 4)) {
							if (chk((TextView) thour, 5)) {
								if (chk((TextView) sum, 6)) {
									for (int j = 0; j < MainActivity.Worklist.size(); j++) {
										if (MainActivity.getLine(MainActivity.Worklist.get(j), 0, '|').equals(na)) {
											ns = true;
											break;
										}
									}
									if (!ns) {
										re = true;
									} else {
										Toast.makeText(this, "名稱重複.", Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(this, "您沒有輸入費用!", Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(this, "您沒有輸入工時!", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(this, "您沒有輸入連續工作上限!", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(this, "您沒有輸入人數!", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(this, "您沒有設定結束時間!", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "您沒有設定開始時間!", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "您沒有輸入名稱!", Toast.LENGTH_SHORT).show();
		}
		if (re && MainActivity.g9) {
			new AlertDialog.Builder(this).setTitle("捨棄自動排班暫存?")
					.setPositiveButton("否", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
						}
					}).setNegativeButton("是", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							MainActivity.out("g11/0" + na + '|' + tr + '|' + pp + '|' + st + '|' + et + '|' + mx + '|'
									+ th + '|' + sm + '|');
							MainActivity.toast(ViewActivityWorkAdd.this, "處理中.");
							MainActivity.toast(ViewActivityWorkAdd.this, "已更新.");
							RE = true;
							new Timer(true).schedule(new TimerTask() {
								public void run() {
									finish();
								}
							}, 3000);
						}
					}).show();
		} else {
			if (re) {
				MainActivity.out("g11/0" + na + '|' + tr + '|' + pp + '|' + st + '|' + et + '|' + mx + '|' + th + '|'
						+ sm + '|');
				MainActivity.toast(this, "處理中.");
				MainActivity.toast(this, "已更新.");
				RE = true;
				new Timer(true).schedule(new TimerTask() {
					public void run() {
						finish();
					}
				}, 3000);
			}
		}
	}

	private boolean chk(TextView a, int b) {
		String s = a.getText().toString();
		if (s.length() != 0) {
			switch (b) {
			case 0:
				na = s;
				break;
			case 1:
				st = s.substring(0, 2);
				break;
			case 2:
				et = s.substring(0, 2);
				break;
			case 3:
				pp = s;
				break;
			case 4:
				mx = s;
				break;
			case 5:
				th = s;
				break;
			case 6:
				sm = s;
				break;
			}
		}
		if (s.length() != 0) {
			return true;
		} else {
			return false;
		}
	}

	private void load() {
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
		types.setSelection(0);
	}

	@SuppressWarnings("deprecation")
	private void myDialogs(final int s) {
		int hour = 0;
		if (s == 0) {
			if (stime.getText().toString().length() != 0) {
				hour = Integer.parseInt(stime.getText().toString().substring(0, 2));
			}
		} else {
			if (etime.getText().toString().length() != 0) {
				hour = Integer.parseInt(etime.getText().toString().substring(0, 2));
			}
		}
		TimePicker mDatePicker = new TimePicker(this);
		mDatePicker.setCurrentHour(hour);
		mDatePicker.setCurrentMinute(0);
		mDatePicker.setIs24HourView(true);
		mDatePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				h = hourOfDay + "";
				if (hourOfDay < 10) {
					h = "0" + h;
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
					stime.setText(getH() + ":00");
				} else {
					etime.setText(getH() + ":00");
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
		if (h != null) {
			return h;
		} else {
			return "00";
		}
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		tr = position;
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		Toast.makeText(this, "您沒有選擇任何班別", Toast.LENGTH_LONG).show();
	}
}
