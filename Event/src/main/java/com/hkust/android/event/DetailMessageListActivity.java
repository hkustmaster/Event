package com.hkust.android.event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hkust.android.event.adapters.DateListAdapter;
import com.hkust.android.event.adapters.MessageAdapter;

public class DetailMessageListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);

        RecyclerView messageList = (RecyclerView)findViewById(R.id.message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        messageList.setLayoutManager(layoutManager);

        MessageAdapter messageAdapter = new MessageAdapter();

        messageList.setAdapter(messageAdapter);
    }
}
