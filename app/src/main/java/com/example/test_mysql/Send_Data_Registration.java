package com.example.test_mysql;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Salim3DD on 11/5/2020.
 */
public class Send_Data_Registration extends StringRequest {

    private static final String SEND_DATA_URL = MainActivity.Main_Link + "Registration.php";
    private Map<String, String> MapData;
    public Send_Data_Registration(
            String UserName,
            String UserEmail,
            String UserPassWord,
            String UserAvatar,

            Response.Listener<String> listener) {

        super(Method.POST, SEND_DATA_URL, listener, null);
        MapData = new HashMap<>();
        MapData.put("UserName", UserName);
        MapData.put("Email", UserEmail);
        MapData.put("Password", UserPassWord);
        MapData.put("ImgCode_Avatar", UserAvatar);

    }

    @Override
    public Map<String, String> getParams() {
        return MapData;
    }
}

