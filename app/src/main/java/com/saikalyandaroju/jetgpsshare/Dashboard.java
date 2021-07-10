package com.saikalyandaroju.jetgpsshare;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;
import com.mapbox.mapboxsdk.maps.Style;
import com.saikalyandaroju.jetgpsshare.AuthActivites.Logout;
import com.saikalyandaroju.jetgpsshare.AuthActivites.RemoveAccount;
import com.saikalyandaroju.jetgpsshare.OnBoardActivities.PrivacyPolicyActivity;
import com.saikalyandaroju.jetgpsshare.OnBoardActivities.Usage;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class Dashboard extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    AdvanceDrawerLayout drawer;
    TextView profilename, profilenumber;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView profileimg;

    Uri uri;
    LocationManager lm;
    boolean gps_enabled = false;
    private InterstitialAd mInterstitialAd;
    Toolbar toolbar;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //  @RequiresApi(api = Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        fragmentManager = getSupportFragmentManager();
        sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        uri = Uri.parse("android.resource://com.saikalyandaroju.jetgpsshare/drawable/myworld");

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //    drawer = findViewById(R.id.drawer_layout);
        drawer = (AdvanceDrawerLayout) findViewById(R.id.drawer_layout);
        drawer.useCustomBehavior(GravityCompat.START); //assign custom behavior for "Left" drawer
        drawer.setRadius(GravityCompat.START, 25);//set end container's corner radius (dimension)
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        profilenumber = headerview.findViewById(R.id.profilemail);
        profilename = headerview.findViewById(R.id.profilename);
        profileimg = headerview.findViewById(R.id.imageView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                profilename.setText(sharedPreferences.getString("name", null));
                profilenumber.setText(sharedPreferences.getString("mobile", null));

                Picasso.get().load(sharedPreferences.getString("profileuri", uri.toString())).into(profileimg);

            }
        }, 400);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.getMenu().findItem(R.id.activity_status).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent i = new Intent(getApplicationContext(), ActivityStatusActivity.class);

                startActivity(i);

                return true;
            }
        });

        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //  item.setChecked(true);

                Intent i = new Intent(getApplicationContext(), Logout.class);
                startActivity(i);
                //   item.setChecked(false);
                return true;
            }
        });
        navigationView.getMenu().findItem(R.id.share_app).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);
                sharetoWhatsapp();
                item.setChecked(false);
                return true;
            }
        });
        navigationView.getMenu().findItem(R.id.remove_account).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);
                Intent i = new Intent(getApplicationContext(), RemoveAccount.class);
                startActivity(i);
                finish();
                item.setChecked(false);
                return true;
            }
        });
        navigationView.getMenu().findItem(R.id.rate_app).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                item.setChecked(false);
                return true;
            }
        });


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        profilename.setText(sharedPreferences.getString("name", null));
                        profilenumber.setText(sharedPreferences.getString("mobile", null));
                        //   profilenumber.setText("xxxxxxxxxx");
                        Picasso.get().load(sharedPreferences.getString("profileuri", uri.toString())).into(profileimg);

                    }
                }, 100);

            }
        });
        //  openPowerSettings();
        if (!isMyServiceRunning(Dashboard.this, ActivityStatusActivity.Myservice.class)) {
            Log.i("info", "notrunnig");
            if (ActivityStatusActivity.getinstanece() != null) {
                ActivityStatusActivity.getinstanece().stopfirstservice();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMobileAds();
        mInterstitialAd = new InterstitialAd(this);
        if (mInterstitialAd != null) {
            mInterstitialAd.setAdUnitId(getString(R.string.admob_id5));
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        if (adRequest != null) {
            mInterstitialAd.loadAd(adRequest);
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                if (mInterstitialAd != null) {
                    mInterstitialAd.loadAd(adRequest);
                }
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                // Code to be executed when the ad is displayed.
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
                // Code to be executed when the interstitial ad is closed.
            }

        });
    }

    // @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isMyServiceRunning(Activity activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("info", service.service.getClassName());
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences.getBoolean("first", false)) {

            ShowcaseConfig config = new ShowcaseConfig();
            config.setDelay(500); // half second between each showcase view

            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "my_id");

            sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
                @Override
                public void onShow(MaterialShowcaseView itemView, int position) {


                    if (position == 1) {

                        sharedPreferences.edit().putBoolean("first", false).apply();

                    }
                }
            });


            sequence.setConfig(config);


            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            //.setSkipText("SKIP")
                            .setTarget(toolbar)
                            .setTitleText("Please check your Activity status is turned ON/OFF,CLICK ON TOP LEFT ICON.")
                            .setDismissText("GOT IT")
                            .setContentText("If not enabled ,please turn on it ,to make your friends access your recent location.")
                            .withOvalShape()
                            .build()
            );

            sequence.addSequenceItem(
                    new MaterialShowcaseView.Builder(this)
                            .setTarget(toolbar)
                            .setDismissText("GOT IT")
                            .setTitleText("Change Style Of Map.")
                            .setContentText("To change please click on top right icon .")
                            .withCircleShape()
                            .build()
            );

            sequence.start();

        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        try {
            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.i("info", "exeception");
        }
        boolean check = gps_enabled;
        Log.i("on", check + "dash");
        if (sharedPreferences.getBoolean("firsttime", true) && gps_enabled) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityStatusActivity.getinstanece().stopfirstservice();
                    Log.i("info", Thread.currentThread().getName());
                    // ActivityStatusActivity.getinstanece().startfirstservice();
                    Intent serviceIntent = new Intent(getApplicationContext(), ActivityStatusActivity.Myservice.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    editor.putBoolean("firsttime", false).apply();
                    editor.putBoolean("checked", true).apply();
                    Log.i("track", "Dashboard");
                }
            }, 0);

        } else if (sharedPreferences.getBoolean("firsttime", true)) {
            //  editor.putBoolean("firsttime", false).apply();
            editor.putBoolean("checked", false).apply();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = fragmentManager.findFragmentById(R.id.nav_host_fragment);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }


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

    private void sharetoWhatsapp() {

        String app_link = "Please Download our JetGPSShare to find your friends \n" + "http://play.google.com/store/apps/details?id=" + getPackageName();

        try {
            //progressDialog.dismiss();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Jet Gps Share");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + app_link + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share App"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent i = new Intent(Dashboard.this, PrivacyPolicyActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.satellite_view) {


            sharedPreferences.edit().putString("view", Style.SATELLITE).apply();

            if (MapFragment.getmyinstance().getmymapboxinstance() != null) {
                if (MapFragment.getmyinstance().symbolManager != null) {
                    MapFragment.getmyinstance().symbolManager.deleteAll();
                }
                MapFragment.getmyinstance().getmymapboxinstance().clear();
                MapFragment.getmyinstance().onMapReady(MapFragment.getmyinstance().getmymapboxinstance());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }

        } else if (item.getItemId() == R.id.Street_View) {
            sharedPreferences.edit().putString("view", Style.MAPBOX_STREETS).apply();
            if (MapFragment.getmyinstance().getmymapboxinstance() != null) {
                if (MapFragment.getmyinstance().symbolManager != null) {
                    MapFragment.getmyinstance().symbolManager.deleteAll();
                }
                MapFragment.getmyinstance().getmymapboxinstance().clear();
                MapFragment.getmyinstance().onMapReady(MapFragment.getmyinstance().getmymapboxinstance());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        } else if (item.getItemId() == R.id.usage) {
            Intent i = new Intent(Dashboard.this, Usage.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
