package com.razerdp.demo2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.razerdp.animatedpieview.R;
import com.razerdp.demo2.lib.AnimatedPieView;
import com.razerdp.demo2.lib.AnimatedPieViewConfig;

import com.razerdp.demo2.lib.IPieInfo;
import com.razerdp.demo2.lib.OnPieSelectListener;
import com.razerdp.demo2.lib.SimplePieInfo;
import com.razerdp.demo2.view.TestAnimatedPieView;

import com.razerdp.widget.animatedpieview.utils.UIUtil;


import java.util.Random;

/**
 * Created by xiaolin on 2018/9/27.
 */

public class Demo2 extends AppCompatActivity {

    public final String ATG = getClass().getSimpleName();
    TestAnimatedPieView animatedPieView;
    AnimatedPieView libAnimatedPieView;
    private final Random random = new Random();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo2);
        animatedPieView = findViewById(R.id.animatedPieView);
        libAnimatedPieView = findViewById(R.id.libAnimatedPieView);


//        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
//        config.startAngle(-90)// 起始角度偏移
//                .addData(new SimplePieInfo(30, Color.parseColor("#FFC5FF8C"), "这是第一段"))//数据（实现IPieInfo接口的bean）
//                .addData(new SimplePieInfo(18.0f, Color.parseColor("#FFFFD28C"), "这是第二段"))
//                .duration(2000);// 持续时间
//        // 以下两句可以直接用 mAnimatedPieView.start(config); 解决，功能一致



        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(0.9224089f)
                /**
                 * not done below!
                 */
                .addData(new SimplePieInfo(0.11943538617599236, getColor("FF446767"))
                        .setLabel(resourceToBitmap(R.mipmap.ic_test_1))
                        .setDefaultSelected(true), false)

                .addData(new SimplePieInfo(0.41780274681129415, getColor("FFFFD28C"), "测试一下~")
                        .setLabel(resourceToBitmap(R.mipmap.ic_test_2)), false)

                .addData(new SimplePieInfo(0.722165651192247, getColor("FFbb76b4"))
                        .setLabel(resourceToBitmap(R.mipmap.ic_test_3)), true)

                .addData(new SimplePieInfo(0.9184314356136125, getColor("FFFFD28C"), "长文字test")
                        .setLabel(resourceToBitmap(R.mipmap.ic_test_4)), false)

                .addData(new SimplePieInfo(0.6028910840057398, getColor("ff2bbc80"))
                        .setLabel(resourceToBitmap(R.mipmap.ic_test_5)), true)

                .addData(new SimplePieInfo(0.6449620647212785, getColor("ff8be8ff")), true)
                .addData(new SimplePieInfo(0.058853315195452116, getColor("fffa734d")), true)
                .addData(new SimplePieInfo(0.6632297717331086, getColor("ff957de0")), true)
                .selectListener(new OnPieSelectListener() {
                    @Override
                    public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {
//                        desc.setText(String.format(Locale.getDefault(),
//                                "touch pie >>> {\n  value = %s;\n  color = %d;\n  desc = %s;\n  isFloatUp = %s;\n }",
//                                pieInfo.getValue(), pieInfo.getColor(), pieInfo.getDesc(), isFloatUp));
                    }
                })
                .drawText(true)
                .duration(1200)
                .textSize(26)
                .focusAlphaType(AnimatedPieViewConfig.FOCUS_WITH_ALPHA)
                .textGravity(AnimatedPieViewConfig.ABOVE)
                .interpolator(new DecelerateInterpolator());
        libAnimatedPieView.applyConfig(config);
//        libAnimatedPieView.start();


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int width = animatedPieView.getWidth();
        int height = animatedPieView.getHeight();
        //09-27 13:06:47.495 4928-4928/? I/Demo2: width= 200
        //09-27 13:06:47.495 4928-4928/? I/Demo2: height= 200
        Log.i(ATG, "width= " + UIUtil.px2dip(this, width));
        Log.i(ATG, "height= " + UIUtil.px2dip(this, height));
    }
    private Bitmap resourceToBitmap(int resid) {
        Drawable drawable = getResources().getDrawable(resid);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
            return bitmap;
        }
    }

    private int randomColor() {
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        return Color.argb(255, red, green, blue);
    }

    private int getColor(String colorStr) {
        if (TextUtils.isEmpty(colorStr)) return Color.BLACK;
        if (!colorStr.startsWith("#")) colorStr = "#" + colorStr;
        return Color.parseColor(colorStr);
    }
}
