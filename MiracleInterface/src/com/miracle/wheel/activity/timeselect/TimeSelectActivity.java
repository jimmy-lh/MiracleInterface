package com.miracle.wheel.activity.timeselect;

import java.util.ArrayList;

import com.miracle.activity.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class TimeSelectActivity extends Activity {
	private TimeSelectView tsView;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, TimeSelectActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wheel_activity_time_select);

		tsView = (TimeSelectView) findViewById(R.id.tsView);
		ArrayList<String> lst = new ArrayList<String>();
		for (int i = 0; i < 200; i++) {
			lst.add("" + (1990 + i));
		}
		tsView.init(lst);
		tsView.onSelectListener(new SelListener() {

			@Override
			public void onClick(String text) {
				Toast.makeText(TimeSelectActivity.this, text, 0).show();
			}
		});
	}
}
