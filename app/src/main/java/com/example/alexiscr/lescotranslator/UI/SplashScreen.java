package com.example.alexiscr.lescotranslator.UI;

import android.os.Bundle;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Window;

import com.example.alexiscr.lescotranslator.Logic.Constants;
import com.example.alexiscr.lescotranslator.R;


public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_splash_screen);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
            Intent mainIntent = new Intent().setClass(
                    SplashScreen.this, RecorderActivity.class);
            startActivity(mainIntent);
            finish();
            }
        };
        new Timer().schedule(task, Constants.SPLASH_SCREEN_DELAY);
    }
}