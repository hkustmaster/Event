package com.hkust.android.event.fragments;

import com.hkust.android.event.R;

/**
 * Created by Gordon Wong on 7/17/2015.
 *
 * Favorite items fragment.
 */
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
        return 7;
    }
}
