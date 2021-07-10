package com.saikalyandaroju.jetgpsshare.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.saikalyandaroju.jetgpsshare.MapFragment;
import com.saikalyandaroju.jetgpsshare.R;

import java.util.Arrays;

public class HomeFragment extends Fragment {


    private BottomNavigationView bottomNavigationView;
    private AdView adsv;
    private FrameLayout frameLayoutAds;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        frameLayoutAds = root.findViewById(R.id.ads_frame);

        bottomNavigationView = root.findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new MapFragment());


        transaction.commit();


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getContext() != null) {
            adsv = new AdView(getContext());
        }
        if (adsv != null) {
            adsv.setAdUnitId(getString(R.string.admob_id2));
        }
        frameLayoutAds.addView(adsv);
        loadMobileAds();
        AdRequest adRequest = new AdRequest.Builder().build();
        if (getActivity() != null) {
            AdSize adSizenew = getSetSize();
            //   if(adSizenew!=null) {
            adsv.setAdSize(adSizenew);
            // }
        }
        if (adRequest != null) {
            adsv.loadAd(adRequest);
        }
        // loadMobileAds();
        adsv.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                frameLayoutAds.setVisibility(View.VISIBLE);

                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                if (adsv != null) {
                    assert adRequest != null;
                    adsv.loadAd(adRequest);
                    Log.i("debug", "loadagain");
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
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void openFragment(Fragment fragment) {

        return;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selected_fragment = null;

            switch (item.getItemId()) {

                case R.id.mapFragment:
                    openFragment(new MapFragment());
                    return true;
                case R.id.shareFragment:
                    // openFragment(new ShareFragment());
                    if (getContext() != null) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("SHARING LOCATION")
                                .setPositiveButton("SHARE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        MapFragment mapFragment = MapFragment.getmyinstance();
                                        mapFragment.sharemylocation();
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

                    return true;

            }

            return false;
        }
    };

    private void loadMobileAds() {
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
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

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float width = displayMetrics.widthPixels;
        float density = displayMetrics.density;
        int adwidth = (int) (width / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext(), adwidth);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adsv = null;
        //frameLayoutAds=null;
    }


}
