package com.razerdp.demo2.lib;

import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


import com.razerdp.widget.animatedpieview.utils.Util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/28.
 */

public class AnimatedPieViewConfig {

    private static final long serialVersionUID = -2285434281608092357L;
    private static final String TAG = "AnimatedPieViewConfig";
    public static final int FOCUS_WITH_ALPHA = 0x10;
    public static final int FOCUS_WITH_ALPHA_REV = 0x11;
    public static final int FOCUS_WITHOUT_ALPHA = 0x12;
    public static final int ABOVE = 0x20;
    public static final int BELOW = 0x21;
    public static final int ALIGN = 0x22;
    public static final int ECTOPIC = 0x23;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FOCUS_WITH_ALPHA, FOCUS_WITH_ALPHA_REV, FOCUS_WITHOUT_ALPHA})
    public @interface FocusAlpha {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ABOVE, BELOW, ALIGN, ECTOPIC})
    public @interface TextGravity {
    }

    //=============================================================default
    public static DecimalFormat sFormateRate = new DecimalFormat("0.##");
    public static final String DEFAULT_AUTO_DESC_FORMAT = "%1$s%%";
    public static final int DEFAULT_STROKE_WIDTH = 80;
    public static final float DEFAULT_START_DEGREE = -90.0f;
    public static final long DEFAULT_ANIMATION_DURATION = 3000;
    public static final long DEFAULT_TOUCH_FLOATUP_DURATION = 500;
    public static final long DEFAULT_TOUCH_FLOATDOWN_DURATION = 800;
    public static final float DEFAULT_SHADOW_BLUR_RADIUS = 18;
    public static final float DEFAULT_FLOAT_EXPAND_ANGLE = 5;
    public static final float DEFAULT_DESC_TEXT_SIZE = 14;
    public static final float DEFAULT_SPLIT_ANGLE = 0;
    public static final float DEFAULT_FLOAT_EXPAND_SIZE = 15;
    public static final int DEFAULT_FOCUS_ALPHA_TYPE = FOCUS_WITH_ALPHA_REV;
    public static int DEFAULT_FOCUS_ALPHA = 150;
    public static final int DEFAULT_TEXT_GRAVITY = ECTOPIC;
    public static final int DEFAULT_GUIDE_POINT_RADIUS = 4;
    public static final int DEFAULT_GUIDE_MARGIN_START = 10;
    public static final int DEFAULT_GUIDE_LINE_WIDTH = 2;
    public static final int DEFAULT_TEXT_MARGIN = 6;
    public static final Interpolator DEFAULT_ANIMATION_INTERPOLATOR = new LinearInterpolator();
    //=============================================================option
    public int strokeWidth = DEFAULT_STROKE_WIDTH;
    public float startAngle = DEFAULT_START_DEGREE;
    public long duration = DEFAULT_ANIMATION_DURATION;
    public long floatUpDuration = DEFAULT_TOUCH_FLOATUP_DURATION;
    public long floatDownDuration = DEFAULT_TOUCH_FLOATDOWN_DURATION;
    public float floatShadowRadius = DEFAULT_SHADOW_BLUR_RADIUS;
    public float floatExpandAngle = DEFAULT_FLOAT_EXPAND_ANGLE;
    public float floatExpandSize = DEFAULT_FLOAT_EXPAND_SIZE;
    public String autoDescStringFormat = DEFAULT_AUTO_DESC_FORMAT;
    public boolean autoSize = true;
    public float pieRadius = 0;
    public float pieRadiusRatio = 0;
    public float textSize = DEFAULT_DESC_TEXT_SIZE;
    public boolean drawText = false;
    public float splitAngle = DEFAULT_SPLIT_ANGLE;
    public boolean animatePie = true;
    public boolean canTouch = true;
    public boolean animTouch = true;
    public OnPieSelectListener mSelectListener;
    @AnimatedPieViewConfig.FocusAlpha
    public int focusAlphaType = DEFAULT_FOCUS_ALPHA_TYPE;
    public int focusAlpha = DEFAULT_FOCUS_ALPHA;
    @AnimatedPieViewConfig.TextGravity
    public int textGravity = DEFAULT_TEXT_GRAVITY;
    public int guidePointRadius = DEFAULT_GUIDE_POINT_RADIUS;
    public int guideLineMarginStart = DEFAULT_GUIDE_MARGIN_START;
    public int guideLineWidth = DEFAULT_GUIDE_LINE_WIDTH;
    public boolean cubicGuide = false;
    public int textMargin = DEFAULT_TEXT_MARGIN;
    public Interpolator animationInterpolator = DEFAULT_ANIMATION_INTERPOLATOR;
    public boolean strokeMode = true;
    public List<Pair<IPieInfo, Boolean>> mDatas;

    public AnimatedPieViewConfig() {
        this(null);
    }

    public AnimatedPieViewConfig(AnimatedPieViewConfig config) {
        mDatas = new ArrayList<>();
        if (config != null) {
            copyFrom(config);
        }
    }

    public AnimatedPieViewConfig copyFrom(AnimatedPieViewConfig config) {
        if (config == null) return this;
        this.mDatas.clear();
        this.mDatas.addAll(config.mDatas);
        return strokeWidth(config.strokeWidth)
                .startAngle(config.startAngle)
                .duration(config.duration)
                .floatUpDuration(config.floatUpDuration)
                .floatDownDuration(config.floatDownDuration)
                .floatShadowRadius(config.floatShadowRadius)
                .floatExpandAngle(config.floatExpandAngle)
                .autoDescStringFormat(config.autoDescStringFormat)
                .autoSize(config.autoSize)
                .pieRadius(config.pieRadius)
                .pieRadiusRatio(config.pieRadiusRatio)
                .textSize(config.textSize)
                .drawText(config.drawText)
                .splitAngle(config.splitAngle)
                .animatePie(config.animatePie)
                .strokeMode(config.strokeMode)
                .canTouch(config.canTouch)
                .animOnTouch(config.animTouch)
                .selectListener(config.mSelectListener)
                .floatExpandSize(config.floatExpandSize)
                .focusAlphaType(config.focusAlphaType)
                .focusAlpha(config.focusAlpha)
                .textGravity(config.textGravity)
                .guidePointRadius(config.guidePointRadius)
                .guideLineMarginStart(config.guideLineMarginStart)
                .cubicGuide(config.cubicGuide)
                .guideLineWidth(config.guideLineWidth)
                .textMargin(config.textMargin)
                .interpolator(config.animationInterpolator);
    }

    public AnimatedPieViewConfig strokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public AnimatedPieViewConfig startAngle(float startAngle) {
        this.startAngle = startAngle;
        return this;
    }

    /**
     * <h3>CN:</h3>甜甜圈的动画时间，过短的话可能会造成部分甜甜圈无法绘制,建议高于500ms
     * <h3>EN:</h3>How long this piechart should draw. The duration cannot be negative.Recommended above 500ms
     *
     * @param duration Duration in milliseconds(Recommended above 500ms)
     */
    public AnimatedPieViewConfig duration(long duration) {
        this.duration = Math.max(500, duration);
        return this;
    }

    public AnimatedPieViewConfig floatUpDuration(long popupDuration) {
        this.floatUpDuration = popupDuration;
        return this;
    }

    public AnimatedPieViewConfig floatDownDuration(long floatDownDuration) {
        this.floatDownDuration = floatDownDuration;
        return this;
    }

    public AnimatedPieViewConfig floatShadowRadius(float floatShadowRadius) {
        this.floatShadowRadius = floatShadowRadius;
        return this;
    }

    public AnimatedPieViewConfig floatExpandAngle(float floatExpandAngle) {
        this.floatExpandAngle = floatExpandAngle;
        return this;
    }

    public AnimatedPieViewConfig autoDescStringFormat(String autoDescStringFormat) {
        this.autoDescStringFormat = autoDescStringFormat;
        return this;
    }

    public AnimatedPieViewConfig autoSize(boolean autoSize) {
        this.autoSize = autoSize;
        return this;
    }

    public AnimatedPieViewConfig pieRadius(float pieRadius) {
        this.pieRadius = pieRadius;
        return autoSize(pieRadius <= 0);
    }

    public AnimatedPieViewConfig pieRadiusRatio(@FloatRange(from = 0f, to = 1f) float pieRadiusRatio) {
        this.pieRadiusRatio = pieRadiusRatio;
        return autoSize(pieRadiusRatio <= 0);
    }


    public AnimatedPieViewConfig textSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public AnimatedPieViewConfig drawText(boolean drawText) {
        this.drawText = drawText;
        return this;
    }

    public AnimatedPieViewConfig splitAngle(float splitAngle) {
        this.splitAngle = splitAngle;
        return this;
    }

    public AnimatedPieViewConfig animatePie(boolean animatePie) {
        this.animatePie = animatePie;
        return this;
    }

    public AnimatedPieViewConfig strokeMode(boolean strokeMode) {
        this.strokeMode = strokeMode;
        return this;
    }

    public AnimatedPieViewConfig canTouch(boolean canTouch) {
        this.canTouch = canTouch;
        return this;
    }

    public AnimatedPieViewConfig animOnTouch(boolean animTouch) {
        this.animTouch = animTouch;
        return this;
    }

    public <T extends IPieInfo> AnimatedPieViewConfig selectListener(OnPieSelectListener<T> selectListener) {
        mSelectListener = selectListener;
        return this;
    }

    public AnimatedPieViewConfig floatExpandSize(float floatExpandSize) {
        this.floatExpandSize = floatExpandSize;
        return this;
    }

    public AnimatedPieViewConfig focusAlphaType(@AnimatedPieViewConfig.FocusAlpha int focusAlphaType) {
        this.focusAlphaType = focusAlphaType;
        return this;
    }

    public AnimatedPieViewConfig focusAlpha(int focusAlpha) {
        this.focusAlpha = focusAlpha;
        return this;
    }

    public AnimatedPieViewConfig textGravity(@AnimatedPieViewConfig.TextGravity int textGravity) {
        this.textGravity = textGravity;
        return this;
    }

    public AnimatedPieViewConfig guidePointRadius(int guidePointRadius) {
        this.guidePointRadius = guidePointRadius;
        return this;
    }

    public AnimatedPieViewConfig guideLineMarginStart(int guideLineMarginStart) {
        this.guideLineMarginStart = guideLineMarginStart;
        return this;
    }

    public AnimatedPieViewConfig cubicGuide(boolean cubicGuide) {
        this.cubicGuide = cubicGuide;
        if (cubicGuide) {
            return textGravity(ALIGN);
        }
        return this;
    }

    public AnimatedPieViewConfig guideLineWidth(int guideLineWidth) {
        this.guideLineWidth = guideLineWidth;
        return this;
    }

    public AnimatedPieViewConfig textMargin(int textMargin) {
        this.textMargin = textMargin;
        return this;
    }

    public AnimatedPieViewConfig interpolator(Interpolator interpolator) {
        this.animationInterpolator = interpolator;
        return this;
    }

    //=============================================================data
    public AnimatedPieViewConfig addData(@NonNull IPieInfo info) {
        return addData(info, false);
    }

    public AnimatedPieViewConfig addData(@NonNull IPieInfo info, boolean autoDesc) {
        if (info == null) {
            Log.e(TAG, "addData: pieinfo is null,abort add data");
            return this;
        }
        mDatas.add(Pair.create(info, autoDesc));
        return this;
    }

    public List<Pair<IPieInfo, Boolean>> getDatas() {
        return mDatas;
    }

    public List<IPieInfo> getRawDatas() {
        List<IPieInfo> result = new ArrayList<>();
        if (!Util.isListEmpty(mDatas)) {
            for (Pair<IPieInfo, Boolean> data : mDatas) {
                result.add(data.first);
            }
        }
        return result;
    }
}
