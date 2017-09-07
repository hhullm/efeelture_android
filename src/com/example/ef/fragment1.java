package com.example.ef;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;

//指向main界面的类
public class fragment1 extends Fragment {
	MainActivity main = new MainActivity();
	private int b = main.a;
	private ImageButton b_robot, b_light, b_air, b_message,b_music,b_sport,b_note,b_more;
	private PopupWindow mPopWindow;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.activity_main2, null);
		b_robot = (ImageButton) view.findViewById(R.id.imageButton8);
		b_air = (ImageButton) view.findViewById(R.id.main_windbutton);
		b_light = (ImageButton) view.findViewById(R.id.main_imageButton3);
		b_music = (ImageButton) view.findViewById(R.id.imageButton7);
		b_sport = (ImageButton) view.findViewById(R.id.imageButton9);
		b_note = (ImageButton) view.findViewById(R.id.imageButton11);
		b_note.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.setButtonFocusChanged(b_note);
				Intent intent = new Intent(getActivity(), CalendarActivity.class);
				startActivity(intent);

			}
		});
		b_sport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.setButtonFocusChanged(b_sport);
				Intent intent = new Intent(getActivity(), Pedometer.class);
				startActivity(intent);

			}
		});
		b_music.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.setButtonFocusChanged(b_music);
				Intent intent = new Intent(getActivity(), MainActivity_music.class);
				startActivity(intent);

			}
		});
		b_robot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.setButtonFocusChanged(b_robot);
				Intent intent = new Intent(getActivity(), CarActivity.class);
				startActivity(intent);

			}
		});
		b_light.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.setButtonFocusChanged(b_light);
				Intent intent = new Intent(getActivity(), LightNew.class);
				startActivity(intent);

			}
		});
		b_air.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.setButtonFocusChanged(b_air);
				Intent intent = new Intent(getActivity(), Air_new.class);
				startActivity(intent);

			}
		});
		b_more = (ImageButton) view.findViewById(R.id.main_imagebutton2);
		b_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow();
			}
		});
		ImageButton button = (ImageButton) view.findViewById(R.id.main_imagebutton1);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 按钮按下，将抽屉打开
				MainActivity.mDrawerLayout.openDrawer(Gravity.LEFT);

			}
		});

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

}