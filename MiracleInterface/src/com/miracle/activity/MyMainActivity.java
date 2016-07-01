package com.miracle.activity;

import com.miracle.activity.yt2glu.ReceiveActivity;
import com.miracle.commonlist.activity.listview.ListViewActivity;
import com.miracle.expandable.activity.expandable.ExpandableActivity;
import com.miracle.netimage.activity.netimage.NetImageActivity;
import com.miracle.netjudge.activity.netjudge.NetJudgeActivity;
import com.miracle.ormlite.activity.testormlite.TestOrmliteActivity;
import com.miracle.roundprogress.activity.roundprogressbar.RoundProgressActivity;
import com.miracle.telephony.activity.telephony.TelephonyManagerActivity;
import com.miracle.water.activity.water.WaterRippleActivity;
import com.miracle.wheel.activity.timeselect.TimeSelectActivity;
import com.miracle.wlan.activity.wlan.WLanActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyMainActivity extends Activity implements OnClickListener {

	private Button mButtonExplain;
	private Button mButtonTimeSelect;
	private Button mButtonYt2GluSelect;
	private Button mButtonListView;
	private Button mButtonExpandable;
	private Button mButtonRoundProgress;
	private Button mButtonTelephonyManager;
	private Button mButtonNetJudge;
	private Button mButtonWaterRipple;
	private Button mButtonTestOrmlite;
	private Button mButtonNetImage;
	private Button mButtonWLan;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, MyMainActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mButtonExplain = (Button) findViewById(R.id.btn_interface_explain);
		mButtonExplain.setOnClickListener(this);

		mButtonTimeSelect = (Button) findViewById(R.id.btn_time_select);
		mButtonTimeSelect.setOnClickListener(this);

		mButtonYt2GluSelect = (Button) findViewById(R.id.btn_yt2glu);
		mButtonYt2GluSelect.setOnClickListener(this);

		mButtonListView = (Button) findViewById(R.id.btn_listview);
		mButtonListView.setOnClickListener(this);

		mButtonExpandable = (Button) findViewById(R.id.btn_expandable);
		mButtonExpandable.setOnClickListener(this);

		mButtonRoundProgress = (Button) findViewById(R.id.btn_round_progress);
		mButtonRoundProgress.setOnClickListener(this);

		mButtonTelephonyManager = (Button) findViewById(R.id.btn_telephony_manager);
		mButtonTelephonyManager.setOnClickListener(this);

		mButtonNetJudge = (Button) findViewById(R.id.btn_net_judge);
		mButtonNetJudge.setOnClickListener(this);

		mButtonWaterRipple = (Button) findViewById(R.id.btn_water_ripple);
		mButtonWaterRipple.setOnClickListener(this);

		mButtonTestOrmlite = (Button) findViewById(R.id.btn_test_ormlite);
		mButtonTestOrmlite.setOnClickListener(this);

		mButtonNetImage = (Button) findViewById(R.id.btn_net_image);
		mButtonNetImage.setOnClickListener(this);

		mButtonWLan = (Button) findViewById(R.id.btn_wlan);
		mButtonWLan.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 接口说明
		case R.id.btn_interface_explain:
			ExplainActivity.actionStart(this, 0);
			break;

		// 血糖仪测试版 yt2glu
		case R.id.btn_yt2glu:
			ReceiveActivity.actionStart(this, 0);
			break;
		/**
		 * ConnectivityManager
		 */
		case R.id.btn_net_judge:
			NetJudgeActivity.actionStart(this, 0);
			break;
		/**
		 * listview
		 */
		// 列表菜单 listview
		case R.id.btn_listview:
			ListViewActivity.actionStart(this, 0);
			break;

		case R.id.btn_expandable:
			ExpandableActivity.actionStart(this, 0);
			break;
		/**
		 * net image 网络图片
		 */
		case R.id.btn_net_image:
			NetImageActivity.actionStart(this, 0);
			break;
		/**
		 * ormlite数据库
		 */
		case R.id.btn_test_ormlite:
			TestOrmliteActivity.actionStart(this, 0);
			break;
		/**
		 * progress
		 */
		// round_progress
		case R.id.btn_round_progress:
			RoundProgressActivity.actionStart(this, 0);
			break;
		/**
		 * TelephonyManager
		 */
		case R.id.btn_telephony_manager:
			TelephonyManagerActivity.actionStart(this, 0);
			break;
		/**
		 * WaterRipple
		 */
		case R.id.btn_water_ripple:
			WaterRippleActivity.actionStart(this, 0);
			break;
		/**
		 * wheel
		 */
		// 时间选择，TimeSelect
		case R.id.btn_time_select:
			TimeSelectActivity.actionStart(this, 0);
			break;
		/**
		 * wlan
		 */
		case R.id.btn_wlan:
			WLanActivity.actionStart(this, 0);
			break;
		}
	}
}
