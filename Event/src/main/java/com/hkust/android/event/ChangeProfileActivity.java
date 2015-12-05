package com.hkust.android.event;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ChangeProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sp;
    private String oName;
    private String oPhone;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        setTitle("Change Profile");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userString = sp.getString("userString", "");
        Gson gson = new Gson();
        user = gson.fromJson(userString, User.class);
        TextView name = (TextView) findViewById(R.id.name_input);
        TextView phone = (TextView) findViewById(R.id.phone_input);

        oName = user.getName();
        oPhone = user.getPhone();

        name.setText(oName);
        phone.setText(oPhone);


        findViewById(R.id.update_profile_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_profile_btn:
                TextView name_view = (TextView) findViewById(R.id.name_input);
                TextView phone_view = (TextView) findViewById(R.id.phone_input);
                final String name = name_view.getText().toString();
                final String phone = phone_view.getText().toString();

                if ("".equalsIgnoreCase(name)) {
                    name_view.setError("This field required");
                } else if ("".equalsIgnoreCase(phone)) {
                    phone_view.setError("This field required");
                }else if(!android.util.Patterns.PHONE.matcher(phone).matches()){
                    phone_view.setError("Invalid phone number");
                }else{

                    AsyncHttpClient client = new AsyncHttpClient();

                    sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                    String token = sp.getString("token", "");
                    User user_temp = new User();

                    user_temp.setName(name);
                    user_temp.setPhone(phone);
                    user_temp.setToken(token);

                    user.setName(name);
                    user.setPhone(phone);

                    Gson gson = new Gson();

                    try {
                        StringEntity entity = new StringEntity(gson.toJson(user_temp));
                        client.post(this.getApplicationContext(), Constants.SERVER_URL + Constants.CHANGE_USER_INFO, entity, "application/json", new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String response = new String(responseBody);
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                    String message = jsonObject.getString("message");

                                    if (message.equalsIgnoreCase("succeed")) {
                                        Toast.makeText(getApplicationContext(), "Successfully, update profile!", Toast.LENGTH_SHORT).show();
                                        //update user profile.
                                        SharedPreferences.Editor editor = sp.edit();
                                        Gson gson = new Gson();
                                        String userString  = gson.toJson(user);
                                        editor.putString("userString",userString);
                                        editor.commit();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (resultCode == 200) {

                this.finish();
            }
        }
    }
}
