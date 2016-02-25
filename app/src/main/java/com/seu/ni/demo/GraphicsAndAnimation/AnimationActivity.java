package com.seu.ni.demo.GraphicsAndAnimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.seu.ni.demo.R;

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mPic;
    private Animation mAnimation;
    private Button mButtonAlpha;
    private AnimCallBack mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        init();

//        mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
//        mPic.startAnimation(mAnimation);

    }

    private void init() {
        mPic = (ImageView) findViewById(R.id.iv_anim_show);
        mButtonAlpha = (Button) findViewById(R.id.bt_anim_alpha);

        mButtonAlpha.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_anim_alpha:
//                AnimationView animationView = (AnimationView) findViewById(R.id.id_animation_view);
//                animationView.onChangeAlpha();
                mCallBack.onChangeAlpha();
                break;

        }
    }

    public void setCallBack(AnimCallBack c) {
        mCallBack = c;
    }


}
