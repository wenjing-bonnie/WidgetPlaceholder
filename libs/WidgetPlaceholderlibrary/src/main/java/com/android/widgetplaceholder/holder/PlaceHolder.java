package com.android.widgetplaceholder.holder;

import android.app.Activity;

/**
 * Created by wenjing.liu on 2020/12/4 in J1.
 *
 * @author wenjing.liu
 */
public class PlaceHolder {

    private Activity activity;
    private PlaceHolderImpl impl;

    public PlaceHolder(Activity activity) {
        this.activity = activity;
        impl = new PlaceHolderImpl(activity);
    }

    public void startPlaceHolderChild() {
        impl.startPlaceHolderChild();
    }

    public void stopPlaceHolderChild() {
        impl.stopPlaceHolderView();
    }


}
