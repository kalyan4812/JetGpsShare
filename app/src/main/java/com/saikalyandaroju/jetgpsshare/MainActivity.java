package com.saikalyandaroju.jetgpsshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.saikalyandaroju.jetgpsshare.AuthActivites.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static int SCREEN;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean loginV;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        lottieAnimationView = findViewById(R.id.globeanimation);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        loginV = sharedPreferences.getBoolean("loginS", false);

        if (!loginV) {
            SCREEN = 5000;
        } else {
            SCREEN = 6000;
        }
        new CustomHandler().postDelayed(new CustomRunnable(), SCREEN);


    }

    public static class CustomHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    }

    private class CustomRunnable implements Runnable {
        @Override
        public void run() {
            if (loginV) {

                Intent intent = new Intent(MainActivity.this, Dashboard.class);

                startActivity(intent);
                finish();
            } else {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);


                startActivity(intent);
                //overridePendingTransition(0,0);
                finish();


            }

        }
    }
}
