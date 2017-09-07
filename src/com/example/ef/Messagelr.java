package com.example.ef;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.service.Connectserver;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import bean.TranObject;
import bean.Type;
import bean.User;
import bean.chatcontent;

public class Messagelr extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private Button mBtnSend;
	private TextView mBtnRcd;
	private Button mBtnBack;
	private EditText mEditTextContent;
	private RelativeLayout mBottom;
	private ListView mListView;
	public static ChatMsgViewAdapter mAdapter;
	public static List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private boolean isShosrt = false;
	private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding, voice_rcd_hint_tooshort;
	private ImageView img1, sc_img1;
	private SoundMeter mSensor;
	private View rcChat_popup;
	private LinearLayout del_re;
	private ImageView chatting_mode_btn, volume;
	private boolean btn_vocie = false;
	private int flag = 1;
	private Handler mHandler = new Handler();
	private String voiceName;
	private long startVoiceT, endVoiceT;

	public static String talkto = "";

	private String uname = "";
	private String uid = "";
	private String[] msgArray;
	private String talkcontent = "";

	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private Thread listen = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		SharedPreferences cys = getSharedPreferences("syy", Context.MODE_PRIVATE);
		TextView titlename = (TextView) findViewById(R.id.talkto);
		titlename.setText(talkto);
		uname = cys.getString("uname", "");
		uid = cys.getString("uid", "");
		SharedPreferences content = getSharedPreferences("talkcontent", Context.MODE_PRIVATE);
		talkcontent = content.getString(uid + talkto, "");
		msgArray = talkcontent.split("##@@##");// 聊天内容存放在uid与talkto命名
												// 的文件中，每条消息用“##@@##”隔开内有类型、内容及时间，用&&隔开
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();

		initData();
		out = Connectserver.out;
	} 

	public void initView() {
		mListView = (ListView) findViewById(R.id.listview);

		mBtnSend = (Button) findViewById(R.id.btn_send);// 发送
		mBtnRcd = (TextView) findViewById(R.id.btn_rcd);// 按住说话
		mBtnSend.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.btn_back);// 返回
		mBottom = (RelativeLayout) findViewById(R.id.btn_bottom);// 底部栏
		mBtnBack.setOnClickListener(this);
		chatting_mode_btn = (ImageView) this.findViewById(R.id.ivPopUp);// 底部栏右

		volume = (ImageView) this.findViewById(R.id.volume);// 语音
		rcChat_popup = this.findViewById(R.id.rcChat_popup);// 录音显示ui
		img1 = (ImageView) this.findViewById(R.id.img1);
		sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);// 取消语音发送
		del_re = (LinearLayout) this.findViewById(R.id.del_re);
		voice_rcd_hint_rcding = (LinearLayout) this.findViewById(R.id.voice_rcd_hint_rcding);
		voice_rcd_hint_loading = (LinearLayout) this.findViewById(R.id.voice_rcd_hint_loading);
		voice_rcd_hint_tooshort = (LinearLayout) this.findViewById(R.id.voice_rcd_hint_tooshort);

		mSensor = new SoundMeter();
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);// 文本输入

		// ���������л���ť
		chatting_mode_btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (btn_vocie) {
					mBtnRcd.setVisibility(View.GONE);
					mBottom.setVisibility(View.VISIBLE);
					btn_vocie = false;
					chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_msg_btn);

				} else {
					mBtnRcd.setVisibility(View.VISIBLE);
					mBottom.setVisibility(View.GONE);
					chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_voice_btn);
					btn_vocie = true;
				}
			}
		});
		mBtnRcd.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// ��������¼�ư�ťʱ����falseִ�и���OnTouch
				return false;
			}
		});
	}

	public void initData() {
		mDataArrays =  new ArrayList<ChatMsgEntity>();
		for (int i = 1; i < msgArray.length; i++) {
			if (msgArray[i] != "" && msgArray[i] != null) {
				System.out.println("22333" + msgArray[i]);
				String[] thiscontent = msgArray[i].split("&&");
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setDate(thiscontent[2]);
				if (thiscontent[0].equals("1")) {
					entity.setName(talkto);
					entity.setMsgType(true);
				} else {
					entity.setName(uname);
					entity.setMsgType(false);
				}

				entity.setText(thiscontent[1]);
				mDataArrays.add(entity);
			}
		}

		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_send:
			send();
			break;
		case R.id.btn_back:
			Messagelr.this.finish();
			break;
		}
	}

	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {
			if(out==null){
				Toast.makeText(Messagelr.this, "发送失败", Toast.LENGTH_SHORT).show();
			}
			else{
				try {
					TranObject tran = new TranObject();
					tran.setType(Type.SENDMESSAGE);
					chatcontent chat = new chatcontent();
					chat.setContent(contString);
					chat.setToid(talkto);
					chat.setSendid(uid);
					tran.setObject(chat);
					out.writeObject(tran);
					out.flush();
					SharedPreferences contentthis = getSharedPreferences("talkcontent", Context.MODE_PRIVATE);
					Editor editor = contentthis.edit();
					editor.putString(uid + talkto, talkcontent + "##@@##" + "2" + "&&" + contString + "&&" + "2017-7-15 20:59");
					editor.commit();
					talkcontent = contentthis.getString(uid + talkto, "");
					ChatMsgEntity entity = new ChatMsgEntity();
					entity.setDate(getDate());
					entity.setName(uname);
					entity.setMsgType(false);
					entity.setText(contString);
					mDataArrays.add(entity);
					mAdapter.notifyDataSetChanged();
					mEditTextContent.setText("");
					mListView.setSelection(mListView.getCount() - 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(Messagelr.this, "发送失败", Toast.LENGTH_SHORT).show();
				}
			}
			
		}
	}

	private void send2(String content) {
		ChatMsgEntity entity = new ChatMsgEntity();
		entity.setDate(getDate());
		entity.setName(talkto);
		entity.setMsgType(true);
		entity.setText(content);
		mDataArrays.add(entity);
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(mListView.getCount() - 1);
		SharedPreferences contentthis = getSharedPreferences("talkcontent", Context.MODE_PRIVATE);
		Editor editor = contentthis.edit();
		editor.putString(uid + talkto, talkcontent +"##@@##1&&"+content+"&&2017-7-15 20:59");
		editor.commit();
	}

	private String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);

		return sbBuffer.toString();
	}

	// ��������¼�ư�ťʱ
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!Environment.getExternalStorageDirectory().exists()) {
			Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
			return false;
		}

		if (btn_vocie) {
			System.out.println("1");
			int[] location = new int[2];
			mBtnRcd.getLocationInWindow(location); // ��ȡ�ڵ�ǰ�����ڵľ�������
			int btn_rc_Y = location[1];
			int btn_rc_X = location[0];
			int[] del_location = new int[2];
			del_re.getLocationInWindow(del_location);
			int del_Y = del_location[1];
			int del_x = del_location[0];
			if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
				if (!Environment.getExternalStorageDirectory().exists()) {
					Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
					return false;
				}
				System.out.println("2");
				if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {// �ж����ư��µ�λ���Ƿ�������¼�ư�ť�ķ�Χ��
					System.out.println("3");
					mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					rcChat_popup.setVisibility(View.VISIBLE);
					voice_rcd_hint_loading.setVisibility(View.VISIBLE);
					voice_rcd_hint_rcding.setVisibility(View.GONE);
					voice_rcd_hint_tooshort.setVisibility(View.GONE);
					mHandler.postDelayed(new Runnable() {
						public void run() {
							if (!isShosrt) {
								voice_rcd_hint_loading.setVisibility(View.GONE);
								voice_rcd_hint_rcding.setVisibility(View.VISIBLE);
							}
						}
					}, 300);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					startVoiceT = SystemClock.currentThreadTimeMillis();
					voiceName = startVoiceT + ".amr";
					start(voiceName);
					flag = 2;
				}
			} else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {// �ɿ�����ʱִ��¼�����
				System.out.println("4");
				mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
				if (event.getY() >= del_Y && event.getY() <= del_Y + del_re.getHeight() && event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					rcChat_popup.setVisibility(View.GONE);
					img1.setVisibility(View.VISIBLE);
					del_re.setVisibility(View.GONE);
					stop();
					flag = 1;
					File file = new File(android.os.Environment.getExternalStorageDirectory() + "/" + voiceName);
					if (file.exists()) {
						file.delete();
					}
				} else {

					voice_rcd_hint_rcding.setVisibility(View.GONE);
					stop();
					endVoiceT = SystemClock.currentThreadTimeMillis();
					flag = 1;
					int time = (int) ((endVoiceT - startVoiceT) / 1000);
					if (time < 1) {
						isShosrt = true;
						voice_rcd_hint_loading.setVisibility(View.GONE);
						voice_rcd_hint_rcding.setVisibility(View.GONE);
						voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
						mHandler.postDelayed(new Runnable() {
							public void run() {
								voice_rcd_hint_tooshort.setVisibility(View.GONE);
								rcChat_popup.setVisibility(View.GONE);
								isShosrt = false;
							}
						}, 500);
						return false;
					}
					ChatMsgEntity entity = new ChatMsgEntity();
					entity.setDate(getDate());
					entity.setName("�߸�˧");
					entity.setMsgType(false);
					entity.setTime(time + "\"");
					entity.setText(voiceName);
					mDataArrays.add(entity);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mListView.getCount() - 1);
					rcChat_popup.setVisibility(View.GONE);

				}
			}
			if (event.getY() < btn_rc_Y) {// ���ư��µ�λ�ò�������¼�ư�ť�ķ�Χ��
				System.out.println("5");
				Animation mLitteAnimation = AnimationUtils.loadAnimation(this, R.anim.cancel_rc);
				Animation mBigAnimation = AnimationUtils.loadAnimation(this, R.anim.cancel_rc2);
				img1.setVisibility(View.GONE);
				del_re.setVisibility(View.VISIBLE);
				del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
				if (event.getY() >= del_Y && event.getY() <= del_Y + del_re.getHeight() && event.getX() >= del_x
						&& event.getX() <= del_x + del_re.getWidth()) {
					del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
					sc_img1.startAnimation(mLitteAnimation);
					sc_img1.startAnimation(mBigAnimation);
				}
			} else {

				img1.setVisibility(View.VISIBLE);
				del_re.setVisibility(View.GONE);
				del_re.setBackgroundResource(0);
			}
		}
		return super.onTouchEvent(event);
	}

	private static final int POLL_INTERVAL = 300;

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			double amp = mSensor.getAmplitude();
			updateDisplay(amp);
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);

		}
	};

	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		volume.setImageResource(R.drawable.amp1);
	}

	private void updateDisplay(double signalEMA) {

		switch ((int) signalEMA) {
		case 0:
		case 1:
			volume.setImageResource(R.drawable.amp1);
			break;
		case 2:
		case 3:
			volume.setImageResource(R.drawable.amp2);

			break;
		case 4:
		case 5:
			volume.setImageResource(R.drawable.amp3);
			break;
		case 6:
		case 7:
			volume.setImageResource(R.drawable.amp4);
			break;
		case 8:
		case 9:
			volume.setImageResource(R.drawable.amp5);
			break;
		case 10:
		case 11:
			volume.setImageResource(R.drawable.amp6);
			break;
		default:
			volume.setImageResource(R.drawable.amp7);
			break;
		}
	}

	public void head_xiaohei(View v) { // ������ ���ذ�ť

	}
}