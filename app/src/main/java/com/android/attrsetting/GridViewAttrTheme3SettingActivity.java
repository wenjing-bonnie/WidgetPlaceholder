package com.android.attrsetting;

import android.os.Bundle;
import android.view.View;

import com.android.base.SubActivity;
import com.android.widgetplaceholder.R;

/**
 * Created by wenjing.liu on 2020/11/17 in J1.
 * 用来测试GridView的属性值设置方式的优先级的问题:
 * 该Activity的主题中含defStyleAttr,但没有给name赋值
 *
 * @author wenjing.liu
 */
public class GridViewAttrTheme3SettingActivity extends SubActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_attr_theme2);
        getActionBar().setTitle("主题设置defAttrStyle但不设置name");
    }

    public void onThemeWithoutDefStyleAttr(View view) {
        finish();
    }
}
