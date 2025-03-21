package com.example.aw_test;

import static com.example.aw_test.KeyboardSimulator.*;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.aw_test.Package.Youtube;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";
    private KeyboardSimulator keyboardSimulator;
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
                AccessibilityNodeInfo source = event.getSource();
                youtube = new Youtube();
                if (source != null) {
                    CharSequence packageName = source.getPackageName();
                    if (event.getEventType() == event.TYPE_WINDOW_STATE_CHANGED) {
                        //获取当前窗口activity名
                        ComponentName componentName = new ComponentName(
                                event.getPackageName().toString(),
                                event.getClassName().toString()
                        );
                        try {
                            String activityName = getPackageManager().getActivityInfo(componentName, 0).toString();
                            activityName = activityName.substring(activityName.indexOf(" "), activityName.indexOf("}"));
                            Log.e(TAG, "=======" + activityName);

                            TimeUnit.SECONDS.sleep(5);
                            if(activityName.equals(" com.google.android.apps.youtube.app.watchwhile.MainActivity")){
                                AccessibilityNodeInfo nodes = getRootInActiveWindow();
                                List<AccessibilityNodeInfo> searchBoxes = nodes.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/menu_item_view");
                                if (searchBoxes  != null) {
                                    Log.d(TAG,"ExistsNodeOrChildren " + searchBoxes.size());
                                    searchBoxes.get(1).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                                    searchBoxes.get(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);

                                    List<AccessibilityNodeInfo> textBox = nodes.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/search_edit_text");
                                    if (textBox  != null) {
                                        textBox.get(0).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                                        Bundle arguments = new Bundle();
                                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "Appium");
                                        textBox.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                                        // 模拟按下 Enter 键
                                        keyboardSimulator = new KeyboardSimulator();
                                        // 模拟按下 Enter 键
                                        keyboardSimulator.sendEnterKey();
                                    }

                                }else{
                                    Log.d(TAG,"The node being not found!");
                                }
                            }else{
                                Log.d(TAG,"Another Activity");
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    break;
                    // 其他事件类型...
                }
        }
    }
    public void getCurrentPage (AccessibilityEvent event){
    }

    @Override
    public void onInterrupt (){

    }

    @Override
    public void onDestroy() {
        if (keyboardSimulator != null) {
            keyboardSimulator.release();
        }
        super.onDestroy();
    }


}