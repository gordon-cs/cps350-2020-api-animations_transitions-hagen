package com.example.simpleanimation;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {

    //Declares variables
    Button fadeButton;
    ImageView wifiImage, smileyImage, patternImage;
    AnimationDrawable wifiAnimation, smileyAnimation;

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
}

