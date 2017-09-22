package com.wangnan.library.painter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.wangnan.library.model.Point;

/**
 * @ClassName: LUcomPainter
 * @Description: (仿)陆金所绘制者
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public class LUcomPainter extends Painter {

    /**
     * 绘制正常状态的点
     *
     * @param point       单位点
     * @param canvas      画布
     * @param normalPaint 正常状态画笔
     */
    @Override
    public void drawNormalPoint(Point point, Canvas canvas, Paint normalPaint) {
        // 1.绘制实心圆
        canvas.drawCircle(point.x, point.y, point.radius / 3.0F, normalPaint);
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
        int originColor = pressPaint.getColor();
        // 1.绘制白色底圆
        pressPaint.setStyle(Paint.Style.FILL);
        pressPaint.setColor(Color.WHITE);
        canvas.drawCircle(point.x, point.y, point.radius, pressPaint);
        // 2.绘制实心点
        pressPaint.setColor(originColor);
        canvas.drawCircle(point.x, point.y, point.radius / 3.0F, pressPaint);
        // 3.绘制外部边界圆
        pressPaint.setStyle(Paint.Style.STROKE);
        pressPaint.setStrokeWidth(point.radius / 20.0F);
        canvas.drawCircle(point.x, point.y, point.radius, pressPaint);
    }

    /**
     * 绘制按下状态的点
     *
     * @param point      单位点
     * @param canvas     画布
     * @param errorPaint 按下状态画笔
     */
    @Override
    public void drawErrorPoint(Point point, Canvas canvas, Paint errorPaint) {
        // TODO 陆金所没有错误状态的点（如果需要使用绘制错误状态的点，请继承该Painter重写drawErrorPoint方法或重新自定义Painter）
    }
}
