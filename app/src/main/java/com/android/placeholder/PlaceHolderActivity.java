package com.android.placeholder;

import android.os.Bundle;

import com.android.base.SubActivity;
import com.android.widgetplaceholder.R;

public class PlaceHolderActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_holder);
    }
}