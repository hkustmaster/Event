package com.hkust.android.event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hkust.android.event.adapters.MessageAdapter;
import com.hkust.android.event.adapters.ParticipantAdapter;

public class DetailParticipantListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_participant_list);

        RecyclerView messageList = (RecyclerView)findViewById(R.id.participants_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        messageList.setLayoutManager(layoutManager);

        ParticipantAdapter participantAdapter = new ParticipantAdapter();

        messageList.setAdapter(participantAdapter);
    }
}
