package com.hkust.android.event;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hkust.android.event.adapters.MainPagerAdapter;
import com.hkust.android.event.model.User;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.notes);
        setContentView(R.layout.activity_main);
        setupActionBar();
        setupDrawer();
        setupTabs();

        findViewById(R.id.sign_out_btn).setOnClickListener(this);
        findViewById(R.id.change_password_btn).setOnClickListener(this);
        findViewById(R.id.edit_profile_btn).setOnClickListener(this);
        findViewById(R.id.notes_list);

        setProfile();

    }

    private void setProfile(){
        //get user info
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userString=sp.getString("userString", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userString, User.class);

        TextView userName = (TextView) findViewById(R.id.username_textview);
        TextView userEmail = (TextView) findViewById(R.id.email_textview);
        TextView userPhone = (TextView) findViewById(R.id.phone_textview);
        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhone());
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
            case R.id.sign_out_btn:
                sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor =sp.edit();
                editor.putString("autoLogin","false");
                editor.putString("userString","");
                editor.putString("token", "");
                editor.commit();
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
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==100){
            sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =sp.edit();
            editor.putString("autoLogin","false");
            editor.putString("userString","");
            editor.putString("token", "");
            editor.putString("password", "");
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivityForResult(intent, 100);
            finish();
        }else if(requestCode==200&&resultCode==200){
            setProfile();
        }
    }
}
