package com.example.aw_test;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

public class NodeBoundsUtils {

    /**
     * 获取节点在屏幕上的边界矩形
     * @param node 无障碍节点
     * @return Rect对象，包含left, top, right, bottom坐标
     */
    public static Rect getNodeBounds(AccessibilityNodeInfo node) {
        if (node == null) {
            return new Rect();
        }
        Rect bounds = new Rect();
        node.getBoundsInScreen(bounds);
        return bounds;
    }

    /**
     * 获取节点中心点坐标
     * @param node 无障碍节点
     * @return 包含x,y坐标的数组
     */
    public static int[] getNodeCenter(AccessibilityNodeInfo node) {
        Rect bounds = getNodeBounds(node);
        return new int[]{
                bounds.centerX(),
                bounds.centerY()
        };
    }

    /**
     * 检查节点是否在屏幕可见区域内
     * @param node 无障碍节点
     * @return 是否可见
     */
    public static boolean isNodeVisible(AccessibilityNodeInfo node) {
        Rect bounds = getNodeBounds(node);
        return !bounds.isEmpty() && bounds.width() > 0 && bounds.height() > 0;
    }
}
