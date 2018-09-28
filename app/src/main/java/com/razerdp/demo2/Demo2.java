package com.razerdp.demo2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.razerdp.animatedpieview.R;
import com.razerdp.demo2.view.TestAnimatedPieView;
import com.razerdp.widget.animatedpieview.utils.UIUtil;

/**
 * Created by xiaolin on 2018/9/27.
 */

public class Demo2 extends AppCompatActivity {

    public final String ATG = getClass().getSimpleName();

    TestAnimatedPieView animatedPieView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo2);
        animatedPieView = findViewById(R.id.animatedPieView);
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
