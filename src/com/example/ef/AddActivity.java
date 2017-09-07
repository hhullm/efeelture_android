package com.example.ef;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends Activity {
	private Button back, submit, save;
	private EditText con;
	private String content = "";
	private String uid = "";
	private String mtype = "1";
	private boolean flag = true;// 用于判断是草稿还是发表的动态

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		back = (Button) findViewById(R.id.dynamic_add_button1);
		submit = (Button) findViewById(R.id.dynamic_add_button2);
		save = (Button) findViewById(R.id.dynamic_add_button3);
		con = (EditText) findViewById(R.id.dynamic_add_editText1);
		SharedPreferences tt = getSharedPreferences("syy", Context.MODE_PRIVATE);
		con.setText(tt.getString("content", ""));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddActivity.this.finish();
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread() {
					public void run() {
						content = con.getText().toString();
						/* 用于从本地获取ID信息 */
						SharedPreferences tt = getSharedPreferences("syy", Context.MODE_PRIVATE);
						uid = tt.getString("uid", "");
						flag = true;
						contentByPost();// 通过此方法上传说说内容，和用户ID
					}
				}.start();
				DynamicMainActivity.instancedy.finish();
				Intent intent2 = new Intent(AddActivity.this, DynamicMainActivity.class);
				startActivity(intent2);
				AddActivity.this.finish();
			}
		});

		// 不知道有没有这个方法
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread() {
					public void run() {
						SharedPreferences cys = getSharedPreferences("syy", Context.MODE_PRIVATE);
						Editor editor = cys.edit();
						editor.putString("content", con.getText().toString());
						editor.commit();
						// contentByPost(id,uid,content,flag);//
						// 通过此方法上传说说内容，和用户ID
					}
				}.start();
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
				AddActivity.this.finish();
			}
		});
	}

	public void contentByPost() {
		// TODO Auto-generated method stub
		new Thread() {
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
					String data = "func=1051" + "&zson={uid:\"" + URLEncoder.encode(uid, "UTF-8") + "\",content:\""
							+ URLEncoder.encode(content, "UTF-8") + "\",mtype:\"" + URLEncoder.encode(mtype, "UTF-8")
							+ "\"}";
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
							Looper.prepare();
							Toast.makeText(AddActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
							Looper.loop();
							AddActivity.this.finish();

						} else {
							Looper.prepare();
							Toast.makeText(AddActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
							Looper.loop();

							AddActivity.this.finish();
						}

					} else {
						// Toast.makeText(this, "出错了............",
						// Toast.LENGTH_SHORT).show();
						System.out.println("出错了。。。。");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}