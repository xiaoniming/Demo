package com.seu.ni.demo.GraphicsAndAnimation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.seu.ni.demo.R;


public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mPic;
    private AnimationView mView;
    private Animation mAnimation;
    private Button mChangeAlpha, mChangeScale, mChangeTranslate, mChangeRotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        init();
    }

    private void init() {

        RelativeLayout root = (RelativeLayout) findViewById(R.id.id_anim_layout);
        mView = new AnimationView(this);
        root.addView(mView);

        mPic = (ImageView) findViewById(R.id.iv_anim_show);

        mChangeAlpha = (Button) findViewById(R.id.bt_anim_alpha);
        mChangeScale = (Button) findViewById(R.id.bt_anim_scale);
        mChangeTranslate = (Button) findViewById(R.id.bt_anim_translate);
        mChangeRotate = (Button) findViewById(R.id.bt_anim_rotate);

        mChangeAlpha.setOnClickListener(this);
        mChangeScale.setOnClickListener(this);
        mChangeTranslate.setOnClickListener(this);
        mChangeRotate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int type = 0;
        switch (v.getId()) {
            case R.id.bt_anim_alpha:
                type = 1;
                mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
//                AnimationView animationView = (AnimationView) findViewById(R.id.id_animation_view);
//                animationView.onChangeAlpha();
                break;

            case R.id.bt_anim_scale:
                type = 2;
                mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
                break;

            case R.id.bt_anim_translate:
                type = 3;
                mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
                break;

            case R.id.bt_anim_rotate:
                type = 4;
                mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

                break;

            default:
                break;
        }
        mPic.startAnimation(mAnimation);
        mView.onChangeAnim(type);
    }


}