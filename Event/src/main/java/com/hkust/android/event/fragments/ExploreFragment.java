package com.hkust.android.event.fragments;

import com.hkust.android.event.R;
import com.hkust.android.event.model.Constants;


public class ExploreFragment extends NotesListFragment {

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
}
