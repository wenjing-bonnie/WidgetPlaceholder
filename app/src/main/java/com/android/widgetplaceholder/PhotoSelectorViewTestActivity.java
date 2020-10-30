package com.android.widgetplaceholder;

import android.app.Activity;
import android.os.Bundle;


/**
 * Created by wenjing.liu on 2020/10/30 .
 *
 * @author wenjing.liu
 */
public class PhotoSelectorViewTestActivity extends Activity {
    private PhotoSelectorView selectorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_selector_view);
        selectorView = findViewById(R.id.psv_test);
    }
}
