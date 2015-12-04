package com.hkust.android.event;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hkust.android.event.model.User;
import com.loopj.android.http.AsyncHttpClient;

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
                String name = name_view.getText().toString();
                String phone = phone_view.getText().toString();

                if ("".equalsIgnoreCase(name)) {
                    name_view.setError("This field required");
                } else if ("".equalsIgnoreCase(phone)) {
                    phone_view.setError("This field required");
                }else if(!android.util.Patterns.PHONE.matcher(phone).matches()){
                    phone_view.setError("Invalid phone number");
                }else{

                    AsyncHttpClient client = new AsyncHttpClient();

                }
                break;
        }
    }
}
