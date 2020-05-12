package com.example.test_mysql;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Salim3DD on 11/5/2020.
 */
public class Send_Data_Update extends StringRequest {

    private static final String SEND_DATA_URL = MainActivity.Main_Link + "UpdateUserProfile.php";
    private Map<String, String> MapData;
    public Send_Data_Update(

            String UserName,
            String UserEmail,
            String UserPassword,
            String ImgCode_Avatar,

            Response.Listener<String> listener) {

        super(Method.POST, SEND_DATA_URL, listener, null);
        MapData = new HashMap<>();
        MapData.put("UserKey", MainActivity.Local_UserKey);
        MapData.put("UserName", UserName);
        MapData.put("Email", UserEmail);
        MapData.put("UserPassword", UserPassword);
        MapData.put("AvatarFileName", MainActivity.Local_UserAvatar);
        MapData.put("ImgCode_Avatar", ImgCode_Avatar);

    }

    @Override
    public Map<String, String> getParams() {
        return MapData;
    }
}

