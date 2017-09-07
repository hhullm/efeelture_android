package com.example.ef;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Hardwarelist extends Activity implements OnItemClickListener, OnScrollListener {
	private ListView listView;
	private ArrayAdapter arr_adapter;
	private SimpleAdapter simp_adapter;
	private List<Map<String, Object>> dataList;
	private String hid = "", hname = "", hipaddress = "", uid = "";
	private Button b1;
	private int i = 0;
	private long clickTime = 0;
	private List<HashMap<String, Object>> Hardlist = new ArrayList<HashMap<String, Object>>();
	private SwipeRefreshLayout swipeRefreshLayout;
	private static Toast toast = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hardwarelist);
		listView = (ListView) findViewById(R.id.listView);
		gethardip();
		SharedPreferences cys2 = getSharedPreferences("syy", Context.MODE_PRIVATE);
		uid = cys2.getString("uid", "");
		hid = cys2.getString("hid", "");
		hname = cys2.getString("hname", "");
		/*
		 * while(true){ try{ hid[i] = hard.getString("hid0", ""); hname[i] =
		 * hard.getString("uname"+i, ""); i++; } catch(Exception e){ break; } }
		 */
		b1 = (Button) findViewById(R.id.hardwarelist_back);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Hardwarelist.this.finish();
				Userhardware.instanceuser.finish();
				Intent intent = new Intent(Hardwarelist.this, Userhardware.class);
				startActivity(intent);

			}
		});

		// 新建适配器
		// ArrayAdapter(上下文，当前listview加载的每一个列表项所对应的布局文件，数据源。）
		// 2，适配器加载数据源
		// String[]arr_data={"李振","张鹏哲","廖诗怡","陈静"};
		/**
		 * context:上下文 data: 数据源（List<? extends Map
		 * <String,?>>data）一个map所组成的list集合 每一个Map都会对应list列表中的一行。
		 * 每一个map（键-值对）中的键必须包含所有在from中所包含的键 resource：列表项的布局文件id map中的键名
		 * to:绑定数据视图中的id，与from中的数据与from成对应关系
		 */
		simp_adapter = new SimpleAdapter(this, Hardlist, R.layout.item, new String[] { "hname", "hid", "hipaddress" },
				new int[] { R.id.hname, R.id.hid, R.id.hipaddress });
		// 3,视图加载适配器
		listView.setAdapter(simp_adapter);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container2);// connect
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
		gethardip();
		simp_adapter.notifyDataSetChanged();
		toastShow(Hardwarelist.this, "刷新成功", Toast.LENGTH_SHORT);
	}
	// private List<Map<String, Object>> getData() {
	//
	// // 新建map
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	// //
	//
	// map.put("pic", R.drawable.app);
	// map.put("text", hname+":"+hid);
	//
	// dataList.add(map);
	//
	// return dataList;
	//
	// }

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO 自动生成的方法存根
		switch (scrollState) {
		case SCROLL_STATE_FLING:
			// Log.i("main","用户在手指离开屏幕之前由于用力划了一下，视图仍在滑动");
			// Map<String, Object>map=new HashMap<String, Object>();
			// map.put("pic", R.drawable.user);
			// map.put("text","设备");
			// dataList.add(map);
			// simp_adapter.notifyDataSetChanged();

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
		HashMap<String, Object> map = Hardlist.get(position);
		hid = (String) map.get("hid");
		hname = (String) map.get("hname");
		hipaddress = (String) map.get("hipaddress");
		if (SystemClock.uptimeMillis() - clickTime <= 1500) {
			addhard();
			toastShow(Hardwarelist.this, "添加成功", Toast.LENGTH_SHORT);
			// 如果两次的时间差＜1s，就不执行操作
			clickTime = 0;

		} else {
			// 当前系统时间的毫秒值
			clickTime = SystemClock.uptimeMillis();
			toastShow(Hardwarelist.this, "再次点击添加", Toast.LENGTH_SHORT);

		}

	}

	public void addhard() {

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
					String data = "func=1021" + "&zson={uid:\"" + URLEncoder.encode(uid, "UTF-8") + "\",hardwareid:\""
							+ URLEncoder.encode(hid, "UTF-8") + "\"}";
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
							Toast.makeText(Hardwarelist.this, "添加失败", Toast.LENGTH_SHORT).show();
							Looper.loop();
						}

					} else {
						Looper.prepare();
						Toast.makeText(Hardwarelist.this, "网络链接失败", Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	public void gethardip() {

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
					String data = "func=1099" + "&zson={utype:\"" + URLEncoder.encode("0", "UTF-8") + "\"}";
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

							JSONArray resultJsonArray = dataJson.getJSONArray("userList");
							// System.out.println(resultJsonArray[0].get);
							Hardlist.clear();
							for (int i = 0; i < resultJsonArray.length(); i++) {
								JSONObject jsonObject = resultJsonArray.getJSONObject(i);

								HashMap<String, Object> hashMap = new HashMap<String, Object>();
								hashMap.put("hid", jsonObject.getString("id"));
								hashMap.put("hname", jsonObject.getString("uname"));
								hashMap.put("hipaddress", jsonObject.getString("ipaddress"));
								Hardlist.add(hashMap);
								System.out.print(jsonObject.getString("id"));
							}

						} else {
							System.out.println("鐧诲綍澶辫触.........");
						}

					} else {
						System.out.println("閾炬帴澶辫触.........");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
