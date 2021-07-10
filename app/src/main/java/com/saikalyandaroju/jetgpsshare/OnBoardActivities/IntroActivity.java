package com.saikalyandaroju.jetgpsshare.OnBoardActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;
import com.saikalyandaroju.jetgpsshare.R;

public class IntroActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder().setTitle("JET GPS SHARE")
                .setContent("WELCOME !!!")
                .setBackgroundColor(Color.parseColor("#a1c4fd")) // int background color
                .setDrawable(R.drawable.myworld) // int top drawable
                .build());
        // Permission Step

        addFragment(new Step.Builder().setTitle("JET GPS SHARE")
                .setContent("THIS APP NEEDS CONTACTS,LOCATION PERMISSION.")
                .setBackgroundColor(Color.parseColor("#a1c4fd")) // int background color
                .setDrawable(R.drawable.myworld) // int top drawable
                .setSummary("Please give permission for working of the app.")
                .build());
        /*addFragment(new Step.Builder().setTitle("JET GPS SHARE")
                .setContent("If you want this app to work ,i.e update your location even when you are not using the app.")
                .setBackgroundColor(Color.parseColor("#a1c4fd")) // int background color
                .setDrawable(R.drawable.myworld) // int top drawable
                .setSummary("You have to give two more permissions .")
                .build());*/
    }

    @Override
    public void currentFragmentPosition(int position) {

    }

    @Override
    public void finishTutorial() {

        Intent i = new Intent(IntroActivity.this, PrivacyRules.class);

        startActivity(i);
        finish();


    }
}
