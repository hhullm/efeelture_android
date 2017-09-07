package com.example.ef;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

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
import android.widget.ImageButton;
import android.widget.Toast;

public class LightcontrolActivity extends Activity {
	private ImageButton b1, b2, b3, b4, b5, b_changjing, b_moshi, b_back;
	private String level = "";
	private String uid = "";
	private String hid = "";
	private String uname = "";
	private String hname = "";
	getip get = new getip();
	private String uipaddress = get.getLocalHostIp();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences cys2 = getSharedPreferences("syy", Context.MODE_PRIVATE);
		hid = cys2.getString("hid", "");
		hname = cys2.getString("hname", "");
		uid = cys2.getString("id", "");
		uname = cys2.getString("uname", "");
		setContentView(R.layout.activity_lightcontrol);
		b1 = (ImageButton) findViewById(R.id.light_contrl_imageButton2);
		b2 = (ImageButton) findViewById(R.id.light_contrl_imageButton3);
		b3 = (ImageButton) findViewById(R.id.light_contrl_imageButton4);
		b4 = (ImageButton) findViewById(R.id.light_contrl_imageButton5);
		b5 = (ImageButton) findViewById(R.id.light_contrl_imageButton6);
		b_changjing = (ImageButton) findViewById(R.id.light_contrl_imageButton8);
		b_moshi = (ImageButton) findViewById(R.id.light_contrl_imageButton7);
		b_back = (ImageButton) findViewById(R.id.light_contrl_imageButton9);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						level = "1";
						loginByPost(level); // 调用loginByPost方法
					};

				}.start();
				b1.setImageResource(R.drawable.onepress);
				b2.setImageResource(R.drawable.two);
				b3.setImageResource(R.drawable.three);
				b4.setImageResource(R.drawable.four);
				b5.setImageResource(R.drawable.five);

			}
		});
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						level = "2";
						loginByPost(level); // 调用loginByPost方法
					};
				}.start();
				b1.setImageResource(R.drawable.one);
				b2.setImageResource(R.drawable.twopress);
				b3.setImageResource(R.drawable.three);
				b4.setImageResource(R.drawable.four);
				b5.setImageResource(R.drawable.five);
			}
		});
		b3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						level = "3";
						loginByPost(level); // 调用loginByPost方法
					};
				}.start();
				b1.setImageResource(R.drawable.one);
				b2.setImageResource(R.drawable.two);
				b3.setImageResource(R.drawable.threepress);
				b4.setImageResource(R.drawable.four);
				b5.setImageResource(R.drawable.five);
			}
		});
		b4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						level = "4";
						loginByPost(level); // 调用loginByPost方法
					};
				}.start();
				b1.setImageResource(R.drawable.one);
				b2.setImageResource(R.drawable.two);
				b3.setImageResource(R.drawable.three);
				b4.setImageResource(R.drawable.fourpress);
				b5.setImageResource(R.drawable.five);
			}
		});
		b5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						level = "5";
						loginByPost(level); // 调用loginByPost方法
					};
				}.start();
				b1.setImageResource(R.drawable.one);
				b2.setImageResource(R.drawable.two);
				b3.setImageResource(R.drawable.three);
				b4.setImageResource(R.drawable.four);
				b5.setImageResource(R.drawable.fivepress);
			}
		});
		b_changjing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(LightcontrolActivity.this, LightmodeActivity.class);
				startActivity(intent2);

			}
		});
		b_moshi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(LightcontrolActivity.this, LightsceneActivity.class);
				startActivity(intent2);

			}
		});
		b_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LightcontrolActivity.this.finish();

			}
		});
	}

	public void loginByPost(String clevel) {

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
					+ URLEncoder.encode(hid, "UTF-8") + "\"" + "\",uname:\"" + URLEncoder.encode(uname, "UTF-8") + "\""
					+ "\",hname:\"" + URLEncoder.encode(hname, "UTF-8") + "\"" + "\",uipaddress:\""
					+ URLEncoder.encode(uipaddress, "UTF-8") + "\"" + "\",clevel:\""
					+ URLEncoder.encode(clevel, "UTF-8") + "\"}";
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

					Toast.makeText(this, "设置成功！", Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(this, "设置失败！", Toast.LENGTH_SHORT).show();
				}

			} else {
				System.out.println("链接失败.........");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
