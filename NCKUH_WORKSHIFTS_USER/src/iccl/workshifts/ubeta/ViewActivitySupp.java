package iccl.workshifts.ubeta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewActivitySupp extends Activity implements OnClickListener, OnItemSelectedListener {
	private int ttp = 0;
	private boolean add = true;
	private ArrayList<String> Spplist = new ArrayList<String>();
	private String[] sort = { "白班", "白觀", "小夜", "夜觀", "大夜" };
	private String ms = "2", spn, sps;
	private Spinner spp;
	private TextView name, people, types, stime, etime, maxcon;
	private ImageView yes, no, menu;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.supp_view);
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
		if (v == menu) {
			new AlertDialog.Builder(this).setTitle("新增至個人月曆?")
					.setPositiveButton("否", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
						}
					}).setNegativeButton("是", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							getCalendar();
						}
					}).show();
		}
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		sps = Spplist.get(position);
	}

	public void onNothingSelected(AdapterView<?> parent) {

	}

	private void findViewById() {
		MainActivity.main = false;
		spp = (Spinner) findViewById(R.id.supp_spp);
		types = (TextView) findViewById(R.id.supp_type);
		name = (TextView) findViewById(R.id.supp_name);
		stime = (TextView) findViewById(R.id.supp_stime);
		etime = (TextView) findViewById(R.id.supp_etime);
		people = (TextView) findViewById(R.id.supp_people);
		maxcon = (TextView) findViewById(R.id.supp_maxcon);
		yes = (ImageView) findViewById(R.id.sp_yes);
		no = (ImageView) findViewById(R.id.sp_no);
		menu = (ImageView) findViewById(R.id.sp_menu);
	}

	private void getCalendar() {
		String tp = types.getText().toString(), ss = stime.getText().toString(), ee = etime.getText().toString();
		int y = MainActivity.Years, m = MainActivity.Months, d = MainActivity.Days;
		int st = Integer.parseInt(ss.substring(0, 2)), sm = Integer.parseInt(ss.substring(3, 5)),
				et = Integer.parseInt(ee.substring(0, 2)), em = Integer.parseInt(ee.substring(3, 5));
		if (ttp == 4) {
			if (d == DateUtils.getMonthDays(y, m)) {
				if (m == 11) {
					y++;
					m = 0;
					d = 1;
				} else {
					m++;
					d = DateUtils.getMonthDays(y, m);
				}
			} else {
				d++;
			}
		}
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(y, m, d, st, sm);
		Calendar endTime = Calendar.getInstance();
		if (st >= et) {
			endTime.set(y, m, d + 1, et, em);
		} else {
			endTime.set(y, m, d, et, em);
		}
		Intent calIntent = new Intent(Intent.ACTION_INSERT);
		calIntent.setType("vnd.android.cursor.item/event");
		calIntent.putExtra(Events.TITLE, name.getText().toString());
		calIntent.putExtra(Events.DESCRIPTION, "班別:" + tp + '\n' + "時間:" + ss + "至" + ee);
		calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
		calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
		startActivity(calIntent);
	}

	private void edtSpp() {
		String sf = ms.substring(0, 1) + sps + ms.substring(1);
		if (sps.equals("缺人")) {
			MainActivity.setSppItem(this, ms.substring(0, 1) + "99" + ms.substring(1));
		} else {
			MainActivity.setSppItem(this, sf);
		}
		if (add) {
			if (!spn.equals("缺人")) {
				MainActivity.addSppItem(this, "3" + spn);
			}
		}
	}

	private void save() {
		if (spn.equals(sps)) {
			finish();
		} else {
			if (MainActivity.getFinal()) {
				edtSpp();
				finish();
			} else {
				if (MainActivity.getlink()) {
					edtSpp();
					MainActivity.edtSppItem(this);
					finish();
				} else {
					MainActivity.toast(this, "無法更新操作.");
				}
			}
		}
	}

	private void load() {
		int run = 1;
		char key = '/';
		String m = MainActivity.getDayItem(); // e:2C/0/,299/1/
		String an = MainActivity.getLine(m, run, key);
		run += an.length() + 1;
		String ap = MainActivity.getLine(m, run, key);
		run += ap.length() + 1;
		String ac = m.substring(run);
		int ps = Integer.parseInt(ap);
		if (ps < MainActivity.Worklist.size()) {
			if (an.equals("99")) {
				an = "缺人";
				add = false;
			}
			ArrayList<String> userlist = new ArrayList<String>();
			ArrayList<String> userwork = new ArrayList<String>();
			ArrayList<String> userbusy = new ArrayList<String>();
			for (int i = 0; i < MainActivity.Daylist.size(); i++) {
				String sm = MainActivity.Daylist.get(i); // 00
				String st = sm.substring(0, 1); // 0:work,1:user
				String ss = sm.substring(1);
				switch (st) {
				case "0":
					break;
				case "1":
					break;
				case "2":
					String pm = MainActivity.getLine(ss, 0, key);
					if (!an.equals(pm)) {
						userwork.add(pm + "(值班)");
					}
					break;
				case "3":
					userlist.add(ss + "(休假)");
					break;
				case "4":
					userbusy.add(ss + "(有事)");
					break;
				default:
					break;
				}
			}
			int r = 0;
			String s = MainActivity.Worklist.get(ps);
			String na = MainActivity.getLine(s, r, '|');
			r += na.length() + 1;
			String tp = MainActivity.getLine(s, r, '|');
			r += 2;
			String pp = MainActivity.getLine(s, r, '|');
			r += pp.length() + 1;
			String st = MainActivity.getLine(s, r, '|');
			r += st.length() + 1;
			String et = MainActivity.getLine(s, r, '|');
			r += et.length() + 1;
			String mx = MainActivity.getLine(s, r, '|');
			types.setText(sort[ttp = Integer.parseInt(tp)]);
			name.setText(na);
			people.setText(pp);
			stime.setText(st.substring(0, 2) + ":" + st.substring(2, 4));
			etime.setText(et.substring(0, 2) + ":" + et.substring(2, 4));
			maxcon.setText(mx);
			if (ac.length() != 0) {
				ArrayList<String> list = new ArrayList<String>();
				int cr = 0;
				char k = '/';
				while (ac.indexOf(k, cr) != -1) {
					String tmp = MainActivity.getLine(ac, cr, k);
					list.add(tmp); // A||
					cr += tmp.length() + 1;
				}
				for (int l = 0; l < list.size(); l++) {
					listload(list.get(l));
				}
			}
			Spplist.add(an);
			spn = an;
			for (int sp1 = 0; sp1 < userlist.size(); sp1++) {
				Spplist.add(userlist.get(sp1));
			}
			for (int sp2 = 0; sp2 < userwork.size(); sp2++) {
				Spplist.add(userwork.get(sp2));
			}
			for (int sp3 = 0; sp3 < userbusy.size(); sp3++) {
				Spplist.add(userbusy.get(sp3));
			}
			if (add) {
				Spplist.add("缺人");
			}
			ArrayAdapter<String> mode_adapter_0 = new ArrayAdapter<String>(this, R.layout.style_spinner, Spplist);
			mode_adapter_0.setDropDownViewResource(R.layout.style_spinner);
			spp.setAdapter(mode_adapter_0);
			spp.setOnItemSelectedListener(this);
			yes.setOnClickListener(this);
			no.setOnClickListener(this);
			menu.setOnClickListener(this);
			ms += ('/' + ap + '/');
			yes.setVisibility(View.INVISIBLE);
		} else {
			MainActivity.toast(this, "無此班別資訊!");
			new Timer(true).schedule(new TimerTask() {
				public void run() {
					finish();
				}
			}, 3000);
		}
	}

	private void listload(String s) {
		String type = s.substring(0, 1);
		String msg = s.substring(1);
		switch (type) {
		case "0":
			name.setText(msg);
			break;
		case "1":
			people.setText(msg);
			break;
		case "2":
			String st = msg;
			stime.setText(st + ":00");
			break;
		case "3":
			String et = msg;
			etime.setText(et + ":00");
			break;
		default:
			break;
		}
	}
}
