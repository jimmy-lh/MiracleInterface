/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.miracle.activity.yt2glu;

import java.io.IOException;

import com.miracle.activity.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiveActivity extends SerialPortActivity implements OnClickListener, Runnable {

	private CheckBox mCheckBoxBlood, mCheckBoxValue, mCheckBoxResistance;
	private TextView mTextViewSnValue, mTextViewTempValue, mTextViewTime;
	private ImageView mImageViewStar;
	private View mLayout[] = new View[5];
	private TextView mTextViewGluValue[] = new TextView[5];
	private TextView mTextViewGluValueMg[] = new TextView[5];
	private TextView mTextViewGluValueNum[] = new TextView[5];
	private ImageView mImageViewPaper;
	private static final String TAG = "ReceiveActivity";
	private Unpack myUnpack;
	private static String dataBlood = "5C0304";// 血糖模式
	private static String dataValue = "5C0315";// 值控模式
	private static String dataResistance = "5C0326";// 电阻片模式（无温补）
	private static String sleepBlood = "5C0001";// 血糖模式
	private static String sleepValue = "5C0102";// 值控模式
	private static String sleepResistance = "5C0203";// 电阻片模式（无温补）
	private static int measureMode = 0;// 0:血糖模式；1：值控模式;2:电阻片模式
	private boolean isKeyDown = false;
	private boolean isBlink = false; // 星星闪烁的标志位
	private Thread mThread = null;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, ReceiveActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yt2glu_receive);
		myUnpack = Unpack.getInstance(ReceiveActivity.this);

		initView();
	}

	private void initView() {
		mCheckBoxBlood = (CheckBox) findViewById(R.id.mode_check_blood);
		mCheckBoxValue = (CheckBox) findViewById(R.id.mode_check_value);
		mCheckBoxResistance = (CheckBox) findViewById(R.id.mode_check_resistance);

		mTextViewSnValue = (TextView) findViewById(R.id.txt_sn_value);
		mTextViewTempValue = (TextView) findViewById(R.id.txt_temp_value);
		mTextViewTime = (TextView) findViewById(R.id.txt_time_value);
		mImageViewStar = (ImageView) findViewById(R.id.img_star);

		mLayout[0] = findViewById(R.id.yt2_glu_value_num0);
		mLayout[1] = findViewById(R.id.yt2_glu_value_num1);
		mLayout[2] = findViewById(R.id.yt2_glu_value_num2);
		mLayout[3] = findViewById(R.id.yt2_glu_value_num3);
		mLayout[4] = findViewById(R.id.yt2_glu_value_num4);

		for (int i = 0; i < mLayout.length; i++) {
			mTextViewGluValue[i] = (TextView) mLayout[i].findViewById(R.id.txt_glu_value);
			mTextViewGluValueMg[i] = (TextView) mLayout[i].findViewById(R.id.txt_glu_value_mg);
			mTextViewGluValueNum[i] = (TextView) mLayout[i].findViewById(R.id.txt_glu_value_num);
		}

		mTextViewGluValueNum[1].setText("算法1");
		mTextViewGluValueNum[2].setText("算法2");
		mTextViewGluValueNum[3].setText("算法3");
		mTextViewGluValueNum[4].setText("算法4");

		mImageViewPaper = (ImageView) findViewById(R.id.img_test_paper);

		mCheckBoxBlood.setOnClickListener(this);
		mCheckBoxValue.setOnClickListener(this);
		mCheckBoxResistance.setOnClickListener(this);
	}

	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {
				int i = 0, j = 0;
				byte[] buf = buffer;
				while (i < size) {
					if (buf[j] == 53) {
						buf = myUnpack.bufShifting(buffer, size, j);
						myUnpack.setData(buf);
						j = 0;

						Message msg = new Message();
						msg.what = myUnpack.step;
						mHandler.sendMessage(msg);
						// for (int k = 0; k < buf.length; k++) {
						// if (buf[k] != 0)
						// Log.e(TAG, "...." + String.valueOf(buf[k]) + ".xxx");
						// }
					}
					i++;
					j++;
				}
			}
		});
	}

	private String myGetString(int id) {
		return getResources().getString(id);
	}

	@Override
	public void run() {
		while (isBlink) {
			Message msg = new Message();
			msg.what = Unpack.GAIN_STARBLINK;
			mHandler.sendMessage(msg);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mThread = null;
	}

	// 收到数据后对应处理
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Unpack.GAIN_INSERT:
				mTextViewTempValue.setText(String.valueOf(myUnpack.temp));
				break;
			case Unpack.GAIN_GETSN:
				mTextViewSnValue.setText(String.valueOf(myUnpack.sn));
				break;
			// 获取倒计时
			case Unpack.GAIN_COUNTDOWN:
				// 开始滴血测量就打开线程，是star闪烁
				if (mThread == null) {
					mThread = new Thread(ReceiveActivity.this);
				}
				if (!mThread.isAlive()) {
					isBlink = true;
					mThread.start();
				}
				break;
			// 收到读取模式命令，则发送当前模式数据
			case Unpack.GAIN_GETMODE:
				// 收到读取模式命令，则发送当前模式数据
				// sendDataMode();
				break;
			// 收到测量完成数值，发送命令，休眠血糖小板
			case Unpack.GAIN_GETVALUE:
				// 保留两位小数
				mTextViewGluValue[myUnpack.value_count].setText(
						String.valueOf((float) Math.round((myUnpack.value[myUnpack.value_count] / 18.0) * 100) / 100));
				mTextViewGluValueMg[myUnpack.value_count].setText(String.valueOf(myUnpack.value[myUnpack.value_count]));
				// 收到测量完成数值，发送命令，休眠血糖小板
				// sendDataSleep();
				break;
			// 获取时间
			case Unpack.GAIN_GETTIME:
				mTextViewTime.setText(String.valueOf(myUnpack.time));
				isBlink = false;
				break;
			case Unpack.GAIN_STARBLINK:
				if (mImageViewStar.getVisibility() == View.INVISIBLE) {
					mImageViewStar.setVisibility(View.VISIBLE);
				} else {
					mImageViewStar.setVisibility(View.INVISIBLE);
				}
				break;
			default:
				// Toast.makeText(ReceiveActivity.this, "step=" + myUnpack.step
				// + ",错误!", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	/**
	 * 根据测量模式发送不同的数据
	 */
	private void sendDataMode() {
		switch (measureMode) {
		case 0:
			sendData(dataBlood);
			break;
		case 1:
			sendData(dataValue);
			break;
		case 2:
			sendData(dataResistance);
			break;
		}
	}

	/**
	 * 收到数据后，根据不同模式发送不同数据，让血糖板睡眠
	 */
	private void sendDataSleep() {
		switch (measureMode) {
		case 0:
			sendData(sleepBlood);
			break;
		case 1:
			sendData(sleepValue);
			break;
		case 2:
			sendData(sleepResistance);
			break;
		}
	}

	// 发送数据
	private void sendData(String data) {
		try {
			mOutputStream.write(data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mode_check_blood:
			if (mCheckBoxBlood.isChecked()) {
				checkNoChecked();
				mCheckBoxBlood.setChecked(true);
			} else {
				mCheckBoxBlood.setChecked(true);
			}
			measureMode = 0;
			break;

		case R.id.mode_check_value:
			if (mCheckBoxValue.isChecked()) {
				checkNoChecked();
				mCheckBoxValue.setChecked(true);
			} else {
				mCheckBoxValue.setChecked(true);
			}
			measureMode = 1;
			break;

		case R.id.mode_check_resistance:
			if (mCheckBoxResistance.isChecked()) {
				checkNoChecked();
				mCheckBoxResistance.setChecked(true);
			} else {
				mCheckBoxResistance.setChecked(true);
			}
			measureMode = 2;
			break;

		default:
			break;
		}
	}

	// 设置所有checkBox为没被选择
	private void checkNoChecked() {
		mCheckBoxBlood.setChecked(false);
		mCheckBoxValue.setChecked(false);
		mCheckBoxResistance.setChecked(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 插入试纸对应的按键响应
		if (keyCode == KeyEvent.KEYCODE_MUTE && !isKeyDown) {
			isKeyDown = true;
			mImageViewPaper.setVisibility(ImageView.VISIBLE);
			mTextViewTempValue.setText("");
			mTextViewSnValue.setText("");
			mTextViewTime.setText("");
			for (int i = 0; i < mTextViewGluValue.length; i++) {
				mTextViewGluValue[i].setText("");
				mTextViewGluValueMg[i].setText("");
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// 拔出试纸对应的按键响应
		if (keyCode == KeyEvent.KEYCODE_MUTE) {
			isKeyDown = false;
			mImageViewPaper.setVisibility(ImageView.INVISIBLE);
			mImageViewStar.setVisibility(View.INVISIBLE);
		}
		return super.onKeyUp(keyCode, event);
	}

}
