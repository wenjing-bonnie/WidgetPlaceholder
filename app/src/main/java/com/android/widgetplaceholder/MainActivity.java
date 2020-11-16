package com.android.widgetplaceholder;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.widgetplaceholder.utils.Log;

public class MainActivity extends Activity {

    private TextView tvWrapView;
    private TextView tvMatchView;
    private ImageView ivWrapView;
    private ImageView ivMatchView;
    private Button btnWrapView;
    private Button btnMatchView;
    private PlaceHolderView phvView;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate");
        setContentView(R.layout.activity_main);
        initWidget();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume");
    }

    private void initWidget() {
        tvWrapView = findViewById(R.id.tv_wrap);
        tvMatchView = findViewById(R.id.tv_match);
        ivWrapView = findViewById(R.id.iv_wrap);
        ivMatchView = findViewById(R.id.iv_match);
        btnWrapView = findViewById(R.id.btn_wrap);
        btnMatchView = findViewById(R.id.btn_match);
        phvView = findViewById(R.id.phv_view);
    }

    public void viewInvisible(View view) {
        Log.d("count = " + count);
        phvView.setVisibility(count % 2 == 0 ? View.INVISIBLE : View.VISIBLE);
        count++;
    }

    public void viewGone(View view) {
        Log.d("count = " + count);
        phvView.setVisibility(count % 2 == 0 ? View.GONE : View.VISIBLE);
        count++;
    }

    public void goOtherActivity(View view) {
        startActivity(OtherActivity.class);
    }

    public void goExtendsView(View view) {
        startActivity(PhotoSelectorViewTestActivity.class);
    }

    private void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(MainActivity.this, PhotoSelectorViewTestActivity.class);
        startActivity(intent);
    }
}