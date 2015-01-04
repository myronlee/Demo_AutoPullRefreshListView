package com.example.ligang.demo_autopullrefreshlistview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class TestOnStopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("", this.toString()+" B onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_on_stop);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_on_stop, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    protected void onStart(){
        Log.e("", this.toString()+" B onStart");
        super.onStart();
    };

    protected void onRestart(){
        Log.e("", this.toString()+" B onRestart");
        super.onRestart();
    };

    protected void onResume(){
        Log.e("", this.toString()+" B onResume ");
        super.onResume();
    };

    protected void onPause(){
        Log.e("", this.toString()+" B onPause");
        super.onPause();
    };

    @Override
    protected void onStop() {
        Log.e("", this.toString()+" B onStop");
        super.onStop();
    }

    protected void onDestroy(){
        Log.e("", this.toString()+" B onDestroy ");
        super.onDestroy();
    };

//    @Override
//    public void onBackPressed() {
////        Log.e("", " B onDestroy");
//        startActivity(new Intent(this, TestActivity.class));
////        super.onBackPressed();
//    }
}
