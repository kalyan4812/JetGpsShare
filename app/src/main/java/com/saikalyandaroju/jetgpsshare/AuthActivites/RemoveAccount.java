package com.saikalyandaroju.jetgpsshare.AuthActivites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saikalyandaroju.jetgpsshare.ActivityStatusActivity;
import com.saikalyandaroju.jetgpsshare.Dashboard;
import com.saikalyandaroju.jetgpsshare.MyApplication;
import com.saikalyandaroju.jetgpsshare.R;
import com.saikalyandaroju.jetgpsshare.db.MyDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class RemoveAccount extends AppCompatActivity {
    EditText email, password;
    LottieAnimationView lottieAnimationView;
    String api_link_remove = "https://oakspro.com/projects/project36/kalyan/JGS/removeaccount.php";
    SharedPreferences sharedPreferences, sharedPreferences2;
    SharedPreferences.Editor editor, editor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_account);
        lottieAnimationView = findViewById(R.id.progressanimation);
        setTitle("Remove Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences2 = (SharedPreferences) getSharedPreferences("MY_FRIENDS", MODE_PRIVATE);
        editor2 = sharedPreferences.edit();
        email = findViewById(R.id.user_ed);
        password = findViewById(R.id.user_pass);
    }

    public void deleteaccount(View view) {
        String email_s = email.getText().toString();
        String pass_s = password.getText().toString();
        if (TextUtils.isEmpty(pass_s) == false && email_s.length() == 10) {
            removeaccount(email_s, pass_s);
        } else {
            Toasty.warning(getApplicationContext(), "Please fill details correctly", Toasty.LENGTH_SHORT, true).show();
        }
    }

    private void removeaccount(String email_s, String pass_s) {
        if (getApplicationContext() != null && !MyApplication.checkmyconnection(getApplicationContext())) {

            Toasty.warning(getApplicationContext(), "Please check your INTERNET connection.", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (getApplicationContext() != null && !MyApplication.isInternetAvailable()) {
            Toasty.warning(getApplicationContext(), "NO PROPER INTERNET connection.", Toasty.LENGTH_SHORT).show();

            return;

        }
        lottieAnimationView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, api_link_remove, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("jstatus");

                    if (status.equals("1")) {
                        lottieAnimationView.setVisibility(View.GONE);
                        Toasty.success(RemoveAccount.this, "Deleted", Toast.LENGTH_SHORT).show();
                        email.setText("");
                        password.setText("");
                        editor.clear().commit();
                        editor2.clear().commit();
                        //RoomDatabase.
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Room.databaseBuilder(RemoveAccount.this, MyDatabase.class, "friendsdb").build().clearAllTables();

                            }
                        });
                        if (ActivityStatusActivity.getinstanece() != null) {
                            ActivityStatusActivity.getinstanece().stopfirstservice();
                        }
                        Intent i = new Intent(RemoveAccount.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();


                    } else if (status.equals("0")) {
                        lottieAnimationView.setVisibility(View.GONE);
                        Toasty.error(RemoveAccount.this, "Failed to remove account", Toast.LENGTH_SHORT).show();

                    } else if (status.equals("3")) {
                        lottieAnimationView.setVisibility(View.GONE);
                        Toasty.warning(RemoveAccount.this, "Account doesn't exist", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(RemoveAccount.this, "Failed to remove account", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RemoveAccount.this, "Failed to remove account", Toast.LENGTH_SHORT).show();

                lottieAnimationView.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", email_s);
                params.put("password", pass_s);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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
}
