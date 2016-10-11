package com.miracle.commonlist.activity.dialogset;

import java.lang.reflect.Field;

import com.miracle.activity.R;
import com.miracle.activity.R.drawable;
import com.miracle.activity.R.id;
import com.miracle.activity.R.layout;
import com.miracle.commonlist.activity.listview.ListViewActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MyDialogActivity extends Activity {

	Button b1, b2, b3, b4, b5, b6;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, MyDialogActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
		// 确定和取消按钮
		b1 = (Button) findViewById(R.id.btn1);
		b1.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				AlertDialog ad = new AlertDialog.Builder(MyDialogActivity.this).create();
				ad.setTitle("标题1");
				ad.setIcon(R.drawable.ic_launcher);
				ad.setMessage("我是消息内容");
				ad.setButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				ad.setButton2("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
				ad.show();
			}
		});

		// 动态创建
		b3 = (Button) findViewById(R.id.btn3);
		b3.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// 创建布局
				LayoutInflater inflater = MyDialogActivity.this.getLayoutInflater();
				View twoEditText = inflater.inflate(R.layout.twoedittext, null);
				// Activity.findViewById最终还是会调用View.findViewById
				// 因为在Activity的onCreate中一定会先setContentView的
				final EditText e1 = (EditText) twoEditText.findViewById(R.id.e1);
				final EditText e2 = (EditText) twoEditText.findViewById(R.id.e2);
				e1.setText("e1");
				e2.setText("e2");
				// 创建对话框
				AlertDialog ad = new AlertDialog.Builder(MyDialogActivity.this).create();
				ad.setView(twoEditText);// 将view填充至对话框中
				ad.setTitle("标题1");
				ad.setIcon(R.drawable.ic_launcher);
				ad.setMessage("动态创建的内容！");
				ad.setButton("取值", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.v("tag", e1.getText().toString() + e2.getText());

					}
				});
				ad.show();

			}
		});

		// 单选框
		b4 = (Button) findViewById(R.id.btn4);
		b4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String[] s = { "a", "b", "c", "d" };
				AlertDialog ad = new AlertDialog.Builder(MyDialogActivity.this)
						.setSingleChoiceItems(s, 1, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.v("tag", "您选中了" + s[which]);
						dialog.dismiss();

					}
				}).create();
				ad.show();

			}
		});

		// 复选框
		b5 = (Button) findViewById(R.id.btn5);
		b5.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				final String[] s = { "a", "b", "c", "d" };
				final AlertDialog ad = new AlertDialog.Builder(MyDialogActivity.this)
						.setMultiChoiceItems(s, null, new OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						Log.v("tag", "选中了第" + which + ",值为" + isChecked);

					}

				}).create();
				// 全选
				final boolean[] bArray = new boolean[] { true, false, true, true };
				ad.setButton("全选", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						ListView lv = ad.getListView();
						for (int i = 0; i < lv.getCount(); i++) {
							lv.setItemChecked(i, true);
						}
						try {
							Field f = ad.getClass().getSuperclass().getDeclaredField("mShowing");
							f.setAccessible(true);
							f.set(ad, false);
						} catch (Exception e) {

						}
					}
				});
				// 反选
				ad.setButton2("反选", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SparseBooleanArray sb = ad.getListView().getCheckedItemPositions();
						ListView lv = ad.getListView();
						for (int i = 0; i < lv.getCount(); i++) {
							lv.setItemChecked(i, !sb.get(i));
						}
						try {
							Field f = ad.getClass().getSuperclass().getDeclaredField("mShowing");
							f.setAccessible(true);
							f.set(ad, false);
						} catch (Exception e) {

						}
					}
				});
				// 取值
				ad.setButton3("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						ListView lv = ad.getListView();
						for (int i = 0; i < lv.getCount(); i++) {
							Log.v("tag", "id:" + lv.getAdapter().getItemId(i) + "item:" + lv.getAdapter().getItem(i));
						}
						try {
							Field f = ad.getClass().getSuperclass().getDeclaredField("mShowing");
							f.setAccessible(true);
							f.set(ad, true);
							ad.dismiss();
						} catch (Exception e) {

						}

					}

				});
				ad.show();
				// 一定要show完之后在初始化
				for (int i = 0; i < bArray.length; i++) {
					ad.getListView().setItemChecked(i, bArray[i]);
				}
			}
		});

		// 普通列表
		b6 = (Button) findViewById(R.id.btn6);
		b6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String[] s = { "q", "w", "s", "d" };
				AlertDialog ad = new AlertDialog.Builder(MyDialogActivity.this)
						.setItems(s, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.v("tag", "您选中了" + s[which]);
						dialog.dismiss();

					}
				}).create();
				ad.show();
			}

		});
	}
}
