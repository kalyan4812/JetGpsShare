package com.saikalyandaroju.jetgpsshare;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.saikalyandaroju.jetgpsshare.BackgroundWorkers.PseudoWorker;
import com.saikalyandaroju.jetgpsshare.Utils.ChinesePermissionUtils;

import java.util.Arrays;
import java.util.Random;

import es.dmoral.toasty.Toasty;

import static com.saikalyandaroju.jetgpsshare.MyApplication.CHANNEL_ID;


public class ActivityStatusActivity extends AppCompatActivity {

    OneTimeWorkRequest oneTimeWorkRequest;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Intent serviceIntent;

    Switch myswitch;
    static ActivityStatusActivity activityStatusActivity = null;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    LocationManager lm;
    boolean worked = false;
    private AdView adsv;
    private FrameLayout frameLayoutAds;
    LinearLayout mylayout;
    TextView mytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        mylayout = findViewById(R.id.mylinear);
        mytext = findViewById(R.id.mytext);

        if (getApplicationContext() != null && !MyApplication.checkmyconnection(getApplicationContext())) {
            mylayout.setVisibility(View.GONE);
            mytext.setText("Please check your INTERNET connection.");
            Toasty.warning(this, "Please check your INTERNET connection.", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (getApplicationContext() != null && !MyApplication.isInternetAvailable()) {
            mylayout.setVisibility(View.GONE);
            mytext.setText("Please check your INTERNET connection.");
            Toasty.warning(getApplicationContext(), "NO PROPER INTERNET connection.", Toasty.LENGTH_SHORT).show();

            return;

        }
        activityStatusActivity = this;
        setTitle(getString(R.string.activitystatus));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myswitch = findViewById(R.id.myswitch);


        sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        frameLayoutAds = findViewById(R.id.ads_frame);


        adsv = new AdView(getApplicationContext());
        if (adsv != null) {
            adsv.setAdUnitId(getString(R.string.admob_id4));
        }
        frameLayoutAds.addView(adsv);
        loadMobileAds();
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSizenew = getSetSize();
        if (adRequest != null && adSizenew != null) {
            adsv.setAdSize(adSizenew);
            adsv.loadAd(adRequest);
        }

        adsv.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                if (adsv != null) {
                    adsv.loadAd(adRequest);
                }
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }


        if (!sharedPreferences.getBoolean("autostart", false)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission is Needed!!")
                    .setMessage("Please enable autostart permission to make location updates even in background.")
                    .setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(ActivityStatusActivity.getinstanece())) {
                                boolean b = AutoStartPermissionHelper.getInstance().getAutoStartPermission(ActivityStatusActivity.getinstanece());
                                Toast.makeText(ActivityStatusActivity.getinstanece(), "Auto start" + b, Toast.LENGTH_SHORT).show();
                                if (b) {
                                    editor.putBoolean("autostart", true).apply();
                                    worked = b;
                                }

                            }
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).setIcon(R.drawable.mylogo)
                    .show();

        }

        if (!sharedPreferences.getBoolean("autostart", false) && worked == false) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Neeed!!")
                    .setMessage("Please enable autostart permission to make location updates even in background.")
                    .setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            ChinesePermissionUtils.GoToSetting(ActivityStatusActivity.getinstanece());
                            editor.putBoolean("autostart", true).apply();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).setIcon(R.drawable.mylogo)
                    .show();
        }

        if (!sharedPreferences.getBoolean("checked", false) && sharedPreferences.getBoolean("firsttime", true)) {
            if (gps_enabled) {
                stopmyservice();
                startmyservice();
                editor.putBoolean("firsttime", false).apply();
                editor.putBoolean("checked", true).apply();
                Log.i("track", "Activitystaus");
            } else {
                openDialog();
                editor.putBoolean("firsttime", false).apply();
                //   editor.putBoolean("checked", false).apply();
                // myswitch.setChecked(false);
            }
        }
        serviceIntent = new Intent(this, Myservice.class);
        oneTimeWorkRequest = new OneTimeWorkRequest.Builder(PseudoWorker.class).build();
        if (isMyServiceRunning(ActivityStatusActivity.this, Myservice.class)) { // Service class name
            // Service running
            Log.i("info", "SERVICE RUNNING");
            editor.putBoolean("checked", true).commit();


        } else {
            // Service Stop
            Log.i("info", "SERVICE STOPPED");
            editor.putBoolean("checked", false).commit();
            WorkManager.getInstance(getApplicationContext()).cancelAllWork();
        }
        if (sharedPreferences.getBoolean("checked", false)) {
            myswitch.setChecked(true);

        } else {
            myswitch.setChecked(false);
            // stopmyservice();
        }
        if (getIntent() != null && getIntent().getBooleanExtra("state", false)) {
            myswitch.setChecked(true);
            editor.putBoolean("checked", true).commit();
        }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean grant = false;
                try {
                    grant = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (isChecked) {


                    if (grant) {
                        editor.putBoolean("checked", true).apply();
                        editor.putBoolean("firsttime", false).apply();
                        startmyservice();
                    } else {
                        openDialog();
                        myswitch.setChecked(false);
                    }
                } else {
                    editor.putBoolean("checked", false).apply();
                    stopmyservice();
                }

            }
        });
        //  serviceIntent = new Intent(this, Myservice.class);
        //   oneTimeWorkRequest = new OneTimeWorkRequest.Builder(PseudoWorker.class).build();


    }

    public static boolean isMyServiceRunning(Activity activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static class Myservice extends Service {


        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            Log.i("info", "created.....");

            // ContextCompat.startForegroundService(getApplicationContext(), new Intent(getApplicationContext(), Myservice.class));

        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Random random = new Random();


            int randomNumber_temp = random.nextInt(999999);
            if (randomNumber_temp == 0) {
                randomNumber_temp = 48;
            }
            Uri ringtonepath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent intents = new Intent(getApplicationContext(), ActivityStatusActivity.class);
            intents.putExtra("state", true);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intents, 0);
            Notification builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.mylogo)
                    .setOngoing(true)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mylogo))
                    //.setContentTitle("Background Service")
                    .setContentTitle("JetGpsShare is Updating Your Location")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    // .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_clear_black_24dp, "CANCEL", pendingIntent)
                    .setAutoCancel(false)
                    .setDefaults(NotificationCompat.DEFAULT_ALL).setSound(ringtonepath).build();
            builder.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_ONGOING_EVENT;


            startForeground(randomNumber_temp, builder);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(PseudoWorker.class).build();
                    WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);
                }
            }).start();
            // OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(PseudoWorker.class).build();
            //   WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);


            // WorkManager.getInstance(getApplicationContext()).enqueue(ActivityStatusActivity.getinstanece().oneTimeWorkRequest);

            return START_STICKY;
        }

        @Override
        public void onDestroy() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true);
            } else {
                stopSelf();
            }
            Log.i("info", "DESTROYED");
            SharedPreferences sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("checked", false).commit();
            WorkManager.getInstance(getApplicationContext()).cancelAllWork();
            super.onDestroy();
        }

        @Override
        public void onTaskRemoved(Intent rootIntent) {
            Log.i("info", "TASKREMOVED");
            super.onTaskRemoved(rootIntent);
            if (rootIntent != null && rootIntent.getBooleanExtra("state", false)) {
                Log.i("info", "TASKREMOVED1");
            } else if (rootIntent != null) {
                Log.i("info", "TASKREMOVED2");
                SharedPreferences sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("checked", false).apply();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("checked", false).apply();
            }
          /*  SharedPreferences sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("checked", false).commit();*/
        }

        public Myservice() {
            super();
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void startmyservice() {
        stopmyservice();

        ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
      /*  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }*/
    }


    public void stopmyservice() {
        WorkManager.getInstance(getApplicationContext()).cancelAllWork();
        stopService(serviceIntent);


    }

    public void startfirstservice() {
        stopfirstservice();
        Intent serviceIntent = new Intent(ActivityStatusActivity.this, Myservice.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ActivityStatusActivity.this.startForegroundService(serviceIntent);
        } else {
            ActivityStatusActivity.this.startService(serviceIntent);
        }
       /* oneTimeWorkRequest = new OneTimeWorkRequest.Builder(PseudoWorker.class).build();
        WorkManager.getInstance(ActivityStatusActivity.this).enqueue(oneTimeWorkRequest);*/
    }

    public void stopfirstservice() {
        WorkManager.getInstance(ActivityStatusActivity.this).cancelAllWork();
    }

    public void openDialog() {
        new AlertDialog.Builder(ActivityStatusActivity.this)
                .setTitle("Permission is Needed!!")
                .setMessage("GPS NOT ENABLED")
                .setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));


                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myswitch.setChecked(false);
                        editor.putBoolean("checked", false).apply();
                        dialog.dismiss();
                    }
                }).setCancelable(false).setIcon(R.drawable.mylogo)
                .show();
    }

    public static ActivityStatusActivity getinstanece() {
        if (activityStatusActivity == null) {
            activityStatusActivity = new ActivityStatusActivity();
        }
        return activityStatusActivity;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // activityStatusActivity = null;
        frameLayoutAds = null;
        adsv = null;

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(i);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(i);
            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMobileAds() {
        MobileAds.initialize(getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("your device id should go here")).build();
        MobileAds.setRequestConfiguration(configuration);
    }

    private void getAdBanners() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSizenew = getSetSize();
        adsv.setAdSize(adSizenew);
        adsv.loadAd(adRequest);
    }

    private AdSize getSetSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float width = displayMetrics.widthPixels;
        float density = displayMetrics.density;
        int adwidth = (int) (width / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getApplicationContext(), adwidth);
    }
}
