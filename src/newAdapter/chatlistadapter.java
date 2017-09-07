package newAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ef.R;
import com.example.ef.fragment2_1;
import com.fortysevendeg.swipelistview.SwipeListView;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class chatlistadapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private SwipeListView mSwipeListView;
	private List<HashMap<String, Object>> mData;
	private fragment2_1 act;
	private Handler handler = null;
	private String friendid = "";
	private String uid = "";
	private String list = "";

	public chatlistadapter(Context con, List<HashMap<String, Object>> data, SwipeListView list,
			fragment2_1 activity,String uid) {
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
		TextView chat;
		LinearLayout main;
		LinearLayout head;
	}

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
			holder.head = (LinearLayout) convertView.findViewById(R.id.friendhead);
			holder.chat = (TextView) convertView.findViewById(R.id.friend_textView4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.head.setVisibility(View.GONE);
		handler = new Handler();
		holder.chat.setText(thisfriend.get("text2").toString());
		holder.name.setText(thisfriend.get("text").toString());
		holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				friendid = thisfriend.get("text").toString();
				list = "";
                for(int i=0;i<mData.size();i++){
                	if(i == position){
                		
                	}
                	else if(list.length()==0){
                		list =  mData.get(0).get("text").toString();
                	}
                	else{
                		list = list +","+ mData.get(i).get("text").toString();
                	}
                }
                handler.post(saveData); 
                mData.remove(position);
                notifyDataSetChanged();
			}
		});
		holder.message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				friendid = thisfriend.get("text").toString();
				handler.post(friendmessage);
			}
		});

		return convertView;
	}
	// 构建Runnable对象，在runnable中更新界面
		Runnable friendmessage = new Runnable() {
			@Override
			public void run() {
				// 更新界面
                act.turntofriendmessage(friendid);
			}

		};
		Runnable saveData = new Runnable() {
			@Override
			public void run() {
				// 更新界面
				act.savaData(list,friendid);
			}

		};
}