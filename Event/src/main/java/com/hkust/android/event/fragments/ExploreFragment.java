package com.hkust.android.event.fragments;

import com.hkust.android.event.R;
import com.hkust.android.event.activity.Constants;


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
        return 5;
    }

    @Override
    protected String getTagName() {
        return Constants.EXPLORE_FRAGMENT;
    }
}
