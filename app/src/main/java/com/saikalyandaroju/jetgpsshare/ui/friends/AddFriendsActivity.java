package com.saikalyandaroju.jetgpsshare.ui.friends;

import android.Manifest;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.saikalyandaroju.jetgpsshare.Pojos.Contacts;
import com.saikalyandaroju.jetgpsshare.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AddFriendsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView recyclerView123;
    List<Contacts> contactsList;
    MyAddFriendAdapter myAddFriendAdapter;
    SharedPreferences sharedPreferences;
    public static AddFriendsActivity addFriendsActivity;
    ShimmerFrameLayout shimmerFrameLayout;
    MenuItem item;
    TextView contactspermission;
    Button enable;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        addFriendsActivity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = (SharedPreferences) getSharedPreferences("MY_FRIENDS", MODE_PRIVATE);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        recyclerView123 = findViewById(R.id.recycler123);
        recyclerView123.setHasFixedSize(true);
        recyclerView123.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        contactsList = new ArrayList<>();
        myAddFriendAdapter = new MyAddFriendAdapter(getApplicationContext(), contactsList);
        recyclerView123.setAdapter(myAddFriendAdapter);
        contactspermission = findViewById(R.id.contactpermission);
        enable = findViewById(R.id.enable);

        loadMobileAds();
        mInterstitialAd = new InterstitialAd(this);
        if (mInterstitialAd != null) {
            mInterstitialAd.setAdUnitId(getString(R.string.admob_id3));
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

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactspermission.setVisibility(View.GONE);
                enable.setVisibility(View.GONE);
                shimmerFrameLayout.startShimmer();
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                onStart();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_CONTACTS).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (permissionGrantedResponse.getPermissionName().equals(Manifest.permission.READ_CONTACTS)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getContacts();
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerView123.setVisibility(View.VISIBLE);
                            if (item != null) {
                                item.setVisible(true);
                            }
                        }
                    }, 3000);


                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                Toasty.warning(getApplicationContext(), "PERMISSION IS NEEDED ", Toast.LENGTH_SHORT, true).show();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                contactspermission.setVisibility(View.VISIBLE);
                enable.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
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

    private void getContacts() {


        Map<String, Contacts> mycontacts = new HashMap<>();
        mycontacts.clear();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null
                , null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenum = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contacts contacts = new Contacts(name, phonenum);
            if (!sharedPreferences.getBoolean(phonenum, false)) {
                if (!mycontacts.containsKey(contacts.getPhone())) {
                    mycontacts.put(contacts.getPhone(), contacts);
                }
            }

        }

        contactsList.addAll(mycontacts.values());
        myAddFriendAdapter.notifyDataSetChanged();


    }

    //  SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_friend, menu);
        item = menu.findItem(R.id.action_search);
        item.setVisible(false);


        SearchView searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) AddFriendsActivity.this);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume() {
        super.onResume();

        myAddFriendAdapter.notifyDataSetChanged();
    }

    public static AddFriendsActivity getmyaddfriendinstance() {
        // WeakReference<AddFriendsActivity> addFriendsActivity;
        if (addFriendsActivity == null) {
            addFriendsActivity = new AddFriendsActivity();
        }
        return addFriendsActivity;
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.startShimmer();

        myAddFriendAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        shimmerFrameLayout.stopShimmer();
        myAddFriendAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("katsssss", "  " + newText);
        if (TextUtils.isEmpty(newText)) {
            myAddFriendAdapter.getFilter().filter("");
        } else {
            myAddFriendAdapter.getFilter().filter(newText);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            super.onBackPressed();
            finish();
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addFriendsActivity = null;
    }
}
