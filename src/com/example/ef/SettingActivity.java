package com.example.ef;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class SettingActivity extends Activity {
	private Button b_login, b_change_password, b_userhardware;
	private ImageButton b_back;
	public static SettingActivity instance3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		instance3 = this;
		b_back = (ImageButton) findViewById(R.id.setting_backbutton);
		b_login = (Button) findViewById(R.id.setting_button8);
		b_change_password = (Button) findViewById(R.id.setting_button3);
		b_userhardware = (Button) findViewById(R.id.setting_button9);
		b_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingActivity.this.finish();

			}
		});
		b_login.setOnClickListener(new OnClickListener() { // 回到登陆界面
			@Override
			public void onClick(View v) {
				SharedPreferences islogin = getSharedPreferences("islogin", Context.MODE_PRIVATE);
				Editor editor2 = islogin.edit();
				editor2.putString("islogin", "0");
				editor2.commit();
				MainActivity.instance1.finish();
				Intent intent2 = new Intent(SettingActivity.this, LoginActivity.class);
				startActivity(intent2);
				SettingActivity.this.finish();
			}
		});
		b_change_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(SettingActivity.this, Change_passwordActivity.class);
				startActivity(intent2);
			}
		});
		b_userhardware.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, Userhardware.class);
				startActivity(intent);

			}
		});
	}
}
