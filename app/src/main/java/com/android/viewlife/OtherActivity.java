package com.android.viewlife;

import android.os.Bundle;

import com.android.base.SubActivity;
import com.android.widgetplaceholder.R;

/**
 * Created by wenjing.liu on 2020/10/10 in J1.
 *
 * @author wenjing.liu
 */
public class OtherActivity extends SubActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
