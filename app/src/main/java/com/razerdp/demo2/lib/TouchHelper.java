package com.razerdp.demo2.lib;

import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.razerdp.widget.animatedpieview.render.*;
import com.razerdp.widget.animatedpieview.utils.PLog;

import java.util.List;

/**
 * Created by Administrator on 2018/9/28.
 */

public class TouchHelper {

    //因为判断点击时是判断内圆和外圆半径，可能很苛刻，所以这里可以考虑增加点击范围
    public int expandClickRange;
    public float centerX;
    public float centerY;
    public RectF touchBounds;
    public PieInfoWrapperr floatingWrapper;
    public ValueAnimator floatUpAnim;
    public float floatUpTime;
    public PieInfoWrapperr lastFloatWrapper;
    public ValueAnimator floatDownAnim;
    public float floatDownTime;
    public float touchX = -1;
    public float touchY = -1;
    public Paint mTouchPaint;
    public boolean sameClick;
    public PieInfoWrapperr lastTouchWrapper;
    public PieManager mPieManager;
    public AnimatedPieViewConfig mConfig;
    public PieChartRender pieChartRender;
    public List<PieInfoWrapperr> mDataWrappers;


    public void setmConfig(AnimatedPieViewConfig mConfig) {
        this.mConfig = mConfig;
    }

    public void setmDataWrappers(List<PieInfoWrapperr> mDataWrappers) {
        this.mDataWrappers = mDataWrappers;
    }


    TouchHelper(PieManager mPieManager, PieChartRender pieChartRender) {
        this(25, mPieManager, pieChartRender);
    }

    TouchHelper(int expandClickRange, PieManager mPieManager,
                PieChartRender pieChartRender) {
        this.expandClickRange = expandClickRange;
        this.mPieManager = mPieManager;
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

    public void setCenter() {
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

    public PieInfoWrapperr pointToPieInfoWrapper(float x, float y) {
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

    public void setTouchBounds(float timeSet) {
        final float scaleSizeInTouch = !mConfig.strokeMode ? mConfig.floatExpandSize : 0;
        touchBounds.set(pieChartRender.pieBounds.left - scaleSizeInTouch * timeSet,
                pieChartRender.pieBounds.top - scaleSizeInTouch * timeSet,
                pieChartRender.pieBounds.right + scaleSizeInTouch * timeSet,
                pieChartRender.pieBounds.bottom + scaleSizeInTouch * timeSet);
    }

    public boolean handleTouch(MotionEvent event) {
        if (mConfig == null || !mConfig.canTouch || pieChartRender.isInAnimating) return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                PieInfoWrapperr touchWrapper = pointToPieInfoWrapper(touchX, touchY);
                if (touchWrapper == null) return false;
                handleUp(touchWrapper);

                return true;
        }

        return false;
    }

    public void handleUp(PieInfoWrapperr touchWrapper) {
        setDrawMode(DrawMode.TOUCH);
        if (touchWrapper.equals(floatingWrapper)) {
            //如果点的是当前正在浮起的wrapper，则移到上一个，当前的置空
            lastFloatWrapper = touchWrapper;
            floatingWrapper = null;
            sameClick = true;
        } else {
            lastFloatWrapper = floatingWrapper;
            floatingWrapper = touchWrapper;
            sameClick = false;
        }

        if (mConfig.animTouch) {
            floatUpAnim.start();
            floatDownAnim.start();
        } else {
            floatUpTime = 1;
            floatDownTime = 1;
            pieChartRender.callInvalidate();
        }

        if (mConfig.mSelectListener != null) {
            mConfig.mSelectListener.onSelectPie(touchWrapper.mPieInfo, touchWrapper.equals(floatingWrapper));
        }
    }

    private void setDrawMode(DrawMode drawMode) {
        if (drawMode == DrawMode.TOUCH && pieChartRender.isInAnimating)
            return;
        pieChartRender.mDrawMode = drawMode;
    }

}
