package iccl.workshifts.ubeta;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewActivityUserMore extends Activity implements OnClickListener {
	private boolean RE = false;
	private ArrayList<EditText> ET = new ArrayList<EditText>();
	private EditText name, pass, other, max;
	private ImageView yes, no;
	private String na, pw, ot, mx;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_view_m);
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
				save();
			} else {
				MainActivity.toast(this, "無法更新操作.");
			}
		}
		if (v == no) {
			finish();
		}
	}

	private void findViewById() {
		ET.add(name = (EditText) findViewById(R.id.uvm_name));
		ET.add(pass = (EditText) findViewById(R.id.uvm_pass));
		ET.add(other = (EditText) findViewById(R.id.uvm_other));
		ET.add(max = (EditText) findViewById(R.id.uvm_max));
		yes = (ImageView) findViewById(R.id.uvm_yes);
		no = (ImageView) findViewById(R.id.uvm_no);
	}

	private void save() {
		boolean re = false, ns = false, ps = false;
		String ms = "g6/1" + na + '/';
		String[] m = { na, pw, ot, mx };
		for (int i = 0; i < ET.size(); i++) {
			String s = ET.get(i).getText().toString();
			if (s.indexOf('/') == -1 && s.indexOf('|') == -1 && s.indexOf('║') == -1) {
				if (s.length() != 0) {
					ms += s + '|';
					if (i == 0) {
						if (!s.equals(m[i])) {
							for (int j = 0; j < MainActivity.Userlist.size(); j++) {
								if (!MainActivity.getLine(MainActivity.Userlist.get(j), 0, '|').equals(s)) {
									re = true;
								} else {
									ns = true;
									break;
								}
							}
							if (ns) {
								MainActivity.toast(this, "代號重複.");
								name.setText("");
								ps = true;
								re = false;
								break;
							}
						}
					} else if (i == 1) {
						if (!s.equals(m[i])) {
							if (s.length() >= 1) {
								re = true;
							} else {
								MainActivity.toast(this, "密碼太短.");
								pass.setText("");
								ps = true;
								re = false;
								break;
							}
						}
					} else {
						if (!s.equals(m[i])) {
							re = true;
						}
					}
				} else {
					if (i == 0) {
						re = false;
						break;
					} else {
						if (i == 3) {
							s = "0";
						}
						ms += s + '|';
						re = true;
					}
				}
			} else {
				MainActivity.toast(this, "無法使用特殊字元.");
				ps = true;
				re = false;
				break;
			}
		}
		if (!ps) {
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
				finish();
			}
		}
	}

	private void load() {
		// A|||
		int r = 0;
		String s = MainActivity.Userlist.get(MoreActivity.getItem());
		na = MainActivity.getLine(s, r, '|');
		r += na.length() + 1;
		pw = MainActivity.getLine(s, r, '|');
		r += pw.length() + 1;
		ot = MainActivity.getLine(s, r, '|');
		r += ot.length() + 1;
		mx = MainActivity.getLine(s, r, '|');
		name.setText(na);
		pass.setText(pw);
		other.setText(ot);
		max.setText(mx);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
		if (!na.equals(MainActivity.name)) {
			for (int i = 0; i < ET.size(); i++) {
				((TextView) ET.get(i)).setEnabled(false);
			}
			yes.setVisibility(View.INVISIBLE);
		}
		if (!"MASTER".equals(MainActivity.name)) {
			max.setEnabled(false);
		}
	}
}
