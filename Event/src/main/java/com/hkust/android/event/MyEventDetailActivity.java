package com.hkust.android.event;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Event;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyEventDetailActivity extends AppCompatActivity implements View.OnClickListener{

    String eventId;
    private SharedPreferences sp;
    private TextView event_title;
    private TextView event_holder;
    private TextView event_date;
    private TextView event_time;
    private TextView event_location;
    private TextView event_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_detail);
        setTitle("My Event Detail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout viewParticipantBtn = (LinearLayout) findViewById(R.id.view_participant_layout_btn);
        viewParticipantBtn.setOnClickListener(this);

        LinearLayout viewMessageBtn = (LinearLayout)findViewById(R.id.view_message_layout_btn);
        viewMessageBtn.setOnClickListener(this);

        LinearLayout dateLayout = (LinearLayout)findViewById(R.id.date_layout);
        dateLayout.setOnClickListener(this);

        Button closeDiscussionBtn = (Button)findViewById(R.id.close_discussion_btn);
        closeDiscussionBtn.setOnClickListener(this);

        event_title = (TextView) findViewById(R.id.event_title_textView);
        event_holder = (TextView) findViewById(R.id.event_holder_textView);
        event_date = (TextView) findViewById(R.id.date_textView);
        event_time = (TextView) findViewById(R.id.time_textView);
        event_location = (TextView) findViewById(R.id.location_textView);
        event_desc = (TextView) findViewById(R.id.event_discription_textView);


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


        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        AsyncHttpClient client = new AsyncHttpClient();
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
                        String eventString = jsonObject.getString("act");
                        Gson gson = new Gson();
                        Event event = gson.fromJson(eventString, Event.class);

                        event_title.setText(event.getTitle());
                        event_holder.setText(event.getHost().getName());
                        event_time.setText(event.getTime());
                        event_location.setText(event.getLocation().get(0));
                        event_desc.setText(event.getDescription());
                        event_date.setText(event.getStartAt()+" "+event.getEndAt());

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_participant_layout_btn:
                Intent intent = new Intent(getApplicationContext(), DetailParticipantListActivity.class);
                startActivityForResult(intent, 100);
                break;
            case  R.id.view_message_layout_btn:
                Intent intent2 = new Intent(getApplicationContext(), DetailMessageListActivity.class);
                startActivityForResult(intent2, 100);
                break;
            case R.id.date_layout:
                Intent intent3 = new Intent(getApplicationContext(), DateVotingActivity.class);
                startActivityForResult(intent3, 100);
                break;
            case R.id.close_discussion_btn:
                Intent intent4 = new Intent(getApplicationContext(), CloseDiscussionActivity.class);
                startActivityForResult(intent4, 100);
                break;
            default:
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }
}
