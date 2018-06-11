package iccl.workshifts.ubeta;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class ViewActivityLogin extends Activity implements OnClickListener {

	private static Activity activity;
	private static EditText name, pass;
	private boolean guest = false;
	private ImageView yes, no;

	public ViewActivityLogin() {
		activity = ViewActivityLogin.this;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_view);
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
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (guest) {
				MainActivity.KDHandler.obtainMessage().sendToTarget();
			} else {
				finish();
			}
		}
		return false;
	}

	public void onClick(View v) {
		if (v == yes) {
			if (MainActivity.getlink()) {
				check();
			} else {
				MainActivity.toast(this, "無法更新操作.");
			}
		}
		if (v == no) {
			if (guest) {
				MainActivity.KDHandler.obtainMessage().sendToTarget();
			} else {
				finish();
			}
		}
	}

	private void findViewById() {
		name = (EditText) findViewById(R.id.lgv_name);
		pass = (EditText) findViewById(R.id.lgv_pass);
		yes = (ImageView) findViewById(R.id.lgv_yes);
		no = (ImageView) findViewById(R.id.lgv_no);
	}

	private void check() {
		String na = name.getText().toString();
		String pp = pass.getText().toString();
		if (!"MASTER".equals(na)) {
			MainActivity.out("g16/" + na + '|' + pp + '|');
		} else {
			MainActivity.toast(this, "使用者權限不足.");
			pass.setText("");
			name.setText("");
		}
	}

	private void load() {
		if ("USER".equals(MainActivity.getUserName())) {
			guest = true;
		}
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
	}

	public static Handler R0Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MainActivity.activity = null;
			MainActivity.name = name.getText().toString();
			MainActivity
					.out("g0/" + MainActivity.ver + '|' + MainActivity.name + '|' + MainActivity.getSQLdate() + '|');
			MainActivity.out("g4/" + MainActivity.getSQLdate());
			MainActivity.toast(ViewActivityLogin.activity, "登入成功!");
			activity.finish();
		};
	};

	public static Handler R1Handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MainActivity.toast(ViewActivityLogin.activity, "使用者名稱或密碼錯誤.");
			pass.setText("");
			name.setText("");
		};
	};
}
