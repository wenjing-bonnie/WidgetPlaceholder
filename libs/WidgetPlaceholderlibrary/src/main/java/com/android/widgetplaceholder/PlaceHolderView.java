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
 * 生命周期调用：
 * (1)该View从xml加载时,默认的状态为Visible:
 * 1)加载完毕,显示出来:
 * onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged
 * ->onMeasure->onLayout->onDraw
 * 2)状态从Visible变成InVisible:
 * onVisibilityChanged
 * 状态再由InVisible变成Visible:
 * onVisibilityChanged->onDraw
 * 3)状态从Visible变成Gone:
 * onVisibilityChanged
 * 状态再由Gone变成Visible:
 * onVisibilityChanged->onMeasure->onLayout->onDraw
 * (2)该View从xml加载时,默认的状态为InVisible,加载完毕的时候:
 * onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged
 * ->onMeasure->onLayout
 * (3)该View从xml加载时,默认的状态为Gone,加载完毕的时候:
 * onCreate->onFinishInflate->onResume->onAttachedToWindow->onVisibilityChanged
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
     * 在xml文件中使用该控件的时候，会设置width和height为match_parent/wrap_content/固定大小,
     * match_parent/wrap_content这两个并不是真正的大小，在绘制View的时候，必须有具体的宽和高，所以就必须复写
     * 该方法进行设置控件的大小。
     * widthMeasureSpec和heightMeasureSpec并不是控件的width和height，
     * 而是包含两部分信息：测量模式和测量大小（int为32位：前2位区分模式，后30位存放尺寸）
     * widthMode =
     *
     *
     * 如果继承于ViewGroup：
     * （1）每个子View的大小
     * （2）根据子View计算出ViewGroup的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.logV(TAG, "onMeasure");
        //1.通过这两个方法可以取出模式和尺寸
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //2.然后根据模式为：UNSPECIFIED，EXACTLY，AT_MOST来分别去设置View的大小
        //EXACTLY：固定尺寸，那么最后控件的大小就是widthSize；
        //AT_MOST：match_parent，那么控件的大小就是widthSize；
        //UNSPECIFIED：wrap_content，那么控件就设置为默认的大小
        //同样的方式获取height
        //3.设置该View的width和height
        int measureWidth = 0;
        int measureHeight = 0;
        setMeasuredDimension(measureWidth, measureHeight);

    }

    /**
     * 确定View需要为子View分配尺寸和位置
     *
     * 如果继承于ViewGroup：
     * 放置子View的位置：child.layout(l, t, l +width, t + height);
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
     * 直接在画板Canvas对象和Paint对象绘制View，例如canvas.drawCircle(centerX, centerY, r, paint);
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
