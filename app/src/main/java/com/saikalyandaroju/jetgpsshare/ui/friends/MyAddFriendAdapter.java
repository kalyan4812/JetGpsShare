package com.saikalyandaroju.jetgpsshare.ui.friends;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.saikalyandaroju.jetgpsshare.db.FriendEntity;
import com.saikalyandaroju.jetgpsshare.MyApplication;
import com.saikalyandaroju.jetgpsshare.Pojos.Contacts;
import com.saikalyandaroju.jetgpsshare.R;
import com.saikalyandaroju.jetgpsshare.db.MyDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class MyAddFriendAdapter extends RecyclerView.Adapter<MyAddFriendAdapter.MyHoder> implements Filterable {
    private Context context;
    private List<Contacts> contactsList;
    private SharedPreferences sharedPreferences, sharedPreferences2;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;
    private static MyDatabase mydb;

    private List<Contacts> mylist;

    private String api_link = "https://oakspro.com/projects/project36/kalyan/JGS/add_friends.php";
    private String mobile;

    MyAddFriendAdapter(Context context, List<Contacts> contactList) {
        this.context = context;
        this.contactsList = contactList;

    }

    @NonNull
    @Override
    public MyHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contentproviderlayout, parent, false);
        mydb = Room.databaseBuilder(parent.getContext(), MyDatabase.class, "friendsdb").build();

        sharedPreferences = (SharedPreferences) parent.getContext().getSharedPreferences("MY_FRIENDS", MODE_PRIVATE);
        sharedPreferences2 = context.getSharedPreferences("MyUser", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mylist = new ArrayList<>(contactsList);
        mobile = sharedPreferences2.getString("mobile", null);
        progressDialog = new ProgressDialog(AddFriendsActivity.getmyaddfriendinstance());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return new MyHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHoder holder, int position) {


        final Contacts contact = contactsList.get(position);
        if (contact != null) {
            holder.name.setText(contact.getContactname());
            holder.phone.setText(contact.getPhone());
        }
        if (sharedPreferences.getBoolean(contact.getPhone(), false)) {
            holder.addfriend.setText("REMOVE");
        }
        holder.addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context != null && !MyApplication.checkmyconnection(context)) {
                    Toasty.warning(context, "Please check your INTERNET connection.", Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (context != null && !MyApplication.isInternetAvailable()) {
                    Toasty.warning(context, "NO PROPER INTERNET connection.", Toasty.LENGTH_SHORT).show();

                    return;

                }

                if (sharedPreferences.getBoolean(contact.getPhone(), false)) {

                    editor.putBoolean(contact.getPhone(), false);
                    editor.commit();
                    holder.addfriend.setText(R.string.add);
                } else {
                    String mycontact = contact.getPhone().replaceAll("\\s", "").trim();
                    if (contact.getPhone().contains("+")) {
                        mycontact = contact.getPhone().substring(1);
                    }
                    if (mycontact.length() == 12) {

                        uploadFriends(mycontact.substring(2), mobile, contact, holder, position);
                    } else if (mycontact.length() == 13) {

                        uploadFriends(mycontact.substring(3), mobile, contact, holder, position);
                    } else {

                        uploadFriends(mycontact, mobile, contact, holder, position);

                    }


                }
            }
        });

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        // run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contacts> filteredlist = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredlist.addAll(mylist);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (Contacts c : mylist) {
                    if (c.getContactname().toLowerCase().trim().contains(filterpattern) || c.getPhone().contains(constraint)) {
                        filteredlist.add(c);
                    }
                }
            }
            FilterResults results = new FilterResults();
            System.out.println("Count Number " + filteredlist.size());
            // Log.i("myinfo", "  " + filteredlist.size());
            results.values = filteredlist;
            results.count = filteredlist.size();
            return results;
        }

        // run on UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (!TextUtils.isEmpty(constraint)) {
                contactsList.clear();
                contactsList.addAll((ArrayList<Contacts>) results.values);
            } else {
                contactsList.clear();
                contactsList.addAll(mylist);
            }

            //Log.i("kat", "  " + constraint);
            notifyDataSetChanged();
        }
    };

    public static class InsertTask extends AsyncTask<FriendEntity, Void, Void> {

        @Override
        protected Void doInBackground(FriendEntity... friendEntities) {
            mydb.myDao().addFriend(friendEntities[0]);
            return null;
        }

    }


    private void uploadFriends(final String numberM, final String userMobile, Contacts contact, MyHoder holder, int position) {

        try {
            progressDialog.show();
        } catch (Exception e) {

        }
        StringRequest request = new StringRequest(Request.Method.POST, api_link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("jstatus");
                    String check = jsonObject.getString("status");
                    if (check.equals("1")) {

                        if (status.equals("1")) {
                            Toasty.success(context, "Added", Toast.LENGTH_SHORT, true).show();
                            //progressDialog.dismiss();
                            FriendEntity friendEntity = new FriendEntity(contact.getContactname(), contact.getPhone());
                            new InsertTask().execute(friendEntity);
                            editor.putBoolean(contact.getPhone(), true);
                            editor.commit();
                            contactsList.remove(position);
                            notifyItemRemoved(position);
                            progressDialog.dismiss();
                        } else {
                            Toasty.info(context, "Failed", Toast.LENGTH_SHORT, true).show();
                            progressDialog.dismiss();
                        }
                    } else if (check.equals("0") && status.equals("3")) {
                        Log.i("enter", "true....");
                        Toasty.info(AddFriendsActivity.getmyaddfriendinstance(), "Mobile Not Found", Toast.LENGTH_SHORT, true).show();
                        progressDialog.dismiss();
                        // lottieAnimationView.setVisibility(View.GONE);
                        final Dialog d = new Dialog(AddFriendsActivity.getmyaddfriendinstance());

                        d.setTitle("MESSAGE");
                        d.setContentView(R.layout.shareappdialog);
                        d.setCancelable(false);
                        final Button sharebtn = d.findViewById(R.id.share);
                        final Button cancelbtn = d.findViewById(R.id.cancelshare);

                        sharebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toasty.info(AddFriendsActivity.getmyaddfriendinstance(), "Sharing Link..", Toast.LENGTH_SHORT, false).show();

                                d.dismiss();
                                sharetoWhatsapp(numberM);
                            }
                        });
                        cancelbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });
                        try {
                            d.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.warning(context, "FAILED,Retry", Toasty.LENGTH_SHORT, false).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(context, "FAILED,Retry", Toast.LENGTH_SHORT, true).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("frnd_num", numberM);
                params.put("user_num", userMobile);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    private void sharetoWhatsapp(String friendmobilenum) {
        boolean isInstalled = appInstalled("com.whatsapp");
        String app_link = "Please Download our JetGPSShare to find your friends \n https://oakspro.com/projects/project35/deepu/JGS/download/JetGPSShare.apk";
        if (isInstalled) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+91" + friendmobilenum + "&text=" + app_link));
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Please Install Whatsapp First", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean appInstalled(String package_name) {
        PackageManager packageManager = context.getPackageManager();
        boolean app_install;
        try {
            packageManager.getPackageInfo(package_name, PackageManager.GET_ACTIVITIES);
            app_install = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_install = false;
        }
        return app_install;
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class MyHoder extends RecyclerView.ViewHolder {
        TextView name, phone;
        Button addfriend;


        public MyHoder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.pname);
            phone = itemView.findViewById(R.id.pnum);
            addfriend = itemView.findViewById(R.id.addfriend);

        }
    }
}
