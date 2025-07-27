package com.example.aw_test;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.aw_test.Package.Youtube;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";
    private static int state = 0;
    //Log.d(TAG, "This is a debug message");
    //Log.i(TAG, "This is an info message");
    //Log.e(TAG, "This is an error message");
    protected Youtube youtube;
    protected String activityName;
    protected AccessibilityNodeInfo rootNode;

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
                AccessibilityNodeInfo source = event.getSource();
                if (source != null) {
                    CharSequence packageName = source.getPackageName();
                    if (event.getEventType() == event.TYPE_WINDOW_STATE_CHANGED) {
                        //获取当前窗口activity名
                        ComponentName componentName = new ComponentName(
                                event.getPackageName().toString(),
                                event.getClassName().toString()
                        );
                        //Log.e(TAG, event.getClassName().toString());
                        try {
                            // 直接从event获取Activity名称
                            String activityName = Objects.requireNonNull(event.getClassName()).toString();

                            Log.d(TAG, "Current Activity: " + activityName);

                            TimeUnit.SECONDS.sleep(5);
                            if (activityName.equals(" com.google.android.apps.youtube.app.watchwhile.MainActivity")) {
                                Log.e(TAG, "Arrived");
                                //performYouTubeSearchClick(1104,84);
                                state+=1;

                                rootNode = getRootInActiveWindow();
                                List<AccessibilityNodeInfo> searchBoxes = rootNode.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/menu_item_view");
                                if (searchBoxes != null) {
                                    Log.d(TAG, "ExistsNodeOrChildren " + searchBoxes.size());
                                    /**
                                    searchBoxes.get(1).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                                    searchBoxes.get(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);

                                    List<AccessibilityNodeInfo> textBox = nodes.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/search_edit_text");
                                    if (textBox != null) {
                                        textBox.get(0).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                                        Bundle arguments = new Bundle();
                                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "Appium");
                                        textBox.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                                    }
                                     **/

                                } else {
                                    Log.d(TAG, "The node being not found!");
                                }

                            }else if(activityName.equals("cn.damai.homepage.MainActivity")){
                                rootNode = getRootInActiveWindow();
                                // 查找所有匹配类名的节点
                                List<AccessibilityNodeInfo> nodes = findNodesByClassName(rootNode, "android.view.ViewGroup");
                                state = 0;
                                // 处理找到的节点
                                for (AccessibilityNodeInfo node : nodes) {
                                    // 执行操作，例如点击
                                    if (node.isClickable()) {
                                        state+=1;
                                    }

                                    /* 获取其他信息
                                    *CharSequence text = node.getText();
                                    *CharSequence contentDescription = node.getContentDescription();*/
                                    Rect bounds = new Rect();
                                    node.getBoundsInScreen(bounds);

                                    // 计算中心点坐标
                                    int centerX = bounds.centerX();
                                    int centerY = bounds.centerY();

                                    // 获取节点文本
                                    /*CharSequence text = node.getText();

                                    *Log.d(TAG, "按钮文本: " + text +
                                    *       ", 位置: (" + bounds.left + "," + bounds.top + ")-(" +
                                    *        bounds.right + "," + bounds.bottom + ")" +
                                    *        ", 中心点: (" + centerX + "," + centerY + ")");
                                    *Log.d(TAG, "Found clickable nodes: " +state);*/

                                    // 记得回收节点
                                    //node.recycle();
                                }
                                nodes.get(0).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                                nodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);

                                // 回收根节点
                                rootNode.recycle();


                            }else if(activityName.equals("com.alibaba.pictures.bricks.search.v2.SearchActivity")) {
                                Log.d(TAG,"In SearchActivity");
                                rootNode = getRootInActiveWindow();
                                List<AccessibilityNodeInfo> textInfo = rootNode.findAccessibilityNodeInfosByViewId("cn.damai:id/header_search_v2_input");
                                if (textInfo != null) {
                                    Bundle arguments = new Bundle();
                                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "李宗盛");
                                    textInfo.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);

                                    TimeUnit.SECONDS.sleep(3);
                                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                        AccessibilityNodeInfo freshRoot = getRootInActiveWindow();
                                        if (freshRoot != null) {
                                            state = 0;
                                            List<AccessibilityNodeInfo> list = findNodesByClassName(freshRoot, "android.widget.RelativeLayout");
                                            if(list != null) {
                                                Log.d(TAG, "ExistsNodeOrChildren " + list.size());
                                                for (AccessibilityNodeInfo node : list){
                                                    CharSequence text = node.getText();
                                                    if (node.isClickable()) {
                                                        Log.d(TAG, state+" 按钮文本: " + text);
                                                        state+=1;
                                                    }else{
                                                        Log.d(TAG, state+" 不是按钮文本: " + text);
                                                    }
                                                }
                                                //list.get(1).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                                                //list.get(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }else {
                                                throw new RuntimeException("未找到目标元素");
                                            }
                                        }
                                    }, 500); // 延迟 500ms 等待界面刷新完成
                                }else {
                                    throw new RuntimeException("未找到目标元素");
                                }
                            }else{
                                Log.d(TAG,"Another Activity");
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    break;
                    // 其他事件类型...
                }
        }
    }

    @Override
    public void onInterrupt (){

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
     * @param className 要查找的类名
     * @return 匹配的节点列表
     */
    private List<AccessibilityNodeInfo> findNodesByClassName(AccessibilityNodeInfo root, String className) {
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        findNodesByClassNameRecursive(root, className, result);
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
        if (className.equals(node.getClassName())) {
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
