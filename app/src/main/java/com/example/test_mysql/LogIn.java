package com.example.test_mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    private EditText ETXT_User_Name,  ETXTpassword;
    private SharedPreferences shared_Save;
    private String User_name = "", User_Password = "";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ETXT_User_Name = findViewById(R.id.ETXT_UserName);
        ETXTpassword = findViewById(R.id.ETXT_Pass);

        progressDialog = new ProgressDialog(this);

        shared_Save = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        findViewById(R.id.BTN_LogIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn();
            }
        });
    }


    void LogIn() {

        User_name = ETXT_User_Name.getText().toString().trim();
        User_Password = ETXTpassword.getText().toString().trim();

        progressDialog.setMessage("انتظر ارسال البيانات");
        progressDialog.setCancelable(true);
        progressDialog.show();

        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MainActivity.Main_Link + "LogIn.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                    String success = jsonResponse.getString("success");

                    if (success.contains("LogIn_OK")) {
                        Toast.makeText(LogIn.this, "تم تسجيل الدخول  ", Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray_usersS = jsonResponse.getJSONArray("Users_Data");
                            JSONObject responsS = jsonArray_usersS.getJSONObject(0);
                            String UserKey = responsS.getString("UserKey").trim();
                            String User_name = responsS.getString("UserName").trim();
                            String Email = responsS.getString("Email").trim();
                            String Avatar_img = responsS.getString("Avatar").trim();

                        SharedPreferences.Editor editor = shared_Save.edit();
                        editor.putString("Local_UserKey", UserKey.trim());
                        editor.putString("Local_UserName", User_name.trim());
                        editor.putString("Local_Email", Email.trim());
                        editor.putString("Local_PassWord", User_Password.trim());
                        editor.putString("Local_UserAvatar",  Avatar_img.trim());
                        editor.apply();

                        MainActivity.Local_UserKey = UserKey.trim();
                        MainActivity.Local_UserName = User_name.trim();
                        MainActivity.Local_UserEmail = Email.trim();
                        MainActivity.Local_UserAvatar = Avatar_img.trim();

                        MainActivity.UserKey = UserKey.trim();
                        MainActivity.UserName = User_name.trim();
                        MainActivity.UserEmail = Email.trim();
                        MainActivity.UserAvatar = Avatar_img.trim();

                        Log.d("UserKey==========>",UserKey);

                        startActivity(new Intent(LogIn.this, User_Profile.class));

                    }
                    if (success.contains("Error")) {
                        Toast.makeText(LogIn.this, "خطأ في البيانات", Toast.LENGTH_SHORT).show();

                    }

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
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
                params.put("UserName", User_name);
                params.put("PassWord", User_Password);
                return params;
            }
        };

        queue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }
}
