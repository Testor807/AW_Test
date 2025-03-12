package com.example.aw_test.check;

import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class PublicActivity {

    //判断页面加载完成
    protected boolean checkPageReady(AccessibilityNodeInfo rootNode, String nodeId){
        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId(nodeId);
        // 加载指示器消失，页面可能已加载完成
        return nodes.isEmpty();
    }
}
