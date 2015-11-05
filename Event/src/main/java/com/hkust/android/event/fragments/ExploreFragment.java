package com.hkust.android.event.fragments;

import com.hkust.android.event.R;
/**
 * Created by Gordon Wong on 7/17/2015.
 *
 * All items fragment.
 */
public class ExploreFragment extends NotesListFragment {

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_all;
    }

    @Override
    protected int getNumColumns() {
        return 1;
    }

    @Override
    protected int getNumItems() {
        return 20;
    }
}
