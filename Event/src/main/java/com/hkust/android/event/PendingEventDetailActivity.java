package com.hkust.android.event;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hkust.android.event.model.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PendingEventDetailActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_event_detail);
        setTitle("Pending Event Detail");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout viewParticipantBtn = (LinearLayout) findViewById(R.id.view_participant_layout_btn);
        viewParticipantBtn.setOnClickListener(this);

        LinearLayout viewMessageBtn = (LinearLayout)findViewById(R.id.view_message_layout_btn);
        viewMessageBtn.setOnClickListener(this);

        LinearLayout dateLayout = (LinearLayout)findViewById(R.id.date_layout);
        dateLayout.setOnClickListener(this);

        Button quitBtn = (Button)findViewById(R.id.leave_btn);
        quitBtn.setOnClickListener(this);

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

            case R.id.leave_btn:
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();

                //put token and id here
                client.post(Constants.SERVER_URL + Constants.EVENT_SHOWMINE, params, new AsyncHttpResponseHandler() {
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

                    }
                });
            default:
                break;
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
