package com.razerdp.demo2.lib;


import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;

import com.razerdp.widget.animatedpieview.utils.PLog;
import com.razerdp.widget.animatedpieview.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/9/28.
 */

public class PieManager {

    IPieView pieView;
    List<BaseRender> mRenders;
    private RectF drawBounds;
    private Paint textMeasurePaint;
    private Rect textBounds;

    public PieManager(IPieView pieView) {
        this.pieView = pieView;
        mRenders = new ArrayList<>();
        drawBounds = new RectF();
        textBounds = new Rect();
        textMeasurePaint = new TextPaint();
        textMeasurePaint.setStyle(Paint.Style.FILL);
    }

    public void setChartContentRect(int width, int height, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        PLog.i(String.format(Locale.getDefault(),
                "size change : { \n width = %s;\n height = %s;\n paddingLeft = %s;" +
                        "\n padding top = %s;\n paddingRight = %s;\n paddingBottom = %s;\n}",
                width, height, paddingLeft, paddingTop, paddingRight, paddingBottom));
        drawBounds.set(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom);
        for (BaseRender baseRender : mRenders) {
            baseRender.onSizeChanged(width, height, paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    public float getDrawWidth() {
        return drawBounds.width();
    }

    public float getDrawHeight() {
        return drawBounds.height();
    }

    public RectF getDrawBounds() {
        return drawBounds;
    }

    public Rect measureTextBounds(String text, int textSize) {
        if (TextUtils.isEmpty(text)) {
            textBounds.setEmpty();
            return textBounds;
        }
        textMeasurePaint.setTextSize(textSize);
        textMeasurePaint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds;
    }

    public Rect measureTextBounds(String text, Paint paint) {
        if (TextUtils.isEmpty(text) || paint == null) {
            textBounds.setEmpty();
            return textBounds;
        }
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds;
    }

    //-----------------------------------------render observer-----------------------------------------
    public void registerRender(BaseRender render) {
        if (render == null) {
            return;
        }
        if (!mRenders.contains(render)) {//TODO 没有添加过的就添加进去，添加注册
            mRenders.add(render);
        }
    }

    public void unRegisterRender(BaseRender render) {
        if (Util.isListEmpty(mRenders) || !mRenders.contains(render)) return;
        mRenders.remove(render); ///TODO 删除注册
    }
}
