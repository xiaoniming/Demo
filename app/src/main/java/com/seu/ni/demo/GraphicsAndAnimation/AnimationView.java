package com.seu.ni.demo.GraphicsAndAnimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.seu.ni.demo.R;

/**
 * Created by ni on 2016/2/24.
 */
public class AnimationView extends View implements Animation.AnimationListener {

    private Paint mPaint;
    private Bitmap mPic;

    public AnimationView(Context context) {
        super(context);
        mPic = getPic();
        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(20);
        setFocusable(true);
    }

    public <T> void L(T para) {
        Log.v("-----Anim", para + "");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measureWidth(width),measureHeight(height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mPic, getPaddingLeft(), getPaddingTop(), mPaint);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //ususlly MatchParent
            result = specSize;
        } else {
            //measure the content
            result = getPaddingLeft() + getPaddingRight() + getExactWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                //usually WrapContent
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    private int measureHeight(int measureSpec) {
        int result = getSuggestedMinimumHeight();
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //ususlly MatchParent
            result = specSize;

        } else {
            //measure the content
            result = getPaddingLeft() + getPaddingRight() + getExactHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                //usually WrapContent
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int getExactWidth() {
        return mPic.getWidth();
    }

    private int getExactHeight() {
        return mPic.getHeight();
    }

    public void onChangeAnim(int type) {
        Animation anim = null;
        switch (type) {
            case 1:
                anim = new AlphaAnimation(0f, 1f);
                break;
            case 2:
                anim = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                break;
            case 3:
                anim = new TranslateAnimation(0, 100, 0, 100);
                break;
            case 4:
                anim = new RotateAnimation(0,368f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

                break;
            default:
                break;
        }

//        AlphaAnimation mAnimation = new AlphaAnimation(0.1f, 1.0f);
        anim.setDuration(5000);
        anim.setFillAfter(true);
        this.startAnimation(anim);

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

    private Bitmap getPic() {
        Matrix m = new Matrix();
        m.postScale(0.2f, 0.2f);
        Bitmap origin = BitmapFactory.decodeResource(this.getResources(), R.drawable.pic_qiaoba);
        Bitmap bitmap = Bitmap.createBitmap(origin, 0, 0, origin.getWidth(), origin.getHeight(), m, true);
        return bitmap;
    }


}
