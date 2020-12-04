package com.android.placeholder;

import android.os.Bundle;

import com.android.base.SubActivity;
import com.android.widgetplaceholder.R;
import com.android.widgetplaceholder.holder.PlaceHolder;

public class PlaceHolderActivity extends SubActivity {
    PlaceHolder placeHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_holder);
        placeHolder = new PlaceHolder(PlaceHolderActivity.this);
        placeHolder.startPlaceHolderChild();
        stopPlaceHolder();
    }

    private void stopPlaceHolder() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                placeHolder.stopPlaceHolderChild();
            }
        }.start();
    }

}