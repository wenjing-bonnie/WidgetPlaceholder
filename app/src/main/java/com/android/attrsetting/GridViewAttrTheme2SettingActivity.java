package com.android.attrsetting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.widgetplaceholder.R;

/**
 * Created by wenjing.liu on 2020/11/17 in J1.
 * 用来测试GridView的属性值设置方式的优先级的问题:
 * 该Activity的主题中不含defStyleAttr
 *
 * @author wenjing.liu
 */
public class GridViewAttrTheme2SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_attr_theme2);
        getActionBar().setTitle("主题设置name但不设置defAttrStyle");
    }

    public void onThemeWithoutDefStyleAttr(View view) {
        finish();
    }
}
