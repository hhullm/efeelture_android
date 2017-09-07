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

import com.example.ef.friendadapter.ViewHolder;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListViewListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemLongClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class ManagefriendActivity extends Activity implements View.OnClickListener {
	// implements View.OnClickListener, OnScrollListener,
	// android.widget.AdapterView.OnItemLongClickListener
	private Button b_back;
	private SwipeListView listview;
	public static friendadapter simp_adapter;
	public static List<HashMap<String, Object>> friendlist = new ArrayList<HashMap<String, Object>>();
	public static List<HashMap<String, Object>> nofriendlist = new ArrayList<HashMap<String, Object>>();
	private int no=1;
	private SwipeRefreshLayout swipeRefreshLayout;

	private String uid = "";
	private String friendid = "";
	private TextView addfriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_managefriend);
		b_back = (Button) findViewById(R.id.manage_back);
		b_back.setOnClickListener(this);
		listview = (SwipeListView) findViewById(R.id.id_swipelistview);
		addfriend = (TextView) findViewById(R.id.manage_myfriend);
		addfriend.setOnClickListener(this);
		System.out.println("1");

		SharedPreferences cys2 = getSharedPreferences("syy", Context.MODE_PRIVATE);
		uid = cys2.getString("uid", "");

		friendlist = MainActivity.friendlist;
		// 视图加载适配器
		simp_adapter = new friendadapter(this, friendlist, listview, this);
		listview.setAdapter(simp_adapter);
		listview.setSwipeListViewListener(new BaseSwipeListViewListener(){        //点击跳转到聊天界面
			public void onClickFrontView(int position)  
            {  
				// 内部存储有关于聊天人的id
				friendid = friendlist.get(position).get("uid").toString();
				SharedPreferences talklist = getSharedPreferences("talklist", Context.MODE_PRIVATE);
				String oldlist = fragment2_1.talkto;// talklist中存放和uid拥有者聊天过的人的名单列表用“，”隔开
				System.out.println(oldlist);
				if (oldlist == "") {
					Editor editor = talklist.edit();
					editor.putString(uid, friendid);// 将新数据存进talklist中
					editor.commit();
					fragment2_1.talkto = friendid;
				}
				else {
					String oldlistcut[] = oldlist.split(",");
					String talkto = "," + friendid;
					for (int i = 0; i < oldlistcut.length; i++) { // 如果之前聊过天，内部存储过数据则不新增数据
						System.out.println(oldlistcut[i]);
						if (friendid.equals(oldlistcut[i])) {
							System.out.println("@@@");
							talkto = "";
							break;

						}
					}
					Editor editor = talklist.edit();
					editor.putString(uid, oldlist + talkto);// 将新数据存进talklist中
					editor.commit();
					fragment2_1.talkto = oldlist + talkto;
					if(talkto!=""){
						fragment2_1.talkto = talklist.getString(uid, "");
						HashMap<String, Object> map1 = new HashMap<String, Object>();
						map1.put("image", R.drawable.girl111);
						map1.put("text", friendid);
						map1.put("text2", "在？吗♂♂♂♂♂♂");
						fragment2_1.Data.add(map1);
						fragment2_1.simp_adapter.notifyDataSetChanged();
					}
				}
				Messagelr.talkto = friendid;
				Intent intent = new Intent(ManagefriendActivity.this, Messagelr.class);
				startActivity(intent);
				ManagefriendActivity.this.finish();
            } 
		});

		// 刷新条匹配
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.manage_container2);// connect
																						// to
																						// swipe
		swipeRefreshLayout.setColorScheme( // 刷新条上色
				android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		// 给下拉刷新设置监听
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				// TODO Auto-generated method stub
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {// while pushing down need to do

						// 下拉之后需要做的事情放在这里
						refresh();
						// TODO Auto-generated method stub
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 1500);
			}
		});
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.manage_back: {
			ManagefriendActivity.this.finish();
		}break;
		case R.id.manage_myfriend: {
			if(no == 1){
				getnofriendlist();
			}
			else{
				Intent intent = new Intent(ManagefriendActivity.this, AddfriendActivity.class);
				startActivity(intent);
			}
		}break;
		}
	}

	public void deletefriend(final String contactid) { // 删除好友
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
					String data = "func=1016" + "&zson={id:\"" + URLEncoder.encode(contactid, "UTF-8") + "\",fstatus:\""
							+ URLEncoder.encode("0", "UTF-8") + "\"}";
					// 璁剧疆璇锋眰鐨勫ご
					System.out.println(data);
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

							Looper.prepare();
							Toast.makeText(ManagefriendActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
							Looper.loop();

						} else {
							Looper.prepare();
							Toast.makeText(ManagefriendActivity.this, "您还没有添加好友，快去添加吧", Toast.LENGTH_SHORT).show();
							Looper.loop();
						}

					} else {
						Looper.prepare();
						Toast.makeText(ManagefriendActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	public void getfriendlist() { // 获得好友的信息列表

		listview.closeOpenedItems();
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
							System.out.println("获取成功");

						} else {
							Looper.prepare();
							Toast.makeText(ManagefriendActivity.this, "您还没有添加好友，快去添加吧", Toast.LENGTH_SHORT).show();
							Looper.loop();
						}

					} else {
						Looper.prepare();
						Toast.makeText(ManagefriendActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	public void refresh() {// refresh listview
		getfriendlist();
		simp_adapter.notifyDataSetChanged();
	}

	public void turntofriendmessage(String friendid){
		DynamicMainActivity.friendidformessage = friendid;
		Intent intent = new Intent(ManagefriendActivity.this, DynamicMainActivity.class);
		startActivity(intent);
    }

	public void getnofriendlist() { // 获得非好友的信息列表获取成功则跳转页面

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
					String data = "func=1015" + "&zson={id:\"" + URLEncoder.encode(uid, "UTF-8") +"\"}";
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

							JSONArray resultJsonArray = dataJson.getJSONArray("noFriendList");
							// System.out.println(resultJsonArray[0].get);
							nofriendlist.clear();
							for (int i = 0; i < resultJsonArray.length(); i++) {
								JSONObject jsonObject = resultJsonArray.getJSONObject(i);

								HashMap<String, Object> hashMap = new HashMap<String, Object>();
								hashMap.put("uname", jsonObject.getString("uname"));
								hashMap.put("phone", jsonObject.getString("phone"));
								hashMap.put("uid", jsonObject.getString("id"));
								nofriendlist.add(hashMap);
							}
							System.out.println("获取成功");
							Intent intent = new Intent(ManagefriendActivity.this, AddfriendActivity.class);
							startActivity(intent);
							no = 2;

						} else {
							System.out.println("获取失败.........");
							Intent intent = new Intent(ManagefriendActivity.this, AddfriendActivity.class);
							startActivity(intent);
						}

					} else {
						System.out.println("连接失败.........");
						Intent intent = new Intent(ManagefriendActivity.this, AddfriendActivity.class);
						startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Intent intent = new Intent(ManagefriendActivity.this, AddfriendActivity.class);
					startActivity(intent);
				}
			}
		}.start();

	}

	
}

class friendadapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private SwipeListView mSwipeListView;
	private List<HashMap<String, Object>> mData;
	private ManagefriendActivity act;
	private String contectid = "";
	private Handler handler = null;

	public friendadapter(Context con, List<HashMap<String, Object>> data, SwipeListView list,
			ManagefriendActivity activity) {
		mInflater = LayoutInflater.from(con);
		context = con;
		mData = data;
		mSwipeListView = list;
		act = activity;
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return mData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView name;
		TextView delete;
		TextView message;
		LinearLayout main;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Map thisfriend = mData.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.friend, null);
			holder.name = (TextView) convertView.findViewById(R.id.friend_textView3);
			holder.delete = (TextView) convertView.findViewById(R.id.friend_delete);
			holder.message = (TextView) convertView.findViewById(R.id.friend_message);
			holder.main = (LinearLayout) convertView.findViewById(R.id.friend_front);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		handler = new Handler();
		holder.name.setText(thisfriend.get("uid").toString());
		holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mData.remove(position);  
                notifyDataSetChanged();  
				contectid = thisfriend.get("friendid").toString();
				handler.post(deletefriend);
			}
		});
		holder.message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				contectid = thisfriend.get("uid").toString();
				handler.post(friendmessage);
			}
		});

		return convertView;
	}

	// 构建Runnable对象，在runnable中更新界面
	Runnable deletefriend = new Runnable() {
		@Override
		public void run() {
			// 更新界面
			act.deletefriend(contectid);
		}

	};
	// 构建Runnable对象，在runnable中更新界面
		Runnable friendmessage = new Runnable() {
			@Override
			public void run() {
				// 更新界面
				act.turntofriendmessage(contectid);
			}

		};

}
