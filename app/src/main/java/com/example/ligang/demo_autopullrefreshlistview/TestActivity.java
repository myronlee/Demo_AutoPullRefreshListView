package com.example.ligang.demo_autopullrefreshlistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Scroller;


public class TestActivity extends Activity {

    private PullRefreshListView pullRefreshListView;
    private Scroller scroller;

    private int testStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("", this.toString()+" A onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        testStop = 1000;

//        pullRefreshListView = (PullRefreshListView) findViewById(R.id.listView);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"});
//        pullRefreshListView.setAdapter(adapter);

        /*scroller = new Scroller(this, new DecelerateInterpolator());
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scroller.startScroll(0, 0, 100, 100);
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            pullRefreshListView.imitatePull();
            startActivity(new Intent(this, TestOnStopActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    protected void onStart(){
        Log.e("", this.toString()+" A onStart");
        super.onStart();
    };

    protected void onRestart(){
        Log.e("", this.toString()+" A onRestart");
        super.onRestart();
    };

    protected void onResume(){
        Log.e("", this.toString()+" A onResume");
        super.onResume();
    };

    protected void onPause(){
        Log.e("", this.toString()+" A onPause");
        super.onPause();
    };

    @Override
    protected void onStop() {
        Log.e("", this.toString()+" A onStop");
        super.onStop();
    }

    protected void onDestroy(){
        Log.e("", this.toString()+" A onDestroy");
        super.onDestroy();
    };
}
