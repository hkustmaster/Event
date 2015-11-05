package com.hkust.android.event.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hkust.android.event.fragments.ExploreFragment;
import com.hkust.android.event.fragments.MyEventFragment;
import com.hkust.android.event.R;
import com.hkust.android.event.fragments.PendingFragment;

/**
 * Created by Gordon Wong on 7/17/2015.
 *
 * Pager adapter for main activity.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

	public static final int NUM_ITEMS = 3;
	public static final int EXPLORE_POS = 0;
	public static final int MYEVENT_POS = 1;
	public static final int PENDING_POS = 2;

	private Context context;

	public MainPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case EXPLORE_POS:
			return ExploreFragment.newInstance();
		case MYEVENT_POS:
			return MyEventFragment.newInstance();
		case PENDING_POS:
			return PendingFragment.newInstance();
		default:
			return null;
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case EXPLORE_POS:
			return context.getString(R.string.explore);
		case MYEVENT_POS:
			return context.getString(R.string.myEvent);
		case PENDING_POS:
			return context.getString(R.string.pending);
		default:
			return "";
		}
	}

	@Override
	public int getCount() {
		return NUM_ITEMS;
	}
}
