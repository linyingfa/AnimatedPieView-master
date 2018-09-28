package com.razerdp.demo2.lib;

import android.view.animation.Animation;
import android.view.animation.Transformation;


import com.razerdp.widget.animatedpieview.utils.PLog;
import com.razerdp.widget.animatedpieview.utils.Util;

import java.util.List;


/**
 * Created by Administrator on 2018/9/28.
 */

public class RenderAnimation extends Animation {

    //
    private PieInfoWrapperr lastFoundWrapper;
    private AnimatedPieViewConfig mConfig;
    private List<PieInfoWrapperr> mDataWrappers;
    private PieChartRender pieChartRender;

    public RenderAnimation(AnimatedPieViewConfig mConfig, List<PieInfoWrapperr> mDataWrappers, PieChartRender pieChartRender) {
        this.mConfig = mConfig;
        this.mDataWrappers = mDataWrappers;
        this.pieChartRender = pieChartRender;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (mConfig == null) {
            throw new NullPointerException("viewConfig为空");
        }
        PLog.i("interpolatedTime = " + interpolatedTime);
        if (interpolatedTime >= 0.0f && interpolatedTime <= 1.0f) {
            float angle = 360 * interpolatedTime + mConfig.startAngle;
            PieInfoWrapperr info = findPieinfoWithAngle(angle);
            pieChartRender. setCurPie(info == null ? lastFoundWrapper : info, angle);
        }
    }

    public PieInfoWrapperr findPieinfoWithAngle(float angle) {
        if (Util.isListEmpty(mDataWrappers)) return null;
        if (lastFoundWrapper != null && lastFoundWrapper.contains(angle)) {
            return lastFoundWrapper;
        }
        for (PieInfoWrapperr infoWrapper : mDataWrappers) {
            if (infoWrapper.contains(angle)) {
                lastFoundWrapper = infoWrapper;
                return infoWrapper;
            }
        }
        return null;
    }
}
