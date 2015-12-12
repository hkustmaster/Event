package com.hkust.android.event;


import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hkust.android.event.adapters.CropOptionAdapter;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.CropOption;
import com.hkust.android.event.model.User;
import com.hkust.android.event.tools.CircleImg;
import com.hkust.android.event.tools.ValidFormTools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SIGN UP ACTIVITY";
    private RequestParams params;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private Uri imgUri;
    CircleImg userImageUploadBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUpBtn = (Button) findViewById(R.id.sign_up_btn);
        TextView signinLink = (TextView) findViewById(R.id.link_signin);
        userImageUploadBtn = (CircleImg)findViewById(R.id.user_image_upload_btn);
        //userImageUploadBtn.setOnClickListener(this);
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
                    //user.setAvatar("default");

                    if (checkRegInfo(user, re_password)) {
                        try {
                            AsyncHttpClient client = new AsyncHttpClient();
                            Gson gson = new Gson();
                            StringEntity entity = new StringEntity(gson.toJson(user));
                            Log.i("pppp", gson.toJson(user));
                            client.post(this.getApplicationContext(), Constants.SERVER_URL + Constants.SIGN_UP, entity, "application/json", new AsyncHttpResponseHandler() {
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

                    } else {
                        Toast.makeText(SignUpActivity.this, "Error: please check your information!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.link_signin:
                this.finish();
                break;
            case R.id.user_image_upload_btn:
                Log.i("ppppp","image btn click");
                new AlertDialog.Builder(SignUpActivity.this).setTitle("选择头像")
                        .setPositiveButton("相册", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 方式1，直接打开图库，只能选择图库的图片
                                Intent i = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                // 方式2，会先让用户选择接收到该请求的APP，可以从文件系统直接选取图片
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(intent, PICK_FROM_FILE);

                            }
                        }).setNegativeButton("拍照", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        imgUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "avatar_"
                                + String.valueOf(System.currentTimeMillis())
                                + ".png"));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    }
                }).create().show();
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();
                break;
            case PICK_FROM_FILE:
                imgUri = data.getData();
                doCrop();
                break;
            case CROP_FROM_CAMERA:
                if (null != data) {
                    setCropImg(data);
                }
                break;
        }
    }


    private void doCrop() {

        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "can't find crop app", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            intent.setData(imgUri);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            // only one
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                // many crop app
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("choose a app");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (imgUri != null) {
                            getContentResolver().delete(imgUri, null, null);
                            imgUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    /**
     * set the bitmap
     *
     * @param picdata
     */
    private void setCropImg(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = bundle.getParcelable("data");
            userImageUploadBtn.setImageBitmap(mBitmap);
            saveBitmap(Environment.getExternalStorageDirectory() + "/crop_"
                    + System.currentTimeMillis() + ".png", mBitmap);
        }
    }

    /**
     * save the crop bitmap
     *
     * @param fileName
     * @param mBitmap
     */
    public void saveBitmap(String fileName, Bitmap mBitmap) {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
                Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

