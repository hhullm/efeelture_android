package com.example.ef;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Userhardware extends Activity implements OnItemClickListener, OnScrollListener, OnItemLongClickListener {
	private ListView listView;
	private SimpleAdapter simp_adapter;
	private Button b1, b2;
	private String hid = "", uid = "", hardid = "";
	private ArrayList<HashMap<String, Object>> AddedHardlist = new ArrayList<HashMap<String, Object>>();
	getip get = new getip();
	private String uipaddress = get.getLocalHostIp();
	public static Userhardware instanceuser;
	private long clickTime = 0;
	private SwipeRefreshLayout swipeRefreshLayout;
	private static Toast toast = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userhardware);
		instanceuser = this;
		listView = (ListView) findViewById(R.id.userhardwareview);
		b2 = (Button) findViewById(R.id.userhardware_back);
		b1 = (Button) findViewById(R.id.useraddware);
		SharedPreferences cys2 = getSharedPreferences("syy", Context.MODE_PRIVATE);
		uid = cys2.getString("uid", "");
		getaddhard();
		simp_adapter = new SimpleAdapter(this, AddedHardlist, R.layout.item2, new String[] { "id", "hid" },
				new int[] { R.id.item_id, R.id.item_hid });
		// arr_adapter = new ArrayAdapter(this,
		// android.R.layout.simple_list_item_activated_1, arr_data);
		// 3,视图加载适配器
		// listView.setAdapter(arr_adapter);
		listView.setAdapter(simp_adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		listView.setOnScrollListener(this);
		refresh();
		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				Userhardware.this.finish();

			}
		});
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Userhardware.this, Hardwarelist.class);
				startActivity(intent);
			}
		});
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);// connect
																						// to
																						// swipe

		swipeRefreshLayout.setColorScheme( // 刷新条上色
				android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				// TODO Auto-generated method stub
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {// while pushing down need to do
						refresh();
						toastShow(Userhardware.this, "刷新成功", Toast.LENGTH_SHORT);
						// TODO Auto-generated method stub
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 1500);
			}
		});

	}

	/**
	 * 判断toast是否存在，如果存在则更新text，达到避免出现时间叠加的问题
	 * 
	 * @param context
	 *            上下文
	 * @param text
	 *            显示的内容
	 * @param duration
	 *            显示时长，默认值为Toast.LENGTH_SHORT (2秒)或Toast.LENGTH_LONG(3.5秒)
	 */
	public static void toastShow(Context context, String text, int duration) {
		if (toast == null) {
			toast = Toast.makeText(context, text, duration);
		} else {
			toast.setText(text);
		}
		toast.show();
	}

	public void refresh() {// refresh listview
		getaddhard();
		simp_adapter.notifyDataSetChanged();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自动生成的方法存根
		switch (scrollState) {
		case SCROLL_STATE_FLING:

			break;

		case SCROLL_STATE_IDLE:
			Log.i("main", "视图已经停止滑动");

			break;
		case SCROLL_STATE_TOUCH_SCROLL:
			Log.i("main", "手指没有离开屏幕，视图正在滑动");
			break;
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 自动生成的方法存根

	}

	public void deletehard() {

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
					String data = "func=1022" + "&zson={id:\"" + URLEncoder.encode(hardid, "UTF-8") + "\"}";
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

						} else {
							Looper.prepare();
							toastShow(Userhardware.this, "删除失败", Toast.LENGTH_SHORT);
							Looper.loop();
						}

					} else {
						Looper.prepare();
						toastShow(Userhardware.this, "链接失败", Toast.LENGTH_SHORT);
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void getaddhard() {

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
					String data = "func=1023" + "&zson={uid:\"" + URLEncoder.encode(uid, "UTF-8") + "\"}";
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
							JSONArray resultJsonArray = dataJson.getJSONArray("hardwareList");
							// System.out.println(resultJsonArray[0].get);
							AddedHardlist.clear();

							for (int i = 0; i < resultJsonArray.length(); i++) {
								JSONObject jsonObject = resultJsonArray.getJSONObject(i);

								HashMap<String, Object> hashMap = new HashMap<String, Object>();
								hashMap.put("hid", jsonObject.getString("hardwareid"));
								hashMap.put("id", jsonObject.getString("id"));
								AddedHardlist.add(hashMap);
								System.out.print(jsonObject.getString("id"));
							}

						} else {
							Looper.prepare();
							toastShow(Userhardware.this, "无可用设备", Toast.LENGTH_SHORT);
							Looper.loop();
						}

					} else {
						Looper.prepare();
						Toast.makeText(Userhardware.this, "网络链接失败", Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		// TODO Auto-generated method stub

		new AlertDialog.Builder(this).setTitle("提示：").setMessage("确认删除设备吗？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						HashMap<String, Object> map = AddedHardlist.get(position);
						hardid = (String) map.get("id");
						AddedHardlist.remove(position);
						simp_adapter.notifyDataSetChanged();
						deletehard();

					}
				}).setNegativeButton("否", null).show();
		return false;
	}

}
