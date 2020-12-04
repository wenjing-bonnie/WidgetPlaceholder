package com.android.widgetplaceholder.holder;

import android.app.Activity;
import android.graphics.Color;
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
 * @author wenjing.liu
 */
public class PlaceHolderImpl {
    private Activity activity;
    private Map<View, PlaceHolderParameter> childBgParams;

    protected PlaceHolderImpl(Activity activity) {
        this.activity = activity;
        childBgParams = new HashMap<>();
    }

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

    private void placeHolderView(View child) {
        Log.d("id = " + child.getId() + " , child = " + child);
        if (child instanceof TextView
                || child instanceof ImageView
                || child instanceof Button) {
            PlaceHolderParameter param = new PlaceHolderParameter();
            param.bgDrawable = child.getBackground();
            childBgParams.put(child, param);
            child.setBackgroundColor(Color.GRAY);
        }
    }


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
