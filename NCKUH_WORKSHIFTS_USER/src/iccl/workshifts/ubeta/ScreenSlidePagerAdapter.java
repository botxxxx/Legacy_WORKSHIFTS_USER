package iccl.workshifts.ubeta;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	public ScreenSlidePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public Fragment getItem(int position) {
		return ScreenSlidePageFragment.create(position);
	}

	public int getCount() {
		if (MainActivity.getPage() != 1) {
			return Integer.MAX_VALUE;
		} else {
			return 1;
		}
	}
}
