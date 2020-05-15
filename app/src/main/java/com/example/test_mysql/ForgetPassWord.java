package com.example.test_mysql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.regex.Pattern;

public class ForgetPassWord extends AppCompatActivity {
    EditText TXTEmail;
    String Email;
    TextView TextCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_word);
        TextCheck = findViewById(R.id.TextCheck);
        TXTEmail = findViewById(R.id.EDTXT_Email);
        findViewById(R.id.BTN_Send_D_forgetPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = TXTEmail.getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(ForgetPassWord.this, "يجب كتابة البريد الالكتروني أولا", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkEmail(Email)) {
                        SendForgetPassWord(Email);
                    } else {
                        Toast.makeText(ForgetPassWord.this, "يجب كتابة البريد الالكتروني بشكل صحيح", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private void SendForgetPassWord(final String MyEmail) {


        TextCheck.setText("انتظر التحقق من البريد الالكتروني...");


        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MainActivity.Main_Link +"PHPMailer/PHPMailer_sendEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                    String success = jsonResponse.getString("success");


                    if (success.contains("Send_OK")) {
                        TextCheck.setText("تم إرسال بيانات حسابك إلى بريدك الالكتروني");
                    }
                    if (success.contains("No_Email")) {
                        TextCheck.setText("لم يتم العثور على البريد الالكتروني");
                    }
                    if (success.contains("Send_Error")) {
                        TextCheck.setText("حدث خطأ في إرسال البريد الالكتروني");
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
                params.put("email", MyEmail);
                return params;
            }
        };

        queue.add(stringRequest);
        stringRequest.setShouldCache(false);


    }


//END
}