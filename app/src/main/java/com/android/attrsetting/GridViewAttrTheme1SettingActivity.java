package com.android.attrsetting;

import android.app.Activity;
import android.os.Bundle;

import com.android.widgetplaceholder.R;


/**
 * Created by wenjing.liu on 2020/10/30 .
 * 用来测试GridView的属性值设置方式的优先级的问题:
 * 该主题中含有defAttrStyle，并且defAttrStyle中有name属性
 *
 * @author wenjing.liu
 */
public class GridViewAttrTheme1SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_attr_theme1);
        getActionBar().setTitle("都设置name");
    }

}
