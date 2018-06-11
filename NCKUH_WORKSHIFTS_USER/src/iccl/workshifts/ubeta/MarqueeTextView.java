package iccl.workshifts.ubeta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils.TruncateAt;
import android.widget.TextView;

public class MarqueeTextView extends TextView implements Runnable {
	public enum MarqueeDirection {
		LEFT, RIGHT
	}

	private volatile boolean m_bIsStop = false;
	private int m_iTextWidth = 0;// 文字寬度
	private boolean m_bIsMeasure = false;
	private int m_iDistance = 1;
	private int m_iSpeed = 25;
	private int m_iScrollPosition = 0;
	private MarqueeDirection m_Direction = MarqueeDirection.RIGHT;

	public MarqueeTextView(Context context) {
		super(context);
		setEllipsize(TruncateAt.MARQUEE);
		setSingleLine(true);
		onResume();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!m_bIsMeasure) { // 文字寬度只要獲取一次就好啦！
			getTextWidth();
			startScroolFrom0();
			m_bIsMeasure = true;
		}
		super.onDraw(canvas); // 先設定好再畫，才不會有殘影
	}

	public void onResume() {
		startScroll();
	}

	public void onPause() {
		stopScroll();
	}

	private void getTextWidth() {
		Paint paint = getPaint();
		String str = getText().toString();
		m_iTextWidth = (int) paint.measureText(str);
	}

	protected void startScroolFrom0() { // 跑馬燈初始值方向
		if (m_Direction == MarqueeDirection.RIGHT)
			m_iScrollPosition = -getWidth();
		else
			m_iScrollPosition = m_iTextWidth;

		scrollTo(m_iScrollPosition, 0);
	}

	@Override
	public void run() {
		if (m_bIsStop)
			return;

		// 跑馬燈初始值方向
		if (m_Direction == MarqueeDirection.RIGHT)
			m_iScrollPosition += m_iDistance;
		else
			m_iScrollPosition -= m_iDistance;
		scrollTo(m_iScrollPosition, 0);
		if ((m_Direction == MarqueeDirection.RIGHT && getScrollX() >= m_iTextWidth) || // 由右向左捲
				(m_Direction == MarqueeDirection.LEFT && getScrollX() <= -getWidth())) // 由左向右捲
		{
			startScroolFrom0();
		}
		postDelayed(this, m_iSpeed);
	}

	private void startScroll() {
		m_bIsStop = false;
		removeCallbacks(this);
		post(this);
	}

	private void stopScroll() {
		m_bIsStop = true;
		removeCallbacks(this);
	}
}