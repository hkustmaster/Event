package com.hkust.android.event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkust.android.event.adapters.DateListAdapter;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Event;
import com.hkust.android.event.model.Participant;
import com.hkust.android.event.model.User;
import com.hkust.android.event.model.VoteRecord;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class DateVotingActivity extends AppCompatActivity {
    Gson gson = new Gson();
    SharedPreferences sp;
    Event event = new Event();
    User user = new User();
    AsyncHttpClient client = new AsyncHttpClient();
    ArrayList<VoteRecord> voteRecords = new ArrayList<VoteRecord>();
    ArrayList<Boolean> oldVoteRecords = new ArrayList<Boolean>();
    DateListAdapter dateListAdapter = new DateListAdapter();
    private String isHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_voting);
        setTitle("Vote For Available Dates");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);


        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String eventId = new String();
        //show the event pass from the main activity
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                isHost=null;
                eventId = null;
            } else {
                eventId = extras.getString("eventId");
                isHost = extras.getString("isHost");
            }
        } else {
            eventId = (String) savedInstanceState.getSerializable("eventId");
            isHost = (String) savedInstanceState.getSerializable("isHost");
        }

        getParticipantListFromServer(eventId);

        //set the vote result to the adapter and display.
        final RecyclerView dateList = (RecyclerView) findViewById(R.id.date_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dateList.setLayoutManager(layoutManager);

        dateList.setAdapter(dateListAdapter);

        Button voteBtn = (Button) findViewById(R.id.vote_btn);

        if(isHost.equalsIgnoreCase("true")){
            voteBtn.setVisibility(View.GONE);
        }else {
            voteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //post the vote result to server;
                    //update the participant;
                    updateVoteRecord(dateListAdapter.getVoteRecords());
                }
            });
        }



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
//count vote record from server
    public void getParticipantListFromServer(final String eventId) {
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("id", eventId);

        client.post(Constants.SERVER_URL + Constants.EVENT_DETAIL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("succeed")) {
                        JSONObject eventJson = new JSONObject(jsonObject.getString("act"));
                        String participantsStirng = eventJson.getString("participants");
                        eventJson.remove("participants");
                        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        String userString = sp.getString("userString", "");
                        user = gson.fromJson(userString, User.class);
                        event = gson.fromJson(eventJson.toString(), Event.class);
                        ArrayList<Participant> participants = gson.fromJson(participantsStirng, new TypeToken<ArrayList<Participant>>() {
                        }.getType());
                        voteRecords = getVoteRecords(participants, event.getStartAt(), event.getEndAt());
                        setOldVoteRecords(voteRecords);
                        dateListAdapter.setVoteRecords(voteRecords);
                        if(isHost.equalsIgnoreCase("true")){
                            dateListAdapter.setCanVote(false);
                        }else{
                            dateListAdapter.setCanVote(true);
                        }
                        dateListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
            }
        });
    }


    public ArrayList<VoteRecord> getVoteRecords(ArrayList<Participant> participants, String startDate, String endDate) {
        Log.i("ppppp ", startDate + " " + endDate);
        ArrayList<VoteRecord> voteRecords = new ArrayList<VoteRecord>();
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime startDateTime = DateTime.parse(startDate, format);
        DateTime endDateTime = DateTime.parse(endDate, format);
        //get vote dates
        while (startDateTime.isBefore(endDateTime.plusDays(1))) {
            VoteRecord voteRecord = new VoteRecord();
            voteRecord.setDate(startDateTime.toString(format));
            voteRecords.add(voteRecord);
            startDateTime = startDateTime.plusDays(1);

        }

        //counting vote result
        for (Participant p : participants) {
            for (VoteRecord v : voteRecords)
                if (voteForThisDate(p.getAvaildableAt(), v.getDate())) {
                    //if this participant is current user, isvote equal true and vote count +1
                    if (p.getId().get_id().equalsIgnoreCase(user.get_id())) {
                        v.setIsVoted(true);
                        v.setVoteCount(v.getVoteCount() + 1);
                    } else {
                        //only set vote count
                        v.setIsVoted(false);
                        v.setVoteCount(v.getVoteCount() + 1);
                    }

                }
        }
        return voteRecords;
    }

    public boolean voteForThisDate(String availableAt, String date) {
        ArrayList<String> availableDateList = getAvailableDateList(availableAt);
        if (availableDateList.contains(date))
            return true;
        else
            return false;
    }

    public ArrayList<String> getAvailableDateList(String availableAt) {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        String[] dateArr = availableAt.split(";");
        ArrayList<String> dateList = new ArrayList<String>();

        for (String d : dateArr) {
            if ("".equalsIgnoreCase(d)) {
                //do not have any vote record
            } else {
                DateTime date = DateTime.parse(d, format);
                dateList.add(date.toString(format));
            }
        }
        return dateList;
    }

    public void updateVoteRecord(ArrayList<VoteRecord> voteRecords) {
        //update local
        updateLocalDateList(voteRecords);

        //update webserver
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("actid", event.getId());
        params.put("vote", transformToString(voteRecords));

        client.post(Constants.SERVER_URL + Constants.VOTE_EVENT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("succeed")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Connection failed.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setOldVoteRecords(ArrayList<VoteRecord> voteRecords){
        for(VoteRecord v: voteRecords){
            if(v.isVoted()){
                oldVoteRecords.add(Boolean.TRUE);
            }else{
                oldVoteRecords.add(Boolean.FALSE);
            }
        }
    }

    public String transformToString(ArrayList<VoteRecord> voteRecords) {
        StringBuffer buff = new StringBuffer();
        for (VoteRecord v : voteRecords) {
            if (v.isVoted()) {
                buff.append(v.getDate() + ";");
            }
        }
        return buff.toString();
    }

    public void updateLocalDateList(ArrayList<VoteRecord> voteRecords){
        for(int i = 0;i<voteRecords.size();i++){
            if(voteRecords.get(i).isVoted()){
                if(oldVoteRecords.get(i)){
                    //do nothing
                }else {
                    voteRecords.get(i).setVoteCount(voteRecords.get(i).getVoteCount()+1);
                    oldVoteRecords.set(i,Boolean.TRUE);
                }
            }else{
                if(oldVoteRecords.get(i)){
                    voteRecords.get(i).setVoteCount(voteRecords.get(i).getVoteCount()-1);
                    oldVoteRecords.set(i,Boolean.FALSE);
                }else {
                    //do nothing
                }
            }
        }
        dateListAdapter.setVoteRecords(voteRecords);
        dateListAdapter.notifyDataSetChanged();
    }
}
