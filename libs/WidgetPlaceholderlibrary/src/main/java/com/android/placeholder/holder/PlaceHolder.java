package com.android.placeholder.holder;

import android.app.Activity;
import android.content.Context;
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
 * <使用规则>
 *      <ol>
 *          <li>1.实例化{@link PlaceHolder},并通过{@link PlaceHolder.Builder}设置参数</li>
 *              <code>
 *                  placeHolder = new PlaceHolder.Builder(PlaceHolderActivity.this).build();
 *              </code>
 *          <li>2.在{@link Activity#setContentView(View)}之后,需要布局文件中的控件完全设置完背景之后调用{@link #startPlaceHolderChild()}</li>
 *              <code>
 *                   placeHolder.startPlaceHolderChild();
 *              </code>
 *          <li>3.在完成之后,调用{@link #stopPlaceHolderChild()}来释放预占位的UI</li>
 *              <code>
 *                  placeHolder.stopPlaceHolderChild();
 *              </code>
 *      </ol>
 * </使用规则>
 * <p>
 * <背景的颜色的优先级>
 *      背景色则将会按照下面的优先级来设置预占位背景:
 *      <ol>
 *          <li>(1)背景颜色过渡动画值{@link PlaceHolder.Builder#setPlaceHolderBackgroundColor(int...)}</li>
 *          <li>(2)设置的背景色{@link PlaceHolder.Builder#setPlaceHolderBackgroundColor(int)}或背景图片{@link PlaceHolder.Builder#setPlaceHolderBackground(Drawable)}<li/>
 *          <li>(3)使用文字默认的字体颜色{@link android.widget.TextView#setTextColor(int)}</li>
 *          <li>(4)字体的背景色{@link android.widget.TextView#setBackground(Drawable)}</li>
 *          <li>(5){link PlaceHolderImpl#DEFAULT_BACKGROUND}</ol>
 *      </ol>
 *      注意：默认会在上面颜色的基础上增加一个透明度，防止字体颜色是黑色系的时候，显示的太难看
 * </背景的颜色的优先级>
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

    /**
     * 该方法必须调用在需要布局文件中的控件完全设置完背景之后调用
     */
    public void startPlaceHolderChild() {
        impl.startPlaceHolderChild();
    }

    public void startPlaceHolderChild(View parent) {
        impl.startPlaceHolderChild(parent);
    }

    /**
     * 该View已经加载完毕
     */
    public void stopPlaceHolderChild() {
        impl.stopPlaceHolderView();
    }


    public static class Builder {
        PlaceHolder holder;
        private Activity activity;
        private PlaceHolderParameter parameter;

        public Builder(Context context) {
            // if (context instanceof  Activity){
            this(((Activity) context));
            // }

        }

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
         * 可设置背景的corner
         * 该背景值样式只可通过{@link #setPlaceHolderBackgroundColor(int)}设置背景颜色值或控件默认的设置的值
         * 默认规则为：TextView默认的使用文字或color背景的颜色，ImageView默认的是#999999
         *
         * @param corner
         * @return
         */
        public Builder setPlaceHolderBackgroundCorner(int corner) {
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
         * 设置动画的模式
         *
         * @param mode
         * @return
         */
        public Builder setPlaceHolderAnimationMode(@AnimationMode int mode) {
            parameter.animationMode = mode;
            return this;
        }

        /**
         * 生成PlaceHolder对象
         *
         * @return
         */
        public PlaceHolder build() {
            holder = new PlaceHolder(activity, parameter);
            return holder;
        }

    }


}
