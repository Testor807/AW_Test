package com.example.aw_test;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;

public class FloatService extends Service {
    private Button btnView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private boolean isAdded;
    public static final String OPERATION = "是否需要开启";
    public static final int OPERATION_SHOW = 1;
    public static final int OPERATION_HIDE = 2;
    private static final int HANDLE_CHECK_ACTIVITY = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int operation = intent.getIntExtra(OPERATION, 3);
        if (operation == OPERATION_SHOW) {
            mHandler.sendEmptyMessage(HANDLE_CHECK_ACTIVITY);
        } else if (operation == OPERATION_HIDE) {
            mHandler.removeMessages(HANDLE_CHECK_ACTIVITY);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createWindowView();
    }

    @SuppressLint({"ClickableViewAccessibility", "RtlHardcoded"})
    private void createWindowView() {
        btnView = new Button(this);
        btnView.setBackgroundResource(R.drawable.ic_launcher_foreground);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();

        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.RGBA_8888;
        params.width = 200;
        params.height = 200;
        params.gravity = Gravity.LEFT;
        params.x = 200;
        params.y = 0;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;

        btnView.setOnTouchListener((v, event) -> {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                if (action == MotionEvent.ACTION_DOWN) {
                    params.x = x;
                    params.y = y;
                } else {
                    params.x += x - params.x;
                    params.y += y - params.y;
                }
                windowManager.updateViewLayout(btnView, params);
            }
            return true;
        });

        windowManager.addView(btnView, params);
        isAdded = true;
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLE_CHECK_ACTIVITY) {
                if (!isAdded) {
                    windowManager.addView(btnView, params);
                    isAdded = true;
                    new Thread(() -> {
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sendMessage(Message.obtain(null, 2));
                        }
                    }).start();
                }
                sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 100);
            }
        }
    };

    @Override
    public void onDestroy() {
        if (isAdded) {
            windowManager.removeView(btnView);
        }
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}