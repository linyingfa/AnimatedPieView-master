package com.razerdp.demo2.lib;

import android.graphics.Canvas;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.razerdp.widget.animatedpieview.render.*;

import java.util.List;


/**
 *
 */
public class PieChartRender extends BaseRender implements ITouchRender {

    IPieView pieView;

    private List<PieInfoWrapperr> mDataWrappers;
    private List<PieInfoWrapperr> mCachedDrawWrappers;
    private PathMeasure mPathMeasure;//path测量
    private AnimatedPieViewConfig mConfig;//配置参数
    private DrawMode mDrawMode = DrawMode.DRAW;//模式
    //-----------------------------------------draw area-----------------------------------------
    public RectF pieBounds;
    public float pieRadius;
    public int maxDescTextLength;
    private volatile boolean isInAnimating;
    //-----------------------------------------anim area-----------------------------------------
    private PieInfoWrapper mDrawingPie;
    private float animAngle;
    //-----------------------------------------other-----------------------------------------
    private TouchHelper mTouchHelper;
    private RenderAnimation mRenderAnimation;
    private volatile boolean animHasStart;
    private volatile boolean hasRenderDefaultSelected;


    public PieChartRender(IPieView pieView) {
        super(pieView);
        this.pieView = pieView;
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean onPrepare() {
        return false;
    }

    @Override
    public void onSizeChanged(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {

    }

    @Override
    public void onDraw(Canvas canvas) {

    }

    //-----------------------------------------touch-----------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchHelper.handleTouch(event);
    }

    @Override
    public void forceAbortTouch() {

    }

    @Override
    public void onDestroy() {

    }
}
