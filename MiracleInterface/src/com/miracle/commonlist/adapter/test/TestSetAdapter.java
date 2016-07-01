package com.miracle.commonlist.adapter.test;

import java.util.ArrayList;
import java.util.List;

import com.miracle.activity.R;
import com.miracle.commonlist.adapter.BaseAdapterItem;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

/**
 * @author Administrator 配置参数的历史数据的 listView
 */
public class TestSetAdapter {
	private View mView;
	private Activity mActivity;
	/**
	 * listView 的 id
	 */
	private int lvId;

	/**
	 * listView 的元素集合
	 */
	public List<BaseAdapterItem> mList;

	/**
	 * 菜单界面中只包含了一个ListView。
	 */
	public ListView mListView;

	/**
	 * ListView的适配器。
	 */
	public TestBaseAdapter mAdapter;

	private int[] mImageViewOnOff = { R.drawable.listview_item_on, R.drawable.listview_item_off };

	// 一个img，三个txt
	public TestSetAdapter(View aView, Activity aActivity, int lvId, int[] aImageViewOnOff, String[] aSingleTextView1,
			String[] aSingleTextView2) {
		mView = aView;
		mActivity = aActivity;
		this.lvId = lvId;
		initData(aImageViewOnOff, aSingleTextView1, aSingleTextView2);
	}

	private void initData(int[] aImageViewOnOff, String[] aSingleTextView1, String[] aSingleTextView2) {
		mList = new ArrayList<BaseAdapterItem>();
		for (int i = 0; i < aSingleTextView1.length; i++) {
			BaseAdapterItem lBaseAdapterItem = new BaseAdapterItem(aImageViewOnOff[0], aSingleTextView1[i],
					aSingleTextView2[i]);
			mList.add(lBaseAdapterItem);
		}
		initView();
	}

	/**
	 * 初始化ListView,配置Adapter
	 */
	private void initView() {
		mListView = (ListView) mView.findViewById(lvId);
		mAdapter = new TestBaseAdapter(mActivity, mList);
		mListView.setAdapter(mAdapter);
	}
}
