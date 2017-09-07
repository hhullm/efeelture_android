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

//import net.sf.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import newAdapter.criadapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class DynamicMainActivity extends baseActivity implements OnItemClickListener, OnScrollListener {
	private ImageButton back, add;
	private EditText replycontent;
	private Button startreply;
	public RelativeLayout replythis;
	private ListView list;
	private criadapter simp_adapter;
	private RelativeLayout criroom;
	MainActivity main = new MainActivity();
	private List<HashMap<String, Object>> message = new ArrayList<HashMap<String, Object>>();
	public static List<HashMap<String, Object>> reply = new ArrayList<HashMap<String, Object>>();
	public static List<HashMap<String, Object>> like = new ArrayList<HashMap<String, Object>>();
	public static DynamicMainActivity instancedy;
	public static Boolean isreply;
	private SwipeRefreshLayout swipeRefreshLayout;
	private String uid = "";
	public static String friendidformessage = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_main);
		SharedPreferences information = getSharedPreferences("syy", Context.MODE_PRIVATE);
	    uid = information.getString("uid", "无");
		getListData(friendidformessage);
		instancedy = this;
		back = (ImageButton) findViewById(R.id.dynamic_button1);
		add = (ImageButton) findViewById(R.id.dynamic_imageButton1);
		replythis = (RelativeLayout) findViewById(R.id.reply_layout);
		simp_adapter = new criadapter(this, message, reply, uid, like, this);
		list = (ListView) findViewById(R.id.dynamic_listView1);
		list.addHeaderView(getLayoutInflater().inflate(R.layout.listviewhead, null));
		list.setAdapter(simp_adapter);
		list.setOnItemClickListener(this);
		list.setOnScrollListener(this);

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				friendidformessage ="";
				DynamicMainActivity.this.finish();
			}
		});

		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(DynamicMainActivity.this, AddActivity.class);// 从本页面跳转到添加说说界面
				startActivity(intent2);
			}
		});
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_containerfriend);// connect
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
						Toast.makeText(DynamicMainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
						// TODO Auto-generated method stub
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 500);
			}
		});
		replycontent = (EditText) findViewById(R.id.replycontent);  
		startreply = (Button) findViewById(R.id.replytothismessage);
		startreply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(replycontent.getText().toString().length()!=0){
					addreply(simp_adapter.thisreplyid, replycontent.getText().toString(), uid, simp_adapter.thisreplyto);
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("messageid", simp_adapter.thisreplyid);
					hashMap.put("content", replycontent.getText().toString());
					hashMap.put("firstid", uid);
					reply.add(hashMap);
					simp_adapter.notifyDataSetChanged();
					replythis.setVisibility(View.GONE);
					replycontent.setText("");
				}
			}
		});
	}

	public void replyroomshow(){
		InputMethodManager imm= ( InputMethodManager ) replycontent.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.showSoftInput(replycontent,InputMethodManager.SHOW_FORCED); 
        replythis.setVisibility(View.VISIBLE);
	}

	public void refresh() {// refresh listview
		getListData(friendidformessage);
		simp_adapter.notifyDataSetChanged();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		replythis.setVisibility(View.GONE);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// // TODO Auto-generated method st
	}

	public void getListData(final String friendid) {

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

							getlikenumber();
							getReplyData();
							JSONArray resultJsonArray = dataJson.getJSONArray("messageList");
							// System.out.println(resultJsonArray[0].get);
							message.clear();
							if(friendid==""){
								for (int i = 0; i < resultJsonArray.length(); i++) {
									JSONObject jsonObject = resultJsonArray.getJSONObject(i);

									HashMap<String, Object> hashMap = new HashMap<String, Object>();
									hashMap.put("messageid", jsonObject.getString("id"));
									hashMap.put("content", jsonObject.getString("content"));
									hashMap.put("likenumber", jsonObject.getString("likenumber"));
									hashMap.put("secondid", jsonObject.getString("uid"));
									hashMap.put("time", jsonObject.getString("mtime"));
									message.add(hashMap);
								}
							}
							else{
								for (int i = 0; i < resultJsonArray.length(); i++) {
									JSONObject jsonObject = resultJsonArray.getJSONObject(i);

									if(jsonObject.getString("uid").equals(friendid)){
										HashMap<String, Object> hashMap = new HashMap<String, Object>();
										hashMap.put("messageid", jsonObject.getString("id"));
										hashMap.put("content", jsonObject.getString("content"));
										hashMap.put("likenumber", jsonObject.getString("likenumber"));
										hashMap.put("secondid", jsonObject.getString("uid"));
										hashMap.put("time", jsonObject.getString("mtime"));
										message.add(hashMap);
									}
								}
							}

						} else {

							Looper.prepare();
							Toast.makeText(DynamicMainActivity.this, "暂无动态", Toast.LENGTH_SHORT).show();
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

	public void getReplyData() {

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
					String data = "func=1063" + "&zson={messageid:\"\"}";
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

							JSONArray resultJsonArray = dataJson.getJSONArray("replyList");
							// System.out.println(resultJsonArray[0].get);
							reply.clear();
							for (int i = 0; i < resultJsonArray.length(); i++) {
								JSONObject jsonObject = resultJsonArray.getJSONObject(i);

								HashMap<String, Object> hashMap = new HashMap<String, Object>();
								hashMap.put("messageid", jsonObject.getString("messageid"));
								hashMap.put("content", jsonObject.getString("content"));
								hashMap.put("firstid", jsonObject.getString("firstid"));
								hashMap.put("secondid", jsonObject.getString("secondid"));
								hashMap.put("replyid", jsonObject.getString("id"));
								reply.add(hashMap);
							}

						} else {

							Looper.prepare();
							Toast.makeText(DynamicMainActivity.this, "暂无动态", Toast.LENGTH_SHORT).show();
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

	public void getlikenumber() {

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
					String data = "func=1043" + "&zson={messageid:\"\"}";
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

							JSONArray resultJsonArray = dataJson.getJSONArray("likeList");
							// System.out.println(resultJsonArray[0].get);
							like.clear();
							for (int i = 0; i < resultJsonArray.length(); i++) {
								JSONObject jsonObject = resultJsonArray.getJSONObject(i);

								HashMap<String, Object> hashMap = new HashMap<String, Object>();
								hashMap.put("messageid", jsonObject.getString("messageid"));
								hashMap.put("likeid", jsonObject.getString("id"));
								hashMap.put("uid", jsonObject.getString("uid"));
								like.add(hashMap);
							}

						} else {
						}

					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	public void addreply(final String messageid, final String content, final String uid, final String secondid) {
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
					String data = "func=1061" + "&zson={firstid:\"" + URLEncoder.encode(uid, "UTF-8")
							+ "\",messageid:\"" + URLEncoder.encode(messageid, "UTF-8") + "\",content:\""
							+ URLEncoder.encode(content, "UTF-8") + "\",secondid:\""
							+ URLEncoder.encode(secondid, "UTF-8") + "\"}";
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

							System.out.println("回复成功");

						} else {

							System.out.println("回复失败");
						}

					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
