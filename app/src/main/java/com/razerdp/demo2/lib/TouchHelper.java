package com.razerdp.demo2.lib;

import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.animation.DecelerateInterpolator;

import com.razerdp.widget.animatedpieview.utils.PLog;

import java.util.List;

/**
 * Created by Administrator on 2018/9/28.
 */

public class TouchHelper {

    //因为判断点击时是判断内圆和外圆半径，可能很苛刻，所以这里可以考虑增加点击范围
    private int expandClickRange;
    private float centerX;
    private float centerY;
    private RectF touchBounds;
    private PieInfoWrapperr floatingWrapper;
    private ValueAnimator floatUpAnim;
    private float floatUpTime;
    private PieInfoWrapperr lastFloatWrapper;
    private ValueAnimator floatDownAnim;
    private float floatDownTime;
    private float touchX = -1;
    private float touchY = -1;
    private Paint mTouchPaint;
    private boolean sameClick;
    private PieInfoWrapperr lastTouchWrapper;
    private PieManager mPieManager;
    private AnimatedPieViewConfig mConfig;
    private PieChartRender pieChartRender;
    private List<PieInfoWrapperr> mDataWrappers;

    public void setmDataWrappers(List<PieInfoWrapperr> mDataWrappers) {
        this.mDataWrappers = mDataWrappers;
    }

    TouchHelper(PieManager mPieManager, AnimatedPieViewConfig mConfig,
                PieChartRender pieChartRender) {
        this(25, mPieManager, mConfig, pieChartRender);
    }

    TouchHelper(int expandClickRange, PieManager mPieManager,
                AnimatedPieViewConfig mConfig, PieChartRender pieChartRender) {
        this.expandClickRange = expandClickRange;
        this.mPieManager = mPieManager;
        this.mConfig = mConfig;
        this.pieChartRender = pieChartRender;
        touchBounds = new RectF();
    }

    public void reset() {
        centerX = 0;
        centerY = 0;
        touchBounds.setEmpty();
        floatUpAnim = floatUpAnim == null ? ValueAnimator.ofFloat(0, 1) : floatUpAnim;
        floatUpAnim.removeAllUpdateListeners();
        floatUpTime = 0;
        floatDownAnim = floatDownAnim == null ? ValueAnimator.ofFloat(0, 1) : floatDownAnim;
        floatDownAnim.removeAllUpdateListeners();
        floatUpTime = 0;
        floatingWrapper = null;
        lastFloatWrapper = null;
        lastTouchWrapper = null;
        touchX = -1;
        touchY = -1;
        sameClick = false;
    }

    public void prepare() {
        setCenter();//设置中心坐标
        mTouchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        floatUpAnim = ValueAnimator.ofFloat(0, 1);
        floatUpAnim.setDuration(mConfig.floatUpDuration);
        floatUpAnim.setInterpolator(new DecelerateInterpolator());
        floatUpAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                floatUpTime = (float) animation.getAnimatedValue();
                pieChartRender.callInvalidate();
            }
        });

        floatDownAnim = ValueAnimator.ofFloat(1, 0);
        floatDownAnim.setDuration(mConfig.floatDownDuration);
        floatDownAnim.setInterpolator(new DecelerateInterpolator());
        floatDownAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                floatDownTime = (float) animation.getAnimatedValue();
                pieChartRender.callInvalidate();
            }
        });
    }

    private void setCenter() {
        centerX = mPieManager.getDrawWidth() / 2;
        centerY = mPieManager.getDrawHeight() / 2;
    }


    public Paint prepareTouchPaint(PieInfoWrapperr wrapper) {
        if (mTouchPaint == null) {
            mTouchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        if (wrapper != null) {
            mTouchPaint.set(wrapper.mDrawPaint);
        }
        return mTouchPaint;
    }

    PieInfoWrapperr pointToPieInfoWrapper(float x, float y) {
        final boolean isStrokeMode = mConfig.strokeMode;
        final float strokeWidth = mConfig.strokeWidth;
        //外圆半径
        final float exCircleRadius = isStrokeMode ? pieChartRender.pieRadius + strokeWidth / 2 : pieChartRender.pieRadius;
        //内圆半径
        final float innerCircleRadius = isStrokeMode ? pieChartRender.pieRadius - strokeWidth / 2 : 0;
        //点击位置到圆心的直线距离(没开根)
        final double touchDistancePow = Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2);
        //内圆半径<=直线距离<=外圆半径
        final boolean isTouchInRing = touchDistancePow >= expandClickRange + Math.pow(innerCircleRadius, 2)
                && touchDistancePow <= expandClickRange + Math.pow(exCircleRadius, 2);
        if (!isTouchInRing) return null;
        return findWrapper(x, y);
    }

    private PieInfoWrapperr findWrapper(float x, float y) {
        //得到角度
        double touchAngle = Math.toDegrees(Math.atan2(y - centerY, x - centerX));
        if (touchAngle < 0) {
            touchAngle += 360.0f;
        }
        if (lastTouchWrapper != null && lastTouchWrapper.containsTouch((float) touchAngle)) {
            return lastTouchWrapper;
        }
        PLog.i("touch角度 = " + touchAngle);
        for (PieInfoWrapperr wrapper : mDataWrappers) {
            if (wrapper.containsTouch((float) touchAngle)) {
                lastTouchWrapper = wrapper;
                return wrapper;
            }
        }
        return null;
    }
}
