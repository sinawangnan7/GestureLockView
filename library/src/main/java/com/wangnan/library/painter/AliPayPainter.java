package com.wangnan.library.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.wangnan.library.model.Point;

import java.util.List;

/**
 * @ClassName: AliPayPainter
 * @Description: (仿)支付宝绘制者
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public class AliPayPainter extends Painter {

    /**
     * 绘制正常状态的点
     *
     * @param point       单位点
     * @param canvas      画布
     * @param normalPaint 正常状态画笔
     */
    @Override
    public void drawNormalPoint(Point point, Canvas canvas, Paint normalPaint) {
        // 1.绘制圆形轮廓边界
        normalPaint.setStyle(Paint.Style.STROKE);
        normalPaint.setStrokeWidth(point.radius / 30.0F);
        canvas.drawCircle(point.x, point.y, point.radius, normalPaint);
    }

    /**
     * 绘制按下状态的点
     *
     * @param point      单位点
     * @param canvas     画布
     * @param pressPaint 按下状态画笔
     */
    @Override
    public void drawPressPoint(Point point, Canvas canvas, Paint pressPaint) {
        // 1.绘制实心点
        pressPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(point.x, point.y, point.radius / 3.0F, pressPaint);
        // 2.绘制圆形轮廓边界
        pressPaint.setStyle(Paint.Style.STROKE);
        pressPaint.setStrokeWidth(point.radius / 20.0F);
        canvas.drawCircle(point.x, point.y, point.radius, pressPaint);
    }

    /**
     * 绘制出错状态的点
     *
     * @param point      单位点
     * @param canvas     画布
     * @param errorPaint 错误状态画笔
     */
    @Override
    public void drawErrorPoint(Point point, Canvas canvas, Paint errorPaint) {
        // 1.绘制实心点
        errorPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(point.x, point.y, point.radius / 3.0F, errorPaint);
        // 2.绘制圆形轮廓边界
        errorPaint.setStyle(Paint.Style.STROKE);
        errorPaint.setStrokeWidth(point.radius / 20.0F);
        canvas.drawCircle(point.x, point.y, point.radius, errorPaint);
    }

    /**
     * 绘制连线
     *
     * @param points   点集合（已被按下记录的点）
     * @param eventX   事件X坐标（当前触摸位置）
     * @param eventY   事件Y坐标（当前触摸位置）
     * @param lineSize 线的粗细值
     * @param canvas   画布
     */
    @Override
    public void drawLines(List<Point> points, float eventX, float eventY, int lineSize, Canvas canvas) {
        super.drawLines(points, eventX, eventY, lineSize, canvas);
        // 绘制三角箭头（又重新复习了一遍三角函数...╮(╯▽╰)╭）
        // 1.三角函数运算,确定3个顶点坐标
        int radius = getGestureLockView().getRadius();
        for (int i = 0; i < points.size() - 1; i++) {
            Point prePoint = points.get(i);
            Point nextPoint = points.get(i + 1);
            int dx = nextPoint.x - prePoint.x;
            int dy = nextPoint.y - prePoint.y;
            int x1 = prePoint.x + (int) (2 * radius / 3 * dx / Math.sqrt(dx * dx + dy * dy));
            int y1 = prePoint.y + (int) (2 * radius / 3 * dy / Math.sqrt(dx * dx + dy * dy));
            int x2 = prePoint.x + (int) (radius / 2 * dx / Math.sqrt(dx * dx + dy * dy));
            int y2 = prePoint.y + (int) (radius / 2 * dy / Math.sqrt(dx * dx + dy * dy));
            int border = (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
            int distanceY = (int) (border * dx / Math.sqrt(dx * dx + dy * dy));
            int distanceX = (int) (border * dy / Math.sqrt(dx * dx + dy * dy));
            // 2.记录三角形3个顶点坐标(第3个顶点是(x1,y1))
            int top1_x = x2 + distanceX;
            int top1_y = y2 - distanceY;
            int top2_x = x2 - distanceX;
            int top2_y = y2 + distanceY;
            // 3.生成三角形路径
            Path path = new Path();
            path.moveTo(top1_x, top1_y);
            path.lineTo(top2_x, top2_y);
            path.lineTo(x1, y1);
            path.close();
            // 4.区分点状态绘制路径
            if (prePoint.status == Point.POINT_PRESS_STATUS) { // 按下状态
                Paint.Style style = mPressPaint.getStyle();
                mPressPaint.setStyle(Paint.Style.FILL);
                canvas.drawPath(path, mPressPaint);
                mPressPaint.setStyle(style);
            } else if (prePoint.status == Point.POINT_ERROR_STATUS) { // 出错状态
                Paint.Style style = mErrorPaint.getStyle();
                mErrorPaint.setStyle(Paint.Style.FILL);
                canvas.drawPath(path, mErrorPaint);
                mErrorPaint.setStyle(style);
            }
        }
    }
}
