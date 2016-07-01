/*
 * TimeSelectView.java
 * Create at 2015-8-26 上午9:30:15
 */
package com.miracle.wheel.activity.timeselect;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

public class TimeSelectView extends View implements OnTouchListener, SelectListener {

	private boolean isMeasure = false;
	private Bitmap bmp;
	private Path path;// 裁减路径
	private Paint textPaint, bgPaint;
	private float viewW, viewH, cycleR;

	private Paint bmpPaint, linePaint;

	private ArrayList<SelectItem> items = new ArrayList<SelectItem>();

	private float startX, startY;

	private int maxItem = 5;

	private float defaultTextSize = 80;
	private double down;
	private double[] beforeTouchTime = new double[2];

	private int addAlpha = 30;
	SelListener sl;
	OnFilingListener fl;

	// public TimeSelectView(Context context, AttributeSet attrs, int
	// defStyleAttr, int defStyleRes) {
	// super(context, attrs, defStyleAttr, defStyleRes);
	// initPaint();
	// }

	private void initPaint() {
		bmpPaint = new Paint();
		bmpPaint.setAntiAlias(true);
		bmpPaint.setStyle(Paint.Style.FILL);

		textPaint = new Paint();
		textPaint.setColor(Color.rgb(120, 103, 173));
		textPaint.setTypeface(Typeface.DEFAULT);

		bgPaint = new Paint();
		bgPaint.setAntiAlias(true);
		bgPaint.setStyle(Paint.Style.FILL);
		bgPaint.setColor(Color.rgb(227, 222, 242));

		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStyle(Paint.Style.FILL);
		linePaint.setColor(Color.rgb(120, 103, 173));

		roundPaint = new Paint();
		roundPaint.setColor(Color.rgb(120, 103, 173)); // 设置圆环的颜�?
		roundPaint.setStyle(Paint.Style.STROKE); // 设置空心
		roundPaint.setStrokeWidth(2); // 设置圆环的宽�?
		roundPaint.setAntiAlias(true); // 消除锯齿

	}

	public TimeSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initPaint();
	}

	public TimeSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	public TimeSelectView(Context context) {
		super(context);
		initPaint();
	}

	float windowWidth, windowHeight;

	@Override
	protected void onLayout(boolean changed, int mleft, int top, int right, int bottom) {
		super.onLayout(changed, mleft, top, right, bottom);
		if (!isMeasure) {
			setOnTouchListener(this);
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

			windowWidth = wm.getDefaultDisplay().getWidth();
			windowHeight = wm.getDefaultDisplay().getHeight();

			viewW = getWidth();
			viewH = getHeight();
			cycleR = viewW > viewH ? viewH / 2 : viewW / 2;

			path = new Path();
			path.addCircle(cycleR, cycleR, cycleR, Path.Direction.CCW);

			itemHeight = cycleR * 2 / maxItem;
			defaultTextSize = (float) ((cycleR / maxItem) + 20);
			left = cycleR * 3 / 4;

			// fl = new OnFilingListener() {
			//
			// @Override
			// public void onFiling() {
			// onFill();
			// }
			// };
			isMeasure = true;
		}

	}

	void onFill(final float margin, final float time, final MotionEvent event) {

		t = new Thread(new Runnable() {

			@Override
			public void run() {
				isRun = true;
				float mTime = Math.abs(time) * 10;
				float percent = Math.abs(margin) / mTime;
				float dY = percent * itemHeight;
				float dTime = mTime * 10 * cycleR / (windowWidth * itemHeight);
				float temp = percent * itemHeight / (time * 10);
				for (; mTime > 0;) {
					if (isRun) {
						try {
							dY = temp * mTime;
							if (margin > 0)
								sortItem(-dY);
							else
								sortItem(dY);
							mTime -= dTime;
							myHandler.sendEmptyMessage(1);
							Thread.sleep(10);

							if (items.get(0).rect.top - itemHeight > cycleR
									|| items.get(items.size() - 1).rect.top + itemHeight * 2 < cycleR) {
								break;
							}
						} catch (InterruptedException e) {
						}
					} else {
						break;
					}
				}

				isRun = false;
				onUp(event);
			}
		});
		t.start();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	Paint roundPaint;

	private void drawOutCycle(Canvas canvas, float centre, float radius) {
		canvas.drawCircle(centre, centre, radius, roundPaint); // 画出圆环

	}

	/**
	 * 设置文本的位置，大小，颜色
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.clipPath(path);
		canvas.drawCircle(cycleR, cycleR, cycleR, bgPaint);
		int size = items.size();
		float sX = cycleR * 3 / 4;
		float sY = cycleR - itemHeight / 2;
		float perc = (float) (cycleR / (2 * windowWidth) * 0.5);
		float stopX = (float) (Math.sqrt(cycleR * cycleR - (sY - cycleR) * (sY - cycleR)) + cycleR) - 2;
		for (int i = 0; i < size; i++) {
			if (items.get(i).isVisible(cycleR * 2)) {
				SelectItem item = items.get(i);
				if (item.rect.top + itemHeight / 2 - cycleR <= 0) {
					textPaint.setTextSize((float) (defaultTextSize + item.rect.top * perc));
				} else {
					textPaint.setTextSize((float) (defaultTextSize + (cycleR * 2 - item.rect.bottom) * perc));
				}
				textPaint.setAlpha((int) textPaint.getTextSize() + addAlpha);
				canvas.drawText(item.text, item.rect.left,
						item.rect.top + itemHeight * 1 / 3 + textPaint.getTextSize() / 2, textPaint);
			}
		}

		canvas.drawLine(sX, sY, stopX, sY, linePaint);
		canvas.drawLine(sX, sY + itemHeight, stopX, sY + itemHeight, linePaint);

		Path path = new Path();
		path.moveTo(cycleR / 4, cycleR - (itemHeight / 3));
		path.lineTo(cycleR / 4, cycleR + (itemHeight / 3));
		path.lineTo((float) (cycleR * 0.7), cycleR);
		path.close();
		canvas.drawPath(path, linePaint);
		drawOutCycle(canvas, cycleR, cycleR - 2);
	}

	float itemHeight;

	float moveLength, margin;

	private void sortItem(float move) {
		int size = items.size();
		float right, top;

		for (int i = 0; i < size; i++) {
			top = items.get(i).rect.top + move;
			right = getWidth();
			items.get(i).setRect(new RectF(left, top, right, top + itemHeight));
		}

	}

	float left;

	public void init(final ArrayList<String> lst) {
		if (lst == null || lst.size() == 0) {
			Toast.makeText(getContext(), "数据不能为空", 0).show();
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (!isMeasure) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
				}
				setItems(lst);
				setMid();
			}
		}).start();
	}

	private void setItems(ArrayList<String> lst) {

		int length = lst.size();
		for (int i = 0; i < length; i++) {
			SelectItem item = new SelectItem(new RectF(), lst.get(i));
			items.add(item);
		}
		int size = items.size();
		if (size < maxItem && size > 0) {
			for (int i = size / 2; i >= 0; i--) {
				float top = cycleR - (size / 2 - i) * itemHeight - (itemHeight / 2);
				float right = (float) (Math.sqrt(cycleR * cycleR - (top - cycleR) * (top - cycleR)) + cycleR);
				items.get(i).setRect(new RectF(left, top, right, top + itemHeight));
			}

			for (int i = size / 2 + 1; i < size; i++) {
				float top = items.get(i - 1).getRect().top + itemHeight;
				float right = (float) (Math
						.sqrt(cycleR * cycleR - (top + itemHeight - cycleR) * (top + itemHeight - cycleR)) + cycleR);
				items.get(i).setRect(new RectF(left, top, right, top + itemHeight));
			}

		} else {
			float top;
			float right;

			for (int i = 0; i < size; i++) {
				top = i * itemHeight;
				right = getWidth();
				items.get(i).setRect(new RectF(left, top, right, top + itemHeight));
			}

		}
		myHandler.sendEmptyMessage(1);
	}

	@Override
	protected void onDetachedFromWindow() {
		if (bmp != null) {
			bmp.recycle();
			bmp = null;
		}
		if (t != null && t.isAlive()) {
			isRun = false;
			t = null;
		}
		super.onDetachedFromWindow();

	}

	Thread t;
	boolean isRun = false;
	boolean isMovew;
	private float downY;

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {

		if (isRun) {
			isRun = false;
			return true;
		}

		// if (!isInTouchArea(event)) {
		// onUp(event);
		// return true;
		// }
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!isInTouchArea(event)) {
				startY = -1;
			} else {
				startY = event.getY();
			}
			downY = startY;
			down = System.currentTimeMillis();
			isMovew = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY != -1) {
				if (Math.abs((event.getY() - startY)) >= itemHeight / 50) {
					isMovew = true;
					sortItem(event.getY() - startY);
					invalidate();
					startY = event.getY();
					double temp = beforeTouchTime[0];
					beforeTouchTime[0] = System.currentTimeMillis();
					beforeTouchTime[1] = temp;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (startY != -1) {
				if (!isMovew || (System.currentTimeMillis() - down) >= 500) {
					if ((System.currentTimeMillis() - down) < 200 && !isMovew) {
						if (sl != null && getItem(event) != null) {
							sl.onClick(getItem(event).text);
						}
						if (isInTouchArea(event)) {
							select(event);
						}

						if (getItem(event) == null || !isInTouchArea(event)) {
							if (!isRun) {
								setMid();
							}
						}

						return true;
					}
					onUp(event);
				} else {
					if (items.get(0).rect.top + itemHeight / 2 > cycleR
							|| items.get(items.size() - 1).rect.top + itemHeight / 2 < cycleR) {
						onUp(event);
					} else {
						onFill(downY - event.getY(), (float) (System.currentTimeMillis() - down), event);
					}
				}
			}
			break;
		default:
			break;
		}
		return true;
	}

	private void onUp(MotionEvent event) {
		// else if (isMovew) {
		// setScroll(downY - event.getY(), System.currentTimeMillis() -
		// down);
		// }
		if (items.get(0).rect.top + itemHeight / 2 > cycleR) {
			t = new Thread(new Runnable() {

				@Override
				public void run() {
					isRun = true;
					for (int i = (int) items.get(0).rect.top; i > cycleR - itemHeight / 2; i -= itemHeight / 10) {
						try {
							if (isRun) {
								sortItem(-itemHeight / 10);
								Thread.sleep(10);
								myHandler.sendEmptyMessage(1);
							} else {
								break;
							}
						} catch (InterruptedException e) {
						}
					}
					isRun = false;
					setMid();
				}
			});
			t.start();
		} else if (items.get(items.size() - 1).rect.top + itemHeight / 2 < cycleR) {
			t = new Thread(new Runnable() {

				@Override
				public void run() {
					isRun = true;
					for (int i = (int) items.get(items.size() - 1).rect.top; i < cycleR
							- itemHeight / 2; i += itemHeight / 10) {
						try {
							if (isRun) {
								sortItem(itemHeight / 10);
								Thread.sleep(10);
								myHandler.sendEmptyMessage(1);
							} else {
								break;
							}
						} catch (InterruptedException e) {
						}
					}
					isRun = false;
					setMid();
				}
			});
			t.start();
		} else {
			startY = event.getY();
			if (!isRun) {
				setMid();
			}
		}
	}

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			invalidate();
		}

	};

	private void select(MotionEvent event) {
		int size = items.size();
		for (int i = 0; i < size; i++) {
			if (items.get(i).isVisible(cycleR * 2)) {
				if (items.get(i).isInRect(event.getX(), event.getY())) {
					margin = items.get(i).rect.top - (cycleR - itemHeight / 2);
					if (Math.abs(margin) >= itemHeight / 2) {

						t = new Thread(new Runnable() {

							@Override
							public void run() {
								isRun = true;
								for (float i = Math.abs(margin); i > 0; i -= Math.abs(margin) / 10) {
									try {
										if (isRun) {
											sortItem(-margin / 10);
											Thread.sleep(10);
											myHandler.sendEmptyMessage(1);
										} else {
											break;
										}
									} catch (InterruptedException e) {
									}
								}
								isRun = false;
								setMid();
							}
						});
						t.start();
					}
					break;
				}
			}
		}
	}

	private void setMid() {
		int size = items.size();
		for (int i = 0; i < size; i++) {
			margin = items.get(i).rect.top - (cycleR - itemHeight / 2);
			if (Math.abs(margin) <= itemHeight / 2 && Math.abs(margin) > 0) {

				t = new Thread(new Runnable() {

					@Override
					public void run() {
						isRun = true;
						for (float i = Math.abs(margin); i > Math.abs(margin) / 10; i -= Math.abs(margin) / 10) {
							try {
								if (isRun) {
									sortItem(-margin / 10);
									Thread.sleep(10);
									myHandler.sendEmptyMessage(1);
								} else {
									break;
								}
							} catch (InterruptedException e) {
							}
						}
						margin = 0;
						isRun = false;
					}
				});
				t.start();
				break;
			}

		}
	}

	private void setScroll(final float move, final double moveTime) {
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				isRun = true;
				double threadMoveTime = moveTime / 100;
				float minMargin = (float) (Math.abs(move) / moveTime);
				for (float i = Math.abs(move); i > minMargin; i -= minMargin) {
					try {
						if (isRun) {
							threadMoveTime += moveTime / 100;
							Log.d("", "threadMoveTime  ===>" + threadMoveTime);
							Log.d("", "-move / threadMoveTime  ===>" + (-move / threadMoveTime));
							sortItem((float) (-move / threadMoveTime));
							Thread.sleep(10);
							myHandler.sendEmptyMessage(1);
						} else {
							break;
						}
					} catch (InterruptedException e) {
					}
				}
				isRun = false;
				setMid();
			}
		});
		t.start();
	}

	public SelectItem getItem(MotionEvent event) {
		int size = items.size();
		for (int i = 0; i < size; i++) {
			if (items.get(i).isVisible(cycleR * 2)) {
				if (items.get(i).isInRect(event.getX(), event.getY())) {
					return items.get(i);
				}
			}
		}
		return null;
	}

	private boolean isInTouchArea(MotionEvent event) {
		if (event.getX() >= cycleR * 3 / 4 && event.getX() <= cycleR * 2 && event.getY() > 0
				&& event.getY() < cycleR * 2) {
			return true;
		}
		return false;
	}

	public String getText() {
		int size = items.size();
		for (int i = 0; i < size; i++) {
			if (items.get(i).isVisible(cycleR * 2)) {
				if (items.get(i).isInRect(cycleR, cycleR)) {
					return items.get(i).text;
				}
			}
		}
		return "";
	}

	@Override
	public void onSelectListener(SelListener sl) {
		if (sl != null) {
			this.sl = sl;
		}
	}

}
