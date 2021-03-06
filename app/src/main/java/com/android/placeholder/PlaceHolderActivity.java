package com.android.placeholder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.base.SubActivity;
import com.android.placeholder.holder.PlaceHolder;
import com.android.widgetplaceholder.R;

/**
 * @author wenjing.liu
 */
public class PlaceHolderActivity extends SubActivity {
    PlaceHolder placeHolder;
    private TextView tvResetBg;
    private TextView tvTestCode;
    private Button btnTestCode;
    private ImageView ivTestCode;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            placeHolder.stopPlaceHolderChild();
            //tvResetBg.setBackgroundResource(R.drawable.ic_launcher_foreground);
            // tvResetBg.setBackgroundColor(Color.GREEN);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_holder);
        tvResetBg = findViewById(R.id.tv_test_reset_bg);
        //tvResetBg.setTextColor(Color.YELLOW);
        tvTestCode = findViewById(R.id.tv_test_code);
        btnTestCode = findViewById(R.id.btn_test_code);
        ivTestCode = findViewById(R.id.iv_test_code);

        tvTestCode.setBackgroundColor(Color.RED);
        ivTestCode.setBackground(getDrawable(R.drawable.bg));
        ivTestCode.setImageResource(R.drawable.computer);

        startPlaceHolder();
    }


    private void startPlaceHolder() {
        placeHolder = new PlaceHolder.Builder(PlaceHolderActivity.this)
                //设置为非圆角
                //.setPlaceHolderBackgroundColor(Color.YELLOW)
                //.setPlaceHolderBackgroundResource(R.drawable.bg)
                //可以设置圆角的
                .setPlaceHolderBackgroundCorner(20)
                //.withoutPlaceHolder(tvResetBg)
                //.withoutPlaceHolder(R.id.tv_test_code,R.id.ll_code_layout)
                //.setPlaceHolderDisable(true)
               // .setPlaceHolderAnimationMode(PlaceHolder.ANIMATION_SWING)
               // .setPlaceHolderBackgroundColor(Color.GRAY, Color.LTGRAY, Color.GRAY)
                .build();
        placeHolder.startPlaceHolderChild();
        stopPlaceHolder();
    }

    private void stopPlaceHolder() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                handler.sendMessageAtTime(msg, 0);

            }
        }.start();
    }

    public void goPlaceHolderListViewActivity(View view) {
        Intent intent = new Intent(PlaceHolderActivity.this, PlaceHolderListViewActivity.class);
        startActivity(intent);
    }

}