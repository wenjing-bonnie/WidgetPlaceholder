package com.android.widgetplaceholder.holder;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.widgetplaceholder.utils.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenjing.liu on 2020/12/4 in J1.
 * 逻辑实现
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
        if (this.param.isDisEnable) {
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
        if (this.param.isDisEnable) {
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
        child.setTextColor(Color.TRANSPARENT);
        setChildBackground(child);
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
        ViewGroup.LayoutParams params = child.getLayoutParams();
//         Log.d("width = " + buffer.srcDrawable.getIntrinsicWidth() + " , height = " + buffer.srcDrawable.getIntrinsicHeight());
//         Log.d("width = " + params.width + " , height = " + params.height);
//         Log.d("width = " + child.getMeasuredWidth() + " , height = " + child.getMeasuredHeight());

        //重新设置
//        if (params.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
//            params.width = buffer.srcDrawable.getIntrinsicWidth();
//        }
//        if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
//            params.height = buffer.srcDrawable.getIntrinsicHeight();
//        }
        //TODO 如果ImageView去掉src之后，而ImageView设置的wrap_content的区域怎么办
        //child.setLayoutParams(params);
        child.setImageDrawable(null);
        setChildBackground(child);
    }


    /**
     * 设置placeholder的背景
     *
     * @param child
     */
    private void setChildBackground(View child) {
        if (param.cornerRadius > 0) {
            GradientDrawable cornerBackground = new GradientDrawable();
            cornerBackground.setColor(param.settingCornerBackgroundColor != 0 ? param.settingCornerBackgroundColor : Color.parseColor(DEFAULT_BACKGROUND));
            cornerBackground.setCornerRadius(param.cornerRadius);
            child.setBackground(cornerBackground);
            return;
        }
        Drawable bg = param.settingBackgroundDrawable;
        child.setBackground(bg == null ? new ColorDrawable(Color.parseColor(DEFAULT_BACKGROUND)) : bg);
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


    private void restorePlaceHolderTextViewIncludeButton(TextView child, PlaceHolderBuffer buffer) {
        child.setTextColor(buffer.textColor);
        child.setBackground(buffer.bgDrawable);
    }

    private void restorePlaceHolderImageView(ImageView child, PlaceHolderBuffer buffer) {
        child.setImageDrawable(buffer.srcDrawable);
        child.setBackground(buffer.bgDrawable);
    }
}
