package com.android.widgetplaceholder;

import android.app.Activity;
import android.os.Bundle;


/**
 * Created by wenjing.liu on 2020/10/30 .
 *
 * @author wenjing.liu
 */
public class PhotoSelectorViewTestActivity extends Activity {
    private GridView gridView;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_selector_view);
        gridView = findViewById(R.id.gv_test);
        gridView.notifyDataSetChanged(7);

        gridLayout = findViewById(R.id.gv_layout);
        gridLayout.notifyDataSetChanged(7);
    }
}
