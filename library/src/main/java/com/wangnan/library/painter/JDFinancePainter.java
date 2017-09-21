package com.wangnan.library.painter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wangnan.library.model.Point;

/**
 * @ClassName: JDFinancePainter
 * @Description:（仿)京东金融绘制者
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public class JDFinancePainter extends Painter {

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
        // 1.改变透明度绘制外层实心圆
        pressPaint.setAlpha(32);
        canvas.drawCircle(point.x, point.y, point.radius, pressPaint);
        // 2.还原透明度绘制内存实心圆
        pressPaint.setAlpha(255);
        canvas.drawCircle(point.x, point.y, point.radius / 3.0F, pressPaint);
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
        // 1.改变透明度绘制外层实心圆
        errorPaint.setAlpha(32);
        canvas.drawCircle(point.x, point.y, point.radius, errorPaint);
        // 2.还原透明度绘制内存实心圆
        errorPaint.setAlpha(255);
        canvas.drawCircle(point.x, point.y, point.radius / 3.0F, errorPaint);
    }
}
