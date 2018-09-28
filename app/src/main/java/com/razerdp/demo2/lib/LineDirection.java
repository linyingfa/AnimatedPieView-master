package com.razerdp.demo2.lib;

/**
 * Created by Administrator on 2018/9/28.
 */

public enum LineDirection {

    TOP_RIGHT(1, 0),
    BOTTOM_RIGHT(1, 1),
    TOP_LEFT(0, 0),
    BOTTOM_LEFT(0, 1),
    CENTER_RIGHT(1, -1),
    CENTER_LEFT(0, -1);
    int xDirection;//0:left 1:right
    int yDirection;//-1:center 0:top 1:bottom

    LineDirection(int xDirection, int yDirection) {
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }
}
