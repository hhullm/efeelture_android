package com.example.ef;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ef.calendar.LunarCalendar;
import com.example.ef.calendar.SpecialCalendar;
import com.example.ef.dao.ScheduleDAO;
import com.example.ef.vo.ScheduleDateTag;

/**
 * ����gridview�е�ÿһ��item��ʾ��textview
 * @author jack_peng
 *
 */
public class CalendarView extends BaseAdapter {

	private ScheduleDAO dao = null;
	private boolean isLeapyear = false;  //�Ƿ�Ϊ����
	private int daysOfMonth = 0;      //ĳ�µ�����
	private int dayOfWeek = 0;        //����ĳһ�������ڼ�
	private int lastDaysOfMonth = 0;  //��һ���µ�������
	private Context context;
	private String[] dayNumber = new String[49];  //һ��gridview�е����ڴ����������
	private static String week[] = {"周日","周一","周二","周三","周四","周五","周六"};
	private SpecialCalendar sc = null;
	private LunarCalendar lc = null; 
	private Resources res = null;
	private Drawable drawable = null;
	
	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
	private int currentFlag = -1;     //���ڱ�ǵ���
	private int[] schDateTagFlag = null;  //�洢�������е��ճ�����
	
	private String showYear = "";   //������ͷ����ʾ�����
	private String showMonth = "";  //������ͷ����ʾ���·�
	private String animalsYear = ""; 
	private String leapMonth = "";   //����һ����
	private String cyclical = "";   //��ɵ�֧
	//ϵͳ��ǰʱ��
	private String sysDate = "";  
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";
	
	//�ճ�ʱ��(��Ҫ��ǵ��ճ�����)
	private String sch_year = "";
	private String sch_month = "";
	private String sch_day = "";
	
	public CalendarView(){
		Date date = new Date();
		sysDate = sdf.format(date);  //��������
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
		
	}
	
	public CalendarView(Context context,Resources rs,int jumpMonth,int jumpYear,int year_c,int month_c,int day_c){
		this();
		this.context= context;
		sc = new SpecialCalendar();
		lc = new LunarCalendar();
		this.res = rs;
		
		int stepYear = year_c+jumpYear;
		int stepMonth = month_c+jumpMonth ;
		if(stepMonth > 0){
			//����һ���»���
			if(stepMonth%12 == 0){
				stepYear = year_c + stepMonth/12 -1;
				stepMonth = 12;
			}else{
				stepYear = year_c + stepMonth/12;
				stepMonth = stepMonth%12;
			}
		}else{
			//����һ���»���
			stepYear = year_c - 1 + stepMonth/12;
			stepMonth = stepMonth%12 + 12;
			if(stepMonth%12 == 0){
				
			}
		}
	
		currentYear = String.valueOf(stepYear);;  //�õ���ǰ�����
		currentMonth = String.valueOf(stepMonth);  //�õ����� ��jumpMonthΪ�����Ĵ�����ÿ����һ�ξ�����һ�»��һ�£�
		currentDay = String.valueOf(day_c);  //�õ���ǰ����������
		
		getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
		
	}
	
	public CalendarView(Context context,Resources rs,int year, int month, int day){
		this();
		this.context= context;
		sc = new SpecialCalendar();
		lc = new LunarCalendar();
		this.res = rs;
		currentYear = String.valueOf(year); //�õ���ת�������
		currentMonth = String.valueOf(month);  //�õ���ת�����·�
		currentDay = String.valueOf(day);  //�õ���ת������
		
		getCalendar(Integer.parseInt(currentYear),Integer.parseInt(currentMonth));
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
	}

	
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.calendar, null);
		 }
		TextView textView = (TextView) convertView.findViewById(R.id.tvtext);
		String d = dayNumber[position].split("\\.")[0];
		String dv = dayNumber[position].split("\\.")[1];
		//Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica.ttf");
		//textView.setTypeface(typeface);
		SpannableString sp = new SpannableString(d+"\n"+dv);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f) , 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		if(dv != null || dv != ""){
            sp.setSpan(new RelativeSizeSpan(0.75f), d.length()+1, dayNumber[position].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		//sp.setSpan(new ForegroundColorSpan(Color.MAGENTA), 14, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
		textView.setText(sp);
		textView.setTextColor(Color.GRAY);
		if(position<7){
			//������
			textView.setTextColor(Color.BLACK);
			drawable = res.getDrawable(R.drawable.week_top);
			textView.setBackgroundDrawable(drawable);
		}
		
		if (position < daysOfMonth + dayOfWeek+7 && position >= dayOfWeek+7) {
			// ��ǰ����Ϣ��ʾ
			textView.setTextColor(Color.BLACK);// �����������
			drawable = res.getDrawable(R.drawable.item);
			//textView.setBackgroundDrawable(drawable);
			//textView.setBackgroundColor(Color.WHITE);

		}
		if(schDateTagFlag != null && schDateTagFlag.length >0){
			for(int i = 0; i < schDateTagFlag.length; i++){
				if(schDateTagFlag[i] == position){
					//�����ճ̱�Ǳ���
					textView.setBackgroundResource(R.drawable.mark);
				}
			}
		}
		if(currentFlag == position){ 
			//���õ���ı���
			drawable = res.getDrawable(R.drawable.current_day_bgc);
			textView.setBackgroundDrawable(drawable);
			textView.setTextColor(Color.WHITE);
		}
		return convertView;
	}
	
	//�õ�ĳ���ĳ�µ����������µĵ�һ�������ڼ�
	public void getCalendar(int year, int month){
		isLeapyear = sc.isLeapYear(year);              //�Ƿ�Ϊ����
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month);  //ĳ�µ�������
		dayOfWeek = sc.getWeekdayOfMonth(year, month);      //ĳ�µ�һ��Ϊ���ڼ�
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month-1);  //��һ���µ�������
		Log.d("DAY", isLeapyear+" ======  "+daysOfMonth+"  ============  "+dayOfWeek+"  =========   "+lastDaysOfMonth);
		getweek(year,month);
	}
	
	//��һ�����е�ÿһ���ֵ���������dayNuber��
	private void getweek(int year, int month) {
		int j = 1;
		int flag = 0;
		String lunarDay = "";
		
		//�õ���ǰ�µ������ճ�����(��Щ������Ҫ���)
		dao = new ScheduleDAO(context);
		ArrayList<ScheduleDateTag> dateTagList = dao.getTagDate(year,month);
		if(dateTagList != null && dateTagList.size() > 0){
			schDateTagFlag = new int[dateTagList.size()];
		}
		
		for (int i = 0; i < dayNumber.length; i++) {
			// ��һ
			if(i<7){
				dayNumber[i]=week[i]+"."+" ";
			}
			else if(i < dayOfWeek+7){  //ǰһ����
				int temp = lastDaysOfMonth - dayOfWeek+1-7;
				lunarDay = lc.getLunarDate(year, month-1, temp+i,false);
				dayNumber[i] = (temp + i)+"."+lunarDay;
			}else if(i < daysOfMonth + dayOfWeek+7){   //����
				String day = String.valueOf(i-dayOfWeek+1-7);   //�õ�������
				lunarDay = lc.getLunarDate(year, month, i-dayOfWeek+1-7,false);
				dayNumber[i] = i-dayOfWeek+1-7+"."+lunarDay;
				//���ڵ�ǰ�²�ȥ��ǵ�ǰ����
				if(sys_year.equals(String.valueOf(year)) && sys_month.equals(String.valueOf(month)) && sys_day.equals(day)){
					//�ʼǵ�ǰ����
					currentFlag = i;
				}
				
				//����ճ�����
				if(dateTagList != null && dateTagList.size() > 0){
					for(int m = 0; m < dateTagList.size(); m++){
						ScheduleDateTag dateTag = dateTagList.get(m);
						int matchYear = dateTag.getYear();
						int matchMonth = dateTag.getMonth();
						int matchDay = dateTag.getDay();
						if(matchYear == year && matchMonth == month && matchDay == Integer.parseInt(day)){
							schDateTagFlag[flag] = i;
							flag++;
						}
					}
				}
				
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
				setAnimalsYear(lc.animalsYear(year));
				setLeapMonth(lc.leapMonth == 0?"":String.valueOf(lc.leapMonth));
				setCyclical(lc.cyclical(year));
			}else{   //��һ����
				lunarDay = lc.getLunarDate(year, month+1, j,false);
				dayNumber[i] = j+"."+lunarDay;
				j++;
			}
		}
        
        String abc = "";
        for(int i = 0; i < dayNumber.length; i++){
        	 abc = abc+dayNumber[i]+":";
        }
        Log.d("DAYNUMBER",abc);


	}
	
	
	public void matchScheduleDate(int year, int month, int day){
		
	}
	
	/**
	 * ���ÿһ��itemʱ����item�е�����
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position){
		return dayNumber[position];
	}
	
	/**
	 * �ڵ��gridViewʱ���õ�������е�һ���λ��
	 * @return
	 */
	public int getStartPositon(){
		return dayOfWeek+7;
	}
	
	/**
	 * �ڵ��gridViewʱ���õ�����������һ���λ��
	 * @return
	 */
	public int getEndPosition(){
		return  (dayOfWeek+daysOfMonth+7)-1;
	}
	
	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}
	
	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}
	
	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}
	
	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}
}
