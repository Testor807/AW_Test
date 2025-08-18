package com.example.aw_test;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.aw_test.Package.Youtube;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";
    protected Youtube youtube;
    protected String activityName = "";
    protected AccessibilityNodeInfo rootNode,curNode;
    // 保存上一次的 rootNode 信息
    private List<AccessibilityNodeInfo> nodes, itemTexts;
    private List<AccessibilityNodeInfo> cur = null;
    private CharSequence text2 = null;
    private CircleOverlay circleOverlay;

    @SuppressLint("SwitchIntDef")
    public void onAccessibilityEvent(AccessibilityEvent event) {
        circleOverlay = new CircleOverlay(this);
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.d(TAG,"WINDOW_STATE_CHANGED");
                CheckInfo(event);
                //reload();
                action();
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                // 处理同一 Activity 内的动态内容更新
                break;
        }
    }
    private void action(){
        if(activityName.equals("cn.damai.trade.newtradeorder.ui.projectdetail.ui.activity.ProjectDetailActivity")){
            if (rootNode != null) {
                // 查找所有FrameLayout
                try{
                    TimeUnit.SECONDS.sleep(3);
                    List<AccessibilityNodeInfo> text = rootNode.findAccessibilityNodeInfosByViewId("cn.damai:id/project_detail_perform_time_tv");
                    if(text != null){
                        Log.d(TAG,"Text　Node :"+text.size());
                        nodes = findNodesByClassName(rootNode,"android.widget.LinearLayout");
                        Log.d(TAG,"Frame Size:"+nodes.size());
                    }else{
                        Log.d(TAG,"Target Node Null");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                    /* 假设要检查的坐标 (x, y)
                    AccessibilityNodeInfo node = findNodeAt(rootNode, 315,1920 );
                    if (node != null && node.isEnabled() && ( node.isLongClickable() || node.isFocusable())) {
                        // 该位置可点击
                        Log.d(TAG,"The node is clickable!");
                        //performYouTubeSearchClick(315,1920);
                        //performClickAtPosition2(318,1925);
                    }else{
                        Log.d(TAG,"The node isn't clickable!");
                    }*/

                    /*
                    for (AccessibilityNodeInfo node : clickableNodes) {
                        // 获取边界值
                        Rect bounds = NodeBoundsUtils.getNodeBounds(node);

                        // 输出边界信息
                        Log.d(TAG, String.format(
                                num+" Button [%s] bounds: left=%d, top=%d, right=%d, bottom=%d, width=%d, height=%d",
                                node.getText(),
                                bounds.left,
                                bounds.top,
                                bounds.right,
                                bounds.bottom,
                                bounds.width(),
                                bounds.height()
                        ));
                        num++;

                        // 获取中心点
                        int[] center = NodeBoundsUtils.getNodeCenter(node);
                        Log.d(TAG, String.format("Center: x=%d, y=%d", center[0], center[1]));
                    }*/
                rootNode.recycle();
            }else{
                Log.d(TAG,"rootnode null!");
            }
        }
    }

    private void performClickAtPosition2(int x, int y) {
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(x, y);

        // 100ms的点击手势
        builder.addStroke(new GestureDescription.StrokeDescription(
                path, 0, 10));

        dispatchGesture(builder.build(), null, null);
        Log.d(TAG,"The node is clicked");
    }

    // 递归查找指定坐标的节点
    private AccessibilityNodeInfo findNodeAt(AccessibilityNodeInfo root, int x, int y) {
        if (root == null) return null;

        Rect bounds = new Rect();
        root.getBoundsInScreen(bounds);

        if (!bounds.contains(x, y)) {
            return null;
        }

        for (int i = 0; i < root.getChildCount(); i++) {
            AccessibilityNodeInfo child = root.getChild(i);
            AccessibilityNodeInfo found = findNodeAt(child, x, y);
            if (found != null) {
                return found;
            }
        }

        return root;
    }

    private List<AccessibilityNodeInfo> findClickableNodes(AccessibilityNodeInfo node) {
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        if (node == null) {
            return result;
        }

        // 检查当前节点是否可点击
        if (node.isClickable()) {
            result.add(node);
            // 注意：这里不返回，因为可能有子节点也是可点击的
        }

        // 递归检查子节点
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                result.addAll(findClickableNodes(child));
                child.recycle(); // 回收子节点
            }
        }

        return result;
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
        circleOverlay.hideCircle();
    }

    public void reload(){
        Log.d(TAG,"Running!");
        if(activityName.equals("com.alibaba.pictures.bricks.search.v2.SearchActivity")){
            nodes = findNodesByClassName(rootNode,"android.widget.TextView");
            if(cur != null) {
                if(nodes.equals(cur)) {
                    Log.d(TAG, "Current Nodes is same as the New Nodes!");
                }else{
                    Log.d(TAG, "Current Nodes isn't same as the New Nodes!");

                    cur = nodes;
                    Log.d(TAG, "ExistsNodeOrChildren " + nodes.size());
                    int num = 0;
                    int state = 0;
                    for (AccessibilityNodeInfo node : nodes) {
                        text2 = node.getText();
                        if (Objects.equals(text2, "临沂")){
                            Log.d(TAG, "No." + state + " Node文本是临沂");
                            break;
                        }else{
                            Log.d(TAG, "No." + state + " Node文本: " + text2);
                        }
                        state++;
                    }
                }
            }else {
                Log.d(TAG,"Current Nodes is null");
                cur = nodes;
                Log.d(TAG, "ExistsNodeOrChildren " + nodes.size());
                int num = 0;
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
                clickPath, 0, 100)); // 100ms點擊持續時間

        boolean dispatched = dispatchGesture(builder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.i(TAG, "Handle Successful");
                circleOverlay.hideCircle(); // 先移除之前的圆形
                circleOverlay.showCircle(730, 1946); // 在新位置绘制圆形
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
    private List<AccessibilityNodeInfo> findNodesByClassName(AccessibilityNodeInfo root,String className) {
        List<AccessibilityNodeInfo> result = new ArrayList<>();
        findNodesByClassNameRecursive(root,className, result);
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
