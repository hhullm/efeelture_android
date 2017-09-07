package newAdapter;

import com.example.ef.R;
import com.example.ef.R.id;
import com.example.ef.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

public class TextadapterActivity extends BaseAdapter {
	Context mCtx;

	public TextadapterActivity(Context ctx) {
		super();
		mCtx = ctx;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mCtx);
			convertView = inflater.inflate(R.layout.activity_dynamic_item, null);
			holder.btn1 = (Button) convertView.findViewById(R.id.dynamic_item_button1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(mCtx, "点击成功", Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	class ViewHolder {
		public Button btn1;
	}
}