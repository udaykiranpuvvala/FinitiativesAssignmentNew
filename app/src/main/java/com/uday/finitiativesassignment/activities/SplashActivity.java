package com.uday.finitiativesassignment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.uday.finitiativesassignment.R;

public class SplashActivity extends AppCompatActivity {
    Animation spinin;
    ImageView ivFlip;
    RelativeLayout relativeLayoutSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_splash);
            spinin = AnimationUtils.loadAnimation(this,
                    R.anim.fade_in_welcomesplash);
            // spinin.setDuration(1000);
            LayoutAnimationController controller = new LayoutAnimationController(
                    spinin);
            relativeLayoutSplash = findViewById(R.id.relativeLayoutSplash);


            relativeLayoutSplash.setLayoutAnimation(controller);


            spinin.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationEnd(Animation animation) {


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();



                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // Toast.makeText(WelcomeSplashActivity.this, "Animation repeat", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAnimationStart(Animation animation) {
                    // Toast.makeText(WelcomeSplashActivity.this, "Animation start", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
