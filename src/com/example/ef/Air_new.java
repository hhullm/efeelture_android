package com.example.ef;

import java.util.ArrayList;
import java.util.List;

import com.example.ef.PickerView;
import com.example.ef.MainActivity;
import com.example.ef.R;
import com.example.ef.PickerView.onSelectListener;

import android.R.bool;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Air_new extends Activity {
	PickerView minute_pv;
	private boolean isVisible = true;
	private LinearLayout layout_1;
	private ImageView zilengonoff,zhireonoff,choushionoff,shoudongfxonoff,zidongfxonoff,shoudngfsonoff,
	zidongfsonoff,windup,winddown,windleft,windright,windlevel,back;
	private TextView zhileng_re;
	boolean isChanged = false;
	boolean iswind = false;
	boolean iswind2 = false;
	boolean iswind3 = false;
	int numb = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_new);
		zilengonoff = (ImageView)findViewById(R.id.airnew_imageView_zloff1);
		zhireonoff = (ImageView)findViewById(R.id.airnew_imageView_zroff1);
		choushionoff = (ImageView)findViewById(R.id.airnew_imageView_csoff2);
		shoudongfxonoff = (ImageView)findViewById(R.id.wind_imageView_off1);
		zidongfxonoff = (ImageView)findViewById(R.id.wind_imageView_off2);
		shoudngfsonoff = (ImageView)findViewById(R.id.wind_imageView_off3);
		zidongfsonoff = (ImageView)findViewById(R.id.wind_imageView_off4);
		windup = (ImageView)findViewById(R.id.wind_imageView_1);
		winddown = (ImageView)findViewById(R.id.wind_imageView_2);
		windleft = (ImageView)findViewById(R.id.wind_imageView_3);
		windright = (ImageView)findViewById(R.id.wind_imageView_4);
		windlevel = (ImageView)findViewById(R.id.wind_imageView_level);
		zhileng_re = (TextView)findViewById(R.id.airnew_textView_xuanxiang);
		minute_pv = (PickerView) findViewById(R.id.minute_pv);
		back = (ImageView)findViewById(R.id.airnew_imageView_bg);
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Air_new.this.finish();
			}
		});
		
		
		zilengonoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				zhireonoff.setImageResource(R.drawable.airoff);
			if(v == zilengonoff)
            {
                if(isChanged){
                	zilengonoff.setImageDrawable(getResources().getDrawable(R.drawable.airoff));
                }else
                {
                	zilengonoff.setImageDrawable(getResources().getDrawable(R.drawable.airon));
                	zhileng_re.setText("制冷");
                }
                isChanged = !isChanged;
                 
            }
			}
		});
		
		
		
		
		zhireonoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				zilengonoff.setImageResource(R.drawable.airoff);
			if(v == zhireonoff)
            {
                if(!isChanged){
                	zhireonoff.setImageDrawable(getResources().getDrawable(R.drawable.airoff));
                }else
                {
                	zhireonoff.setImageDrawable(getResources().getDrawable(R.drawable.airon));
                	zhileng_re.setText("制热");
                }
                isChanged = !isChanged;
                 
            }
			}
		});
		
		
		
		
		shoudongfxonoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				zidongfxonoff.setImageResource(R.drawable.airoff);
			if(v == shoudongfxonoff)
            {
                if(iswind){
                	shoudongfxonoff.setImageDrawable(getResources().getDrawable(R.drawable.airoff));
                }else
                {
                	shoudongfxonoff.setImageDrawable(getResources().getDrawable(R.drawable.airon));
                	
                }
                iswind = !iswind;
                 
            }
			}
		});
		
		
		
		
		zidongfxonoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				windup.setImageResource(R.drawable.windup1);
				winddown.setImageResource(R.drawable.winddown1);
				windleft.setImageResource(R.drawable.windleft1);
				windright.setImageResource(R.drawable.windright1);
				shoudongfxonoff.setImageResource(R.drawable.airoff);
			if(v == zidongfxonoff)
            {
                if(!iswind){
                	zidongfxonoff.setImageDrawable(getResources().getDrawable(R.drawable.airoff));
                }else
                {
                	zidongfxonoff.setImageDrawable(getResources().getDrawable(R.drawable.airon));
                	
                }
                iswind = !iswind;
                 
            }
			}
		});
		
		
		
		
		shoudngfsonoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				zidongfsonoff.setImageResource(R.drawable.airoff);
			if(v == shoudngfsonoff)
            {
                if(iswind2){
                	shoudngfsonoff.setImageDrawable(getResources().getDrawable(R.drawable.airoff));
                }else
                {
                	shoudngfsonoff.setImageDrawable(getResources().getDrawable(R.drawable.airon));
                	
                }
                iswind2 = !iswind2;
                 
            }
			}
		});
		
		
		
		
		zidongfsonoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				shoudngfsonoff.setImageResource(R.drawable.airoff);
			if(v == zidongfsonoff)
            {
                if(!iswind2){
                	zidongfsonoff.setImageDrawable(getResources().getDrawable(R.drawable.airoff));
                }else
                {
                	zidongfsonoff.setImageDrawable(getResources().getDrawable(R.drawable.airon));
                	
                }
                iswind2 = !iswind2;
                 
            }
			}
		});
		
		
		
		windup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				windup.setImageResource(R.drawable.windup);
				winddown.setImageResource(R.drawable.winddown1);
				windleft.setImageResource(R.drawable.windleft1);
				windright.setImageResource(R.drawable.windright1);
				shoudongfxonoff.setImageResource(R.drawable.airon);
				zidongfxonoff.setImageResource(R.drawable.airoff);
			
			}
		});
		winddown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				windup.setImageResource(R.drawable.windup1);
				winddown.setImageResource(R.drawable.winddown);
				windleft.setImageResource(R.drawable.windleft1);
				windright.setImageResource(R.drawable.windright1);
				shoudongfxonoff.setImageResource(R.drawable.airon);
				zidongfxonoff.setImageResource(R.drawable.airoff);
			
			}
		});
		windleft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				windup.setImageResource(R.drawable.windup1);
				winddown.setImageResource(R.drawable.winddown1);
				windleft.setImageResource(R.drawable.windleft);
				windright.setImageResource(R.drawable.windright1);
				shoudongfxonoff.setImageResource(R.drawable.airon);
				zidongfxonoff.setImageResource(R.drawable.airoff);
			
			}
		});
		windright.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				windup.setImageResource(R.drawable.windup1);
				winddown.setImageResource(R.drawable.winddown1);
				windleft.setImageResource(R.drawable.windleft1);
				windright.setImageResource(R.drawable.windright);
				shoudongfxonoff.setImageResource(R.drawable.airon);
			    zidongfxonoff.setImageResource(R.drawable.airoff);
			}
		});
		
		
		windlevel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				zidongfsonoff.setImageResource(R.drawable.airoff);
				shoudngfsonoff.setImageResource(R.drawable.airon);
				
				switch (numb) {
				case 0:
					windlevel.setImageResource(R.drawable.windlevel1);
					numb++;
					break;
				case 1:
					windlevel.setImageResource(R.drawable.windlevel2);
					numb++;
					break;
				case 2:
					windlevel.setImageResource(R.drawable.windlevel3);
					numb = 0;
					break;
				
				}
			}
		});
		
		
		
		choushionoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				
			if(v == choushionoff)
            {
                
				if(iswind3){
                	choushionoff.setImageDrawable(getResources().getDrawable(R.drawable.airoff));
                }else
                {
                	choushionoff.setImageDrawable(getResources().getDrawable(R.drawable.airon));
                	
                }
                iswind3 = !iswind3;
                 
            }
			}
		});
		
		
		
		List<String> data = new ArrayList<String>();
		 layout_1 = (LinearLayout) findViewById(R.id.linearLayout_id);
         layout_1.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域

         //点击触发的图标
         ImageView more = (ImageView) findViewById(R.id.airnew_imageView_xiala);
         more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    layout_1.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    layout_1.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
            }
        });

		for (int i = 16; i < 31; i++)
		{
			data.add("" + i);//添加da
		}
		minute_pv.setData(data);
		minute_pv.setOnSelectListener(new onSelectListener()
		{

			@Override
			public void onSelect(String text)
			{
				Toast.makeText(Air_new.this, "选择了 " + text + " ℃",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
