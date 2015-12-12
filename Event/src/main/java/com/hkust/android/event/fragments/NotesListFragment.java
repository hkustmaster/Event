package com.hkust.android.event.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkust.android.event.PendingEventDetailActivity;
import com.hkust.android.event.ExploreEventDetailActivity;
import com.hkust.android.event.MyEventDetailActivity;
import com.hkust.android.event.R;
import com.hkust.android.event.adapters.NotesAdapter;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Event;
import com.hkust.android.event.model.Location;
import com.hkust.android.event.model.ParticipantsForAllEvent;
import com.hkust.android.event.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class NotesListFragment extends Fragment implements NotesAdapter.ClickListener, SwipeRefreshLayout.OnRefreshListener,

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "NotesListFragment";
    private LocationRequest mLocationRequest;
    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract int getNumColumns();

    protected abstract String getTagName();


    private SwipeRefreshLayout refreshLayout;
    private SharedPreferences sp;
    private NotesAdapter notesAdapter;
    private Gson gson = new Gson();
    private AsyncHttpClient client = new AsyncHttpClient();
    private ArrayList<Event> exploreEvents = new ArrayList<Event>();
    private ArrayList<Event> myEvents = new ArrayList<Event>();
    private ArrayList<Event> pendingEvents = new ArrayList<Event>();
    private User user;
    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;

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

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        //if current fragment is explore fragment
        if (getTagName().equalsIgnoreCase(Constants.EXPLORE_FRAGMENT)) {
            if (mLastLocation == null) {
                Log.i(TAG, "null lastLocation");
                getExploreEvent();
            } else {
                Log.i(TAG, "have lastLocation");
                getNearbyExploreEvent();
            }

        } else {
            //if current fragment is myevent or pending event
            getMyEventAndPendingEvent();
        }

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return view;
    }



    @Override
    public void itemClicked(View view, int position) {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userString = sp.getString("userString", "");
        user = gson.fromJson(userString, User.class);

        if (getTagName().equalsIgnoreCase(Constants.MYEVENT_FRAGMENT)) {
            //from my event fragment
            Intent intent = new Intent(getActivity(), MyEventDetailActivity.class);
            intent.setAction(getTagName());
            String eventJson = gson.toJson(myEvents.get(position));
            intent.putExtra("eventString", eventJson);
            startActivity(intent);
        } else if (getTagName().equalsIgnoreCase(Constants.EXPLORE_FRAGMENT)) {

            if (user.get_id().equalsIgnoreCase(exploreEvents.get(position).getHost().get_id())) {
                //from explore fragment if ishost go to my event fragment
                Intent intent = new Intent(getActivity(), MyEventDetailActivity.class);
                String eventJson = gson.toJson(exploreEvents.get(position));
                intent.putExtra("eventString", eventJson);
                intent.setAction(getTagName());
                startActivity(intent);

            } else {
                //if is participant go to pending event detail
                if (isParticipant(exploreEvents.get(position))) {
                    Intent intent = new Intent(getActivity(), PendingEventDetailActivity.class);
                    intent.setAction(getTagName());
                    String eventJson = gson.toJson(exploreEvents.get(position));
                    intent.putExtra("eventString", eventJson);
                    startActivity(intent);
                } else {
                    //else go to explore event detail
                    Intent intent = new Intent(getActivity(), ExploreEventDetailActivity.class);
                    intent.setAction(getTagName());
                    String eventJson = gson.toJson(exploreEvents.get(position));
                    intent.putExtra("eventString", eventJson);
                    startActivity(intent);
                }
            }
        } else if (getTagName().equalsIgnoreCase(Constants.PENDING_FRAGMENT)) {
            //if from pending fragment, go to pending event detail
            Intent intent = new Intent(getActivity(), PendingEventDetailActivity.class);
            intent.setAction(getTagName());
            String eventJson = gson.toJson(pendingEvents.get(position));
            intent.putExtra("eventString", eventJson);
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        if (refreshLayout.isRefreshing()) {
            if (getTagName().equalsIgnoreCase(Constants.EXPLORE_FRAGMENT)) {
                if (mLastLocation == null) {
                    Log.i(TAG, "null lastLocation");
                    getExploreEvent();
                } else {
                    Log.i(TAG, "have lastLocation");
                    getNearbyExploreEvent();
                }

            } else {
                getMyEventAndPendingEvent();
            }
            refreshLayout.setRefreshing(false);
        }
    }

    private boolean isParticipant(Event event) {
        for (ParticipantsForAllEvent p : event.getParticipants()) {
            if (p.getId().equalsIgnoreCase(user.get_id())) {
                return true;
            }
        }
        return false;
    }


    private void getExploreEvent() {
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
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMyEventAndPendingEvent() {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        String userString = sp.getString("userString", "");
        Log.i("pppp", userString);
        user = gson.fromJson(userString, User.class);
        RequestParams params = new RequestParams();
        params.put("token", token);
        client.post(Constants.SERVER_URL + Constants.EVENT_SHOWMINE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if (message.equalsIgnoreCase("succeed")) {
                        String eventString = jsonObject.getString("act");
                        ArrayList<Event> arrayEventList = gson.fromJson(eventString, new TypeToken<ArrayList<Event>>() {
                        }.getType());

                        myEvents.clear();
                        pendingEvents.clear();
                        for (Event e : arrayEventList) {
                            if (e.getHost().get_id().equalsIgnoreCase(user.get_id())) {
                                myEvents.add(e);
                            } else {
                                pendingEvents.add(e);
                            }
                        }
                        if (getTagName().equalsIgnoreCase(Constants.MYEVENT_FRAGMENT)) {
                            notesAdapter.setEventsList(myEvents);
                            notesAdapter.notifyDataSetChanged();
                        } else if (getTagName().equalsIgnoreCase(Constants.PENDING_FRAGMENT)) {
                            notesAdapter.setEventsList(pendingEvents);
                            notesAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean doesUserHavePermission() {
        int result = getContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

    }

    private void getNearbyExploreEvent() {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        RequestParams params = new RequestParams();
        params.put("token", token);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        double[] lnglat = {mLastLocation.getLongitude(), mLastLocation.getLatitude()};
        try {
            params.put("location", getLocationCor(lnglat).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.post(Constants.SERVER_URL + Constants.GET_NEAR_EVENT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if (message.equalsIgnoreCase("succeed")) {
                        String eventString = jsonObject.getString("act");
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
                Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private JSONArray getLocationCor(double[] cor) throws JSONException {
        JSONArray ja = new JSONArray();
        for (double d : cor) {
            ja.put(d);
        }
        return ja;
    }

}
