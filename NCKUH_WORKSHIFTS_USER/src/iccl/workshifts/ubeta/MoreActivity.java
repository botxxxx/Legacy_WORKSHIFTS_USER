package iccl.workshifts.ubeta;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

public class MoreActivity extends Activity
		implements OnClickListener, OnItemClickListener, OnItemLongClickListener, OnPageChangeListener {
	private boolean res = false;
	private int page = 0, w = 0, u = 0;
	private ViewPager pager;
	private TableRow m_down;
	private TextView w_title, u_title;
	private ImageView w_menu, u_menu, m_yes, m_no;
	private Handler mHandler = new Handler();
	private ArrayList<Boolean> cktworkClick = new ArrayList<Boolean>();
	private ArrayList<Boolean> cktuserClick = new ArrayList<Boolean>();
	private static ListView w_listview, u_listview;
	private static int item;
	private ArrayList<View> pagerView = new ArrayList<View>();
	private static ArrayList<View> workView = new ArrayList<View>();
	private static ArrayList<View> userView = new ArrayList<View>();
	private static ArrayList<View> cktworkView = new ArrayList<View>();
	private static ArrayList<View> cktuserView = new ArrayList<View>();
	public static boolean m_ckt = true, m_up = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_view);
		findViewById();
		addTab();
		view_0();
		view_1();
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
			if (res) {
				cktChange(false);
			} else {
				if (MainActivity.getlink()) {
					if (!MainActivity.getupdate() && m_up) {
						// The data can abandon, so don't update
						if (!MainActivity.getAutoDate()) {
							MainActivity.out("g1/" + MainActivity.getSQLdate());
						}
					}
				}
				MainActivity.main = true;
				finish();
			}
		}
		return false;
	}

	public void onClick(View v) {
		if (v == w_menu) {
			showDialog(true);
		}
		if (v == u_menu) {
			showDialog(false);
		}
		if (v == m_yes) {
			w = 0;
			u = 0;
			int mMonthDays = DateUtils.getMonthDays(MainActivity.getYearDate(), MainActivity.getMonthDate());
			cktChange(false);
			String ws = getsktSelect(0, cktworkClick);
			String us = getsktSelect(1, cktuserClick);
			// TODO auto (mMonthDays - (((w * mMonthDays) / u) + 1))
			if (w != 0 && u != 0) {
				String GS = mMonthDays + "" + u + "/" + (((w * mMonthDays) / u) + 1) + "/";
				MainActivity.out("g3/" + mMonthDays + MainActivity.getSQLdate() + '/' + ws + us);
				MainActivity.out("g8/" + MainActivity.getSQLdate() + GS);
				MainActivity.toast(this, "已更新.");
			} else {
				MainActivity.toast(this, "操作錯誤.");
			}
		}
		if (v == m_no) {
			cktChange(false);
		}
	}

	private int getWorkpp(int pp) {
		String s = MainActivity.Worklist.get(pp);
		String na = MainActivity.getLine(s, 0, '|');
		int p = Integer.parseInt(MainActivity.getLine(s, na.length() + 3, '|'));
		return p;
	}

	private String getsktSelect(int type, ArrayList<Boolean> blist) {
		String msg = "";
		for (int i = 0; i < blist.size(); i++) {
			if (blist.get(i)) {
				// 已勾選
				if (type == 0) {
					w += getWorkpp(i);
					msg += type + "" + i + "/|";
				} else {
					u++;
					msg += type + "" + i + '/' + "11111" + '|';
				}
			}
		}
		return msg;
	}

	private void showDialog(boolean w) {
		ArrayList<String> list = new ArrayList<String>();
		if (w) {
			list.add("開始約班");
			list.add("自動排班");
			list.add("清除班表");
			list.add("新增班別");
			list.add("重置班別");
			list.add("說明");
		} else {
			list.add("新增人員");
			list.add("重置人員");
			list.add("說明");
		}
		final boolean n = w;
		new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogTheme))
				.setItems(list.toArray(new String[list.size()]), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int view) {
						if (n) {
							switch (view) {
							case 0:
								reserMonth();
								break;
							case 1:
								autoMonth();
								break;
							case 2:
								cleanMonth();
								break;
							case 3:
								addWork();
								break;
							case 4:
								reWork();
								break;
							case 5:
								MainActivity.about = 1;
								startActivity(new Intent(MoreActivity.this, AboutActivity.class));
								break;
							}
						} else {
							switch (view) {
							case 0:
								addUser();
								break;
							case 1:
								reUser();
								break;
							case 2:
								MainActivity.about = 2;
								startActivity(new Intent(MoreActivity.this, AboutActivity.class));
								break;
							}
						}
					}
				}).show();
	}

	private void cktChange(boolean ckt) {
		if (ckt) {
			res = true;
			w_title.setText("選擇班別");
			u_title.setText("選擇人員");
			m_down.setVisibility(View.VISIBLE);
			setcktView();
		} else {
			res = false;
			w_title.setText("班表管理");
			u_title.setText("班表人員");
			m_down.setVisibility(View.GONE);
			w_listview.setAdapter(new MyListAdapter(workView));
			u_listview.setAdapter(new MyListAdapter(userView));
		}
	}

	private void reWork() {
		if (MainActivity.getlink()) {
			if (!MainActivity.getupdate()) {
				if (!MainActivity.getReser()) {
					new AlertDialog.Builder(this).setTitle("確定要重置所有班別?")
							.setPositiveButton("否", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
								}
							}).setNegativeButton("是", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									MainActivity.g7 = true;
									MainActivity.out("g7/0");
									MainActivity.toast(MoreActivity.this, "處理中.");
									MainActivity.toast(MoreActivity.this, "已更新.");
									reHandler.obtainMessage().sendToTarget();
								}
							}).show();
				} else {
					MainActivity.toast(this, "當月份有班表時無法重置.");
				}
			} else {
				MainActivity.toast(this, "請回上一層進行手動更新.");
			}
		} else {
			MainActivity.toast(this, "無法更新操作.");
		}
	}

	private void reUser() {
		if (MainActivity.getlink()) {
			if (!MainActivity.getupdate()) {
				if (!MainActivity.getReser()) {
					new AlertDialog.Builder(this).setTitle("確定要重置所有使用者?")
							.setPositiveButton("否", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
								}
							}).setNegativeButton("是", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									MainActivity.g7 = true;
									MainActivity.out("g7/1");
									MainActivity.toast(MoreActivity.this, "處理中.");
									MainActivity.toast(MoreActivity.this, "已更新.");
									reHandler.obtainMessage().sendToTarget();
								}
							}).show();
				} else {
					MainActivity.toast(this, "當月份有班表時無法重製.");
				}
			} else {
				MainActivity.toast(this, "請回上一層進行手動更新.");
			}
		} else {
			MainActivity.toast(this, "無法更新操作.");
		}
	}

	private void addWork() {
		if (MainActivity.getlink()) {
			startActivity(new Intent(this, ViewActivityWorkAdd.class));
		} else {
			MainActivity.toast(this, "無法更新操作.");
		}
	}

	private void addUser() {
		if (MainActivity.getlink()) {
			startActivity(new Intent(this, ViewActivityUserAdd.class));
		} else {
			MainActivity.toast(this, "無法更新操作.");
		}
	}

	private void autoMonth() {
		if (MainActivity.getlink()) {
			if (!MainActivity.getupdate()) {
				if (MainActivity.getReser()) {
					if (MainActivity.getAutoDate()) {
						new AlertDialog.Builder(this).setTitle("捨棄自動排班暫存?")
								.setPositiveButton("否", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
									}
								}).setNegativeButton("是", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
										MainActivity.g9 = false;
										MainActivity.out("g1/" + MainActivity.getSQLdate());
										startActivity(new Intent(MoreActivity.this, ViewActivityAuto.class));
									}
								}).show();
					} else {
						startActivity(new Intent(this, ViewActivityAuto.class));
					}
				} else {
					MainActivity.toast(this, "沒有資料可排班.");
				}
			} else {
				MainActivity.toast(this, "請回上一層進行手動更新.");
			}
		} else {
			MainActivity.toast(this, "無法更新操作.");
		}
	}

	private void reserMonth() {
		if (MainActivity.getlink()) {
			if (!MainActivity.getupdate()) {
				if (!MainActivity.getReser()) {
					cktChange(true);
				} else {
					MainActivity.toast(this, "只能在空白班表使用.");
				}
			} else {
				MainActivity.toast(this, "請回上一層進行手動更新.");
			}
		} else {
			MainActivity.toast(this, "無法更新操作.");
		}
	}

	private void cleanMonth() {
		if (MainActivity.getlink()) {
			if (!MainActivity.getupdate()) {
				if (MainActivity.getReser()) {
					new AlertDialog.Builder(this).setTitle("清除本月記錄?")
							.setPositiveButton("否", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
								}
							}).setNegativeButton("是", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									MainActivity.out("g2/" + MainActivity.getSQLdate());
									MainActivity.toast(MoreActivity.this, "已更新.");
								}
							}).show();
				} else {
					MainActivity.toast(this, "沒有資料.");
				}
			} else {
				MainActivity.toast(this, "請回上一層進行手動更新.");
			}
		} else {
			MainActivity.toast(this, "無法更新操作.");
		}
	}

	private void findViewById() {
		MainActivity.main = false;
		pager = (ViewPager) findViewById(R.id.m_pager);
		m_down = (TableRow) findViewById(R.id.m_down);
		m_yes = (ImageView) findViewById(R.id.m_yes);
		m_no = (ImageView) findViewById(R.id.m_no);
		m_yes.setOnClickListener(this);
		m_no.setOnClickListener(this);
	}

	@SuppressWarnings({ "static-access" })
	@SuppressLint("InflateParams")
	private void addTab() {
		pagerView.clear();
		LayoutInflater mInflater = getLayoutInflater().from(this);
		View v1 = mInflater.inflate(R.layout.work_list, null);
		View v2 = mInflater.inflate(R.layout.user_list, null);
		pagerView.add(v1);
		pagerView.add(v2);
		pager.setAdapter(new MyPagerAdapter(pagerView));
		pager.setCurrentItem(0);
		pager.addOnPageChangeListener(this);
	}

	private void view_0() {
		w_listview = (ListView) pagerView.get(0).findViewById(R.id.w_listview);
		w_menu = (ImageView) pagerView.get(0).findViewById(R.id.w_menu);
		w_title = (TextView) pagerView.get(0).findViewById(R.id.w_title);
		w_menu.setVisibility(View.INVISIBLE);
		w_menu.setOnClickListener(this);
		w_listview.setOnItemClickListener(this);
		// w_listview.setOnItemLongClickListener(this);
	}

	private void view_1() {
		u_listview = (ListView) pagerView.get(1).findViewById(R.id.u_listview);
		u_menu = (ImageView) pagerView.get(1).findViewById(R.id.u_menu);
		u_title = (TextView) pagerView.get(1).findViewById(R.id.u_title);
		u_menu.setVisibility(View.INVISIBLE);
		u_menu.setOnClickListener(this);
		u_listview.setOnItemClickListener(this);
		// u_listview.setOnItemLongClickListener(this);
	}

	@SuppressLint("InflateParams")
	private void setCheckedListViewAdapter(int f, ArrayList<View> view, ArrayList<String> list,
			ArrayList<Boolean> clicklist) {
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
			view.add(vi);
			clicklist.add(true);
		}
	}

	@SuppressLint("InflateParams")
	private static void setListViewAdapter(int f, ArrayList<View> view, ArrayList<String> list, Context con) {
		final int i = f;
		View vi = LayoutInflater.from(con).inflate(R.layout.style_textview, null);
		TextView textview_left = (TextView) vi.findViewById(R.id.textview_left);
		// 白班教學回診1|1|08|11| or A||
		if (!MainActivity.debug) {
			textview_left.setText(MainActivity.getLine(list.get(i), 0, '|'));
		} else {
			textview_left.setText(list.get(i));
		}
		view.add(vi);
	}

	public static void setView(Context con) {
		workView.clear();
		for (int i = 0; i < MainActivity.Worklist.size(); i++) {
			setListViewAdapter(i, workView, MainActivity.Worklist, con);
		}
		w_listview.setAdapter(new MyListAdapter(workView));

		userView.clear();
		for (int i = 0; i < MainActivity.Userlist.size(); i++) {
			setListViewAdapter(i, userView, MainActivity.Userlist, con);
		}
		u_listview.setAdapter(new MyListAdapter(userView));
	}

	private void setcktView() {
		cktworkView.clear();
		cktworkClick.clear();
		for (int i = 0; i < MainActivity.Worklist.size(); i++) {
			setCheckedListViewAdapter(i, cktworkView, MainActivity.Worklist, cktworkClick);
		}
		w_listview.setAdapter(new MyListAdapter(cktworkView));

		cktuserView.clear();
		cktuserClick.clear();
		for (int i = 0; i < MainActivity.Userlist.size(); i++) {
			setCheckedListViewAdapter(i, cktuserView, MainActivity.Userlist, cktuserClick);
		}
		u_listview.setAdapter(new MyListAdapter(cktuserView));
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (res) {
			CheckedTextView ckt = (CheckedTextView) parent.getAdapter().getView(position, null, null)
					.findViewById(R.id.ckt_left);
			ckt.setChecked(!ckt.isChecked());
			if (m_ckt) {
				cktworkClick.set(position, ckt.isChecked());
				// MainActivity.toast(this, "w_listview");
			} else {
				cktuserClick.set(position, ckt.isChecked());
				// MainActivity.toast(this, "u_listview");
			}
		} else {
			item = position;
			if (page == 0) {
				startActivity(new Intent(this, ViewActivityWorkMore.class));
			} else {
				startActivity(new Intent(this, ViewActivityUserMore.class));
			}
		}
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		if (!res) {
			if (MainActivity.getlink()) {
				if (!MainActivity.getupdate()) {
					if (!MainActivity.getReser()) {
						String dmsg = "";
						if (page == 0) {
							dmsg = MainActivity.getLine(MainActivity.Worklist.get(position), 0, '|');
						} else {
							dmsg = MainActivity.getLine(MainActivity.Userlist.get(position), 0, '|');
						}
						final String fdmsg = (page + "") + dmsg;
						new AlertDialog.Builder(this).setTitle("是否移除「" + dmsg + "」?")
								.setPositiveButton("否", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
									}
								}).setNegativeButton("是", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0, int arg1) {
										MainActivity.out("g12/" + fdmsg);
										MainActivity.toast(MoreActivity.this, "已更新.");
										reHandler.obtainMessage().sendToTarget();
									}
								}).show();
					} else {
						MainActivity.toast(this, "當月份有班表時無法移除.");
					}
				} else {
					MainActivity.toast(this, "請回上一層進行手動更新.");
				}
			} else {
				MainActivity.toast(this, "無法更新操作.");
			}
		}
		return true;
	}

	public void onPageScrollStateChanged(int arg0) {

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	public void onPageSelected(int arg0) {
		page = arg0;
	}

	public static int getItem() {
		return item;
	}

	@SuppressLint("HandlerLeak")
	private Handler reHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mHandler.removeCallbacks(reSMS);
			mHandler.postDelayed(reSMS, 1000);
		};
	};

	private Runnable reSMS = new Runnable() {
		public void run() {
			setView(MoreActivity.this);
			MainActivity.day_upload(MoreActivity.this);
		}
	};
}
