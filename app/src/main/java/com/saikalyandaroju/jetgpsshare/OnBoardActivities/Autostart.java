package com.saikalyandaroju.jetgpsshare.OnBoardActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.saikalyandaroju.jetgpsshare.Dashboard;
import com.saikalyandaroju.jetgpsshare.R;
import com.saikalyandaroju.jetgpsshare.Utils.ChinesePermissionUtils;

public class Autostart extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView mytext;
    Button permission;
    ProgressBar mybar;
    LinearLayout mylayout;
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autostart);
        mytext = findViewById(R.id.autostart);
        mybar = findViewById(R.id.mybar);
        mylayout = findViewById(R.id.mylayout);
        sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        permission = (Button) findViewById(R.id.permission);
        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permission.getText().toString().equals(" START THE APP ")) {
                    //     mylayout.setBackground(null);

                    mybar.setVisibility(View.VISIBLE);
                    permission.setEnabled(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(Autostart.this, Dashboard.class);

                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    startActivity(i);


                                    finish();

                                }
                            });
                        }
                    }).start();


                } else {
                    boolean worked = false;

                    if (!sharedPreferences.getBoolean("autostart", false)) {
                        // boolean worked = false;
                        if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(getApplicationContext())) {
                            boolean b = AutoStartPermissionHelper.getInstance().getAutoStartPermission(getApplicationContext());
                            // Toast.makeText(Autostart.this, "Auto start" + b, Toast.LENGTH_SHORT).show();
                            if (b) {
                                editor.putBoolean("autostart", true).apply();
                                check = true;
                                return;
                                //worked = b;
                            }
                            worked = b;

                        }

                    }

                    //   myshowcase();
                    if (!sharedPreferences.getBoolean("autostart", false) && worked == false) {
                        ChinesePermissionUtils.GoToSetting(Autostart.this);
                        editor.putBoolean("autostart", true).apply();
                        check = true;


                    } else {
                        check = true;
                        permission.setText(" START THE APP ");
                        mytext.setVisibility(View.GONE);
                        mylayout.setBackground(null);

                    }


                }
                //  startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:" + getPackageName())));
                //  Intent intent = new Intent();
                //  intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                // intent.setData(Uri.parse("package:" + getPackageName()));
                //  startActivity(intent);
                // permission.setText(" START THE APP ");


            }


        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences.getBoolean("autostart", false)) {
            permission.setText(" START THE APP ");
            mytext.setVisibility(View.GONE);
            mylayout.setBackground(null);
        }

    }
}
