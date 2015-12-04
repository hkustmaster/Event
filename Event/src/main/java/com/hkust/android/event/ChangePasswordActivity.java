package com.hkust.android.event;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("Change Password");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.update_password_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_password_btn:

                TextView current_password_view = (TextView) findViewById(R.id.current_password_input);
                TextView new_Password_view = (TextView) findViewById(R.id.new_password_input);
                TextView re_new_Password_view = (TextView) findViewById(R.id.re_new_password_input);
                String c_password = current_password_view.getText().toString();
                String new_password = new_Password_view.getText().toString();
                String re_new_password = re_new_Password_view.getText().toString();


                if ("".equalsIgnoreCase(c_password)) {
                    current_password_view.setError("This field required");
                } else if ("".equalsIgnoreCase(new_password)) {
                    new_Password_view.setError("This field required");
                } else if ("".equalsIgnoreCase(re_new_password)) {
                    re_new_Password_view.setError("This field required");
                } else if (new_password.length() < 6 || new_password.length() > 18) {
                    new_Password_view.setError("between 6 and 18 alphanumeric characters");
                } else {
                    //check current password.
                    sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    String oldPassword = sp.getString("password", "");
                    if (oldPassword.equals(c_password)) {
                        // current password ok!
                        if (new_password.equals(re_new_password)) {
                            // new password and re-password ok!
                            AsyncHttpClient client = new AsyncHttpClient();
                            //client.post();

                        } else {
                            re_new_Password_view.setError("Passwords do not match");
                        }
                    } else {
                        current_password_view.setError("Old password does not match");
                    }

                }
                break;
            default:
        }
    }
}
