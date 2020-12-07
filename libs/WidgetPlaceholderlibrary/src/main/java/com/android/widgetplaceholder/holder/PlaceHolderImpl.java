package com.android.widgetplaceholder.holder;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.widgetplaceholder.utils.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenjing.liu on 2020/12/4 in J1.
 *
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
     * 开始循环遍历布局文件中的所有的子View
     */
    protected void startPlaceHolderChild() {
        ViewGroup content = activity.findViewById(android.R.id.content);
        startPlaceHolderChild(content);
    }

    private void startPlaceHolderChild(View viewGroup) {
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
     * 保存控件的原布局参数，然后更新UI
     *
     * @param child
     */
    private void placeHolderView(View child) {
        Log.d("id = " + child.getId() + " , child = " + child);
        if (child instanceof TextView
                || child instanceof ImageView
                || child instanceof Button) {
            PlaceHolderBuffer buffer = new PlaceHolderBuffer();
            buffer.bgDrawable = child.getBackground();
            childBgParams.put(child, buffer);
            Drawable bg = param.settingBackgroundDrawable;
            child.setBackground(bg == null ? new ColorDrawable(Color.parseColor(DEFAULT_BACKGROUND)) : param.settingBackgroundDrawable);
        }
    }

    /**
     * 还原
     */
    protected void stopPlaceHolderView() {
        restorePlaceHolderView();
    }

    private void restorePlaceHolderView() {
        if (childBgParams == null || childBgParams.isEmpty()) {
            return;
        }
        Log.d("Restore place holder view count is " + childBgParams.size());
        for (View child : childBgParams.keySet()) {
            Log.d("This child is " + child.getClass().getSimpleName());
            child.setBackground(childBgParams.get(child).bgDrawable);
        }

        childBgParams.clear();
    }
}
