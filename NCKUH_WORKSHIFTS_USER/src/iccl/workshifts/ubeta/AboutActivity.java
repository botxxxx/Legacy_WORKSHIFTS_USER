package iccl.workshifts.ubeta;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class AboutActivity extends Activity {
	private ListView listview;
	private ArrayList<String[]> Aboutlist = new ArrayList<String[]>();
	public static ArrayList<View> View = new ArrayList<View>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_view);
		findViewById();
		setAbout();
		setView();
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
			if (MainActivity.about == 0) {
				MainActivity.main = true;
			}
			finish();
		}
		return false;
	}

	private void findViewById() {
		if (MainActivity.about == 0) {
			MainActivity.main = false;
		}
		listview = (ListView) findViewById(R.id.listview);
	}

	private void setAbout() {
		switch (MainActivity.about) {
		case 0:
			Aboutlist.add(new String[] { "連線狀態", "右上方雲朵形狀圖示為連線狀態,更換月份時須點擊圖示進行手動更新." });
			// Aboutlist.add(new String[] { "新增班別", "點擊左上方的日期,可於當日新增班別." });
			// Aboutlist.add(new String[] { "修改班別", "點擊清單內的班別資料,可修改當日班別." });
			// Aboutlist.add(new String[] { "移除班別", "長按清單內的選項可移除,當日將不排此班別." });
			// Aboutlist.add(new String[] { "新增人員", "點擊左上方的日期,可於當日新增人員." });
			Aboutlist.add(new String[] { "修改意願", "點擊清單內的使用者,可修改當日值班意願." });
			// Aboutlist.add(new String[] { "移除人員", "長按清單內的選項可移除,並於班表顯示有事." });
			// Aboutlist.add(new String[] { "檢視排班", "點擊上方的(↑)圖示可檢視自動排班結果." });
			// Aboutlist.add(new String[] { "約班紀錄", "右上方設定選單內的約班紀錄,可切換至約班或排班班表." });
			Aboutlist.add(new String[] { "詳細資料", "右上方設定選單內的詳細資料,可檢視班表與人員." });
			Aboutlist.add(new String[] { "缺人提示", "如有缺人情形將在月曆上用紅點標示." });
			Aboutlist.add(new String[] { "調班作業", "點擊清單內已完成排班之班別進行修改." });
			Aboutlist.add(new String[] { "小提醒", "自動排班後無法新增、修改與移除約班." });
			break;
		case 1:
			Aboutlist.add(new String[] { "開始約班", "新增班別與人員至當月份." });
			Aboutlist.add(new String[] { "自動排班", "已完成約班後可使用自動排班." });
			Aboutlist.add(new String[] { "清除班表", "清除當月份所有約班與排班." });
			Aboutlist.add(new String[] { "新增班別", "將自訂班別新增至清單內." });
			Aboutlist.add(new String[] { "修改班別", "點擊清單內的班別可修改,班別類型如:白班,白觀等." });
			Aboutlist.add(new String[] { "修改名稱", "點擊清單內的班別可修改,班別名稱可與其他班別重複." });
			Aboutlist.add(new String[] { "修改時間", "點擊清單內的班別可修改." });
			Aboutlist.add(new String[] { "修改人數", "點擊清單內的班別可修改." });
			Aboutlist.add(new String[] { "工作上限", "點擊清單內的班別可修改." });
			Aboutlist.add(new String[] { "移除班別", "長按清單內的選項可移除." });
			Aboutlist.add(new String[] { "重置班別", "重置班別為預設值." });
			Aboutlist.add(new String[] { "小提醒", "欲清除已上傳的自動排班結果請至Xls長按標題." });
			break;
		case 2:
			Aboutlist.add(new String[] { "新增人員", "將自訂人員新增至清單內." });
			Aboutlist.add(new String[] { "更改代號", "點擊清單內的人員可修改,可使用中英日文但不可與其他使用者重複." });
			Aboutlist.add(new String[] { "排班上限", "點擊清單內的人員可修改,可限制人員於自動排班之排班上限." });
			Aboutlist.add(new String[] { "設定密碼", "點擊清單內的人員可修改." });
			Aboutlist.add(new String[] { "添加備註", "點擊清單內的人員可修改." });
			Aboutlist.add(new String[] { "移除人員", "長按清單內的選項可移除." });
			Aboutlist.add(new String[] { "重置人員", "重置人員為預設值." });
			Aboutlist.add(new String[] { "小提醒", "重置人員將清除所有使用者的設定值." });
			break;
		default:
			break;
		}
	}

	@SuppressLint("InflateParams")
	private void setListViewAdapter(ArrayList<View> view, String t0, String t1) {
		View vi = LayoutInflater.from(this).inflate(R.layout.style_textview8, null);
		TextView tv_s0 = (TextView) vi.findViewById(R.id.tv_s0);
		TextView tv_s1 = (TextView) vi.findViewById(R.id.tv_s1);
		tv_s0.setText(t0);
		tv_s1.setText(t1);
		view.add(vi);
	}

	private void setView() {
		View.clear();
		for (int i = 0; i < Aboutlist.size(); i++) {
			setListViewAdapter(View, Aboutlist.get(i)[0], Aboutlist.get(i)[1]);
		}
		listview.setAdapter(new MyListAdapter(View));
	}

	public void onPageScrollStateChanged(int arg0) {

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	public void onPageSelected(int arg0) {

	}
}
