package com.seu.ni.demo.UI.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.seu.ni.demo.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentChildB extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_child_b, container, false);
    }
}
