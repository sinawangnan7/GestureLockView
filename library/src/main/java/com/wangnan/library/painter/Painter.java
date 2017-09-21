package com.wangnan.library.painter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.ColorInt;

import com.wangnan.library.GestureLockView;
import com.wangnan.library.model.Point;
import com.wangnan.library.util.BitmapUtil;

import java.util.List;

/**
 * @ClassName: Painter
 * @Description: 绘制者
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public abstract class Painter {

    public static final int NORMAL_COLOR = Color.GRAY; // 正常状态画笔颜色
    public static final int PRESS_COLOR = Color.BLACK; // 按压状态画笔颜色
    public static final int ERROR_COLOR = Color.RED; // 出错状态画笔颜色

    /**
     * 正常 & 按下 & 错误状态画笔
     */
    public final Paint mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public final Paint mPressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public final Paint mErrorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 正常 & 按下 & 错误等状态下的图片（根据图片资源ID获取，进行缩放、圆形剪裁之后的位图）
     */
    private Bitmap mNormalBitmap;
    private Bitmap mPressBitmap;
    private Bitmap mErrorBitmap;

    /**
     * 手势解锁视图
     */
    protected GestureLockView mGestureLockView;

    /**
     * 手势解锁视图点半径值
     */
    private int mRadius;

    /**
     * 关联手势解锁视图
     *
     * @param gestureLockView 手势解锁视图
     * @param context         上下文环境
     * @param radius          半径值
     * @param normalColor     正常状态画笔颜色
     * @param pressColor      按下状态画笔颜色
     * @param errorColor      错误状态画笔颜色
     * @param normalImageId   正常状态图片资源Id
     * @param pressImageId    按下状态图片资源Id
     * @param errorImageId    出错状态图片资源Id
     */
    public void attach(GestureLockView gestureLockView, Context context, int radius,
                       int normalColor, int pressColor, int errorColor,
                       int normalImageId, int pressImageId, int errorImageId) {
        // 1.关联手势解锁视图
        mGestureLockView = gestureLockView;
        // 2.记录原始点半径大小
        mRadius = radius;
        // 3.设置Painter画笔颜色
        setNormalColor(normalColor);
        setPressColor(pressColor);
        setErrorColor(errorColor);
        // 4.设置Painter图片引用
        setNormalBitmap(context, radius, normalImageId);
        setPressBitmap(context, radius, pressImageId);
        setErrorBitmap(context, radius, errorImageId);
    }

    /**
     * 设置正常状态画笔颜色
     *
     * @param normalColor 正常状态画笔颜色 (具体颜色值,不是引用值)
     */
    public void setNormalColor(@ColorInt int normalColor) {
        mNormalPaint.setColor(normalColor);
    }

    /**
     * 设置按下状态画笔颜色
     *
     * @param pressColor 按下状态画笔颜色 (具体颜色值,不是引用值)
     */
    public void setPressColor(@ColorInt int pressColor) {
        mPressPaint.setColor(pressColor);
    }

    /**
     * 设置出错状态画笔颜色
     *
     * @param errorColor 出错状态画笔颜色 (具体颜色值,不是引用值)
     */
    public void setErrorColor(@ColorInt int errorColor) {
        mErrorPaint.setColor(errorColor);
    }


    /**
     * 设置正常状态图片
     */
    public void setNormalBitmap(Context context, int radius, int normalImageId) {
        if (normalImageId != 0) {
            mNormalBitmap = BitmapUtil.createScaledCircleBitmap(context, radius, normalImageId);
        } else {
            mNormalBitmap = null;
        }
    }

    /**
     * 设置按下状态图片
     */
    public void setPressBitmap(Context context, int radius, int pressImageId) {
        if (pressImageId != 0) {
            mPressBitmap = BitmapUtil.createScaledCircleBitmap(context, radius, pressImageId);
        } else {
            mPressBitmap = null;
        }
    }

    /**
     * 设置出错状态图片
     */
    public void setErrorBitmap(Context context, int radius, int errorImageId) {
        if (errorImageId != 0) {
            mErrorBitmap = BitmapUtil.createScaledCircleBitmap(context, radius, errorImageId);
        } else {
            mErrorBitmap = null;
        }
    }

    /** ********************************** 辅助线的绘制方法(↓) **************************************/

    /**
     * 绘制辅助线
     *
     * @param viewSize 视图边长
     * @param canvas   画布
     */
    public final void drawGuidesLine(int viewSize, Canvas canvas) {
        canvas.drawLine(0, 0, viewSize, 0, mNormalPaint);
        canvas.drawLine(0, viewSize / 3, viewSize, viewSize / 3, mNormalPaint);
        canvas.drawLine(0, 2 * viewSize / 3, viewSize, 2 * viewSize / 3, mNormalPaint);
        canvas.drawLine(0, viewSize - 1, viewSize - 1, viewSize - 1, mNormalPaint);
        canvas.drawLine(0, 0, 0, viewSize, mNormalPaint);
        canvas.drawLine(viewSize / 3, 0, viewSize / 3, viewSize, mNormalPaint);
        canvas.drawLine(2 * viewSize / 3, 0, 2 * viewSize / 3, viewSize, mNormalPaint);
        canvas.drawLine(viewSize - 1, 0, viewSize - 1, viewSize - 1, mNormalPaint);
    }

    /** **********************************  点的绘制方法(↓)  ***************************************/

    /**
     * 3x3点绘制方法
     *
     * @param points 3x3点数组
     * @param canvas 画布
     */
    public final void drawPoints(Point[][] points, Canvas canvas) {
        for (int i = 0; i < 3; i++) { // i为"行标"
            for (int j = 0; j < 3; j++) { // j为"列标"
                Point point = points[i][j];
                switch (point.status) {
                    case Point.POINT_NORMAL_STATUS: // 正常状态的点
                        if (mNormalBitmap != null) {
                            drawBitmap(point, point.radius, mNormalBitmap, canvas, mNormalPaint);
                        } else {
                            drawNormalPoint(point, canvas, mNormalPaint);
                        }
                        break;
                    case Point.POINT_PRESS_STATUS: // 按下状态的点
                        if (mPressBitmap != null) {
                            drawBitmap(point, point.radius, mPressBitmap, canvas, mPressPaint);
                        } else {
                            drawPressPoint(point, canvas, mPressPaint);
                        }
                        break;
                    case Point.POINT_ERROR_STATUS: // 出错状态的点
                        if (mErrorBitmap != null) {
                            drawBitmap(point, point.radius, mErrorBitmap, canvas, mErrorPaint);
                        } else {
                            drawErrorPoint(point, canvas, mErrorPaint);
                        }
                        break;
                }
            }
        }
    }

    /**
     * 绘制图片
     *
     * @param point  单位点
     * @param radius 半径
     * @param bitmap 图片
     * @param canvas 画布
     * @param paint  画笔
     */
    private void drawBitmap(Point point, int radius, Bitmap bitmap, Canvas canvas, Paint paint) {
        // 判断mGestureLockView开启了动画
        if (mGestureLockView.isUseAnim()) {
            RectF rectF = new RectF(point.x - mRadius, point.y - mRadius, point.x + mRadius, point.y + mRadius);
            int index = canvas.saveLayer(rectF, paint, Canvas.ALL_SAVE_FLAG);
            canvas.drawCircle(point.x, point.y, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, point.x - mRadius, point.y - mRadius, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(index);
        } else {
            canvas.drawBitmap(bitmap, point.x - radius, point.y - radius, null);
        }
//        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, radius * 2, radius * 2, false), point.x - radius, point.y - radius, null);
    }

    /**
     * 绘制正常状态的点
     *
     * @param point       单位点
     * @param canvas      画布
     * @param normalPaint 正常状态画笔
     */
    public abstract void drawNormalPoint(Point point, Canvas canvas, Paint normalPaint);

    /**
     * 绘制按下状态的点
     *
     * @param point      单位点
     * @param canvas     画布
     * @param pressPaint 按下状态画笔
     */
    public abstract void drawPressPoint(Point point, Canvas canvas, Paint pressPaint);

    /**
     * 绘制出错状态的点
     *
     * @param point      单位点
     * @param canvas     画布
     * @param errorPaint 错误状态画笔
     */
    public abstract void drawErrorPoint(Point point, Canvas canvas, Paint errorPaint);

    /** ********************************** 连线的绘制方法（↓） **************************************/

    /**
     * 绘制连线
     *
     * @param points   点集合（已被按下的点）
     * @param eventX   事件X坐标（当前触摸位置）
     * @param eventY   事件Y坐标（当前触摸位置）
     * @param lineSize 线的粗细值
     * @param canvas   画布
     */
    public void drawLines(List<Point> points, float eventX, float eventY, int lineSize, Canvas canvas) {
        // 1.参数合法性判断
        if (points.size() <= 0) {
            return;
        }
        // 2.根据点列表生成连线路径
        Path path = generateLinePath(points, eventX, eventY);
        // 3.区分点的状态，使用不同画笔绘制连线
        switch (points.get(0).status) {
            case Point.POINT_PRESS_STATUS: // 按下状态
                Paint pressPaint = new Paint(mPressPaint);
                pressPaint.setStyle(Paint.Style.STROKE);
                pressPaint.setStrokeWidth(lineSize);
                canvas.drawPath(path, pressPaint);
                break;
            case Point.POINT_ERROR_STATUS: // 出错状态
                Paint errorPaint = new Paint(mErrorPaint);
                errorPaint.setStyle(Paint.Style.STROKE);
                errorPaint.setStrokeWidth(lineSize);
                canvas.drawPath(path, errorPaint);
                break;
        }
    }

    /**
     * 生成连线路径
     *
     * @param points 点集合（已被按下记录的点）
     * @param eventX 事件X坐标（当前触摸位置）
     * @param eventY 事件Y坐标（当前触摸位置）
     */
    private Path generateLinePath(List<Point> points, float eventX, float eventY) {
        Path path = new Path();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (i == 0) {
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }
        path.lineTo(eventX, eventY);
        return path;
    }
}
