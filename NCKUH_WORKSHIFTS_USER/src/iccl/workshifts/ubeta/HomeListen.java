package iccl.workshifts.ubeta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HomeListen {
	private Context mContext = null;
	private IntentFilter mHomeBtnIntentFilter = null;
	private OnHomeBtnPressLitener mOnHomeBtnPressListener = null;
	private HomeBtnReceiver mHomeBtnReceiver = null;

	public HomeListen(Context context) {
		mContext = context;
		mHomeBtnReceiver = new HomeBtnReceiver();
		mHomeBtnIntentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	}

	public void setOnHomeBtnPressListener(OnHomeBtnPressLitener onHomeBtnPressListener) {
		mOnHomeBtnPressListener = onHomeBtnPressListener;
	}

	public void start() {
		mContext.registerReceiver(mHomeBtnReceiver, mHomeBtnIntentFilter);
	}

	public void stop() {
		mContext.unregisterReceiver(mHomeBtnReceiver);
	}

	private class HomeBtnReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			receive(context, intent);
		}
	}

	private void receive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
			String reason = intent.getStringExtra("reason");
			if (reason != null) {
				if (null != mOnHomeBtnPressListener) {
					if (reason.equals("homekey")) {
						mOnHomeBtnPressListener.onHomeBtnPress();
					} else if (reason.equals("recentapps")) {
						mOnHomeBtnPressListener.onHomeBtnLongPress();
					}
				}
			}
		}
	}

	public interface OnHomeBtnPressLitener {
		public void onHomeBtnPress();

		public void onHomeBtnLongPress();
	}

}
