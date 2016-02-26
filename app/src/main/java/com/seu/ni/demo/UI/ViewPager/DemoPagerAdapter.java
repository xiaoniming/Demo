package com.seu.ni.demo.UI.ViewPager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

import com.seu.ni.demo.UI.Fragment.FragmentChildA;
import com.seu.ni.demo.UI.Fragment.FragmentChildB;

/**
 * Created by Ni on 2016/2/6.
 */
// Here we use FragmentPagerAdapter from package "android.support.v13.app",
// for FragmentPagerAdapter is not in Android.app, it can be found in v4 and v13
// if we use v4.FragmentPagerAdapter, then constructor parament: FragmentManager must be v4.FragmentManager

public class DemoPagerAdapter  extends FragmentPagerAdapter{
    public DemoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f =null;
        switch (position){
            case 0 :
                f = new FragmentChildA();
                break;
            case 1:
                f = new FragmentChildB();
                break;
            case 2:
                Bundle bundle  = new Bundle();
                bundle.putString("NAME","FragmentChildC");
                bundle.putInt("NUM",3);
                f = new FragmentChildC();
                f.setArguments(bundle);
                break;
        }
        return f;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
