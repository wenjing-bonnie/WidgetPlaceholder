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

import com.android.widgetplaceholder.holder.PlaceHolder;

/**
 * Created by wenjing.liu on 2020/12/7 in J1.
 * <p>
 * 设置预占位符含有圆角
 *
 * @author wenjing.liu
 */
public class PlaceHolderAnimationDrawable extends Drawable {
    private @PlaceHolder.AnimationMode int mAnimationMode;
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
     * 一次动画的持续时间
     */
    private int mDuration = 1000;
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


    public PlaceHolderAnimationDrawable(int mode) {
        mPaint = new Paint();
        mRectF = new RectF();
        this.mAnimationMode = mode;
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
     * Set the duration time for one animation from start to end
     *
     * @param duration
     */
    public void setDuration(int duration) {
        if (duration <= 0) {
            return;
        }
        this.mDuration = duration;
    }

    /**
     * Start animation
     */
    private void startAnimation() {
        if (!enableAnimation()) {
            return;
        }
        //必须设置动画的时间 TODO 需要考虑下在请求过程中的这个时间怎么计算，所以这个时间估计不能这么设置
        if (mDuration <= 0) {
            throw new IllegalArgumentException("Must set the animation duration ");
        }
        switch (mAnimationMode) {
            case PlaceHolder.ANIMATION_SWING: {
                startRightAnimator();
                break;
            }
            case PlaceHolder.ANIMATION_BACKGROUND_COLORS: {
                startBackgroundAnimator();
                break;
            }
            default:
        }
    }

    /**
     * Set right value animation infinite until call the {@link #clearAnimation()}
     */
    private void startRightAnimator() {
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
        // 除数代表的是执行的次数
        mRightAnimator.setDuration(mDuration);
        mRightAnimator.start();
    }

    /**
     * Set paint color value animation
     */
    private void startBackgroundAnimator() {
        if (mColorBackgrounds == null || mColorBackgrounds.length == 0) {
            throw new IllegalArgumentException("Must set background colors");
        }
        mBackgroundAnimator = ValueAnimator.ofArgb(mColorBackgrounds);
        mBackgroundAnimator.setRepeatMode(ValueAnimator.RESTART);
        mBackgroundAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaintColor = (int) animation.getAnimatedValue();
                setColor(mPaintColor);
                invalidateSelf();
            }
        });
        // 除数代表的是执行的次数
        mBackgroundAnimator.setDuration(mDuration);
        mBackgroundAnimator.start();
    }

    /**
     * Finish the animation
     */
    public void clearAnimation() {
        if (!enableAnimation()) {
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

    private boolean enableAnimation() {
        return mAnimationMode > 0;
    }
}
