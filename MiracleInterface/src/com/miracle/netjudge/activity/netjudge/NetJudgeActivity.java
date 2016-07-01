package com.miracle.netjudge.activity.netjudge;

import com.miracle.activity.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * 获取网络状态，要加权限
 * 
 * @author Administrator
 *
 */
public class NetJudgeActivity extends Activity {

	private static final String TAG = "NetJudgeActivity";
	private TextView mTextView;
	private boolean isNetworkCon;
	private boolean isMobile;
	private boolean isNetworkAva;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, NetJudgeActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.netjudge_activity_net_judge);
		initIntentFilter();
		isNetworkCon = isNetworkConnected(this);
		mTextView = (TextView) findViewById(R.id.txt_net_judge);
		mTextView.setText("xxxx");
		isMobile = isMobileConnected(this);
		isNetworkAva = isNetworkAvailable(this);
	}

	// 注册广播接收器
	private void initIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectionReceiver, intentFilter);
	}

	/**
	 * 检查当前网络是否可用
	 * 
	 * @param context
	 * @return
	 */

	public boolean isNetworkAvailable(Activity activity) {
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					Log.e(TAG, i + "===状态===" + networkInfo[i].getState());
					Log.e(TAG, i + "===类型===" + networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 判断是否有网络连接
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 判断wifi网络是否能用
	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 判断MOBILE网络是否可用
	public boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// 获取当前网络连接的类型信息
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	// 判断当前网络是wifi还是3g，用户的体现性在这里了，wifi就可以建议下载或者在线播放。
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	// 当网络状态改变时系统会发送广播，在此处接收
	BroadcastReceiver connectionReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				Log.e(TAG, "unconnect...");
				// unconnect network
				mTextView.setText(" unconnect network\n" + " isNetworkConnected = " + isNetworkCon
						+ "\n isMobileConnected = " + isMobile + "\n isNetworkAvailable = " + isNetworkAva);
			} else {
				Log.e(TAG, "connect...");
				// connect network
				mTextView.setText(" connect network \n" + " isNetworkConnected = " + isNetworkCon
						+ "\n isMobileConnected = " + isMobile + "\n isNetworkAvailable = " + isNetworkAva);
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (connectionReceiver != null) {
			unregisterReceiver(connectionReceiver);
		}
	}

}
