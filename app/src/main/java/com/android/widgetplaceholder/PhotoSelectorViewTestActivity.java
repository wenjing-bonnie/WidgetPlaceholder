package com.android.widgetplaceholder;

import android.app.Activity;
import android.os.Bundle;

import com.android.widgetplaceholder.utils.Log;


/**
 * Created by wenjing.liu on 2020/10/30 .
 *
 * @author wenjing.liu
 */
public class PhotoSelectorViewTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grid_view_attr);
        Log.d("GridViewTheme = "+R.style.GridViewTheme);
        Log.d("GridViewStyleInTheme = "+R.style.GridViewStyleInTheme);
        Log.d("GridViewStyleInTheme = "+R.style.GridViewStyleInTheme);
        Log.d("DefaultGridViewStyleRes = "+R.style.DefaultGridViewStyleRes);
    }
}
