package com.example.aw_test;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.aw_test.Package.Youtube;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";
    //Log.d(TAG, "This is a debug message");
    //Log.i(TAG, "This is an info message");
    //Log.e(TAG, "This is an error message");
    protected Youtube youtube;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 打印事件类型和包名
        //Log.d(TAG, "onAccessibilityEvent: Event Type = " + event.getEventType());
        //Log.d(TAG, "onAccessibilityEvent: Package Name = " + event.getPackageName());

        // 可以根据事件类型处理不同的逻辑
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.d(TAG, "View Clicked");
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.d(TAG, "Window State Changed");
                break;
            // 其他事件类型...
        }
    }
    public void onAccessibilityEvent2(AccessibilityEvent event) {
        // 处理可访问性事件
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.d("AccessibilityService","Page Changed!");
            String packageName = event.getPackageName().toString();
            if (packageName.equals("com.google.android.youtube")) {
                Log.d("AccessibilityService","Youtube Searching");
                performSearch("Appium");
            }
        }
    }

    private void performSearch(String query) {
        System.out.println("Youtube Windows");
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode != null) {
            // 查找搜索框并输入关键字
            List<AccessibilityNodeInfo> searchBoxes = rootNode.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/search_box");
            if (!searchBoxes.isEmpty()) {
                AccessibilityNodeInfo searchBox = searchBoxes.get(0);
                searchBox.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, query);
                searchBox.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);

                // 查找并点击搜索按钮
                List<AccessibilityNodeInfo> searchButtons = rootNode.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/search_button");
                if (!searchButtons.isEmpty()) {
                    searchButtons.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        } else {
            Log.e("AccessibilityService", "Root node is null");
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: Service Interrupted");
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        info.packageNames = new String[]{"com.example.android.youtube"};
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        setServiceInfo(info);
        Log.d(TAG,"Service Started");
        //youtube = new Youtube();
    }

}