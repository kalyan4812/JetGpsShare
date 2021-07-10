package com.saikalyandaroju.jetgpsshare.ui.friends;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {
    private Context context;
    private static List<Contacts> contactsList;

    private SharedPreferences sharedPreferences, sharedPreferences2;
    private SharedPreferences.Editor editor, editor2;
    private MyDatabase mydb;
    private ProgressDialog progressDialog;
    private String user_mobile;
    private String api_link_delete = "https://oakspro.com/projects/project36/kalyan/JGS/delete_friends.php";

    public FriendAdapter(Context context, List<Contacts> contactsList) {
        this.context = context;
        this.contactsList = contactsList;

    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_friend_layout, parent, false);
        mydb = Room.databaseBuilder(context, MyDatabase.class, "friendsdb").build();
        sharedPreferences = (SharedPreferences) parent.getContext().getSharedPreferences("MY_FRIENDS", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences2 = (SharedPreferences) parent.getContext().getSharedPreferences("MyUser", MODE_PRIVATE);
        user_mobile = sharedPreferences2.getString("mobile", null);
        progressDialog = new ProgressDialog(parent.getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return new FriendAdapter.FriendHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        final Contacts contact = contactsList.get(position);
        holder.name.setText(contact.getContactname());
        holder.phone.setText(contact.getPhone());
        holder.removefriend.setOnClickListener(new View.OnClickListener() {
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
                Toasty.info(context, "DELETED", Toast.LENGTH_SHORT, true).show();
                FriendEntity friendEntity = new FriendEntity();
                friendEntity.setPhone(contact.getPhone());
                String mycontact = contact.getPhone().replaceAll("\\s", "").trim();
                if (contact.getPhone().contains("+")) {
                    mycontact = contact.getPhone().substring(1);
                }
                if (mycontact.length() == 12) {

                    deleteFriends(mycontact.substring(2), user_mobile);
                } else if (mycontact.length() == 13) {

                    deleteFriends(mycontact.substring(3), user_mobile);
                } else {

                    deleteFriends(mycontact, user_mobile);

                }
                new DeleteTask().execute(friendEntity);


                notifyDataSetChanged();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class DeleteTask extends AsyncTask<FriendEntity, Void, Void> {
        FriendEntity friendEntity;

        @Override
        protected Void doInBackground(FriendEntity... friendEntities) {
            mydb.myDao().deleteFriend(friendEntities[0]);
            friendEntity = friendEntities[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AddFriendsActivity addFriendsActivity = new AddFriendsActivity();
            Contacts contacts = new Contacts(friendEntity.getName(), friendEntity.getPhone());
            ReadTask readTask = new ReadTask();
            readTask.execute();
            notifyDataSetChanged();
            editor.putBoolean(friendEntity.getPhone(), false);
            editor.commit();

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ReadTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            List<FriendEntity> friends = mydb.myDao().getFriends();
            contactsList.clear();
            for (FriendEntity f : friends) {
                Contacts contacts = new Contacts(f.getName(), f.getPhone());
                contactsList.add(contacts);
            }
            notifyDataSetChanged();

            return null;
        }
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class FriendHolder extends RecyclerView.ViewHolder {
        TextView name, phone;
        Button removefriend;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pname);
            phone = itemView.findViewById(R.id.pnum);
            removefriend = itemView.findViewById(R.id.removefriend);
        }
    }

    private void deleteFriends(String numberM, String userMobile) {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, api_link_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("jstatus");

                    if (status.equals("1")) {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Volley: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
}
