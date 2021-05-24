package com.gcoen.navgo;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SplashActivity extends Activity {
    ImageView imageview ;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageview = findViewById(R.id.logo);
        onWindowFocusChanged(true);
        int SPLASH_SCREEN_TIME_OUT = 2000;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {

                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                finish();

            }
        }, SPLASH_SCREEN_TIME_OUT);

    }
    boolean hasAnimationStarted;

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !hasAnimationStarted) {
            hasAnimationStarted=true;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            ObjectAnimator translationY = ObjectAnimator.ofFloat(imageview, "y",    metrics.heightPixels / 2 - imageview.getHeight() / 2 ); // metrics.heightPixels or root.getHeight()
            translationY.setDuration(2500);
            translationY.start();
        }
    }

}
