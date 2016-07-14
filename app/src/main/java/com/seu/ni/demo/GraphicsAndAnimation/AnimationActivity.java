package com.seu.ni.demo.GraphicsAndAnimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.seu.ni.demo.R;


public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mPic, mLoadingPic;
    private AnimationView mView;
    private Animation mAnimation;
    private ObjectAnimator mObjectAnimator;
    private Button mChangeAlpha, mChangeScale, mChangeTranslate, mChangeRotate, mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        initView();
        initData();
        AnimationSet animationSet;
        AnimatorSet animatorSet;
    }

    private void initView() {
        RelativeLayout root = (RelativeLayout) findViewById(R.id.id_anim_layout);
        mView = new AnimationView(this);
        root.addView(mView);

        mPic = (ImageView) findViewById(R.id.iv_anim_show);
        mLoadingPic = (ImageView) findViewById(R.id.iv_anim_loading);
        mChangeAlpha = (Button) findViewById(R.id.bt_anim_alpha);
        mChangeScale = (Button) findViewById(R.id.bt_anim_scale);
        mChangeTranslate = (Button) findViewById(R.id.bt_anim_translate);
        mChangeRotate = (Button) findViewById(R.id.bt_anim_rotate);
        mLoading = (Button) findViewById(R.id.bt_anim_loading);
    }

    private void initData() {
        mChangeAlpha.setOnClickListener(this);
        mChangeScale.setOnClickListener(this);
        mChangeTranslate.setOnClickListener(this);
        mChangeRotate.setOnClickListener(this);
        mLoading.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int type = 0;
        mAnimation = null;
        switch (v.getId()) {
            case R.id.bt_anim_alpha:
                type = 1;
                mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
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
            case R.id.bt_anim_loading:
                AnimationDrawable a = (AnimationDrawable) mLoadingPic.getDrawable();
                a.start();
            default:
                break;
        }
        if (mAnimation != null && type != 0) {
            mPic.startAnimation(mAnimation);
            mView.onChangeAnim(type);
        }
    }
}