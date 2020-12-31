package com.android.placeholder;

import android.os.Bundle;
import android.widget.ListView;

import com.android.base.SubActivity;
import com.android.widgetplaceholder.R;

import java.util.ArrayList;
import java.util.List;

public class PlaceHolderListViewActivity extends SubActivity {
    private ListView lvTestPlaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_holder_list_view);
        lvTestPlaceHolder = findViewById(R.id.lv_test_place_holder);
        List<String> source = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            source.add("标题标题标题标题标题 " + i);
        }
        PlaceHolderListViewAdapter adapter = new PlaceHolderListViewAdapter(PlaceHolderListViewActivity.this, source);
        lvTestPlaceHolder.setAdapter(adapter);
    }
}