package com.android.widgetplaceholder;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.attrsetting.GridViewAttrSettingActivity;
import com.android.placeholder.PlaceHolderActivity;
import com.android.viewlife.ViewLifeCycleActivity;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void goViewLifeCycleActivity(View view) {
        startActivity(ViewLifeCycleActivity.class);
    }

    public void goGridViewAttrSettingActivity(View view) {
        startActivity(GridViewAttrSettingActivity.class);
    }

    public void goPlaceHolderActivity(View view) {
        startActivity(PlaceHolderActivity.class);
    }


    private void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(MainActivity.this, targetClass);
        startActivity(intent);
    }
}