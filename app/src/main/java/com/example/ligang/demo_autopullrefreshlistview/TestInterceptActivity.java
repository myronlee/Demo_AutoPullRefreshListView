package com.example.ligang.demo_autopullrefreshlistview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TestInterceptActivity extends Activity {
    private final static String TAG = TestInterceptActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewGroup viewGroup = new MyViewGroup(this);
        MyView view = new MyView(this);
        view.setText("hello");
        viewGroup.addView(view);
        setContentView(viewGroup);
    }

    class MyViewGroup extends LinearLayout {

        public MyViewGroup(Context context) {
            super(context);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.e(TAG, "ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.e(TAG, "ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e(TAG, "ACTION_UP");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.e(TAG, "ACTION_CANCEL");
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return super.onTouchEvent(event);
        }
    }

    class MyView extends TextView {

        public MyView(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }
    }
}
