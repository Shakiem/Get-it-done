package com.shakiemsaunders.get_it_done.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.shakiemsaunders.get_it_done.R;


public class SplashActivity extends Activity {

    TextView appNametxtview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appNametxtview = (TextView) findViewById(R.id.appNameTxtView);

        Thread textAnimator = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

                animateText();
            }
        };

        Thread splashTimer= new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    finish();
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                }
            }
        };

        textAnimator.start();
        splashTimer.start();

    }

    private void animateText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                appNametxtview.setVisibility(View.VISIBLE);
                appNametxtview.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.slide_in_left));

            }
        });
    }
}
