package com.miracle.commonlist.adapter.test;

import java.util.List;

import com.miracle.activity.R;
import com.miracle.commonlist.adapter.BaseAdapterItem;
import com.miracle.commonlist.adapter.SBaseAdapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator Adapter类
 */
public class TestBaseAdapter extends SBaseAdapter {

	public TestBaseAdapter(Context context, List<?> data) {
		super(context, data, R.layout.commonlist_listview_item);
	}

	// 设置最大增加Item数
	@Override
	public int getCount() {
		if (data.size() < 20)
			return data.size();
		else
			return 20;
	}

	@Override
	protected void newView(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.mLayoutItem = (LinearLayout) convertView.findViewById(R.id.layout_remind_add);
		holder.mImageView = (ImageView) convertView.findViewById(R.id.img_remind_add_on_off);
		holder.mTextView1 = (TextView) convertView.findViewById(R.id.txt_remind_time);
		holder.mTextView2 = (TextView) convertView.findViewById(R.id.txt_remind_frequency);
		convertView.setTag(holder);
	}

	@Override
	protected void holderView(View convertView, Object itemObject, int position) {
		ViewHolder holder = (ViewHolder) convertView.getTag();

		final BaseAdapterItem lBaseAdapterItem = (BaseAdapterItem) itemObject;
		// 重置图片和文本
		holder.mImageView.setImageResource(lBaseAdapterItem.getImageView1());
		holder.mTextView1.setText(lBaseAdapterItem.getTextView1());
		holder.mTextView2.setText(lBaseAdapterItem.getTextView2());

		setWidgetVisibility(holder);

		// item按键事件
		holder.mLayoutItem.setOnClickListener(new onHistoryDeleteListener(position, holder));
		holder.mLayoutItem.setOnLongClickListener(new OnDeleteItem(position, holder));

		// 设置Item背景色
		// super.setItemColor(context.getResources().getColor(R.color.Gainsboro),
		// context.getResources().getColor(R.color.LightGrey), true);
	}

	/**
	 * 设置控件是否显示
	 * 
	 * @param holder
	 */
	private void setWidgetVisibility(ViewHolder holder) {
		// holder.mbtn1.setVisibility(View.VISIBLE);
	}

	// 短按事件响应
	class onHistoryDeleteListener implements OnClickListener {
		private int position;
		private ViewHolder holder;
		private boolean isOnOff = false;

		public onHistoryDeleteListener(int position, ViewHolder holder) {
			this.position = position;
			this.holder = holder;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_remind_add:
				if (isOnOff == true) {
					holder.mImageView.setImageResource(R.drawable.listview_item_on);
					isOnOff = false;
				} else {
					holder.mImageView.setImageResource(R.drawable.listview_item_off);
					isOnOff = true;
				}
				break;
			default:
				break;
			}
		}
	}

	// 长按事件响应
	class OnDeleteItem implements OnLongClickListener {
		private int position;
		private ViewHolder holder;

		public OnDeleteItem(int position, ViewHolder holder) {
			this.position = position;
			this.holder = holder;
		}

		@Override
		public boolean onLongClick(View v) {
			switch (v.getId()) {
			case R.id.layout_remind_add:
				// 删除对应listView 的 Item项
				data.remove(position);
				notifyDataSetChanged();
				break;
			default:
				break;
			}
			return true;
		}
	}

	class ViewHolder {
		LinearLayout mLayoutItem;
		ImageView mImageView;
		TextView mTextView1;
		TextView mTextView2;
	}
}
