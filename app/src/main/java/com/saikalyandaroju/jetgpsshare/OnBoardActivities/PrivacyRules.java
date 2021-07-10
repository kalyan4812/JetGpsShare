package com.saikalyandaroju.jetgpsshare.OnBoardActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.saikalyandaroju.jetgpsshare.R;

public class PrivacyRules extends AppCompatActivity {
    TextView mylink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_rules);
        mylink = findViewById(R.id.mylink);
        mylink.setMovementMethod(LinkMovementMethod.getInstance());
        mylink.setLinkTextColor(Color.WHITE);
    }


    public void openintent(View view) {
        Intent i = new Intent(PrivacyRules.this, Autostart.class);
        startActivity(i);
        finish();
    }

}
