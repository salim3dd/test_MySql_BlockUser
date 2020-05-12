package com.example.test_mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateUser_Profile extends AppCompatActivity {

    private EditText ETXT_User_Name, ETXT_Email, ETXTpassword;
    private ImageView imageView_avatar;
    private String User_name = "", User_Email = "", User_Password = "";

    private Boolean isAvatarChange = false;
    private SharedPreferences shared_Save;
    Button BTN_Update;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_userprofile);

        ETXT_User_Name = findViewById(R.id.ETXT_UserName);
        ETXT_Email = findViewById(R.id.ETXT_Email);
        ETXTpassword = findViewById(R.id.ETXT_Pass);

        imageView_avatar = findViewById(R.id.imageView_avatar);

        ETXT_User_Name.setText(MainActivity.UserName);
        ETXT_Email.setText(MainActivity.UserEmail);

        shared_Save = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);

        try {
            String UserAvatar = MainActivity.Main_Link + "Images/" + MainActivity.UserAvatar;
            Picasso.with(UpdateUser_Profile.this).load(UserAvatar)
                    .error(R.drawable.avatar)
                    .placeholder(R.drawable.avatar).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                    .into(imageView_avatar);


        } catch (Exception e) {
        }

        BTN_Update = findViewById(R.id.BTN_Update);
        BTN_Update.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HardwareIds")
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });

    }



    void UpdateData() {

        User_name = ETXT_User_Name.getText().toString().trim();
        User_Email = ETXT_Email.getText().toString().trim();
        User_Password = ETXTpassword.getText().toString().trim();


        if (TextUtils.isEmpty(User_name) || TextUtils.isEmpty(User_Email) || TextUtils.isEmpty(User_Password)) {
            Toast.makeText(this, "يجب تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();

        } else {


            String ImgCode_Avatar = "";
            if (isAvatarChange) {
                Bitmap Bimg = ((BitmapDrawable) imageView_avatar.getDrawable()).getBitmap();
                Bitmap bitmap = getCroppedBitmap(Bimg);
                Base64_Img_Compress_Reg compress = new Base64_Img_Compress_Reg(this);
                ImgCode_Avatar = compress.Img_Compress(bitmap, 70);
            }

            progressDialog.setMessage("انتظر تحديث البيانات");
            progressDialog.setCancelable(true);
            progressDialog.show();
            BTN_Update.setEnabled(false);

                Response.Listener<String> responseLisener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonResponse = jsonArray.getJSONObject(0);

                            String success = jsonResponse.getString("success");
                            String NewAvatar = jsonResponse.getString("NewAvatar");

                            if (success.contains("Update_OK")) {
                                Toast.makeText(UpdateUser_Profile.this, "تم إرسال البيانات بشكل صحيح", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = shared_Save.edit();

                                editor.putString("Local_UserName", User_name);
                                editor.putString("Local_PassWord", User_Email);
                                editor.putString("Local_Email", User_Password);
                                editor.putString("Local_UserAvatar",  NewAvatar);
                                editor.apply();

                                if(TextUtils.isEmpty(NewAvatar)&& isAvatarChange) {
                                    MainActivity.Local_UserAvatar = NewAvatar;
                                }
                                MainActivity.Local_UserEmail = User_Email;
                                MainActivity.Local_UserName = User_name;

                                MainActivity.UserAvatar = MainActivity.Local_UserAvatar;
                                MainActivity.UserKey = MainActivity.Local_UserKey;
                                MainActivity.UserName = User_name.trim();
                                MainActivity.UserEmail = User_Email.trim();

                                startActivity(new Intent(UpdateUser_Profile.this, User_Profile.class));


                            } else if (success.contains("Error")) {
                                Toast.makeText(UpdateUser_Profile.this, "عذرا حدث خطأ لم يتم تحديث البيانات", Toast.LENGTH_SHORT).show();

                                BTN_Update.setEnabled(true);
                            }

                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Send_Data_Update send_Data = new Send_Data_Update(
                        User_name,
                        User_Email,
                        User_Password,
                        ImgCode_Avatar,
                        responseLisener);
                RequestQueue queue = Volley.newRequestQueue(UpdateUser_Profile.this);
                queue.add(send_Data);

        }
    }



    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public void imageView_avatar(View view) {
        Add_Avatar();
    }

    public void Add_Avatar() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            int colorYallow = Color.argb(255, 244, 171, 54);
            int colorRed = Color.argb(255, 255, 111, 0);
            int colorBlack = Color.argb(150, 0, 0, 0);
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setActivityTitle(getResources().getString(R.string.app_name))
                    .setAutoZoomEnabled(true)
                    .setBorderCornerColor(colorRed)
                    .setBackgroundColor(colorBlack)
                    .setBorderLineColor(colorYallow)
                    .setBorderLineThickness(2)
                    .setMaxCropResultSize(4000, 4000)
                    .setAllowCounterRotation(true)
                    .setAllowRotation(true)
                    .setAutoZoomEnabled(true)
                    .setFixAspectRatio(true)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageView_avatar.setImageURI(resultUri);
                isAvatarChange = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    //end
}
