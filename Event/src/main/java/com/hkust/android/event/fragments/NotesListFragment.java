package com.hkust.android.event.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkust.android.event.PendingEventDetailActivity;
import com.hkust.android.event.ExploreEventDetailActivity;
import com.hkust.android.event.MyEventDetailActivity;
import com.hkust.android.event.R;
import com.hkust.android.event.adapters.NotesAdapter;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Event;
import com.hkust.android.event.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public abstract class NotesListFragment extends Fragment implements NotesAdapter.ClickListener, SwipeRefreshLayout.OnRefreshListener {

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract int getNumColumns();

    protected abstract int getNumItems();

    protected abstract String getTagName();

    private SwipeRefreshLayout refreshLayout;

    private SharedPreferences sp;

    private NotesAdapter notesAdapter;

    private ArrayList<Event> exploreEvents = new ArrayList<Event>();
    private ArrayList<Event> myEvents = new ArrayList<Event>();
    private ArrayList<Event> pendingEvents = new ArrayList<Event>();
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

        notesAdapter = new NotesAdapter(getActivity(), new ArrayList<Event>());
        notesAdapter.setClickListener(this);
        recyclerView.setAdapter(notesAdapter);


        AsyncHttpClient client = new AsyncHttpClient();
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        RequestParams params = new RequestParams();
        params.put("token", token);
        client.post(Constants.SERVER_URL + Constants.GET_ALL_EVENT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if (message.equalsIgnoreCase("succeed")) {
                        String eventString = jsonObject.getString("act");
                        Gson gson = new Gson();
                       // Log.i("ppppp", eventString);
                        ArrayList<Event> arrayEventList = gson.fromJson(eventString, new TypeToken<ArrayList<Event>>() {
                        }.getType());
                        //Log.i("ppppp", arrayEventList.get(0).getTitle());
                        exploreEvents = arrayEventList;
                        notesAdapter.setEventsList(arrayEventList);
                        notesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return view;
    }


    @Override
    public void itemClicked(View view, int position) {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userString=sp.getString("userString", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userString, User.class);

        if (getTagName().equalsIgnoreCase(Constants.MYEVENT_FRAGMENT)) {
            Intent intent = new Intent(getActivity(), MyEventDetailActivity.class);
            intent.setAction(getTagName());
            intent.putExtra("eventId", exploreEvents.get(position).get_id());
            startActivity(intent);

        } else if (getTagName().equalsIgnoreCase(Constants.EXPLORE_FRAGMENT)) {
            if(user.get_id().equalsIgnoreCase(exploreEvents.get(position).getHost().get_id())){
                Intent intent = new Intent(getActivity(), MyEventDetailActivity.class);
                intent.putExtra("eventId",exploreEvents.get(position).get_id());
                intent.setAction(getTagName());
                startActivity(intent);

            }else{
                Intent intent = new Intent(getActivity(), ExploreEventDetailActivity.class);
                intent.setAction(getTagName());
                intent.putExtra("eventId",exploreEvents.get(position).get_id());
                startActivity(intent);
            }
        } else if (getTagName().equalsIgnoreCase(Constants.PENDING_FRAGMENT)) {
            Intent intent = new Intent(getActivity(), PendingEventDetailActivity.class);
            intent.setAction(getTagName());
            intent.putExtra("eventId",exploreEvents.get(position).get_id());
            startActivity(intent);
        }
        Integer pos = new Integer(position);
        //action when a item clicked
        Log.i("position=========", pos.toString());
    }

    @Override
    public void onRefresh() {
        if (refreshLayout.isRefreshing()) {
            Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
            AsyncHttpClient client = new AsyncHttpClient();
            sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String token = sp.getString("token", "");
            RequestParams params = new RequestParams();
            params.put("token", token);
            client.post(Constants.SERVER_URL + Constants.GET_ALL_EVENT, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");

                        if (message.equalsIgnoreCase("succeed")) {
                            String eventString = jsonObject.getString("act");
                            Gson gson = new Gson();
                            // Log.i("ppppp", eventString);
                            ArrayList<Event> arrayEventList = gson.fromJson(eventString, new TypeToken<ArrayList<Event>>() {
                            }.getType());
                            //Log.i("ppppp", arrayEventList.get(0).getTitle());
                            exploreEvents = arrayEventList;
                            notesAdapter.setEventsList(arrayEventList);
                            notesAdapter.notifyDataSetChanged();
                            refreshLayout.setRefreshing(false);
                        } else {
                            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                }
            });


            refreshLayout.setRefreshing(false);
        }
    }

}
