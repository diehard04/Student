package com.mtx.mobile.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity  extends Activity implements Animation.AnimationListener {
    static Context context;
    Animation animFadeIn;
    ImageView iv;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = SplashActivity.this;
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //----------------My app Tracker-----start
        //   Tracker t = ((GoogleAnalyticsAppTracker) getApplication()).getTracker(GoogleAnalyticsAppTracker.TrackerName.APP_TRACKER);
        //   t.setScreenName("SplashScreen");
        //  t.send(new HitBuilders.ScreenViewBuilder().build());
        //------------------My App Tracker -----end

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
        }
        // load the animation
        animFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        // set animation listener
        animFadeIn.setAnimationListener(this);

        // animation for image
        iv = findViewById(R.id.imageView1);
        // start the animation

        iv.setVisibility(View.VISIBLE);
        iv.startAnimation(animFadeIn);
    }


    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    public void onAnimationEnd(Animation animation) {
        Intent i = new Intent(context, RegisterActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
