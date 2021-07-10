package com.saikalyandaroju.jetgpsshare.AuthActivites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saikalyandaroju.jetgpsshare.Dashboard;
import com.saikalyandaroju.jetgpsshare.OnBoardActivities.IntroActivity;
import com.saikalyandaroju.jetgpsshare.MyApplication;
import com.saikalyandaroju.jetgpsshare.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    EditText passwordEd, userEd;
    Button loginBtn;
    ProgressDialog progressDialog;
    String api_link = "https://oakspro.com/projects/project36/kalyan/JGS/login_api.php";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myedit;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lottieAnimationView = findViewById(R.id.progressanimation);
        sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
        myedit = sharedPreferences.edit();

        //set ids

        passwordEd = findViewById(R.id.pass_ed);
        userEd = findViewById(R.id.user_ed);
        loginBtn = findViewById(R.id.loginBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
                String umobile = userEd.getText().toString();
                String upass = passwordEd.getText().toString();
                if (getApplicationContext() != null && !MyApplication.checkmyconnection(getApplicationContext())) {

                    Toasty.warning(getApplicationContext(), "Please check your INTERNET connection.", Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (getApplicationContext() != null && !MyApplication.isInternetAvailable()) {
                    Toasty.warning(getApplicationContext(), "NO PROPER INTERNET connection.", Toasty.LENGTH_SHORT).show();

                    return;

                }
                if (!TextUtils.isEmpty(umobile) && !TextUtils.isEmpty(upass)) {

                    VerifyData(umobile, upass);
                } else {
                    Toasty.warning(LoginActivity.this, "Fill all feilds", Toast.LENGTH_SHORT, true).show();
                }
            }
        });


    }

    private void VerifyData(final String umobile, final String upass) {

        //  progressDialog.show();
        lottieAnimationView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, api_link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // JsonParser parser = new JsonParser();
                    JSONObject responser = new JSONObject(response);

                    String status = responser.getString("status");

                    if (status.equals("1")) {


                        String name = responser.getString("name");
                        String mobile = responser.getString("mobile");
                        String email = responser.getString("email");
                        Toasty.success(LoginActivity.this, "Login Success: " + name, Toast.LENGTH_SHORT, true).show();

                        myedit.putString("name", name);
                        myedit.putString("mobile", mobile);
                        myedit.putString("email", email);
                        myedit.putBoolean("loginS", true);
                        myedit.putBoolean("checked", true);

                        myedit.commit();

                        if (sharedPreferences.getBoolean("firsttime", true)) {
                            Intent i = new Intent(getApplicationContext(), IntroActivity.class);
                            startActivity(i);


                        } else {
                            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                            startActivity(intent);

                        }

                        finish();
                        //progressDialog.dismiss();
                        lottieAnimationView.setVisibility(View.GONE);

                    } else if (status.equals("0")) {

                        Toasty.error(LoginActivity.this, "Incorrect Password ", Toast.LENGTH_SHORT, true).show();

                        //  progressDialog.dismiss();
                        lottieAnimationView.setVisibility(View.GONE);

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    Toasty.error(LoginActivity.this, "Account doesn't exist", Toast.LENGTH_SHORT, true).show();
                    //progressDialog.dismiss();
                    lottieAnimationView.setVisibility(View.GONE);
                }

                //   progressDialog.dismiss();
                lottieAnimationView.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //  progressDialog.dismiss();
                Toasty.error(LoginActivity.this, "Logging failed", Toast.LENGTH_SHORT, true).show();
                lottieAnimationView.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parmas = new HashMap<>();
                parmas.put("mobile", umobile);
                parmas.put("password", upass);
                return parmas;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void openSignupLink(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void openForgot(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
        Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(intent);
    }
}
