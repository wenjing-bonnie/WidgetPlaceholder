package com.android.widgetplaceholder.grid;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.android.widgetplaceholder.R;

/**
 * Created by wenjing.liu on 2020/10/19 in J1.
 * <p>
 * PhotoSelectorView的封装类：
 * 含有position、ImageView以及是否为添加图片的按钮
 *
 * @author wenjing.liu
 */
public class PhotoSelectorItem {

    private final String TAG = "PhotoSelectorItem";

    /**
     * 图片
     */
    public ImageView imageView;
    /**
     * 该图片在列表中的位置
     */
    public int position;
    /**
     * 是否为添加图片的按钮
     */
    public boolean isCanContinue;

    public Uri uri;

    public String uriPath;

    private Context context;

    public PhotoSelectorItem(Context context) {
        this.context = context;
    }

    public void updateImageView() {
        parsePathFromUri(uri);
    }

    private void parsePathFromUri(final Uri uri) {
        imageView.setImageResource(R.drawable.icon_camera);
    }
}
