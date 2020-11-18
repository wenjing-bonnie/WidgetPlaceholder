package com.android.widgetplaceholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * Created by wenjing.liu on 2020/10/30 .
 * 用来测试GridView的属性值设置方式的优先级的问题:
 * 该主题中含有defAttrStyle，并且defAttrStyle中有name属性
 *
 * @author wenjing.liu
 */
public class GridViewAttrSettingTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_attr);
    }

    public void onThemeWithoutDefStyleAttr(View view) {
        Intent intent = new Intent(GridViewAttrSettingTestActivity.this, GridViewThemeAttrSettingTestActivity.class);
        startActivity(intent);
    }

    public void onThemeWithDefStyleNoNameAttr(View view) {
        Intent intent = new Intent(GridViewAttrSettingTestActivity.this, GridViewThemeAttr1SettingTestActivity.class);
        startActivity(intent);
    }

}
