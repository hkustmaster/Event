package com.hkust.android.event.fragments;

import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.hkust.android.event.R;
import com.hkust.android.event.model.Constants;


public class PendingFragment extends NotesListFragment {

    public static PendingFragment newInstance() {
        return new PendingFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_my_event;
    }

    @Override
    protected int getNumColumns() {
        return 1;
    }

    @Override
    protected String getTagName() {
        return Constants.PENDING_FRAGMENT;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
