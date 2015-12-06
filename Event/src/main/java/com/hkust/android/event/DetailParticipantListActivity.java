package com.hkust.android.event;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkust.android.event.adapters.MessageAdapter;
import com.hkust.android.event.adapters.ParticipantAdapter;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Event;
import com.hkust.android.event.model.Participant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DetailParticipantListActivity extends AppCompatActivity {

    private ArrayList<Participant> participants = new ArrayList<Participant>();
    private AsyncHttpClient client = new AsyncHttpClient();
    private SharedPreferences sp;
    private ParticipantAdapter participantAdapter = new ParticipantAdapter(participants);
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_participant_list);
        setTitle("Participants");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        //set recycler view
        RecyclerView messageList = (RecyclerView) findViewById(R.id.participants_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messageList.setLayoutManager(layoutManager);

        //get event id from the detail activity
        String eventId = new String();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                 eventId = null;
            } else {
                eventId = extras.getString("eventId");
            }
        } else {
            eventId = (String) savedInstanceState.getSerializable("eventId");
        }
        messageList.setAdapter(participantAdapter);
        getParticipantFromServer(eventId);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }


    private void getParticipantFromServer(String eventId){
        RequestParams params = new RequestParams();
        params.put("id",eventId);
        params.put("token",sp.getString("token", ""));
        client.post(Constants.SERVER_URL + Constants.EVENT_DETAIL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("succeed")) {
                        JSONObject event = new JSONObject(jsonObject.getString("act"));
                        String participantString = event.getString("participants");
                        Log.i("dddd",participantString);
                        participants = gson.fromJson(participantString, new TypeToken<ArrayList<Participant>>(){}.getType());
                        participantAdapter.setParticipantsList(participants);
                        participantAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "No Connection..", Toast.LENGTH_LONG).show();
            }
        });

    }


}
