package com.seu.ni.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L("onCreate");


    }

    @Override
    protected void onStart() {
        super.onStart();
        L("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        L("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        L("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        L("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        L("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L("onDestroy");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        L("onWindowFocusChanged");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public static  void L(String s){
        Log.v("MainActivity-----> ",s);
    }

}
