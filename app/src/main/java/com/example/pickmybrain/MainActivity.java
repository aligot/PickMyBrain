package com.example.pickmybrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    ImageView imageView;
    ScaleAnimation scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaPlayer ring= MediaPlayer.create(getApplicationContext(),R.raw.ring);
        ring.start();
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.mov_image);
        scale = new ScaleAnimation(0, 2, 0, 2);
        scale.setDuration(500);
        imageView.startAnimation(scale);
        new Handler().postDelayed(() -> {
            ring.start();
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            startActivity(homeIntent);
        }, SPLASH_TIME_OUT);
    }
}