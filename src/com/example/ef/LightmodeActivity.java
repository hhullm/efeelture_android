package com.example.ef;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

public class LightmodeActivity extends Activity {
	private ImageButton b1, b2, b3, b4, b_back;
	private String level = "";
	private String uid = "";
	private String hid = "";
	private String uname = "";
	private String hname = "";
	getip get = new getip();
	private String uipaddress = get.getLocalHostIp();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lightmode);
		SharedPreferences cys2 = getSharedPreferences("syy", Context.MODE_PRIVATE);
		hid = cys2.getString("hid", "");
		hname = cys2.getString("hname", "");
		uid = cys2.getString("id", "");
		uname = cys2.getString("uname", "");
		b1 = (ImageButton) findViewById(R.id.light_mode_work);
		b2 = (ImageButton) findViewById(R.id.light_mode_party);
		b3 = (ImageButton) findViewById(R.id.light_mode_rest);
		b4 = (ImageButton) findViewById(R.id.light_mode_mingxiang);
		b_back = (ImageButton) findViewById(R.id.light_mode_imageButton5);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						level = "1";
						loginByPost(level); // ����loginByPost����
					};
				}.start();
				b1.setImageResource(R.drawable.workpress);
				b2.setImageResource(R.drawable.party);
				b3.setImageResource(R.drawable.rest);
				b4.setImageResource(R.drawable.mingxiang1);
				// Animation aiAnimation =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale);
				// b1.setAnimation(aiAnimation);
				// Animation aiAnimation1 =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale2);
				// b2.setAnimation(aiAnimation1);
				// Animation aiAnimation2 =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale);
				// b3.setAnimation(aiAnimation2);
				// Animation aiAnimation3 =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale);
				// b4.setAnimation(aiAnimation3);
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						level = "2";
						loginByPost(level); // ����loginByPost����
					};
				}.start();
				b1.setImageResource(R.drawable.work);
				b2.setImageResource(R.drawable.partypress);
				b3.setImageResource(R.drawable.rest);
				b4.setImageResource(R.drawable.mingxiang1);
				// Animation aiAnimation =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale);
				// b1.setAnimation(aiAnimation);
				// Animation aiAnimation1 =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale);
				// b2.setAnimation(aiAnimation1);
				// Animation aiAnimation2 =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale2);
				// b3.setAnimation(aiAnimation2);
				// Animation aiAnimation3 =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale);
				// b4.setAnimation(aiAnimation3);
			}
		});
		b3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						level = "3";
						loginByPost(level); // ����loginByPost����
					};
				}.start();
				b1.setImageResource(R.drawable.work);
				b2.setImageResource(R.drawable.party);
				b3.setImageResource(R.drawable.restpress);
				b4.setImageResource(R.drawable.mingxiang1);
				// Animation aiAnimation =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale);
				// b1.setAnimation(aiAnimation);
				// Animation aiAnimation1 =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale);
				// b2.setAnimation(aiAnimation1);
				// Animation aiAnimation2 =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale);
				// b3.setAnimation(aiAnimation2);
				// Animation aiAnimation3 =
				// AnimationUtils.loadAnimation(LightmodeActivity.this,
				// R.anim.anim_scale2);
				// b4.setAnimation(aiAnimation3);
			}
		});
		b4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					public void run() {
						level = "4";
						loginByPost(level); // ����loginByPost����
					};
				}.start();
				b1.setImageResource(R.drawable.work);
				b2.setImageResource(R.drawable.party);
				b3.setImageResource(R.drawable.rest);
				b4.setImageResource(R.drawable.mingxiang);
			}
		});
		b_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LightmodeActivity.this.finish();

			}
		});
	}

	public void loginByPost(String clevel) {

		try {
			// ����ĵ�ַ
			String spec = "http://115.159.120.220:8080/efeelture/mobileAppServlet";
			// ���ݵ�ַ����URL����
			URL url = new URL(spec);
			// ����URL���������
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			// ��������ķ�ʽ
			urlConnection.setRequestMethod("POST");
			// ��������ĳ�ʱʱ��
			urlConnection.setReadTimeout(5000);
			urlConnection.setConnectTimeout(5000);

			// ���ݵ�����
			String data = "func=1002" + "&zson={uid:\"" + URLEncoder.encode(uid, "UTF-8") + "\",hid:\""
					+ URLEncoder.encode(hid, "UTF-8") + "\"" + "\",uname:\"" + URLEncoder.encode(uname, "UTF-8") + "\""
					+ "\",hname:\"" + URLEncoder.encode(hname, "UTF-8") + "\"" + "\",uipaddress:\""
					+ URLEncoder.encode(uipaddress, "UTF-8") + "\"" + "\",clevel:\""
					+ URLEncoder.encode(clevel, "UTF-8") + "\"}";
			// ���������ͷ
			urlConnection.setRequestProperty("Connection", "keep-alive");
			// ���������ͷ
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// ���������ͷ
			urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
			// ���������ͷ
			urlConnection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

			urlConnection.setDoOutput(true); // ����POST������������������
			urlConnection.setDoInput(true); // ����POST�������������������
											// setDoInput��Ĭ��ֵ����true
			// ��ȡ�����
			OutputStream os = urlConnection.getOutputStream();
			os.write(data.getBytes());
			os.flush();
			if (urlConnection.getResponseCode() == 200) {
				// ��ȡ��Ӧ������������
				InputStream is = urlConnection.getInputStream();
				// �����ֽ����������
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// �����ȡ�ĳ���
				int len = 0;
				// ���建����
				byte buffer[] = new byte[1024];
				// ���ջ������Ĵ�С��ѭ����ȡ
				while ((len = is.read(buffer)) != -1) {
					// ���ݶ�ȡ�ĳ���д�뵽os������
					baos.write(buffer, 0, len);
				}
				// �ͷ���Դ
				is.close();
				baos.close();
				// �����ַ���
				String result = new String(baos.toByteArray());

				JSONObject dataJson = new JSONObject(result);
				String code1 = dataJson.getString("resultCode");
				if (code1.equals("999")) {

					Toast.makeText(this, "设置成功！", Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(this, "设置失败！", Toast.LENGTH_SHORT).show();
				}

			} else {
				System.out.println("����ʧ��.........");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
