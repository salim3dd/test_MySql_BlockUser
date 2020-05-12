package com.example.test_mysql;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Salim3DD on 9/28/2016.
 */

public class Base64_Img_Compress_Reg {
    private Context context;

    public Base64_Img_Compress_Reg(Context context) {
        this.context = context;
    }

    public String Img_Compress(Bitmap bitmap, int com) {
        Bitmap resized_thumbs = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resized_thumbs.compress(Bitmap.CompressFormat.JPEG, com, byteArrayOutputStream);
        String compress = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compress;
    }

}
