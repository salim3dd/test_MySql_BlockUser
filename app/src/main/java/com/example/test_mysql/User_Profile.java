package com.example.test_mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User_Profile extends AppCompatActivity {

    private TextView Text_ShowUserName, Text_ShowEmail;
    private ImageView imageView_ShowAvatar;
    Button BTN_Edit,BTN_Delete;
    private SharedPreferences shared_getData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Text_ShowUserName = findViewById(R.id.Text_ShowUserName);
        Text_ShowEmail = findViewById(R.id.Text_ShowEmail);

        imageView_ShowAvatar = findViewById(R.id.imageView_ShowAvatar);

        Text_ShowUserName.setText(MainActivity.UserName);
        Text_ShowEmail.setText(MainActivity.UserEmail);


        try {
            String UserAvatar = MainActivity.Main_Link + "Images/" + MainActivity.UserAvatar;
            Picasso.with(this).load(UserAvatar)
                    .error(R.drawable.avatar)
                    .placeholder(R.drawable.avatar).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .into(imageView_ShowAvatar);
        } catch (Exception e) {
        }

        BTN_Delete= findViewById(R.id.BTN_Delete);
        BTN_Edit = findViewById(R.id.BTN_Edit);

        if (MainActivity.Local_UserKey.equals(MainActivity.UserKey)){
            BTN_Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(User_Profile.this, UpdateUser_Profile.class));
                }
            });

            BTN_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Delete_User();
                }
            });
        }else{
            BTN_Edit.setVisibility(View.INVISIBLE);
            BTN_Delete.setVisibility(View.INVISIBLE);
            BTN_Delete.setEnabled(false);
            BTN_Edit.setEnabled(false);
        }

    }



    public void Delete_User() {

        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MainActivity.Main_Link + "Delete_User.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                    String success = jsonResponse.getString("success");

                    if (success.contains("Delete_OK")) {
                        Toast.makeText(User_Profile.this, "تم حذف البيانات", Toast.LENGTH_SHORT).show();
                        SingOut();
                        startActivity(new Intent(User_Profile.this, MainActivity.class));

                    }
                    if (success.contains("Error")) {
                        Toast.makeText(User_Profile.this, "حدث خطأ لم يتم حذف البيانات", Toast.LENGTH_SHORT).show();

                    }

                    queue.stop();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserKey", MainActivity.UserKey);
                return params;
            }
        };

        queue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }



    private void SingOut() {

        MainActivity.Local_UserKey = "";
        MainActivity.Local_UserName = "";
        MainActivity.Local_UserEmail = "";
        MainActivity.Local_UserAvatar = "";

        shared_getData = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_getData.edit();
        editor.remove("Local_User_id");
        editor.remove("Local_UserName");
        editor.remove("Local_PassWord");
        editor.remove("Local_Email");
        editor.remove("Local_UserAvatar");

        editor.apply();

    }
}
