package com.wangnan.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.wangnan.library.model.Point;
import com.wangnan.library.painter.Painter;

/**
 * @ClassName: GestureLockThumbnailView
 * @Description: 手势解锁缩率图View
 * @Author: wangnan7
 * @Date: 2017/11/9
 */

public class GestureLockThumbnailView extends View {

    /**
     * 3x3点数组
     */
    private final Point[][] mPoints = new Point[3][3];

    /**
     * 视图边长（宽&高的最小值）
     */
    private int viewSize;

    /**
     * 点半径（取值范围[0,viewSize的1/6]）
     * <p>
     * 注:mRadius代表单位点的可见半径和有效触摸半径，不会随单位点的动画而改变
     */
    private int mRadius;

    /**
     * 点半径比例 (不设置默认0.6F)
     */
    private float mRadiusRatio = 0.6F;

    /**
     * 画笔
     */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 正常 & 选中状态下画笔的颜色值
     */
    private int mNormalColor = Painter.NORMAL_COLOR;
    private int mSelectorColor = Painter.PRESS_COLOR;

    public GestureLockThumbnailView(Context context) {
        this(context, null);
    }

    public GestureLockThumbnailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockThumbnailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GestureLockThumbnailView);
        mNormalColor = array.getColor(R.styleable.GestureLockThumbnailView_thumbnail_color, Painter.NORMAL_COLOR);
        mRadiusRatio = array.getFloat(R.styleable.GestureLockThumbnailView_thumbnail_ratio, 0.6F);
        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1.边长修正（宽高不一致时，以最小值为准）
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        viewSize = Math.min(width, height);
        // 2.设置宽&高值
        setMeasuredDimension(viewSize, viewSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 1.初始化参数
        initParams();
        // 2.初始化3x3点数组
        initPointArray();
    }

    /**
     * 初始化参数
     */
    private void initParams() {
        // 1.设置点半径 (视图显示半径、有效触摸半径)
        mRadius = (int) (viewSize / 6 * mRadiusRatio);
    }

    /**
     * 初始化3x3点数组
     */
    private void initPointArray() {
        for (int i = 0; i < 3; i++) { // i为"行标"
            for (int j = 0; j < 3; j++) { // j为"列标"
                Point point = new Point();
                point.x = (2 * j + 1) * viewSize / 6;
                point.y = (2 * i + 1) * viewSize / 6;
                point.radius = mRadius;
                point.status = Point.POINT_NORMAL_STATUS;
                point.index = i * 3 + j;
                mPoints[i][j] = point;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制3x3点数组
        for (int i = 0; i < 3; i++) { // i为"行标"
            for (int j = 0; j < 3; j++) { // j为"列标"
                Point point = mPoints[i][j];
                switch (point.status) {
                    case Point.POINT_NORMAL_STATUS:
                        mPaint.setColor(mNormalColor);
                        break;
                    case Point.POINT_PRESS_STATUS:
                        mPaint.setColor(mSelectorColor);
                        break;
                    default:
                        mPaint.setColor(mNormalColor);
                        break;
                }
                canvas.drawCircle(point.x, point.y, point.radius, mPaint);
            }
        }
    }

    /**
     * 设置缩略图
     *
     * @param number 数字序列 （0~9位数字字符串，不满足条件不处理）
     * @param color  颜色值
     */
    public void setThumbnailView(String number, @ColorInt int color) {
        // 0.数字序列合法性判断
        if (TextUtils.isEmpty(number)) {
            return;
        }
        if (number.length() > 9) {
            return;
        }
        if (!number.matches("^\\d+$")) {
            return;
        }
        // 1.还原点状态
        restorePointStatus();
        // 2.解析已按下的点位置
        char[] chars = number.toCharArray();
        int[] numbers = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            numbers[i] = chars[i] - 48;
        }
        // 3.设置被按下的点状态
        for (int num : numbers) {
            Point point = mPoints[num / 3][num % 3];
            point.status = Point.POINT_PRESS_STATUS;
        }
        // 4.设置选中点的颜色值
        mSelectorColor = color;
        // 5.重绘
        postInvalidate();
    }

    /**
     * 还原点状态
     */
    private void restorePointStatus() {
        for (int i = 0; i < 3; i++) { // i为"行标"
            for (int j = 0; j < 3; j++) { // j为"列标"
                mPoints[i][j].status = Point.POINT_NORMAL_STATUS;
            }
        }
    }

    /**
     * 设置半径比例
     *
     * @param radiusRatio 半径比例，取值范围[0,1]
     */
    public void setRadiusRatio(float radiusRatio) {
        mRadiusRatio = (radiusRatio < 0) ? 0 : radiusRatio > 1 ? 1 : radiusRatio;
        onSizeChanged(0, 0, 0, 0);
        postInvalidate();
    }

    /**
     * 设置正常画笔颜色值
     */
    public void setNormalColor(@ColorInt int normalColor) {
        mNormalColor = normalColor;
        postInvalidate();
    }
}
