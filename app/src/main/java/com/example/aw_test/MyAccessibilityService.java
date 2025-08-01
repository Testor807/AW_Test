package com.example.aw_test;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.graphics.Path;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.aw_test.Package.Youtube;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";
    protected Youtube youtube;
    protected String activityName;
    protected AccessibilityNodeInfo rootNode,curNode;
    // 保存上一次的 rootNode 信息
    private String lastRootNodeHash = "";
    private List<AccessibilityNodeInfo> nodes;
    private List<AccessibilityNodeInfo> cur = null;

    @SuppressLint("SwitchIntDef")
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.d(TAG,"WINDOW_STATE_CHANGED");
                CheckInfo(event);
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                Log.d(TAG,"WINDOW_CONTENT_CHANGED");
                reloadRootNode();
                reload();
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                // 处理同一 Activity 内的动态内容更新
                break;
        }
    }

    private void reloadRootNode() {
        rootNode = getRootInActiveWindow();
    }

    public void CheckInfo(AccessibilityEvent event){
        AccessibilityNodeInfo source = event.getSource();
        if (source != null) {
            CharSequence packageName = source.getPackageName();
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                //获取当前窗口activity名
                ComponentName componentName = new ComponentName(
                        event.getPackageName().toString(),
                        Objects.requireNonNull(event.getClassName()).toString()
                );
                //Log.e(TAG, event.getClassName().toString());
                try {
                    // 直接从event获取Activity名称
                    activityName = Objects.requireNonNull(event.getClassName()).toString();
                    Log.d(TAG, "Current Activity: " + activityName);

                    rootNode = getRootInActiveWindow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            Log.d(TAG,"Null Source");
        }
    }

    @Override
    public void onInterrupt (){

    }

    public void reload(){
        Log.d(TAG,"Running!");
        if(activityName.equals("com.alibaba.pictures.bricks.search.v2.SearchActivity")) {
            nodes = findNodesByClassName(rootNode);
            if(cur != null) {
                if(nodes.equals(cur)) {
                    Log.d(TAG, "Current Nodes is same as the New Nodes!");
                }else{
                    Log.d(TAG, "Current Nodes isn't same as the New Nodes!");
                    Log.d(TAG, "ExistsNodeOrChildren " + nodes.size());

                    int state = 0;
                    for (AccessibilityNodeInfo node : nodes) {
                        CharSequence text2 = node.getText();
                        Log.d(TAG, "No." + state + " Node文本: " + text2);
                        state++;
                    }
                }
            }else {
                Log.d(TAG,"Current Nodes is null");
                cur = nodes;
                Log.d(TAG, "ExistsNodeOrChildren " + nodes.size());

                int state = 0;
                for (AccessibilityNodeInfo node : nodes) {
                    CharSequence text2 = node.getText();
                    Log.d(TAG, "No." + state + " Node文本: " + text2);
                    state++;
                }
            }
        }
    }
    private void performYouTubeSearchClick(float x, float y) {
        Path clickPath = new Path();
        clickPath.moveTo(x, y);

        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(
                clickPath, 0, 5)); // 100ms點擊持續時間

        boolean dispatched = dispatchGesture(builder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.i(TAG, "Handle Successful");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.e(TAG, "Handle Cancel");
            }
        }, null);

        if (!dispatched) {
            Log.e(TAG, "Search Handle is false");
        }
    }


    /**
     * 通过类名查找所有匹配的节点
     * * @param root 根节点
     *
     * @return 匹配的节点列表
     */
    private List<AccessibilityNodeInfo> findNodesByClassName(AccessibilityNodeInfo root) {
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        findNodesByClassNameRecursive(root, "android.widget.TextView", result);
        return result;
    }

    /**
     * 递归查找匹配类名的节点
     */
    private void findNodesByClassNameRecursive(AccessibilityNodeInfo node, String className, List<AccessibilityNodeInfo> result) {
        if (node == null) {
            return;
        }

        // 检查当前节点是否匹配
        if (className.contentEquals(node.getClassName())) {
            // 创建节点副本添加到结果中（因为原始节点会在遍历后被回收）
            AccessibilityNodeInfo copy = AccessibilityNodeInfo.obtain(node);
            result.add(copy);
        }

        // 递归检查子节点
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                findNodesByClassNameRecursive(child, className, result);
                // 回收子节点（因为我们创建了副本）
                child.recycle();
            }
        }
    }
}
