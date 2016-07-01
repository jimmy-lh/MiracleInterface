package com.miracle.water.activity.water;

import com.miracle.activity.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class WaterRippleActivity extends Activity {

	private ImageView mNormal, mWave1, mWave2, mWave3;

	private AnimationSet mAnimationSet1, mAnimationSet2, mAnimationSet3;

	private static final int OFFSET = 600; // 每个动画的播放时间间隔
	private static final int MSG_WAVE2_ANIMATION = 2;
	private static final int MSG_WAVE3_ANIMATION = 3;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WAVE2_ANIMATION:
				mWave2.startAnimation(mAnimationSet2);
				break;
			case MSG_WAVE3_ANIMATION:
				mWave3.startAnimation(mAnimationSet3);
				break;
			}
		}
	};

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, WaterRippleActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.water_activity_water_ripple);

		mNormal = (ImageView) findViewById(R.id.normal);
		mWave1 = (ImageView) findViewById(R.id.wave1);
		mWave2 = (ImageView) findViewById(R.id.wave2);
		mWave3 = (ImageView) findViewById(R.id.wave3);

		mAnimationSet1 = initAnimationSet();
		mAnimationSet2 = initAnimationSet();
		mAnimationSet3 = initAnimationSet();

		mNormal.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					showWaveAnimation();
					break;
				case MotionEvent.ACTION_UP:
					clearWaveAnimation();
					break;
				case MotionEvent.ACTION_CANCEL:
					clearWaveAnimation();
				}
				return true;
			}
		});
	}

	private AnimationSet initAnimationSet() {
		AnimationSet as = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1f, 2.3f, 1f, 2.3f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(OFFSET * 3);
		sa.setRepeatCount(Animation.INFINITE);// 设置循环
		AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
		aa.setDuration(OFFSET * 3);
		aa.setRepeatCount(Animation.INFINITE);// 设置循环
		as.addAnimation(sa);
		as.addAnimation(aa);
		return as;
	}

	private void showWaveAnimation() {
		mWave1.startAnimation(mAnimationSet1);
		mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, OFFSET);
		mHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, OFFSET * 2);
	}

	private void clearWaveAnimation() {
		mWave1.clearAnimation();
		mWave2.clearAnimation();
		mWave3.clearAnimation();
	}

}
