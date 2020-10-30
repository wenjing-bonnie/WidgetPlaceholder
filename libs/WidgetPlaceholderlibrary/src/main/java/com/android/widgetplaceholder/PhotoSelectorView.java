package com.android.widgetplaceholder;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenjing.liu on 2020/10/15 in J1.
 * 展示选中的图片的9宫格
 *
 * @author wenjing.liu
 */
public class PhotoSelectorView extends View {
    private final String TAG = "PhotoSelectorView";
    private boolean DEBUG = true;
    private Context context;

    private OnItemClickListener onItemClickListener;

    private List<Uri> selectorPathList;
    private List<PhotoSelectorItem> childViewList;
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


    public PhotoSelectorView(Context context) {
        this(context, null);

    }

    /**
     * 如果从布局文件中加载，必须复写该方法
     *
     * @param context
     * @param attrs
     */
    public PhotoSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        selectorPathList = new ArrayList<>();
        childViewList = new ArrayList<>();
        initAddPhotoLayout();
        initAttrs(attrs);
    }

    private void initAddPhotoLayout() {
        defaultItemWidth = 60;
        //默认为初始化的一个添加图片的按钮
        addDefaultImageView(0);
    }

    /**
     * 默认为初始化的一个添加图片的按钮
     * attrs.xml
     *
     * @param position 该图片在集合中的位置
     */
    private void addDefaultImageView(int position) {
        Uri defaultUri = Uri.fromFile(new File(String.valueOf(R.drawable.icon_camera)));
        selectorPathList.add(defaultUri);
        childViewList.add(getChildView(defaultUri, position, true));
    }

    private void initAttrs(AttributeSet attrs) {
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
    }

    /**
     * 设置ImageView的监听事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    /**
     * 加号始终在最后一个，所以始终处理的是集合最后一个元素
     *
     * @param uri
     */
    public void notifyDataSetChanged(Uri uri) {
        //mDataSetObservable.notifyChanged();
        if (selectorPathList.size() == 0) {
            return;
        }
        //直接将该元素之前添加该路径
        int size = selectorPathList.size();
        List<Uri> paths = new ArrayList<>();
        paths.addAll(selectorPathList);
        //将默认的添加按钮给移除，然后把给uri累加到后面, updateAllAddChildView();后默认的累加默认值
        paths.remove(size - 1);
        paths.add(uri);
        if (DEBUG) {
            Log.e(TAG, "uri = " + uri.getPath());
        }
        selectorPathList.clear();
        selectorPathList.addAll(paths);
        //更新UI
        updateAllAddChildView();
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
     * 测量整个控件高度，默认的就是每个Column的width，设置其他值不起作用。
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        //该控件设置height，也不起作用，默认采用的是每个Column的width
        return mColumnWidth * mNumRow + mVerticalSpacing * (mNumRow - 1) + paddingTop + paddingBottom;
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
     * 根据计算的控件的宽度，重新调整item的宽度
     */
    private void updateColumnWidth() {
        //getMeasuredWidth()获取的是父控件的除去margin的宽度
        mColumnWidth = (getMeasuredWidth() - mHorizontalSpacing * (mNumColumns - 1)
                - paddingLeft - paddingRight) / mNumColumns;
    }

    /**
     * 对应的是相对于整个屏幕的绝对位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left, top, right, bottom = 0;
        if (DEBUG) {
            Log.v(TAG, String.format(" onLayout 初始化的位置 left = %d , top = %d , right = %d, bottom = %d ", l, t, r, b));
        }
        int firstItemPaddingLeft, firstItemPaddingTop = 0;
        //每行
        for (int row = 0; row < mNumRow; row++) {
            //每列
            //第一行要加paddingTop
            firstItemPaddingTop = row == 0 ? paddingTop : 0;
            for (int col = 0; col < mNumColumns; col++) {

                int index = row * mNumColumns + col;
                if (index >= childViewList.size()) {
                    break;
                }
                //第一列要加paddingLeft
                firstItemPaddingLeft = col == 0 ? paddingLeft : 0;
                PhotoSelectorItem item = childViewList.get(index);
                ImageView child = item.imageView;
                item.updateImageView();
                //更新位置
                left = l + firstItemPaddingLeft + mColumnWidth * col + mHorizontalSpacing * col;
                top = t + firstItemPaddingTop + mColumnWidth * row + mVerticalSpacing * row;
                right = left + mColumnWidth;
                bottom = top + mColumnWidth;

                if (DEBUG) {
                    Log.d(TAG, String.format(" onLayout 第%d行第%d列 left = %d , top = %d , right = %d, bottom = %d ", row, col, left, top, right, bottom));
                }
                child.layout(left, top, right, bottom);
            }
        }
    }

    /**
     * 将View绘制到画布上
     * <p>
     * <p>
     * dispatchDraw(Canvas canvas)：ViewGroup及其派生类具有的方法，主要用于控制子View的绘制分发。
     * 自定义控件时，重载该方法可以改变子View的绘制，进而实现一些复杂的视效。
     * .drawChild(Canvas canvas, View child, long drawingTime)：ViewGroup及其派生类具有的方法，用于直接绘制具体的子View。
     * 自定义控件时，重载该方法可以直接绘制具体的子View。
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (DEBUG) {
            Log.v(TAG, String.format(" onDraw child size  = %d ", childViewList.size()));
        }
        if (childViewList.size() == 0) {
            return;
        }
        //继承于View，要通过移动坐标系，将每个ImageView给画出来

        //每行
        int transX = 0;
        int transY = 0;
        for (int row = 0; row < mNumRow; row++) {
            if (row != 0) {
                //将画布移动到最左边,并且往下移动一行,因为坐标系已经移动到第(mNumColumns-1)列了
                transX = -(mColumnWidth + mHorizontalSpacing) * (mNumColumns - 1) - paddingLeft;
                transY = mColumnWidth + mVerticalSpacing;
                if (DEBUG) {
                    Log.v(TAG, String.format(" onDraw 第%d行%d列 transX = %d , transY = %d ", row, 0, transX, transY));
                }
                canvas.translate(transX, transY);
            }
            //每列
            for (int col = 0; col < mNumColumns; col++) {

                int index = row * mNumColumns + col;
                if (index >= childViewList.size()) {
                    break;
                }
                PhotoSelectorItem item = childViewList.get(index);
                ImageView child = item.imageView;
                //要将画布移动下，才可以画另外一个,移动的是坐标，画布的位置并没有变
                //位移都是基于当前位置移动，而不是(0,0)
                transX = col > 0 ? (mColumnWidth + mHorizontalSpacing) : paddingLeft;
                transY = (col == 0 && row == 0) ? paddingTop : 0;
                canvas.translate(transX, transY);

                if (DEBUG) {
                    Log.v(TAG, String.format(" onDraw 第%d行%d列 transX = %d , transY = %d ", row, 0, transX, transY));
                }
                child.draw(canvas);

                //保存Canvas状态
                canvas.save();
            }

        }
        // 返回最新的save状态
        canvas.restore();
    }


    /**
     * 获取所有新增的View的个数，会自动在selectorPathList中添加累加默认值
     */
    private void updateAllAddChildView() {
        List<Uri> paths = selectorPathList;
        childViewList.clear();
        int count = paths.size();
        if (count == 0) {
            addDefaultImageView(0);
            return;
        }
        int size;
        //如果设置了最大数量，则只显示最大数量的View
        if (count > maxNumber && maxNumber > 0) {
            size = maxNumber;
            Log.w(TAG, String.format("You set max number is %d , but set Adapter size is %d, only can display %d",
                    maxNumber, count, maxNumber));
        } else {
            size = count;
        }
        for (int i = 0; i < size; i++) {
            childViewList.add(getChildView(paths.get(i), size, false));
        }
        //如果没有超出最大值，则在后面累加可以添加图片的button
        if ((maxNumber > 0 && size < maxNumber) || maxNumber <= 0) {
            addDefaultImageView(size);
        }
        //更新行数
        mNumRow = childViewList.size() / mNumColumns + 1;
        // 更新UI
        requestLayout();
        if (DEBUG) {
            Log.v(TAG, String.format("selector path size  = %d , child size = %d", selectorPathList.size(), childViewList.size()));
        }
    }

    private PhotoSelectorItem getChildView(Uri uri, int position, final boolean isCanContinue) {
        final PhotoSelectorItem item = new PhotoSelectorItem(context);
        ImageView imageView = new ImageView(context);
        item.imageView = imageView;
        item.position = position;
        item.isCanContinue = isCanContinue;
        item.uri = uri;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mColumnWidth, mColumnWidth);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        // imageView.setCornerType();
        //如果可以添加的，则为默认的添加的icon
        if (isCanContinue) {
            imageView.setImageResource(R.drawable.icon_camera);
        } else {
            item.updateImageView();
        }

        return item;
    }


    /**
     * 获取每个Column的宽度
     *
     * @return
     */
    public int getColumnWidth() {
        return mColumnWidth;
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

        //如果该ViewGroup的有父控件，则取父控件的宽度
        View parent = (View) getParent();
        if (parent != null) {
            width = parent.getMeasuredWidth();
            if (DEBUG) {
                Log.e(TAG, mVerticalSpacing + "px parent width  = " + width);
            }
            if (width > 0) {
                return width;
            }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onItemClickListener == null) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_DOWN:

                int index = touchWhichItem(event.getX(), event.getY());
                if (DEBUG) {
                    Log.v(TAG, String.format("x = %f , y = %f, 点击的为第 %d 个 ", event.getX(), event.getY(), index));
                }
                if (index == -1) {
                    return super.onTouchEvent(event);
                }
                onItemClickListener.onItemClickListener(childViewList.get(index));
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 传入的x和y是相对于该view的原点的x和y的坐标
     *
     * @param x
     * @param y
     * @return
     */
    private int touchWhichItem(float x, float y) {

        for (int i = 0; i < childViewList.size(); i++) {
            ImageView image = childViewList.get(i).imageView;
            int left = image.getLeft();
            int right = image.getRight();
            int top = image.getTop();
            int bottom = image.getBottom();

            int relativeTop = top - this.getTop();
            int relativeBottom = bottom - this.getTop();

            if (DEBUG) {
                Log.d(TAG, String.format(" touchWhichItem 第%d个 left = %d , top = %d , right = %d, bottom = %d , bottomRel = %d , topRel = %d", i, left, top, right, bottom, relativeBottom, relativeTop));
            }
            //传入的x和y是相对屏幕的绝对坐标值
            //left/right/top/bottom相对于父控件的坐标
            if (x <= right && x >= left && y <= relativeBottom && y >= relativeTop) {
                return i;
            }
        }
        return -1;
    }

    public interface OnItemClickListener {
        void onItemClickListener(PhotoSelectorItem item);
    }

}
