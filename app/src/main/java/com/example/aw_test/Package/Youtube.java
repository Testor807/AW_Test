package com.example.aw_test.Package;

import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class Youtube {
    protected AccessibilityEvent CurrentEvent;
    public Youtube(){
    }
    protected Youtube(AccessibilityEvent event){
        CurrentEvent = event;
    }
    public void YoutubeListener(String query,AccessibilityNodeInfo rootNode){
        AccessibilityNodeInfo node = rootNode;
        if (node != null) {
            // 查找搜索框并输入关键字
            List<AccessibilityNodeInfo> searchBoxes = node.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/search_box");
            if (!searchBoxes.isEmpty()) {
                AccessibilityNodeInfo searchBox = searchBoxes.get(0);
                searchBox.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, query);
                searchBox.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);

                // 查找并点击搜索按钮
                List<AccessibilityNodeInfo> searchButtons = node.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/search_button");
                if (!searchButtons.isEmpty()) {
                    searchButtons.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        } else {
            //Log.e("AccessibilityService", "Root node is null");
            System.out.println("Root node is null");
        }
    }
}
