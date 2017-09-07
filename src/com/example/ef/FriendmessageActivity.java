package com.example.ef;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class FriendmessageActivity extends Activity implements OnItemClickListener, OnScrollListener {
	private ImageButton back, add;
	private ListView list;
	private TextView title;
	private SimpleAdapter simp_adapter;
	MainActivity main = new MainActivity();
	private List<HashMap<String, Object>> Data = new ArrayList<HashMap<String, Object>>();
	public static FriendmessageActivity instancedy;
	public static String friendid = "";

	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendmessage);
		getListData();
		instancedy = this;
		back = (ImageButton) findViewById(R.id.friendmessage_button1);
		simp_adapter = new SimpleAdapter(this, Data, R.layout.activity_dynamic_item,
				new String[] { "content", "likenumber" },
				new int[] { R.id.dynamic_item_textView_content, R.id.dynamic_item_textView1, });
		list = (ListView) findViewById(R.id.friendmessage_listView1);
		list.addHeaderView(getLayoutInflater().inflate(R.layout.listviewhead, null));
		list.setAdapter(simp_adapter);
		list.setOnItemClickListener(this);
		list.setOnScrollListener(this);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.friendmessage_container);// connect
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

						// TODO Auto-generated method stub
						refresh();
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 1500);
			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						FriendmessageActivity.this.finish();
					};
				}.start();

			}
		});

	}

	public void refresh() {// refresh listview
		getListData();
		simp_adapter.notifyDataSetChanged();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		// 已知在滚动中，多次触发
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// // TODO Auto-generated method stub
	}

	public void getListData() {

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
					String data = "func=1054" + "&zson={uid:\"" + URLEncoder.encode(friendid, "UTF-8") + "\"}";
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

							JSONArray resultJsonArray = dataJson.getJSONArray("messageList");
							// System.out.println(resultJsonArray[0].get);
							Data.clear();
							for (int i = 0; i < resultJsonArray.length(); i++) {
								JSONObject jsonObject = resultJsonArray.getJSONObject(i);

								HashMap<String, Object> hashMap = new HashMap<String, Object>();
								hashMap.put("content", jsonObject.getString("content"));
								hashMap.put("likenumber", jsonObject.getString("likenumber"));
								Data.add(hashMap);
								System.out.print(jsonObject.getString("content"));
							}

						} else {

							Looper.prepare();
							Toast.makeText(FriendmessageActivity.this, "暂无动态", Toast.LENGTH_SHORT).show();
							Looper.loop();
						}

					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
}
