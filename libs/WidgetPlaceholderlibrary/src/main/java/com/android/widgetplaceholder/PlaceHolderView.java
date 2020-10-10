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
 * <p>
 * 生命周期调用：https://www.cnblogs.com/feng9exe/p/9254741.html
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
     * 当前View从xml加载完后触发调用。调用在onCreate中加载xml
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.logV(TAG, "onFinishInflate");
    }

    /**
     * view的状态可见性改变的时候被调用。
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.logV(TAG, "onVisibilityChanged");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.logV(TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.logV(TAG, "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.logV(TAG, "onDraw");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.logV(TAG, "onSizeChanged");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.logV(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.logV(TAG, "onDetachedFromWindow");
    }


}
