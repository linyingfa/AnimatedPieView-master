package com.razerdp.demo2.lib;

import android.content.Context;
import android.view.View;


/**
 * Created by Administrator on 2018/9/28.
 */

public interface IPieView {

    PieManager getManager();

    Context getViewContext();

    AnimatedPieViewConfig getConfig();

    View getPieView();

    void onCallInvalidate();
}
