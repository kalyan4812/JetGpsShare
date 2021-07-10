package com.saikalyandaroju.jetgpsshare.AuthActivites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.saikalyandaroju.jetgpsshare.ActivityStatusActivity;
import com.saikalyandaroju.jetgpsshare.R;

public class Logout extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        textView = findViewById(R.id.logout);
        sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        lottieAnimationView = findViewById(R.id.animationView);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        new Thread(new CustomRunnable()).start();

    }

    private class CustomRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            editor.putBoolean("loginS", false);
            editor.putBoolean("firsttime", false);

            editor.apply();
            try {
                ActivityStatusActivity.getinstanece().stopmyservice();
            } catch (Exception e) {

            }

            Intent intent = new Intent(Logout.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
