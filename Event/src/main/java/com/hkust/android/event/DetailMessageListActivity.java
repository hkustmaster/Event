package com.hkust.android.event;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkust.android.event.adapters.DateListAdapter;
import com.hkust.android.event.adapters.MessageAdapter;
import com.hkust.android.event.model.Constants;
import com.hkust.android.event.model.Event;
import com.hkust.android.event.model.Message;
import com.hkust.android.event.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DetailMessageListActivity extends AppCompatActivity implements View.OnClickListener {

    private AsyncHttpClient client = new AsyncHttpClient();
    Gson gson = new Gson();
    SharedPreferences sp;
    String event_id = new String();
    User user = new User();
    ArrayList<Message> messages = new ArrayList<Message>();
    MessageAdapter messageAdapter = new MessageAdapter(messages);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);
        setTitle("Message");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //show the event pass from the main activity
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                event_id = null;
            } else {
                event_id = extras.getString("event_id");
            }
        } else {
            event_id = (String) savedInstanceState.getSerializable("event_id");
        }

        RecyclerView messageList = (RecyclerView) findViewById(R.id.message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messageList.setLayoutManager(layoutManager);

        messageList.setAdapter(messageAdapter);
        Button leaveMessageBtn = (Button) findViewById(R.id.leave_message_btn);
        leaveMessageBtn.setOnClickListener(this);

        getMessageList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leave_message_btn:
                // get prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View promptView = layoutInflater.inflate(R.layout.layout_prompt_message_input, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set prompts.xml to be the layout file of the alertdialog builder
                alertDialogBuilder.setView(promptView);
                final EditText input = (EditText) promptView.findViewById(R.id.userInput);

                // setup a dialog window
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                postMessage(input.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();
                break;
            default:
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

    private void postMessage(String message) {
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("id", event_id);
        params.put("content", message);

        client.post(Constants.SERVER_URL + Constants.POST_MESSAGE, params, new AsyncHttpResponseHandler() {
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
        getMessageList();
    }


    private void getMessageList(){
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("id", event_id);
        client.post(Constants.SERVER_URL + Constants.VIEW_MESSAGE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equalsIgnoreCase("succeed")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        String messagesString = jsonObject.getString("comments");
                        messages = gson.fromJson(messagesString, new TypeToken<ArrayList<Message>>() {}.getType());
                        messageAdapter.setMessages(messages);
                        messageAdapter.notifyDataSetChanged();
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
}
