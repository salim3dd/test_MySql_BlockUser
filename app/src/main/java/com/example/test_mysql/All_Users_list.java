package com.example.test_mysql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class All_Users_list extends AppCompatActivity {


    private RecyclerView recyclerView;
    private static Recycler_ViewAdapter_all_users recyclerView_dAdapter;
    private static List<List_All_Users> mList;
    private RecyclerView.LayoutManager mLayoutManager;


    private String WordSearch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_list);

        recyclerView = findViewById(R.id.Show_Data_RecyclerView);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        mList = new ArrayList<>();
        recyclerView_dAdapter = new Recycler_ViewAdapter_all_users(mList, this);
        recyclerView.setAdapter(recyclerView_dAdapter);

        Get_All_Data();
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public void Get_All_Data() {

        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MainActivity.Main_Link + "get_All_Users.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);

                    mList.clear();
                    JSONArray jsonArray_usersS = jsonResponse.getJSONArray("All_Users");
                    //Log.d("jsonArray_usersS", "jsonArray_usersS" + jsonArray_usersS.length());
                    for (int i = 0; i < jsonArray_usersS.length(); i++) {
                        JSONObject responsS = jsonArray_usersS.getJSONObject(i);
                        String UserKey = responsS.getString("UserKey").trim();
                        String User_name = responsS.getString("UserName").trim();
                        String Email = responsS.getString("Email").trim();
                        String RegDate = responsS.getString("RegDate").trim();
                        String Avatar_img = responsS.getString("Avatar").trim();


                        List_All_Users myString = new List_All_Users();
                        myString.setUserKey(UserKey);
                        myString.setUser_name(User_name);
                        myString.setEmail(Email);
                        myString.setRegDate(RegDate);
                        myString.setAvatar_img(Avatar_img);
                        myString.setViewType(1);
                        mList.add(myString);
                    }


                    if (mList.size() > 0) {
                        recyclerView_dAdapter.notifyDataSetChanged();
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

        });

        queue.add(stringRequest);
        stringRequest.setShouldCache(false);


    }



    //END
}
