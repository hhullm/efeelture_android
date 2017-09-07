package com.example.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;

import com.example.ef.ChatMsgEntity;
import com.example.ef.Messagelr;
import com.example.ef.R;
import com.example.ef.fragment2;
import com.example.ef.fragment2_1;
import com.example.ef.R.drawable;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import bean.TranObject;
import bean.Type;
import bean.User;
import bean.chatcontent;

public class Connectserver extends Service {

	public static ObjectOutputStream out = null;
	public static ObjectInputStream in = null;
	public static String uid = "";
	public static Thread listen = null;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("MyService", "正在连接服务器");  
		startconnect();
	}

	private void startconnect() {
		listen = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO
				// 在这里进行 http request.网络请求相关操作
				try {
					Socket client;
					client = new Socket("192.168.1.104", 6666);
					out = new ObjectOutputStream(client.getOutputStream());
					in = new ObjectInputStream(client.getInputStream());
					TranObject tran = new TranObject();
					tran.setType(Type.LOGIN);
					User user = new User();
					user.setUid(uid);
					tran.setObject(user);
					out.writeObject(tran);
					out.flush();
					while (true) { // 监听接收服务器发来数据
						try {
							chatcontent chat = (chatcontent) in.readObject();
							Message msg = new Message();
							Bundle data = new Bundle();
							data.putString("content", chat.getContent());
							data.putString("sendid", chat.getSendid());
							msg.setData(data);
							handler.sendMessage(msg);

						} catch (OptionalDataException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		});
		listen.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String content = data.getString("content");
			String sendid = data.getString("sendid");
			int have = 0;
			String[] list = fragment2_1.talkto.split(",");
			for (int i = 0; i < list.length; i++) {
				if (list[i].equals(sendid)) {
					if (Messagelr.talkto.equals(sendid)) {
						ChatMsgEntity entity = new ChatMsgEntity();
						entity.setDate(getDate());
						entity.setName(sendid);
						entity.setMsgType(true);
						entity.setText(content);
						Messagelr.mDataArrays.add(entity);
						Messagelr.mAdapter.notifyDataSetChanged();
					}
					have = 1;
					SharedPreferences contentthis = getSharedPreferences("talkcontent", Context.MODE_PRIVATE);
					String talkcontent = contentthis.getString(uid + sendid, "");
					if (talkcontent == null || talkcontent.length() == 0) {
						Editor editor = contentthis.edit();
						editor.putString(uid + sendid, "1&&" + content + "&&2017-7-15 20:59");
						editor.commit();
					} else {
						Editor editor = contentthis.edit();
						editor.putString(uid + sendid, talkcontent + "##@@##1&&" + content + "&&2017-7-15 20:59");
						editor.commit();
					}
				}
			}
			if (have == 0) {
				SharedPreferences talklist = getSharedPreferences("talklist", Context.MODE_PRIVATE);
				Editor editor2 = talklist.edit();
				editor2.putString(uid, talklist.getString(uid, "") + "," + sendid);
				editor2.commit();
				SharedPreferences contentthis = getSharedPreferences("talkcontent", Context.MODE_PRIVATE);
				Editor editor = contentthis.edit();
				editor.putString(uid + sendid, content + "&&2017-7-15 20:59");
				editor.commit();
				fragment2_1.talkto = fragment2_1.talkto + "," + sendid;
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("image", R.drawable.girl111);
				map1.put("text", sendid);
				map1.put("text2", "在？吗♂♂♂♂♂♂");
				fragment2_1.Data.add(map1);
				fragment2_1.simp_adapter.notifyDataSetChanged();
			}
			// TODO
			// UI界面的更新等相关操作
		}
	};

	private String getDate() {
		
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);

		return sbBuffer.toString();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


}