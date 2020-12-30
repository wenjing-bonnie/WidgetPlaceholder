package com.android.widgetplaceholder.holder;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by wenjing.liu on 2020/12/4 in J1.
 * <p>
 * 逻辑配置
 * <p>
 * 1.实例化PlaceHolder
 * placeHolder = new PlaceHolder.Builder(PlaceHolderActivity.this)
 * //设置为非圆角
 * //.setPlaceHolderBackgroundColor(Color.YELLOW)
 * //.setPlaceHolderBackgroundResource(R.drawable.bg)
 * //可以设置圆角的
 * .setPlaceHolderBackgroundCorner(Color.RED, 20)
 * .build();
 * 如果没有设置背景的颜色，背景色则将会按照"使用文字默认的字体颜色>字体的背景色>DEFAULT_BACKGROUND",并且在上面增加一个透明度，防止字体颜色是黑色系的时候，显示的太难看
 * 2.在setContentView()之后,需要预占位的控件完全设置完背景之后
 * placeHolder.startPlaceHolderChild();
 * 3.在完成之后,调用placeHolder.stopPlaceHolderChild();来释放预占位的UI
 *
 * @author wenjing.liu
 */
public class PlaceHolder {

    public final static int ANIMATION_NONE = 0;
    public final static int ANIMATION_SWING = 1;
    public final static int ANIMATION_BACKGROUND_COLORS = 2;

    @IntDef({ANIMATION_NONE, ANIMATION_SWING, ANIMATION_BACKGROUND_COLORS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationMode {
    }

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
         * 禁用预加载占位符功能，默认为false，即不禁用
         *
         * @param isDisable true：禁用；false:不禁用
         * @return
         */
        public Builder setPlaceHolderDisable(boolean isDisable) {
            parameter.isDisable = isDisable;
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
         * 可以设置背景色的过渡动画
         *
         * @param color
         * @return
         */
        public Builder setPlaceHolderBackgroundColor(@ColorInt int... color) {
            parameter.settingBackgroundColors = color;
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
            parameter.settingBackgroundColor = color;
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
            parameter.settingBackgroundColor = 0;
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

        /**
         * 设置动画效果
         *
         * @return
         */
        public Builder setPlaceHolderAnimationDuration(int duration) {
            parameter.duration = duration;
            return this;
        }

        public Builder setPlaceHolderAnimationMode(@AnimationMode int mode) {
            parameter.animationMode = mode;
            return this;
        }

        public PlaceHolder build() {
            holder = new PlaceHolder(activity, parameter);
            return holder;
        }

    }


}
