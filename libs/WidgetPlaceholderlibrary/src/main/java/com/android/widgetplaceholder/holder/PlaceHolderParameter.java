package com.android.widgetplaceholder.holder;

import android.graphics.drawable.Drawable;
import android.view.View;

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
    protected boolean isDisable = false;
    /**
     * 设置的背景图片或者颜色
     */
    protected Drawable settingBackgroundDrawable;
    /**
     * 背景的圆角弧度
     */
    protected int cornerRadius;
    /**
     * 不需要设置预背景的控件
     */
    protected int[] withoutChildIds;
    protected View[] withoutChildes;
    /**
     * 动画持续时间
     */
    protected int duration;
    /**
     * 动画样式
     */
    protected int animationMode = PlaceHolder.ANIMATION_NONE;
    protected int[] settingBackgroundColors;
}
