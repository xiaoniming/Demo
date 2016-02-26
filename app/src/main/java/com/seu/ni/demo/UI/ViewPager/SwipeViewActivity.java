package com.seu.ni.demo.UI.ViewPager;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.seu.ni.demo.R;
import com.seu.ni.demo.UI.Fragment.FragmentChildA;
import com.seu.ni.demo.UI.Fragment.FragmentChildB;

public class SwipeViewActivity extends Activity {
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view);

        mViewPager = (ViewPager) findViewById(R.id.id_view_pager);
        mViewPager.setAdapter(new DemoPagerAdapter(getFragmentManager()));
    }
}


