package iccl.workshifts.ubeta;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import iccl.workshifts.ubeta.MainSocketOutput;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainActivity extends FragmentActivity
		implements OnClickListener, OnPageChangeListener, OnItemClickListener, OnItemLongClickListener {
	
	private int port = xxxx;
	private String IP = "127.0.0.1";
	private ViewPager mPager;
	private static boolean link = false, update = false;
	private static int CY, CM, page, pos;
	private static String item;
	private static ListView Daylistview;
	public static Activity activity;
	public static Socket skt;
	public static boolean main = true, g7 = false, g9 = false, g10 = false, g13 = false;
	public static boolean debug = false, net = false, linker;
	public static int Years, Months, Days, about;
	public static String email = "", ver = "未更新", name = "USER", GU, GW, GM;
	public static ArrayList<String> Daylist = new ArrayList<String>(); // 日期
	public static ArrayList<String[]> Itenlist = new ArrayList<String[]>();
	public static ArrayList<View> DayView = new ArrayList<View>(); // 10/11111|類型。位置/內容|
	public static ArrayList<String> Userlist = new ArrayList<String>(); // 姓名。備註。最後更新。更新碼(key)
	public static ArrayList<String> Worklist = new ArrayList<String>(); // 編號(key)。名稱。類別。人數。開始。結束
	public static ArrayList<String> Datelist = new ArrayList<String>(); // 年月(key)。班表
	public static ArrayList<String> Sortlist = new ArrayList<String>();
	public static ArrayList<GetPoint> RedPoint = new ArrayList<GetPoint>(); // 點
	public static ArrayList<GetPoint> Holidays = new ArrayList<GetPoint>(); // 假日
	public static ArrayList<MonthDateView> ViewDate = new ArrayList<MonthDateView>();
	public static DisplayMetrics metrics = new DisplayMetrics();
	public static ImageView im_menu, im_cloud, im_up;
	public static TextView tv_date, tv_add;
	public static TableRow tr_block;

	public MainActivity() {
		activity = MainActivity.this;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		findViewById();
		setToday();
		load();
		setView();
		setNET();
		showLogin();
		updateDay(this);
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
		save();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			save();
			android.os.Process.killProcess(android.os.Process.myPid());
			return true;
		default:
			break;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	private void setNET() {
		// 取得通訊服務
		ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			Boolean IsConnected = false;
			for (NetworkInfo network : connectivity.getAllNetworkInfo()) {
				if (network.getState() == NetworkInfo.State.CONNECTED) {
					IsConnected = true;
					net = true;
					// toast(this, "取得:" + network.getTypeName() + "連線");
					new Thread(read).start();
				}
			}
			if (!IsConnected) {
				// toast(this, "沒有網路連線");
			}
		} else {
			// toast(this, "沒有網路連線");
		}
	}

	private void load() {
		ArrayList<String> list = new ArrayList<String>();
		loadfile(load("TMP.txt"), list);
		if (list.size() > 0) {
			ver = list.get(0);
			name = list.get(1);
			CY = Integer.parseInt(list.get(2));
			CM = Integer.parseInt(list.get(3));
			if (list.size() == 5) {
				email = list.get(4);
			}
			Years = CY;
			Months = CM;
			Days = 15;
		} else {
			Years = CY;
			Months = CM;
			Days = 15;
		}
		list.clear();
		loadfile(load("USER.txt"), Userlist);
		loadfile(load("WORK.txt"), Worklist);
		loadfile(load("DATE.txt"), Datelist);
	}

	private void save() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(ver);
		list.add(name);
		list.add(CY + "");
		list.add(CM + "");
		list.add(email + "");
		saveFile("TMP.txt", list);
		saveFile("USER.txt", Userlist);
		saveFile("WORK.txt", Worklist);
		saveFile("DATE.txt", Datelist);
	}

	private String load(String file) {
		String Date = "";
		try {
			FileInputStream inStream = this.openFileInput(file);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			Date = stream.toString();
			stream.close();
			inStream.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return Date;
	}

	private void loadfile(String msg, ArrayList<String> list) {
		int run, s = 0;
		if (msg.length() > 0 && msg.indexOf('≡') > 0) {
			run = Integer.parseInt(msg.substring(0, msg.indexOf('=')));
			s = (run + "").length() + 1;
			for (int i = 0; i < run; i++) {
				String m = getLine(msg, s + i, '≡');
				list.add(m);
				s += m.length();
			}
		}
	}

	private void saveFile(String File, ArrayList<String> list) {
		String tmp = "";
		if (list.size() > 0) {
			tmp = list.size() + "=";
			for (int i = 0; i < list.size(); i++) {
				tmp += list.get(i) + '≡';
			}
		}
		try {
			FileOutputStream outStream = this.openFileOutput(File, MODE_PRIVATE);
			outStream.write(tmp.getBytes());
			outStream.close();
		} catch (IOException e) {
		}
	}

	private void findViewById() {
		im_menu = (ImageView) findViewById(R.id.im_menu);
		im_cloud = (ImageView) findViewById(R.id.im_cloud);
		im_up = (ImageView) findViewById(R.id.im_up);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_add = (TextView) findViewById(R.id.tv_add);
		tr_block = (TableRow) findViewById(R.id.tr_block);
		mPager = (ViewPager) findViewById(R.id.page);
		Daylistview = (ListView) findViewById(R.id.lv_1_main);
		Daylistview.setOnItemClickListener(this);
		// Daylistview.setOnItemLongClickListener(this);
	}

	private void setView() {
		page = 99;
		ScreenSlidePagerAdapter Adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(Adapter);
		mPager.setCurrentItem(page);
		mPager.addOnPageChangeListener(this);
		im_menu.setOnClickListener(this);
		im_cloud.setOnClickListener(this);
		im_up.setOnClickListener(this);
		// tr_block.setOnClickListener(this);
	}

	private void setToday() {
		Calendar calendar = Calendar.getInstance();
		Years = calendar.get(Calendar.YEAR);
		Months = calendar.get(Calendar.MONTH);
		Days = calendar.get(Calendar.DATE);
		CY = Years;
		CM = Months;
	}

	@SuppressWarnings("unused")
	private void edtRecovey() {
		for (int day = 0; day < DateUtils.getMonthDays(MainActivity.Years, MainActivity.Months); day++) {
			out("g5/" + getSQLdate() + getSQLday() + '/' + Datelist.get(day));
		}
		toast(this, "已更新.");
	}

	private void setHandShake() {
		if (net && link) {
			linker = false;
			out("a1/");
			new Timer(true).schedule(new TimerTask() {
				public void run() {
					if (!linker) {
						LKHandler.obtainMessage().sendToTarget();
					}
				}
			}, 3000);
		}
	}

	@TargetApi(23)
	private void setPermission() {
		@SuppressWarnings("static-access")
		int mBuild = Integer.parseInt(String.valueOf(new android.os.Build.VERSION().SDK_INT));
		if (mBuild >= 23) {
			int STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

			if (permission(STORAGE)) {
				showMessageOKCancel("親愛的用戶您好:\n由於Android 6.0 以上的版本在權限上有些更動，我們需要您授權以下的權限，感謝。",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								showPermission();
							}
						});
			}
		}
	}

	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", okListener).setCancelable(false)
				.create().show();
	}

	@TargetApi(23)
	@SuppressLint("NewApi")
	private void showPermission() {
		// We don't have permission so prompt the user
		List<String> permissions = new ArrayList<String>();
		permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
	}

	@TargetApi(23)
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			// 許可授權
		} else {
			// 沒有權限
			toast(this, "沒有使用權限");
			showPermission();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		showPermission();
	}

	private boolean permission(int mp) {
		return mp != PackageManager.PERMISSION_GRANTED;
	}

	private void showLogin() {
		if ("USER".equals(name)) {
			startActivity(new Intent(MainActivity.this, ViewActivityLogin.class));
		}
		setPermission();
	}

	private void showDialog(boolean defaule) {
		ArrayList<String> list = new ArrayList<String>();
		if (defaule) {
			list.add("更新碼： " + ver); // 0
			list.add("使用者： " + name); // 1
			list.add("詳細資料"); // 2
			if (Years == CY && Months == CM && g9) {
				if (g10) {
					list.add("排班記錄"); // 3
				} else {
					list.add("約班記錄"); // 3
				}
			}
			list.add("說明"); // 3 or 4
			new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogTheme))
					.setItems(list.toArray(new String[list.size()]), new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								break;
							case 1:
								startActivity(new Intent(MainActivity.this, ViewActivityLogin.class));
								break;
							case 2:
								startActivity(new Intent(MainActivity.this, MoreActivity.class));
								break;
							case 3:
								if (Years == CY && Months == CM && g9) {
									if (g10) {
										g10 = false;
										// toast(MainActivity.this, "g10");
									} else {
										g10 = true;
										// toast(MainActivity.this, "!g10");
									}
								} else {
									about = 0;
									startActivity(new Intent(MainActivity.this, AboutActivity.class));
								}
								break;
							case 4:
								startActivity(new Intent(MainActivity.this, AboutActivity.class));
								break;
							default:
								break;
							}
						}
					}).show();
		} else {
			list.add("新增班別"); // 0
			list.add("新增人員"); // 1
			new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogTheme))
					.setItems(list.toArray(new String[list.size()]), new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								g13 = true;
								break;
							case 1:
								g13 = false;
								break;
							default:
								break;
							}
							startActivity(new Intent(MainActivity.this, DateActivity.class));
						}

					}).show();
		}
	}

	// TODO OnClickListener
	// -----------------------------------------------------------------------

	public void onClick(View v) {
		if (v == tr_block) {
			if (link) {
				if (getReser()) {
					if (Years == CY && Months == CM && !g9) {
						if (getAutoMonth()) {
							showDialog(false);
						} else {
							out("g14/" + getSQLdate() + getSQLday());
						}
					} else {
						toast(this, "須手動更新班表.");
					}
				} else {
					toast(this, "僅能在約班使用.");
				}
			} else {
				toast(this, "無法更新操作.");
			}
		}
		if (v == im_cloud) {
			if (link) {
				if (update) {
					im_cloud.setImageResource(R.drawable.ic_cloud_computing);
					out("g4/" + getSQLdate());
					CY = Years;
					CM = Months;
					update = false;
					setHandShake();
				} else {
					toast(this, "連線正常.");
				}
			} else {
				toast(this, "無法更新操作.");
			}
		}
		if (v == im_up) {
			startActivity(new Intent(this, ViewActivityAutoFinal.class));
			if (!link) {
				toast(this, "無法更新操作.");
			}
		}
		if (v == im_menu) {
			showDialog(true);
		}
	}

	// TODO OnItemClickListener
	// -----------------------------------------------------------------------

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!getAutoDate() || g10) {
			int p = Integer.parseInt(getLine(item = Daylist.get(pos = position), 1, '/'));
			switch (item.substring(0, 1)) {
			case "0":
				if (p < Worklist.size()) {
					startActivity(new Intent(this, ViewActivityWork.class));
				} else {
					toast(this, "無法使用.");
				}
				break;
			case "1":
				if (p < Userlist.size()) {
					startActivity(new Intent(this, ViewActivityUser.class));
				} else {
					toast(this, "無法使用.");
				}
				break;
			case "2":
				break;
			case "3":
				break;
			case "4":
				break;
			}
		} else {
			if (getAutoDate() && !g10) {
				pos = Integer.parseInt(Itenlist.get(position)[1]);
				switch ((item = Itenlist.get(position)[0]).substring(0, 1)) {
				case "0":
					break;
				case "1":
					break;
				case "2":
					startActivity(new Intent(this, ViewActivitySupp.class));
					break;
				case "3":
					break;
				case "4":
					break;
				}
			}
		}
	}

	// TODO OnItemLongClickListener
	// -----------------------------------------------------------------------

	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		if (!getAutoDate()) {
			pos = position;
			item = Daylist.get(pos);
			String types = item.substring(0, 1);
			switch (types) {
			case "0":
				new AlertDialog.Builder(this).setTitle("確定移除?")
						.setPositiveButton("否", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						}).setNegativeButton("是", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								if (link) {
									String c = "";
									Daylist.set(position, c);
									for (int i = 0; i < Daylist.size(); i++) {
										if (Daylist.get(i).length() > 0) {
											c += (Daylist.get(i) + '|');
										}
									}
									Datelist.set(Days, c);
									out("g5/" + getSQLdate() + getSQLday() + '/' + c);
								} else {
									toast(MainActivity.this, "無法更新操作.");
								}
							}
						}).show();
				break;
			case "1":
				new AlertDialog.Builder(this).setTitle("確定移除?")
						.setPositiveButton("否", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						}).setNegativeButton("是", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								if (link) {
									String c = "";
									Daylist.set(position, c);
									for (int i = 0; i < Daylist.size(); i++) {
										if (Daylist.get(i).length() > 0) {
											c += (Daylist.get(i) + '|');
										}
									}
									Datelist.set(Days, c);
									out("g5/" + getSQLdate() + getSQLday() + '/' + c);
								} else {
									toast(MainActivity.this, "無法更新操作.");
								}
							}
						}).show();
				break;
			case "2":
				break;
			case "3":
				break;
			}
		}
		return true;
	}

	// TODO OnPageChangeListener
	// -----------------------------------------------------------------------

	public void onPageScrollStateChanged(int arg0) {

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	public void onPageSelected(int arg0) {
		boolean Right = false;
		if (page < arg0) {
			Right = true;
		} else if (page > arg0) {
			Right = false;
		}
		if (page != arg0) { // 翻頁動作
			if (Right) {
				page++;
				if (Months == 11) {
					Years++;
					Months = 0;
				} else {
					Months++;
				}
			} else {
				page--;
				if (Months == 0) {
					Years--;
					Months = 11;
				} else {
					Months--;
				}
			}
			if (Days > DateUtils.daysInGregorianMonth(Years, Months)) {
				Days = DateUtils.daysInGregorianMonth(Years, Months);
			}
			if (ViewDate.size() > 0) {
				for (int i = 0; i < ViewDate.size(); i++) {
					MonthDateView MDV = ViewDate.get(i);
					MDV.setSeldayToView(Years, Months, Days);
					MDV.setTextView(tv_date, tv_add);
				}
			}
			updateDay(this);
			mPager.setCurrentItem(page, false);
			if (link) {
				if (Years == CY && Months == CM) {
					// this is today
					update = false;
					im_cloud.setImageResource(R.drawable.ic_cloud_computing);

				} else {
					update = true;
					im_cloud.setImageResource(R.drawable.ic_cloud_refresh);
				}
			}
			return;
		}
	}

	// TODO private
	// -----------------------------------------------------------------------

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	private static void setMyDAYAdapter(boolean das, int f, String m, Context con) {
		final int i = f;
		View vi;
		int run = 1;
		char key = '/';
		String t = m.substring(0, 1); // 0:work,1:user
		String s;
		if (!debug) {
			if (das && !g10) {
				vi = LayoutInflater.from(con).inflate(R.layout.style_textview4, null);
				TextView textview_left = (TextView) vi.findViewById(R.id.textview_left);
				TextView textview_right = (TextView) vi.findViewById(R.id.textview_right);
				switch (t) {
				case "0":
					break;
				case "1":
					break;
				case "2":
					String an = getLine(m, run, key);
					run += an.length() + 1;
					String ap = getLine(m, run, key);
					run += ap.length() + 1;
					String ac = m.substring(run);
					int apc = Integer.parseInt(ap);
					if (apc < Worklist.size()) {
						s = Worklist.get(apc);
						s = getLine(s, 0, '|');
						if (ac.length() != 0) {
							ArrayList<String> list = new ArrayList<String>();
							int r = 0;
							char k = '/';
							while (ac.indexOf(k, r) != -1) {
								String tmp = getLine(ac, r, k);
								list.add(tmp); // A||
								r += tmp.length() + 1;
							}
							for (int l = 0; l < list.size(); l++) {
								String a = list.get(l).substring(0, 1);
								String b = list.get(l).substring(1);
								// name
								if (a.equals("0")) {
									s = b;
								}
							}
						}
						if (an.equals("99")) {
							an = "缺人";
							textview_right.setTextColor(con.getResources().getColorStateList(R.color.red));
						}
						if (an.equals(MainActivity.name)) {
							textview_left.setTextColor(con.getResources().getColorStateList(R.color.red));
							textview_right.setTextColor(con.getResources().getColorStateList(R.color.red));
						}
						textview_left.setText(s);
						textview_right.setText(an);
					} else {
						textview_left.setText("?");
					}
					Itenlist.add(new String[] { m, i + "" });
					DayView.add(vi);
					break;
				case "3":
					String na_3 = m.substring(1);
					textview_left.setText(na_3);
					textview_right.setText("休假");
					Itenlist.add(new String[] { m, i + "" });
					DayView.add(vi);
					break;
				case "4":
					String na_4 = m.substring(1);
					textview_left.setText(na_4);
					textview_right.setText("有事");
					Itenlist.add(new String[] { m, i + "" });
					DayView.add(vi);
					break;
				default:
					break;
				}
			} else {
				// msg += 0 + "" + i + "/"; 白班,白觀,小夜,夜觀,大夜 msg += 1 + "" + i +
				// "/11111"
				vi = LayoutInflater.from(con).inflate(R.layout.style_textview, null);
				TextView textview_left = (TextView) vi.findViewById(R.id.textview_left);
				TextView textview_right = (TextView) vi.findViewById(R.id.textview_right);
				switch (t) {
				case "0":
					// 白班教學回診1|0|1|08|11|8|10000|
					String wp = getLine(m, run, key);
					String wc = m.substring(wp.length() + 2);
					int awp = Integer.parseInt(wp);
					if (awp < Worklist.size()) {
						s = Worklist.get(awp);
						s = getLine(s, 0, '|');
						if (wc != null) {
							if (wc.length() != 0) {
								ArrayList<String> list = new ArrayList<String>();
								int r = 0;
								char k = '/';
								while (wc.indexOf(k, r) != -1) {
									String tmp = getLine(wc, r, k);
									list.add(tmp); // A||
									r += tmp.length() + 1;
								}
								for (int l = 0; l < list.size(); l++) {
									String a = list.get(l).substring(0, 1);
									String b = list.get(l).substring(1);
									// name
									if (a.equals("0")) {
										s = b;
									}
								}
							}
							wc = "";
						} else {
							wc = "null";
						}
						textview_left.setText(s);
						textview_right.setText(wc);
					} else {
						textview_left.setText("?");
					}
					DayView.add(vi);
					break;
				case "1":
					// A||
					String up = getLine(m, run, key);
					String uc = m.substring(2 + up.length());
					if (uc != null) {
						if (uc.length() == 5) {
							uc = getWork(uc);
						}
					} else {
						uc = "null";
					}
					int aup = Integer.parseInt(up);
					if (aup < Userlist.size()) {
						s = Userlist.get(aup);
						s = getLine(s, 0, '|');
						textview_left.setText(s);
						textview_right.setText(uc);
					} else {
						textview_left.setText("?");
					}
					DayView.add(vi);
					break;
				case "2":
					break;
				case "3":
					break;
				case "4":
					break;
				default:
					break;
				}
			}
		} else {
			vi = LayoutInflater.from(con).inflate(R.layout.style_textview4, null);
			TextView textview_left = (TextView) vi.findViewById(R.id.textview_left);
			textview_left.setText(m);
			DayView.add(vi);
		}
	}

	private static String getWorktoString(ArrayList<String> list, boolean Long) {
		int j = 0;
		String msg = "";
		String[] sa = { "白班", "白觀", "小夜", "夜觀", "大夜" };
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals("1")) {
				if (msg != null) {
					if (msg.length() > 1) {
						msg += ',';
						j++;
						if (j == 2 && Long) {
							msg += "\n";
						}
					}
				}
				msg += sa[i];
			}
		}
		return msg;
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private void setColor(TextView TV, int text, int background) {
		ColorStateList color = this.getResources().getColorStateList(text);
		TV.setTextColor(color);
		Drawable drawable = this.getResources().getDrawable(background);
		TV.setBackground(drawable);
	}

	private static String getWork(String c) {
		// "11111"
		int r = 0;
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			String a = c.substring(i, i + 1);
			if (!a.equals("0")) {
				r++;
			}
			list.add(a);
		}
		if (r == 0) {
			return "有事";
		} else if (r == 5) {
			return "都可以";
		} else if (r == 4) {
			return getWorktoString(list, true);
		} else {
			return getWorktoString(list, false);
		}
	}

	// TODO public
	// -----------------------------------------------------------------------

	public static void setDayItem(Context con, String c) {
		if (link) {
			String m = "";
			Daylist.set(pos, c);
			for (int i = 0; i < Daylist.size(); i++) {
				m += (Daylist.get(i) + '|');
			}
			day_upload(con);
			Datelist.set(Days, m);
			out("g5/" + getSQLdate() + getSQLday() + '/' + m);
			toast(con, "已更新.");
		} else {
			toast(con, "無法更新操作.");
		}
	}

	public static void edtSppItem(Context con) {
		String m = "";
		for (int i = 0; i < Daylist.size(); i++) {
			m += (Daylist.get(i) + '|');
		}
		day_upload(con);
		Datelist.set(Days, m);
		out("g5/" + getSQLdate() + getSQLday() + '/' + m);
		toast(con, "已更新.");
	}

	public static void setSppItem(Context con, String c) {
		String m = "", pm = "3" + getLine(c, 1, '/');
		Daylist.set(pos, c);
		for (int i = 0; i < Daylist.size(); i++) {
			if (pm.equals(Daylist.get(i))) {
				Daylist.remove(i);
			}
		}
		for (int j = 0; j < Daylist.size(); j++) {
			m += (Daylist.get(j) + '|');
		}
		day_upload(con);
		Datelist.set(Days, m);
	}

	public static void addSppItem(Context con, String c) {
		String m = "";
		Daylist.add(c);
		for (int i = 0; i < Daylist.size(); i++) {
			m += (Daylist.get(i) + '|');
		}
		day_upload(con);
		Datelist.set(Days, m);
	}

	public static String getUserName() {
		return name;
	}

	public static String getDayItem() {
		return item;
	}

	public static String getSQLday() {
		int D = Days;
		if (D < 10) {
			return "0" + D;
		} else {
			return "" + D;
		}
	}

	public static String getSQLdate() {
		int M = Months + 1;
		String Y = (Years + "").substring(2);
		if (M < 10) {
			return Y + 0 + M;
		} else {
			return Y + M;
		}
	}

	public static String getSQLYM() {
		int M = Months;
		String Y = Years + "";
		if (M < 10) {
			return Y + '0' + M;
		} else {
			return Y + M;
		}
	}

	public static void toast(Context act, String text) {
		Toast.makeText(act, text + "", Toast.LENGTH_SHORT).show();
	}

	public static boolean getAdd() {
		// if (link) {
		// if (getReser()) {
		// if (Years == CY && Months == CM && !g9) {
		// if (getAutoMonth()) {
		// return true;
		// }
		// }
		// }
		// }
		return false;
	}

	public static boolean getlink() {
		return link;
	}

	public static boolean getupdate() {
		return update;
	}

	public static boolean getReser() {
		String ms = "";
		if (Datelist.size() != 0) {
			for (int i = 0; i < Datelist.size(); i++) {
				ms += Datelist.get(i);
			}
			if (ms.length() > 4) {
				// 1705
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean getAutoDate() {
		String ms = "";
		if (Datelist.size() != 0) {
			ms = Datelist.get(0);
			if (ms.length() > 4) {
				if (ms.indexOf('/', 0) != -1) {
					int r = 0;
					String sr = getLine(ms, r, '/');
					r += sr.length() + 1;
					String ssr = getLine(ms, r, '/');
					r += ssr.length() + 1;
					String sssr = ms.substring(r);
					if (sssr.length() > 0) {
						return true;
					}
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public static boolean getAutoMonth() {
		String ms = Datelist.get(0);
		int r = 0;
		String sr = getLine(ms, r, '/');
		r += sr.length() + 1;
		String ssr = getLine(ms, r, '/');
		r += ssr.length() + 1;
		String sssr = ms.substring(r);
		if ("".equals(sssr)) {
			return true;
		}
		return false;
	}

	public static boolean getFinal() {
		String ms = Datelist.get(0);
		int r = 0;
		String sr = getLine(ms, r, '/');
		r += sr.length() + 1;
		String ssr = getLine(ms, r, '/');
		r += ssr.length() + 1;
		String sssr = ms.substring(r);
		if (sssr.equals("0")) {
			return true;
		}
		return false;
	}

	public static void out(String msg) {
		if (net) {
			new MainSocketOutput(msg).start();
		}
	}

	public static String getLine(String msg, int run, char key) {
		return msg.substring(run).substring(0, msg.substring(run).indexOf(key));
	}

	@SuppressLint("InflateParams")
	public static void day_upload(Context con) {
		boolean das = getAutoDate();
		for (int i = 0; i < Daylist.size(); i++) {
			setMyDAYAdapter(das, i, Daylist.get(i), con);
		}
		Daylistview.setAdapter(new MyListAdapter(DayView));
		Daylistview.deferNotifyDataSetChanged();
	}

	public static void updateDay(Context con) {
		// 點擊日期
		Sortlist.clear();
		RedPoint.clear();
		Itenlist.clear();
		DayView.clear();
		Daylist.clear();
		if (Datelist.size() > 0) { // 本月資料
			String dateTag = Datelist.get(0);
			if (dateTag.substring(0, 4).equals(getSQLdate())) { // 比對月份
				String mmsg = Datelist.get(Days), tmp;
				int run = 0;
				char key = '|';
				// TODO msg += 0 + "" + i + "/|"; 白班,白觀,小夜,夜觀,大夜
				// TODO msg += 1 + "" + i + '/' + "11111" + '|'
				// TODO msg += 2 + "" + u + '/' + w + '≠' + '|'
				while (mmsg.indexOf(key, run) != -1) {
					tmp = getLine(mmsg, run, key);
					run += tmp.length() + 1;
					if ("0".equals(tmp.substring(0, 1))) {
						Sortlist.add(tmp);
					} else {
						Daylist.add(tmp);
					}
				} 
				// TODO setSort
				for (int k = 0; k < Sortlist.size(); k++) {
					Daylist.add(Sortlist.get(k));
				}
				// TODO setPoint
				for (int i = 1; i < Datelist.size(); i++) {
					ArrayList<String> dlist = new ArrayList<String>();
					String dmsg = Datelist.get(i);
					int drun = 0;
					char dkey = '|';
					while (dmsg.indexOf(dkey, drun) != -1) {
						tmp = getLine(dmsg, drun, dkey);
						drun += tmp.length() + 1;
						dlist.add(tmp);
					}
					for (int di = 0; di < dlist.size(); di++) {
						if (dlist.get(di).substring(0, 1).equals("2")) {
							String dis = getLine(dlist.get(di), 1, '/');
							if (dis.equals("99") || dis.equals(name)) {
								RedPoint.add(new GetPoint(Years, (Months + 1), i));
							}
						}
					}
				}
				if (dateTag.length() > 4) {
					// toast(con, dateTag);
					if (dateTag.indexOf('/', 0) != -1) {
						int r = 0;
						String sr = getLine(dateTag, r, '/');
						r += sr.length() + 1;
						String ssr = getLine(dateTag, r, '/');
						r += ssr.length() + 1;
						String sssr = dateTag.substring(r);
						if (sssr.length() > 0) {
							boolean err = false;
							switch (sssr.substring(0, 1)) {
							case "0":
								im_up.setImageResource(R.drawable.ic_up);
								im_up.setVisibility(View.VISIBLE);
								g9 = true;
								break;
							case "1":
								im_up.setImageResource(R.drawable.ic_xls);
								im_up.setVisibility(View.VISIBLE);
								break;
							default:
								err = true;
								break;
							}
							if (!err) {
								ArrayList<Integer> holdays = new ArrayList<Integer>();
								if (sssr.length() > 1) {
									sssr = sssr.substring(1);
									while (sssr.length() != 0) {
										holdays.add(Integer.parseInt(sssr.substring(0, 2)));
										sssr = sssr.substring(2);
									}
								}
								for (int hd = 0; hd < holdays.size(); hd++) {
									Holidays.add(new GetPoint(Years, (Months + 1), holdays.get(hd)));
								}
							}
						}
					}
				}
			} else {
				im_up.setVisibility(View.GONE);
			}
		}
		day_upload(con);
	}

	public static void setDate(int Y, int M, int D) {
		Years = Y;
		Months = M;
		Days = D;
		if (link) {
			if (Years == CY && Months == CM) {
				// this is today
				update = false;
				im_cloud.setImageResource(R.drawable.ic_cloud_computing);
			} else {
				update = true;
				im_cloud.setImageResource(R.drawable.ic_cloud_refresh);
			}
		}
	}

	public static int getYearDate() {
		return Years;
	}

	public static int getMonthDate() {
		return Months;
	}

	public static int getDayDate() {
		return Days;
	}

	public static int getPage() {
		return page;
	}

	// TODO Runnable
	// -----------------------------------------------------------------------

	private Runnable read = new Runnable() {
		// 注意!! Runnable不能更動View
		public void run() {
			try {
				skt = new Socket(InetAddress.getByName(IP), port);
				BufferedReader buf = new BufferedReader(new InputStreamReader(skt.getInputStream(), "UTF-8"));
				out("g0/" + ver + '|' + name + '|' + getSQLdate() + '|');
				String m;
				while ((m = buf.readLine()) != null) {
					UIHandler.obtainMessage().sendToTarget();
					if (m.length() == 2) {
						switch (m) {
						case "a1":
							linker = true;
							break;
						case "s1":
							out("s1");
							break;
						case "g1":
							out("g1/" + getSQLdate());
							break;
						case "g4":
							out("g4/" + getSQLdate());
							break;
						case "r0":
							ViewActivityLogin.R0Handler.obtainMessage().sendToTarget();
							break;
						case "r1":
							ViewActivityLogin.R1Handler.obtainMessage().sendToTarget();
							break;
						default:
							break;
						}
					} else {
						// Msg.length() >= 3
						String tag = m.substring(0, 2);
						// 取得標籤
						switch (tag) {
						case "GG": // 更新碼與資料庫不符
							ver = m.substring(3);
							if (debug) {
								GGHandler.obtainMessage().sendToTarget();
							}
							break;
						case "GU": // getUser
							GU = m;
							GUHandler.obtainMessage().sendToTarget();
							break;
						case "GW": // getWork
							GW = m;
							GWHandler.obtainMessage().sendToTarget();
							break;
						case "GM": // getMonth
							GM = m;
							GMHandler.obtainMessage().sendToTarget();
							break;
						default:
							break;
						}
					}
				}
			} catch (IOException e) {
			}
		}
	};

	// TODO Handler
	// -----------------------------------------------------------------------

	private Handler GGHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			toast(MainActivity.this, "目前版本:" + ver);
		};
	};

	@SuppressLint("HandlerLeak")
	private Handler GUHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			/*
			 * A|||║B|||║C|||║D|||║E|||║F|||║G|||║H|||║I|||║J|||║JK|||║K|||║
			 * L|||║M|||║N|||║O|||║P|||║Q|||║R|||║S|||║T|||║U|||║V|||║W|||║
			 * X|||║Y|||║Z|||║
			 */
			Userlist.clear();
			String mmsg = GU.substring(3), tmp;
			int run = 0;
			char key = '║';
			while (mmsg.indexOf(key, run) != -1) {
				tmp = getLine(mmsg, run, key);
				Userlist.add(tmp); // A|||
				run += tmp.length() + 1;
			}
		};
	};

	private Handler GWHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			/*
			 * 白班教學回診1|0|1|08|11|8|10000|║白班教學回診2|0|1|11|14|8|10000|║白班教學回診3|0|1
			 * |14|17|8|10000|║
			 * 白班觀察室|1|1|08|20|8|10000|║夜班教學回診1|2|1|17|20|8|10000|║夜班教學回診2|2|1|
			 * 20|23|8|10000|║
			 * 夜班教學回診3|2|1|23|02|8|10000|║夜班觀察室|3|1|20|08|8|10000|║大夜教學回診1|4|1|
			 * 02|05|8|10000|║ 大夜教學回診2|4|1|05|08|8|10000|║
			 */
			Worklist.clear();
			String mmsg = GW.substring(3), tmp;
			int run = 0;
			char key = '║';
			while (mmsg.indexOf(key, run) != -1) {
				tmp = getLine(mmsg, run, key);
				Worklist.add(tmp); // 白班教學回診1|0|1|08|11|0|
				run += tmp.length() + 1;
			}
			if (g7) {
				MoreActivity.setView(getApplication());
			}
		};
	};

	private Handler GMHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			/*
			 * ║║║║║║║║║║║║║║║║║║║║║║║║║1/A|2/B|3/D|║║║║║║
			 */
			Holidays.clear();
			RedPoint.clear();
			Datelist.clear();
			g9 = false;
			g10 = false;
			im_up.setVisibility(View.GONE);
			String mmsg = GM.substring(3), tmp;
			int run = 0;
			char key = '║';
			while (mmsg.indexOf(key, run) != -1) {
				tmp = getLine(mmsg, run, key);
				Datelist.add(tmp); // 1/A|2/B|3/D|
				run += tmp.length() + 1;
				if (Datelist.size() == 1) {
					if (tmp.length() == 0) {
						Datelist.set(0, getSQLdate());
					}
				}
			}
			updateDay(MainActivity.this);
			for (int m = 0; m < ViewDate.size(); m++) {
				MonthDateView MDV = ViewDate.get(m);
				MDV.invalidate();
			}
		};
	};

	private Handler UIHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			link = true;
			im_cloud.setImageResource(R.drawable.ic_cloud_computing);
		};
	};

	public static Handler KDHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			activity.finish();
		};
	};

	private Handler LKHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			link = false;
			im_cloud.setImageResource(R.drawable.ic_cloud_erron);
			toast(MainActivity.this, "失去連線");
		};
	};
}
