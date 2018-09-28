package com.razerdp.demo2.lib;

import android.support.annotation.NonNull;

import com.razerdp.widget.animatedpieview.data.IPieInfo;

/**
 * Created by Administrator on 2018/9/28.
 */

public interface OnPieSelectListener<T extends IPieInfo> {
    /**
     * 选中的回调
     *
     * @param pieInfo   数据实体
     * @param isFloatUp 是否浮起
     */
    void onSelectPie(@NonNull T pieInfo, boolean isFloatUp);
}