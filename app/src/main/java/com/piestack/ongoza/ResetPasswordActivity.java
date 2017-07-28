package com.piestack.ongoza;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.email)
    EditText edEmail;
    @BindView(R.id.newpassword)
    EditText edNewPassword;
    @BindView(R.id.phone)
    EditText edPhone;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private String responses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ButterKnife.bind(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.btn_back)
    public void back(){
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_reset_password)
    public void resetPassword(){
        updatePassword();
    }

    private void updatePassword(){

        progressBar.setVisibility(View.VISIBLE);

        String email = edEmail.getText().toString().trim();
        String password = edNewPassword.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);

        Map<String, String> headers = new HashMap<String, String>();
        params.put("Accept", "application/json");
        params.put("Content-Type", "application/x-www-form-urlencoded");

        RequestBody formBody = new FormBody.Builder()
                .add("email",email)
                .add("password",password)
                .add("phone",phone)
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Config.updateUrl)
                .post(formBody)
                .build();

        try{
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    L.e("ResetPassword", e.getMessage());

                    ResetPasswordActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                    General.backgroundThreadShortToast(ResetPasswordActivity.this, "Check your internet connection");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    responses = response.body().string();
                    L.json("Reset Password",responses);

                    ResetPasswordActivity.this.runOnUiThread(new Runnable() {
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

                            General.backgroundThreadShortToast(ResetPasswordActivity.this, "Password change successful! Please login");

                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }else{

                            L.e("ResetPassword", loginResponse.getErrorMsg());
                            General.backgroundThreadShortToast(ResetPasswordActivity.this, "Email and password do not match");
                        }
                    }else {
                        ResetPasswordActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                        General.backgroundThreadShortToast(ResetPasswordActivity.this, "Please try again later");
                    }





                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }



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
