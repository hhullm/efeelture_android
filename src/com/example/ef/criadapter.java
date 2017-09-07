package com.example.ef;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.net.Uri;
import android.os.Looper;
import android.provider.ContactsContract.Contacts.Data;

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
import org.w3c.dom.Text;

import com.example.ef.R.drawable;

/**
 * An easy adapter to map static data to views defined in an XML file. You can
 * specify the data backing the list as an ArrayList of Maps. Each entry in the
 * ArrayList corresponds to one row in the list. The Maps contain the data for
 * each row. You also specify an XML file that defines the views used to display
 * the row, and a mapping from keys in the Map to specific views.
 *
 * Binding data to views occurs in two phases. First, if a
 * {@link android.widget.SimpleAdapter.ViewBinder} is available,
 * {@link ViewBinder#setViewValue(android.view.View, Object, String)} is
 * invoked. If the returned value is true, binding has occurred. If the returned
 * value is false, the following views are then tried in order:
 * <ul>
 * <li>A view that implements Checkable (e.g. CheckBox). The expected bind value
 * is a boolean.
 * <li>TextView. The expected bind value is a string and
 * {@link #setViewText(TextView, String)} is invoked.
 * <li>ImageView. The expected bind value is a resource id or a string and
 * {@link #setViewImage(ImageView, int)} or
 * {@link #setViewImage(ImageView, String)} is invoked.
 * </ul>
 * If no appropriate binding can be found, an {@link IllegalStateException} is
 * thrown.
 */
public class criadapter extends BaseAdapter {
	private String uid;
	private Context context;
	private List<HashMap<String, Object>> mData;
	private List<HashMap<String, Object>> replyData;
	private List<HashMap<String, Object>> likeData;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The context where the View associated with this SimpleAdapter
	 *            is running
	 * @param data
	 *            A List of Maps. Each entry in the List corresponds to one row
	 *            in the list. The Maps contain the data for each row, and
	 *            should include all the entries specified in "from"
	 * @param resource
	 *            Resource identifier of a view layout that defines the views
	 *            for this list item. The layout file should include at least
	 *            those named views defined in "to"
	 * @param from
	 *            A list of column names that will be added to the Map
	 *            associated with each item.
	 * @param to
	 *            The views that should display column in the "from" parameter.
	 *            These should all be TextViews. The first N views in this list
	 *            are given the values of the first N columns in the from
	 *            parameter.
	 * @return
	 * @return
	 */
	public criadapter(Context con, List<HashMap<String, Object>> data, List<HashMap<String, Object>> data2, String id,
			List<HashMap<String, Object>> like) {
		context = con;
		mData = data;
		replyData = data2;
		uid = id;
		likeData = like;
	}

	/**
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return mData.size();
	}

	/**
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return mData.get(position);
	}

	/**
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {
		public TextView content;
		public EditText textView;
		public Button button;
		public LinearLayout layout;
		public TextView name;
		public TextView likenum;
		public ImageButton likethis;
	}
	private int index=-1;
	public View getView(final int position, View convertView, ViewGroup parent) { // 在这里定义每个item显示的内容
		Map thismessage = mData.get(position);
		final String messageid = thismessage.get("messageid").toString();
		final String secondid = thismessage.get("secondid").toString();
		String content = thismessage.get("content").toString();
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_dynamic_item, null);
			viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.replyroom);
			viewHolder.content = (TextView) convertView.findViewById(R.id.dynamic_item_textView_content);
			viewHolder.button = (Button) convertView.findViewById(R.id.dynamic_item_button1);
			viewHolder.textView = (EditText) convertView.findViewById(R.id.dynamic_item_editView);
			viewHolder.name = (TextView) convertView.findViewById(R.id.dynamic_item_textview_head);
			viewHolder.likenum = (TextView) convertView.findViewById(R.id.dynamic_item_textView1);
			viewHolder.likethis = (ImageButton) convertView.findViewById(R.id.dynamic_item_likeButton1);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 点赞信息
		viewHolder.likethis.setBackgroundResource(R.drawable.heixin);
		int likenumber = 0;
		for (int i = 0; i < likeData.size(); i++) {
			Map thislike = likeData.get(i);
			if (thislike.get("messageid").toString().equals(messageid)) {
				likenumber++;
				if (thislike.get("uid").toString().equals(uid)) {
					viewHolder.likethis.setBackgroundResource(R.drawable.hongxin);
				}
			}
		}
		// 每一条动态设置初始数据
		viewHolder.likenum.setText(Integer.toString(likenumber));
		viewHolder.name.setText(secondid);
		if(secondid.length()==0){
			viewHolder.name.setText("匿名侠");
		}
		viewHolder.content.setText(content);
		viewHolder.layout.removeAllViews();

		viewHolder.textView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {

				 if (event.getAction() == MotionEvent.ACTION_UP) {
	                    index = position;
	                }
				return false;
			}
		});
		viewHolder.textView.clearFocus();
        if (index != -1 && index == position) {
            // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
        	viewHolder.textView.requestFocus();
        	viewHolder.textView.setSelection(viewHolder.textView.getText().length());
        }
        
		final int lastlikenumber = likenumber;
		viewHolder.likethis.setOnClickListener(new OnClickListener() {        //点赞和取消赞
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int islike = 0;
				int likeaddress = 0;
				for (int j = 0; j < likeData.size(); j++) {
					if (likeData.get(j).get("uid").toString().equals(uid)
							&& likeData.get(j).get("messageid").toString().equals(messageid)) {
						System.out.println("他赞过这条");
						islike = 1;
						likeaddress = j;
						break;
					}
				}
				if (islike == 0) {
					addlike(messageid);
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("messageid", messageid);
					hashMap.put("uid", uid);
					likeData.add(hashMap);
					viewHolder.likethis.setBackgroundResource(R.drawable.hongxin);
					viewHolder.likenum.setText(Integer.toString(Integer.parseInt(viewHolder.likenum.getText().toString())+1));
				} else {
					if(likeData.get(likeaddress).get("likeid")!=null){
						deletelike(likeData.get(likeaddress).get("likeid").toString());
						viewHolder.likethis.setBackgroundResource(R.drawable.heixin);
						viewHolder.likenum.setText(Integer.toString(Integer.parseInt(viewHolder.likenum.getText().toString())-1));
						likeData.remove(likeaddress);
					}
					else{
						likeData.remove(likeaddress);
						getlikenumber(messageid);
					}
				}
			}
		});

		viewHolder.button.setOnClickListener(new OnClickListener() { // 点击回复的操作

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String replycontent = viewHolder.textView.getText().toString();
				if (replycontent != null && replycontent.length() != 0) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("messageid", messageid);
					hashMap.put("content", replycontent);
					hashMap.put("firstid", uid);
					replyData.add(hashMap);
					addreply(messageid, replycontent, uid, secondid);
					// 创建一个layout
					LinearLayout onereply = new LinearLayout(v.getContext());
					onereply.setOrientation(0);// 0代表HORIZONTAL，1代表VERTICAL
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.setMargins(10, 3, 0, 0);
					onereply.setBackgroundColor(Color.parseColor("#f0f0f0"));
					onereply.setLayoutParams(layoutParams);

					// 创建layout里边的东西
					TextView name = new TextView(v.getContext());
					if(uid.length()==0){
						name.setText("匿名侠:");
					}
					else{
						name.setText(uid + ":");
					}
					name.setTextColor(Color.BLUE);
					name.setPadding(20, 3, 0, 0);
					name.setBackgroundColor(Color.parseColor("#f0f0f0"));

					TextView thiscontent = new TextView(v.getContext());
					thiscontent.setText(replycontent);
					thiscontent.setTextColor(Color.BLACK);
					thiscontent.setPadding(0, 3, 0, 0);
					thiscontent.setBackgroundColor(Color.parseColor("#f0f0f0"));

					onereply.addView(name);
					onereply.addView(thiscontent);
					viewHolder.layout.addView(onereply);

					viewHolder.textView.setText("");
					onereply.setOnLongClickListener(new OnLongClickListener() { // 长按此条回复弹出popwindow

						@Override
						public boolean onLongClick(final View parent) {
							// TODO Auto-generated method stub
							// 关联弹窗的layout，并设置在哪个页面弹出
							View contentView = LayoutInflater.from(parent.getContext())
									.inflate(R.layout.frienditemlongclick, null);
							// 关联弹窗
							final PopupWindow mPopWindow = new PopupWindow(contentView);
							// 设置弹窗的高宽
							mPopWindow.setWidth(200);
							mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
							mPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

							// 设置点击弹窗以外的地方是否会响应点击
							mPopWindow.setOutsideTouchable(false);

							// 设置点击其他地方是否会关闭弹窗
							mPopWindow.setFocusable(true);

							// 获取弹窗的焦点
							TextView tv1 = (TextView) contentView.findViewById(R.id.pop_begintalk);
							TextView tv2 = (TextView) contentView.findViewById(R.id.pop_deletefriend);
							TextView tv3 = (TextView) contentView.findViewById(R.id.pop_message);
							tv1.setText("编辑");
							tv3.setVisibility(View.GONE);
							tv2.setText("删除");
							tv2.setOnClickListener(new OnClickListener() { // 删除此条回复

								@Override
								public void onClick(View v) {
									getReplyData(messageid,replyData.size());
									replyData.remove(replyData.size()-1);
									viewHolder.layout.removeView(parent);
									mPopWindow.dismiss();
									// TODO Auto-generated method stub
								}
							});
							// 显示弹窗
							mPopWindow.showAsDropDown(parent);
							return false;
						}
					});
				}

			}
		});
		int replylength = replyData.size();
		for (int j = 0; j < replylength; j++) {
			final int num = j;
			final Map replyset = replyData.get(j);
			if (thismessage.get("messageid").equals(replyset.get("messageid"))) {
				String replycontent = (String) replyset.get("content");
				final String id = (String) replyset.get("firstid");
				// 创建一个layout
				LinearLayout onereply = new LinearLayout(convertView.getContext());
				onereply.setOrientation(0);// 0代表HORIZONTAL，1代表VERTICAL
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(10, 3, 0, 0);
				onereply.setBackgroundColor(Color.parseColor("#f0f0f0"));
				onereply.setLayoutParams(layoutParams);

				// 创建layout里边的东西
				TextView name = new TextView(convertView.getContext());
				if(id.length()==0){
					name.setText("匿名侠:");
				}
				else{
					name.setText(id + ":");
				}
				name.setTextColor(Color.BLUE);
				name.setPadding(20, 3, 0, 0);
				name.setBackgroundColor(Color.parseColor("#f0f0f0"));

				TextView thiscontent = new TextView(convertView.getContext());
				thiscontent.setText(replycontent);
				thiscontent.setTextColor(Color.BLACK);
				thiscontent.setPadding(0, 3, 0, 0);
				thiscontent.setBackgroundColor(Color.parseColor("#f0f0f0"));

				onereply.addView(name);
				onereply.addView(thiscontent);
				viewHolder.layout.addView(onereply);
				onereply.setOnLongClickListener(new OnLongClickListener() { // 长按此条回复弹出popwindow

					@Override
					public boolean onLongClick(final View parent) {
						// TODO Auto-generated method stub
						// 关联弹窗的layout，并设置在哪个页面弹出
						View contentView = LayoutInflater.from(parent.getContext())
								.inflate(R.layout.frienditemlongclick, null);
						// 关联弹窗
						final PopupWindow mPopWindow = new PopupWindow(contentView);
						// 设置弹窗的高宽
						mPopWindow.setWidth(200);
						mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
						mPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));

						// 设置点击弹窗以外的地方是否会响应点击
						mPopWindow.setOutsideTouchable(false);

						// 设置点击其他地方是否会关闭弹窗
						mPopWindow.setFocusable(true);

						// 获取弹窗的焦点
						TextView tv1 = (TextView) contentView.findViewById(R.id.pop_begintalk);
						TextView tv2 = (TextView) contentView.findViewById(R.id.pop_deletefriend);
						TextView tv3 = (TextView) contentView.findViewById(R.id.pop_message);
						tv1.setText("编辑");
						tv2.setText("回复");
						tv3.setVisibility(View.GONE);
						if (id.equals(uid)) {
							tv2.setText("删除");
							tv2.setOnClickListener(new OnClickListener() { // 删除此条回复

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									mPopWindow.dismiss();
									replyData.remove(replyset);
									viewHolder.layout.removeView(parent);
									if(replyset.get("replyid")!=null){
										String replyid = replyset.get("replyid").toString();
										deletereply(replyid);
									}
									else{
										getReplyData(messageid,num);
									}
								}
							});
						}
						// 显示弹窗
						mPopWindow.showAsDropDown(parent);
						return false;
					}
				});
			}
		}

		return convertView;
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

	public void deletereply(final String replyid) {
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
					String data = "func=1062" + "&zson={id:\"" + URLEncoder.encode(replyid, "UTF-8") + "\"}";
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

							System.out.println("删除成功");

						} else {

							System.out.println("删除失败");
						}

					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void addlike(final String messageid) {
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
					String data = "func=1041" + "&zson={messageid:\"" + URLEncoder.encode(messageid, "UTF-8")
							+ "\",uid:\"" + URLEncoder.encode(uid, "UTF-8") + "\"}";
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

							System.out.println("点赞成功");

						} else {

							System.out.println("点赞失败");
						}

					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void deletelike(final String likeid) {
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
					String data = "func=1042" + "&zson={id:\"" + URLEncoder.encode(likeid, "UTF-8") + "\"}";
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

							System.out.println("取消赞成功");

						} else {

							System.out.println("取消失败");
						}

					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void getReplyData(final String messageid,final int num) {

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
					String data = "func=1063" + "&zson={messageid:\""+ URLEncoder.encode(messageid, "UTF-8") +"\"}";
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
							for (int i = 0; i <resultJsonArray.length(); i++) {
								JSONObject jsonObject = resultJsonArray.getJSONObject(i);

								if(jsonObject.getString("messageid").equals(messageid)&&jsonObject.getString("firstid").equals(uid)&&i==(num-1)){
									deletereply(jsonObject.getString("id"));
									break;
								}
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

	public void getlikenumber(final String messageid) {

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
					String data = "func=1043" + "&zson={messageid:\""+ URLEncoder.encode(messageid, "UTF-8") +"\"}";
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
							for (int i = 0; i < resultJsonArray.length(); i++) {
								JSONObject jsonObject = resultJsonArray.getJSONObject(i);
								if(jsonObject.getString("uid").equals(uid)){
									HashMap<String, Object> hashMap = new HashMap<String, Object>();
									hashMap.put("messageid", jsonObject.getString("messageid"));
									hashMap.put("likeid", jsonObject.getString("id"));
									hashMap.put("uid", jsonObject.getString("uid"));
									likeData.add(hashMap);
								}
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

}
