package com.android.widgetplaceholder;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.widgetplaceholder.utils.Log;

/**
 * Created by wenjing.liu on 2020/10/10 .
 *
 * 生命周期调用：
 * (1)该View从xml加载时,默认的状态为Visible:
 *    1)加载完毕,显示出来:
 *    onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged
 * ->onMeasure->onLayout->onDraw
 *    2)状态从Visible变成InVisible:
 *       onVisibilityChanged
 *      状态再由InVisible变成Visible:
 *      onVisibilityChanged->onDraw
 *    3)状态从Visible变成Gone:
 *      onVisibilityChanged
 *      状态再由Gone变成Visible:
 *      onVisibilityChanged->onMeasure->onLayout->onDraw
 * (2)该View从xml加载时,默认的状态为InVisible,加载完毕的时候:
 *      onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged
 * ->onMeasure->onLayout
 * (3)该View从xml加载时,默认的状态为Gone,加载完毕的时候:
 *      onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged
 *
 * @author wenjing.liu
 */
public class PlaceHolderView extends View {
    private final String TAG = getClass().getSimpleName();


    public PlaceHolderView(Context context) {
        super(context);
    }

    public PlaceHolderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 当前View从xml加载完后触发调用。在onCreate中调用setContentView加载xml
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.logV(TAG, "onFinishInflate");
    }

    /**
     * view的状态可见性改变的时候被调用。会随着调用setVisible/onPause/onResume被调用多次
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.logV(TAG, "onVisibilityChanged");
    }

    /**
     * View被附着到Window时触发。即执行Activity的onResume,仅调用一次
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.logV(TAG, "onAttachedToWindow");
    }

    /**
     * 确定View大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.logV(TAG, "onMeasure");
    }

    /**
     * 确定View需要为子View分配尺寸和位置
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.logV(TAG, "onLayout");
    }

    /**
     * View渲染内容的时候
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.logV(TAG, "onDraw");
    }

    /**
     * View从Window上移除的时候,即执行Activity的onDestroy,仅调用一次
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.logV(TAG, "onDetachedFromWindow");
    }


}
