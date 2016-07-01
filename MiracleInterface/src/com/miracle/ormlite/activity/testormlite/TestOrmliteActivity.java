package com.miracle.ormlite.activity.testormlite;

import com.miracle.activity.R;
import com.miracle.ormlite.db.bean.GluValue;
import com.miracle.ormlite.db.dao.GluValueDao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TestOrmliteActivity extends Activity {
	private TextView mTextView;
	// 数据库参数
	private GluValueDao mGluValueDao;

	GluValue gluValue;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, TestOrmliteActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ormlite_activity_test_ormlite);
		initDB();

		mTextView = (TextView) findViewById(R.id.txt_test_ormlite);

		mGluValueDao.deleteAll();
		mTextView.append("\n#######Begin to Insert#########\n");
		insertTest();
		mGluValueDao.display(mTextView);
		mTextView.append("\n#######Begin to Update#########\n");
		gluValue.setUsername("update");
		mGluValueDao.update(gluValue);
		mGluValueDao.display(mTextView);
		mTextView.append("\n#######Begin to Delete#########\n");
		mGluValueDao.delete("name2");
		mGluValueDao.display(mTextView);
		mTextView.append("\n#######Begin to Search#########\n");
		mTextView.append(mGluValueDao.search("name1").toString());
	}

	// 初始化Ormlite数据库
	private void initDB() {
		mGluValueDao = new GluValueDao(this);
	}

	/**
	 * 插入值测试
	 */
	private void insertTest() {
		for (int i = 0; i < 5; i++) {
			gluValue = new GluValue();
			gluValue.setUsername("name" + i);
			gluValue.setPassword("test_pass " + i);
			mGluValueDao.insert(gluValue);
		}
	}
}
