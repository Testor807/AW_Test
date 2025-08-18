package com.example.aw_test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public class CircleOverlay {
    private WindowManager windowManager;
    private View overlayView;
    private Context context;

    public CircleOverlay(Context context) {
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }


    public void showCircle(int x, int y) {
        // 创建覆盖层视图
        overlayView = new View(context) {
            @Override
            @SuppressLint("DrawAllocation")
            protected void onDraw(@NonNull Canvas canvas) {
                super.onDraw(canvas);
                Paint paint = new Paint();
                paint.setColor(Color.BLUE); // 设置圆形颜色
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(true);

                // 绘制半径为5的圆形
                canvas.drawCircle(5, 5, 5, paint);
                Log.d("MyAccessibilityService","Drawed!");
            }
        };

        // 设置视图布局参数
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                10, // 宽度 (直径)
                10, // 高度 (直径)
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // 设置圆形位置
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = x - 5; // 调整位置使圆心在指定坐标
        params.y = y - 5;

        // 添加覆盖层
        windowManager.addView(overlayView, params);
    }

    public void hideCircle() {
        if (overlayView != null) {
            windowManager.removeView(overlayView);
            overlayView = null;
        }
    }
}
