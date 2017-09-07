package com.example.ef;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.ef.borderText.BorderText;
import com.example.ef.dao.ScheduleDAO;
import com.example.ef.vo.ScheduleVO;

/**
 * ������ʾactivity
 * @author jack_peng
 *
 */
public class CalendarActivity extends Activity implements OnGestureListener,OnClickListener,OnLongClickListener {

	private ViewFlipper flipper = null;
	private GestureDetector gestureDetector = null;
	private CalendarView calV = null;
	private GridView gridView = null;
	private BorderText topText = null;
//	private TextView foot_tv = null;
	private Drawable draw = null;
	private static int jumpMonth = 0;      //ÿ�λ��������ӻ��ȥһ����,Ĭ��Ϊ0������ʾ��ǰ�£�
	private static int jumpYear = 0;       //������Խһ�꣬�����ӻ��߼�ȥһ��,Ĭ��Ϊ0(����ǰ��)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private ScheduleDAO dao = null;
	private ScheduleVO scheduleVO;
	private String[] scheduleIDs;
	private  ArrayList<String> scheduleDate;
	private Dialog builder;
	private ScheduleVO scheduleVO_del;

	public CalendarActivity() {

		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //��������
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	
    	dao = new ScheduleDAO(this);
	}

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rili);
		gestureDetector = new GestureDetector(this);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.removeAllViews();
        calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        
        addGridView();
        gridView.setAdapter(calV);
        //flipper.addView(gridView);
        flipper.addView(gridView,0);
        
		topText = (BorderText) findViewById(R.id.toptext);
		addTextToTopTextView(topText);
//		foot_tv =(TextView) findViewById(R.id.foot_tv);
	}
	
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0;         //ÿ�����gridview��viewflipper��ʱ���ı��
		if (e1.getX() - e2.getX() > 50) {
            //���󻬶�
			addGridView();   //���һ��gridView
			jumpMonth++;     //��һ����
			
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        //flipper.addView(gridView);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView, gvFlag);
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
			this.flipper.showNext();
			flipper.removeViewAt(0);
			return true;
		} else if (e1.getX() - e2.getX() < -50) {
            //���һ���
			addGridView();   //���һ��gridView
			jumpMonth--;     //��һ����
			
			calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        gvFlag++;
	        addTextToTopTextView(topText);
	        //flipper.addView(gridView);
	        flipper.addView(gridView,gvFlag);
	        
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
			this.flipper.showPrevious();
			flipper.removeViewAt(0);
			return true;
		}
		return false;
	}
	
	/**
	 * �����˵�
	 */
	
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, menu.FIRST, menu.FIRST, "今天");
		menu.add(0, menu.FIRST+1, menu.FIRST+1, "跳转");
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * ѡ��˵�
	 */
	
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
        case Menu.FIRST:
        	//��ת������
        	int xMonth = jumpMonth;
        	int xYear = jumpYear;
        	int gvFlag =0;
        	jumpMonth = 0;
        	jumpYear = 0;
        	addGridView();   //���һ��gridView
        	year_c = Integer.parseInt(currentDate.split("-")[0]);
        	month_c = Integer.parseInt(currentDate.split("-")[1]);
        	day_c = Integer.parseInt(currentDate.split("-")[2]);
        	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	        flipper.addView(gridView,gvFlag);
	        if(xMonth == 0 && xYear == 0){
	        	//nothing to do
	        }else if((xYear == 0 && xMonth >0) || xYear >0){
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
				this.flipper.showNext();
	        }else{
	        	this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
				this.flipper.showPrevious();
	        }
			flipper.removeViewAt(0);
        	break;
        case Menu.FIRST+1:
        	
        	new DatePickerDialog(this, new OnDateSetListener() {
				
				
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					//1901-1-1 ----> 2049-12-31
					if(year < 1901 || year > 2049){
						//���ڲ�ѯ��Χ��
						new AlertDialog.Builder(CalendarActivity.this).setTitle("错误日期").setMessage("跳转日期范围(1901/1/1-2049/12/31)").setPositiveButton("确认", null).show();
					}else{
						int gvFlag = 0;
						addGridView();   //���һ��gridView
			        	calV = new CalendarView(CalendarActivity.this, CalendarActivity.this.getResources(),year,monthOfYear+1,dayOfMonth);
				        gridView.setAdapter(calV);
				        addTextToTopTextView(topText);
				        gvFlag++;
				        flipper.addView(gridView,gvFlag);
				        if(year == year_c && monthOfYear+1 == month_c){
				        	//nothing to do
				        }
				        if((year == year_c && monthOfYear+1 > month_c) || year > year_c ){
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_left_out));
				        	CalendarActivity.this.flipper.showNext();
				        }else{
				        	CalendarActivity.this.flipper.setInAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_in));
				        	CalendarActivity.this.flipper.setOutAnimation(AnimationUtils.loadAnimation(CalendarActivity.this,R.anim.push_right_out));
				        	CalendarActivity.this.flipper.showPrevious();
				        }
				        flipper.removeViewAt(0);
				        //��ת֮����ת֮�����������Ϊ��������
				        year_c = year;
						month_c = monthOfYear+1;
						day_c = dayOfMonth;
						jumpMonth = 0;
						jumpYear = 0;
					}
				}
			},year_c, month_c-1, day_c).show();
        	break;
        }
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	public boolean onTouchEvent(MotionEvent event) {

		return this.gestureDetector.onTouchEvent(event);
	}

	
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//���ͷ������� �����µ���Ϣ
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		draw = getResources().getDrawable(R.drawable.top_day);
		view.setBackgroundDrawable(draw);
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		if (!calV.getLeapMonth().equals("") && calV.getLeapMonth() != null) {
			textDate.append("闰").append(calV.getLeapMonth()).append("月")
					.append("\t");
		}
		textDate.append(calV.getAnimalsYear()).append("年").append("(").append(
				calV.getCyclical()).append("年)");
		view.setText(textDate);
		view.setTextColor(Color.BLACK);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	//���gridview
	private void addGridView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		//ȡ����Ļ�Ŀ�Ⱥ͸߶�
		WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth(); 
        int Height = display.getHeight();
        
		gridView = new GridView(this);
		gridView.setNumColumns(7);
		gridView.setColumnWidth(46);
	//	gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		if(Width == 480 && Height == 800){
			gridView.setColumnWidth(69);
		}
		gridView.setGravity(Gravity.CENTER_VERTICAL);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // ȥ��gridView�߿�
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
        gridView.setBackgroundResource(R.drawable.gridview_bk);
		gridView.setOnTouchListener(new OnTouchListener() {
            //��gridview�еĴ����¼��ش���gestureDetector
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return CalendarActivity.this.gestureDetector
						.onTouchEvent(event);
			}
		});

		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView�е�ÿһ��item�ĵ���¼�
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				  //����κ�һ��item���õ����item������(�ų�����������յ�����(�������Ӧ))
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position  && position <= endPosition){
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //��һ�������
					  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //��һ�������
	                  String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = calV.getShowMonth();
	                  String week = "";
	                  
	                  //ͨ�����ڲ�ѯ��һ���Ƿ񱻱�ǣ����������ճ̾Ͳ�ѯ������������ճ���Ϣ
	                  scheduleIDs = dao.getScheduleByTagDate(Integer.parseInt(scheduleYear), Integer.parseInt(scheduleMonth), Integer.parseInt(scheduleDay));
	                  //�õ���һ�������ڼ�
	                  switch(position%7){
	                  case 0:
	                	  week = "周日";
	                	  break;
	                  case 1:
	                	  week = "周一";
	                	  break;
	                  case 2:
	                	  week = "周二";
	                	  break;
	                  case 3:
	                	  week = "周三";
	                	  break;
	                  case 4:
	                	  week = "周四";
	                	  break;
	                  case 5:
	                	  week = "周五";
	                	  break;
	                  case 6:
	                	  week = "周六";
	                	  break;
	                  }
					 
	                  scheduleDate = new ArrayList<String>();
	                  scheduleDate.add(scheduleYear);
	                  scheduleDate.add(scheduleMonth);
	                  scheduleDate.add(scheduleDay);
	                  scheduleDate.add(week);
	                  if(scheduleIDs != null && scheduleIDs.length > 0){
	                	  LayoutInflater inflater=getLayoutInflater();
		              		View linearlayout= inflater.inflate(R.layout.mydialog, null);
		              		ImageButton img_creat=(ImageButton)linearlayout.findViewById(R.id.img_creat);
		              		ImageButton img_del=(ImageButton) linearlayout.findViewById(R.id.img_close);
		              		TextView day_tv=(TextView) linearlayout.findViewById(R.id.day_tv);
		              		day_tv.setText(scheduleDay+"日");
		              		TableLayout dialog_tab=(TableLayout) linearlayout.findViewById(R.id.dialog_tab);
		              		img_creat.setOnClickListener(new OnClickListener() {
								
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(builder!=null&&builder.isShowing()){
										builder.dismiss();
										Intent intent = new Intent();
						                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
						                  intent.setClass(CalendarActivity.this, ScheduleView.class);
						                  startActivity(intent);
									}
								}
							});img_del.setOnClickListener(new OnClickListener() {
								
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if(builder!=null&&builder.isShowing()){
										builder.dismiss();
									}
								}
							});
							ScheduleDAO dao=new ScheduleDAO(CalendarActivity.this);
	                	  for(int i=0;i<scheduleIDs.length;i++){
	                	  scheduleVO=dao.getScheduleByID(CalendarActivity.this,Integer.parseInt(scheduleIDs[i]));
	                	  String info="";
	                	  info=scheduleVO.getTime()+"   "+scheduleVO.getScheduleContent();
	                	  TableRow localTableRow = new TableRow(CalendarActivity.this);
                		  TextView tv=new TextView(CalendarActivity.this);
                		  tv.setPadding(10, 5, 5, 5);
                		  tv.setTextSize(18.0F);
                		  tv.setTextColor(Color.BLACK);
                		  tv.setSingleLine(true);
                		  tv.setText(info);
                		  localTableRow.addView(tv);
                		  localTableRow.setTag(scheduleVO);
                		  localTableRow.setOnLongClickListener(CalendarActivity.this);
                		  localTableRow.setOnClickListener(CalendarActivity.this);
                		  dialog_tab.addView(localTableRow);
                		  
	                	  }
	                	   //�Զ���dialog���õ��ײ������ÿ��
	                	  builder =	new Dialog(CalendarActivity.this,R.style.FullScreenDialog);
	                	  builder.setContentView(linearlayout);
	                	  WindowManager windowManager = getWindowManager();
	                	  Display display = windowManager.getDefaultDisplay();
	                	  WindowManager.LayoutParams lp = builder.getWindow().getAttributes();
	                	  lp.width = (int)(display.getWidth()); //���ÿ��
	                	  lp.y=display.getHeight()-10;
	                	  builder.getWindow().setAttributes(lp); 
	                	  builder.setCanceledOnTouchOutside(true);
	                	  builder.show();
	                  }else{
	                  //ֱ����ת����Ҫ����ճ̵Ľ���
		                  //scheduleDate.add(scheduleLunarDay);
		                  Intent intent = new Intent();
		                  intent.putStringArrayListExtra("scheduleDate", scheduleDate);
		                  intent.setClass(CalendarActivity.this,ScheduleView.class);
		                  startActivity(intent);
	                  }
				  }
			}
		});
		gridView.setLayoutParams(params);
	}
	@Override
	protected void onRestart() {
		int xMonth = jumpMonth;
    	int xYear = jumpYear;
    	int gvFlag =0;
    	jumpMonth = 0;
    	jumpYear = 0;
    	addGridView();   //���һ��gridView
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
    	calV = new CalendarView(this, getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        gridView.setAdapter(calV);
        addTextToTopTextView(topText);
        gvFlag++;
        flipper.addView(gridView,gvFlag);
		flipper.removeViewAt(0);
		super.onRestart();
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(builder!=null&&builder.isShowing()){
			builder.dismiss();
		}
		ScheduleVO scheduleVO=  (ScheduleVO) v.getTag();
		Intent intent = new Intent();
		if(scheduleDate!=null){
			intent.putStringArrayListExtra("scheduleDate", scheduleDate);
		}
		intent.setClass(CalendarActivity.this,ScheduleInfoView.class);
        intent.putExtra("scheduleVO", scheduleVO);
			  startActivity(intent);
	}


	public boolean onLongClick(View v) {
		scheduleVO_del=  (ScheduleVO) v.getTag();
		Dialog alertDialog = new AlertDialog.Builder(CalendarActivity.this). 
        setMessage("ɾ���ճ���Ϣ��"). 
        setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
             
            public void onClick(DialogInterface dialog, int which) { 
            	dao.delete(scheduleVO_del.getScheduleID());
            	ScheduleView.setAlart(CalendarActivity.this);
            	if(builder!=null&&builder.isShowing()){
            		builder.dismiss();
            	}
            }
 
        }). 
        setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { 
             
            public void onClick(DialogInterface dialog, int which) { 
           	 
            } 
        }). 
        create(); 
		alertDialog.show();

		return false;
	}
}