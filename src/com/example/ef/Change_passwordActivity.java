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
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class Change_passwordActivity extends Activity {
	private ImageButton back;
	private Button next;
	private EditText e_password, e_newpassword;
	private String id = "";
	private String password = "";
	private String newpassword = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		back = (ImageButton) findViewById(R.id.change_button_backbutton);
		next = (Button) findViewById(R.id.change_password_button1);
		e_password = (EditText) findViewById(R.id.change_password_editText2);
		e_newpassword = (EditText) findViewById(R.id.change_password_editText3);
		SharedPreferences information = getSharedPreferences("syy", Context.MODE_PRIVATE);
		id = information.getString("uid", "无");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Change_passwordActivity.this.finish();
			}
		});
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						password = e_password.getText().toString();
						newpassword = e_newpassword.getText().toString();
						loginByPost(); // 调用loginByPost方法
					};
				}.start();

			}
		});
	}

	public void loginByPost() {
		new Thread() {
			@Override
			public void run() {
				// 把网络访问的代码放在这里
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
					String data = "func=1094" + "&zson={upassword:\"" + URLEncoder.encode(password, "UTF-8")
							+ "\",newpassword:\"" + URLEncoder.encode(newpassword, "UTF-8") + "\"" + ",id:\""
							+ URLEncoder.encode(id, "UTF-8") + "\"}";
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
					System.out.println(urlConnection.getResponseCode());
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
						System.out.println(code1);
						if (code1.equals("999")) {
							SharedPreferences cys = getSharedPreferences("syy", Context.MODE_PRIVATE);
							Editor editor = cys.edit();
							editor.putString("password", "");
							editor.commit();
							MainActivity.instance1.finish();
							SettingActivity.instance3.finish();
							Change_passwordActivity.this.finish();
							Intent intent = new Intent(Change_passwordActivity.this, LoginActivity.class);
							startActivity(intent);

						} else {
							Looper.prepare();
							Toast.makeText(Change_passwordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
							Looper.loop();
						}

					} else {
						Looper.prepare();
						Toast.makeText(Change_passwordActivity.this, "链接失败", Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
