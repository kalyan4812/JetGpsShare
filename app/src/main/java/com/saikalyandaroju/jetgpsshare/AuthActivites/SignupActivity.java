package com.saikalyandaroju.jetgpsshare.AuthActivites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.saikalyandaroju.jetgpsshare.MyApplication;
import com.saikalyandaroju.jetgpsshare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {

    EditText nameEd, mobileEd, passEd, emailEd, otp;
    Button signupBtn, nextBtn;
    ProgressDialog progressDialog;
    TextView resendotp, otptext;
    String api_link = "https://oakspro.com/projects/project36/kalyan/JGS/register_api.php";
    String name_s, phone_s, pass_s, email_s;
    LottieAnimationView lottieAnimationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String randomNumber;
    String otpentred;
    String verificationId;
    CountryCodePicker ccp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        lottieAnimationView = findViewById(R.id.progressanimation);
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        otptext = findViewById(R.id.otptext);
        resendotp = findViewById(R.id.resendotp);
        otp = findViewById(R.id.otp);
        nextBtn = findViewById(R.id.next);
        nameEd = findViewById(R.id.name_ed);
        ccp = findViewById(R.id.ccp);
        mobileEd = findViewById(R.id.user_ed);
        ccp.registerPhoneNumberTextView(mobileEd);
        ccp.setCountryForNameCode("IND");
        ccp.setClickable(false);
        passEd = findViewById(R.id.pass_ed);
        signupBtn = findViewById(R.id.signupBtn);
        emailEd = findViewById(R.id.email_ed);
        StrictMode.ThreadPolicy st = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(st);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        nextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (nextBtn.getText().toString().equals("NEXT")) {
                    name_s = nameEd.getText().toString();
                    phone_s = mobileEd.getText().toString();
                    email_s = emailEd.getText().toString();
                    if (!TextUtils.isEmpty(name_s) && !TextUtils.isEmpty(phone_s) && !TextUtils.isEmpty(email_s)) {
                        Log.i("num", ccp.getFullNumberWithPlus());
                        if (ccp.isValid()) {
                            sendotp(ccp.getFullNumberWithPlus());
                        } else {
                            Toasty.warning(SignupActivity.this, "Please Enter a Valid Number.", Toast.LENGTH_SHORT, true).show();
                        }
                    } else {

                        Toasty.warning(SignupActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    progressDialog.show();
                    otpentred = otp.getText().toString();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otpentred);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                nameEd.setKeyListener(null);
                                mobileEd.setKeyListener(null);
                                emailEd.setKeyListener(null);
                                otp.setVisibility(View.GONE);
                                otptext.setVisibility(View.GONE);
                                resendotp.setVisibility(View.GONE);
                                nextBtn.setVisibility(View.GONE);
                                passEd.setVisibility(View.VISIBLE);
                                signupBtn.setVisibility(View.VISIBLE);

                                progressDialog.dismiss();
                            } else {
                                Toasty.warning(SignupActivity.this, "OTP was incorrect", Toast.LENGTH_SHORT, true).show();
                                progressDialog.dismiss();
                            }
                        }
                    });


                }


            }
        });
        resendotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ccp.isValid()) {
                    sendotp(ccp.getFullNumberWithPlus());
                } else {
                    Toasty.warning(SignupActivity.this, "Please Enter a Valid Number.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
                name_s = nameEd.getText().toString();
                phone_s = mobileEd.getText().toString();
                pass_s = passEd.getText().toString();
                email_s = emailEd.getText().toString();
                if(getApplicationContext()!=null && !MyApplication.checkmyconnection(getApplicationContext())){

                    Toasty.warning(getApplicationContext(),"Please check your INTERNET connection.",Toasty.LENGTH_SHORT).show();
                    return;
                }
                if(getApplicationContext()!=null && !MyApplication.isInternetAvailable()){
                    Toasty.warning(getApplicationContext(), "NO PROPER INTERNET connection.", Toasty.LENGTH_SHORT).show();

                    return;

                }

                if (!TextUtils.isEmpty(name_s) && !TextUtils.isEmpty(phone_s) && !TextUtils.isEmpty(pass_s) && !TextUtils.isEmpty(email_s)) {
                    uploadData(name_s, phone_s, pass_s, email_s);
                } else {
                    // Toast.makeText(SignupActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                    Toasty.warning(SignupActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT, true).show();
                }
            }
        });


    }

    private void sendotp(String phone_s) {

        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_s, 60, TimeUnit.SECONDS,SignupActivity.this, mcallback);


    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
            otp.setVisibility(View.VISIBLE);
            otptext.setVisibility(View.VISIBLE);
            resendotp.setVisibility(View.VISIBLE);
            nextBtn.setText("CONFIRM");
            progressDialog.dismiss();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            //assert code != null;
          //  Log.i("otp", code);
            if (code != null) {
                otp.setText(code);
                resendotp.setVisibility(View.GONE);
                progressDialog.dismiss();

            } else {
                resendotp.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toasty.error(SignupActivity.this, "can't send otp now try after some time", Toasty.LENGTH_SHORT).show();
            if(e!=null && e.getMessage()!=null){
                Toasty.error(SignupActivity.this, e.getMessage(), Toasty.LENGTH_SHORT).show();
            }
          //  Log.i("error",e.getMessage());
            progressDialog.dismiss();
        }
    };


    private void uploadData(final String name_s, final String phone_s, final String pass_s, final String email_s) {

        //  progressDialog.show();
        lottieAnimationView.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, api_link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String jStatus = jsonObject.getString("jstatus");

                    if (jStatus.equals("1")) {
                        // progressDialog.dismiss();
                        lottieAnimationView.setVisibility(View.GONE);
                        Toasty.success(SignupActivity.this, "Signup Succes", Toast.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // progressDialog.dismiss();
                        lottieAnimationView.setVisibility(View.GONE);
                        //   Toast.makeText(SignupActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                        Toasty.info(SignupActivity.this, "Account already exists", Toast.LENGTH_SHORT, true).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    // progressDialog.dismiss();
                    lottieAnimationView.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(SignupActivity.this, "Failed to register", Toast.LENGTH_SHORT, true).show();
                //   progressDialog.dismiss();
                lottieAnimationView.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name_s);
                params.put("mobile", phone_s);
                params.put("password", pass_s);
                params.put("email", email_s);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    public void openLoginLink(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce));
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);

        startActivity(intent);
        finish();
    }


}
