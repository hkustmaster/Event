package com.hkust.android.event.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.hkust.android.event.EventDetailActivity;
import com.hkust.android.event.R;
import com.hkust.android.event.adapters.NotesAdapter;

/**
 * Created by Gordon Wong on 7/18/2015.
 *
 * Generic fragment displaying a list of notes.
 */
public abstract class NotesListFragment extends Fragment implements NotesAdapter.ClickListener{

	@LayoutRes
	protected abstract int getLayoutResId();

	protected abstract int getNumColumns();

	protected abstract int getNumItems();

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutResId(), container, false);

		// Setup list
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notes_list);
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(getNumColumns(),
                StaggeredGridLayoutManager.VERTICAL));

        NotesAdapter notesAdapter = new NotesAdapter(getActivity(), getNumItems());
		notesAdapter.setClickListener(this);

        recyclerView.setAdapter(notesAdapter);

		return view;
	}


    @Override
    public void itemClicked(View view, int position) {
        startActivity(new Intent(getActivity(), EventDetailActivity.class));
        Integer pos = new Integer(position);

        Log.i("position=========", pos.toString());
    }
}
