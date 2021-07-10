package com.saikalyandaroju.jetgpsshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.motion.widget.MotionScene.TAG;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textOffset;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        MapboxMap.OnMapClickListener, PermissionsListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;
    private LottieAnimationView lottieAnimationView;
    static MapFragment mapFragment;
    private static Point orgincopy, destinationcopy;

    private static final String SHOWCASE_ID = "sequence example";
    private SharedPreferences sharedPreferences;
    private Button startnavigation;

    private String api_call_check = "https://oakspro.com/projects/project36/kalyan/JGS/check_friends.php";
    private ProgressDialog progressDialog;
    private Style styles;
    private SymbolLayer destinationSymbolLayer;
    private FloatingActionButton fab, mclear, refersh;
    private Point destinationPoint, originPoint;
    private ArrayAdapter<String> arrayAdapter;

    private MapboxOptimization optimizedClient;
    private GeoJsonSource source;
    private String friendmobilenum;


    SymbolManager symbolManager;
    private Feature originFeature, destinationFeature;
    private Symbol symbol;
    private List<Address> adr = new ArrayList<>();
    private DirectionsRoute optimizedroute;
    private boolean gps_enabled, network_enabled;
    boolean ok = false;
    private LocationManager lm;
    private AutoCompleteTextView friendmobile;
    private ArrayList<String> mynames = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Mapbox.getInstance(getContext(), "sk.eyJ1Ijoia2FseWFuNDQ0NCIsImEiOiJja2sydWpqMTIxNTJkMm5wY25pbHo1NXJtIn0.VYJ_eEeC2N343PxwB458GA");
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) root.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        lottieAnimationView = root.findViewById(R.id.maploading);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);


        startnavigation = root.findViewById(R.id.startnavigation);
        startnavigation.setClickable(false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setIcon(R.drawable.world);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        fab = root.findViewById(R.id.fab);
        startnavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNavigationBtnClick(v);
            }
        });
        refersh = root.findViewById(R.id.refresh);
        refersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                refersh.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_fab_icon));

                progressDialog.show();

                if (mapboxMap != null && styles != null) {
                    mapboxMap.clear();
                    enableLocationComponent(styles);


                    sendnumbertoserver(friendmobilenum, sharedPreferences.getString("mobile", null));

                }
                if (getContext() != null) {
                    Toasty.info(getContext(), "REFRESHING...", Toast.LENGTH_SHORT, false).show();
                }
                progressDialog.dismiss();

            }

        });

        mclear = root.findViewById(R.id.clear);
        mclear.setVisibility(View.GONE);
        mclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mapboxMap != null && styles != null && destinationSymbolLayer != null) {
                    if (getContext() != null) {
                        Toasty.info(getContext(), "SCREEN CLEARED", Toast.LENGTH_SHORT, false).show();
                    }
                    styles.removeLayer(destinationSymbolLayer);
                    onMapReady(mapboxMap);
                    mapboxMap.clear();
                    if (symbolManager != null && symbol != null) {
                        symbolManager.delete(symbol);
                        symbolManager.deleteAll();
                    }

                    destinationcopy = null;

                } else {
                    if (getContext() != null) {
                        Toasty.info(getContext(), "SCREEN CLEARED", Toast.LENGTH_SHORT, false).show();
                    }
                    startnavigation.setClickable(false);
                    startnavigation.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    return;
                }


                startnavigation.setClickable(false);
                startnavigation.setVisibility(View.GONE);
                refersh.setVisibility(View.GONE);
                mclear.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() == null) {
                    return;
                }
                final Dialog d = new Dialog(getActivity());


                d.setTitle("MESSAGE");
                d.setContentView(R.layout.customdialoglayout);
                d.setCancelable(false);
                final Button getlocation = d.findViewById(R.id.getlocation);
                final Button cancel = d.findViewById(R.id.cancel);
                friendmobile = d.findViewById(R.id.friendmobile);


                if (mynames != null && arrayAdapter != null) {
                    arrayAdapter.notifyDataSetChanged();
                    friendmobile.setAdapter(arrayAdapter);
                } else {
                    friendmobile.setHint("ENTER MOBILE NUMBER");

                }


                friendmobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String text = parent.getItemAtPosition(position).toString();
                        if (text.length() >= 10) {
                            friendmobile.setText(text.substring(text.length() - 10));
                        } else {

                            friendmobile.setText("");
                        }
                    }
                });
                getlocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (friendmobile.getText().toString().length() >= 10 && friendmobile.getText().toString().matches("[0-9]+")) {
                            friendmobilenum = friendmobile.getText().toString().substring(friendmobile.length() - 10);
                        } else {
                            if (getContext() != null) {
                                Toasty.warning(getContext(), "Please Enter a 10 digit Number ", Toasty.LENGTH_SHORT).show();
                            }
                            return;
                        }


                        sendnumbertoserver(friendmobilenum, sharedPreferences.getString("mobile", null));

                        d.dismiss();

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        d.dismiss();

                    }
                });
                try {
                    d.show();
                } catch (Exception e) {
                    Log.i("info", "exeception");
                }
            }
        });


        presentShowcaseSequence();


        mapFragment = this;

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


            try {

                assert lm != null;
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                Log.i("info", "exeception");
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                Log.i("info", "exeception");
            }
        }
        if (!gps_enabled && !network_enabled && !sharedPreferences.getBoolean("firsttime", true) && fab != null) {
            fab.setVisibility(View.GONE);
            // notify user
            if (getContext() != null) {
                new AlertDialog.Builder(getContext())
                        .setMessage("PLEASE ENABLE GPS !")
                        .setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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
        }
        //Log.i()
        if (sharedPreferences.getBoolean("firsttime", true)) {
            if (getContext() != null) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Please make sure your activity status is turned ON")
                        .setMessage("To make your friends to find your recent location.")
                        .setPositiveButton("Check", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                startActivity(new Intent(getContext(), ActivityStatusActivity.class));
                                getActivity().finish();
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
            Log.i("track", "Map Fragment");
        }
    }

    public static MapFragment getmyinstance() {
        if (mapFragment == null) {
            mapFragment = new MapFragment();
        }
        return mapFragment;
    }


    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);

        sequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            @Override
            public void onShow(MaterialShowcaseView itemView, int position) {


                if (position == 0) {

                    mclear.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);

                } else if (position == 1) {
                    mclear.setVisibility(View.GONE);
                    refersh.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    refersh.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    sharedPreferences.edit().putBoolean("first", true).apply();

                }
            }
        });


        sequence.setConfig(config);


        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        //.setSkipText("SKIP")
                        .setTarget(mclear)
                        .setDismissText("GOT IT")
                        .setContentText("To Refresh  the Map")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(getActivity())
                        .setTarget(refersh)
                        .setDismissText("GOT IT")
                        .setContentText("To refresh destination of your friend.")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(fab,
                "Find Your Friends by search using Name or Mobile.", "GOT IT");


        sequence.start();

    }

    private void sendnumbertoserver(final String friendmobilenum, final String userMobile) {
        progressDialog.show();
        if (getContext() != null && !MyApplication.checkmyconnection(getContext())) {
            Toasty.warning(getContext(), "Please check your INTERNET connection.", Toasty.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        if (getContext() != null && !MyApplication.isInternetAvailable()) {
            Toasty.warning(getContext(), "NO PROPER INTERNET CONNECTION.", Toasty.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;

        }
        StringRequest request = new StringRequest(Request.Method.POST, api_call_check, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    String friend_status = jsonObject.getString("friend_status");


                    if (status.equals("success")) {

                        if (friend_status.equals("1")) {
                            if (getContext() != null) {
                                Toasty.success(getContext(), "Found YOUR Friend....", Toast.LENGTH_SHORT, false).show();
                            }
                            String friend_lat_cord = jsonObject.getString("lat_cord");
                            String friend_long_cord = jsonObject.getString("long_cord");
                            Toast.makeText(getContext(), "Lat: " + friend_lat_cord + " Long: " + friend_long_cord, Toast.LENGTH_SHORT).show();
                            Double lats = Double.valueOf(friend_lat_cord);
                            Double longs = Double.valueOf(friend_long_cord);
                            LatLng latLng = new LatLng(lats, longs);
                            progressDialog.dismiss();

                            if (lats == 0.0 && longs == 0.0) {
                                if (getContext() != null) {
                                    Toasty.info(getContext(), "User hasn't enabled activity status yet.", Toasty.LENGTH_SHORT, true).show();
                                }
                                return;
                            }
                            onMapClick(latLng);


                            startnavigation.setVisibility(View.VISIBLE);
                            startnavigation.setClickable(true);
                            fab.setVisibility(View.GONE);
                            refersh.setVisibility(View.VISIBLE);
                            refersh.setClickable(true);
                            mclear.setVisibility(View.VISIBLE);
                            mclear.setClickable(true);


                        } else {
                            if (getContext() != null) {
                                Toasty.warning(getContext(), "You are not allowed to access this user location.", Toast.LENGTH_SHORT, true).show();
                            }
                            progressDialog.dismiss();
                            //  lottieAnimationView.setVisibility(View.GONE);
                        }


                    } else {
                        if (getContext() != null) {
                            Toasty.info(getContext(), "Mobile Not Found", Toast.LENGTH_SHORT, true).show();
                        }
                        progressDialog.dismiss();
                        // lottieAnimationView.setVisibility(View.GONE);
                        if (getActivity() != null) {
                            final Dialog d = new Dialog(getActivity());

                            d.setTitle("MESSAGE");
                            d.setContentView(R.layout.shareappdialog);
                            d.setCancelable(false);
                            final Button sharebtn = d.findViewById(R.id.share);
                            final Button cancelbtn = d.findViewById(R.id.cancelshare);

                            sharebtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toasty.info(getContext(), "Sharing Link..", Toast.LENGTH_SHORT, false).show();

                                    d.dismiss();
                                    sharetoWhatsapp(friendmobilenum);
                                }
                            });
                            cancelbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    d.dismiss();
                                }
                            });
                            d.show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progressDialog.dismiss();
                // lottieAnimationView.setVisibility(View.GONE);
            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getContext() != null) {
                    Toasty.error(getContext(), "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
                progressDialog.dismiss();
                //lottieAnimationView.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> checkpar = new HashMap<>();
                checkpar.put("user_mobile", userMobile);
                checkpar.put("friend_mobile", friendmobilenum);
                return checkpar;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }

    private void sharetoWhatsapp(String friendmobilenum) {
        if (getContext() == null) {
            return;
        }
        String app_link = "Please Download our JetGPSShare to find your friends \n" + "http://play.google.com/store/apps/details?id=" + getContext().getPackageName();

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Jet Gps Share");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + app_link + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share App"));
        } catch (Exception e) {
            //e.toString();
            e.printStackTrace();
        }


    }

    private boolean appInstalled(String package_name) {
        if (getActivity() == null) {
            return false;
        }
        PackageManager packageManager = getActivity().getPackageManager();

        boolean app_install;
        try {
            packageManager.getPackageInfo(package_name, PackageManager.GET_ACTIVITIES);
            app_install = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_install = false;
        }

        return app_install;

    }

    private void startNavigationBtnClick(View v) {
        if (optimizedroute == null) {
            return; // Route has not been set, so we ignore the button press
        }
        if (optimizedroute.distance() == 0.0 && getContext() != null) {
            Toasty.info(getContext(), "YOUR AT YOUR DESTINATION", Toast.LENGTH_SHORT).show();
            return;
        }
        if (optimizedroute.distance() <= 0.300 && getContext() != null) {
            Toasty.info(getContext(), "YOUR DESTINATION IS NEARBY YOU", Toast.LENGTH_SHORT).show();
        }

        if (destinationPoint != null) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destinationPoint.latitude() + "," + destinationPoint.longitude());

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            //Log.i("granted", "   " + granted);

            enableLocationComponent(Objects.requireNonNull(mapboxMap.getStyle()));

        } else {
            if (getContext() != null) {
                Toasty.info(getContext(), "Permission not granted", Toast.LENGTH_LONG, true).show();
            }
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }


    private void opendialog() {

        if (!gps_enabled) {
            // notify user
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission Neeed!!")
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
                            dialog.dismiss();
                        }
                    }).setIcon(R.drawable.mylogo).setCancelable(false)
                    .show();
        }
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("granted", false + "");

            if (mapboxMap != null && mapboxMap.getStyle() != null) {
                enableLocationComponent(mapboxMap.getStyle());
            }
            return true;

        }

        if (mapView != null && mapboxMap != null) {
            destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
            destinationcopy = destinationPoint;
        }
        if (locationComponent != null && locationComponent.getLastKnownLocation() != null) {
            originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                    locationComponent.getLastKnownLocation().getLatitude());
            orgincopy = originPoint;
        } else if (locationComponent == null) {
            enableLocationComponent(mapboxMap.getStyle());
            return true;
        } else if (!(locationComponent == null && gps_enabled)) {
            // mapView.onStart();
            opendialog();
            return true;
        }
        if (symbolManager != null && !symbolManager.getAnnotations().isEmpty()) {
            symbolManager.delete(symbol);
        }

        if (mapboxMap != null && mapboxMap.getStyle() != null) {
            source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        }
        getOriginAndDestinationFeatureCollection();

        if (source != null) {
            //    getAddress(point.getLatitude(),point.getLongitude(),destinationPoint);
            source.setGeoJson(getOriginAndDestinationFeatureCollection());
        }


        CameraPosition position = new CameraPosition.Builder()
                .target(point) // Sets the new camera position
                .zoom(14) // Sets the zoom
                .bearing(180) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder
        if (mapboxMap != null) {
            mapboxMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), 7000);
        }

        Log.i("path", "mapClick");
        new getaddresstask().execute(destinationPoint);


        getRoute(originPoint, destinationPoint);


        return true;

    }

    public void sharemylocation() {
        progressDialog.show();
        if (originPoint == null) {
            sharemylocationWhatsapp(destinationcopy, orgincopy);
        } else if (destinationcopy != null) {
            sharemylocationWhatsapp(destinationcopy, orgincopy);
            Log.i("pathing", "destination!=null");
        } else if (destinationcopy == null && orgincopy != null) {
            sharemylocationWhatsapp(destinationcopy, orgincopy);
        } else if (mapView == null && mapboxMap == null) {
            Toasty.warning(getContext(), "PLEASE WAIT..", Toasty.LENGTH_SHORT).show();
            Log.i("checking", "BOTH ARE NULL");
            // return;
        } else {
            return;
        }
    }

    private void sharemylocationWhatsapp(Point destinationPoints, Point originPoints) {
        boolean isInstalled = appInstalled("com.whatsapp");
        String app_link = null;

        if (destinationPoints != null) {
            app_link = "text=https://maps.google.com/?q=" + destinationPoints.latitude() + "," + destinationPoints.longitude();
            if (getContext() != null) {
                Toasty.success(getContext(), "Sharing Destinaion", Toasty.LENGTH_SHORT).show();
            }
        } else if (destinationPoints == null && originPoints != null) {
            app_link = "text=https://maps.google.com/?q=" + originPoints.latitude() + "," + originPoints.longitude();
            if (getContext() != null) {
                Toasty.success(getContext(), "Sharing Your Location", Toasty.LENGTH_SHORT).show();
            }

        } else if (originPoint == null && locationComponent != null && locationComponent.getLastKnownLocation() != null) {
            app_link = "text=https://maps.google.com/?q=" + locationComponent.getLastKnownLocation().getLatitude() + "," +
                    locationComponent.getLastKnownLocation().getLongitude();
            if (getContext() != null) {
                Toasty.success(getContext(), "Sharing Your Location", Toasty.LENGTH_SHORT).show();
            }
        }


        try {
            progressDialog.dismiss();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Jet Gps Share");
            String shareMessage = "\n\n";
            shareMessage = shareMessage + app_link + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share Location"));
        } catch (Exception e) {
            progressDialog.dismiss();
            //e.toString();
        }

    }


    @SuppressLint("StaticFieldLeak")
    public class getaddresstask extends AsyncTask<Point, Void, Void> {

        Point point;

        @Override
        protected Void doInBackground(Point... points) {
            adr.clear();
            point = points[0];
            Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
            try {
                adr = gc.getFromLocation(points[0].latitude(), points[0].longitude(), 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (adr != null) {
                if (adr.size() > 0 && adr.get(0) != null && adr.get(0).getLocality() != null && symbolManager != null) {
                    symbol = symbolManager.create(new SymbolOptions().withGeometry(point)
                            .withTextField(adr.get(0).getLocality()).withTextOffset(new Float[]{0f, -2.5f}));
                    Toast.makeText(getContext(), adr.get(0).getLocality() + "", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void getRoute(Point originPoint, Point destinationPoint) {


        List<Point> coordinates = new ArrayList<>();
        coordinates.clear();
        coordinates.add(originPoint);
        coordinates.add(destinationPoint);
        double x1 = originPoint.latitude();
        double x2 = destinationPoint.latitude();
        double y1 = originPoint.longitude();
        double y2 = destinationPoint.longitude();


        double theta = y1 - y2;
        double dist = Math.sin(Math.toRadians(x1)) * Math.sin(Math.toRadians(x2)) + Math.cos(Math.toRadians(x1)) * Math.cos(Math.toRadians(x2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;


        // Toasty.success(getContext(), "Distance :" + dist, Toasty.LENGTH_SHORT).show();
        // Log.i("dist", "" + dist);
        if (getContext() != null) {
            Toasty.success(getContext(), "Distance :" + dist, Toasty.LENGTH_SHORT).show();

        }
        if (getContext() != null && !MyApplication.checkmyconnection(getContext())) {
            Toasty.warning(getContext(), "Please check your INTERNET connection.", Toasty.LENGTH_SHORT).show();
            mclear.setVisibility(View.VISIBLE);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            return;
        }
        if (getContext() != null && !MyApplication.isInternetAvailable()) {
            Toasty.warning(getContext(), "NO PROPER INTERNET connection.", Toasty.LENGTH_SHORT).show();
            mclear.setVisibility(View.VISIBLE);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            return;

        }
        if (dist <= 0.450) {
            assert Mapbox.getAccessToken() != null;
            optimizedClient = MapboxOptimization.builder()
                    .coordinates(coordinates)
                    .profile(DirectionsCriteria.PROFILE_WALKING)
                    .accessToken(Mapbox.getAccessToken())
                    .build();
        } else {
            assert Mapbox.getAccessToken() != null;
            optimizedClient = MapboxOptimization.builder()
                    .coordinates(coordinates)
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .accessToken(Mapbox.getAccessToken())
                    .build();
        }
        optimizedClient.enqueueCall(new Callback<OptimizationResponse>() {
            @Override
            public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {

                if (!response.isSuccessful()) {
                    Log.d(TAG, "optimization call not successful");
                    return;
                } else {
                    if (response.body() != null && response.body().trips() != null && response.body().trips().isEmpty()) {
                        Log.d(TAG, "optimization call successful but no routes");
                        return;
                    }
                }

                optimizedroute = response.body().trips().get(0);
                if (optimizedroute != null) {
                    //  Log.i("route", optimizedroute.toString());
                }
                if (mapboxMap != null) {
                    mapboxMap.clear();
                }
                new MyTask().execute(optimizedroute);
                Log.i("path", "getROute");
                startnavigation.setVisibility(View.VISIBLE);
                startnavigation.setClickable(true);
                //Toasty.success(getContext(), "Distance :" + dist, Toasty.LENGTH_SHORT).show();

                mclear.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
                mclear.setClickable(true);


            }

            //Toasty.success(getContext(), "Distance :" + dist, Toasty.LENGTH_SHORT).show();
            @Override
            public void onFailure(Call<OptimizationResponse> call, Throwable throwable) {
                // Log.d(TAG, "Error: " + throwable.getMessage());
            }
        });

    }

    private FeatureCollection getOriginAndDestinationFeatureCollection() {
        originFeature = Feature.fromGeometry(originPoint);
        originFeature.addStringProperty("originDestination", "origin");

        destinationFeature = Feature.fromGeometry(destinationPoint);
        destinationFeature.addStringProperty("originDestination", "destination");

        return FeatureCollection.fromFeatures(new Feature[]{originFeature, destinationFeature});
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        Log.i("path", "mapready");  //getString(R.string.navigation_guidance_day)
        this.mapboxMap.setMinZoomPreference(12);
        mapboxMap.setStyle(sharedPreferences.getString("view", Style.MAPBOX_STREETS), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                styles = style;
                enableLocationComponent(style);
                addDestinationIconLayer(style);
                symbolManager = new SymbolManager(mapView, mapboxMap, style);


                mapboxMap.addOnMapClickListener(MapFragment.this);


            }
        });


    }

    public class MyTask extends AsyncTask<DirectionsRoute, Void, Void> {

        LatLng[] points;
        List<Point> coordinates = new ArrayList<>();

        @Override
        protected Void doInBackground(DirectionsRoute... directionsRoutes) {

            LineString lineString = LineString.fromPolyline(directionsRoutes[0].geometry(), PRECISION_6);
            //   lineLayerRouteGeoJsonSource.setGeoJson(Feature.fromGeometry(lineString));
            if (coordinates.size() > 0) {
                coordinates.clear();
            }
            coordinates = lineString.coordinates();

            points = new LatLng[coordinates.size()];
            //Log.i("pointsize", points.length + "");
            for (int i = 0; i < coordinates.size(); i++) {
                Log.i("coordinates", coordinates.get(i).latitude() + "     " + coordinates.get(i).longitude());
                points[i] = new LatLng(
                        coordinates.get(i).latitude(),
                        coordinates.get(i).longitude());

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            com.mapbox.mapboxsdk.annotations.PolylineOptions polylineOptions = new PolylineOptions()
                    .color(Color.GREEN)
                    .add(points)
                    .width(6);


            Polyline polyline = mapboxMap.addPolyline(polylineOptions);
            progressDialog.dismiss();


        }
    }

    private void addDestinationIconLayer(Style style) {
        styles = style;
        Log.i("path", "addDestinationLayer");
        style.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));


        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        style.addSource(geoJsonSource);

        //  SymbolLayer
        destinationSymbolLayer = new SymbolLayer
                ("destination-symbol-layer-id", "destination-source-id");

        destinationSymbolLayer.withProperties(iconImage("destination-icon-id"), iconAllowOverlap(true),
                iconIgnorePlacement(true), textAllowOverlap(true), textOffset(new Float[]{0f, -2.5f}), textIgnorePlacement(true)
        );


        style.addLayer(destinationSymbolLayer);


    }


    private void enableLocationComponent(@NotNull Style loadedMapStyle) {
        Log.i("granted", "enablelocationcomponenet");
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
            locationComponent = mapboxMap.getLocationComponent();
            destinationcopy = null;

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                return;
            }
            locationComponent.activateLocationComponent(getActivity(), loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
            Log.i("granted", "requestlocationpermission");

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    public MapboxMap getmymapboxinstance() {
        if (mapboxMap != null) {
            return mapboxMap;
        }
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }

        try {
            if (lm != null) {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception ex) {
        }
        if (gps_enabled) {
            if (fab != null) {
                fab.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Mapbox.getInstance(context, "sk.eyJ1Ijoia2FseWFuNDQ0NCIsImEiOiJja2sydWpqMTIxNTJkMm5wY25pbHo1NXJtIn0.VYJ_eEeC2N343PxwB458GA");
        sharedPreferences = context.getSharedPreferences("MyUser", MODE_PRIVATE);
        Dexter.withContext(context).withPermission(Manifest.permission.READ_CONTACTS).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (permissionGrantedResponse.getPermissionName().equals(Manifest.permission.READ_CONTACTS)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getContacts(context);

                        }
                    }, 100);


                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toasty.warning(context, "PERMISSION IS NEEDED ", Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (optimizedClient != null) {
            optimizedClient.cancelCall();
            mapFragment = null;
        }
        if (mapView != null) {
            mapView.onDestroy();
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    private void getContacts(Context context) {
        Map<String, String> mydisplaycontacts = new HashMap<>();
        mydisplaycontacts.clear();
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null
                , null, null);
        assert phones != null;
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phonenum = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s", "").trim();
            ;
            if (phonenum.length() > 10) {

                phonenum = phonenum.substring(phonenum.length() - 10);
            }
            String namephone = name + " : " + phonenum;

            if (!mydisplaycontacts.containsKey(phonenum) && phonenum.length() == 10) {
                mydisplaycontacts.put(phonenum, namephone);
            }


        }
        mynames.clear();
        mynames.addAll(mydisplaycontacts.values());

        arrayAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, mynames);
        phones.close();

    }


}