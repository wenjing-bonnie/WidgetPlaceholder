package com.android.widgetplaceholder.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.widgetplaceholder.utils.Log;

/**
 * Created by wenjing.liu on 2020/12/7 in J1.
 * <p>
 * 设置预占位符含有圆角
 *
 * @author wenjing.liu
 */
public class PlaceHolderAnimationDrawable extends Drawable {
    private Paint mPaint;
    /**
     * Paint透明度
     */
    private int mAlpha;
    /**
     * 绘制的有圆角的矩形
     */
    private RectF mRectF;
    /**
     * 圆角弧度
     */
    private float mRadius;
    /**
     * 产生动画的过渡值
     */
    private ValueAnimator mRightAnimator;
    /**
     * 动画的持续时间
     */
    private int mDuration;
    /**
     * 该Drawable的width/height
     */
    private int canvasWidth;
    private int canvasHeight;
    /**
     * 动画的实时width
     */
    private int drawRight;


    public PlaceHolderAnimationDrawable() {
        mPaint = new Paint();
        mRectF = new RectF();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //没有使用动画或者动画已经完成了初始化（即在动画过程中）
        if (mRightAnimator == null || canvasWidth == 0 || canvasHeight == 0) {
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            drawRight = canvasWidth;
            startAnimation();
        }
        drawRoundRect(canvas, drawRight);
    }

    private void drawRoundRect(Canvas canvas, int right) {
        mRectF.left = 0;
        mRectF.right = right;
        mRectF.top = 0;
        mRectF.bottom = canvasHeight;
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
    }


    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        mPaint.setAlpha(mAlpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    /**
     * Specifies the radius for the corners of the animation drawable.
     *
     * @param radius
     */
    public void setCornerRadius(float radius) {
        mRadius = radius;
    }

    /**
     * This drawable to use a single color .
     *
     * @param argb
     */
    public void setColor(@ColorInt int argb) {
        mPaint.setColor(argb);
    }

    @Override
    public int getOpacity() {
        //TODO 逻辑还没有写完
        return mAlpha == 255 ? PixelFormat.OPAQUE : PixelFormat.TRANSLUCENT;
    }

    /**
     * 设置动画的持续时间
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    /**
     * 开始动画
     */
    private void startAnimation() {
        //必须设置动画的时间 TODO 需要考虑下在请求过程中的这个时间怎么计算，所以这个时间估计不能这么设置
        if (mDuration <= 0) {
            throw new IllegalArgumentException("Must set the animation duration ");
        }
        mRightAnimator = ValueAnimator.ofInt(canvasWidth, canvasWidth / 3, canvasWidth);
        // 除数代表的是执行的次数
        mRightAnimator.setDuration(mDuration / 2);
        mRightAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRightAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawRight = (int) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
        mRightAnimator.start();
    }

    /**
     * 结束动画
     */
    public void clearAnimation() {
        if (mRightAnimator == null) {
            return;
        }
        mRightAnimator.cancel();
        mRightAnimator = null;
        canvasWidth = 0;
        canvasHeight = 0;
        drawRight = 0;
    }
}
