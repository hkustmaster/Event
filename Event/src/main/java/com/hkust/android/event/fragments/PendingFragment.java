package com.hkust.android.event.fragments;

import com.hkust.android.event.R;
import com.hkust.android.event.activity.Constants;


public class PendingFragment extends NotesListFragment {

    public static PendingFragment newInstance() {
        return new PendingFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_favorites;
    }

    @Override
    protected int getNumColumns() {
        return 1;
    }

    @Override
    protected int getNumItems() {
        return 1;
    }

    @Override
    protected String getTagName() {
        return Constants.PENDING_FRAGMENT;
    }
}
