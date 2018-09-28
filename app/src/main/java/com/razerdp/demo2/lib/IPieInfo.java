package com.razerdp.demo2.lib;

import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;



/**
 * Created by Administrator on 2018/9/28.
 */

public interface IPieInfo {

    double getValue();

    @ColorInt
    int getColor();

    String getDesc();

    @Nullable
    PieOption getPieOpeion();
}
