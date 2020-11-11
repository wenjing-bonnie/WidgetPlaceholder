package com.android.widgetplaceholder;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenjing.liu on 2020/11/5 in J1.
 * 继承ViewGroup来实现网格UI
 *
 * @author wenjing.liu
 */
public class GridLayout extends ViewGroup {
    private final String TAG = "GridLayout";
    private GridView.OnItemClickListener itemClickListener;
    private boolean DEBUG = true;
    private Context context;
    /**
     * 不包括四周的边距
     */
    private int mHorizontalSpacing = 0;
    private int mVerticalSpacing = 0;
    /**
     * 四周的内边距
     */
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    /**
     * 每行放置几个图片,默认放置4个
     */
    private int mNumColumns = 4;
    /**
     * 总共创建了几行，要随时更新该值,通过传入的selectorList的大小来计算
     */
    private int mNumRow = 1;
    /***
     * 根据每行的个数来调整,每行的宽度
     */
    private int mColumnWidth;
    /**
     * 默认的图片的宽度
     */
    private int defaultItemWidth;
    /**
     * 最多可显示的图片数量，若不设置，则传入集合为多少就显示多少
     */
    private int maxNumber;

    private int childCount;

    private List<PhotoSelectorItem> childGroup;

    /**
     * 每个Item的点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(PhotoSelectorItem item);
    }

    public GridLayout(Context context) {
        this(context, null);
    }

    public GridLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        childGroup = new ArrayList<>();
        initAttributes(attrs);
    }

    private void initAttributes(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PhotoSelectorView);
        if (array == null) {
            return;
        }

        mVerticalSpacing = array.getDimensionPixelSize(R.styleable.PhotoSelectorView_verticalSpacing, 0);
        mHorizontalSpacing = array.getDimensionPixelOffset(R.styleable.PhotoSelectorView_horizontalSpacing, 0);
        maxNumber = array.getInt(R.styleable.PhotoSelectorView_maxNumber, 0);
        mNumColumns = array.getInt(R.styleable.PhotoSelectorView_numColumns, 4);

        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();

        if (DEBUG) {
            Log.d(TAG, String.format("paddingLeft = %d , paddingRight = %d ", paddingLeft, paddingRight));
        }
        array.recycle();
    }

    /**
     * 更新显示的个数
     *
     * @param count
     */
    public void notifyDataSetChanged(int count) {
        if (mNumColumns <= 0) {
            throw new IllegalArgumentException("You must set number of column");
        }
        childCount = count;
        //计算行数
        int result = childCount / mNumColumns;
        mNumRow = childCount % mNumColumns == 0 ? result : (result + 1);
        //得到所有的child
        setAllChildView();
        //刷新UI
        requestLayout();
    }

    /**
     * 设置点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(GridView.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * 获取的是ViewGroup的width和height，所以该控件的height不起作用，width会影响到每个column的宽度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //该控件的宽度
        int width = measureWidth(widthMeasureSpec);
        //根据控件的width和mNumColumns（默认为4个）的计算每个item的宽度
        updateColumnWidth();

        //该控件的高度
        int height = measureHeight(heightMeasureSpec);
        //设置ViewGroup的宽和高
        setMeasuredDimension(width, height);
        if (DEBUG) {
            Log.d(TAG, String.format("onMeasure width = %d , height = %d, mColumnWidth = %d ", width, height, mColumnWidth));
        }
    }

    /**
     * 测量整个控件的宽度
     *
     * @param widthMeasureSpec
     * @return
     */

    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width = widthSize;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                //固定尺寸,需要重新设置下每个column的宽度,要保证这个item能够显示出来
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                // match_parent/wrap_content,根据下面方法的计算规则，重新计算width
                width = measureWidth();
                break;
            default:
        }
        return width;
    }

    /**
     * 计算控件的宽度的顺序为：(不包括margin，但是含padding的宽度)
     * （1）根据传入的ViewGroup的宽度则为具体值.
     * （2）若没有设置width，则若有父控件，则读取父控件的宽度
     * （3）否则采用屏幕宽度
     * （4）否则为默认的宽度
     */
    private int measureWidth() {
        int width = 0;
        Activity activity;
        if (mNumColumns <= 0) {
            throw new IllegalArgumentException("You should set NumColumns > 0 !");
        }

        //屏幕宽度
        if ((context instanceof Activity) &&
                (getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity = (Activity) context;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
            width = metrics.widthPixels - layoutParams.leftMargin - layoutParams.rightMargin;
            return width;
        }
        //默认的宽度
        width = defaultItemWidth * mNumColumns + mVerticalSpacing * (mNumColumns - 1);
        return width;
    }

    /**
     * 根据计算的控件的宽度，重新调整item的宽度
     */
    private void updateColumnWidth() {
        //getMeasuredWidth()获取的是父控件的除去margin的宽度
        mColumnWidth = (getMeasuredWidth() - mHorizontalSpacing * (mNumColumns - 1)
                - paddingLeft - paddingRight) / mNumColumns;
    }

    /**
     * 测量整个控件高度，默认的就是每个Column的width，设置其他值不起作用。
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        //该控件设置height，也不起作用，默认采用的是每个Column的width
        return mColumnWidth * mNumRow + mVerticalSpacing * (mNumRow - 1) + paddingTop + paddingBottom;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int leftItem, topItem, rightItem, bottomItem = 0;
        //每行
        for (int row = 0; row < mNumRow; row++) {
            //每列
            for (int col = 0; col < mNumColumns; col++) {

                int index = row * mNumColumns + col;
                if (index >= childGroup.size()) {
                    break;
                }
                ImageView child = childGroup.get(index).imageView;

                leftItem = paddingLeft + (mColumnWidth + mHorizontalSpacing) * col;
                topItem = paddingTop + (mColumnWidth + mVerticalSpacing) * row;
                rightItem = leftItem + mColumnWidth;
                bottomItem = topItem + mColumnWidth;
                if (DEBUG) {
                    Log.d(TAG, String.format("onLayout row = %d, col = %d, left =%d , top = %d, right = %d, bottom = %d", row, col, leftItem, topItem, rightItem, bottomItem));
                }
                child.layout(leftItem, topItem, rightItem, bottomItem);
                //区别于GridView的不同地方
                addView(child);
            }
        }
    }


    private void setAllChildView() {
        childGroup.clear();
        for (int i = 0; i < childCount; i++) {
            childGroup.add(getChildItem(i, i == childCount - 1 ? true : false));
        }
    }

    private PhotoSelectorItem getChildItem(int index, boolean isCanContinue) {
        final PhotoSelectorItem item = new PhotoSelectorItem(context);
        ImageView child = new ImageView(context);
        child.setBackgroundColor(isCanContinue ? Color.GREEN : Color.RED);
        item.imageView = child;
        item.isCanContinue = isCanContinue;
        item.position = index;
        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DEBUG) {
                    Log.e(TAG, String.format("OnClickListener 第%d个", item.position));
                }
                if (itemClickListener == null) {
                    return;
                }
                itemClickListener.onItemClick(item);
            }
        });
        return item;
    }
}
