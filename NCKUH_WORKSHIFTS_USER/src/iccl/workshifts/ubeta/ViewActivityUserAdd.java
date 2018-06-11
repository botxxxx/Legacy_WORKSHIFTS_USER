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

public class ViewActivityUserAdd extends Activity implements OnClickListener {
	private boolean RE = false;
	private ArrayList<EditText> ET = new ArrayList<EditText>();
	private String ms = "g11/1";
	private EditText name, pass;
	private ImageView yes, no;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_add_m);
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

	@SuppressWarnings("unused")
	private void findViewById() {
		EditText other, max;
		ET.add(name = (EditText) findViewById(R.id.uvm_name));
		ET.add(pass = (EditText) findViewById(R.id.uvm_pass));
		ET.add(other = (EditText) findViewById(R.id.uvm_other));
		ET.add(max = (EditText) findViewById(R.id.uvm_max));
		yes = (ImageView) findViewById(R.id.uvm_yes);
		no = (ImageView) findViewById(R.id.uvm_no);
	}

	private void save() {
		boolean re = false, ns = false, ps = false;
		for (int i = 0; i < ET.size(); i++) {
			String s = ET.get(i).getText().toString();
			if (s.indexOf('/') == -1 && s.indexOf('|') == -1 && s.indexOf('║') == -1) {
				if (s.length() != 0) {
					ms += s + '|';
					if (i == 0) {
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
					} else if (i == 1) {
						if (s.length() >= 1) {
							re = true;
						} else {
							MainActivity.toast(this, "密碼太短.");
							pass.setText("");
							ps = true;
							re = false;
							break;
						}
					} else {
						re = true;
					}
				} else {
					if (i == 0) {
						ps = true;
						re = false;
						MainActivity.toast(this, "操作異常.");
						break;
					} else if (i == 3) {
						ms += "0|";
						re = true;
					} else {
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
			if (re && MainActivity.g9) {
				new AlertDialog.Builder(this).setTitle("捨棄自動排班暫存?")
						.setPositiveButton("否", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						}).setNegativeButton("是", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								MainActivity.out(ms);
								MainActivity.toast(ViewActivityUserAdd.this, "處理中.");
								MainActivity.toast(ViewActivityUserAdd.this, "已更新.");
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
	}

	private void load() {
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
	}
}
