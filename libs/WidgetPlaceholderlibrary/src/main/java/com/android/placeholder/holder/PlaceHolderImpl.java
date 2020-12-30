package com.android.placeholder.holder;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.android.placeholder.utils.Log;
import com.android.placeholder.view.PlaceHolderAnimationDrawable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenjing.liu on 2020/12/4 in J1.
 * 逻辑实现
 * TODO
 * 1)三者是圆形
 * 2）插件化
 *
 * @author wenjing.liu
 */
public class PlaceHolderImpl {
    private static final String DEFAULT_BACKGROUND = "#dddddd";
    private Activity activity;
    private Map<View, PlaceHolderBuffer> childBgParams;
    private PlaceHolderParameter param;

    protected PlaceHolderImpl(Activity activity, PlaceHolderParameter param) {
        this.activity = activity;
        this.param = param;
        childBgParams = new HashMap<>();
    }

    /**
     * 开始循环遍历布局文件中的所有的子View,应用于Activity/Fragment
     */
    protected void startPlaceHolderChild() {
        //如果不需要使用预占位UI，则直接返回
        if (this.param.isDisable) {
            return;
        }
        ViewGroup content = activity.findViewById(android.R.id.content);
        startPlaceHolderChild(content);
    }

    /**
     * 开始循环遍历给定的ViewGroup上的子View,应用于ListView等
     *
     * @param viewGroup
     */
    protected void startPlaceHolderChild(View viewGroup) {
        //有设置不需要预加载UI的View
        if (isWithoutPlaceHolderView(viewGroup)
                || isWithoutPlaceHolderView(viewGroup.getId())) {
            return;
        }
        if (viewGroup instanceof ViewGroup) {
            int count = ((ViewGroup) viewGroup).getChildCount();
            for (int i = 0; i < count; i++) {
                View child = ((ViewGroup) viewGroup).getChildAt(i);
                if (child instanceof ViewGroup) {
                    startPlaceHolderChild(child);
                } else {
                    placeHolderView(child);
                }
            }
        } else {
            placeHolderView(viewGroup);
        }
    }

    /**
     * 是否为不需要设置预占位的View
     *
     * @param child
     * @return
     */
    private boolean isWithoutPlaceHolderView(View child) {
        if (this.param.isDisable) {
            return true;
        }
        if (this.param.withoutChildes == null || this.param.withoutChildes.length == 0) {
            return false;
        }
        for (View view : this.param.withoutChildes) {
            if (view == child) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否该View为不需要设置预占位
     *
     * @param childId
     * @return true：为不需要设置；false为需要
     */
    private boolean isWithoutPlaceHolderView(int childId) {
        if (this.param.isDisable) {
            return true;
        }
        if (this.param.withoutChildIds == null || this.param.withoutChildIds.length == 0) {
            return false;
        }
        for (int id : this.param.withoutChildIds) {
            if (childId == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * 保存控件的原布局参数，然后更新UI
     *
     * @param child
     */
    private void placeHolderView(View child) {
        Log.d("id = " + child.getId() + " , child = " + child);
        //有设置不需要预加载UI的View
        if (isWithoutPlaceHolderView(child)
                || isWithoutPlaceHolderView(child.getId())) {
            return;
        }
        if (child instanceof TextView) {
            //Button extends TextView
            placeHolderTextViewIncludeButton((TextView) child);
        } else if (child instanceof ImageView) {
            placeHolderImageView((ImageView) child);
        }
    }

    /**
     * TextView/Button控件的处理
     *
     * @param child
     */
    private void placeHolderTextViewIncludeButton(TextView child) {
        //保存之前的信息
        PlaceHolderBuffer buffer = new PlaceHolderBuffer();
        buffer.bgDrawable = child.getBackground();
        buffer.textColor = child.getTextColors();
        childBgParams.put(child, buffer);
        //重新设置
        setChildBackground(child);
        child.setTextColor(Color.TRANSPARENT);
    }

    /**
     * ImageViewView控件的处理
     *
     * @param child
     */
    private void placeHolderImageView(ImageView child) {
        //保存之前的信息
        PlaceHolderBuffer buffer = new PlaceHolderBuffer();
        buffer.bgDrawable = child.getBackground();
        buffer.srcDrawable = child.getDrawable();
        childBgParams.put(child, buffer);
        //TODO 如果ImageView去掉src之后，而ImageView设置的wrap_content的区域怎么办
        //child.setLayoutParams(params);
        setChildBackground(child);
        child.setImageDrawable(null);
    }


    /**
     * 设置placeholder的背景
     * 优先使用设置settingBackgroundDrawable/settingCornerBackgroundColor
     * 若没有设置，则使用文字默认的字体颜色>字体的背景色>DEFAULT_BACKGROUND,并且在上面增加一个透明度，防止字体颜色是黑色系的时候，显示的太难看
     *
     * @param child
     */
    private void setChildBackground(View child) {
        //有圆角的背景 或 设置动画
        if (param.cornerRadius > 0 || isSetAnimationStyle()) {
            int settingBackgroundColor = 0;
            if (param.settingBackgroundDrawable instanceof ColorDrawable) {
                settingBackgroundColor = ((ColorDrawable) param.settingBackgroundDrawable).getColor();
            }
            PlaceHolderAnimationDrawable animationDrawable = new PlaceHolderAnimationDrawable(param.animationMode);
            animationDrawable.setColor(settingBackgroundColor != 0 ? settingBackgroundColor : getTextViewDefaultBackground(child));
            animationDrawable.setAlpha(50);
            animationDrawable.setDuration(param.duration);
            if (param.cornerRadius > 0) {
                animationDrawable.setCornerRadius(param.cornerRadius);
            }
            animationDrawable.setBackgroundAnimationColor(param.settingBackgroundColors);
            animationDrawable.setRemoveOnAttachStateChangeListener(child);
            child.setBackground(animationDrawable);
            return;
        }
        //设置的背景样式或者默认的样式
        Drawable bg = param.settingBackgroundDrawable;
        child.setAlpha(0.5f);
        child.setBackground(bg == null ? new ColorDrawable(getTextViewDefaultBackground(child)) : bg);
    }


    /**
     * 根据字体的颜色或color背景设置TextView的预占位的背景色
     *
     * @param view
     * @return
     */
    private @ColorInt
    int getTextViewDefaultBackground(View view) {
        int defaultColor = Color.parseColor(DEFAULT_BACKGROUND);
        if (!(view instanceof TextView)) {
            return defaultColor;
        }
        TextView child = (TextView) view;
        int setColor = child.getCurrentTextColor();
        if (setColor != 0) {
            return setColor;
        }
        Drawable textBackground = child.getBackground();
        if (textBackground == null || !(textBackground instanceof ColorDrawable)) {
            return defaultColor;
        }
        if (textBackground instanceof ColorDrawable) {
            return ((ColorDrawable) textBackground).getColor();
        }
        return defaultColor;
    }


    /**
     * 还原
     */
    protected void stopPlaceHolderView() {
        restorePlaceHolderView();
    }

    /**
     * 还原之前保存的内容
     */
    private void restorePlaceHolderView() {
        if (childBgParams == null || childBgParams.isEmpty()) {
            return;
        }
        Log.d("Restore place holder view count is " + childBgParams.size());
        for (View child : childBgParams.keySet()) {
            Log.d("This child is " + child.getClass().getSimpleName());
            if (child instanceof TextView) {
                restorePlaceHolderTextViewIncludeButton((TextView) child, childBgParams.get(child));
            } else if (child instanceof ImageView) {
                restorePlaceHolderImageView((ImageView) child, childBgParams.get(child));
            }
        }

        childBgParams.clear();
    }

    /**
     * 还原
     *
     * @param child
     * @param buffer
     */
    private void restorePlaceHolderTextViewIncludeButton(TextView child, PlaceHolderBuffer buffer) {
        stopPlaceHolderAnimation(child);
        child.setTextColor(buffer.textColor);
        child.setBackground(buffer.bgDrawable);

    }

    /**
     * 还原
     *
     * @param child
     * @param buffer
     */
    private void restorePlaceHolderImageView(ImageView child, PlaceHolderBuffer buffer) {
        stopPlaceHolderAnimation(child);
        child.setImageDrawable(buffer.srcDrawable);
        child.setBackground(buffer.bgDrawable);
    }

    /**
     * 结束动画
     *
     * @param child
     */
    private void stopPlaceHolderAnimation(View child) {
        if (!isSetAnimationStyle()) {
            return;
        }
        ((PlaceHolderAnimationDrawable) child.getBackground()).clearAnimation();
    }

    /**
     * 判断是否设置过动画
     *
     * @return
     */
    private boolean isSetAnimationStyle() {
        return param != null && param.animationMode > 0;
    }
}
