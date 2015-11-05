package com.hkust.android.event.fragments;

import com.hkust.android.event.R;

/**
 * Created by Gordon Wong on 7/17/2015.
 *
 * Shared items fragment.
 */
public class MyEventFragment extends NotesListFragment {

    public static MyEventFragment newInstance() {
        return new MyEventFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_shared;
    }

    @Override
    protected int getNumColumns() {
        return 1;
    }

    @Override
    protected int getNumItems() {
        return 10;
    }
}
