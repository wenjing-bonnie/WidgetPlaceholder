package com.android.widgetplaceholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by wenjing.liu on 2020/11/17 in J1.
 * 该Activity的主题中不含邮defStyleAttr
 *
 * @author wenjing.liu
 */
public class GridViewThemeAttrSettingTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_attr_without);
    }

    public void onThemeWithoutDefStyleAttr(View view) {
        finish();
    }
}
