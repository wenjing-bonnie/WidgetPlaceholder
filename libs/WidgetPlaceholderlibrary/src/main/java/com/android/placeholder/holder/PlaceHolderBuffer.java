package com.android.placeholder.holder;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by wenjing.liu on 2020/12/7 in J1.
 * <p>
 * 用来缓存在重新设置之前控件的属性
 *
 * @author wenjing.liu
 */
public class PlaceHolderBuffer {
    /**
     * 原控件的背景色
     */
    protected Drawable bgDrawable;
    /**
     * 原控件的文字颜色
     */

    protected ColorStateList textColor;
    /**
     * 原控件的图片资源
     */

    protected Drawable srcDrawable;

    /**
     * 要设置预占位的控件
     */
    protected View childView;
    /**
     * 针对CompoundButton，例如RadioButton的button
     */
    protected Drawable buttonDrawable;

}
