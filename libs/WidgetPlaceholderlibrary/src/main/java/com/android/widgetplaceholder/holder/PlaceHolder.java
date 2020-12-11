package com.android.widgetplaceholder.holder;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

/**
 * Created by wenjing.liu on 2020/12/4 in J1.
 * <p>
 * 逻辑配置
 * 1.实例化placeHolder
 * placeHolder = new PlaceHolder.Builder(PlaceHolderActivity.this)
 * //设置为非圆角
 * //.setPlaceHolderBackgroundColor(Color.YELLOW)
 * //.setPlaceHolderBackgroundResource(R.drawable.bg)
 * //可以设置圆角的
 * .setPlaceHolderBackgroundCorner(Color.RED, 20)
 * .build();
 * 2.在setContentView()之后,需要预占位的控件完全设置完背景之后
 * placeHolder.startPlaceHolderChild();
 * 3.在完成之后,调用placeHolder.stopPlaceHolderChild();来释放预占位的UI
 *
 * @author wenjing.liu
 */
public class PlaceHolder {

    private PlaceHolderImpl impl;

    protected PlaceHolder(Activity activity, PlaceHolderParameter parameter) {
        impl = new PlaceHolderImpl(activity, parameter);
    }

    public void startPlaceHolderChild() {
        impl.startPlaceHolderChild();
    }

    public void stopPlaceHolderChild() {
        impl.stopPlaceHolderView();
    }


    public static class Builder {
        PlaceHolder holder;
        private Activity activity;
        private PlaceHolderParameter parameter;

        public Builder(Activity activity) {
            this.activity = activity;
            parameter = new PlaceHolderParameter();
        }

        /**
         * 设置预加载UI不起作用，默认的为true
         *
         * @param isEnable true:起作用；false:不起作用
         * @return
         */
        public Builder setPlaceHolderDisEnable(boolean isEnable) {
            parameter.isDisEnable = isEnable;
            return this;
        }

        /**
         * 可以设置颜色
         *
         * @param color
         * @return
         */
        public Builder setPlaceHolderBackgroundColor(@ColorInt int color) {
            parameter.settingBackgroundDrawable = new ColorDrawable(color);
            return this;
        }

        /**
         * 可以设置Drawable
         *
         * @param drawable
         * @return
         */
        public Builder setPlaceHolderBackground(Drawable drawable) {
            parameter.settingBackgroundDrawable = drawable;
            return this;
        }

        /**
         * 可以设置Drawable的资源文件
         *
         * @param resource
         * @return
         */
        public Builder setPlaceHolderBackgroundResource(@DrawableRes int resource) {
            parameter.settingBackgroundDrawable = activity.getDrawable(resource);
            return this;
        }

        /**
         * 可以设置背景的corner
         *
         * @param color
         * @param corner
         * @return
         */
        public Builder setPlaceHolderBackgroundCorner(@ColorInt int color, int corner) {
            parameter.settingBackgroundDrawable = null;
            parameter.settingCornerBackgroundColor = color;
            parameter.cornerRadius = corner;
            return this;
        }

        /**
         * 可设置背景的corner，TextView默认的使用文字或color背景的颜色，ImageView默认的是#999999
         *
         * @param corner
         * @return
         */
        public Builder setPlaceHolderBackgroundCorner(int corner) {
            parameter.settingBackgroundDrawable = null;
            parameter.settingCornerBackgroundColor = 0;
            parameter.cornerRadius = corner;
            return this;
        }

        /**
         * 去除不需要考虑预占位的ViewGroup
         *
         * @param withoutChildId 不需要考虑预占位的child的id的集合
         * @return
         */
        public Builder withoutPlaceHolder(@IdRes int... withoutChildId) {
            parameter.withoutChildIds = withoutChildId;
            return this;
        }

        /**
         * 去除不需要考虑预占位的ViewGroup
         *
         * @param child 不需要考虑预占位的child的集合
         * @return
         */
        public Builder withoutPlaceHolder(View... child) {
            parameter.withoutChildes = child;
            return this;
        }

        public PlaceHolder build() {
            holder = new PlaceHolder(activity, parameter);
            return holder;
        }

    }


}
