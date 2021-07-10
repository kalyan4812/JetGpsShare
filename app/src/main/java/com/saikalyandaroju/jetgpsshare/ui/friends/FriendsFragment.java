package com.saikalyandaroju.jetgpsshare.ui.friends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.saikalyandaroju.jetgpsshare.Pojos.Contacts;
import com.saikalyandaroju.jetgpsshare.db.FriendEntity;
import com.saikalyandaroju.jetgpsshare.db.MyDatabase;
import com.saikalyandaroju.jetgpsshare.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FriendsFragment extends Fragment {

    private Button addBtn;
    private RecyclerView friendsRecycler;
    private SharedPreferences sharedPreferences;
    private FriendAdapter friendAdapter;
    private MyDatabase mydb;
    private AdView adsv;
    private FrameLayout frameLayoutAds;
    public static List<Contacts> contactList;
    private SharedPreferences.Editor editor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_friends, container, false);
        mydb = Room.databaseBuilder(getContext(), MyDatabase.class, "friendsdb").fallbackToDestructiveMigration().build();
        addBtn = root.findViewById(R.id.add_btn);
        friendsRecycler = root.findViewById(R.id.recylerView32);

        sharedPreferences = (SharedPreferences) getContext().getSharedPreferences("MY_FRIENDS", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        contactList = new ArrayList<>();

        new ReadTask().execute();

        friendsRecycler.setHasFixedSize(true);
        friendsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        friendAdapter = new FriendAdapter(getContext(), contactList);
        friendsRecycler.setAdapter(friendAdapter);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddFriendsActivity.class);
                startActivity(i);
            }
        });

        frameLayoutAds = root.findViewById(R.id.ads_frame);


        adsv = new AdView(getContext());
        adsv.setAdUnitId(getString(R.string.admob_id1));

        frameLayoutAds.addView(adsv);
        loadMobileAds();
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSizenew = getSetSize();
        if (adsv != null && adSizenew != null && adRequest != null) {
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
                adsv.loadAd(adRequest);
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
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();


        ReadTask readTask = new ReadTask();
        readTask.execute();
        friendAdapter.notifyDataSetChanged();
    }

    public class ReadTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            contactList.clear();
            List<FriendEntity> friends = mydb.myDao().getFriends();
            for (FriendEntity f : friends) {
                Contacts contacts = new Contacts(f.getName(), f.getPhone());
                contactList.add(contacts);
            }
            //   friendAdapter.notifyDataSetChanged();

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            friendAdapter.notifyDataSetChanged();
        }
    }

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
        frameLayoutAds = null;

        // contactList=null;
    }
}

