package com.seu.ni.demo.GraphicsAndAnimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.seu.ni.demo.R;

/**
 * Created by ni on 2016/2/24.
 */
public class AnimationView extends View implements Animation.AnimationListener, AnimCallBack {

    private Paint mPaint;

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(20);
        setFocusable(true);

    }

    public <T> void L(T para) {
        Log.v("-----Anim", para + "");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Matrix matrix = new Matrix();
        matrix.postScale(0.2f, 0.2f);
        Bitmap obitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.pic_qiaoba);
        Bitmap bitmap = Bitmap.createBitmap(obitmap, 0, 0, obitmap.getWidth(), obitmap.getHeight(), matrix, true);
        canvas.drawBitmap(bitmap, 0, getHeight() / 2 - bitmap.getHeight() / 2,
                mPaint);
    }

    @Override
    public void onChangeAlpha() {
       Animation mAnimation = new ScaleAnimation(0f, 1f, 0f, 2f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,0f);
//        AlphaAnimation mAnimation = new AlphaAnimation(0.1f, 1.0f);
        mAnimation.setDuration(5000);
        this.startAnimation(mAnimation);

        L("here");
    }

    @Override
    public void onChangeScale() {

    }

    @Override
    public void onChangeRotate() {

    }

    @Override
    public void onChangeTranslate() {

    }

    @Override
    public void onAnimationStart(Animation animation) {
        L("start");

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        L("end");

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
