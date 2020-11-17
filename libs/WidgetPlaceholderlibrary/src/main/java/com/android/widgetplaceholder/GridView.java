package com.android.widgetplaceholder;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.android.widgetplaceholder.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenjing.liu on 2020/11/5 in J1.
 * 继承于View来实现网格UI
 *
 * @author wenjing.liu
 */
public class GridView extends View {
    private OnItemClickListener itemClickListener;
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
    /**
     * 初始化个数
     */
    private int initNum;
    /**
     * 测试不同的设置属性值的优先级
     */
    private String name;

    private int childCount;

    private List<PhotoSelectorItem> childGroup;

    /**
     * 每个Item的点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(PhotoSelectorItem item);
    }

    public GridView(Context context) {
        this(context, null, -1, 0);
    }

    public GridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.GridViewStyle, 0);
    }

    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr 对应的是R.attr
     * @param defStyleRes  对应的是R.style
     */
    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        childGroup = new ArrayList<>();
        initAttributes(attrs, defStyleAttr, defStyleRes);
    }


    private void initAttributes(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GridView, defStyleAttr, defStyleRes);
        if (array == null) {
            return;
        }

        Log.d(String.format("defStyleAttr = %d , defStyleRes = %d", defStyleAttr, defStyleRes));
        mVerticalSpacing = array.getDimensionPixelSize(R.styleable.GridView_verticalSpacing, 0);
        mHorizontalSpacing = array.getDimensionPixelOffset(R.styleable.GridView_horizontalSpacing, 0);
        maxNumber = array.getInt(R.styleable.GridView_maxNumber, 0);
        mNumColumns = array.getInt(R.styleable.GridView_numColumns, 4);
        initNum = array.getInt(R.styleable.GridView_initNum, 1);
        name = array.getString(R.styleable.GridView_name);

        Log.v(String.format("mNumColumns = %d , initNum = %d , name = %s", mNumColumns, initNum, name));

        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();

        if (initNum > 0) {
            notifyDataSetChanged(initNum);
        }

        //Log.d(String.format("paddingLeft = %d , paddingRight = %d ", paddingLeft, paddingRight));
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
    public void setOnItemClickListener(OnItemClickListener listener) {
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
        Log.d(String.format("onMeasure width = %d , height = %d, mColumnWidth = %d ", width, height, mColumnWidth));
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
            case MeasureSpec.AT_MOST:
                width = widthSize;
                break;
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
     * （1）采用屏幕宽度
     * （2）否则为默认的宽度
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
        super.onLayout(changed, left, top, right, bottom);
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

                Log.d(String.format("onLayout row = %d, col = %d, left =%d , top = %d, right = %d, bottom = %d", row, col, leftItem, topItem, rightItem, bottomItem));
                child.layout(leftItem, topItem, rightItem, bottomItem);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int transX, transY;
        //每行
        for (int row = 0; row < mNumRow; row++) {
            //将画布坐标系移到每一行的第一列.原canvas是从该view的(0,0)的位置开始的
            if (row > 0) {
                transX = -(mColumnWidth + mHorizontalSpacing) * (mNumColumns - 1);
                transY = mColumnWidth + mVerticalSpacing;

            } else {
                transX = paddingLeft;
                transY = paddingTop;
            }
            canvas.translate(transX, transY);
            //每列
            for (int col = 0; col < mNumColumns; col++) {
                int index = row * mNumColumns + col;
                if (index >= childGroup.size()) {
                    break;
                }

                ImageView child = childGroup.get(index).imageView;
                child.draw(canvas);

                Log.d(String.format("onDraw row = %d, col = %d, left =%d , top = %d, right = %d, bottom = %d", row, col, child.getLeft(), child.getTop(), child.getRight(), child.getBottom()));

                //只要不是最后一列，都要将画布的坐标系依次向后移动
                if (col < mNumColumns - 1) {
                    //平移的是相对于自身的位置
                    canvas.translate((mColumnWidth + mHorizontalSpacing), 0);
                }
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int index = touchWhichItem(event);

                Log.d(String.format("onTouchEvent 第%d个", index));

                if (index < 0 || index > childGroup.size() || itemClickListener == null) {
                    return super.onTouchEvent(event);
                }
                itemClickListener.onItemClick(childGroup.get(index));
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    private int touchWhichItem(MotionEvent event) {
        //获取的该点击坐标系是以该View的坐标系，是相对于View的位置
        float eventX = event.getX();
        float eventY = event.getY();
        int left, top, right, bottom;
        int index = 0;
        Log.v(String.format("touchWhichItem  x =%f , y = %f", eventX, eventY));
        for (PhotoSelectorItem item : childGroup) {
            ImageView image = item.imageView;
            //返回的是该imageView在View中的坐标位置
            left = image.getLeft();
            top = image.getTop();
            right = image.getRight();
            bottom = image.getBottom();
            //event返回的是该点在View的坐标系中的位置

            Log.v(String.format("touchWhichItem  left =%d , top = %d, right = %d, bottom = %d", left, top, right, bottom));

            //所以这个是可以直接进行比较
            if (eventX >= left && eventX <= right && eventY <= bottom && eventY >= top) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private void setAllChildView() {
        childGroup.clear();
        for (int i = 0; i < childCount; i++) {
            childGroup.add(getChildItem(i, i == childCount - 1 ? true : false));
        }
    }

    private PhotoSelectorItem getChildItem(int index, boolean isCanContinue) {
        PhotoSelectorItem item = new PhotoSelectorItem(context);
        ImageView child = new ImageView(context);
        child.setImageResource(isCanContinue ? R.drawable.icon_camera : R.drawable.ic_launcher);
        child.setBackgroundResource(R.drawable.bg);
        item.imageView = child;
        item.isCanContinue = isCanContinue;
        item.position = index;
        return item;
    }
}
