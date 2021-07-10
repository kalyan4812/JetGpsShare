package com.saikalyandaroju.jetgpsshare.BackgroundWorkers;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.saikalyandaroju.jetgpsshare.ActivityStatusActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;


public class PseudoWorker extends Worker {
    private Context context;
    private String api_upload_cord = "https://oakspro.com/projects/project36/kalyan/JGS/cord_upload_api.php";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private LocationManager locationManager;
    private String provider;


    private static final int UPDATE_INTERVAL = 5000;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location currentLocation;
    private final int LOCATION_PERMISSION = 100;


    public PseudoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        initPrfes();
        setUpLocationCallbacks(context);


    }

    private void setUpLocationCallbacks(Context context) {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if (locationAvailability.isLocationAvailable()) {

                } else {

                }
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {

                super.onLocationResult(locationResult);
            }
        };
    }

    private void initPrfes() {
        sharedPreferences = context.getSharedPreferences("MyUser", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    private void startGettingLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, context.getMainLooper());
            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.i("info", "getting location");
                    currentLocation = location;

                    if (location != null) {

                        String lat = location.getLatitude() + "";
                        String longi = location.getLongitude() + "";
                        String num = sharedPreferences.getString("mobile", null);
                        UploadCordServer(lat, longi, num);
                    }
                }


            });

            locationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    // Log.i(TAG, "Exception while getting the location: " + e.getMessage());
                }
            });


        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityStatusActivity.getinstanece(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, "Permission needed", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(ActivityStatusActivity.getinstanece(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION);
            }
        }


    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.i("info", "pass1");

            doGeoWork();


        } catch (Exception e) {
            Log.i("info", "fail" + e.getMessage());
            return Result.retry();
        }
        return Result.success();
    }


    private void doGeoWork() {


        startGettingLocation();
        Log.i("info", "pass");
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(PseudoWorker.class).
                setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()).setInitialDelay(2, TimeUnit.MINUTES).build();

        WorkManager.getInstance(context).enqueue(request);


    }


    @Override
    public void onStopped() {
        super.onStopped();
        locationProviderClient.removeLocationUpdates(locationCallback);
        Log.i("info", "WORK STOPPED");
    }

    private void UploadCordServer(final String latCord, final String longCord, final String mobile_user) {

        StringRequest uploadReq = new StringRequest(Request.Method.POST, api_upload_cord, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("info", "Sucessssssssssssss");
              /*  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(120000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        startGettingLocation();
                    }
                }).start();*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("info", "volleyerror");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> uploadCord = new HashMap<>();
                uploadCord.put("mobile", mobile_user);
                uploadCord.put("lat", latCord);
                uploadCord.put("long", longCord);
                return uploadCord;
            }
        };
        RequestQueue requestQueue_cord = Volley.newRequestQueue(context);
        requestQueue_cord.add(uploadReq);

    }
}
