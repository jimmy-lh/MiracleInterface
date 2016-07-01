package com.miracle.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ExplainActivity extends Activity {
	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, ExplainActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_explain);
	}

}
