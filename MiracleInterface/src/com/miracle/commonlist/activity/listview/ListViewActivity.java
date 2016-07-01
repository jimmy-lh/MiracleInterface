package com.miracle.commonlist.activity.listview;

import com.miracle.activity.R;
import com.miracle.commonlist.adapter.BaseAdapterItem;
import com.miracle.commonlist.adapter.test.TestSetAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ListViewActivity extends Activity implements OnClickListener {
	private TestSetAdapter mSetAdapter;
	private Button mButtonAdd;

	// 时间
	private int[] mImageViewOnOff = { R.drawable.listview_item_on, R.drawable.listview_item_off };
	// 数组赋值（如下）则开始就有item，为空（如：new String[]{}）则刚打开listview界面没有item,两个数组大小需一致,否则出错
	private String[] mTextViewTime = new String[] { "12", "3" };
	private String[] mTextViewFrequency = new String[] { "23", "32" };

	private String hour = "0", minute = "0", frequency = "";
	private int num = 0;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, ListViewActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commonlist_activity_listview);
		view();
		// getWindow().getDecorView()获取当前的view
		initListView(getWindow().getDecorView());
	}

	private void view() {
		mButtonAdd = (Button) findViewById(R.id.btn_listview_add_item);
		mButtonAdd.setOnClickListener(this);
	}

	private void initListView(View view) {
		// 关于历史数据的listView的配置
		mSetAdapter = new TestSetAdapter(view, this, R.id.lv_listview, mImageViewOnOff, mTextViewTime,
				mTextViewFrequency);
	}

	private void addListViewItem() {
		hour = String.valueOf(num);
		minute = String.valueOf(num);
		frequency = String.valueOf(num);
		// 获取最新数据加入listView
		mSetAdapter.mList.add(0,
				new BaseAdapterItem(mImageViewOnOff[0], hour + ":" + minute, "设置频率:" + "1天" + frequency + "次"));
		// 刷新listView
		mSetAdapter.mAdapter.notifyDataSetChanged();
		num++;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_listview_add_item:
			Toast.makeText(this, "xxx", Toast.LENGTH_SHORT).show();
			addListViewItem();
			break;
		default:
			break;
		}
	}

}
