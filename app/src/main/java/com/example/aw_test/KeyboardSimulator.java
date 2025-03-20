package com.example.aw_test;

import android.app.Instrumentation;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.KeyEvent;

public class KeyboardSimulator {

    private HandlerThread handlerThread;
    private Handler backgroundHandler;

    public KeyboardSimulator() {
        // 创建 HandlerThread
        handlerThread = new HandlerThread("KeyboardSimulatorThread");
        handlerThread.start();

        // 获取 HandlerThread 的 Looper 并创建 Handler
        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    // 在后台线程中发送按键事件
    public void sendKeyEvent(final int keyCode) {
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                Instrumentation instrumentation = new Instrumentation();
                instrumentation.sendKeyDownUpSync(keyCode);
            }
        });
    }

    // 模拟按下 Enter 键
    public void sendEnterKey() {
        sendKeyEvent(KeyEvent.KEYCODE_ENTER);
    }

    // 释放资源
    public void release() {
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }
}