package com.android.attrsetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.widgetplaceholder.R;

public class GridViewAttrSettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_attr_setting);
    }

    public void onThemeDefStyleAttr(View view) {
        Intent intent = new Intent(GridViewAttrSettingActivity.this, GridViewAttrTheme1SettingActivity.class);
        startActivity(intent);
    }

    public void onThemeWithoutDefStyleAttr(View view) {
        Intent intent = new Intent(GridViewAttrSettingActivity.this, GridViewAttrTheme2SettingActivity.class);
        startActivity(intent);
    }

    public void onThemeWithDefStyleNoNameAttr(View view) {
        Intent intent = new Intent(GridViewAttrSettingActivity.this, GridViewAttrTheme3SettingActivity.class);
        startActivity(intent);
    }

}