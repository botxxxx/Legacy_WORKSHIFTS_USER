package iccl.workshifts.ubeta;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScreenSlidePageFragment extends Fragment { // page
	private static final String ARG_PAGE = "page";
	private int mPageNumber;

	public static ScreenSlidePageFragment create(int m) {
		ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, m);
		fragment.setArguments(args);
		return fragment;
	}

	public ScreenSlidePageFragment() {
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.calendar_page, container, false);
		return rootView;
	}

	public int getPageNumber() {
		return mPageNumber;
	}
}