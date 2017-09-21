package com.wangnan.library.painter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.wangnan.library.model.Point;

/**
 * @ClassName: System360Painter
 * @Description: (仿)360系统桌面绘制者
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public class System360Painter extends Painter {

    /**
     * 绘制正常状态的点
     *
     * @param point       单位点
     * @param canvas      画布
     * @param normalPaint 正常状态画笔
     */
    @Override
    public void drawNormalPoint(Point point, Canvas canvas, Paint normalPaint) {
        canvas.drawCircle(point.x, point.y, point.radius / 4.0F, normalPaint);
    }

    /**
     * 绘制按下状态的点
     */
    @Override
    public void drawPressPoint(Point point, Canvas canvas, Paint pressPaint) {
        canvas.drawCircle(point.x, point.y, point.radius / 4.0F, pressPaint);
    }

    /**
     * 绘制出错状态的点
     */
    @Override
    public void drawErrorPoint(Point point, Canvas canvas, Paint errorPaint) {
        canvas.drawCircle(point.x, point.y, point.radius / 4.0F, errorPaint);
    }
}
