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
    protected Youtube youtube;
    @Override
    public void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        info.packageNames = new String[]{"com.example.android.youtube"};
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        setServiceInfo(info);
        Log.d("AccessibilityService", "Service Started");
        youtube = new Youtube();
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 处理可访问性事件
        String packageName = event.getPackageName().toString();
        System.out.println("Checking");
        if (packageName.equals("com.google.android.youtube")) {
            //AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            //youtube.YoutubeListener("Appium",rootNode);
            System.out.println("Youtube Starting");
            performSearch("Appium");
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

    }

}