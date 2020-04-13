package com.example.simpleanimation;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Declares variables
    Button fadeButton, moveButton;
    ImageView wifiImage, smileyImage, patternImage, paradise, runningBird;
    AnimationDrawable wifiAnimation, smileyAnimation;
    ObjectAnimator birdAnimation;
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://developer.android.com/guide/topics/graphics/drawable-animation
        wifiImage = (ImageView) findViewById(R.id.image);
        wifiImage.setBackgroundResource(R.drawable.animation);
        wifiAnimation = (AnimationDrawable) wifiImage.getBackground();

        smileyImage = (ImageView) findViewById(R.id.image1);
        smileyImage.setBackgroundResource(R.drawable.animation2);
        smileyAnimation = (AnimationDrawable) smileyImage.getBackground();

        //https://developer.android.com/guide/topics/graphics/view-animation?hl=tr
        patternImage = (ImageView) findViewById(R.id.image2);
        rotateAnimation();

        paradise = (ImageView) findViewById(R.id.image3);

        runningBird = (ImageView) findViewById(R.id.image4);
        moveButton = (Button) findViewById(R.id.btn2);

        birdAnimation = ObjectAnimator.ofFloat(runningBird, "translationX", -700f);

        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //4000 means 3 seconds
                birdAnimation.setDuration(3000);
                birdAnimation.start();

            }
        });
    }


    private void rotateAnimation() {
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        patternImage.startAnimation(rotateAnimation);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        wifiAnimation.start();
        smileyAnimation.start();
    }

    public void fadeClick(View view) {
        paradise.setAlpha(0f);
        paradise.setVisibility(View.VISIBLE);
        paradise.animate().alpha(1f).setDuration(5000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                paradise.setVisibility(View.GONE);
            }
        });
    }
}

