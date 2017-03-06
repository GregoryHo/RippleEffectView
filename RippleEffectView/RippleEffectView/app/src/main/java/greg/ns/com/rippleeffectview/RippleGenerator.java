package greg.ns.com.rippleeffectview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Gregory on 2016/8/31.
 */
public class RippleGenerator {

	private float duration  = 150;
	private float speed     = 2;
	private float radius    = 0;
	private float endRadius = 0;
	private float startX    = 0;
	private float startY    = 0;
	private float rippleX   = 0;
	private float rippleY   = 0;
	private int width       = 0;
	private int height      = 0;

	private Paint paint;
	private int rippleColor;
	private CustomHandler handler;
	private int touchAction;
	private boolean isMoveAble;
	private long clickedTime;

	// The target view
	private View rippleView;

	// The target click listener
	private View.OnClickListener clickListener = null;

	public RippleGenerator(View rippleView, int color) {
		this.rippleView = rippleView;
		this.rippleColor = color;
		
		/** 
		 * Set the default background color as white,
		 * the background is necessary, otherwsie the ripple won't work.
		 */
		rippleView.setBackgroundColor(Color.WHITE); 
		init();
	}

	private void init() {
		if (rippleView.isInEditMode()) {
			return;
		}

		paint = new Paint();
		handler = new CustomHandler(this);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(rippleColor);
		paint.setAntiAlias(true);
	}

	public void setOnClickListener(View.OnClickListener l) {
		clickListener = l;
		if (paint == null) {
			init();
		}
	}

	public void setMoveAble(boolean isMoveAble) {
		this.isMoveAble = isMoveAble;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void onSizeChanged(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void onDraw(@NonNull Canvas canvas) {
		if (radius > 0 && radius < endRadius) {
			canvas.drawCircle(rippleX, rippleY, radius, paint);
			if (touchAction == MotionEvent.ACTION_UP) {
				invalidate();
			}
		}
	}

	public boolean onTouchEvent(@NonNull MotionEvent event) {
		rippleX = event.getX();
		rippleY = event.getY();

		if (System.currentTimeMillis() < clickedTime + 200) {
			return false;
		}

		switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				return actionUP();

			case MotionEvent.ACTION_CANCEL:
				return actionCancel();

			case MotionEvent.ACTION_DOWN:
				return actionDown(event);

			case MotionEvent.ACTION_MOVE: {
				if (isMoveAble) {
					if (rippleX < 0 || rippleX > width || rippleY < 0 || rippleY > height) {
						return actionCancel();
					} else {
						touchAction = MotionEvent.ACTION_MOVE;
						invalidate();
						return true;
					}
				} else {
					requestDisallowInterceptTouchEvent();
				}
				break;
			}

			default:
				break;
		}
		
		return false;
	}

	private boolean actionUP() {
		// over widget size, then return
		if (Math.abs(rippleX - startX) > width || Math.abs(rippleY - startY) > height) {
			return false;
		}

		clickedTime = System.currentTimeMillis();

		requestDisallowInterceptTouchEvent();
		touchAction = MotionEvent.ACTION_UP;

		radius = 1;
		endRadius = Math.max(Math.max(Math.max(width - rippleX, rippleX), rippleY), height - rippleY);
		speed = endRadius / duration * 10;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				System.out.println("run - " + "radius" + " : [" + radius + "]");
				if (radius < endRadius) {
					radius += speed;
					paint.setAlpha(90 - (int) (radius / endRadius * 90));
					handler.postDelayed(this, 1);
				} else {
					if (clickListener != null && rippleView != null) {
						System.out.println("run - " + "onClick" + " : [" + true + "]");
						clickListener.onClick(rippleView);
					}
				}
			}
		}, 10);
		invalidate();

		return false;
	}

	private boolean actionCancel() {
		requestDisallowInterceptTouchEvent();
		touchAction = MotionEvent.ACTION_CANCEL;
		radius = 0;
		invalidate();

		return false;
	}

	private boolean actionDown(@NonNull MotionEvent event) {
		requestDisallowInterceptTouchEvent();
		touchAction = MotionEvent.ACTION_UP;
		endRadius = Math.max(Math.max(Math.max(width - rippleX, rippleX), rippleY), height - rippleY);
		paint.setAlpha(90);
		radius = endRadius / 4;
		startX = event.getX();
		startY = event.getY();
		invalidate();

		return true;
	}

	private void requestDisallowInterceptTouchEvent() {
		if (rippleView != null) {
			rippleView.getParent().requestDisallowInterceptTouchEvent(false);
		}
	}

	private void invalidate() {
		if (rippleView != null) {
			rippleView.invalidate();
		}
	}

	private static class CustomHandler extends Handler {

		private final RippleGenerator instance;

		public CustomHandler(RippleGenerator rippleGenerator) {
			WeakReference<RippleGenerator> weakReference = new WeakReference<RippleGenerator>(rippleGenerator);
			instance = weakReference.get();
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	}
}
