package com.hkust.android.event;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class DateVotingActivity extends AppCompatActivity {
    int year_x, month_x, day_x;
    static final int DATE_DIALOG_ID = 0;
    Gson gson = new Gson();
    SharedPreferences sp;
    Event event = new Event();
    User user = new User();
    AsyncHttpClient client = new AsyncHttpClient();
    ArrayList<VoteRecord> voteRecords = new ArrayList<VoteRecord>();
    DateListAdapter dateListAdapter = new DateListAdapter();

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
                eventId = null;
            } else {
                eventId = extras.getString("eventId");
            }
        } else {
            eventId = (String) savedInstanceState.getSerializable("eventId");
        }

        getParticipantListFromServer(eventId);

        //set the vote result to the adapter and display.
        final RecyclerView dateList = (RecyclerView) findViewById(R.id.date_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        dateList.setLayoutManager(layoutManager);

        dateList.setAdapter(dateListAdapter);


        Button voteBtn = (Button) findViewById(R.id.vote_btn);
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post the vote result to server;
                //update the participant;
                updateVoteRecord(dateListAdapter.getVoteRecords());
            }
        });

        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        Button suggestion = (Button) findViewById(R.id.suggestion_btn);
        suggestion.setVisibility(View.INVISIBLE);
        showDialogOnDateTextFieldClick();
    }

    public void showDialogOnDateTextFieldClick() {
        Button suggestionBtn = (Button) findViewById(R.id.suggestion_btn);
        suggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
    }

    public Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            //AutoCompleteTextView dateTextField = (AutoCompleteTextView) findViewById(R.id.new_event_date);
            //dateTextField.setText(year_x+"-"+month_x+"-"+day_x);
        }
    };

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
                        dateListAdapter.setVoteRecords(voteRecords);
                        dateListAdapter.setCanVote(true);
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
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("actid", event.getId());
        params.put("vote", transformToString(voteRecords));
        Log.i("pppp", params.toString());
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


    public String transformToString(ArrayList<VoteRecord> voteRecords) {
        StringBuffer buff = new StringBuffer();
        for (VoteRecord v : voteRecords) {
            if (v.isVoted()) {
                buff.append(v.getDate() + ";");
            }
        }
        return buff.toString();
    }
}
