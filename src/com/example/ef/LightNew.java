package com.example.ef;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LightNew extends Activity {
	private TextView xuanxiang;
	private ImageView shoudong;
	private ImageView zidong,back;
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private TextView text4;
	private TextView text5;
	private TextView kt;
	private TextView wsh;
	private TextView chufang;
	private TextView yyuan;
	private TextView xiuxi;
	private TextView paidui;
	private TextView gongzuo;
	private TextView mingxiang;
	private TextView zdsd;
	boolean isChanged = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_light_new);
		xuanxiang = (TextView)findViewById(R.id.lg_textView_xuanxiang);
		shoudong = (ImageView)findViewById(R.id.lgn_imageView_off1);
		zidong = (ImageView)findViewById(R.id.lgn_imageView_off2);
		text1 = (TextView)findViewById(R.id.lgn_textView1_1);
		text2 = (TextView)findViewById(R.id.lgn_textView1_2);
		text3 = (TextView)findViewById(R.id.lgn_textView1_3);
		text4 = (TextView)findViewById(R.id.lgn_textView1_4);
		text5 = (TextView)findViewById(R.id.lgn_textView1_5);
		kt = (TextView)findViewById(R.id.lgn_textView1_kt);
		wsh = (TextView)findViewById(R.id.lgn_textView1_ws);
		chufang = (TextView)findViewById(R.id.lgn_textView1_cf);
		yyuan = (TextView)findViewById(R.id.lgn_textView1_yy);
		xiuxi = (TextView)findViewById(R.id.lgn_textView1_xx);
		paidui = (TextView)findViewById(R.id.lgn_textView1_pd);
		gongzuo = (TextView)findViewById(R.id.lgn_textView1_gz);
		mingxiang = (TextView)findViewById(R.id.lgn_textView1_mx);
		zdsd = (TextView)findViewById(R.id.lg_textView3_zidong);
		back = (ImageView)findViewById(R.id.lgnew_imageView_bg);
				
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LightNew.this.finish();
			}
		});
		
		text1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("档位1");
			zdsd.setText("手动");
			
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		         
		
		text2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("档位2");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		text3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("档位3");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		text4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("档位4");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});        
		
		text5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("档位5");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});     
		
		kt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("客厅");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		wsh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("卧室");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		chufang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("厨房");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		yyuan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("影院");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		xiuxi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("休息");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		paidui.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("派对");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		gongzuo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("工作");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		mingxiang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("冥想");
			zdsd.setText("手动");
			zidong.setImageResource(R.drawable.lightoff);
			shoudong.setImageResource(R.drawable.lighton);
			}
		});
		
		shoudong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("手动");
			zdsd.setText("自动");
			zidong.setImageResource(R.drawable.lightoff);
			if(v == shoudong)
            {
                if(isChanged){
                	shoudong.setImageDrawable(getResources().getDrawable(R.drawable.lighton));
                }else
                {
                	shoudong.setImageDrawable(getResources().getDrawable(R.drawable.lightoff));
                }
                isChanged = !isChanged;
                 
            }
			
			}
		});
		
		zidong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			xuanxiang.setText("自动");
			zdsd.setText("手动");
			shoudong.setImageResource(R.drawable.lightoff);
			if(v == zidong)
            {
                if(isChanged){
                	zidong.setImageDrawable(getResources().getDrawable(R.drawable.lightoff));
                }else
                {
                	zidong.setImageDrawable(getResources().getDrawable(R.drawable.lighton));
                }
                isChanged = !isChanged;
                 
            }
			}
		});
		         
		         
	}
}
