package com.android.widgetplaceholder.holder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;

/**
 * Created by wenjing.liu on 2020/12/4 in J1.
 * <p>
 * 用来保持设置的背景样式
 *
 * @author wenjing.liu
 */
public class PlaceHolderParameter {
    /**
     * 设置该功能不起作用
     * 如果在全局应用该功能的化，有可能有些特殊页面不需要使用该功能，所以增加一个选项来关闭该功能
     */
    protected boolean isDisEnable = false;
    /**
     * 设置的背景图片
     */
    protected Drawable settingBackgroundDrawable;

    /**
     * 设备背景的圆角,如果设置圆角，只能设置颜色，其他Drawable类型不支持
     */
    protected @ColorInt
    int settingCornerBackgroundColor;
    protected int cornerRadius;
    /**
     * 不需要设置预背景的控件
     */
    protected int[] withoutChildIds;
    protected View[] withoutChildes;
    /**
     * 动画样式
     */
    protected int animationStyle;
    /**
     * 动画持续时间
     */
    protected int duration;
}
