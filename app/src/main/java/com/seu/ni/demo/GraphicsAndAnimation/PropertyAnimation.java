package com.seu.ni.demo.GraphicsAndAnimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.seu.ni.demo.R;

public class PropertyAnimation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);


        final ImageView iv = (ImageView) findViewById(R.id.iv_anim_property);
        final Button bt_propertyValueHolder = (Button) findViewById(R.id.bt_anim_propertyValueHolder);
        final Button bt_AnimatorSet = (Button) findViewById(R.id.bt_anim_AnimatorSet);
        final Button bt_ValueAnimator = (Button) findViewById(R.id.bt_anim_ValueAnimator);
        final Button bt_anim_viewPropertyAnimator = (Button) findViewById(R.id.bt_anim_viewPropertyAnimator);

        { // easiest way to start a property animation
            ObjectAnimator oa = new ObjectAnimator();
            oa.ofFloat(iv, "alpha", 0.0F, 0.5F).setDuration(1000).start();
        }

        // 多属性动画同时工作管理类
        bt_propertyValueHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyValuesHolder holder_alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.5f);
                PropertyValuesHolder holder_scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1.0f);
                ObjectAnimator.ofPropertyValuesHolder(iv, holder_alpha, holder_scaleX).setDuration(1000).start();


            }
        });
        // 动画集合，提供把多个动画组合成一个组合的机制，并可设置动画的时序关系，如同时播放、顺序播
        bt_AnimatorSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa_alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.5f, 1.0f);
                ObjectAnimator oa_scaleX = ObjectAnimator.ofFloat(iv, "scaleX", 1.0f, 2.0f);

                AnimatorSet as = new AnimatorSet();
                as.playTogether(oa_alpha, oa_scaleX);
                //  as.play(oa_alpha).after(oa_scaleX);
                as.setDuration(1000);
                as.start();
            }
        });
        // ValueAnimator
        //属性动画中的时间驱动，管理着动画时间的开始、结束属性值，相应时间属性值计算方法等。
        // 包含所有计算动画值的核心函数以及每一个动画时间节点上的信息、一个动画是否重复、是否监听更新事件等，并且还可以设置自定义的计算类型。
        // 特别注意：ValueAnimator只是动画计算管理驱动，设置了作用目标，但没有设置属性，需要通过updateListener里设置属性才会生效。

        bt_ValueAnimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
                final int screenWidth = getWindowManager().getDefaultDisplay().getWidth();

                ValueAnimator va = new ValueAnimator();
                va.setTarget(bt_ValueAnimator);

                // 自己实现 accelerate-decelerate-interpolator 插值器
                va.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        return (float) (Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
                    }
                });
                //设置属性值
                va.setObjectValues(new float[2]);
                //属性值计算（这里是个抛物线）
                va.setEvaluator(new TypeEvaluator() {
                    @Override
                    public Object evaluate(float fraction, Object startValue, Object endValue) {
                        float xyPos[] = new float[2];
                        xyPos[0] = screenWidth * (-fraction);
                        xyPos[1] = fraction * fraction * screenHeight;
                        return xyPos;
                    }
                });
                // 更新监听,更新 属性值
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float xyPos[] = (float[]) animation.getAnimatedValue();
                        bt_ValueAnimator.setTranslationX(xyPos[0]);
                        bt_ValueAnimator.setTranslationY(xyPos[1]);
                    }
                });

                va.setDuration(5000).start();

            }
        });

        bt_anim_viewPropertyAnimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.animate().x(0f).y(100f).alpha(0.5f).start();
            }
        });

    }


}
