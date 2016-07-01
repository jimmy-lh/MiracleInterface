package com.miracle.roundprogress.activity.roundprogressbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.miracle.activity.R;

public class RoundProgressActivity extends Activity {
	private RoundProgressBar mRoundProgressBar1, mRoundProgressBar2, mRoundProgressBar3, mRoundProgressBar4,
			mRoundProgressBar5;
	private int progress = 0;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, RoundProgressActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roundprogress_activity_round_progress);

		mRoundProgressBar1 = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
		mRoundProgressBar2 = (RoundProgressBar) findViewById(R.id.roundProgressBar2);
		mRoundProgressBar3 = (RoundProgressBar) findViewById(R.id.roundProgressBar3);
		mRoundProgressBar4 = (RoundProgressBar) findViewById(R.id.roundProgressBar4);
		mRoundProgressBar5 = (RoundProgressBar) findViewById(R.id.roundProgressBar5);

		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						while (progress <= 100) {
							progress += 3;

							System.out.println(progress);

							mRoundProgressBar1.setProgress(progress);
							mRoundProgressBar2.setProgress(progress);
							mRoundProgressBar3.setProgress(progress);
							mRoundProgressBar4.setProgress(progress);
							mRoundProgressBar5.setProgress(progress);

							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

					}
				}).start();
			}
		});

	}

}
