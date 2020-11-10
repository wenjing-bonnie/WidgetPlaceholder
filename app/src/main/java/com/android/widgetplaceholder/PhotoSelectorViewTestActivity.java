package com.android.widgetplaceholder;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wenjing.liu on 2020/10/30 .
 *
 * @author wenjing.liu
 */
public class PhotoSelectorViewTestActivity extends Activity {
    private PhotoSelectorView selectorView;
    private PhotoSelectorLayout selectorLayout;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_selector_view);
        selectorView = findViewById(R.id.psv_test);
        selectorLayout = findViewById(R.id.psv_test1);
        gridView = findViewById(R.id.gv_test);


        Uri defaultUri = Uri.fromFile(new File(String.valueOf(R.drawable.icon_camera)));
        List<Uri> uris = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            uris.add(defaultUri);
        }
        selectorView.notifyDataSetChanged(uris);
        selectorLayout.notifyDataSetChanged(uris);
        gridView.notifyDataSetChanged(7);
    }
}
