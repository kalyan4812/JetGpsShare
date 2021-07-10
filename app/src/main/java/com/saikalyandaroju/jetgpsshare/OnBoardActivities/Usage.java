package com.saikalyandaroju.jetgpsshare.OnBoardActivities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.saikalyandaroju.jetgpsshare.R;

public class Usage extends AppCompatActivity {
    TextView mytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);
        mytext = findViewById(R.id.mytext);
        setTitle("Usage Of App");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
