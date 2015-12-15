package com.hkust.android.event;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hkust.android.event.adapters.MainPagerAdapter;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.User;
import com.hkust.android.event.service.NotificationService;
import com.hkust.android.event.tools.CircleImg;
import com.hkust.android.event.tools.FileUtil;
import com.hkust.android.event.tools.SelectPicPopupWindow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private SharedPreferences sp;


    //上传头像
    private Context mContext;
    private CircleImg avatarImg;// avatar img
    private SelectPicPopupWindow menuWindow;
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// avatar filename
    private String urlpath;            // local pic path
    private static ProgressDialog pd;// progress
    private static final int REQUESTCODE_PICK = 0;        // gallery
    private static final int REQUESTCODE_TAKE = 1;        // take a picture
    private static final int REQUESTCODE_CUTTING = 2;    // cut picture
    private AsyncHttpClient client;
    private String uEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.notes);
        setContentView(R.layout.activity_main);
        setupActionBar();
        setupDrawer();
        setupTabs();

        mContext = MainActivity.this;

        findViewById(R.id.sign_out_btn).setOnClickListener(this);
        findViewById(R.id.change_password_btn).setOnClickListener(this);
        findViewById(R.id.edit_profile_btn).setOnClickListener(this);
        findViewById(R.id.notes_list);

        avatarImg = (CircleImg) findViewById(R.id.imageView);
        avatarImg.setOnClickListener(this);

        setProfile();
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }


    private void setProfile() {
        //get user info
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userString = sp.getString("userString", "");
        String token = sp.getString("token","");
        Gson gson = new Gson();
        User user = gson.fromJson(userString, User.class);

        TextView userName = (TextView) findViewById(R.id.username_textview);
        TextView userEmail = (TextView) findViewById(R.id.email_textview);
        TextView userPhone = (TextView) findViewById(R.id.phone_textview);

        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhone());

        uEmail=user.getEmail();

        Log.i("pppppp", token);

        //get Avatar from local storage
        String tmpUrl = mContext.getFilesDir()+"/event/"+uEmail+"/temphead.jpg";
        if(FileUtil.fileIsExists(tmpUrl)){
            //local storage has Avatar
            Bitmap bmp = BitmapFactory.decodeFile(tmpUrl);
            avatarImg.setImageBitmap(bmp);
        }else{
            getAvatar();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * Sets up the action bar.
     */
    private void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * Sets up the navigation drawer.
     */
    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.opendrawer,
                R.string.closedrawer);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    /**
     * Sets up the tabs.
     */
    private void setupTabs() {
        // Setup view pager
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new MainPagerAdapter(this, getSupportFragmentManager()));
        viewpager.setOffscreenPageLimit(MainPagerAdapter.NUM_ITEMS);

        // Setup tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * Toggles opening/closing the drawer.
     */
    private void toggleDrawer() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                //click avatar
                menuWindow = new SelectPicPopupWindow(mContext, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.drawer_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.sign_out_btn:
                sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("autoLogin", "false");
                editor.putString("userString", "");
                editor.putString("token", "");
                editor.putString("email", "");
                editor.putString("password", "");
                editor.commit();
                Intent serviceIntent = new Intent(this, NotificationService.class);
                stopService(serviceIntent);
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.change_password_btn:
                Intent intent2 = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(intent2);
                break;
            case R.id.edit_profile_btn:
                Intent intent3 = new Intent(MainActivity.this, ChangeProfileActivity.class);
                startActivityForResult(intent3, 200);
            default:
                // Toast.makeText(this, R.string.sheet_item_pressed, Toast.LENGTH_SHORT).show();
        }

    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // take a picture
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //get picture location
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    //Log.i("ppppp",Environment.getExternalStorageDirectory()+"");
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // from gallery
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // limit picture type
                    pickIntent.setType("image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggleDrawer();
                return true;
            case R.id.menu_main_add:
                Intent intent = new Intent(this.getApplicationContext(), NewEventActivity.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:// from photo gallery
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// cancel operation
                }
                break;
            case REQUESTCODE_TAKE:// take a picture
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// cut picture
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        if (requestCode == 100 && resultCode == 100) {
            sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("autoLogin", "false");
            editor.putString("userString", "");
            editor.putString("token", "");
            editor.putString("password", "");
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivityForResult(intent, 100);
            finish();
        } else if (requestCode == 200 && resultCode == 200) {
            setProfile();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * the cut picture method
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true
        intent.putExtra("crop", "true");
        // aspectX aspectY
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * save the cut picture
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // get picture path
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo, uEmail);
            avatarImg.setImageDrawable(drawable);

            //Log.i("ppppp1",urlpath);
            // upload picture to server
            // set a progress dialog to display the progress
            pd = new ProgressDialog(mContext, R.style.Base_Theme_AppCompat_Light_Dialog);
            pd.setIndeterminate(true);
            pd.setMessage("Uploading...");
            pd.show();

            // set a listener to stop upload
            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    client.cancelAllRequests(true);
                    pd.dismiss();
                }
            });
            // start upload pic
            try{
                client = new AsyncHttpClient();

                String token = sp.getString("token", "");
                RequestParams params = new RequestParams();
                params.put("token", token);
                params.put("ext", "jpg");

                File file = new File(urlpath);

                //Log.i("PPPP",urlpath);
                params.put("picc", file);

                client.post(Constants.SERVER_URL + Constants.UPLOAD_AVATAR, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String response = new String(responseBody);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if(message.equalsIgnoreCase("succeed")){

                            }
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                    }

                });
                pd.dismiss();

            }catch (Exception e){
                e.printStackTrace();
            }
            // end upload pic
        }
    }

    /*
    * get Avatar from server
    * */
    private void getAvatar(){
        try{
            client = new AsyncHttpClient();

            String token = sp.getString("token", "");
            RequestParams params = new RequestParams();
            params.put("token", token);

            client.post(Constants.SERVER_URL + Constants.GET_AVATAR, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        Log.i("ppppp",message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
