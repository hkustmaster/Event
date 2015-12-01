package com.hkust.android.event.fragments;

import com.hkust.android.event.R;
import com.hkust.android.event.model.Constants;


public class MyEventFragment extends NotesListFragment {

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
    protected int getNumItems() {
        return 1;
    }

    @Override
    protected String getTagName() {
        return Constants.MYEVENT_FRAGMENT;
    }
}
