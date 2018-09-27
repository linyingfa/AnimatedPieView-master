package com.razerdp.widget.animatedpieview.utils;

/**
 * Created by 大灯泡 on 2017/11/9.
 */

public class DegreeUtil {

    /**
     * 点击
     * 甜甜圈的点击是一个比较麻烦的点，主要原因如下：
     * <p>
     * 甜甜圈支持起始角度设置，而对起始角度并没有做要求，也就是传入-3600°也是可以的
     * 点击的时候需要精确判定点击的区域在哪个甜甜圈里
     * 甜甜圈被点击后的动作，以及上一次点击的甜甜圈动画和本次点击的甜甜圈动画需要切换（一个还原一个上浮）
     * 首先看看第一个问题，我们的甜甜圈虽然可以设置无限角度，但实际上其实归根结底可以归到0 ~ 360°之间，
     * 即便传入一个很大的值，其实也是一定倍数 * 360 + 偏移量而已，所以针对任意角度，我们需要将其收束到0 ~ 360°之间：
     *
     * @param inputAngle
     * @return
     */

    public static float limitDegreeInTo360(double inputAngle) {
        float result;
        double tInputAngle = inputAngle - (int) inputAngle;//取小数
        //TODO 取余（取模）有个规律就是：左边小于右边，结果为左边，左边大于右边，看余数
        result = (float) ((int) inputAngle % 360.0f + tInputAngle);
        return result < 0 ? 360.0f + result : result;
    }
}
