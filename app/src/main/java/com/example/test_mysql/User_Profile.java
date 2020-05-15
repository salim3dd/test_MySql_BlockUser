package com.example.test_mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

    private TextView Text_ShowUserName, Text_ShowEmail, Text_active;
    private ImageView imageView_ShowAvatar;
    Button BTN_Edit,BTN_Delete, BTN_SendActiveCode;
    private EditText ETXT_active_code;
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
        BTN_SendActiveCode = findViewById(R.id.BTN_Send_ActiveCode);

        Text_active = findViewById(R.id.text_active_code);
        ETXT_active_code = findViewById(R.id.ETXT_active_code);

        if (MainActivity.Local_UserActiveCode==1) {

            Text_active.setVisibility(View.INVISIBLE);
            BTN_SendActiveCode.setVisibility(View.INVISIBLE);
            BTN_SendActiveCode.setEnabled(false);
            ETXT_active_code.setVisibility(View.INVISIBLE);
            ETXT_active_code.setEnabled(false);

            BTN_Edit.setVisibility(View.VISIBLE);
            BTN_Delete.setVisibility(View.VISIBLE);
            BTN_Delete.setEnabled(true);
            BTN_Edit.setEnabled(true);

            if (MainActivity.Local_UserKey.equals(MainActivity.UserKey)) {
                BTN_Edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(User_Profile.this, UpdateUser_Profile.class));
                    }
                });

                BTN_Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String msg = "<font color='#ff0000'>سيتم إلغاء الحساب نهائياً</font>";
                        String title = "<font color='#ff0000'>إلغاء الحساب</font>";
                        AlertDialog.Builder build = new AlertDialog.Builder(User_Profile.this);
                        build.setTitle(Html.fromHtml(title))
                                .setIcon(R.mipmap.ic_launcher)
                                .setMessage(Html.fromHtml(msg))
                                .setPositiveButton("إلغاء الحساب", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        Delete_User();
                                    }
                                })
                                .setNegativeButton("تراجع", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).show();


                    }
                });
            } else {
                BTN_Edit.setVisibility(View.INVISIBLE);
                BTN_Delete.setVisibility(View.INVISIBLE);
                BTN_Delete.setEnabled(false);
                BTN_Edit.setEnabled(false);
            }

        }else{
            BTN_Edit.setVisibility(View.INVISIBLE);
            BTN_Delete.setVisibility(View.INVISIBLE);
            BTN_Delete.setEnabled(false);
            BTN_Edit.setEnabled(false);

            Text_active.setVisibility(View.VISIBLE);
            BTN_SendActiveCode.setVisibility(View.VISIBLE);
            BTN_SendActiveCode.setEnabled(true);

            BTN_SendActiveCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Send_ActiveCode(ETXT_active_code.getText().toString().trim());
                }
            });


        }

    }


    //حذف الحساب
     void Delete_User() {

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
                params.put("UserKey", MainActivity.Local_UserKey);
                return params;
            }
        };

         stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                 DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }


    //حذف بيانات المستخدم
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


    //رمز التفعيل
    void Send_ActiveCode(final String code) {

        BTN_SendActiveCode.setEnabled(false);
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MainActivity.Main_Link + "ActiveCode.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                    String success = jsonResponse.getString("success");

                    if (success.contains("Active_OK")) {

                        BTN_Edit.setVisibility(View.VISIBLE);
                        BTN_Delete.setVisibility(View.VISIBLE);
                        BTN_Delete.setEnabled(true);
                        BTN_Edit.setEnabled(true);

                        Text_active.setText("تم تفعيل الحساب بنجاح");

                        shared_getData = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared_getData.edit();
                        editor.putInt("Local_UserActiveCode",1);

                        editor.apply();

                        MainActivity.Local_UserActiveCode = 1;

                        ETXT_active_code.setVisibility(View.INVISIBLE);
                        ETXT_active_code.setEnabled(false);

                        BTN_SendActiveCode.setVisibility(View.INVISIBLE);
                        BTN_SendActiveCode.setEnabled(false);

                    }
                    if (success.contains("Code_Error")) {
                        Toast.makeText(User_Profile.this, "حدث خطأ رمز التفعيل غير صحيح", Toast.LENGTH_SHORT).show();
                        BTN_SendActiveCode.setEnabled(true);

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
                params.put("ActiveCode", code);
                params.put("UserKey", MainActivity.Local_UserKey);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }


    //END
}
