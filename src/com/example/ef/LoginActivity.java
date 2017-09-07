package com.example.ef;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText p1, u1;
	private Button login, in, forget, register;
	private String phone = "";
	private String upassword = "";
	// 给返回键安排操作
	private long clickTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (SystemClock.uptimeMillis() - clickTime <= 1500) {

				// 如果两次的时间差＜1s，就不执行操作

			} else {
				// 当前系统时间的毫秒值
				clickTime = SystemClock.uptimeMillis();
				Toast.makeText(LoginActivity.this, "再次点击退出", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SharedPreferences cys = getSharedPreferences("syy", Context.MODE_PRIVATE);
		upassword = cys.getString("password", "");
		phone = cys.getString("phone", "");
		p1 = (EditText) findViewById(R.id.login_first_editText1);
		u1 = (EditText) findViewById(R.id.login_first_editText2);
		login = (Button) findViewById(R.id.login_first_button1);
		forget = (Button) findViewById(R.id.login_forget);
		register = (Button) findViewById(R.id.login_register);
		in = (Button) findViewById(R.id.login_first_button3);
		p1.setText(phone);
		u1.setText(upassword);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						phone = p1.getText().toString();
						upassword = u1.getText().toString();
						loginByPost(); // 璋冪敤loginByPost鏂规硶
					};
				}.start();

			}
		});

		in.setOnClickListener(new OnClickListener() {// 匿名登录
			@Override
			public void onClick(View v) {
				SharedPreferences cys = getSharedPreferences("syy", Context.MODE_PRIVATE);
				Editor editor = cys.edit();
				editor.putString("uid", "");
				editor.putString("uname", "");
				editor.putString("phone", "");
				editor.putString("password", "");
				editor.commit();
				SharedPreferences islogin = getSharedPreferences("islogin", Context.MODE_PRIVATE);
				Editor editor2 = islogin.edit();
				editor2.putString("islogin", "1");
				editor2.commit();

				Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent2);
				LoginActivity.this.finish();
				
			}
		});
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(LoginActivity.this, RegisterfirstActivity.class);
				startActivity(intent2);

			}
		});
		forget.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(LoginActivity.this, Forget_passwordActivity.class);
				startActivity(intent2);

			}
		});
		SharedPreferences islogin = getSharedPreferences("islogin", Context.MODE_PRIVATE);
		String isalreadlylogin = islogin.getString("islogin", "");
		if(isalreadlylogin.equals("1")){
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
		}
	}

	public void loginByPost() {
		new Thread() {
			@Override
			public void run() {
				// 把网络访问的代码放在这里
				try {
					// 璇锋眰鐨勫湴鍧�
					String spec = "http://115.159.120.220:8080/efeelture/mobileAppServlet";
					// 鏍规嵁鍦板潃鍒涘缓URL瀵硅薄
					URL url = new URL(spec);
					// 鏍规嵁URL瀵硅薄鎵撳紑閾炬帴
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					// 璁剧疆璇锋眰鐨勬柟寮�
					urlConnection.setRequestMethod("POST");
					// 璁剧疆璇锋眰鐨勮秴鏃舵椂闂�
					urlConnection.setReadTimeout(5000);
					urlConnection.setConnectTimeout(5000);

					// 浼犻�掔殑鏁版嵁
					String data = "func=1093" + "&zson={phone:\"" + URLEncoder.encode(phone, "UTF-8")
							+ "\",upassword:\"" + URLEncoder.encode(upassword, "UTF-8") + "\"}";
					// 璁剧疆璇锋眰鐨勫ご
					urlConnection.setRequestProperty("Connection", "keep-alive");
					// 璁剧疆璇锋眰鐨勫ご
					urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					// 璁剧疆璇锋眰鐨勫ご
					urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
					// 璁剧疆璇锋眰鐨勫ご
					urlConnection.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

					urlConnection.setDoOutput(true); // 鍙戦�丳OST璇锋眰蹇呴』璁剧疆鍏佽杈撳嚭
					urlConnection.setDoInput(true); // 鍙戦�丳OST璇锋眰蹇呴』璁剧疆鍏佽杈撳叆
													// setDoInput鐨勯粯璁ゅ�煎氨鏄痶rue
					// 鑾峰彇杈撳嚭娴�
					OutputStream os = urlConnection.getOutputStream();
					os.write(data.getBytes());
					os.flush();
					if (urlConnection.getResponseCode() == 200) {
						// 鑾峰彇鍝嶅簲鐨勮緭鍏ユ祦瀵硅薄
						InputStream is = urlConnection.getInputStream();
						// 鍒涘缓瀛楄妭杈撳嚭娴佸璞�
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						// 瀹氫箟璇诲彇鐨勯暱搴�
						int len = 0;
						// 瀹氫箟缂撳啿鍖�
						byte buffer[] = new byte[1024];
						// 鎸夌収缂撳啿鍖虹殑澶у皬锛屽惊鐜鍙�
						while ((len = is.read(buffer)) != -1) {
							// 鏍规嵁璇诲彇鐨勯暱搴﹀啓鍏ュ埌os瀵硅薄涓�
							baos.write(buffer, 0, len);
						}
						// 閲婃斁璧勬簮
						is.close();
						baos.close();
						// 杩斿洖瀛楃涓�
						String result = new String(baos.toByteArray());

						JSONObject dataJson = new JSONObject(result);
						String code1 = dataJson.getString("resultCode");
						if (code1.equals("999")) {
							String code2 = dataJson.getString("user");
							JSONObject dataJson2 = new JSONObject(code2);
							String code3 = dataJson2.getString("id");
							String uname = dataJson2.getString("uname");
							SharedPreferences cys = getSharedPreferences("syy", Context.MODE_PRIVATE);
							Editor editor = cys.edit();
							editor.putString("uid", code3);
							editor.putString("uname", uname);
							editor.putString("phone", phone);
							editor.putString("password", upassword);
							editor.commit();
							SharedPreferences islogin = getSharedPreferences("islogin", Context.MODE_PRIVATE);
							Editor editor2 = islogin.edit();
							editor2.putString("islogin", "1");
							editor2.commit();
							Log.i("login", "登陆成功！");
							LoginActivity.this.finish();
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							startActivity(intent);

						} else {

							Looper.prepare();
							Toast.makeText(LoginActivity.this, "用户名或密码不正确", Toast.LENGTH_SHORT).show();
							Looper.loop();
						}

					} else {
						Looper.prepare();
						Toast.makeText(LoginActivity.this, "链接失败", Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
