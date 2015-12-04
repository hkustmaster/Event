package com.hkust.android.event;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.User;
import com.hkust.android.event.tools.ValidFormTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private SharedPreferences sp;

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String autoLogin = sp.getString("autoLogin","");
        String email=sp.getString("email", "");
        String password =sp.getString("password", "");

        _emailText.setText(email);
        _passwordText.setText(password);

        if(autoLogin.equalsIgnoreCase("true")){
            login();
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this,
                R.style.Base_Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        // send request to server
        AsyncHttpClient client = new AsyncHttpClient();
        // set up the parameters
//        RequestParams params = new RequestParams();
//        params.put("email", email);
//        params.put("password", password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        final Gson gson = new Gson();

        // execute post method
        try {
            StringEntity entity = new StringEntity(gson.toJson(user));
            client.post(this.getApplicationContext(), Constants.SERVER_URL + Constants.SIGN_IN, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show();
                        if (message.equalsIgnoreCase("succeed")) {

                            //get user json
                            String userString = jsonObject.getString("user");
                            String token = jsonObject.getString("token");

                            // re-package user
                            User userLogined = gson.fromJson(userString, User.class);
                            userLogined.setToken(token);


                            //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
                            // 第一个参数是存储时的名称，第二个参数则是文件的打开方式
                            /*
                            * SharedPreferences的四种操作模式:
                                Context.MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
                                Context.MODE_APPEND：模式会检查文件是否存在,存在就往文件追加内容,否则就创建新文件.
                                Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件.
                                MODE_WORLD_READABLE：表示当前文件可以被其他应用读取.
                                MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入.
                            */
                            sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            //2、让setting处于编辑状态
                            SharedPreferences.Editor editor = sp.edit();
                            //3、存放数据
//                            editor.putString("userName",user.getName());
//                            editor.putString("userEmail",user.getEmail());
//                            editor.putString("userPhone",user.getPhone());
                            editor.putString("autoLogin","true");
                            editor.putString("email",email);
                            editor.putString("password",password);
                            editor.putString("token", token);
                            editor.putString("userString", userString);
                            //4、完成提交
                            editor.commit();

//                            Toast.makeText(getApplicationContext(), user.getName(), Toast.LENGTH_LONG).show();
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            // On complete call either onLoginSuccess or onLoginFailed
                                            onLoginSuccess();
                                            //onLoginFailed();
                                            progressDialog.dismiss();
                                        }
                                    }, 2000);
                            _loginButton.setEnabled(true);
                        } else {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            // On complete call either onLoginSuccess or onLoginFailed
                                            //onLoginSuccess();
                                            onLoginFailed();
                                            progressDialog.dismiss();
                                        }
                                    }, 2000);
                            _loginButton.setEnabled(true);
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

        } catch (Exception e) {
            e.printStackTrace();
        }

//        client.post(url, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                if (statusCode == 200) {
//                    String response = new String(responseBody);
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = new JSONObject(response);
//                        String message = jsonObject.getString("message");
//                        String t = new String(responseBody);
//                        //Toast.makeText(SignInActivity.this, t, Toast.LENGTH_LONG).show();
//
//                        if (message.equalsIgnoreCase("succeed")) {
//                            String token = jsonObject.getString("token");
//                            //store token and username
//                            //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
//                            SharedPreferences settings = getSharedPreferences("setting", 0);
//                            //2、让setting处于编辑状态
//                            SharedPreferences.Editor editor = settings.edit();
//                            //3、存放数据
//                            editor.putString("token", token);
//                            //4、完成提交
//                            editor.commit();
//                            //Toast.makeText(SignInActivity.this, token, Toast.LENGTH_LONG).show();
//                            new android.os.Handler().postDelayed(
//                                    new Runnable() {
//                                        public void run() {
//                                            // On complete call either onLoginSuccess or onLoginFailed
//                                            onLoginSuccess();
//                                            //onLoginFailed();
//                                            progressDialog.dismiss();
//                                        }
//                                    }, 2000);
//                            _loginButton.setEnabled(true);
//                        } else {
//                            new android.os.Handler().postDelayed(
//                                    new Runnable() {
//                                        public void run() {
//                                            // On complete call either onLoginSuccess or onLoginFailed
//                                            //onLoginSuccess();
//                                            onLoginFailed();
//                                            progressDialog.dismiss();
//                                        }
//                                    }, 2000);
//                            _loginButton.setEnabled(true);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {

        //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 18) {
            _passwordText.setError("between 6 and 18 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

