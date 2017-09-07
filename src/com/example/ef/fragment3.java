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

import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

//指向朋友圈界面的类
public class fragment3 extends Fragment implements OnItemClickListener {
	MainActivity main = new MainActivity();
	private int b = main.a;
	private Button bFri;
	private ListView list;
	private SimpleAdapter simp_adapter;
	private List<HashMap<String, Object>> Data = new ArrayList<HashMap<String, Object>>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.activity_friendcri, null);
		// bFri = (Button) view.findViewById(R.id.friendcri_button1);
		// bFri.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent2 = new Intent(getActivity(),
		// DynamicMainActivity.class);
		// startActivity(intent2);
		//
		// }
		// });
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map1.put("image", R.drawable.circle);
		map1.put("text", "朋友圈");
		Data.add(map1);
		map2.put("image", R.drawable.people);
		map2.put("text", "附近的人");
		Data.add(map2);
		map3.put("image", R.drawable.shoppig);
		map3.put("text", "购物");
		Data.add(map3);
		map4.put("image", R.drawable.qrcode);
		map4.put("text", "扫一扫");
		Data.add(map4);
		simp_adapter = new SimpleAdapter(getActivity(), Data, R.layout.setting_listviewitem,
				new String[] { "image", "text" },
				new int[] { R.id.setting_listviewitem_image, R.id.setting_listviewitem_text, });
		list = (ListView) view.findViewById(R.id.friendlistview);
		list.setAdapter(simp_adapter);
		list.setOnItemClickListener(this);
		return view;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (position == 0) {
			DynamicMainActivity.friendidformessage ="";
			Intent intent2 = new Intent(getActivity(), DynamicMainActivity.class);
			startActivity(intent2);
		}
	}

}
