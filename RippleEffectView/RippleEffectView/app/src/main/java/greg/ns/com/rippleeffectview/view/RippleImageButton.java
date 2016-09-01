package greg.ns.com.rippleeffectview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import greg.ns.com.rippleeffectview.R;
import greg.ns.com.rippleeffectview.RippleGenerator;
import greg.ns.com.rippleeffectview.RippleInterface;

/**
 * Created by Gregory on 2016/8/31.
 */
public class RippleImageButton extends ImageButton implements RippleInterface {

	private RippleGenerator rippleGenerator;

	public RippleImageButton(Context context) {
		this(context, null, 0);
	}

	public RippleImageButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RippleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	public void init() {
		rippleGenerator = new RippleGenerator(this, getResources().getColor(R.color.colorAccent));
	}

	@Override
	public void setMoveAble(boolean isMoveAble) {
		if (rippleGenerator != null) {
			rippleGenerator.setMoveAble(isMoveAble);
		}
	}

	@Override
	public void setDuration(int duration) {
		if (rippleGenerator != null) {
			rippleGenerator.setDuration(duration);
		}
	}

	@Override
	public void setSpeed(int speed) {
		if (rippleGenerator != null) {
			rippleGenerator.setSpeed(speed);
		}
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		if (rippleGenerator != null) {
			rippleGenerator.setOnClickListener(l);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (rippleGenerator != null) {
			rippleGenerator.onSizeChanged(w, h);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (rippleGenerator != null) {
			rippleGenerator.onDraw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (rippleGenerator != null) {
			return rippleGenerator.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}
}
