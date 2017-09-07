package com.example.ef;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Air_modeActivity extends Activity {
	private ImageButton b1, b2, b3, b_mode, b_temp, b_wind_direction, b_wind_speed, b_back, btn;
	private String level = "";
	private String uid = "";
	private String hid = "";
	private String uname = "";
	private String hname = "";
	private TextView t1, t2, t3;
	getip get = new getip();
	private String uipaddress = get.getLocalHostIp();
	private static Toast toast;
	public static int pageair = 1;// 当前页面的页码
	public static boolean b1_flag = false, b2_flag = false, b3_flag = false;// 当前按钮的信息
	public static boolean bm1 = false, bm2 = false, bm3 = false, bt1 = false, bt2 = false, bt3 = false, bd1 = false,
			bd2 = false, bd3 = false, bs1 = false, bs2 = false, bs3 = false;

	// 12个按钮的信息存储在这里
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_mode);
		SharedPreferences cys2 = getSharedPreferences("syy", Context.MODE_PRIVATE);
		hid = cys2.getString("hid", "");
		hname = cys2.getString("hname", "");
		uid = cys2.getString("id", "");
		uname = cys2.getString("uname", "");
		b_mode = (ImageButton) findViewById(R.id.air_mode_imageview03);
		b_temp = (ImageButton) findViewById(R.id.air_mode_imageButton02);
		b_wind_direction = (ImageButton) findViewById(R.id.air_mode_imageButton01);
		b_wind_speed = (ImageButton) findViewById(R.id.air_mode_imageButton4);
		b_back = (ImageButton) findViewById(R.id.air_mode_backbutton);
		t1 = (TextView) findViewById(R.id.air_mode_textView1);
		t2 = (TextView) findViewById(R.id.air_mode_textView2);
		t3 = (TextView) findViewById(R.id.air_mode_textView3);
		b1 = (ImageButton) findViewById(R.id.air_temp_imageButton1);
		b2 = (ImageButton) findViewById(R.id.air_temp_imageButton2);
		b3 = (ImageButton) findViewById(R.id.air_temp_imageButton3);
		t1.setText("制冷");// 按钮文字信息初始化为mode界面的
		t2.setText("制热");
		t3.setText("抽湿");
		// 将按钮信息初始化为mode的属性
		showbutton(1);
		b_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Air_modeActivity.this.finish();
			}
		});
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickbutton(pageair, 1);
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickbutton(pageair, 2);
			}
		});
		b3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickbutton(pageair, 3);
			}
		});
		b_mode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pageair != 1) {
					pageair = 1; // 点击该按钮后，界面转化为mode界面的内容
					t1.setText("制冷");
					t2.setText("制热");
					t3.setText("抽湿");
					showbutton(1);
				}
				b_mode.setImageResource(R.drawable.kkkms1);
				b_temp.setImageResource(R.drawable.kkkwd2);
				b_wind_direction.setImageResource(R.drawable.kkkfx2);
				b_wind_speed.setImageResource(R.drawable.kkkfs2);

			}
		});
		b_temp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pageair != 2) {
					pageair = 2;
					t1.setText("增温");
					t2.setText("减温");
					t3.setText("自动");
					showbutton(2);
				}
				b_mode.setImageResource(R.drawable.kkkms2);
				b_temp.setImageResource(R.drawable.kkkwd1);
				b_wind_direction.setImageResource(R.drawable.kkkfx2);
				b_wind_speed.setImageResource(R.drawable.kkkfs2);

			}
		});
		b_wind_direction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (pageair != 3) {
					pageair = 3;
					t1.setText("上下扫风");
					t2.setText("左右扫风");
					t3.setText("自动扫风");
					showbutton(3);
				}
				b_mode.setImageResource(R.drawable.kkkms2);
				b_temp.setImageResource(R.drawable.kkkwd2);
				b_wind_direction.setImageResource(R.drawable.kkkfx1);
				b_wind_speed.setImageResource(R.drawable.kkkfs2);
			}
		});
		b_wind_speed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (pageair != 4) {
					pageair = 4;
					t1.setText("低速");
					t2.setText("中速");
					t3.setText("快速");
					showbutton(4);
				}
				b_mode.setImageResource(R.drawable.kkkms2);
				b_temp.setImageResource(R.drawable.kkkwd2);
				b_wind_direction.setImageResource(R.drawable.kkkfx2);
				b_wind_speed.setImageResource(R.drawable.kkkfs1);
			}
		});

	}

	public static void toastShow(Context context, String text, int duration) {
		if (toast == null) {
			toast = Toast.makeText(context, text, duration);
		} else {
			toast.setText(text);
		}
		toast.show();
	}

	public String judgelevel(boolean button) {
		if (button == true)
			level = "1";
		else
			level = "0";
		System.out.println(level);
		return level;
	}

	public void clickbutton(int number, int buttonnum) { // click this number
															// interface
															// buttonnum will do
															// this
		switch (number) {
		case 1: {
			switch (buttonnum) {
			case 1: {
				bm1 = !bm1;
				bm2 = false;
				bm3 = false;
				showbutton(1);
				contrl(judgelevel(bm1));
			}
				break;
			case 2: {
				bm2 = !bm2;
				bm1 = false;
				bm3 = false;
				showbutton(1);
				contrl(judgelevel(bm2));

			}
				break;
			case 3: {
				bm3 = !bm3;
				bm2 = false;
				bm1 = false;
				showbutton(1);
				contrl(judgelevel(bm3));
			}
				break;
			}
		}
			break;
		case 2: {
			switch (buttonnum) {
			case 1: {
				bt1 = !bt1;
				bt2 = false;
				bt3 = false;
				showbutton(2);
				contrl(judgelevel(bt1));
			}
				break;
			case 2: {
				bt2 = !bt2;
				bt1 = false;
				bt3 = false;
				showbutton(2);
				contrl(judgelevel(bt2));

			}
				break;
			case 3: {
				bt3 = !bt3;
				bt2 = false;
				bt1 = false;
				showbutton(2);
				contrl(judgelevel(bt3));
			}
				break;
			}
		}
			break;
		case 3: {
			switch (buttonnum) {
			case 1: {
				bd1 = !bd1;
				bd2 = false;
				bd3 = false;
				showbutton(3);
				contrl(judgelevel(bd1));
			}
				break;
			case 2: {
				bd2 = !bd2;
				bd1 = false;
				bd3 = false;
				showbutton(3);
				contrl(judgelevel(bd2));

			}
				break;
			case 3: {
				bd3 = !bd3;
				bd2 = false;
				bd1 = false;
				showbutton(3);
				contrl(judgelevel(bd3));
			}
				break;
			}
		}
			break;
		case 4: {
			switch (buttonnum) {
			case 1: {
				bs1 = !bs1;
				bs2 = false;
				bs3 = false;
				showbutton(4);
				contrl(judgelevel(bs1));
			}
				break;
			case 2: {
				bs2 = !bs2;
				bs1 = false;
				bs3 = false;
				showbutton(4);
				contrl(judgelevel(bs2));

			}
				break;
			case 3: {
				bs3 = !bs3;
				bs2 = false;
				bs1 = false;
				showbutton(4);
				contrl(judgelevel(bs3));
			}
				break;
			}
		}
			break;
		}
	}

	public void showbutton(int number) {// show number page's button
		switch (number) {
		case 1: {
			if (bm1 == true)
				b1.setImageResource(R.drawable.on1);
			else
				b1.setImageResource(R.drawable.off1);
			if (bm2 == true)
				b2.setImageResource(R.drawable.on1);
			else
				b2.setImageResource(R.drawable.off1);
			if (bm3 == true)
				b3.setImageResource(R.drawable.on1);
			else
				b3.setImageResource(R.drawable.off1);
		}
			break;
		case 2: {
			if (bt1 == true)
				b1.setImageResource(R.drawable.on1);
			else
				b1.setImageResource(R.drawable.off1);
			if (bt2 == true)
				b2.setImageResource(R.drawable.on1);
			else
				b2.setImageResource(R.drawable.off1);
			if (bt3 == true)
				b3.setImageResource(R.drawable.on1);
			else
				b3.setImageResource(R.drawable.off1);
		}
			break;
		case 3: {
			if (bd1 == true)
				b1.setImageResource(R.drawable.on1);
			else
				b1.setImageResource(R.drawable.off1);
			if (bd2 == true)
				b2.setImageResource(R.drawable.on1);
			else
				b2.setImageResource(R.drawable.off1);
			if (bd3 == true)
				b3.setImageResource(R.drawable.on1);
			else
				b3.setImageResource(R.drawable.off1);
		}
			break;
		case 4: {
			if (bs1 == true)
				b1.setImageResource(R.drawable.on1);
			else
				b1.setImageResource(R.drawable.off1);
			if (bs2 == true)
				b2.setImageResource(R.drawable.on1);
			else
				b2.setImageResource(R.drawable.off1);
			if (bs3 == true)
				b3.setImageResource(R.drawable.on1);
			else
				b3.setImageResource(R.drawable.off1);
		}
			break;
		}
	}

	public void contrl(String clevel) {

		new Thread() {
			@Override
			public void run() {
				try {
					// 请求的地址
					String spec = "http://115.159.120.220:8080/efeelture/mobileAppServlet";
					// 根据地址创建URL对象
					URL url = new URL(spec);
					// 根据URL对象打开链接
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					// 设置请求的方式
					urlConnection.setRequestMethod("POST");
					// 设置请求的超时时间
					urlConnection.setReadTimeout(5000);
					urlConnection.setConnectTimeout(5000);

					// 传递的数据
					String data = "func=1002" + "&zson={uid:\"" + URLEncoder.encode(uid, "UTF-8") + "\",hid:\""
							+ URLEncoder.encode(hid, "UTF-8") + "\"" + "\",uname:\"" + URLEncoder.encode(uname, "UTF-8")
							+ "\"" + "\",hname:\"" + URLEncoder.encode(hname, "UTF-8") + "\"" + "\",uipaddress:\""
							+ URLEncoder.encode(uipaddress, "UTF-8") + "\"" + "\",clevel:\""
							+ URLEncoder.encode(level, "UTF-8") + "\"}";
					// 设置请求的头
					urlConnection.setRequestProperty("Connection", "keep-alive");
					// 设置请求的头
					urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					// 设置请求的头
					urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
					// 设置请求的头
					urlConnection.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

					urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
					urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
													// setDoInput的默认值就是true
					// 获取输出流
					OutputStream os = urlConnection.getOutputStream();
					os.write(data.getBytes());
					os.flush();
					System.out.println("成功进入");
					if (urlConnection.getResponseCode() == 200) {
						// 获取响应的输入流对象
						InputStream is = urlConnection.getInputStream();
						// 创建字节输出流对象
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						// 定义读取的长度
						int len = 0;
						// 定义缓冲区
						byte buffer[] = new byte[1024];
						// 按照缓冲区的大小，循环读取
						while ((len = is.read(buffer)) != -1) {
							// 根据读取的长度写入到os对象中
							baos.write(buffer, 0, len);
						}
						// 释放资源
						is.close();
						baos.close();
						// 返回字符串
						String result = new String(baos.toByteArray());

						JSONObject dataJson = new JSONObject(result);
						String code1 = dataJson.getString("resultCode");
						if (code1.equals("999")) {

							Looper.prepare();
							toastShow(Air_modeActivity.this, "设置成功", Toast.LENGTH_SHORT);
							Looper.loop();

						} else {
							Looper.prepare();
							toastShow(Air_modeActivity.this, "设置失败", Toast.LENGTH_SHORT);
							Looper.loop();
						}

					} else {
						Looper.prepare();
						toastShow(Air_modeActivity.this, "链接失败", Toast.LENGTH_SHORT);
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
