package com.android.widgetplaceholder.holder;

import android.graphics.drawable.Drawable;

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
     * 设置的背景图片
     */
    protected Drawable settingBackgroundDrawable;

    /**
     * 设备背景的圆角,如果设置圆角，只能设置颜色，其他Drawable类型不支持
     */
    protected @ColorInt
    int settingCornerBackgroundColor;
    protected int cornerRadius;
}
