package com.example.test_mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

     public static String Main_Link = "https://novbook.net/test/android/";

     public static String Local_UserKey , Local_UserName, Local_UserEmail, Local_UserAvatar;

     public static int Local_UserActiveCode=0;

     public static String UserKey , UserName, UserEmail, UserAvatar;

    private SharedPreferences shared_getData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shared_getData = getSharedPreferences("UserData", Context.MODE_PRIVATE);

       findViewById(R.id.BTN_Show_Data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, All_Users_list.class));
            }
        });


        findViewById(R.id.BTN_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Registration.class));
            }
        });


        findViewById(R.id.BTN_LogIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LogIn.class));
            }
        });

        findViewById(R.id.BTN_ForgetPassWord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgetPassWord.class));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        Local_UserKey = shared_getData.getString("Local_UserKey", "").trim();
        Local_UserName = shared_getData.getString("Local_UserName", "").trim();
        Local_UserEmail = shared_getData.getString("Local_Email", "").trim();
        Local_UserAvatar = shared_getData.getString("Local_UserAvatar", "").trim();
        Local_UserActiveCode = shared_getData.getInt("Local_UserActiveCode", 0);


    }
}
