package com.example.simpleanimation;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Declares variables
    Button wildButton, moveButton;
    ImageView wifiImage, smileyImage, patternImage, paradise, runningBird, butterfly;
    AnimationDrawable wifiAnimation, smileyAnimation;
    ObjectAnimator birdAnimation;
    Animation rotateAnimation, blinkAnimation;

    //Variables for zoom method
    private Animator currentAnimator;
    private int shortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Animate drawable graphics
         * https://developer.android.com/guide/topics/graphics/drawable-animation
         */
        wifiImage = (ImageView) findViewById(R.id.image);
        wifiImage.setBackgroundResource(R.drawable.animation);
        wifiAnimation = (AnimationDrawable) wifiImage.getBackground();

        /*
         * Android Studio cannot resolve the start() method - it appears as red and does not work
        wifiImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiImage.start();
            }
        });
        */

        smileyImage = (ImageView) findViewById(R.id.image1);
        smileyImage.setBackgroundResource(R.drawable.animation2);
        smileyAnimation = (AnimationDrawable) smileyImage.getBackground();


        /*
         * View animation
         * https://developer.android.com/guide/topics/graphics/view-animation?hl=tr
         */
        patternImage = (ImageView) findViewById(R.id.image2);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        patternImage.startAnimation(rotateAnimation);

        butterfly = (ImageView) findViewById(R.id.image6);
        blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        butterfly.startAnimation(blinkAnimation);


        // Binds the XML image with java object using findViewById function
        paradise = (ImageView) findViewById(R.id.image3);
        runningBird = (ImageView) findViewById(R.id.image4);

        // Buttons for view animation
        moveButton = (Button) findViewById(R.id.btn2);
        wildButton = (Button) findViewById(R.id.btn3);

        // Runningbird image for view animation
        birdAnimation = ObjectAnimator.ofFloat(runningBird, "translationX", -300f);

        // Setting on click listener for the button
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //1000 means 1 seconds
                birdAnimation.setDuration(1000);
                birdAnimation.start();

            }
        });

        // Setting on click listener for the button
        wildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.mixed_anim);
                wildButton.startAnimation(animation);
            }
        });


        /*
         * Zoom animation set up
         * Hook up clicks on the thumbnail views.
         */
        final View thumb1View = findViewById(R.id.thumb_button_1);
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomImageFromThumb(thumb1View, R.drawable.birds_flying);
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }


    /*
     * Enlarge a view using zoom
     * https://developer.android.com/training/animation/zoom
     */
    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.image5);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }


    /*
     * Animate drawable graphics
     * https://developer.android.com/guide/topics/graphics/drawable-animation
     * https://developer.android.com/reference/android/view/ViewTreeObserver.OnWindowFocusChangeListener
     */

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        wifiAnimation.start();
        smileyAnimation.start();
    }

    /*
     * Reveal or hide a view using animation
     * https://developer.android.com/training/animation/reveal-or-hide-view
     */

    public void fadeClick(View view) {
        // Set the image paradise to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        paradise.setAlpha(0f);
        paradise.setVisibility(View.VISIBLE);
        // Animate the image paradise to 100% opacity, and clear any animation
        // After the animation ends, set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        paradise.animate().alpha(1f).setDuration(5000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                paradise.setVisibility(View.GONE);
            }
        });
    }
}

