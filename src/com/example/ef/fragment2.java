package com.example.ef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import newAdapter.*;
//指向message的界面

public class fragment2 extends Fragment {
	private TextView message;
	private TextView person;
	private PopupWindow mPopWindow;
	private ImageView b_more;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.activity_message, null);
		message = (TextView) view.findViewById(R.id.message_message);
		person = (TextView) view.findViewById(R.id.message_person);
		b_more = (ImageView) view.findViewById(R.id.message_more);
		b_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow();
			}
		});
		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changefragment(1);
			}
		});
		person.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changefragment(2);
			}
		});
		changefragment(1);
		return view;
	}
	private void showPopupWindow() {
		// 关联弹窗的layout，并设置在哪个页面弹出
		View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.more, null);

		// 关联弹窗
		mPopWindow = new PopupWindow(contentView);

		// 设置弹窗的高宽
		mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

		// 设置点击弹窗以外的地方是否会响应点击
		mPopWindow.setOutsideTouchable(false);

		// 设置点击其他地方是否会关闭弹窗
		mPopWindow.setFocusable(true);

		// 获取弹窗的焦点
		TextView tv1 = (TextView) contentView.findViewById(R.id.pop_addfriend);
		TextView tv2 = (TextView) contentView.findViewById(R.id.pop_managefriend);
		tv1.setText("设备信息");
		tv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), Userhardware.class);
				startActivity(intent);
			mPopWindow.dismiss();
			}
		});
		tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ManagefriendActivity.class);
				startActivity(intent);
				mPopWindow.dismiss();
			}
		});

		// 显示弹窗
		mPopWindow.showAsDropDown(b_more);
	}
	public void changefragment(final int c) { // 根据c的值来判定转换为哪个界面
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (c) {
				case 1: {
					FragmentManager manager = getFragmentManager();
					FragmentTransaction transaction = manager.beginTransaction();
					fragment2_1 fragment2_1 = new fragment2_1();
					transaction.replace(R.id.message_contain, fragment2_1);
					transaction.commit();
					message.setTextColor(Color.parseColor("#127C56"));
					message.setBackgroundResource(R.drawable.messagehead2);
					person.setTextColor(Color.parseColor("#ffffff"));
					person.setBackgroundResource(R.drawable.messagehead1);
				}
					break;
				case 2: {
					FragmentManager manager = getFragmentManager();
					FragmentTransaction transaction = manager.beginTransaction();
					fragment2_2 fragment2_2 = new fragment2_2();
					transaction.replace(R.id.message_contain, fragment2_2);
					transaction.commit();
					message.setTextColor(Color.parseColor("#ffffff"));
					message.setBackgroundResource(R.drawable.messagehead1);
					person.setTextColor(Color.parseColor("#127C56"));
					person.setBackgroundResource(R.drawable.messagehead2);
				}
					break;
				}
			}
		});
	}
}
