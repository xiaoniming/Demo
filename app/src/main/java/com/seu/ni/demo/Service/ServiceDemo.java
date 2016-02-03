package com.seu.ni.demo.Service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.seu.ni.demo.R;

public class ServiceDemo extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "-----ServiceDemo";
    Button mStartIntentService;

    public <T> void L(T p) {
        Log.v(TAG, p + "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_demo_activity);
        L("Main Thread : " + Thread.currentThread().getName() + "    id : " + Thread.currentThread().getId());

        mStartIntentService = (Button) findViewById(R.id.bt_service_intent);
        mStartIntentService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_service_intent:
                Intent serviceIntent = new Intent(this, IntentServiceDemo.class);
                startService(serviceIntent);
                break;
        }

    }
}
