package com.hkust.android.event;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

public class ExploreEventDetailActivity extends AppCompatActivity {

    String eventString;
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
        setContentView(R.layout.activity_explore_detail);
        setTitle("Event Explore Detail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event_title = (TextView) findViewById(R.id.event_title_textView);
        event_holder = (TextView) findViewById(R.id.event_holder_textView);
        event_date = (TextView) findViewById(R.id.date_textView);
        event_time = (TextView) findViewById(R.id.time_textView);
        event_location = (TextView) findViewById(R.id.location_textView);
        event_desc = (TextView) findViewById(R.id.event_discription_textView);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                eventString = null;
            } else {
                eventString = extras.getString("eventString");
            }
        } else {
            eventString = (String) savedInstanceState.getSerializable("eventString");
        }

        Gson gson = new Gson();
        Event event = gson.fromJson(eventString,Event.class);

        event_title.setText(event.getTitle());
        event_holder.setText(event.getHost().getName());
        event_time.setText(event.getTime());
        event_location.setText(event.getLocation().get(0));
        event_desc.setText(event.getDescription());
        event_date.setText(event.getStartAt()+" "+event.getEndAt());

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");

        Log.i("ppppp", event.getId());
        Log.i("ppppp", token);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("id", event.getId());
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


        //Toast.makeText(getApplicationContext(),eventId,Toast.LENGTH_LONG).show();
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


}
