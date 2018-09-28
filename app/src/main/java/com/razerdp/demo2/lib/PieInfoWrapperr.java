package com.razerdp.demo2.lib;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;


import com.razerdp.widget.animatedpieview.data.*;
import com.razerdp.widget.animatedpieview.utils.DegreeUtil;
import com.razerdp.widget.animatedpieview.utils.PLog;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/9/28.
 */

final class PieInfoWrapperr implements Serializable {

    private static final long serialVersionUID = -8551831728967624659L;

    public String id = null;
    public volatile boolean hasCached;
    public IPieInfo mPieInfo = null;
    //============= 绘制设置 =============
    public Paint mDrawPaint;
    public Paint mAlphaDrawPaint;
    public Paint mTexPaint;
    public Paint mIconPaint;
    public Path mLinePath;
    public Path mLinePathMeasure;
    public Bitmap icon;
    //============= 参数 =============
    public float fromAngle;
    public float sweepAngle;
    public float toAngle;
    public boolean autoDesc;
    public String desc;

    //============= 节点 =============
    public PieInfoWrapperr preWrapper;
    public PieInfoWrapperr nextWrapper;
    //=============================================================generate id
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);


    PieInfoWrapperr(IPieInfo pieInfo) {
        if (pieInfo == null) {
            throw new NullPointerException("pieinfo must not be null");
        }
        id = generateId();
        mPieInfo = pieInfo;
    }

    public String getId() {
        return id;
    }

    private String generateId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1;//Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return Integer.toString(result);
            }
        }
    }

    public PieInfoWrapperr prepare(AnimatedPieViewConfig config) {
        hasCached = false;
        if (mDrawPaint == null) mDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        if (mAlphaDrawPaint == null)
            mAlphaDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        if (mTexPaint == null) mTexPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        if (mIconPaint == null) {
            mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            mIconPaint.setFilterBitmap(true);
        }
        if (mLinePath == null) mLinePath = new Path();
        if (mLinePathMeasure == null) mLinePathMeasure = new Path();

        mDrawPaint.setStyle(config.strokeMode ? Paint.Style.STROKE : Paint.Style.FILL);
        mDrawPaint.setStrokeWidth(config.strokeWidth);
        mDrawPaint.setColor(mPieInfo.getColor());
        mAlphaDrawPaint.set(mDrawPaint);

        mTexPaint.setStyle(Paint.Style.FILL);
        mTexPaint.setTextSize(config.textSize);

        mLinePath.reset();
        return this;
    }

    boolean contains(float angle) {
        return angle >= fromAngle && angle <= toAngle;
    }

    boolean containsTouch(float angle) {
        //所有点击的角度都需要收归到0~360的范围，兼容任意角度
        final float tAngle = DegreeUtil.limitDegreeInTo360(angle);
        float tStart = DegreeUtil.limitDegreeInTo360(fromAngle);
        float tEnd = DegreeUtil.limitDegreeInTo360(toAngle);
        PLog.d("containsTouch  >>  tStart： " + tStart + "   tEnd： " + tEnd + "   tAngle： " + tAngle);
        boolean result;
        if (tEnd < tStart) {
            if (tAngle > 180) {
                //已经过界
                result = tAngle >= tStart && (360 - tAngle) <= sweepAngle;
            } else {
                result = tAngle + 360 >= tStart && tAngle <= tEnd;
            }
        } else {
            result = tAngle >= tStart && tAngle <= tEnd;
        }
        if (result) {
            PLog.i("find touch point  >>  " + toString());
        }
        return result;
    }

    public float getMiddleAngle() {
        return fromAngle + sweepAngle / 2;
    }

    public PieOption getPieOption() {
        return mPieInfo.getPieOpeion();
    }


    public Bitmap getIcon(int textWidth, int textHeight) {
        if (icon != null) return icon;
        PieOption option = mPieInfo.getPieOpeion();
        if (option == null || option.getLabelIcon() == null || option.getLabelIcon().isRecycled())
            return null;

        boolean disableAutoScaleWithText = option.getIconWidth() > 0
                || option.getIconHeight() > 0
                || option.getIconScaledWidth() > 0
                || option.getIconScaledHeight() > 0
                || TextUtils.isEmpty(desc);

        Bitmap mIcon = option.getLabelIcon();
        final int iconWidth = mIcon.getWidth();
        final int iconHeight = mIcon.getHeight();

        if (disableAutoScaleWithText) {
            Matrix matrix = null;
            float sX = 1.0f;
            float sY = 1.0f;

            //优先取固定数值的，有需要的时候才缩放，。
            if (option.getIconWidth() > 0 || option.getIconHeight() > 0) {
                matrix = new Matrix();
                sX = (option.getIconWidth() <= 0 ? option.getIconHeight() : option.getIconWidth()) / iconWidth;
                sY = (option.getIconHeight() <= 0 ? option.getIconWidth() : option.getIconHeight()) / iconHeight;
            } else if (option.getIconScaledWidth() > 0 || option.getIconScaledHeight() > 0) {
                matrix = new Matrix();
                sX = option.getIconScaledWidth() <= 0 ? option.getIconScaledHeight() : option.getIconScaledWidth();
                sY = option.getIconScaledHeight() <= 0 ? option.getIconScaledWidth() : option.getIconScaledHeight();
            }
            if (matrix != null) {
                matrix.postScale(sX, sY);
                icon = Bitmap.createBitmap(mIcon, 0, 0, iconWidth, iconHeight, matrix, true);
            } else {
                icon = mIcon;
            }
        } else {
            if (iconWidth > textWidth || iconHeight > textHeight) {
                Matrix matrix = new Matrix();
                float sX = 1.0f;
                float sY = 1.0f;
                if (iconWidth > textWidth) {
                    sX = (float) textWidth / iconWidth;
                }
                if (iconHeight > textHeight) {
                    sY = (float) textHeight / iconHeight;
                }
                float scale = Math.min(sX, sY);
                matrix.postScale(scale, scale);
                icon = Bitmap.createBitmap(mIcon, 0, 0, iconWidth, iconHeight, matrix, true);
            }
        }
        return icon;
    }

    public float calculateDegree(float lastPieDegree, double sum, AnimatedPieViewConfig config) {
        fromAngle = lastPieDegree;
        sweepAngle = (float) (360f * (Math.abs(mPieInfo.getValue()) / sum));
        toAngle = fromAngle + sweepAngle;
        if (autoDesc) {
            //自动填充描述auto
            desc = String.format(config.autoDescStringFormat,
                    com.razerdp.widget.animatedpieview.AnimatedPieViewConfig.sFormateRate.format((mPieInfo.getValue() / sum) * 100));
            if (mPieInfo instanceof SimplePieInfo) {
                ((SimplePieInfo) mPieInfo).setDesc(desc);
            }
        } else {
            desc = mPieInfo.getDesc();
        }
        PLog.d("【calculate】 " + "{ \n" + "id = " + id + "\nfromAngle = " + fromAngle + "\nsweepAngle = " + sweepAngle + "\ntoAngle = " + toAngle + "\n desc = " + desc + "\n  }");
        return toAngle;
    }




    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PieInfoWrapperr)) {
            return false;
        } else {
            PieInfoWrapperr from = (PieInfoWrapperr) obj;
            return obj == this || TextUtils.equals(from.getId(), id);
        }
    }



    @Override
    public String toString() {
        return "{ \nid = " + id + '\n'
                + "value =  " + mPieInfo.getValue() + '\n'
                + "fromAngle = " + fromAngle + '\n'
                + "toAngle = " + toAngle + "\n  }";
    }
}
