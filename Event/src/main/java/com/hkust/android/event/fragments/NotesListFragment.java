package com.hkust.android.event.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hkust.android.event.PendingEventDetailActivity;
import com.hkust.android.event.ExploreEventDetailActivity;
import com.hkust.android.event.MyEventDetailActivity;
import com.hkust.android.event.R;
import com.hkust.android.event.adapters.NotesAdapter;
import com.hkust.android.event.model.Constants;


public abstract class NotesListFragment extends Fragment implements NotesAdapter.ClickListener, SwipeRefreshLayout.OnRefreshListener {

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract int getNumColumns();

    protected abstract int getNumItems();

    protected abstract String getTagName();

    private SwipeRefreshLayout refreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        // Setup list
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(getNumColumns(),
                StaggeredGridLayoutManager.VERTICAL));

        NotesAdapter notesAdapter = new NotesAdapter(getActivity(), getTagName(), getNumItems());
        notesAdapter.setClickListener(this);

        recyclerView.setAdapter(notesAdapter);

        return view;
    }


    @Override
    public void itemClicked(View view, int position) {
        if (getTagName().equalsIgnoreCase(Constants.MYEVENT_FRAGMENT)) {
            Intent intent = new Intent(getActivity(), MyEventDetailActivity.class);
            intent.setAction(getTagName());
            startActivity(intent);

        } else if (getTagName().equalsIgnoreCase(Constants.EXPLORE_FRAGMENT)) {
            Intent intent = new Intent(getActivity(), ExploreEventDetailActivity.class);
            intent.setAction(getTagName());
            startActivity(intent);
        } else if (getTagName().equalsIgnoreCase(Constants.PENDING_FRAGMENT)) {
            Intent intent = new Intent(getActivity(), PendingEventDetailActivity.class);
            intent.setAction(getTagName());
            startActivity(intent);
        }
        Integer pos = new Integer(position);
        //action when a item clicked
        Log.i("position=========", pos.toString());
    }

    @Override
    public void onRefresh() {
        if(refreshLayout.isRefreshing()){
            Toast.makeText(getActivity(),"refreshed", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }
}
