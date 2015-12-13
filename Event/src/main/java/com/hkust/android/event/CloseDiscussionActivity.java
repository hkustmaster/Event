package com.hkust.android.event;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Event;
import com.hkust.android.event.model.Participant;
import com.hkust.android.event.model.VoteRecord;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CloseDiscussionActivity extends AppCompatActivity {
    AsyncHttpClient client = new AsyncHttpClient();
    Gson gson = new Gson();
    private SharedPreferences sp;
    Event event = new Event();
    ArrayList<VoteRecord> voteRecords;
    ArrayList<Participant> participants;
    Context context;
    RadioGroup radioGroup;
    ArrayList<String> radioButtonsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_discussion);
        setTitle("Close Discussion");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        //get event id from the my event activity
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

        setRadioButton(eventId);

        Button confirmBtn = (Button) findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("pppp radio btn id",""+radioGroup.getCheckedRadioButtonId());
                updateEventProfile(radioButtonsText.get(radioGroup.getCheckedRadioButtonId()));
                finish();
            }
        });
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


    public void setRadioButton(String eventId) {
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
                        event = gson.fromJson(eventJson.toString(), Event.class);
                        ArrayList<Participant> participants = gson.fromJson(participantsStirng, new TypeToken<ArrayList<Participant>>() {
                        }.getType());
                        voteRecords = setVoteRecords(participants, event.getStartAt(), event.getEndAt());
                        radioGroup = (RadioGroup) findViewById(R.id.close_radio_group);
                        radioButtonsText = new ArrayList<String>();
                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.FILL_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        for (int i = 0; i < voteRecords.size(); i++) {

                            RadioButton tempButton = new RadioButton(context);
                            tempButton.setText("Vote Count: "+voteRecords.get(i).getVoteCount() + "  " + "Date: "+voteRecords.get(i).getDate());
                            tempButton.setId(i);
                            radioButtonsText.add(voteRecords.get(i).getDate());
                            radioGroup.addView(tempButton, p);
                        }

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


    public ArrayList<VoteRecord> setVoteRecords(ArrayList<Participant> participants, String startDate, String endDate) {
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
                    v.setVoteCount(v.getVoteCount() + 1);
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

    public void updateEventProfile(String startDate) {
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        event.setStartAt(startDate);
        event.setStatus(Constants.STATUS_EVENT_ING);
        event.setEndAt("");
        event.setToken(token);
        StringEntity entity = null;
        try {
            entity = new StringEntity(gson.toJson(event));
            Log.i("pppp json event",gson.toJson(event));
            client.post(this.getApplicationContext(),Constants.SERVER_URL + Constants.EDIT_EVENT, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
