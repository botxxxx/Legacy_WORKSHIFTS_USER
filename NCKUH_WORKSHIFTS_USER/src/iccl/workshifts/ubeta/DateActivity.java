package iccl.workshifts.ubeta;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;

public class DateActivity extends Activity implements OnClickListener, OnItemClickListener,OnPageChangeListener {
	@SuppressWarnings("unused")
	private int w = 0, u = 0, page = 0;
	private ViewPager pager;
	private ImageView yes, no;
	private ArrayList<Boolean> cktworkClick = new ArrayList<Boolean>();
	private ArrayList<Boolean> cktuserClick = new ArrayList<Boolean>();
	private ArrayList<CheckedTextView> workClick = new ArrayList<CheckedTextView>();
	private ArrayList<CheckedTextView> userClick = new ArrayList<CheckedTextView>();
	private static ListView w_listview, u_listview;
	public static ArrayList<View> pagerView = new ArrayList<View>();
	public static ArrayList<View> cktworkView = new ArrayList<View>();
	public static ArrayList<View> cktuserView = new ArrayList<View>();
	public static boolean d_ckt = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_view);
		findViewById();
		addTab();
		view_0();
		view_1();
		setBoolean();
		setcktView();
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
			if (MainActivity.getlink()) {
				if (!MainActivity.getupdate()) {
					if (!MainActivity.getAutoDate()) {
						MainActivity.out("g1/" + MainActivity.getSQLdate());
					}
				}
			}
			MainActivity.main = true;
			finish();

		}
		return false;
	}

	public void onClick(View v) {
		if (v == yes) {
			w = 0;
			u = 0;
			String ws = getsktSelect(0, cktworkClick, workClick);
			String us = getsktSelect(1, cktuserClick, userClick);
			if (w != 0 || u != 0) {
				String mmsg = MainActivity.Datelist.get(MainActivity.getDayDate());
				MainActivity.out("g13/" + MainActivity.getSQLdate() + MainActivity.getDayDate() + '/' + mmsg + ws + us);
				MainActivity.toast(this, "已更新.");
			}
			finish();
		}
		if (v == no) {
			finish();
		}
	}

	private String getsktSelect(int type, ArrayList<Boolean> blist, ArrayList<CheckedTextView> cc) {
		String msg = "";
		for (int i = 0; i < blist.size(); i++) {
			if (cc.get(i).isEnabled() && blist.get(i)) {
				if (type == 0) {
					w++;
					msg += type + "" + i + "/|";
				} else {
					u++;
					msg += type + "" + i + '/' + "11111" + '|';
				}
			}
		}
		return msg;
	}

	private void findViewById() {
		MainActivity.main = false;
		pager = (ViewPager) findViewById(R.id.d_pager);
		yes = (ImageView) findViewById(R.id.d_yes);
		no = (ImageView) findViewById(R.id.d_no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
	}

	@SuppressWarnings({ "static-access" })
	@SuppressLint("InflateParams")
	private void addTab() {
		pagerView.clear();
		LayoutInflater mInflater = getLayoutInflater().from(this);
		View v1 = mInflater.inflate(R.layout.date_view_0, null);
		View v2 = mInflater.inflate(R.layout.date_view_1, null);
		pagerView.add(v1);
		pagerView.add(v2);
		pager.setAdapter(new MyPagerAdapter(pagerView));
		if (MainActivity.g13) {
			pager.setCurrentItem(0);
		} else {
			pager.setCurrentItem(1);
		}
		pager.addOnPageChangeListener(this);
	}

	private void view_0() {
		w_listview = (ListView) pagerView.get(0).findViewById(R.id.d_listview);
		w_listview.setOnItemClickListener(this);
	}

	private void view_1() {
		u_listview = (ListView) pagerView.get(1).findViewById(R.id.d_listview);
		u_listview.setOnItemClickListener(this);
	}

	@SuppressLint("InflateParams")
	private void setCheckedListViewAdapter(int f, ArrayList<View> view, ArrayList<String> list,
			ArrayList<CheckedTextView> cc, boolean click) {
		final int i = f;
		View vi = LayoutInflater.from(this).inflate(R.layout.style_checked, null);
		CheckedTextView ckt_left = (CheckedTextView) vi.findViewById(R.id.ckt_left);
		// 白班教學回診1|1|08|11| or A||
		String s = MainActivity.getLine(list.get(i), 0, '|');
		if (s.length() != 0) {
			if (s.length() > 7) {
				s = s.substring(0, 7) + "\n" + s.substring(7);
			}
			ckt_left.setText(s);
			ckt_left.setChecked(click);
			ckt_left.setEnabled(!click);
			cc.add(ckt_left);
			view.add(vi);
		}
	}

	private void setBoolean() {
		cktworkClick.clear();
		for (int i = 0; i < MainActivity.Worklist.size(); i++) {
			cktworkClick.add(false);
		}
		cktuserClick.clear();
		for (int i = 0; i < MainActivity.Userlist.size(); i++) {
			cktuserClick.add(false);
		}
		for (int i = 0; i < MainActivity.Daylist.size(); i++) {
			String msg = MainActivity.Daylist.get(i);
			String types = msg.substring(0, 1);
			int p = Integer.parseInt(MainActivity.getLine(msg, 1, '/'));
			switch (types) {
			case "0":
				if (p < MainActivity.Worklist.size()) {
					cktworkClick.set(p, true);
				}
				break;
			case "1":
				if (p < MainActivity.Userlist.size()) {
					cktuserClick.set(p, true);
				}
				break;
			case "2":
				break;
			case "3":
				break;
			}
		}
	}

	private void setcktView() {
		cktworkView.clear();
		for (int i = 0; i < MainActivity.Worklist.size(); i++) {
			setCheckedListViewAdapter(i, cktworkView, MainActivity.Worklist, workClick, cktworkClick.get(i));
		}
		w_listview.setAdapter(new MyListAdapter(cktworkView));

		cktuserView.clear();
		for (int i = 0; i < MainActivity.Userlist.size(); i++) {
			setCheckedListViewAdapter(i, cktuserView, MainActivity.Userlist, userClick, cktuserClick.get(i));
		}
		u_listview.setAdapter(new MyListAdapter(cktuserView));
	}

	public void onPageScrollStateChanged(int arg0) {

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	public void onPageSelected(int arg0) {
		page = arg0;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CheckedTextView ckt = (CheckedTextView) parent.getAdapter().getView(position, null, null)
				.findViewById(R.id.ckt_left);
		if (ckt.isEnabled()) {
			ckt.setChecked(!ckt.isChecked());
			if (d_ckt) {
				cktworkClick.set(position, ckt.isChecked());
				// MainActivity.toast(this, "w_listview");
			} else {
				cktuserClick.set(position, ckt.isChecked());
				// MainActivity.toast(this, "u_listview");
			}
		}
	}
}
