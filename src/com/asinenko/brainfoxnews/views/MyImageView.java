package com.asinenko.brainfoxnews.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class MyImageView extends ImageView{

	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;

	public MyImageView(Context context) {
		super(context);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		mScaleDetector.onTouchEvent(ev);
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor);

		if(this.getDrawable() != null){
			this.getDrawable().draw(canvas);
		}
		//getImageMatrix().preScale(mScaleFactor, mScaleFactor);
		canvas.restore();
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
			Log.w("1111111111111",String.valueOf(mScaleFactor));
			invalidate();
			return true;
		}
	}
}
