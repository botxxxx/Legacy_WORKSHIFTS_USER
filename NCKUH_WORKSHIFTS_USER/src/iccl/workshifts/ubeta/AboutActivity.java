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
			Aboutlist.add(new String[] { "�s�u���A", "�k�W�足���Ϊ��ϥܬ��s�u���A,�󴫤���ɶ��I���ϥܶi���ʧ�s." });
			// Aboutlist.add(new String[] { "�s�W�Z�O", "�I�����W�誺���,�i����s�W�Z�O." });
			// Aboutlist.add(new String[] { "�ק�Z�O", "�I���M�椺���Z�O���,�i�ק���Z�O." });
			// Aboutlist.add(new String[] { "�����Z�O", "�����M�椺���ﶵ�i����,���N���Ʀ��Z�O." });
			// Aboutlist.add(new String[] { "�s�W�H��", "�I�����W�誺���,�i����s�W�H��." });
			Aboutlist.add(new String[] { "�ק�N�@", "�I���M�椺���ϥΪ�,�i�ק���ȯZ�N�@." });
			// Aboutlist.add(new String[] { "�����H��", "�����M�椺���ﶵ�i����,�é�Z����ܦ���." });
			// Aboutlist.add(new String[] { "�˵��ƯZ", "�I���W�誺(��)�ϥܥi�˵��۰ʱƯZ���G." });
			// Aboutlist.add(new String[] { "���Z����", "�k�W��]�w��椺�����Z����,�i�����ܬ��Z�αƯZ�Z��." });
			Aboutlist.add(new String[] { "�ԲӸ��", "�k�W��]�w��椺���ԲӸ��,�i�˵��Z��P�H��." });
			Aboutlist.add(new String[] { "�ʤH����", "�p���ʤH���αN�b���W�ά��I�Х�." });
			Aboutlist.add(new String[] { "�կZ�@�~", "�I���M�椺�w�����ƯZ���Z�O�i��ק�." });
			Aboutlist.add(new String[] { "�p����", "�۰ʱƯZ��L�k�s�W�B�ק�P�������Z." });
			break;
		case 1:
			Aboutlist.add(new String[] { "�}�l���Z", "�s�W�Z�O�P�H���ܷ���." });
			Aboutlist.add(new String[] { "�۰ʱƯZ", "�w�������Z��i�ϥΦ۰ʱƯZ." });
			Aboutlist.add(new String[] { "�M���Z��", "�M�������Ҧ����Z�P�ƯZ." });
			Aboutlist.add(new String[] { "�s�W�Z�O", "�N�ۭq�Z�O�s�W�ܲM�椺." });
			Aboutlist.add(new String[] { "�ק�Z�O", "�I���M�椺���Z�O�i�ק�,�Z�O�����p:�կZ,���[��." });
			Aboutlist.add(new String[] { "�ק�W��", "�I���M�椺���Z�O�i�ק�,�Z�O�W�٥i�P��L�Z�O����." });
			Aboutlist.add(new String[] { "�ק�ɶ�", "�I���M�椺���Z�O�i�ק�." });
			Aboutlist.add(new String[] { "�ק�H��", "�I���M�椺���Z�O�i�ק�." });
			Aboutlist.add(new String[] { "�u�@�W��", "�I���M�椺���Z�O�i�ק�." });
			Aboutlist.add(new String[] { "�����Z�O", "�����M�椺���ﶵ�i����." });
			Aboutlist.add(new String[] { "���m�Z�O", "���m�Z�O���w�]��." });
			Aboutlist.add(new String[] { "�p����", "���M���w�W�Ǫ��۰ʱƯZ���G�Ц�Xls�������D." });
			break;
		case 2:
			Aboutlist.add(new String[] { "�s�W�H��", "�N�ۭq�H���s�W�ܲM�椺." });
			Aboutlist.add(new String[] { "���N��", "�I���M�椺���H���i�ק�,�i�ϥΤ��^�������i�P��L�ϥΪ̭���." });
			Aboutlist.add(new String[] { "�ƯZ�W��", "�I���M�椺���H���i�ק�,�i����H����۰ʱƯZ���ƯZ�W��." });
			Aboutlist.add(new String[] { "�]�w�K�X", "�I���M�椺���H���i�ק�." });
			Aboutlist.add(new String[] { "�K�[�Ƶ�", "�I���M�椺���H���i�ק�." });
			Aboutlist.add(new String[] { "�����H��", "�����M�椺���ﶵ�i����." });
			Aboutlist.add(new String[] { "���m�H��", "���m�H�����w�]��." });
			Aboutlist.add(new String[] { "�p����", "���m�H���N�M���Ҧ��ϥΪ̪��]�w��." });
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
