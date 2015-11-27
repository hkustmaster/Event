package com.hkust.android.event;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hkust.android.event.adapters.MainPagerAdapter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawerLayout;
	private int statusBarColor;

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
        switch(v.getId()){
            case R.id.sign_out_btn:
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivityForResult(intent, 100);
                break;
//			case R.id.create_event_item:
//                Intent intent2 = new Intent(getApplicationContext(), NewEventActivity.class);
//                startActivityForResult(intent2, 100);
//                break;
			case R.id.change_password_btn:
				Intent intent3 = new Intent(getApplicationContext(), DateVotingActivity.class);
				startActivityForResult(intent3, 100);
				break;

            default:
                Toast.makeText(this, R.string.sheet_item_pressed, Toast.LENGTH_SHORT).show();

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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private int getStatusBarColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			return getWindow().getStatusBarColor();
		}
		return 0;
	}

	private void setStatusBarColor(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(color);
		}
	}
}
