package com.example.ef;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ef.borderText.BorderTextView;
import com.example.ef.calendar.LunarCalendar;
import com.example.ef.constant.CalendarConstant;
import com.example.ef.dao.ScheduleDAO;
import com.example.ef.vo.ScheduleDateTag;
import com.example.ef.vo.ScheduleVO;

/**
 * ����ճ�������
 * @author jack_peng
 *
 */
public class ScheduleView extends Activity {

	private LunarCalendar lc = null;
	private ScheduleDAO dao = null;
	private BorderTextView scheduleType = null;
	private BorderTextView dateText = null;
	private BorderTextView scheduleTop = null;
	private EditText scheduleText = null;
	private BorderTextView scheduleSave = null;  //���水ťͼƬ
	private static int hour = -1;
	private static int minute = -1;
	private static ArrayList<String> scheduleDate = null;
	private ArrayList<ScheduleDateTag> dateTagList = new ArrayList<ScheduleDateTag>();
	private String scheduleYear = "";
	private String scheduleMonth = "";
	private String scheduleDay = "";
	private String week = "";
	private ScheduleVO scheduleVO;
	//��ʱ����ʱ�������
	private String tempMonth;
	private String tempDay;

	private String[] sch_type = CalendarConstant.sch_type;
	private String[] remind = CalendarConstant.remind;
	private int sch_typeID = 0;   //�ճ�����
	private int remindID = 0;     //��������
	private int mSelectedItem=0;
	
	private static String schText = "";
    int schTypeID = 0;
	public ScheduleView() {
		lc = new LunarCalendar();
		dao = new ScheduleDAO(this);
	}
	private Calendar mCalendar = Calendar.getInstance();
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		ObjectPool.mAlarmHelper = new AlarmHelper(this);
		scheduleTop = (BorderTextView) findViewById(R.id.scheduleTop);
		scheduleType = (BorderTextView) findViewById(R.id.scheduleType);
		scheduleSave = (BorderTextView) findViewById(R.id.save);
		scheduleType.setBackgroundColor(Color.WHITE);
		scheduleType.setText(sch_type[0]);
		dateText = (BorderTextView) findViewById(R.id.scheduleDate);
		dateText.setBackgroundColor(Color.WHITE);
		scheduleText = (EditText) findViewById(R.id.scheduleText);
		scheduleText.setBackgroundColor(Color.WHITE);
		if(schText != null){
			//��ѡ���ճ�����֮ǰ�Ѿ��������ճ̵���Ϣ��������ת��ѡ���ճ�����֮ǰӦ�����ճ���Ϣ���浽schText�У�������ʱ�ٴο���ȡ�á�
			scheduleText.setText(schText);
			//һ���������֮���Ӧ�ý��˾�̬��������Ϊ�գ�
			schText = "";  
		}

		Date date = new Date();
		if(hour == -1 && minute == -1){
			hour = date.getHours();
			minute = date.getMinutes();
		}
		dateText.setText(getScheduleDate());

		//����ճ�����
		scheduleType.setOnClickListener(new OnClickListener() {
			  
			
			public void onClick(View v) {
				schText = scheduleText.getText().toString();
				AlertDialog mDialog = new AlertDialog.Builder(ScheduleView.this)  
				.setTitle("日程类型")  
                   .setIcon(android.R.drawable.ic_dialog_alert)  
                    .setSingleChoiceItems(sch_type, 0,  
                            new DialogInterface.OnClickListener() {  

                               public void onClick(DialogInterface dialog,  
                                       int which) {  
                            	   mSelectedItem = which;
                              } 
                          }) 
                    .setPositiveButton("确定",  
                            new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface dialog,  
                                        int which) {  
                                	scheduleType.setText(sch_type[mSelectedItem]);
                                }  
                            })  
                   .setNegativeButton("取消",  
                            new DialogInterface.OnClickListener() {  

                               public void onClick(DialogInterface dialog,  
                                       int which) {  
                                }  
                            }).create(); 
				mDialog.show();
			}
				
			});

		//���ʱ��
		dateText.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {

				new TimePickerDialog(ScheduleView.this, new OnTimeSetListener() {
					
					
					public void onTimeSet(TimePicker view, int hourOfDay, int min) {

						hour = hourOfDay;
						minute = min;
						dateText.setText(getScheduleDate());
					}
				}, hour, minute, true).show();
			}
		});
		
		//�����ճ���Ϣ
		scheduleSave.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				if(TextUtils.isEmpty(scheduleText.getText().toString())){
					//�ж�������Ƿ�Ϊ��
					new AlertDialog.Builder(ScheduleView.this).setTitle("输入日程").setMessage("日程信息不能为空").setPositiveButton("确认", null).show();
				}else{
//					Calendar mCalendar1 = Calendar.getInstance();
//					mCalendar1.set(Calendar.MILLISECOND, 0); 
//					Log.i("t+++i++++m", Integer.parseInt(scheduleYear)+"+++++++++"+Integer.parseInt(tempMonth)+"++++++"+Integer.parseInt(tempDay)+"+++++++"+hour+"+++++"+minute);
//                	mCalendar1.set(Integer.parseInt(scheduleYear), Integer.parseInt(tempMonth), Integer.parseInt(tempDay), hour, minute, 0);
//                	Log.i("___________1", mCalendar1.getTimeInMillis()+"");
//                	Log.i("_____2",String.format("%tF %<tT", mCalendar1.getTimeInMillis()));
					
					//��ʱ���ʽ��΢�룬��������ݿ���
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-d H:m:s");
					String start=Integer.parseInt(scheduleYear)+"-"+Integer.parseInt(tempMonth)+"-"+Integer.parseInt(tempDay)+" "+hour+":"+minute+":"+"0";
					long timeStart = 0;
					try {
						timeStart = sdf.parse(start).getTime();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i("_____2",String.format("%tF %<tT",timeStart ));
					//���ճ���Ϣ����
					String showDate = handleInfo(Integer.parseInt(scheduleYear), Integer.parseInt(tempMonth), Integer.parseInt(tempDay), hour, minute, week, remindID);
	                ScheduleVO schedulevo = new ScheduleVO();
	                schedulevo.setScheduleTypeID(mSelectedItem);
	                schedulevo.setRemindID(remindID);
	                schedulevo.setScheduleDate(showDate);
	                schedulevo.setTime(hour+"点"+minute+"分");
	                schedulevo.setScheduleContent(scheduleText.getText().toString());
	                schedulevo.setAlartime(timeStart);
					int scheduleID = dao.save(schedulevo);
					//��scheduleID���浽������(��Ϊ��CalendarActivity�е��gridView�е�һ��Item���ܻ��Ӧ�������ճ�(scheduleID))
					String [] scheduleIDs = new String[]{String.valueOf(scheduleID)};
					finish();
					//�����ճ̱������(�������ճ̱�����ڷ�װ��list��)
					setScheduleDateTag(remindID, scheduleYear, tempMonth, tempDay, scheduleID);
					Toast.makeText(ScheduleView.this, "保存成功", 0).show();
					setAlart(ScheduleView.this);
				}
			}
		});
		
	}

	/**
	 * �����ճ̱������
	 * @param remindID
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setScheduleDateTag(int remindID, String year, String month, String day,int scheduleID){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
		String d = year+"-"+month+"-"+day;
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(format.parse(d));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��װҪ��ǵ�����
		if(remindID >= 0 && remindID <= 3){
			//"����һ��","��10����","��30����","��һСʱ"��ֻ���ǵ�ǰ��һ�죩
			ScheduleDateTag dateTag = new ScheduleDateTag();
			dateTag.setYear(Integer.parseInt(year));
			dateTag.setMonth(Integer.parseInt(month));
			dateTag.setDay(Integer.parseInt(day));
			dateTag.setScheduleID(scheduleID);
			dateTagList.add(dateTag);
		}else if(remindID == 4){
			//ÿ���ظ�(�����õ��ճ̵Ŀ�ʼ��֮��ÿһ���Ҫ���)
			for(int i =0; i <= (2049-Integer.parseInt(year))*12*4*7; i++){
				if( i==0 ){
					cal.add(Calendar.DATE, 0);
				}else{
				    cal.add(Calendar.DATE, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 5){
			//ÿ���ظ�(�������ճ̵�����(���ڼ�)����������ÿ�ܵ���һ���Ҫ���)
			for(int i =0; i <= (2049-Integer.parseInt(year))*12*4; i++){
				if( i==0 ){
					cal.add(Calendar.WEEK_OF_MONTH, 0);
				}else{
				    cal.add(Calendar.WEEK_OF_MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 6){
			//ÿ���ظ�(�������ճ̵�����(���¼���)����������ÿ�µ���һ���Ҫ���)
			for(int i =0; i <= (2049-Integer.parseInt(year))*12; i++){
				if( i==0 ){
					cal.add(Calendar.MONTH, 0);
				}else{
				    cal.add(Calendar.MONTH, 1);
				}
				handleDate(cal,scheduleID);
			}
		}else if(remindID == 7){
			//ÿ���ظ�(�������ճ̵�����(��һ�꼸�¼���)����������ÿ�����һ���Ҫ���)
			for(int i =0; i <= 2049-Integer.parseInt(year); i++){
				if( i==0 ){
					cal.add(Calendar.YEAR, 0);
				}else{
				    cal.add(Calendar.YEAR, 1);
				}
				handleDate(cal,scheduleID);
			}
		}
		//��������ڴ������ݿ���
		dao.saveTagDate(dateTagList);
	}
	
	/**
	 * �ճ̱�����ڵĴ���
	 * @param cal
	 */
	public void handleDate(Calendar cal, int scheduleID){
		ScheduleDateTag dateTag = new ScheduleDateTag();
		dateTag.setYear(cal.get(Calendar.YEAR));
		dateTag.setMonth(cal.get(Calendar.MONTH)+1);
		dateTag.setDay(cal.get(Calendar.DATE));
		dateTag.setScheduleID(scheduleID);
		dateTagList.add(dateTag);
	}
	
	/**
	 * ͨ��ѡ�����Ѵ���������������ʾ���
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param week
	 * @param remindID
	 */
	public String handleInfo(int year, int month, int day, int hour, int minute, String week, int remindID){
		String remindType = remind[remindID];     //��������
		String show = "";
		if(0 <= remindID && remindID <= 4){
			//����һ��,��10����,��30����,��һСʱ
			show = year+"-"+month+"-"+day+"\t"+hour+":"+minute+"\t"+week+"\t\t"+remindType;
		}else if(remindID == 5){
			//ÿ��
			show = "每周"+week+"\t"+hour+":"+minute;
		}else if(remindID == 6){
			//ÿ��
			show = "每月"+day+"号"+"\t"+hour+":"+minute;
		}else if(remindID == 7){
			//ÿ��
			show = "每年"+month+"-"+day+"\t"+hour+":"+minute;
		}
		return show;
	}
	
	/**
	 * ���item֮����ʾ��������Ϣ
	 * 
	 * @return
	 */
	public String getScheduleDate() {
		Intent intent = getIntent();
		// intent.getp
		if(intent.getStringArrayListExtra("scheduleDate") != null){
			//��CalendarActivity�д�����ֵ��������������Ϣ��
			scheduleDate = intent.getStringArrayListExtra("scheduleDate");
		}else if(intent.getExtras().getInt("from")==1){
			scheduleVO=(ScheduleVO) intent.getExtras().getSerializable("scheduleVO");
		}
		int [] schType_remind = intent.getIntArrayExtra("schType_remind");  //��ScheduleTypeView�д�����ֵ(�����ճ����ͺ����Ѵ�����Ϣ)
		
		if(schType_remind != null){
			sch_typeID = schType_remind[0];
			remindID = schType_remind[1];
			scheduleType.setText(sch_type[sch_typeID]+"\t\t\t\t"+remind[remindID]);
		}
		// �õ������պ�����
		scheduleYear = scheduleDate.get(0);
		scheduleMonth = scheduleDate.get(1);
		tempMonth = scheduleMonth;
		if (Integer.parseInt(scheduleMonth) < 10) {
			scheduleMonth = "0" + scheduleMonth;
		}
		scheduleDay = scheduleDate.get(2);
		tempDay = scheduleDay;
		if (Integer.parseInt(scheduleDay) < 10) {
			scheduleDay = "0" + scheduleDay;
		}
		week = scheduleDate.get(3);
		String hour_c = String.valueOf(hour);
		String minute_c = String.valueOf(minute);
		if(hour < 10){
			hour_c = "0"+hour_c;
		}
		if(minute < 10){
			minute_c = "0"+minute_c;
		}
		// �õ���Ӧ����������
		String scheduleLunarDay = getLunarDay(Integer.parseInt(scheduleYear),
				Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
		String scheduleLunarMonth = lc.getLunarMonth(); // �õ��������·�
		StringBuffer scheduleDateStr = new StringBuffer();
		scheduleDateStr.append(scheduleYear).append("-").append(scheduleMonth)
				.append("-").append(scheduleDay).append(" ").append(hour_c).append(":").append(minute_c).append("\n").append(
						scheduleLunarMonth).append(scheduleLunarDay)
				.append(" ").append(week);
		// dateText.setText(scheduleDateStr);
		return scheduleDateStr.toString();
	}

	/**
	 * �������ڵ������շ�����������
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public String getLunarDay(int year, int month, int day) {
		String lunarDay = lc.getLunarDate(year, month, day, true);
		// {������ȡ��������Ӧ����������ʱ������������ڶ�Ӧ����������Ϊ"��һ"���ͱ����ó����·�(��:���£����¡�������)},�����ڴ˾�Ҫ�жϵõ������������Ƿ�Ϊ�·ݣ�������·ݾ�����Ϊ"��һ"
		if (lunarDay.substring(1, 2).equals("��")) {
			lunarDay = "��һ";
		}
		return lunarDay;
	}
	//�������ӣ�ֻ������һ������ʱ�䣬�������������Ҫ�����ж������ʱ��������������
	 public static void setAlart(Context context){
		 ScheduleDAO dao1=new ScheduleDAO(context);
		ArrayList<ScheduleVO> arrSch=dao1.getAllSchedule();
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		long time;
		String content=arrSch.get(0).getScheduleContent();
		time=arrSch.get(0).getAlartime();
		for (ScheduleVO vo : arrSch) {
			if(vo.getAlartime()>mCalendar.getTimeInMillis()){
				if(time<mCalendar.getTimeInMillis()){
					time=vo.getAlartime();
					content=vo.getScheduleContent();
				if(time>vo.getAlartime()){
					time=vo.getAlartime();
					content=vo.getScheduleContent();
				}
				}else{
					if(time>vo.getAlartime()){
						time=vo.getAlartime();
						content=vo.getScheduleContent();
					}
				}
			}
		}
		if(time>mCalendar.getTimeInMillis()){
		ObjectPool.mAlarmHelper.openAlarm(32,content,time);
		}
	}
}
