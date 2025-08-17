package com.example.aw_test;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.aw_test.Package.Youtube;

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
    private WindowManager mWindowManager;
    private View mMarkerView;
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mHandler = new Handler(getMainLooper());
    }

    @SuppressLint("SwitchIntDef")
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                Log.d(TAG,"WINDOW_STATE_CHANGED");
                CheckInfo(event);
                //Youtub();
                action();
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                // 处理同一 Activity 内的动态内容更新
                break;
        }
    }

    public void Youtub()  {
        if(activityName.equals("com.google.android.apps.youtube.app.watchwhile.MainActivity")){
            Log.d(TAG,"Youtube App");
            try{
                TimeUnit.SECONDS.sleep(3);
                Log.d(TAG, "Try to set dispatchGesture");
                //performYouTubeSearchClick(1104,84);
                // 2. 创建手势描述对象 (解决错误的关键)
                GestureDescription.Builder builder = new GestureDescription.Builder();
                Path path = new Path();
                path.moveTo(1006,136);

                // 创建 StrokeDescription (点击动作)
                GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(
                        path,
                        0,   // 开始时间 (立即执行)
                        50   // 持续时间 (毫秒)
                );

                // 3. 构建手势对象 (注意变量名声明)
                builder.addStroke(stroke);
                GestureDescription gesture = builder.build(); // 正确定义变量

                // 4. 执行手势
                dispatchGesture(gesture, new GestureResultCallback() {
                            @Override
                            public void onCompleted(GestureDescription gestureDescription) {
                                Log.d(TAG, "GestureDescription successfully");
                                // 进一步检查目标按钮的状态变化或页面跳转
                            }

                            @Override
                            public void onCancelled(GestureDescription gestureDescription) {
                                Log.e(TAG, "GestureDescription Fail");
                            }
                        },
                        null
                );

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private void action() {
        if(activityName.equals("cn.damai.trade.newtradeorder.ui.projectdetail.ui.activity.ProjectDetailActivity")){
            if (rootNode != null) {
                try{
                    TimeUnit.SECONDS.sleep(2);
                    GestureDescription.Builder builder = new GestureDescription.Builder();
                    Path path = new Path();
                    path.moveTo(723,1996);

                    // 创建 StrokeDescription (点击动作)
                    GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(
                            path,
                            0,   // 开始时间 (立即执行)
                            50   // 持续时间 (毫秒)
                    );

                    // 3. 构建手势对象 (注意变量名声明)
                    builder.addStroke(stroke);
                    GestureDescription gesture = builder.build(); // 正确定义变量

                    // 4. 执行手势
                    dispatchGesture(gesture, new GestureResultCallback() {
                                @Override
                                public void onCompleted(GestureDescription gestureDescription) {
                                    Log.d(TAG, "GestureDescription successfully");
                                    showClickMarker(723,1996);
                                    // 进一步检查目标按钮的状态变化或页面跳转
                                }

                                @Override
                                public void onCancelled(GestureDescription gestureDescription) {
                                    Log.e(TAG, "GestureDescription Fail");
                                }
                            },
                            null
                    );

                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                Log.d(TAG,"rootnode null!");
            }
        }
    }

    private void showClickMarker(int x, int y) {
        // 移除旧标记
        removeMarker();

        View marker = new View(this);
        marker.setBackgroundColor(Color.RED); // 红色标记点
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                10, // 宽度（像素）
                10, // 高度（像素）
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        //params.x = x - 10; // 居中显示
        //params.y = y - 10;
        // 添加覆盖层
        mWindowManager.addView(mMarkerView, params);

        // 3秒后移除标记
        mHandler.postDelayed(this::removeMarker, 3000);
    }

    private Drawable createMarkerDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.RED);
        drawable.setAlpha(180); // 半透明
        drawable.setSize(5, 5);
        drawable.setStroke(5, Color.WHITE);
        return drawable;
    }

    private void removeMarker() {
        if (mMarkerView != null) {
            mWindowManager.removeView(mMarkerView);
            mMarkerView = null;
        }
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
                try {
                    //获取当前窗口activity名
                    ComponentName componentName = new ComponentName(
                            event.getPackageName().toString(),
                            Objects.requireNonNull(event.getClassName()).toString()
                    );
                    //Log.e(TAG, event.getClassName().toString());
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
        Log.d(TAG,"Ready Click");
        Path clickPath = new Path();
        clickPath.moveTo(x, y);

        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(
                clickPath, 0, 5)); // 100ms點擊持續時間

        boolean dispatched = dispatchGesture(builder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "Handle Successful");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "Handle Cancel");
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
