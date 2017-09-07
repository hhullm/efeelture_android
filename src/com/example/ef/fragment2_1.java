package com.example.ef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import newAdapter.chatlistadapter;

public class fragment2_1 extends Fragment{
	MainActivity main = new MainActivity();
	private SwipeListView list1;
	public static chatlistadapter simp_adapter;
	public static List<HashMap<String, Object>> Data = new ArrayList<HashMap<String, Object>>();
	private String[] talktolist;
	private String friendid = "";
    public static String talkto;
    private String uid;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.fragment2_1, null);
		SharedPreferences information = getActivity().getSharedPreferences("syy", Context.MODE_PRIVATE);
		uid = information.getString("uid", "");
		SharedPreferences talklist = getActivity().getSharedPreferences("talklist", Context.MODE_PRIVATE);// 获取聊天人列表
		talkto = talklist.getString(uid, "");
		Data.clear();
		if (talkto != "") {
			talktolist = talkto.split(",");
			for (int i = 1; i < talktolist.length; i++) {
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("image", R.drawable.girl111);
				map1.put("text", talktolist[i]);
				map1.put("text2", "在？吗♂♂♂♂♂♂");
				if(talktolist[i].length()!=0){
					Data.add(map1);
				}
			}
		}
		if(Data.size()==0){
			Toast.makeText(getActivity(), "暂无聊天", Toast.LENGTH_SHORT).show();
		}
		simp_adapter = new chatlistadapter(getActivity(), Data,list1,this,uid);
		list1 = (SwipeListView) view.findViewById(R.id.chatlist_swipelistview);
		 try 
		 {
			 list1.setAdapter(simp_adapter);
			 list1.setSwipeListViewListener(new BaseSwipeListViewListener(){        //点击跳转到聊天界面
					public void onClickFrontView(int position)  
		            {  
						// 内部存储有关于聊天人的id
						friendid = Data.get(position).get("text").toString();
						System.out.println(friendid);
						Messagelr.talkto = friendid;
						Intent intent = new Intent(getActivity(), Messagelr.class);
						startActivity(intent);
		            } 
				});
				final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.message_container);
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
								simp_adapter.notifyDataSetChanged();
								// TODO Auto-generated method stub
								swipeRefreshLayout.setRefreshing(false);
							}
						}, 1500);
					}
				});
		 } 
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }
		return view;
	}
	public void savaData(String list,String friendid){
		System.out.println("改变了列表");
		talkto = list;
		Data.clear();
		talktolist = talkto.split(",");
		if (talkto != "") {
			System.out.println(talkto);
			for (int i = 0; i < talktolist.length; i++) {
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("image", R.drawable.girl111);
				map1.put("text", talktolist[i]);
				map1.put("text2", "在？吗♂♂♂♂♂♂");
				Data.add(map1);
			}
		}
		SharedPreferences talklist = getActivity().getSharedPreferences("talklist", Context.MODE_PRIVATE);// 获取聊天人列表
		Editor editor = talklist.edit();
		editor.remove(uid);
		editor.putString(uid, list);
		editor.commit();
		System.out.println(talklist.getString(uid, ""));
		SharedPreferences content =  getActivity().getSharedPreferences("talkcontent", Context.MODE_PRIVATE);
		Editor editor2 = content.edit();
		editor2.remove(uid+friendid);
		editor2.commit();
	}
	public void refresh() {
		Data.clear();
		talktolist = talkto.split(",");
		if (talkto != "") {
			System.out.println(talkto);
			for (int i = 0; i < talktolist.length; i++) {
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("image", R.drawable.girl111);
				map1.put("text", talktolist[i]);
				map1.put("text2", "在？吗♂♂♂♂♂♂");
				Data.add(map1);
			}
		}
		simp_adapter.notifyDataSetChanged();
		if (talktolist.length == 0 || talkto == "") {
			Toast.makeText(getActivity(), "暂无消息，快去打招呼吧", Toast.LENGTH_SHORT).show();
		}
	}
	public void turntofriendmessage(String friendid){
		DynamicMainActivity.friendidformessage = friendid;
		Intent intent = new Intent(getActivity(), DynamicMainActivity.class);
		startActivity(intent);
    }
}
