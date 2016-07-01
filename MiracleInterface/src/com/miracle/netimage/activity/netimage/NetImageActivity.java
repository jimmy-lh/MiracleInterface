package com.miracle.netimage.activity.netimage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.miracle.activity.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * 从网络上获取图片
 * 
 * @author miracle 2016年5月10日
 */
public class NetImageActivity extends Activity {

	/** Called when the activity is first created. */
	String imageUrl = "http://content.52pk.com/files/100623/2230_102437_1_lit.jpg";
	Bitmap bmImg;
	ImageView imView;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, NetImageActivity.class);
		context.startActivity(intent);
	}

	private Future controlThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.netimage_activity_net_image);
		imView = (ImageView) findViewById(R.id.imageView);
		// imView.setImageBitmap(returnBitMap(imageUrl));
		if (mThread == null) {
			mThread = new BitThread();
		}
		// 增加线程控制器,线程没有结束也可以直接终止线程
		ExecutorService transThread = Executors.newSingleThreadExecutor();
		// 自动运行线程
		controlThread = transThread.submit(mThread);

	}

	Thread mThread = null;
	private boolean isExit = false;

	class BitThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (bitMap == null && !isExit) {
				// 需要放在线程里面运行
				bitMap = returnBitMap(imageUrl);
				// 没有获取到图片，则延时
				if (bitMap == null) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			mThread = null;
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			imView.setImageBitmap(bitMap);
		}
	};

	Bitmap bitMap = null;

	/**
	 * HttpURLConnection.connect()需要放在线程中运行，所以此方法需要放在线程中运行
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			// 设置是否从httpUrlConnection读入，默认情况下是true;
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isExit = true;
		// 无论线程现在是否运行中，立刻终止(只是sleep延时退出，但是线程还是会继续运行完，所以要注意终止前必须要有退出线程循环的条件，否则while循环不会退出)
		controlThread.cancel(true);
	}

}
