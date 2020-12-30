package com.android.widgetplaceholder.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
    private boolean isAnimationEnable = false;
    private Paint mPaint;
    private @ColorInt int mPaintColor;
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
    /**
     * 背景色变化
     */
    private ValueAnimator mBackgroundAnimator;
    private int[] mColorBackgrounds;


    public PlaceHolderAnimationDrawable(boolean isAnimEnable) {
        mPaint = new Paint();
        mRectF = new RectF();
        this.isAnimationEnable = isAnimEnable;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //没有使用动画或者动画已经完成了初始化（即在动画过程中）
        if (canvasWidth == 0 || canvasHeight == 0) {
            canvasWidth = getBounds().width();
            canvasHeight = getBounds().height();
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
        this.mPaintColor = argb;
        mPaint.setColor(argb);
    }

    /**
     * This drawable to use change color
     *
     * @param color
     */
    public void setBackgroundAnimationColor(@ColorInt int... color) {
        this.mColorBackgrounds = color;
    }

    @Override
    public int getOpacity() {
        //TODO 逻辑还没有写完
        return mAlpha == 255 ? PixelFormat.OPAQUE : PixelFormat.TRANSLUCENT;
    }

    /**
     * Set the animation duration time
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    /**
     * Start animation
     */
    private void startAnimation() {
        if (!isAnimationEnable) {
            return;
        }
        //必须设置动画的时间 TODO 需要考虑下在请求过程中的这个时间怎么计算，所以这个时间估计不能这么设置
        if (mDuration <= 0) {
            throw new IllegalArgumentException("Must set the animation duration ");
        }
        setRightAnimator();
        setBackgroundAnimator();
        AnimatorSet set = new AnimatorSet();
        AnimatorSet.Builder builder = set.play(mRightAnimator);
        if (mBackgroundAnimator != null) {
            builder.with(mBackgroundAnimator);
        }
        // 除数代表的是执行的次数
        set.setDuration(mDuration / 2);

        set.start();
    }

    /**
     * Set right value animation
     */
    private void setRightAnimator() {
        mRightAnimator = ValueAnimator.ofInt(canvasWidth, canvasWidth / 3, canvasWidth);
        mRightAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRightAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawRight = (int) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
    }

    /**
     * Set paint color value animation
     */
    private void setBackgroundAnimator() {
        if (mColorBackgrounds == null || mColorBackgrounds.length == 0) {
            return;
        }
        mBackgroundAnimator = ValueAnimator.ofArgb(mColorBackgrounds);
        mBackgroundAnimator.setRepeatMode(ValueAnimator.RESTART);
        mBackgroundAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaintColor = (int) animation.getAnimatedValue();
                setColor(mPaintColor);
            }
        });
    }

    /**
     * Finish the animation
     */
    public void clearAnimation() {
        if (!isAnimationEnable) {
            return;
        }
        clearSwingAnimation();
        clearBackgroundAnimation();
    }


    private void clearSwingAnimation() {
        if (mRightAnimator == null) {
            return;
        }
        mRightAnimator.cancel();
        mRightAnimator = null;
        canvasWidth = 0;
        canvasHeight = 0;
        drawRight = 0;
    }

    private void clearBackgroundAnimation() {
        if (mBackgroundAnimator == null) {
            return;
        }
        mBackgroundAnimator.cancel();
        mBackgroundAnimator = null;
    }
}
