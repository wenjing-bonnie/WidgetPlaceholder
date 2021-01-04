package com.android.placeholder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.android.base.SubActivity;
import com.android.placeholder.holder.PlaceHolder;
import com.android.placeholder.utils.Log;
import com.android.widgetplaceholder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题1：
 * 如果ListView上面有静态数据，那么在从服务器中获取数据之前，这段时间并不知道ListView上面有几个item，ListView并不会渲染到界面上
 * 所以对于ListView的意义在哪？
 * 针对这种现象，是不是ListView控件就直接用和TextView那种一样，同样方式就可以了呢？
 * <p>
 * 第一个版本先不考虑这种是实际情况，先按照有数据了
 *
 * @author wenjing.liu
 */
public class PlaceHolderListViewActivity extends SubActivity {
    private ListView lvTestPlaceHolder;
    private PlaceHolder placeHolder;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            placeHolder.stopPlaceHolderChild();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_holder_list_view);
        lvTestPlaceHolder = findViewById(R.id.lv_test_place_holder);

        placeHolder = new PlaceHolder.Builder(PlaceHolderListViewActivity.this).build();

        List<String> source = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            source.add(String.format("第%d行：永定河永定河", i));
        }
        PlaceHolderListViewAdapter adapter = new PlaceHolderListViewAdapter(PlaceHolderListViewActivity.this, source, placeHolder);
        lvTestPlaceHolder.setAdapter(adapter);

        Message msg = new Message();
        handler.sendMessageDelayed(msg, 2000);
    }
}