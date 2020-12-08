package com.android.widgetplaceholder.holder;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

/**
 * Created by wenjing.liu on 2020/12/4 in J1.
 * <p>
 * 逻辑配置
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


        public PlaceHolder build() {
            holder = new PlaceHolder(activity, parameter);
            return holder;
        }

    }


}
