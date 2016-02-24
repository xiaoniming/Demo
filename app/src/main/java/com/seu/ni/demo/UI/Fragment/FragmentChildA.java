package com.seu.ni.demo.UI.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seu.ni.demo.R;

/**
 * Created by NM on 2015-11-29.
 */
public class FragmentChildA extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_child_a, container, false);

    }
}
