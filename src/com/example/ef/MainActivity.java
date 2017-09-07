package com.example.ef;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.service.Connectserver;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bean.TranObject;
import bean.Type;
import bean.User;
import bean.chatcontent;

public class MainActivity extends FragmentActivity {
	public static DrawerLayout mDrawerLayout = null;
	private ImageButton b1, b2, b3, b_setting;
	private TextView t1, t2, t3;
	private String uname = "无";
	private String address = "无";
	private String phone = "无";
	private static String uid = "";
	int i = 0;
	String content = "";
	String likenubmber = "";
	public static MainActivity instance1;// 指向当前页面
	getip get = new getip();// 获取IP地址
	public static int a = 1;// 当前页面状态1为主页面，2为message界面，3为朋友圈界面
	public static List<HashMap<String, Object>> friendlist = new ArrayList<HashMap<String, Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance1 = this;
		setContentView(R.layout.activity_main);
		b1 = (ImageButton) findViewById(R.id.main_tomain);
		b3 = (ImageButton) findViewById(R.id.main_tofriend);
		b2 = (ImageButton) findViewById(R.id.main_tomessage);
		t1 = (TextView) findViewById(R.id.main_chouti_text1);
		t2 = (TextView) findViewById(R.id.main_chouti_text2);
		t3 = (TextView) findViewById(R.id.main_chouti_text3);
		b_setting = (ImageButton) findViewById(R.id.main_chouti_button1);

		SharedPreferences information = getSharedPreferences("syy", Context.MODE_PRIVATE);
		uname = information.getString("uname", "无");
		phone = information.getString("phone", "无");
		uid = information.getString("uid", "");
		address = get.getLocalHostIp();



		b_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SettingActivity.class);
				startActivity(intent);

			}
		});
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (a != 1) {
					a = 1;
					changefragment(a);
				}
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (a != 2) {
					a = 2;
					changefragment(a);
				}

			}
		});
		b3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (a != 3) {
					a = 3;
					changefragment(a);
				}
			}
		});
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
			/**
			 * 当抽屉滑动状态改变的时候被调用 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）,
			 * STATE_SETTLING（固定--2）中之一。
			 * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
			 */
			@Override
			public void onDrawerStateChanged(int arg0) {
				Log.i("drawer", "drawer的状态：" + arg0);
			}

			/**
			 * 当抽屉被滑动的时候调用此方法 arg1 表示 滑动的幅度（0-1）
			 */
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				Log.i("drawer", arg1 + "");
				if (uname.equals(""))
					uname = "无";
				if (phone.equals(""))
					phone = "无";
				t1.setText("用户名：" + uname);
				t3.setText("地址：" + address);
				t2.setText("手机：" + phone);
			}

			/**
			 * 当一个抽屉被完全打开的时候被调用
			 */
			@Override
			public void onDrawerOpened(View arg0) {
				Log.i("drawer", "抽屉被完全打开了！");
				if (uname.equals(""))
					uname = "无";
				if (phone.equals(""))
					phone = "无";
				t1.setText("用户名：" + uname);
				t3.setText("地址：" + address);
				t2.setText("手机：" + phone);
			}

			/**
			 * 当一个抽屉完全关闭的时候调用此方法
			 */
			@Override
			public void onDrawerClosed(View arg0) {
				Log.i("drawer", "抽屉被完全关闭了！");
				if (uname.equals(""))
					uname = "无";
				if (phone.equals(""))
					phone = "无";
				t1.setText("用户名：" + uname);
				t3.setText("地址：" + address);
				t2.setText("手机：" + phone);
			}
		});
		startconnect();
		changefragment(1);
	}
	public void startconnect(){
		Intent startIntent = new Intent(MainActivity.this, Connectserver.class);
		Connectserver.uid = uid;
        startService(startIntent);
	}
	@Override
	public void onBackPressed() {// 关闭抽屉
		super.onBackPressed();
		if (mDrawerLayout != null) {
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
				mDrawerLayout.closeDrawers();
			} else
				super.onBackPressed();
		}
	}

	public void changefragment(int c) { // 根据c的值来判定转换为哪个界面
		switch (c) {
		case 1: {
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			fragment1 fragment1 = new fragment1();
			transaction.replace(R.id.fragment_container, fragment1);
			transaction.commit();
			b1.setImageResource(R.drawable.home);
			b2.setImageResource(R.drawable.message2);
			b3.setImageResource(R.drawable.moment2);
		}
			break;
		case 2: {
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			fragment2 fragment2 = new fragment2();
			transaction.replace(R.id.fragment_container, fragment2);
			transaction.commit();
			b1.setImageResource(R.drawable.home2);
			b2.setImageResource(R.drawable.message);
			b3.setImageResource(R.drawable.moment2);
		}
			break;
		case 3: {
			FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			fragment3 fragment3 = new fragment3();
			transaction.replace(R.id.fragment_container, fragment3);
			transaction.commit();
			b1.setImageResource(R.drawable.home2);
			b2.setImageResource(R.drawable.message2);
			b3.setImageResource(R.drawable.moment);
		}
			break;
		}
	}

	/**
	 * 按下这个按钮进行的颜色过滤
	 */
	public final static float[] BT_SELECTED = new float[] { 2, 0, 0, 0, 2, 0, 2, 0, 0, 2, 0, 0, 2, 0, 2, 0, 0, 0, 1,
			0 };

	/**
	 * 按钮恢复原状的颜色过滤
	 */
	public final static float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1,
			0 };

	/**
	 * 按钮焦点改变
	 */
	public final static OnFocusChangeListener buttonOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			} else {
				v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			}
		}
	};

	/**
	 * 按钮触碰按下效果
	 */
	public final static OnTouchListener buttonOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.getBackground().setColorFilter(new ColorMatrixColorFilter(BT_NOT_SELECTED));
				v.setBackgroundDrawable(v.getBackground());
			}
			return false;
		}
	};

	/**
	 * 设置图片按钮获取焦点改变状态
	 * 
	 * @param inImageButton
	 */
	public final static void setButtonFocusChanged(View inView) {
		inView.setOnTouchListener(buttonOnTouchListener);
		inView.setOnFocusChangeListener(buttonOnFocusChangeListener);
	}

	public void getfriendlist() { // 获得好友的信息列表并获取成功则跳转页面

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
					String data = "func=1014" + "&zson={firstid:\"" + URLEncoder.encode(uid, "UTF-8") + "\",fstatus:\""
							+ URLEncoder.encode("1", "UTF-8") + "\"}";
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
					System.out.print("成功连接");
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

							JSONArray resultJsonArray = dataJson.getJSONArray("friendList");
							// System.out.println(resultJsonArray[0].get);
							friendlist.clear();
							for (int i = 0; i < resultJsonArray.length(); i++) {
								JSONObject jsonObject = resultJsonArray.getJSONObject(i);

								HashMap<String, Object> hashMap = new HashMap<String, Object>();
								hashMap.put("uid", jsonObject.getString("secondid"));
								hashMap.put("friendid", jsonObject.getString("id"));// 为好友之间联系
																					// 的唯一id
								friendlist.add(hashMap);
							}
							Intent intent = new Intent(MainActivity.this, ManagefriendActivity.class);
							startActivity(intent);
							System.out.println("获取成功");

						} else {
							System.out.println("获取失败.........");
							Intent intent = new Intent(MainActivity.this, ManagefriendActivity.class);
							startActivity(intent);
						}

					} else {
						System.out.println("连接失败.........");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	

}
