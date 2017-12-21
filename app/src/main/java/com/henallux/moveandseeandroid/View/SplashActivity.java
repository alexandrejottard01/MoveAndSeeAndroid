package com.henallux.moveandseeandroid.View;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.henallux.moveandseeandroid.R;

/**
 * Created by Alexandre on 05-12-17.
 */

public class SplashActivity extends AppCompatActivity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StartAnimations();


        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2400);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent intentToLogin = new Intent(SplashActivity.this, HomeNotConnectedActivity.class);
                    startActivity(intentToLogin);
                    finish();
                }
            }
        };
        timer.start();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l = findViewById(R.id.relative);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = findViewById(R.id.logo_move_and_see);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }
}
