package it.uniba.di.easyhome;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        AnimatedVectorDrawable animatedVectorDrawable =
                (AnimatedVectorDrawable) getDrawable(R.drawable.avd_anim);
        imageView.setImageDrawable(animatedVectorDrawable);
        animatedVectorDrawable.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

               Intent i = new Intent(SplashScreenActivity.this,LoginActivity.class);
                startActivity(i);
                finish();

            }
        }, 4000);

}}
