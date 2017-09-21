package com.wangnan.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;

/**
 * @ClassName: BitmapUtil
 * @Description: 图片处理工具类
 * @Author: wangnan7
 * @Date: 2017/9/21
 */

public class BitmapUtil {

    /**
     * 工具类说明(修改工具类时请完善文档注释)：
     *
     * 1.类属性/常量：(暂无)
     *
     * 2.工具方法:
     * {@link BitmapUtil#createScaledCircleBitmap(Context, int, int)} 创建缩放后的圆形图片
     * {@link BitmapUtil#createCircleImage(Bitmap)} 创建圆形图片
     */


    /**
     * 创建缩放后的圆形位图
     *
     * @param context 上下文环境
     * @param radius  点半径
     * @param imageId 图片资源ID
     */
    @Nullable
    public static Bitmap createScaledCircleBitmap(Context context, int radius, int imageId) {
        // 1.获取原始图片
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
        // 2.判断是否能获取到图片
        if (bitmap == null) {
            return null;
        }
        // 2.缩放至指定大小
        bitmap = Bitmap.createScaledBitmap(bitmap, radius * 2, radius * 2, false);
        // 3.创建圆形图片并返回
        return createCircleImage(bitmap);
    }

    /**
     * 创建圆形位图（将图片裁剪为圆形图片并返回，矩形图片居中剪裁）
     *
     * @param source 原图片
     */
    @Nullable
    public static Bitmap createCircleImage(Bitmap source) {
        // 1.参数合法性判断
        if (source == null) {
            return null;
        }
        // 2.创建画笔
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 3.取宽、高的最小值创建目标Bitmap（最终要返回的Bitmap）
        int size = Math.min(source.getWidth(), source.getHeight());
        Bitmap target = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        // 4.创建画布;绘制圆形图案;设置画笔的Xfermode;绘制"源Bitmap"；返回target
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, -(source.getWidth() - size) / 2.0F, -(source.getHeight() - size) / 2.0F, paint);
        return target;
    }
}
