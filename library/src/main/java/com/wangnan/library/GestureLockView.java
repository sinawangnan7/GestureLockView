package com.wangnan.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wangnan.library.listener.OnGestureLockListener;
import com.wangnan.library.model.Point;
import com.wangnan.library.painter.GestureLockPainter;
import com.wangnan.library.painter.Painter;
import com.wangnan.library.util.DimensionUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: GestureLockView
 * @Description: 手势解锁视图
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public class GestureLockView extends View {

    /**
     * 缩放模式(注解)
     */
    @IntDef({NORMAL, REVERSE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScaleMode {
    }

    public static final int NORMAL = 0x0000; // 正常缩放
    public static final int REVERSE = 0x0001; // 反转缩放

    /**
     * 3x3点数组
     */
    private final Point[][] mPoints = new Point[3][3];

    /**
     * 点列表容器（用于记录已被按下的点）
     */
    private final List<Point> mPressPoints = new ArrayList<>(9);

    /**
     * 点动画列表
     */
    private final List<ValueAnimator> mPointAnimators = new ArrayList<>(9);

    /**
     * 视图边长（宽&高的最小值）
     */
    private int viewSize;

    /**
     * 点半径（取值范围[0,viewSize的1/6]，通过{@link GestureLockView#mRadiusRatio}属性进行控制）
     *
     * 注:mRadius代表单位点的可见半径和有效触摸半径，不会随单位点的动画而改变
     */
    private int mRadius;

    /**
     * 点半径比例 (不设置默认0.6F)
     */
    private float mRadiusRatio;

    /**
     * 线的粗细值（不设置则默认为1dp）
     */
    private int mLineThickness;

    /**
     * 是否显示辅助线
     */
    private boolean mIsShowGuides;

    /**
     * 是否使用动画
     */
    private boolean mIsUseAnimation;

    /**
     * 动画持续时长 (不设置默认200毫秒)
     */
    private long mAnimationDuration;

    /**
     * 动画缩放模式
     */
    private int mAnimationScaleMode;

    /**
     * 动画缩放比例（不设置默认1.5F）
     */
    private float mAnimationScaleRate;

    /**
     * 是否使用震动
     */
    private boolean mIsUseVibrate;

    /**
     * 震动持续时间（不设置默认40毫秒）
     */
    private long mVibrateDuration;

    /**
     * 绘制时是否是线在顶部,点在下(控制绘制顺序)
     */
    private boolean mIsLineTop;

    /**
     * 正常 & 按下 & 错误等状态下画笔的颜色值
     */
    private int mNormalColor;
    private int mPressColor;
    private int mErrorColor;

    /**
     * 正常 & 按下 & 错误等状态下的图片资源ID（使用图片代替代码绘制，懒人福利O(∩_∩)O哈哈~）
     */
    private int mNormalImageId;
    private int mPressImageId;
    private int mErrorImageId;

    /**
     * 手势解锁监听器
     */
    private OnGestureLockListener mGestureLockListener;

    /**
     * 绘制者 (默认使用GestureLockPainter)
     */
    private Painter mPainter = new GestureLockPainter();

    /**
     * 震动器
     */
    private Vibrator mVibrator;

    /**
     * 记录当前视图是否处于错误状态
     */
    private boolean isErrorStatus;

    public GestureLockView(Context context) {
        this(context, null);
    }

    public GestureLockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context 上下文环境
     * @param attrs   XML属性信息集
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        // 1.初始化XML属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GestureLockView);
        mRadiusRatio = array.getFloat(R.styleable.GestureLockView_radius_ratio, 0.6F);
        mLineThickness = array.getDimensionPixelSize(R.styleable.GestureLockView_line_thickness, DimensionUtil.dp2px(context, 1));
        mNormalColor = array.getColor(R.styleable.GestureLockView_normal_color, Painter.NORMAL_COLOR);
        mPressColor = array.getColor(R.styleable.GestureLockView_press_color, Painter.PRESS_COLOR);
        mErrorColor = array.getColor(R.styleable.GestureLockView_error_color, Painter.ERROR_COLOR);
        mIsShowGuides = array.getBoolean(R.styleable.GestureLockView_is_show_guides, false);
        mIsLineTop = array.getBoolean(R.styleable.GestureLockView_is_line_top, false);
        mIsUseAnimation = array.getBoolean(R.styleable.GestureLockView_is_use_animation, false);
        mAnimationDuration = array.getInt(R.styleable.GestureLockView_animation_duration, 200);
        mAnimationScaleMode = array.getInt(R.styleable.GestureLockView_animation_scale_mode, NORMAL);
        mAnimationScaleRate = array.getFloat(R.styleable.GestureLockView_animation_scale_rate, 1.5F);
        mIsUseVibrate = array.getBoolean(R.styleable.GestureLockView_is_use_vibrate, false);
        mVibrateDuration = array.getInt(R.styleable.GestureLockView_vibrate_duration, 40);
        mNormalImageId = array.getResourceId(R.styleable.GestureLockView_normal_image, 0);
        mPressImageId = array.getResourceId(R.styleable.GestureLockView_press_image, 0);
        mErrorImageId = array.getResourceId(R.styleable.GestureLockView_error_image, 0);
        array.recycle();
        // 2.修正部分参数（防止参数越界）
        mRadiusRatio = (mRadiusRatio < 0) ? 0 : mRadiusRatio > 1 ? 1 : mRadiusRatio;
        mAnimationScaleRate = mAnimationScaleRate < 0 ? 0 : mAnimationScaleRate;
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
        // 3.初始化绘制者
        initPainter();
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

    /**
     * 初始化绘制者
     */
    private void initPainter() {
        // 使Painter关联当前手势解锁视图
        mPainter.attach(this, getContext(),
                mNormalColor, mPressColor, mErrorColor,
                mNormalImageId, mPressImageId, mErrorImageId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 是否绘制辅助线(帮助开发者确定视图边界)
        if (mIsShowGuides) {
            mPainter.drawGuidesLine(viewSize, canvas);
        }
        if (mIsLineTop) { // 是否是"连线"显示在上层
            // 绘制3x3点数组
            mPainter.drawPoints(mPoints, canvas);
            // 绘制连线
            mPainter.drawLines(mPressPoints, mEventX, mEventY, mLineThickness, canvas);
        } else {
            // 绘制连线
            mPainter.drawLines(mPressPoints, mEventX, mEventY, mLineThickness, canvas);
            // 绘制3x3点数组
            mPainter.drawPoints(mPoints, canvas);
        }
    }

    /**
     * 当前触摸位置在GestureLockView上的坐标（x,y）
     */
    private float mEventX;
    private float mEventY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEventX = event.getX();
        mEventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downEventDeal(mEventX, mEventY);
                break;
            case MotionEvent.ACTION_MOVE:
                moveEventDeal(mEventX, mEventY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                upEventDeal();
                break;
        }
        // 2.重绘
        postInvalidate();
        return true;
    }

    /**
     * ACTION_DOWN事件处理方法
     *
     * @param eventX 事件X坐标（相对于GestureLockView）
     * @param eventY 事件Y坐标（相对于GestureLockView）
     */
    private void downEventDeal(float eventX, float eventY) {
        // 1.回调手势解锁监听器onStarted方法
        if (mGestureLockListener != null) {
            mGestureLockListener.onStarted();
        }
        // 2.清理之前的绘制信息
        clear();
        // 3.修改点状态
        modifyPointStatus(eventX, eventY);
    }

    /**
     * ACTION_MOVE事件处理方法
     *
     * @param eventX 事件X坐标（相对于GestureLockView）
     * @param eventY 事件Y坐标（相对于GestureLockView）
     */
    private void moveEventDeal(float eventX, float eventY) {
        // 1.修改点状态
        modifyPointStatus(eventX, eventY);

    }

    /**
     * ACTION_UP/ACTION_CANCEL事件处理方法
     */
    private void upEventDeal() {
        // 1.回调手势解锁监听器Complete方法
        if (mGestureLockListener != null) {
            mGestureLockListener.onComplete(getPassword());
        }
        // 2.清除触摸点到最后按下单元点的连线
        if (!mPressPoints.isEmpty()) {
            mEventX = mPressPoints.get(mPressPoints.size() - 1).x;
            mEventY = mPressPoints.get(mPressPoints.size() - 1).y;
        }
        // 3.提前结束未执行完的动画
        if (!mPointAnimators.isEmpty()) {
            for (ValueAnimator animator : mPointAnimators) {
                animator.end();
            }
            mPointAnimators.clear();
        }
        // 4.重绘
        postInvalidate();
    }

    /**
     * 根据触摸事件修改点的状态
     */
    private void modifyPointStatus(float x, float y) {
        for (int i = 0; i < 3; i++) { // i为"行标"
            for (int j = 0; j < 3; j++) { // j为"列标"
                Point point = mPoints[i][j];
                float dx = Math.abs(x - point.x);
                float dy = Math.abs(y - point.y);
                if (Math.sqrt(dx * dx + dy * dy) < mRadius) {
                    point.status = Point.POINT_PRESS_STATUS;
                    addPressPoint(point);
                    return;
                }
            }
        }
    }

    /**
     * 添加按下的点
     *
     * @param point 点对象
     */
    private void addPressPoint(Point point) {
        // 1.判断该点是否之前已添加过
        if (mPressPoints.contains(point)) {
            return;
        }
        // 2.如果两点之间还有点没添加,先添加中间点
        if (!mPressPoints.isEmpty()) {
            addMiddlePoint(point);
        }
        // 3.添加按下的点
        mPressPoints.add(point);
        // 4.开启动画
        if (mIsUseAnimation) {
            startAnimation(point, mAnimationDuration);
        }
        // 5.开启震动
        if (mIsUseVibrate) {
            if (mVibrator == null) {
                mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            }
            mVibrator.vibrate(mVibrateDuration);
        }
        // 6.回调手势解锁监听器的onPregress方法
        if (mGestureLockListener != null) {
            mGestureLockListener.onProgress(getPassword());
        }
    }

    /**
     * 添加中间点（判断两点之间是否存在中间点，如果有还没有添加则先行添加进来）
     *
     * @param point 点对象
     */
    private void addMiddlePoint(Point point) {
        Point lastPoint = mPressPoints.get(mPressPoints.size() - 1);
        // 1.判断两个点是否是同一个点
        if (lastPoint == point) {
            return;
        }
        // 2.判断两点之间是否存在中间点
        int middleX = (lastPoint.x + point.x) / 2;
        int middleY = (lastPoint.y + point.y) / 2;
        for (int i = 0; i < 3; i++) { // i为"行标"
            for (int j = 0; j < 3; j++) { // j为"列标"
                Point tempPoint = mPoints[i][j];
                if (tempPoint.x == middleX && tempPoint.y == middleY) {
                    // 3.开启递归调用
                    tempPoint.status = Point.POINT_PRESS_STATUS;
                    addPressPoint(tempPoint);
                    return;
                }
            }
        }
    }

    /**
     * 开启动画
     *
     * @param point    单位点
     * @param duration 持续时长
     */
    private void startAnimation(final Point point, long duration) {
        ValueAnimator valueAnimator;
        // 1.判断是否有按下状态的图片的资源ID，采用不同策略的属性动画
        if (mPressImageId == 0) {
            // 2.判断动画缩放模式，采用不同策略的属性动画
            if (mAnimationScaleMode == 1) {
                valueAnimator = ValueAnimator.ofInt(point.radius, (int) (mAnimationScaleRate * point.radius), point.radius);
            } else {
                valueAnimator = ValueAnimator.ofInt((int) (mAnimationScaleRate * point.radius), point.radius);
            }
        } else {
            valueAnimator = ValueAnimator.ofInt(0, point.radius);
        }
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                point.radius = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
        // 3.添加ValueAnimator至动画列表
        mPointAnimators.add(valueAnimator);
    }

    /**
     * 清理数据至初始状态
     */
    private void clear() {
        for (int i = 0; i < 3; i++) { // i为"行标"
            for (int j = 0; j < 3; j++) { // j为"列标"
                mPoints[i][j].status = Point.POINT_NORMAL_STATUS;
                mPoints[i][j].radius = mRadius;
            }
        }
        mPressPoints.clear();
        mPointAnimators.clear();
        isErrorStatus = false;
    }

    /**
     * 获取手势密码（手势图案以数字密码形式返回）
     */
    private String getPassword() {
        StringBuilder builder = new StringBuilder();
        for (Point pressPoint : mPressPoints) {
            builder.append(pressPoint.index);
        }
        return builder.toString();
    }

    /** ******************************** 对外公开方法（↓）******************************************/

    /**
     * 获取半径值（View执行完onSizeChanged(w, h, oldw, oldh)方法后mRadius才有值）
     */
    public int getRadius() {
        return mRadius;
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
     * 设置线厚度
     *
     * @param px 线厚度（单位:px）
     */
    public void setLineThickness(int px) {
        this.mLineThickness = px;
        postInvalidate();
    }

    /**
     * 设置正常状态的画笔颜色
     *
     * @param normalColor 正常状态的颜色值
     */
    public void setNormalColor(@ColorInt int normalColor) {
        this.mNormalColor = normalColor;
        mPainter.setNormalColor(normalColor);
        postInvalidate();
    }

    /**
     * 设置按下状态的画笔颜色
     *
     * @param pressColor 按下状态的颜色值
     */
    public void setPressColor(@ColorInt int pressColor) {
        this.mPressColor = pressColor;
        mPainter.setPressColor(pressColor);
        postInvalidate();
    }

    /**
     * 设置出错状态的画笔颜色
     *
     * @param errorColor 出错状态的颜色值
     */
    public void setErrorColor(@ColorInt int errorColor) {
        this.mErrorColor = errorColor;
        mPainter.setErrorColor(errorColor);
        postInvalidate();
    }

    /**
     * 设置是否显示辅助线
     *
     * @param isShowGuides 是否显示辅助线（true显示,false隐藏）
     */
    public void setShowGuides(boolean isShowGuides) {
        this.mIsShowGuides = isShowGuides;
        postInvalidate();
    }

    /**
     * 设置绘制时是否线显示在最上层
     *
     * @param isLineTop 线是否显示在顶部（控制绘制顺序:true先绘制点后绘制线，false线绘制线后绘制点）
     */
    public void setLineTop(boolean isLineTop) {
        this.mIsLineTop = isLineTop;
        postInvalidate();
    }

    /**
     * 判断是否使用了动画
     */
    public boolean isUseAnim() {
        return mIsUseAnimation;
    }

    /**
     * 设置是否使用动画
     *
     * @param isUseAnim 是否使用动画（true使用动画，false不使用动画）
     */
    public void setUseAnim(boolean isUseAnim) {
        this.mIsUseAnimation = isUseAnim;
    }

    /**
     * 设置动画时长
     *
     * @param duration 动画持续时长（单位:毫秒）
     */
    public void setAnimationDuration(long duration) {
        this.mAnimationDuration = duration;
    }

    /**
     * 设置动画缩放模式
     *
     * @param scaleMode 缩放模式（可填参数参考{@link GestureLockView#NORMAL}、{@link GestureLockView#REVERSE}）
     */
    public void setAnimationScaleMode(@ScaleMode int scaleMode) {
        this.mAnimationScaleMode = scaleMode;
    }

    /**
     * 设置动画的缩放比例
     *
     * @param scaleRate 动画缩放比例（取值范围[0,正无穷)，建议取值范围[0,1)和(1，2]）
     */
    public void setAnimationScaleRate(float scaleRate) {
        scaleRate = scaleRate < 0 ? 0 : scaleRate;
        this.mAnimationScaleRate = scaleRate;
    }

    /**
     * 设置是否使用震动
     *
     * @param isUseVibrate 是否使用震动（true使用震动，false使用震动）
     */
    public void setUseVibrate(boolean isUseVibrate) {
        this.mIsUseVibrate = isUseVibrate;
    }

    /**
     * 设置震动时长
     *
     * @param duration 震动时长（单位:毫秒, 时间越长震感越强，建议控制在100毫秒之内）
     */
    public void setVibrateDuration(long duration) {
        this.mVibrateDuration = duration;
    }

    /**
     * 设置正常状态的图片资源
     *
     * @param normalImageId 正常状态的图片资源Id
     */
    public void setNormalImageResource(@DrawableRes int normalImageId) {
        this.mNormalImageId = normalImageId;
        if (mRadius != 0) {
            mPainter.setNormalBitmap(getContext(), mRadius, normalImageId);
        }
        postInvalidate();
    }

    /**
     * 设置按下状态的图片资源
     *
     * @param pressImageId 按下状态的图片资源Id
     */
    public void setPressImageResource(@DrawableRes int pressImageId) {
        this.mPressImageId = pressImageId;
        if (mRadius != 0) {
            mPainter.setPressBitmap(getContext(), mRadius, pressImageId);
        }
        postInvalidate();
    }

    /**
     * 设置出错状态的图片资源
     *
     * @param errorImageId 出错状态的图片资源Id
     */
    public void setErrorImageResource(@DrawableRes int errorImageId) {
        this.mErrorImageId = errorImageId;
        if (mRadius != 0) {
            mPainter.setErrorBitmap(getContext(), mRadius, errorImageId);
        }
        postInvalidate();
    }

    /**
     * 设置手势监听器
     *
     * @param listener 手势监听器
     */
    public void setGestureLockListener(OnGestureLockListener listener) {
        this.mGestureLockListener = listener;
    }

    /**
     * 设置绘制者
     *
     * @param painter 绘制者
     */
    public void setPainter(Painter painter) {
        this.mPainter = painter;
        initPainter();
        postInvalidate();
    }

    /**
     * 显示错误状态 (当没有按下的点时，使用该方法无效的)
     */
    public void showErrorStatus() {
        isErrorStatus = true;
        for (Point point : mPressPoints) {
            point.status = Point.POINT_ERROR_STATUS;
        }
        postInvalidate();
    }

    /**
     * 显示错误状态（持续millisecond毫秒后还原至初始状态）
     *
     * @param millisecond 持续时间
     */
    public void showErrorStatus(long millisecond) {
        showErrorStatus();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isErrorStatus) {
                    clearView();
                }
            }
        }, millisecond);
    }

    /**
     * 清理视图至初始状态
     */
    public void clearView() {
        post(new Runnable() {
            @Override
            public void run() {
                clear();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear();
    }
}
