package com.piestack.ongoza;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.piestack.ongoza.app.Config;
import com.piestack.ongoza.app.MyApplication;
import com.piestack.ongoza.models.LoginResponse;
import com.piestack.ongoza.models.User;
import com.piestack.ongoza.utils.General;
import com.piestack.ongoza.utils.L;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.email)
    EditText edEmail;
    @BindView(R.id.password)
    EditText edPassword;

    private String responses;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         * Check for login session. It user is already logged in
         * redirect him to main activity
         * */
        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login(){

        progressBar.setVisibility(View.VISIBLE);

        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        Map<String, String> headers = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Content-Type", "application/x-www-form-urlencoded");
       /* OkHttpUtils//
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"))
                .headers(headers)
                .content(new Gson().toJson(params))
                .build()
                .execute(new MyStringCallback());
        OkHttpUtils
                .post()
                .url(url)
                .addParams("email","jeffnyauke@gmail.com")
                .addParams("password","newaccount")
                .build()
                .execute(new MyStringCallback());*/

        RequestBody formBody = new FormBody.Builder()
                .add("email",email)
                .add("password",password)
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Config.loginUrl)
                .post(formBody)
                .build();

        try{
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    L.e(TAG, e.getMessage());

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                    General.backgroundThreadShortToast(LoginActivity.this, "Check your internet connection");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    responses = response.body().string();
                    L.json(TAG,responses);

                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                    if(isJSONValid(responses)){
                        Gson gson = new Gson();
                        LoginResponse loginResponse =  gson.fromJson(responses,LoginResponse.class);
                        User user = loginResponse.getUser();

                        if(!loginResponse.getError()) {

                            MyApplication.getInstance().getPrefManager().storeUser(user);

                            General.backgroundThreadShortToast(LoginActivity.this, "Welcome " + user.getName());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }else{

                            L.e(TAG, loginResponse.getErrorMsg());
                            General.backgroundThreadShortToast(LoginActivity.this, "Wrong credentials");
                        }
                    }else {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                        General.backgroundThreadShortToast(LoginActivity.this, "Please try again later");
                    }





                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @OnClick(R.id.btn_reset_password)
    public void forgot(){
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public boolean isJSONValid(String test){
        try{
            Gson gson = new Gson();
            gson.fromJson(test, LoginResponse.class);
            return  true;
        }catch (JsonSyntaxException ex){
            return false;
        }
    }


}
