package com.razerdp.demo2.lib;


import android.graphics.Canvas;
import android.support.annotation.Nullable;

/**
 * 渲染器基类,具备：重置，准备工作，准备完成监听，绘制，布局改变的回调，销毁，等一系列基本的操作
 */
public abstract class BaseRender {

    protected String TAG = this.getClass().getSimpleName();
    IPieView mIPieView;
    PieManager mPieManager;
    private volatile boolean isPrepared;

    public BaseRender(IPieView iPieView) {
        mIPieView = iPieView;
        mPieManager = iPieView.getManager();
        mPieManager.registerRender(this);
    }

    public void draw(Canvas canvas) {
        if (!isPrepared) return;
        onDraw(canvas);
    }

    public void destroy() {
        onDestroy();
        mPieManager.unRegisterRender(this);
    }

    public final void prepare() {
        prepare(null);
    }

    //TODO 准备
    public final void prepare(@Nullable final OnPrepareFinishListener l) {
        isPrepared = false;
        reset();
        mIPieView.getPieView().post(new Runnable() {
            @Override
            public void run() {
                isPrepared = onPrepare();
                if (isPrepared) {
                    handlePrepareFinish(l);
                }
            }
        });
    }

    //TODO 准备完成
    protected void handlePrepareFinish(OnPrepareFinishListener l) {
        if (l != null) {//todo 准备工作完成
            boolean handled = l.onPrepareFin();
            if (handled) {
                return;
            }
        }
        //TODO 通知绘制
        callInvalidate();
    }

    public void callInvalidate() {
        mIPieView.onCallInvalidate();
    }

    public interface OnPrepareFinishListener {
        //TODO 准备完成监听
        boolean onPrepareFin();
    }

    public abstract void reset();

    public abstract boolean onPrepare();

    public abstract void onSizeChanged(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom);

    public abstract void onDraw(Canvas canvas);

    public abstract void onDestroy();
}
