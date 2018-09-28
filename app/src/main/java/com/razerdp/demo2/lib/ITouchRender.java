package com.razerdp.demo2.lib;

import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/9/28.
 */

public interface ITouchRender {


    boolean onTouchEvent(MotionEvent event);

    void forceAbortTouch();
}
