package com.razerdp.demo2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.razerdp.animatedpieview.R;
import com.razerdp.demo2.lib.AnimatedPieView;
import com.razerdp.demo2.lib.AnimatedPieViewConfig;

import com.razerdp.demo2.lib.SimplePieInfo;
import com.razerdp.demo2.view.TestAnimatedPieView;

import com.razerdp.widget.animatedpieview.utils.UIUtil;

/**
 * Created by xiaolin on 2018/9/27.
 */

public class Demo2 extends AppCompatActivity {

    public final String ATG = getClass().getSimpleName();
    TestAnimatedPieView animatedPieView;
    AnimatedPieView libAnimatedPieView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo2);
        animatedPieView = findViewById(R.id.animatedPieView);
        libAnimatedPieView = findViewById(R.id.libAnimatedPieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)// 起始角度偏移
                .addData(new SimplePieInfo(30, Color.parseColor("#FFC5FF8C"), "这是第一段"))//数据（实现IPieInfo接口的bean）
                .addData(new SimplePieInfo(18.0f, Color.parseColor("#FFFFD28C"), "这是第二段"))
                .duration(2000);// 持续时间
        // 以下两句可以直接用 mAnimatedPieView.start(config); 解决，功能一致
        libAnimatedPieView.applyConfig(config);
        libAnimatedPieView.start();


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
}
