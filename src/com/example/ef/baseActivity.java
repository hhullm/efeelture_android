package com.example.ef;



import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class baseActivity extends Activity {       //继承activity，继承该类可以让activity在点击其他地方时候关闭软键盘


    public boolean isShouldHideKeyBoard(View v, MotionEvent event) {  
        // 如果获取焦点的View存在并且是RelativeLayout  
        if (v != null && (v instanceof RelativeLayout)) {  
            int[] leftTop = { 0, 0 };  
            // 获取输入框在屏幕中的位置  
            v.getLocationInWindow(leftTop);  
            int left = leftTop[0];  
            int top = leftTop[1];  
            int bottom = top + v.getHeight();  
            int right = left + v.getWidth();  
  
            // 判断点击区域是否在RelativeLayout内部,是则保留RelativeLayout的事件  
            if (event.getX() > left && event.getX() < right  
                    && event.getY() > top && event.getY() < bottom) {  
                return false;// 在RelativeLayout内部，不消耗该事件  
            } else {  
                return true;// 隐藏键盘  
            }  
        }  
        return false;// 如果获取焦点的View不是RelativeLayout，此处用就是原流程该怎么分发事件就怎么做  
    }  
  
    /** 
     * 事件分发，是否显示键盘 
     */  
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        // return super.dispatchTouchEvent(ev);  
        // 如果是点击按下动作  
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {  
            View v = getCurrentFocus();  
            if (isShouldHideKeyBoard(v, ev)) {  
                // 隐藏键盘  
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
                if (im != null) {  
                    im.hideSoftInputFromWindow(v.getWindowToken(), 0);  
                }  
            }  
            return super.dispatchTouchEvent(ev);  
        }  
        if (getWindow().superDispatchTouchEvent(ev)) {  
            return true;  
        }  
        return onTouchEvent(ev);  
  
    }  


}