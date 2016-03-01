package com.seu.ni.demo.UI.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.seu.ni.demo.R;


public class MainFragmentActivity extends Activity {
    FragmentChildA mFragById;
    FragmentChildA mFramgA;
    FragmentChildB mFramgB;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mFragmentManager = getFragmentManager();

        // you can new a Fragment instance or get one by FragmentManager.findFragmentById
        mFramgA = new FragmentChildA();
        mFramgB = new FragmentChildB();
        mFragById = (FragmentChildA)mFragmentManager.findFragmentById(R.id.id_existing_fragment);

        // begin transaction
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.id_fragment_content, mFramgA);
        transaction.commit();


        Button bt = (Button) findViewById(R.id.bt_fragment_change);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.id_fragment_content, mFramgB);
                transaction.commit();
            }
        });


    }
}
