package com.seu.ni.demo.UI.ViewPager;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seu.ni.demo.R;

/**
 * Created by Ni on 2016/2/6.
 */
public class FragmentChildC extends Fragment {
    String mName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        Log.v("-------",bundle.getString("NAME"));
        if (bundle.containsKey("NAME")) {
            mName = bundle.getString("NAME");
        } else mName = "cannot get fragment name";
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_child_c, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.tv_fragment_name);
        textView.setText(mName);
        return rootView;
    }
}
