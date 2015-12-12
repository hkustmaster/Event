package com.hkust.android.event.fragments;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.hkust.android.event.R;
import com.hkust.android.event.model.Constants;


public class ExploreFragment extends NotesListFragment {
    private final static String TAG = "ExploreFragment";
    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_explore;
    }

    @Override
    protected int getNumColumns() {
        return 1;
    }

    @Override
    protected String getTagName() {
        return Constants.EXPLORE_FRAGMENT;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG,"onConnectionFailed");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG,"onConnectionSuspended");
    }


}
