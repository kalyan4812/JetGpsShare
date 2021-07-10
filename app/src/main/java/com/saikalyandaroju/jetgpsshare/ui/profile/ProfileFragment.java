package com.saikalyandaroju.jetgpsshare.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.saikalyandaroju.jetgpsshare.R;
import com.saikalyandaroju.jetgpsshare.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private TextInputEditText name;
    private Button namechange, update;
    private TextView photochange, phone, email, toppname;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView image;
    private Uri imageUri, uri;
    private FrameLayout frameLayout;
    private Toolbar toolbar;
    private KeyListener keyListener;
    private NavigationView navigationView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        image = root.findViewById(R.id.profileimg);
        if (getContext() != null) {
            sharedPreferences = getContext().getSharedPreferences("MyUser", MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        if (getActivity() != null && getActivity().findViewById(R.id.toolbar) != null) {
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        }
        if (getActivity() != null && getActivity().findViewById(R.id.nav_view) != null) {
            navigationView = getActivity().findViewById(R.id.nav_view);
        }
        uri = Uri.parse("android.resource://com.saikalyandaroju.jetgpsshare/drawable/myworld");
        Picasso.get().load(sharedPreferences.getString("profileuri", uri.toString())).into(image);

        phone = root.findViewById(R.id.phone);
        email = root.findViewById(R.id.email);
        toppname = root.findViewById(R.id.topname);
        toppname.setText(sharedPreferences.getString("name", null));
        email.setText(sharedPreferences.getString("email", null));
        frameLayout = root.findViewById(R.id.backframe);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null && getActivity().findViewById(R.id.nav_host_fragment) == null) {
                    return;
                }

                if (toolbar != null) {
                    toolbar.setTitle("Home");
                }
                if (navigationView != null) {
                    navigationView.setCheckedItem(R.id.nav_home);
                }


                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();

                //   Intent i = new Intent(getActivity(), Dashboard.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                //   startActivity(i);
                // getActivity().finish();
            }
        });

        namechange = root.findViewById(R.id.namechange);
        name = root.findViewById(R.id.username);

        keyListener = name.getKeyListener();
        name.setKeyListener(null);
        update = root.findViewById(R.id.update_button);
        update.setVisibility(View.GONE);
        photochange = root.findViewById(R.id.photochange);
        name.setText(sharedPreferences.getString("name", null));
        phone.setText(sharedPreferences.getString("mobile", null));
        photochange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "CLICKED", Toast.LENGTH_SHORT).show();
                pickimage();
            }
        });
        namechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setKeyListener(keyListener);
                name.setCursorVisible(true);
                name.setFocusable(true);
                update.setVisibility(View.VISIBLE);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(name.getText().toString())) {
                    editor.putString("name", name.getText().toString());
                    editor.commit();
                    update.setVisibility(View.GONE);
                    name.setKeyListener(null);
                    toppname.setText(name.getText().toString());
                } else {
                    Toasty.warning(getContext(), "Username is Empty,Please fill it", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        return root;
    }


    private void pickimage() {
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }


                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri filepath = data.getData();

            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(filepath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);
                imageUri = data.getData();
                editor.putString("profileuri", imageUri.toString()).commit();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}

