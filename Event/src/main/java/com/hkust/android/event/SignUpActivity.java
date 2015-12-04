package com.hkust.android.event;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.User;
import com.hkust.android.event.tools.ValidFormTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "SIGN UP ACTIVITY";
    private RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUpBtn = (Button) findViewById(R.id.sign_up_btn);
        TextView signinLink = (TextView) findViewById(R.id.link_signin);

        signUpBtn.setOnClickListener(this);
        signinLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_btn:

                params = new RequestParams();
                TextView sign_up_email = (TextView) findViewById(R.id.sign_up_email);
                TextView sign_up_name = (TextView) findViewById(R.id.sign_up_name);
                TextView sign_up_password = (TextView) findViewById(R.id.sign_up_password);
                TextView sign_up_re_password = (TextView) findViewById(R.id.sign_up_re_password);
                RadioGroup sign_up_gender = (RadioGroup) findViewById(R.id.sign_up_gender);
                TextView sign_up_phone = (TextView) findViewById(R.id.sign_up_phone);

                String email = sign_up_email.getText().toString();
                String name = sign_up_name.getText().toString();
                String phone = sign_up_phone.getText().toString();
                String password = sign_up_password.getText().toString();
                String re_password = sign_up_re_password.getText().toString();
                String gender = new String();
                if (sign_up_gender.getCheckedRadioButtonId() != -1) {
                    RadioButton gender_button = (RadioButton) findViewById(sign_up_gender.getCheckedRadioButtonId());
                    gender = gender_button.getText().toString();
                }


                if ("".equalsIgnoreCase(email) ||
                        "".equalsIgnoreCase(name) ||
                        "".equalsIgnoreCase(phone) ||
                        "".equalsIgnoreCase(password) ||
                        "".equalsIgnoreCase(re_password) ||
                        "".equalsIgnoreCase(gender)) {
                    Toast.makeText(SignUpActivity.this, "All Fields Required.", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User();
                    user.setEmail(email);
                    user.setGender(gender);
                    user.setPassword(password);
                    user.setName(name);
                    user.setPhone(phone);
                    if (checkRegInfo(user, re_password)) {
                        try {
                            AsyncHttpClient client = new AsyncHttpClient();
                            Gson gson = new Gson();
                            StringEntity entity = new StringEntity(gson.toJson(user));
                            Log.i("pppp",gson.toJson(user));
                            client.post(this.getApplicationContext(), Constants.SERVER_URL+Constants.SIGN_UP, entity, "application/json", new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    String response = new String(responseBody);
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        String message = jsonObject.getString("message");
                                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                                        if (message.equalsIgnoreCase("succeed")) {
                                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


//                        params.put("name", user.getName());
//                        params.put("email", user.getEmail());
//                        params.put("password", user.getPassword());
//                        params.put("phone", user.getPhone());
//                        params.put("gender", user.getGender());
//
//                        AsyncHttpClient client = new AsyncHttpClient();
//                        client.post(Constants.SERVER_URL + Constants.SIGN_UP, params, new AsyncHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                if (statusCode == 200) {
//                                    String response = new String(responseBody);
//
//                                    JSONObject jsonObject = null;
//                                    try {
//                                        jsonObject = new JSONObject(response);
//                                        String message = jsonObject.getString("message");
//                                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
//
//                                        if (message.equalsIgnoreCase("succeed")) {
//                                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                            }
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                Toast.makeText(SignUpActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//                                error.printStackTrace();
//                            }
//                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Error: please check your information!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.link_signin:
                this.finish();
                break;
            default:
        }
    }


    /*
    * validate Register info
    * */
    private boolean checkRegInfo(User user, String re_password) {
        ValidFormTools validTools = new ValidFormTools();
        boolean flag = true;
        /*
        * 1. check
        * */

        TextView sign_up_email = (TextView) findViewById(R.id.sign_up_email);
        TextView sign_up_name = (TextView) findViewById(R.id.sign_up_name);
        TextView sign_up_re_password = (TextView) findViewById(R.id.sign_up_re_password);
        TextView sign_up_phone = (TextView) findViewById(R.id.sign_up_phone);
        TextView sign_up_password = (TextView) findViewById(R.id.sign_up_password);

        if (!validTools.isValidEmailAddress(user.getEmail())) {
            sign_up_email.setError("invalid email address");
            flag = false;
        }

        if (!validTools.isValidatePasswordLength(user.getPassword())) {
            sign_up_password.setError("between 6 and 18 alphanumeric characters");
            flag = false;
        }

        if (!validTools.isValidateName(user.getName())) {
            sign_up_name.setError("invalid name");
            flag = false;
        }

        if (!validTools.isValidatePassword(user.getPassword(), re_password)) {
            sign_up_re_password.setError("passwords do not match.");
            flag = false;
        }

        if (!validTools.isValidCellPhone(user.getPhone())) {
            sign_up_phone.setError("invalid phone");
            flag = false;
        }

        return flag;
    }
}

