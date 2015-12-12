package com.hkust.android.event.fragments;

import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.hkust.android.event.R;
import com.hkust.android.event.model.Constants;


public class MyEventFragment extends NotesListFragment {
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        return;
    }

    @Override
    public void onConnectionSuspended(int i) {
return;
    }

    public static MyEventFragment newInstance() {
        return new MyEventFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_pending;
    }

    @Override
    protected int getNumColumns() {
        return 1;
    }

    @Override
    protected String getTagName() {
        return Constants.MYEVENT_FRAGMENT;
    }


}
