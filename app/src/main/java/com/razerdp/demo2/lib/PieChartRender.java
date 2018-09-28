package com.razerdp.demo2.lib;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.animation.Animation;



import com.razerdp.widget.animatedpieview.utils.AnimationCallbackUtils;
import com.razerdp.widget.animatedpieview.utils.PLog;
import com.razerdp.widget.animatedpieview.utils.Util;

import java.util.ArrayList;
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
    public DrawMode mDrawMode = DrawMode.DRAW;//模式
    //-----------------------------------------draw area-----------------------------------------
    public RectF pieBounds;
    public float pieRadius;
    public int maxDescTextLength;
    public volatile boolean isInAnimating;
    //-----------------------------------------anim area-----------------------------------------
    private PieInfoWrapperr mDrawingPie;
    private float animAngle;
    //-----------------------------------------other-----------------------------------------
    private TouchHelper mTouchHelper;
    private RenderAnimation mRenderAnimation;
    private volatile boolean animHasStart;
    private volatile boolean hasRenderDefaultSelected;


    public PieChartRender(IPieView pieView) {
        super(pieView);
        //TODO 初始化一些容器
        mDataWrappers = new ArrayList<>();
        mCachedDrawWrappers = new ArrayList<>();
        mPathMeasure = new PathMeasure();
        pieBounds = new RectF();
        mTouchHelper = new TouchHelper(mPieManager, this);
        pieRadius = 0;
    }

    @Override
    public void reset() {
        mTouchHelper.reset();
        pieBounds.setEmpty();
        animHasStart = false;
        isInAnimating = false;
        pieRadius = 0;
        hasRenderDefaultSelected = false;
        mDataWrappers = mDataWrappers == null ? new ArrayList<PieInfoWrapperr>() : mDataWrappers;
        mDataWrappers.clear();
        mCachedDrawWrappers = mCachedDrawWrappers == null ? new ArrayList<PieInfoWrapperr>() : mCachedDrawWrappers;
        mCachedDrawWrappers.clear();
        mDrawingPie = null;
        mRenderAnimation = null;
        mIPieView.getPieView().clearAnimation();
    }

    @Override
    public boolean onPrepare() {
        mConfig = mIPieView.getConfig();
        mTouchHelper.setmConfig(mConfig);
        if (mConfig == null) {
            Log.e(TAG, "onPrepare: config is null,abort draw because of preparing failed");
            return false;
        }
        setDrawMode(DrawMode.DRAW);
        mTouchHelper.prepare();//准备Touch
        prepareAnim();//准备动画
        //wrap datas and calculate sum value
        //包裹数据并且计算总和
        double sum = 0;
        PieInfoWrapperr preWrapper = null;
        for (Pair<IPieInfo, Boolean> info : mConfig.getDatas()) {
            sum += Math.abs(info.first.getValue());
            PieInfoWrapperr wrapper = new PieInfoWrapperr(info.first);
            wrapper.autoDesc = info.second;
            //简单的形成一个链表
            if (preWrapper != null) {
                preWrapper.nextWrapper = wrapper;
                wrapper.preWrapper = preWrapper;
            }
            preWrapper = wrapper;
            mDataWrappers.add(wrapper);
        }

        //calculate degree for each pieInfoWrapper
        //计算每个wrapper的角度
        float lastAngle = mConfig.startAngle;
        for (PieInfoWrapperr dataWrapper : mDataWrappers) {
            dataWrapper.prepare(mConfig);
            lastAngle = dataWrapper.calculateDegree(lastAngle, sum, mConfig);
            int textWidth = mPieManager.measureTextBounds(dataWrapper.desc, (int) mConfig.textSize).width();
            int textHeight = mPieManager.measureTextBounds(dataWrapper.desc, (int) mConfig.textSize).height();
            int labelWidth = 0;
            int labelHeight = 0;
            int labelPadding = 0;
            Bitmap label = dataWrapper.getIcon(textWidth, textHeight);
            if (label != null) {
                if (dataWrapper.getPieOption() != null) {
                    labelPadding = dataWrapper.getPieOption().getLabelPadding();
                }
                labelWidth = label.getWidth();
                labelHeight = label.getHeight();
            }
            textWidth += labelWidth + labelPadding * 2;
            maxDescTextLength = Math.max(maxDescTextLength, textWidth);
            PLog.i("desc >> " + dataWrapper.desc + "  maxDesTextSize >> " + maxDescTextLength);
        }
        mTouchHelper.setmDataWrappers(mDataWrappers);
        return true;
    }

    private void prepareAnim() {
        if (mConfig.animatePie) {
            mRenderAnimation = new RenderAnimation(mConfig, mDataWrappers, this);
            mRenderAnimation.setInterpolator(mConfig.animationInterpolator);
            mRenderAnimation.setDuration(mConfig.duration);
            mRenderAnimation.setAnimationListener(new AnimationCallbackUtils.SimpleAnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    super.onAnimationStart(animation);
                    isInAnimating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isInAnimating = false;
                    onDrawFinish();
                }
            });
        }

    }

    @Override
    public void onSizeChanged(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        if (mTouchHelper != null) {
            mTouchHelper.setCenter();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        float width = mPieManager.getDrawWidth();
        float height = mPieManager.getDrawHeight();
        float centerX = width / 2;
        float centerY = height / 2;
        canvas.translate(centerX, centerY);
        measurePieRadius(width, height);
        switch (mDrawMode) {
            case DRAW:
                renderDraw(canvas);
                break;
            case TOUCH:
                renderTouch(canvas);
                break;
        }
    }

    private void renderTouch(Canvas canvas) {
        drawCachedPie(canvas, mTouchHelper.sameClick ? mTouchHelper.lastFloatWrapper : mTouchHelper.floatingWrapper);
        renderTouchDraw(canvas, mTouchHelper.lastFloatWrapper, mTouchHelper.floatDownTime);
        PLog.i("lastFloatWrapper id = " + (mTouchHelper.lastFloatWrapper == null ? "null" : mTouchHelper.lastFloatWrapper.getId()) + "  downTime = " + mTouchHelper.floatDownTime);
        renderTouchDraw(canvas, mTouchHelper.floatingWrapper, mTouchHelper.floatUpTime);
        PLog.d("floatingWrapper id = " + (mTouchHelper.floatingWrapper == null ? "null" : mTouchHelper.floatingWrapper.getId()) + "  upTime = " + mTouchHelper.floatUpTime);
    }

    private void renderTouchDraw(Canvas canvas, PieInfoWrapperr wrapper, float timeSet) {
        if (wrapper == null) return;
        mTouchHelper.setTouchBounds(timeSet);
        Paint touchPaint = mTouchHelper.prepareTouchPaint(wrapper);
        touchPaint.setShadowLayer(mConfig.floatShadowRadius * timeSet, 0, 0, touchPaint.getColor());
        touchPaint.setStrokeWidth(mConfig.strokeWidth + (mConfig.floatExpandSize * timeSet));
        applyAlphaToPaint(wrapper, touchPaint);
        canvas.drawArc(mTouchHelper.touchBounds,
                wrapper.fromAngle - (mConfig.floatExpandSize * timeSet),
                wrapper.sweepAngle + (mConfig.floatExpandAngle * 2 * timeSet) - mConfig.splitAngle,
                !mConfig.strokeMode,
                touchPaint);
    }

    private void renderDraw(Canvas canvas) {
        if (mConfig.animatePie) {
            if (mRenderAnimation != null && !isInAnimating && !animHasStart) {
                animHasStart = true;
                mIPieView.getPieView().startAnimation(mRenderAnimation);
                return;
            }
            renderAnimDraw(canvas);
        } else {
            renderNormalDraw(canvas);
        }
    }

    private void renderNormalDraw(Canvas canvas) {
        if (Util.isListEmpty(mCachedDrawWrappers) || mCachedDrawWrappers.size() != mDataWrappers.size()) {
            mCachedDrawWrappers.clear();
            mCachedDrawWrappers.addAll(mDataWrappers);
        }
        drawCachedPie(canvas, null);
        onDrawFinish();
    }

    private void renderAnimDraw(Canvas canvas) {
        if (mDrawingPie != null) {
            drawCachedPie(canvas, mDrawingPie);
            canvas.drawArc(pieBounds,
                    mDrawingPie.fromAngle, animAngle - mDrawingPie.fromAngle
                            - mConfig.splitAngle, !mConfig.strokeMode, mDrawingPie.mDrawPaint);
            if (mConfig.drawText && animAngle >= mDrawingPie.getMiddleAngle() && animAngle <= mDrawingPie.toAngle) {
//                drawText(canvas, mDrawingPie);
            }
        }
    }

    private void drawCachedPie(Canvas canvas, PieInfoWrapperr excluded) {
        if (!Util.isListEmpty(mCachedDrawWrappers)) {
            for (PieInfoWrapperr cachedDrawWrapper : mCachedDrawWrappers) {
                if (mConfig.drawText) {
//                    drawText(canvas, cachedDrawWrapper);
                }
                Paint paint = cachedDrawWrapper.mAlphaDrawPaint;
                applyAlphaToPaint(cachedDrawWrapper, paint);
                if (cachedDrawWrapper.equals(excluded)) {
                    continue;
                }
                canvas.drawArc(pieBounds,
                        cachedDrawWrapper.fromAngle,
                        cachedDrawWrapper.sweepAngle - mConfig.splitAngle,
                        !mConfig.strokeMode,
                        paint);
            }
        }
    }

    private void onDrawFinish() {
        PLog.i("drawFinish");
        if (mConfig.animatePie) {
            if (!animHasStart || isInAnimating) return;
        }
        if (hasRenderDefaultSelected) return;
        for (PieInfoWrapperr dataWrapper : mDataWrappers) {
            if (dataWrapper.getPieOption() != null && dataWrapper.getPieOption().isDefaultSelected()) {
                hasRenderDefaultSelected = true;
                mTouchHelper.handleUp(dataWrapper);
                break;
            }
        }
    }
    //-----------------------------------------touch-----------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchHelper.handleTouch(event);
    }

    @Override
    public void forceAbortTouch() {

    }

    //-----------------------------------------tools-----------------------------------------

    public void setCurPie(PieInfoWrapperr infoWrapper, float degree) {
        if (mDrawingPie != null) {
            if (degree >= mDrawingPie.toAngle / 2) {
                if (!mDrawingPie.hasCached) {
                    // fix anim duration too short
                    PieInfoWrapperr preWrapper = mDrawingPie.preWrapper;
                    if (preWrapper != null && !preWrapper.hasCached) {
                        preWrapper.hasCached = true;
                        mCachedDrawWrappers.add(preWrapper);
                    }
                    mCachedDrawWrappers.add(mDrawingPie);
                    mDrawingPie.hasCached = true;
                }
            }
        }
        mDrawingPie = infoWrapper;
        animAngle = degree;
        callInvalidate();
    }

    public void setDrawMode(DrawMode drawMode) {
        if (drawMode == DrawMode.TOUCH && isInAnimating) {
            return;
        }
        mDrawMode = drawMode;
    }

    private void measurePieRadius(float width, float height) {
        if (pieRadius > 0) {
            pieBounds.set(-pieRadius, -pieRadius, pieRadius, pieRadius);
            return;
        }
        final float minSize = Math.min(width / 2, height / 2);
        //最低0.5的最小高宽值
        float minPieRadius = minSize / 4;
        if (mConfig.autoSize) {
            //按照最大的文字测量
            float radius = Float.MAX_VALUE;
            while (radius > minSize) {
                if (radius == Float.MAX_VALUE) {
                    radius = minSize
                            - (mConfig.drawText ? maxDescTextLength : 0)
                            - (mConfig.strokeMode ? (mConfig.strokeWidth >> 1) : 0)
                            - mConfig.guideLineMarginStart;
                } else {
                    radius -= minSize / 10;
                }
            }
            pieRadius = Math.max(minPieRadius, radius);

        } else {
            //优先判定size
            if (mConfig.pieRadius > 0) {
                pieRadius = mConfig.pieRadius;
            } else if (mConfig.pieRadiusRatio > 0) {
                pieRadius = minSize / 2 * mConfig.pieRadiusRatio;
            } else {
                pieRadius = minPieRadius;
            }
        }
        pieBounds.set(-pieRadius, -pieRadius, pieRadius, pieRadius);
    }

    private float absMathSin(double angdeg) {
        return (float) Math.abs(Math.sin(Math.toRadians(angdeg)));
    }

    private float absMathCos(double angdeg) {
        return (float) Math.abs(Math.cos(Math.toRadians(angdeg)));
    }

    private float angleToProgress(float angle, PieInfoWrapperr wrapper) {
        if (wrapper == null || !mConfig.animatePie) return 1f;
        if (angle < wrapper.getMiddleAngle()) return 0f;
        if (angle >= wrapper.toAngle) return 1f;
        return (angle - wrapper.getMiddleAngle()) / (wrapper.toAngle - wrapper.getMiddleAngle());
    }

    //-----------------------------------------render draw fin-----------------------------------------

    private void applyAlphaToPaint(PieInfoWrapperr target, Paint paint) {
        if (paint == null) return;
        if (mDrawMode == DrawMode.DRAW) {
            paint.setAlpha(255);
            return;
        }

        //如果点的是同一个，则需要特殊处理
        boolean selected = false;
        if (mTouchHelper.floatingWrapper != null) {
            //针对浮起来的
            selected = mTouchHelper.floatingWrapper.equals(target);
        } else {
            if (mTouchHelper.lastFloatWrapper != null) {
                selected = mTouchHelper.lastFloatWrapper.equals(target);
            }
        }
        final float alphaCut = 255 - mConfig.focusAlpha;
        switch (mConfig.focusAlphaType) {
            case com.razerdp.widget.animatedpieview.AnimatedPieViewConfig.FOCUS_WITH_ALPHA:
                //选中的对象有alpha变化
                boolean alphaDown = selected && mTouchHelper.floatingWrapper != null;
                if (selected) {
                    paint.setAlpha((int) (255 - (alphaCut * (alphaDown ? mTouchHelper.floatUpTime : mTouchHelper.floatDownTime))));
                } else {
                    paint.setAlpha(255);
                }
                break;
            case com.razerdp.widget.animatedpieview.AnimatedPieViewConfig.FOCUS_WITH_ALPHA_REV:
                boolean alphaDown2 = !selected && mTouchHelper.floatingWrapper != null;
                if (selected) {
                    paint.setAlpha(255);
                } else {
                    paint.setAlpha((int) (255 - (alphaCut * (alphaDown2 ? mTouchHelper.floatUpTime : mTouchHelper.floatDownTime))));
                }
                break;
            case com.razerdp.widget.animatedpieview.AnimatedPieViewConfig.FOCUS_WITHOUT_ALPHA:
            default:
                paint.setAlpha(255);
                break;
        }

    }

    @Override
    public void onDestroy() {

    }
}
