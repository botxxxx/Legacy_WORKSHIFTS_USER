package iccl.workshifts.ubeta;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ViewActivityAuto extends Activity implements OnClickListener, OnItemSelectedListener {
	private ArrayList<View> UserworkView = new ArrayList<View>();
	private ArrayList<View> pageView = new ArrayList<View>();
	private ArrayList<EditText> ed = new ArrayList<EditText>();
	private ArrayList<String> J = new ArrayList<String>();
	private ListView list;
	private ViewPager page;
	private Spinner spp, ac, md;
	private TextView day, work, user;
	private EditText max;
	private TableRow tac;
	private ImageView yes, no;
	private int spp_mode = 0, ac_mode = 0, md_mode = 0, umax = 0;
	private int rule_0 = 0, rule_1 = 0, rule_2 = 0, rule_3 = 0, rule_4 = 0;
	private int fdy = DateUtils.getMonthDays(MainActivity.Years, MainActivity.Months);
	private boolean debug = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_view);
		findViewById();
		addTab();
		view_0();
		view_1();
		load();
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
			finish();
		}
		return false;
	}

	public void onClick(View v) {
		if (v == yes) {
			if (!"".equals(max.getText().toString())) {
				umax = Integer.parseInt(max.getText().toString());
				String ds = MainActivity.Datelist.get(0);
				String ms = MainActivity.getLine(ds, 0, '/') + '/' + umax + '/';
				MainActivity.Datelist.set(0, ms);
				save();
			} else {
				max.setText("0");
			}
		}
		if (v == no) {
			finish();
		}
	}

	private void findViewById() {
		page = (ViewPager) findViewById(R.id.auto_page);
		yes = (ImageView) findViewById(R.id.auto_yes);
		no = (ImageView) findViewById(R.id.auto_no);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("static-access")
	private void addTab() {
		pageView.clear();
		LayoutInflater mInflater = getLayoutInflater().from(this);
		View v1 = mInflater.inflate(R.layout.auto_view_0, null);
		View v2 = mInflater.inflate(R.layout.auto_view_1, null);
		pageView.add(v1);
		pageView.add(v2);
		page.setAdapter(new MyPagerAdapter(pageView));
		page.setCurrentItem(0);
	}

	private void view_0() {
		day = (TextView) pageView.get(0).findViewById(R.id.auto_day);
		work = (TextView) pageView.get(0).findViewById(R.id.auto_work);
		user = (TextView) pageView.get(0).findViewById(R.id.auto_user);
		max = (EditText) pageView.get(0).findViewById(R.id.auto_max);
		spp = (Spinner) pageView.get(0).findViewById(R.id.auto_spp);
		tac = (TableRow) pageView.get(0).findViewById(R.id.auto_tac);
		ac = (Spinner) pageView.get(0).findViewById(R.id.auto_ac);
		md = (Spinner) pageView.get(0).findViewById(R.id.auto_mode);
	}

	private void view_1() {
		list = (ListView) pageView.get(1).findViewById(R.id.auto_list);
		list.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		list.setItemsCanFocus(true);
	}

	private void save() {
		int fx = umax / 3, r0 = 0, r1 = 0, r2 = 0, r3 = 0, r4 = 0;
		ArrayList<String> autoWrite = new ArrayList<String>(); // 白班暫存
		ArrayList<String> autoMonth = new ArrayList<String>(); // 月排班暫存
		ArrayList<Integer> workMonth = new ArrayList<Integer>(); // 月排班累計
		ArrayList<Integer> workDay = new ArrayList<Integer>(); // 連續上班數
		ArrayList<Integer> yesterday = new ArrayList<Integer>(); // 昨日班次

		for (int i = 0; i < ed.size(); i++) {
			int last = 0;
			String msg = ed.get(i).getText().toString();
			if (msg.length() != 0 && msg != null) {
				last = Integer.parseInt(msg);
			}
			workDay.add(last);
			workMonth.add(0);
			yesterday.add(99);
		}
		for (int k = 1; k <= fdy; k++) {
			// TODO 1~31
			ArrayList<Boolean> join = new ArrayList<Boolean>(); // 已排
			ArrayList<String> autoDay = new ArrayList<String>(); // 日排班暫存
			ArrayList<String> User = new ArrayList<String>(); // 人
			ArrayList<String> ARule = new ArrayList<String>(); // 白
			ArrayList<String> A = new ArrayList<String>(); // 白班 08~17
			ArrayList<String> B = new ArrayList<String>(); // 白觀 08~20
			ArrayList<String> C = new ArrayList<String>(); // 小夜 17~02
			ArrayList<String> D = new ArrayList<String>(); // 夜觀 20~08
			ArrayList<String> E = new ArrayList<String>(); // 大夜 02~08
			ArrayList<String> F = new ArrayList<String>(); // 有事
			ArrayList<String> P = new ArrayList<String>(); // Random

			String mmsg = MainActivity.Datelist.get(k);
			int run = 0;
			char key = '|';
			while (mmsg.indexOf(key, run) != -1) {
				String tmp = MainActivity.getLine(mmsg, run, key);
				run += tmp.length() + 1;
				if (tmp.substring(0, 1).equals("0")) {
					String wp = MainActivity.getLine(tmp, 1, '/');
					int wps = Integer.parseInt(wp);
					if (wps < MainActivity.Worklist.size()) {
						String rss = MainActivity.Worklist.get(wps);
						int r = 0;
						String na = MainActivity.getLine(rss, r, '|');
						r += na.length() + 1;
						String tp = MainActivity.getLine(rss, r, '|');
						if ("0".equals(tp)) {
							ARule.add(tmp);
						}
					}
				}
				if (tmp.substring(0, 1).equals("1")) {
					User.add(tmp);
				}
			}
			if (ARule.size() != 0) {
				ArrayList<String> ar = new ArrayList<String>();
				for (int ri = r0; ri < ARule.size(); ri++) {
					ar.add(ARule.get(ri));
				}
				for (int rj = 0; rj < r0; rj++) {
					ar.add(ARule.get(rj));
				}
				ARule.clear();
				r0++;
				if (r0 == ar.size()) {
					r0 = 0;
				}
				for (int rk = 0; rk < ar.size(); rk++) {
					ARule.add(ar.get(rk));
				}
			}
			for (int jo = 0; jo < ed.size(); jo++) {
				join.add(false);
			}
			for (int pp = 0; pp < MainActivity.Userlist.size(); pp++) {
				P.add("");
			}
			for (int u = 0; u < User.size(); u++) {
				String usb = User.get(u);
				String upb = MainActivity.getLine(usb, 1, '/'); // user.get(p);
				int ups = Integer.parseInt(upb);
				int mps = MainActivity.Userlist.size();
				if (ups < mps) {
					String nab = MainActivity.getLine(MainActivity.Userlist.get(ups), 0, '|');
					if (md_mode == 0) {
						NameToRandomPath(nab, usb, P);
					} else {
						P.set(RegularPath(ups, ac_mode, mps), usb);
					}
				}
			}

			// 每天的人
			for (int upp = 0; upp < P.size(); upp++) {
				String us = P.get(upp);
				if (us.length() > 0) {
					// MainActivity.out("g9/upp.. " + us);
					String up = MainActivity.getLine(us, 1, '/'); // user.get(p);
					String uss = MainActivity.Userlist.get(Integer.parseInt(up));
					String uc = us.substring(2 + up.length());
					String na = MainActivity.getLine(uss, 0, '|');
					if (uc.length() > 0) {
						// 白班1 白觀1 小夜1 夜觀1 大夜1
						if (uc.equals("00000")) {
							F.add(na);
						} else {
							for (int ui = 0; ui < uc.length(); ui++) {
								int si = Integer.parseInt(uc.substring(ui, ui + 1));
								if (si == 1) {
									switch (ui) {
									case 0:
										A.add(na);
										break;
									case 1:
										B.add(na);
										break;
									case 2:
										C.add(na);
										break;
									case 3:
										D.add(na);
										break;
									case 4:
										E.add(na);
										break;
									}
								}
							}
						}
					} else {
						MainActivity.toast(this, na + "發生排班錯誤!");
					}
				}
			}
			// 每天的班
			for (int w = 0; w < ARule.size(); w++) {
				String ws = ARule.get(w), na, tp, pp, st, et, mx;
				String wp = MainActivity.getLine(ws, 1, '/'); // work.get(p);
				int wps = Integer.parseInt(wp);
				if (wps < MainActivity.Worklist.size()) {
					String wss = MainActivity.Worklist.get(wps); // work.get(p).toString;
					String wc = ws.substring(2 + wp.length()); // other
					// 白班教學回診1|1|08|11|
					int r = 0;
					na = MainActivity.getLine(wss, r, '|');
					r += na.length() + 1;
					tp = MainActivity.getLine(wss, r, '|');
					r += 2;
					pp = MainActivity.getLine(wss, r, '|');
					r += pp.length() + 1;
					st = MainActivity.getLine(wss, r, '|');
					r += st.length() + 1;
					et = MainActivity.getLine(wss, r, '|');
					r += et.length() + 1;
					mx = MainActivity.getLine(wss, r, '|');
					if (wc.length() > 0 && wc != null) {
						ArrayList<String> list = new ArrayList<String>();
						int wr = 0;
						char wk = '/';
						while (wc.indexOf(wk, wr) != -1) {
							String ctmp = MainActivity.getLine(wc, wr, wk);
							list.add(ctmp); // A||
							wr += ctmp.length() + 1;
						}
						for (int ci = 0; ci < list.size(); ci++) {
							String tmp = list.get(ci);
							String type = tmp.substring(0, 1);
							String msg = tmp.substring(1);
							switch (type) {
							case "0": // 名稱
								na = msg;
								break;
							case "1": // 人數
								pp = msg;
								break;
							case "2": // 開始
								st = msg;
								break;
							case "3": // 結束
								et = msg;
								break;
							default:
								break;
							}
						}
					}
					// 什麼班
					int ip = Integer.parseInt(tp);
					ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
					if (ip == 0) {
						list.add(A);
						list.add(B);
						list.add(C);
						list.add(D);
						list.add(E);
					} else if (ip == 1) {
						list.add(B);
						list.add(A);
						list.add(C);
						list.add(D);
						list.add(E);
					} else if (ip == 2) {
						list.add(C);
						list.add(D);
						list.add(E);
						list.add(A);
						list.add(B);
					} else if (ip == 3) {
						list.add(D);
						list.add(E);
						list.add(C);
						list.add(A);
						list.add(B);
					} else {
						list.add(E);
						list.add(D);
						list.add(B);
						list.add(A);
						list.add(C);
					}
					ArrayList<String> autoRule = new ArrayList<String>();
					// 排班順序
					for (int au = 0; au < list.size(); au++) {
						for (int aug = 0; aug < list.get(au).size(); aug++) {
							String sms = list.get(au).get(aug);
							if (addUs(sms, autoRule)) {
								autoRule.add(sms);
								// MainActivity.out("g9/auto.."+autoUser.get(index));
							}
						}
					}

					int ipp = Integer.parseInt(pp); // 人數
					int imx = Integer.parseInt(mx); // 連班上限
					for (int mp = 0; mp < ipp; mp++) {
						boolean adduser = false;
						ArrayList<String> ruleG1 = new ArrayList<String>(); // 候補一
						for (int wau = 0; wau < autoRule.size(); wau++) {
							int mmx = NameToMax(autoRule.get(wau));
							if (mmx == 0) {
								mmx = 99;
							}
							// TODO rule_0
							int mwd = workMonth.get(NameToPath(autoRule.get(wau)));
							if (mwd >= rule_0 || mwd >= mmx) {
								// 加入第二階段候補名單
								ruleG1.add(autoRule.get(wau));
							} else {
								boolean inside = false;
								boolean overtime = false;
								boolean fatigue = false;

								// 當日未排
								for (int adt = 0; adt < autoDay.size(); adt++) {
									String adts = MainActivity.getLine(autoDay.get(adt), 1, '/');
									if (debug) {
										if (adts.equals(autoRule.get(wau))) {
											inside = true;
										}
									} else {
										if (PathToName(Integer.parseInt(adts)).equals(autoRule.get(wau))) {
											inside = true;
										}
									}
								}

								if (!inside) {
									// 連續上班檢查
									// umx 使用者已連班上限
									// imx 工作指定連班上限
									int umx = workDay.get(NameToPath(autoRule.get(wau)));
									if (umx >= imx) {
										overtime = true;
									}
								}

								if (!inside && !overtime) {
									// 昨日上班檢查
									int up = yesterday.get(NameToPath(autoRule.get(wau)));
									if (up != 99) {
										if (ip != up) {
											fatigue = true;
										}
									}
								}
								if (!inside && !overtime && !fatigue) {// 連續
									adduser = true;
									int wmi = workMonth.get(NameToPath(autoRule.get(wau))) + 1;
									int wdi = workDay.get(NameToPath(autoRule.get(wau))) + 1;
									join.set(NameToPath(autoRule.get(wau)), true); // 有上班
									workMonth.set(NameToPath(autoRule.get(wau)), wmi); // 月班數+1
									workDay.set(NameToPath(autoRule.get(wau)), wdi); // 連續上班+1
									yesterday.set(NameToPath(autoRule.get(wau)), ip); // 昨日班次
									if (debug) {
										autoDay.add("2" + autoRule.get(wau) + '/' + ws.substring(1) + '|'); // 今日
									} else {
										autoDay.add("2" + NameToPath(autoRule.get(wau)) + '/' + ws.substring(1) + '|'); // 今日
									}
									break;
								}
							}
						}
						if (!adduser) {
							for (int wau = 0; wau < ruleG1.size(); wau++) {
								int mxx = NameToMax(ruleG1.get(wau));
								if (mxx == 0) {
									mxx = 99;
								}
								// TODO (rule_0 + 1)
								int mwd = workMonth.get(NameToPath(ruleG1.get(wau)));
								if (!(mwd >= (rule_0 + 1) || mwd >= mxx)) {
									boolean inside = false;
									boolean overtime = false;
									boolean fatigue = false;
									// 當日未排
									for (int adt = 0; adt < autoDay.size(); adt++) {
										String adts = MainActivity.getLine(autoDay.get(adt), 1, '/');
										if (debug) {
											if (adts.equals(ruleG1.get(wau))) {
												inside = true;
											}
										} else {
											if (PathToName(Integer.parseInt(adts)).equals(ruleG1.get(wau))) {
												inside = true;
											}
										}
									}

									if (!inside) {
										// 連續上班檢查
										// umx 使用者已連班上限
										// imx 工作指定連班上限
										int umx = workDay.get(NameToPath(ruleG1.get(wau)));
										if (umx >= imx) {
											overtime = true;
										}
									}

									if (!inside && !overtime) {
										// 昨日上班檢查
										int up = yesterday.get(NameToPath(ruleG1.get(wau)));
										if (up != 99) {
											if (ip != up) {
												fatigue = true;
											}
										}
									}

									if (!inside && !overtime && !fatigue) {// 連續
										adduser = true;
										int wmi = workMonth.get(NameToPath(ruleG1.get(wau))) + 1;
										int wdi = workDay.get(NameToPath(ruleG1.get(wau))) + 1;
										join.set(NameToPath(ruleG1.get(wau)), true); // 有上班
										workMonth.set(NameToPath(ruleG1.get(wau)), wmi); // 月班數+1
										workDay.set(NameToPath(ruleG1.get(wau)), wdi); // 連續上班+1
										yesterday.set(NameToPath(ruleG1.get(wau)), ip); // 昨日班次
										if (debug) {
											autoDay.add("2" + ruleG1.get(wau) + '/' + ws.substring(1) + '|'); // 今日
										} else {
											autoDay.add(
													"2" + NameToPath(ruleG1.get(wau)) + '/' + ws.substring(1) + '|'); // 今日
										}
										break;
									}
								}
							}
						}
					}
				}
			}

			String sadm = "";
			for (int adm = 0; adm < autoDay.size(); adm++) {
				sadm += autoDay.get(adm);
			}
			autoWrite.add(sadm);
		}
		for (int id = 0; id < ed.size(); id++) {
			int last = 0;
			String msg = ed.get(id).getText().toString();
			if (msg.length() != 0 && msg != null) {
				last = Integer.parseInt(msg);
			}
			workDay.set(id, last);
			yesterday.set(id, 99);
		}
		for (int j = 1; j <= fdy; j++) {
			// TODO 1~31
			// msg += 0 + "" + i + "/0白/12/26/319/4|"; 白班,白關,小夜,夜觀,大夜
			// msg += 1 + "" + i + "/11111|";
			ArrayList<Boolean> join = new ArrayList<Boolean>(); // 已排
			ArrayList<String> autoDay = new ArrayList<String>(); // 日排班暫存
			ArrayList<String> User = new ArrayList<String>(); // 人
			ArrayList<String> Work = new ArrayList<String>(); // 班
			ArrayList<String> ARule = new ArrayList<String>(); //
			ArrayList<String> FRule = new ArrayList<String>(); //
			ArrayList<String> BRule = new ArrayList<String>(); // 白觀
			ArrayList<String> CRule = new ArrayList<String>(); // 小夜
			ArrayList<String> DRule = new ArrayList<String>(); // 夜觀
			ArrayList<String> ERule = new ArrayList<String>(); // 大夜
			ArrayList<String> A = new ArrayList<String>(); // 白班 08~17
			ArrayList<String> B = new ArrayList<String>(); // 白觀 08~20
			ArrayList<String> C = new ArrayList<String>(); // 小夜 17~02
			ArrayList<String> D = new ArrayList<String>(); // 夜觀 20~08
			ArrayList<String> E = new ArrayList<String>(); // 大夜 02~08
			ArrayList<String> F = new ArrayList<String>(); // 有事
			ArrayList<String> I = new ArrayList<String>(); // User轉存
			ArrayList<String> P = new ArrayList<String>(); // Random

			String mmsg = MainActivity.Datelist.get(j);
			int run = 0;
			char key = '|';
			while (mmsg.indexOf(key, run) != -1) {
				String tmp = MainActivity.getLine(mmsg, run, key);
				run += tmp.length() + 1;
				if (tmp.substring(0, 1).equals("0")) {
					String wp = MainActivity.getLine(tmp, 1, '/');
					int wps = Integer.parseInt(wp);
					if (wps < MainActivity.Worklist.size()) {
						String rss = MainActivity.Worklist.get(wps);
						int r = 0;
						String na = MainActivity.getLine(rss, r, '|');
						r += na.length() + 1;
						String tp = MainActivity.getLine(rss, r, '|');
						if ("1".equals(tp)) {
							BRule.add(tmp);
						}
						if ("2".equals(tp)) {
							CRule.add(tmp);
						}
						if ("3".equals(tp)) {
							DRule.add(tmp);
						}
						if ("4".equals(tp)) {
							ERule.add(tmp);
						}
					}
				}
				if (tmp.substring(0, 1).equals("1")) {
					User.add(tmp);
				}
			}
			String awsg = autoWrite.get(j - 1), ssl = "";
			if (j < autoWrite.size()) {
				ssl = autoWrite.get(j);
			}
			run = 0;
			while (awsg.indexOf(key, run) != -1) {
				String tmp = MainActivity.getLine(awsg, run, key);
				run += tmp.length() + 1;
				ARule.add(tmp + key);
			}
			run = 0;
			while (ssl.indexOf(key, run) != -1) {
				String tmpa = MainActivity.getLine(ssl, run, key);
				run += tmpa.length() + 1;
				FRule.add(tmpa + key);
			}

			// ----------------
			if (BRule.size() != 0) {
				ArrayList<String> br = new ArrayList<String>();
				for (int ri = r1; ri < BRule.size(); ri++) {
					br.add(BRule.get(ri));
				}
				for (int rj = 0; rj < r1; rj++) {
					br.add(BRule.get(rj));
				}
				r1++;
				if (r1 == br.size()) {
					r1 = 0;
				}
				for (int rk = 0; rk < br.size(); rk++) {
					Work.add(br.get(rk));
				}
			}
			// ----------------
			if (CRule.size() != 0) {
				ArrayList<String> cr = new ArrayList<String>();
				for (int ri = r2; ri < CRule.size(); ri++) {
					cr.add(CRule.get(ri));
				}
				for (int rj = 0; rj < r2; rj++) {
					cr.add(CRule.get(rj));
				}
				r2++;
				if (r2 == CRule.size()) {
					r2 = 0;
				}
				for (int rk = 0; rk < CRule.size(); rk++) {
					Work.add(cr.get(rk));
				}
			}
			// ----------------
			if (DRule.size() != 0) {
				ArrayList<String> dr = new ArrayList<String>();
				for (int ri = r3; ri < DRule.size(); ri++) {
					dr.add(DRule.get(ri));
				}
				for (int rj = 0; rj < r3; rj++) {
					dr.add(DRule.get(rj));
				}
				r3++;
				if (r3 == DRule.size()) {
					r3 = 0;
				}
				for (int rk = 0; rk < DRule.size(); rk++) {
					Work.add(dr.get(rk));
				}
			}
			// ----------------
			if (ERule.size() != 0) {
				ArrayList<String> er = new ArrayList<String>();
				for (int ri = r4; ri < ERule.size(); ri++) {
					er.add(ERule.get(ri));
				}
				for (int rj = 0; rj < r4; rj++) {
					er.add(ERule.get(rj));
				}
				r4++;
				if (r4 == ERule.size()) {
					r4 = 0;
				}

				for (int rk = 0; rk < ERule.size(); rk++) {
					Work.add(er.get(rk));
				}
			}
			// ----------------
			for (int jo = 0; jo < ed.size(); jo++) {
				join.add(false);
			}

			if (Work.size() != 0 && User.size() != 0) {
				// 每天的人
				for (int pp = 0; pp < MainActivity.Userlist.size(); pp++) {
					P.add("");
				}
				for (int u = 0; u < User.size(); u++) {
					String usb = User.get(u);
					String upb = MainActivity.getLine(usb, 1, '/'); // user.get(p);
					int ups = Integer.parseInt(upb);
					int mps = MainActivity.Userlist.size();
					if (ups < mps) {
						String nab = MainActivity.getLine(MainActivity.Userlist.get(ups), 0, '|');
						if (md_mode == 0) {
							NameToRandomPath(nab, usb, P);
						} else {
							P.set(RegularPath(ups, ac_mode, mps), usb);
						}
					}
				}
				for (int upp = 0; upp < P.size(); upp++) {
					// 順序
					String us = P.get(upp);
					if (us.length() > 0) {
						// MainActivity.out("g9/upp.. " + us);
						String up = MainActivity.getLine(us, 1, '/'); // user.get(p);
						String uss = MainActivity.Userlist.get(Integer.parseInt(up));
						String uc = us.substring(2 + up.length());
						String na = MainActivity.getLine(uss, 0, '|');
						if (uc.length() > 0) {
							// 白班1 白觀1 小夜1 夜觀1 大夜1
							if (uc.equals("00000")) {
								F.add(na);
							} else {
								for (int ui = 0; ui < uc.length(); ui++) {
									int si = Integer.parseInt(uc.substring(ui, ui + 1));
									if (si == 1) {
										switch (ui) {
										case 0:
											A.add(na);
											break;
										case 1:
											B.add(na);
											break;
										case 2:
											C.add(na);
											break;
										case 3:
											D.add(na);
											break;
										case 4:
											E.add(na);
											break;
										}
									}
								}
							}
						} else {
							MainActivity.toast(this, na + "發生排班錯誤!");
						}
					}
				}

				for (int ape = 0; ape < ARule.size(); ape++) {
					autoDay.add(ARule.get(ape));
					String shap = MainActivity.getLine(ARule.get(ape), 1, '/');
					int wadi = workDay.get(NameToPath(shap)) + 1;
					join.set(NameToPath(shap), true); // 有上班
					workDay.set(NameToPath(shap), wadi); // 連續上班+1
					yesterday.set(NameToPath(shap), 0); // 昨日班次
				}

				for (int afe = 0; afe < FRule.size(); afe++) {
					String sfap = MainActivity.getLine(FRule.get(afe), 1, '/');
					int wdi = workDay.get(NameToPath(sfap)) + 1;
					workDay.set(NameToPath(sfap), wdi); // 連續上班+1
				}

				// 每天的班
				for (int w = 0; w < Work.size(); w++) {
					String ws = Work.get(w), na, tp, pp, st, et, mx;
					String wp = MainActivity.getLine(ws, 1, '/'); // work.get(p);
					int wps = Integer.parseInt(wp);
					if (wps < MainActivity.Worklist.size()) {
						String wss = MainActivity.Worklist.get(wps); // work.get(p).toString;
						String wc = ws.substring(2 + wp.length()); // other
						// 白班教學回診1|1|08|11|
						int r = 0;
						na = MainActivity.getLine(wss, r, '|');
						r += na.length() + 1;
						tp = MainActivity.getLine(wss, r, '|');
						r += 2;
						pp = MainActivity.getLine(wss, r, '|');
						r += pp.length() + 1;
						st = MainActivity.getLine(wss, r, '|');
						r += st.length() + 1;
						et = MainActivity.getLine(wss, r, '|');
						r += et.length() + 1;
						mx = MainActivity.getLine(wss, r, '|');
						if (wc.length() > 0 && wc != null) {
							ArrayList<String> list = new ArrayList<String>();
							int wr = 0;
							char wk = '/';
							while (wc.indexOf(wk, wr) != -1) {
								String ctmp = MainActivity.getLine(wc, wr, wk);
								list.add(ctmp); // A||
								wr += ctmp.length() + 1;
							}
							for (int ci = 0; ci < list.size(); ci++) {
								String tmp = list.get(ci);
								String type = tmp.substring(0, 1);
								String msg = tmp.substring(1);
								switch (type) {
								case "0": // 名稱
									na = msg;
									break;
								case "1": // 人數
									pp = msg;
									break;
								case "2": // 開始
									st = msg;
									break;
								case "3": // 結束
									et = msg;
									break;
								default:
									break;
								}
							}
						}
						// 什麼班
						int ip = Integer.parseInt(tp);
						ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
						if (ip == 0) {
							list.add(A);
							list.add(B);
							list.add(C);
							list.add(D);
							list.add(E);
						} else if (ip == 1) {
							list.add(B);
							list.add(A);
							list.add(C);
							list.add(D);
							list.add(E);
						} else if (ip == 2) {
							list.add(C);
							list.add(D);
							list.add(E);
							list.add(A);
							list.add(B);
						} else if (ip == 3) {
							list.add(D);
							list.add(E);
							list.add(C);
							list.add(A);
							list.add(B);
						} else {
							list.add(E);
							list.add(D);
							list.add(B);
							list.add(A);
							list.add(C);
						}
						ArrayList<String> autoUser = new ArrayList<String>();
						// 排班順序
						for (int au = 0; au < list.size(); au++) {
							for (int aug = 0; aug < list.get(au).size(); aug++) {
								String sms = list.get(au).get(aug);
								if (addUs(sms, autoUser)) {
									autoUser.add(sms);
									// MainActivity.out("g9/auto.."+autoUser.get(index));
								}
							}
						}

						int ipp = Integer.parseInt(pp); // 人數
						int imx = Integer.parseInt(mx); // 連班上限
						for (int mp = 0; mp < ipp; mp++) {
							boolean adduser = false;
							ArrayList<String> autoG1 = new ArrayList<String>(); // 候補一
							ArrayList<String> autoG2 = new ArrayList<String>(); // 候補二
							ArrayList<String> autoG3 = new ArrayList<String>(); // 候補三
							ArrayList<String> autoG4 = new ArrayList<String>(); // 候補四

							for (int wau = 0; wau < autoUser.size(); wau++) {
								int mmx = NameToMax(autoUser.get(wau));
								if (mmx == 0) {
									mmx = 99;
								}
								// TODO fx
								int mwd = workMonth.get(NameToPath(autoUser.get(wau)));
								if (mwd >= (fx * 2) || mwd >= mmx) {
									// 加入第二階段候補名單
									autoG1.add(autoUser.get(wau));
									autoG4.add(autoUser.get(wau));
								} else {
									boolean inside = false;
									boolean overtime = false;
									boolean fatigue = false;

									// 當日未排
									for (int adt = 0; adt < autoDay.size(); adt++) {
										String adts = MainActivity.getLine(autoDay.get(adt), 1, '/');
										if (debug) {
											if (adts.equals(autoUser.get(wau))) {
												inside = true;
											}
										} else {
											if (PathToName(Integer.parseInt(adts)).equals(autoUser.get(wau))) {
												inside = true;
											}
										}
									}

									if (!inside) {
										// 連續上班檢查
										// umx 使用者已連班上限
										// imx 工作指定連班上限
										int umx = workDay.get(NameToPath(autoUser.get(wau))) + 1;
										if (umx >= imx) {
											overtime = true;
										}
									}

									if (!inside && !overtime) {
										// 昨日上班檢查
										int up = yesterday.get(NameToPath(autoUser.get(wau)));
										if (up != 99) {
											if (ip != up) {
												fatigue = true;
											}
										}
									}
									if (!inside && !overtime && !fatigue) {
										// 連續
										adduser = true;
										int wmi = workMonth.get(NameToPath(autoUser.get(wau))) + 1;
										int wdi = workDay.get(NameToPath(autoUser.get(wau))) + 1;
										join.set(NameToPath(autoUser.get(wau)), true); // 有上班
										workMonth.set(NameToPath(autoUser.get(wau)), wmi); // 月班數+1
										workDay.set(NameToPath(autoUser.get(wau)), wdi); // 連續上班+1
										yesterday.set(NameToPath(autoUser.get(wau)), ip); // 昨日班次
										if (debug) {
											autoDay.add("2" + autoUser.get(wau) + '/' + ws.substring(1) + '|'); // 今日
										} else {
											autoDay.add(
													"2" + NameToPath(autoUser.get(wau)) + '/' + ws.substring(1) + '|'); // 今日
										}
										break;
									}
								}
							}
							if (!adduser) {
								for (int wau = 0; wau < autoG1.size(); wau++) {
									int mxx = NameToMax(autoG1.get(wau));
									if (mxx == 0) {
										mxx = 99;
									}
									// TODO (fx * 2)
									int mwd = workMonth.get(NameToPath(autoG1.get(wau)));
									if (mwd >= (fx * 2) || mwd >= mxx) {
										// 加入第三階段候補
										autoG2.add(autoG1.get(wau));
									} else {
										boolean inside = false;
										boolean overtime = false;
										boolean fatigue = false;
										// 當日未排
										for (int adt = 0; adt < autoDay.size(); adt++) {
											String adts = MainActivity.getLine(autoDay.get(adt), 1, '/');
											if (debug) {
												if (adts.equals(autoG1.get(wau))) {
													inside = true;
												}
											} else {
												if (PathToName(Integer.parseInt(adts)).equals(autoG1.get(wau))) {
													inside = true;
												}
											}
										}

										if (!inside) {
											// 連續上班檢查
											// umx 使用者已連班上限
											// imx 工作指定連班上限
											int umx = workDay.get(NameToPath(autoG1.get(wau))) + 1;
											if (umx >= imx) {
												overtime = true;
											}
										}

										if (!inside && !overtime) {
											// 昨日上班檢查
											int up = yesterday.get(NameToPath(autoG1.get(wau)));
											if (up != 99) {
												if (ip != up) {
													fatigue = true;
												}
											}
										}

										if (!inside && !overtime && !fatigue) {// 連續
											adduser = true;
											int wmi = workMonth.get(NameToPath(autoG1.get(wau))) + 1;
											int wdi = workDay.get(NameToPath(autoG1.get(wau))) + 1;
											join.set(NameToPath(autoG1.get(wau)), true); // 有上班
											workMonth.set(NameToPath(autoG1.get(wau)), wmi); // 月班數+1
											workDay.set(NameToPath(autoG1.get(wau)), wdi); // 連續上班+1
											yesterday.set(NameToPath(autoG1.get(wau)), ip); // 昨日班次
											if (debug) {
												autoDay.add("2" + autoG1.get(wau) + '/' + ws.substring(1) + '|'); // 今日
											} else {
												autoDay.add("2" + NameToPath(autoG1.get(wau)) + '/' + ws.substring(1)
														+ '|'); // 今日
											}
											break;
										}
									}
								}

								if (!adduser) {
									for (int wau = 0; wau < autoG2.size(); wau++) {
										int mmx = NameToMax(autoG2.get(wau));
										if (mmx == 0) {
											mmx = 99;
										}
										// TODO umax
										int mwd = workMonth.get(NameToPath(autoG2.get(wau)));
										if (!(mwd >= umax || mwd >= mmx)) {
											boolean inside = false;
											boolean overtime = false;
											boolean fatigue = false;

											// 當日未排
											for (int adt = 0; adt < autoDay.size(); adt++) {
												String adts = MainActivity.getLine(autoDay.get(adt), 1, '/');
												if (debug) {
													if (adts.equals(autoG2.get(wau))) {
														inside = true;
													}
												} else {
													if (PathToName(Integer.parseInt(adts)).equals(autoG2.get(wau))) {
														inside = true;
													}
												}
											}

											if (!inside) {
												// 連續上班檢查
												// umx 使用者已連班上限
												// imx 工作指定連班上限
												int umx = workDay.get(NameToPath(autoG2.get(wau))) + 1;
												if (umx >= imx) {
													overtime = true;
												}
											}

											if (!inside && !overtime) {
												// 昨日上班檢查
												int up = yesterday.get(NameToPath(autoG2.get(wau)));
												if (up != 99) {
													if (ip != up) {
														fatigue = true;
													}
												}
											}
											if (!inside && !overtime && !fatigue) {// 連續
												adduser = true;
												int wmi = workMonth.get(NameToPath(autoG2.get(wau))) + 1;
												int wdi = workDay.get(NameToPath(autoG2.get(wau))) + 1;
												join.set(NameToPath(autoG2.get(wau)), true); // 有上班
												workMonth.set(NameToPath(autoG2.get(wau)), wmi); // 月班數+1
												workDay.set(NameToPath(autoG2.get(wau)), wdi); // 連續上班+1
												yesterday.set(NameToPath(autoG2.get(wau)), ip); // 昨日班次
												if (debug) {
													autoDay.add("2" + autoG2.get(wau) + '/' + ws.substring(1) + '|'); // 今日
												} else {
													autoDay.add("2" + NameToPath(autoG2.get(wau)) + '/'
															+ ws.substring(1) + '|'); // 今日
												}
												break;
											}
										}
									}
								}
								if (!adduser) {
									if (md_mode == 0) {
										while (autoG2.size() != 0) {
											int mng = new Random().nextInt(autoG2.size());
											String gmna = autoG2.get(mng);
											autoG3.add(gmna);
											autoG2.remove(mng);
										}
									} else {
										for (int hx = 0; hx < autoG2.size(); hx++) {
											autoG3.add(autoG2.get(hx));
										}
									}
									for (int eau = 0; eau < autoG3.size(); eau++) {
										int mxx = NameToMax(autoG3.get(eau));
										if (mxx == 0) {
											mxx = 99;
										}
										// TODO (umax + 1)
										int mwd = workMonth.get(NameToPath(autoG3.get(eau)));
										if (!(mwd >= (umax + 1) || mwd >= mxx)) {
											boolean inside = false;
											boolean overtime = false;
											boolean fatigue = false;

											// 當日未排
											for (int adt = 0; adt < autoDay.size(); adt++) {
												String adts = MainActivity.getLine(autoDay.get(adt), 1, '/');
												if (debug) {
													if (adts.equals(autoG3.get(eau))) {
														inside = true;
													}
												} else {
													if (PathToName(Integer.parseInt(adts)).equals(autoG3.get(eau))) {
														inside = true;
													}
												}
											}

											if (!inside) {
												// 連續上班檢查
												// umx 使用者已連班上限
												// imx 工作指定連班上限
												int umx = workDay.get(NameToPath(autoG3.get(eau))) + 1;
												if (umx >= imx) {
													overtime = true;
												}
											}

											if (!inside && !overtime) {
												// 昨日上班檢查
												int up = yesterday.get(NameToPath(autoG3.get(eau)));
												if (up != 99) {
													if (ip == 0) {
														// 白班
														if (up == 2 || up == 3 || up == 4) {
															// 不上小夜,夜觀,大夜
															fatigue = true;
														}
													} else if (ip == 1) {
														// 白觀
														if (up == 2 || up == 3) {
															// 不上小夜,夜觀
															fatigue = true;
														}
													} else if (ip == 2) {
														// 小夜
														if (up == 3 || up == 4) {
															// 不上夜觀,大夜
															fatigue = true;
														}
													} else if (ip == 3) {
														// 夜觀
														if (up == 0 || up == 1) {
															// 不上白班,白觀
															fatigue = true;
														}
													} else {
														// 大夜
														if (up == 0 || up == 1) {
															// 不上白班,白觀
															fatigue = true;
														}
													}
												}
											}

											if (!inside && !overtime && !fatigue) {
												adduser = true;
												int wmi = workMonth.get(NameToPath(autoG3.get(eau))) + 1;
												int wdi = workDay.get(NameToPath(autoG3.get(eau))) + 1;
												join.set(NameToPath(autoG3.get(eau)), true); // 有上班
												workMonth.set(NameToPath(autoG3.get(eau)), wmi); // 月班數+1
												workDay.set(NameToPath(autoG3.get(eau)), wdi); // 連續上班+1
												yesterday.set(NameToPath(autoG3.get(eau)), ip); // 昨日班次
												if (debug) {
													autoDay.add("2" + autoG3.get(eau) + '/' + ws.substring(1) + '|'); // 今日
												} else {
													autoDay.add("2" + NameToPath(autoG3.get(eau)) + '/'
															+ ws.substring(1) + '|'); // 今日
												}
												break;
											}
										}
									}
								}
								if (!adduser && spp_mode == 0) {
									while (autoG4.size() != 0) {
										int ng = new Random().nextInt(autoG4.size());
										int x = NameToMax(autoG4.get(ng));
										if (x == 0) {
											x = 99;
										}
										int mwd = workMonth.get(NameToPath(autoG4.get(ng)));
										// 無法排班處理
										if (mwd >= (umax + 1) || mwd >= x) {
											autoG4.remove(ng);
										} else {
											boolean inside = false;
											boolean overtime = false;
											boolean fatigue = false;

											// 當日未排
											for (int adt = 0; adt < autoDay.size(); adt++) {
												String adts = MainActivity.getLine(autoDay.get(adt), 1, '/');
												if (debug) {
													if (adts.equals(autoG4.get(ng))) {
														inside = true;
													}
												} else {
													if (PathToName(Integer.parseInt(adts)).equals(autoG4.get(ng))) {
														inside = true;
													}
												}
											}
											// 連續上班檢查
											if (!inside) {
												// umx 使用者已連班上限
												// imx 工作指定連班上限
												int umx = workDay.get(NameToPath(autoG4.get(ng))) + 1;
												if (umx >= imx) {
													overtime = true;
												}
											}
											// 昨日上班檢查
											if (!inside && !overtime) {
												int up = yesterday.get(NameToPath(autoG4.get(ng)));
												if (up != 99) {
													if (ip == 0) {
														// 白班
														if (up == 2 || up == 3 || up == 4) {
															// 不上小夜,夜觀,大夜
															fatigue = true;
														}
													} else if (ip == 1) {
														// 白觀
														if (up == 2 || up == 3) {
															// 不上小夜,夜觀
															fatigue = true;
														}
													} else if (ip == 2) {
														// 小夜
														if (up == 3 || up == 4) {
															// 不上夜觀,大夜
															fatigue = true;
														}
													} else if (ip == 3) {
														// 夜觀
														if (up == 0 || up == 1) {
															// 不上白班,白觀
															fatigue = true;
														}
													} else {
														// 大夜
														if (up == 0 || up == 1) {
															// 不上白班,白觀
															fatigue = true;
														}
													}
												}
											}
											if (!inside && !overtime && !fatigue) {
												adduser = true;
												int wmi = workMonth.get(NameToPath(autoG4.get(ng))) + 1;
												int wdi = workDay.get(NameToPath(autoG4.get(ng))) + 1;
												join.set(NameToPath(autoG4.get(ng)), true); // 有上班
												workMonth.set(NameToPath(autoG4.get(ng)), wmi); // 月班數+1
												workDay.set(NameToPath(autoG4.get(ng)), wdi); // 連續上班+1
												yesterday.set(NameToPath(autoG4.get(ng)), ip); // 昨日班次
												if (debug) {
													autoDay.add("2" + autoG4.get(ng) + '/' + ws.substring(1) + '|'); // 今日
												} else {
													autoDay.add("2" + NameToPath(autoG4.get(ng)) + '/' + ws.substring(1)
															+ '|'); // 今日
												}
												break;
											} else {
												autoG4.remove(ng);
											}
										}
									}
								}
								if (!adduser) {
									autoDay.add("299/" + ws.substring(1) + '|');
									MainActivity.RedPoint
											.add(new GetPoint(MainActivity.Years, (MainActivity.Months + 1), j));
								}
							}
						}
					}
				} // Work.size()
			} else {
				if (Work.size() != 0 && User.size() == 0) {
					// MainActivity.out("g9/error.. " + j + " 班表錯誤!");
					for (int w = 0; w < Work.size(); w++) {
						String ws = Work.get(w), na, pp;
						String wp = MainActivity.getLine(ws, 1, '/'); // work.get(p);
						int xwp = Integer.parseInt(wp);
						if (xwp < MainActivity.Worklist.size()) {
							String wss = MainActivity.Worklist.get(xwp); // work.get(p).toString;
							// 白班教學回診1|1|08|11|
							int r = 0;
							na = MainActivity.getLine(wss, r, '|');
							r += na.length() + 3;
							pp = MainActivity.getLine(wss, r, '|');
							int ipp = Integer.parseInt(pp); // 人數
							for (int mp = 0; mp < ipp; mp++) {
								autoDay.add("299/" + ws.substring(1) + '|');
								MainActivity.RedPoint
										.add(new GetPoint(MainActivity.Years, (MainActivity.Months + 1), j));
							}
						}
					}
				}
			}
			// 不在班表內
			for (int jvls = 0; jvls < P.size(); jvls++) {
				String ppi = P.get(jvls);
				if (ppi.length() != 0) {
					I.add(MainActivity.getLine(
							MainActivity.Userlist.get(Integer.parseInt(MainActivity.getLine(P.get(jvls), 1, '/'))), 0,
							'|'));
				}
			}
			for (int cvls = 0; cvls < join.size(); cvls++) {
				String csls = MainActivity.getLine(MainActivity.Userlist.get(cvls), 0, '|');
				if (!NameToName(csls, I)) {
					F.add(csls);
				}
			}
			// 有事
			String usfu = "";
			for (int us = 0; us < F.size(); us++) {
				if (debug) {
					usfu += "4" + F.get(us) + '|';
				} else {
					usfu += "4" + NameToPath(F.get(us)) + '|';
				}
			}
			// 休假
			String unji = "";
			for (int ji = 0; ji < join.size(); ji++) {
				if (!join.get(ji)) {
					workDay.set(ji, 0);
					yesterday.set(ji, 99);
					String jina = MainActivity.getLine(MainActivity.Userlist.get(ji), 0, '|');
					if (!debug) {
						jina = NameToPath(jina) + "";
					}
					if (!NameToName(jina, F)) {
						if (debug) {
							unji += "3" + PathToName(ji) + '|';
						} else {
							unji += "3" + NameToPath(PathToName(ji)) + '|';
						}
					}
				}
			}
			String sadm = "";
			for (int adm = 0; adm < autoDay.size(); adm++) {
				sadm += autoDay.get(adm);
			}
			autoMonth.add(sadm + unji + usfu);
		}

		MainActivity.Datelist.set(0, (MainActivity.Datelist.get(0) + "0"));
		for (int amm = 0; amm < autoMonth.size(); amm++) {
			MainActivity.Datelist.set((amm + 1), (MainActivity.Datelist.get(amm + 1) + autoMonth.get(amm)));
		}
		MoreActivity.m_up = false;
		MainActivity.updateDay(this);
		MainActivity.toast(this, "已完成.");
		// new Timer(true).schedule(new TimerTask() {
		// public void run() {
		// }
		// }, 3000);
		finish();
	}

	@SuppressWarnings("unused")
	private String UsertoName(String na) {
		String una = MainActivity.getLine(na, 1, '/');
		String mus = MainActivity.Userlist.get(Integer.parseInt(una));
		String mna = MainActivity.getLine(mus, 0, '|');
		return mna;
	}

	private boolean NameToName(String na, ArrayList<String> F) {
		boolean rev = false;
		for (int rf = 0; rf < F.size(); rf++) {
			if (F.get(rf).equals(na)) {
				rev = true;
			}
		}
		return rev;
	}

	private String PathToName(int name) {
		if (name < MainActivity.Userlist.size()) {
			return MainActivity.getLine(MainActivity.Userlist.get(name), 0, '|');
		} else {
			return "99";
		}
	}

	private void NameToRandomPath(String name, String item, ArrayList<String> P) {
		for (int i = 0; i < J.size(); i++) {
			String na = J.get(i);
			if (na.equals(name)) {
				P.set(i, item);
				// MainActivity.out("g9/ntr.. " + i + " " + us);
			}
		}
	}

	private int RegularPath(int i, int j, int m) {
		int k = i - j;
		if (k < 0) {
			return m + k;
		} else {
			return k;
		}
	}

	private int NameToMax(String name) {
		for (int i = 0; i < MainActivity.Userlist.size(); i++) {
			int r = 0;
			String s = MainActivity.Userlist.get(i);
			String na = MainActivity.getLine(s, r, '|');
			r += na.length() + 1;
			String pw = MainActivity.getLine(s, r, '|');
			r += pw.length() + 1;
			String ot = MainActivity.getLine(s, r, '|');
			r += ot.length() + 1;
			int mx = Integer.parseInt(MainActivity.getLine(s, r, '|'));
			if (na.equals(name)) {
				return mx;
			}
		}
		return 0;
	}

	private int NameToPath(String name) {
		for (int i = 0; i < MainActivity.Userlist.size(); i++) {
			String s = MainActivity.Userlist.get(i);
			String na = MainActivity.getLine(s, 0, '|');
			if (na.equals(name)) {
				return i;
			}
		}
		return 99;
	}

	private boolean addUs(String name, ArrayList<String> aug) {
		for (int i = 0; i < aug.size(); i++) {
			if (name.equals(aug.get(i))) {
				return false;
			}
		}
		return true;
	}

	private void load() {
		// 白班教學回診1|1|08|11|0|
		J.clear();
		ArrayList<String> K = new ArrayList<String>(); // 亂數暫存
		for (int jk = 0; jk < MainActivity.Userlist.size(); jk++) {
			String kmt = MainActivity.getLine(MainActivity.Userlist.get(jk), 0, '|');
			K.add(kmt);
		}
		while (K.size() != 0) {
			int ks = new Random().nextInt(K.size());
			J.add(K.get(ks));
			K.remove(ks);
		}
		int w = 0;
		int u = 0;
		for (int i = 0; i < MainActivity.Datelist.size(); i++) {
			String mmsg = MainActivity.Datelist.get(i), tmp;
			if (i == 0) {
				tmp = mmsg.substring(4);
				if (tmp.length() > 0) {
					// String d = tmp.substring(0, 2);
					// day = Integer.parseInt(d);
					int r = 2;
					String us = MainActivity.getLine(tmp, r, '/');
					r += us.length() + 1;
					String m = MainActivity.getLine(tmp, r, '/');
					// 舊紀錄
					// sm = Integer.parseInt(m);
					u = Integer.parseInt(us);
					user.setText(u + "");
					max.setText(m + "");
					umax = Integer.parseInt(m);
				} else {
					MainActivity.toast(this, "操作錯誤.");
				}
			} else {
				int run = 0;
				char key = '|';
				// msg += 0 + "" + i + "/|"; 白班,白關,小夜,夜觀,大夜
				// msg += 1 + "" + i + "/11111|;"
				while (mmsg.indexOf(key, run) != -1) {
					tmp = MainActivity.getLine(mmsg, run, key);
					run += tmp.length() + 1;
					if (tmp.substring(0, 1).equals("0")) {
						String p = MainActivity.getLine(tmp, 1, '/');
						int awp = Integer.parseInt(p);
						if (awp < MainActivity.Worklist.size()) {
							String s = MainActivity.Worklist.get(awp);
							int r = 0;
							String sna = MainActivity.getLine(s, r, '|');
							r += sna.length() + 1;
							String stp = MainActivity.getLine(s, r, '|');
							r += 2;
							String spp = MainActivity.getLine(s, r, '|');
							// MainActivity.out("g16/" + s);
							// String sp = MainActivity.getLine(s,
							// MainActivity.getLine(s, 0, '|').length() + 1,
							// '|');
							String c = tmp.substring(2 + p.length());
							if (c != null) {
								if (c.length() != 0) {
									ArrayList<String> list = new ArrayList<String>();
									int cr = 0;
									char ck = '/';
									while (c.indexOf(ck, cr) != -1) {
										String ctmp = MainActivity.getLine(c, cr, ck);
										list.add(ctmp); // A||
										cr += ctmp.length() + 1;
									}
									for (int l = 0; l < list.size(); l++) {
										String a = list.get(l).substring(0, 1);
										String b = list.get(l).substring(1);
										// m += "1" + i + "/";
										if (a.equals("1")) {
											spp = b;
										}
									}
								}
							}
							w += Integer.parseInt(spp);
							// TODO rule
							if ("0".equals(stp)) {
								rule_0 += Integer.parseInt(spp);
							}
							if ("1".equals(stp)) {
								rule_1 += Integer.parseInt(spp);
							}
							if ("2".equals(stp)) {
								rule_2 += Integer.parseInt(spp);
							}
							if ("3".equals(stp)) {
								rule_3 += Integer.parseInt(spp);
							}
							if ("4".equals(stp)) {
								rule_4 += Integer.parseInt(spp);
							}
						}
					}
				}
			}
		}
		rule_0 = rule_0 / u;
		rule_1 = rule_1 / u;
		rule_2 = rule_2 / u;
		rule_3 = rule_3 / u;
		rule_4 = rule_4 / u;
		Integer.parseInt(MainActivity.getSQLdate().substring(2, 4));
		day.setText(DateUtils.getMonthDays(MainActivity.Years, MainActivity.Months) + "");
		work.setText(w + "");
		// umax = ((w / su) + 1);
		// max.setText(umax + "");
		ArrayList<String> spplist = new ArrayList<String>();
		spplist.add("自動");
		spplist.add("手動");
		ArrayAdapter<String> spp_adapter_0 = new ArrayAdapter<String>(this, R.layout.style_spinner, spplist);
		spp_adapter_0.setDropDownViewResource(R.layout.style_spinner);
		spp.setAdapter(spp_adapter_0);
		spp.setOnItemSelectedListener(this);

		ArrayList<String> aclist = new ArrayList<String>();
		for (int i = 0; i < MainActivity.Userlist.size(); i++) {
			aclist.add(MainActivity.getLine(MainActivity.Userlist.get(i), 0, '|'));
		}
		ArrayAdapter<String> ac_adapter_0 = new ArrayAdapter<String>(this, R.layout.style_spinner, aclist);
		ac_adapter_0.setDropDownViewResource(R.layout.style_spinner);
		ac.setAdapter(ac_adapter_0);
		ac.setOnItemSelectedListener(this);

		ArrayList<String> mdlist = new ArrayList<String>();
		mdlist.add("隨機");
		mdlist.add("順序");
		ArrayAdapter<String> md_adapter_0 = new ArrayAdapter<String>(this, R.layout.style_spinner, mdlist);
		md_adapter_0.setDropDownViewResource(R.layout.style_spinner);
		md.setAdapter(md_adapter_0);
		md.setOnItemSelectedListener(this);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
	}

	@SuppressLint("InflateParams")
	private void setListViewAdapter(int f, ArrayList<View> view, ArrayList<String> list, Context con) {
		final int i = f;
		View vi = LayoutInflater.from(con).inflate(R.layout.style_textview3, null);
		TextView name = (TextView) vi.findViewById(R.id.t3_name);
		EditText equal = (EditText) vi.findViewById(R.id.t3_equal);
		if (i < list.size()) {
			name.setText(MainActivity.getLine(list.get(i), 0, '|'));
			ed.add(equal);
		} else {
			equal.setVisibility(View.INVISIBLE);
			vi.setEnabled(false);
		}
		view.add(vi);
	}

	private void setView(Context con) {
		UserworkView.clear();
		for (int i = 0; i < MainActivity.Userlist.size(); i++) {
			setListViewAdapter(i, UserworkView, MainActivity.Userlist, con);
		}
		list.setAdapter(new MyListAdapter(UserworkView));
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
		if (parent.getId() == R.id.auto_spp) {
			spp_mode = position;
		}
		if (parent.getId() == R.id.auto_ac) {
			ac_mode = position;
		}
		if (parent.getId() == R.id.auto_mode) {
			md_mode = position;
			if (position == 0) {
				tac.setVisibility(View.GONE);
			} else {
				tac.setVisibility(View.VISIBLE);
			}
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {

	}
}
