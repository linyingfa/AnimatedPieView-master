package com.razerdp.demo2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.razerdp.widget.animatedpieview.utils.UIUtil;

/**
 * Created by xiaolin on 2018/9/27.
 */

public class AnimatedPieView extends View {

    public final String ATG = getClass().getSimpleName();
    private Paint paint1, paint2, paint3, paint4;
    private RectF mDrawRectf = new RectF();
    private Context mContext;

    public AnimatedPieView(Context context) {
        super(context);
        init(context);
    }

    public AnimatedPieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimatedPieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context mComtext) {
        if (paint1 == null) paint1 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(80);
        paint1.setColor(Color.RED);
        if (paint2 == null) paint2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(80);
        paint2.setColor(Color.GREEN);
        if (paint3 == null) paint3 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeWidth(80);
        paint3.setColor(Color.BLUE);
        if (paint4 == null) paint4 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setColor(Color.BLACK);
        paint4.setStrokeWidth(10);
        this.mContext = mComtext;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //TODO 一个控件在xml布局文件中定义宽高，居中等等，是在外面的容器一个定位，
        //TODO 而该view里面的内容不受影响，依然是在这个宽高区间内进行绘制
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float height = getHeight() - getPaddingBottom() - getPaddingTop();
        //TODO 如果不移动到这个view的中点开始绘制，有部分可能不在这个view的区域内
        canvas.drawPoint(0, 0, paint4);
        canvas.translate(width / 2, height / 2);
        canvas.drawPoint(0, 0, paint4);//canvas 平移到这个点
        canvas.drawLine(0, 0, width / 2, 0, paint4);
        canvas.drawLine(0, 0, 0, height / 2, paint4);
        float radius = (float) (Math.min(width, height) / 2 * 0.85);//255
        Log.i(ATG, "width= " + UIUtil.px2dip(mContext, width));
        Log.i(ATG, "height= " + UIUtil.px2dip(mContext, height));
        Log.i(ATG, "radius= " + UIUtil.px2dip(mContext, radius));
        //TODO 因为绘制是坐标起点是在这个控件的左上角作为原点坐标
//        mDrawRectf.set(0, 0, radius, radius);
        //-255 -255 就超过出了这个view的范围，看不见了
        mDrawRectf.set(-radius, -radius, radius, radius);
        canvas.drawRect(mDrawRectf, paint4);
        //startAngle  // 开始角度
        //sweepAngle  // 扫过角度
        //useCenter   // 是否使用中心
        //红点起点到绿点终点经过240°，剩余120°，共360°
        canvas.drawArc(mDrawRectf, 0, 120, false, paint1);
        canvas.drawArc(mDrawRectf, 120, 120, false, paint2);
        canvas.drawArc(mDrawRectf, 240, 120, false, paint3);
    }
}
